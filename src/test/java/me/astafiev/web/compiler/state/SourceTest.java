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

import junit.framework.TestCase;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class SourceTest extends TestCase {
	
	public SourceTest(String testName) {
		super(testName);
		String txt = "";
		for (int i=0; i< 13; i++) {
			txt += i + "\n";
		}
		instance13.setSource(txt);
	}
	Source instance13 = new Source("SomeClass");

	public void testSetSource() {
		System.out.println("setSource");
		String text = "";
		instance13.setSource(text);
	}

	public void testInsert() {
		System.out.println("insert");
		checkConsist(0, 10, 13);
		instance13.insert(5, "");
		checkConsist(0, 11, 14);
		instance13.insert(12, "");
		checkConsist(0, 11, 15);
	}
	
	void checkConsist(int s0, int s1, int e1) {
		assertEquals(s0, instance13.findLocation(s0).getIndex());
		assertEquals(s0, instance13.findLocation((s0+s1)/2).getIndex());
		assertEquals(s0, instance13.findLocation(s1-1).getIndex());
		assertEquals(s1, instance13.findLocation(s1).getIndex());
		assertEquals(s1, instance13.findLocation((s1+e1)/2).getIndex());
		assertEquals(s1, instance13.findLocation(e1).getIndex());
	}
/*
	public void testReplace() {
		System.out.println("replace");
		int index = 0;
		String line = "";
		String expResult = "";
		String result = instance13.replace(index, line);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	public void testRemove() {
		System.out.println("remove");
		int index = 0;
		String expResult = "";
		String result = instance13.remove(index);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}
*/

}
