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

import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class IO {

	public static String readSample() throws IOException {
		InputStreamReader isr = new InputStreamReader(IO.class.getResourceAsStream("Sample.java"));
		char[] buffer = new char[512];
		int cnt = 0;
		StringBuilder sb = new StringBuilder();
		while ((cnt = isr.read(buffer)) >= 0) {
			sb.append(buffer, 0, cnt);
		}
		isr.close();
		String source = sb.toString();
		return source;
	}

}
