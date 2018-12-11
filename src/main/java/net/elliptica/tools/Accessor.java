/*
 * Авторское право принадлежит ООО «Эллиптика» ѱ 2018.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Elliptica Ltd. All rights reserved.
 * 
 *  Частная лицензия Эллиптика
 * Данный программный код является собственностью ООО «Эллиптика»
 * и может быть использован только с его разрешения
 */

package net.elliptica.tools;

import java.lang.reflect.Field;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class Accessor {
	private final Object parent;
	private final Class parentType;

	public Accessor(Object parent) {
		this.parent = parent;
		this.parentType = parent.getClass();
	}

	public <T> T getField(String name, Class<T> type) {
		return getField(name);
	}

	public <T> T getField(String name) {
		try {
			Field f = parentType.getDeclaredField(name);
			f.setAccessible(true);
			return (T) f.get(parent);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			return null;
		}
	}
}
