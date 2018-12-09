/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package me.astafiev.web.compiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.source.util.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import static javax.tools.StandardLocation.SOURCE_PATH;
import javax.tools.ToolProvider;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.beans.CompletionType;
import me.astafiev.web.compiler.state.CompilationCtx;
import me.astafiev.web.compiler.state.CompileState;
import me.astafiev.web.compiler.state.Source;
import ру.астафьев.компилятор.ДиспетчерФайлов;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class JCompiler {
	private final CompilationCtx compileState;
	private static final Completion STOP_COMPLETIONS = new Completion();

	private static final Logger LOG = Logger.getLogger(JCompiler.class.getName());

	public JCompiler(CompilationCtx compileState) {
		this.compileState = compileState;
	}

	public Stream<Completion> suggestToken(Source source, final int row, final int column) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		ДиспетчерФайлов fm = new ДиспетчерФайлов(compileState, diagnostics);
		JavaFileObject jfo = (JavaFileObject) fm.getFileForInput(SOURCE_PATH, null, source.getName());
        CompilationTask task = compiler.getTask(null, fm, diagnostics, null, null, Collections.singletonList(jfo));

        // Here we switch to Sun-specific APIs
        JavacTask javacTask = (JavacTask) task;
        SourcePositions sourcePositions = Trees.instance(javacTask).getSourcePositions();
        final Iterable<? extends CompilationUnitTree> parseResult;
		{
	        Iterable<? extends CompilationUnitTree> parseResult_ = null;
			try {
				parseResult_ = javacTask.parse();
			} catch (IOException e) {
				e.printStackTrace();
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
					terminated = ( c == STOP_COMPLETIONS );
					return c;
				} catch (InterruptedException ex) {
					LOG.log(Level.SEVERE, null, ex);
					terminated = true;
					Thread.currentThread().interrupt();
					throw new IllegalStateException(ex);
				}
			}
		};

		BiFunction<CompilationUnitTree,Tree, Boolean> coverCheck = (cuTree,t) -> {
			try{
				LineMap lineMap = cuTree.getLineMap();
				if (cuTree.getSourceFile().getCharContent(true).length() < 1) {
					return false;
				}
				long curPos = lineMap.getPosition(row, column);
				long start	= sourcePositions.getStartPosition	(cuTree, t);
				if (start == Diagnostic.NOPOS || start > curPos) {
					return false;
				}
				long end	= sourcePositions.getEndPosition	(cuTree, t);
				if (end == Diagnostic.NOPOS || end < curPos) {
					return false;
				}
				return true;
			} catch (Exception ex) {
				LOG.log(Level.SEVERE, null, ex);
				return false;
			}
		};

		BiPredicate<CompilationUnitTree,Tree> tokenConsumer = (cuTree,t) -> {
			if (!coverCheck.apply(cuTree, t)){
				return false;
			}
			processToken(queue, t);
			return true;
		};
		
		compileState.getExecutor().submit(() -> {
			for (CompilationUnitTree cuTree : parseResult) {
				if (!coverCheck.apply(cuTree, cuTree)){
					continue;
				}
				try {
					Predicate<Tree> consumer = t -> tokenConsumer.test(cuTree, t);
					new TreeParser().scan(cuTree, consumer);
				} catch (Exception ex) {
					LOG.log(Level.SEVERE, null, ex);
					stop(queue);
				}
			}
			stop(queue);
		});

		Spliterator<Completion> spliterator = Spliterators.spliteratorUnknownSize(it, Spliterator.NONNULL | Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false).filter(c -> c != STOP_COMPLETIONS);
	}
	
	private void stop(BlockingQueue<Completion> queue) {
		try {
			queue.put(STOP_COMPLETIONS);
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE, null, ex);
			Thread.currentThread().interrupt();
		}
	}

	private static void processToken(BlockingQueue<Completion> queue, Tree tree) {
		try {
			queue.put(new Completion(CompletionType.MEMEBER, tree.toString()));
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE, null, ex);
			queue.add(STOP_COMPLETIONS);
			Thread.currentThread().interrupt();
		}
	}
	
	private class TreeParser extends CommonTreeScanner {
		@Override
		public Boolean visitCommonTree(Tree node, Predicate<Tree> consumer) {
			return consumer.test(node);
		}
	}

}
