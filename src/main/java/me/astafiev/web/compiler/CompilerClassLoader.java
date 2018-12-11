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

import me.astafiev.web.compiler.isol.CommonTreeScanner;
import me.astafiev.web.compiler.isol.IsolatedCompiler;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class CompilerClassLoader extends URLClassLoader {

	private final ClassLoader localLoader;

	public CompilerClassLoader(ClassLoader localLoader, URL[] urls) {
		super(urls, localLoader);
		this.localLoader = localLoader;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (intools(name)) {
			Class c = findClass(name);
			if (resolve) {
				resolveClass(c);
			}
			return c;
		}
		return super.loadClass(name, resolve);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}
	
	private boolean intools(String name) {
		if (name == null) {
			return false;
		}
		return name.startsWith("com.sun.")
			||name.startsWith("sun.tools.")
			||name.startsWith(IsolatedCompiler.class.getPackage().getName())
		;
	}

}
