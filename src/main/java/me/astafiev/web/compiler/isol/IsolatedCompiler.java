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
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTool;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import me.astafiev.web.compiler.IIsolatedCompiler;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.state.CompilationCtx;
import me.astafiev.web.compiler.state.Source;
import sun.misc.Launcher;
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
		final CompilationCtx compileState = compCtx;
		JavaCompiler compiler = new JavacTool();
//		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		ДиспетчерФайлов fm = new ДиспетчерФайлов(compileState, diagnostics);
		JavaFileObject jfo = (JavaFileObject) fm.getFileForInput(StandardLocation.SOURCE_PATH, null, source.getName());
		JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diagnostics, null, null, Collections.singletonList(jfo));

		// Here we switch to Sun-specific APIs
		JavacTask javacTask = (JavacTask) task;
		Trees trees = Trees.instance(javacTask);
		SourcePositions sourcePositions = trees.getSourcePositions();
		final Iterable<? extends CompilationUnitTree> parseResult;
		{
			Iterable<? extends CompilationUnitTree> parseResult_ = null;
			try {
				parseResult_ = javacTask.parse();
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, null, ex);
				parseResult_ = Collections.EMPTY_LIST;
			} finally {
				parseResult = parseResult_;
			}
		}
		BlockingQueue<Completion> queue = new LinkedBlockingQueue();
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
		BiFunction<CompilationUnitTree, Tree, Boolean> coverCheck = (com.sun.source.tree.CompilationUnitTree cuTree, com.sun.source.tree.Tree t) -> {
			try {
				LineMap lineMap = cuTree.getLineMap();
				if (cuTree.getSourceFile().getCharContent(true).length() < 1) {
					return false;
				}
				long curPos = lineMap.getPosition(row, column);
				long start = sourcePositions.getStartPosition(cuTree, t);
				if (start == Diagnostic.NOPOS || start > curPos) {
					return false;
				}
				long end = sourcePositions.getEndPosition(cuTree, t);
				if (end == Diagnostic.NOPOS || end < curPos) {
					return false;
				}
				return true;
			} catch (Exception ex) {
				LOG.log(Level.SEVERE, null, ex);
				return false;
			}
		};
		TriPredicate<CompilationUnitTree, Tree, TreePath> tokenConsumer = (CompilationUnitTree cuTree, Tree t, TreePath tp) -> {
			if (!coverCheck.apply(cuTree, t)) {
				return false;
			}
			switch (t.getKind()) {
				case IDENTIFIER:
					Stream<Completion> subres = completer.guessCompletionsFor(javacTask, tp);
					subres.filter((c) -> c.getValue().startsWith(prefix)).forEach((Completion c) -> supply(queue, c));
					break;
				default:
			}
			return true;
		};
		compileState.getExecutor().submit(() -> {
			for (CompilationUnitTree cuTree : parseResult) {
				if (!coverCheck.apply(cuTree, cuTree)) {
					continue;
				}
				try {
					BiPredicate<Tree, TreePath> consumer = (t, tp) -> tokenConsumer.test(cuTree, t, tp);
					new TreeParser().scan(cuTree, consumer);
				} catch (Exception ex) {
					LOG.log(Level.SEVERE, null, ex);
					stop(queue);
				}
			}
			stop(queue);
		});
		Spliterator<Completion> spliterator = Spliterators.spliteratorUnknownSize(it, Spliterator.NONNULL | Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false).filter((me.astafiev.web.compiler.beans.Completion c) -> c != STOP_COMPLETIONS);
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
