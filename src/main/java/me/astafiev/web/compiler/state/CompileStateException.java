/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */
package me.astafiev.web.compiler.state;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class CompileStateException extends Exception {

	/**
	 * Creates a new instance of <code>CompileStateException</code> without detail message.
	 */
	public CompileStateException() {
	}

	/**
	 * Constructs an instance of <code>CompileStateException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public CompileStateException(String msg) {
		super(msg);
	}
}
