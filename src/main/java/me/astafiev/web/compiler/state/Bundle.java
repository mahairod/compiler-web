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

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class Bundle {
	private int start;
	private final LinkedList<String> lines;

	public Bundle(int index) {
		this.start = index;
		lines = new LinkedList<>();
	}

	public int getIndex() {
		return start;
	}

	void increase() {
		start++;
	}

	void decrease() {
		start--;
	}

	public int size() {
		return lines.size();
	}

	public boolean add(String e) {
		return lines.add(e);
	}

	public boolean addAll(Collection<? extends String> c) {
		return lines.addAll(c);
	}

	public String get(int index) {
		return lines.get(index-start);
	}

	public void insert(int index, String element) {
		lines.add(index-start, element);
	}

	public String set(int index, String element) {
		return lines.set(index-start, element);
	}

	public String remove(int index) {
		return lines.remove(index - start);
	}
	
	Stream<String> toStream() {
		return lines.stream();
	}

	@Override
	public String toString() {
		return "Bundle{" + "start=" + start + ", lines=" + toStream().reduce("", (l,r)->l+"\n"+r) + '}';
	}
}
