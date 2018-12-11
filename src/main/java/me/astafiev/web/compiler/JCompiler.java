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

import com.sun.source.tree.Tree;
import com.sun.source.util.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.state.CompilationCtx;
import me.astafiev.web.compiler.state.Source;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class JCompiler {
	private final CompilationCtx compileState;

	private static final Logger LOG = Logger.getLogger(JCompiler.class.getName());

	public JCompiler(CompilationCtx compileState) {
		this.compileState = compileState;
	}

	public Stream<Completion> suggestToken(Source source, final int row, final int column, String prefix) {
		Thread thread = Thread.currentThread();
		ClassLoader oldCl = thread.getContextClassLoader();
		ClassLoader localCl = getClass().getClassLoader();
		try {
			Class<? extends URLClassLoader> cclClass
					= (Class<? extends URLClassLoader>) Class.forName("me.astafiev.web.compiler.CompilerClassLoader", true, localCl);
			ClassLoader toolCl = JavacTask.class.getClassLoader();
			URL[] urlsLocal = ((URLClassLoader)localCl).getURLs();
			URL[] urls = urlsLocal;

//			List<URL> lst = new ArrayList<>();
//			Collections.addAll(lst, urls);
//			Collections.addAll(lst, urlsLocal);
//			urls = lst.toArray(urls);
			URLClassLoader ccl = cclClass.getConstructor(ClassLoader.class, URL[].class).newInstance(localCl, urls);

			thread.setContextClassLoader(ccl);
			Object comp = ccl.loadClass(IsolatedCompiler.class.getCanonicalName()).newInstance();
			IIsolatedCompiler isoComp = (IIsolatedCompiler) comp;
			
			return isoComp.suggestToken(compileState, source, row, column, prefix);
//			return (Stream<Completion>) comp.getClass().getDeclaredMethods()[0].invoke(comp, compileState, source, row, column, prefix);
			
		} catch (ReflectiveOperationException | SecurityException ex) {
			LOG.log(Level.SEVERE, null, ex);
			return null;
		} finally {
			thread.setContextClassLoader(oldCl);
		}
	}

}
