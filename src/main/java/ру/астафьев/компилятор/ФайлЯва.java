/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package ру.астафьев.компилятор;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;
import me.astafiev.web.compiler.state.Source;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class ФайлЯва extends SimpleJavaFileObject {
	static final String DB_PREFIX = "db:///";
	protected final Source source;

	public ФайлЯва(String name, Source source, Kind kind) {
		super(URI.create(DB_PREFIX + name), kind);
		this.source = source;
	}

	public ФайлЯва(URI uri, Source source, Kind kind) {
		super(uri, kind);
		this.source = source;
	}

	@Override
	public String getName() {
		return super.getName() + Kind.SOURCE.extension;
	}

	@Override
	public boolean isNameCompatible(String simpleName, Kind kind) {
		String baseName = simpleName + kind.extension;
		return kind.equals(getKind()) && (
				getName().equals(baseName) ||
				getName().endsWith("/" + baseName)
			);
	}
}
