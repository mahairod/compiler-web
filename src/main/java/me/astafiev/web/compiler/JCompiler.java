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

import me.astafiev.web.compiler.isol.IsolatedCompiler;
import java.net.URL;
import java.net.URLClassLoader;
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
	private IIsolatedCompiler isoComp;

	private static final Logger LOG = Logger.getLogger(JCompiler.class.getName());

	public JCompiler(CompilationCtx compileState) {
		this.compileState = compileState;
	}

	public Stream<Completion> suggestToken(Source source, final int row, final int column, String prefix) {
		prefix = prefix == null ? "" : prefix;
		try {
			return getTools().suggestToken(compileState, source, row+1, column+1, prefix);
		} catch (ReflectiveOperationException | SecurityException ex) {
			LOG.log(Level.SEVERE, null, ex);
			return null;
		}
	}

	private IIsolatedCompiler getTools() throws ReflectiveOperationException {
		if (isoComp != null) {
			return isoComp;
		}
//		ClassLoader toolCl = JavacTask.class.getClassLoader();
		ClassLoader localCl = getClass().getClassLoader();
		URL[] urlsLocal = ((URLClassLoader)localCl).getURLs();
		URLClassLoader ccl = new CompilerClassLoader(localCl, urlsLocal);
		Object comp = ccl.loadClass(IsolatedCompiler.class.getCanonicalName()).newInstance();
		isoComp = (IIsolatedCompiler) comp;
		return isoComp;
	}

}
