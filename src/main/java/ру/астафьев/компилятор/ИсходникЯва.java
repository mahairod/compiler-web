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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.astafiev.web.compiler.state.Source;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class ИсходникЯва extends ФайлЯва {
	public ИсходникЯва(String name, Source source) {
		super(name, source, Kind.SOURCE);
	}

	@Override
	public long getLastModified() {
		return source.getDate().getTime();
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return source.getТекст();
	}

	@Override
	public InputStream openInputStream() throws IOException {
		return new ByteArrayInputStream(source.getТекст().getBytes());
	}
}
