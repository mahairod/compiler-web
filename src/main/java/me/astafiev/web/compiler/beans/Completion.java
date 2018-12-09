/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package me.astafiev.web.compiler.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
@XmlRootElement
public class Completion {
	private CompletionType type;
	private String value;

	public Completion() {
	}

	public Completion(CompletionType completionType, String value) {
		this.type = completionType;
		this.value = value;
	}

	public CompletionType getType() {
		return type;
	}

	public void setType(CompletionType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Completion{" + "type=" + type + ", value=" + value + '}';
	}
}
