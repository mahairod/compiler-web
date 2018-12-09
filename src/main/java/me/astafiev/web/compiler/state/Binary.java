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

import java.util.Date;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class Binary {
	private final Source source;
	private byte[] image;
	private Date date = new Date(0);

	public Binary(Source source) {
		this.source = source;
	}

	public Source getSource() {
		return source;
	}

	public Date getDate() {
		return date;
	}

	public void setImage(byte[] image) {
		this.image = image;
		date = new Date();
	}

}
