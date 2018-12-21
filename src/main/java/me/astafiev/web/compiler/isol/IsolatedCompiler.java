/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package me.astafiev.web.compiler.isol;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTool;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import me.astafiev.web.compiler.IIsolatedCompiler;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.state.CompilationCtx;
import me.astafiev.web.compiler.state.Source;
import ру.астафьев.компилятор.ДиспетчерФайлов;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class IsolatedCompiler implements IIsolatedCompiler {
	private static final Logger LOG = Logger.getLogger(IsolatedCompiler.class.getName());

	private static final Completion STOP_COMPLETIONS = new Completion();

	final Completer completer;

	public IsolatedCompiler() {
		completer = new Completer();
	}

	@Override
	public Stream<Completion> suggestToken(CompilationCtx compCtx, Source source, final int row, final int column, String prefix) throws ReflectiveOperationException {
		LOG.log(Level.INFO, "Position:{0}/{1}", new Object[]{column, row});
		final CompilationCtx compileState = compCtx;
		JavaCompiler compiler = new JavacTool();
//		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		ДиспетчерФайлов fm = new ДиспетчерФайлов(compileState, diagnostics);
		JavaFileObject jfo = (JavaFileObject) fm.getFileForInput(StandardLocation.SOURCE_PATH, null, source.getName());
		CompilationTask task = compiler.getTask(null, fm, diagnostics, null, null, Collections.singletonList(jfo));

		// Here we switch to Sun-specific APIs
		JavacTask javacTask = (JavacTask) task;
		Trees trees = Trees.instance(javacTask);
		SourcePositions sourcePositions = trees.getSourcePositions();
		final Iterable<? extends CompilationUnitTree> parseResult;
		{
			Iterable<? extends CompilationUnitTree> parseResult_ = null;
			try {
				parseResult_ = javacTask.parse();
				Iterable<? extends Element> roots = javacTask.analyze();
				roots.getClass();
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, null, ex);
				parseResult_ = Collections.EMPTY_LIST;
			} finally {
				parseResult = parseResult_;
			}
		}

		BlockingQueue<Completion> queue = new LinkedBlockingQueue();

		class TokenContext {
			public TokenContext(CompilationUnitTree curUnit) {
				this.curUnit = curUnit;
				this.currentPosition = curUnit.getLineMap().getPosition(row, column);
				String curLine = source.get(row-1);
				String head = curLine.substring(0, column-1);
				int tabsNum = head.replaceAll("[^\t]", "").length();
				codePosition = currentPosition + tabsNum * (4-1);
			}
			final CompilationUnitTree curUnit;
			final long currentPosition;
			final long codePosition;
		}

		TriPredicate<TokenContext, Tree, TreePath> tokenConsumer = (TokenContext ctx, Tree t, TreePath tp) -> {
			CompilationUnitTree cuTree = ctx.curUnit;
			try {
				if (cuTree.getSourceFile().getCharContent(true).length() < 1) {
					return false;
				}
				long start = sourcePositions.getStartPosition(cuTree, t);
				if (start == Diagnostic.NOPOS || start > ctx.codePosition) {
					return false;
				}
				long end = sourcePositions.getEndPosition(cuTree, t);
				if (end == Diagnostic.NOPOS || end < ctx.codePosition) {
					return false;
				}
			} catch (Exception ex) {
				LOG.log(Level.SEVERE, null, ex);
				throw new IllegalStateException(ex);
			}

			final Stream<Completion> subres;
			switch (t.getKind()) {
				case IDENTIFIER:
					subres = completer.guessCompletionsFor(null, prefix, javacTask, tp);
					break;
				case VARIABLE:{
					VariableTree vt = (VariableTree) t;
					subres = completer.composeName(vt.getType(), prefix, javacTask, tp);
				}; break;
				default:
					subres = null;
			}
			if (subres != null) {
				subres.filter((c) -> c.getValue().startsWith(prefix)).forEach((Completion c) -> supply(queue, c));
			}
			return true;
		};

		compileState.getExecutor().submit(() -> {
			for (CompilationUnitTree cuTree : parseResult) {
				try {
					new TreeParser().scan(cuTree, (t, tp) -> tokenConsumer.test(new TokenContext(cuTree), t, tp));
				} catch (Exception ex) {
					LOG.log(Level.SEVERE, null, ex);
					break;
				}
			}
			stop(queue);
		});

		Iterator<Completion> it = new Iterator<Completion>() {
			boolean terminated = false;

			@Override
			public boolean hasNext() {
				return !terminated;
			}

			@Override
			public Completion next() {
				try {
					Completion c = queue.take();
					terminated = (c == STOP_COMPLETIONS);
					return c;
				} catch (InterruptedException ex) {
					LOG.log(Level.SEVERE, null, ex);
					terminated = true;
					Thread.currentThread().interrupt();
					throw new IllegalStateException(ex);
				}
			}
		};
		Spliterator<Completion> spliterator = Spliterators.spliteratorUnknownSize(it, Spliterator.NONNULL | Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false).filter((Completion c) -> c != STOP_COMPLETIONS);
	}

	private static void stop(BlockingQueue<Completion> queue) {
		try {
			queue.put(STOP_COMPLETIONS);
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE, null, ex);
			Thread.currentThread().interrupt();
		}
	}
	
	private static void supply(BlockingQueue<Completion> queue, Completion c) {
		try {
			queue.put(c);
//			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE, null, ex);
			stop(queue);
			Thread.currentThread().interrupt();
		}
	}

	public interface TriPredicate<T, U, V> {
		boolean test(T t, U u, V v);
	}

	private static class TreeParser extends CommonTreeScanner {
		@Override
		public Boolean visitCommonTree(Tree node, BiPredicate<Tree,TreePath> consumer) {
			TreePath tp = getCurrentPath();
			return consumer.test(node, tp);
		}
	}

}
