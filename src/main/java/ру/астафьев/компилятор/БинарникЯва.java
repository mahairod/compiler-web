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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.astafiev.web.compiler.state.Binary;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class БинарникЯва extends ФайлЯва {
	private final Binary binary;

	public БинарникЯва(String name, Binary binary) {
		super(name, binary.getSource(), Kind.CLASS);
		this.binary = binary;
	}
	public БинарникЯва(ФайлЯва файлЯва) {
		super(файлЯва.toUri(), файлЯва.source, Kind.CLASS);
		this.binary = new Binary(source);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		class DBOutputStream extends ByteArrayOutputStream {
			@Override
			public void close() throws IOException {
				flush();
				super.close();
			}
			@Override
			public void flush() throws IOException {
				super.flush();
				binary.setImage(this.toByteArray());
			}
		}
		return new DBOutputStream();
	}

	@Override
	public long getLastModified() {
		return binary.getDate().getTime();
	}

}
