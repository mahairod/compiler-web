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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import me.astafiev.web.compiler.beans.Completion;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class CompileStateTest {
	
	public CompileStateTest() {
	}
	
	private static EJBContainer container;
	private CompileStateLocal instance;
	
	@BeforeClass
	public static void setUpClass() {
		container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
	}
	
	@AfterClass
	public static void tearDownClass() {
		container.close();
	}
	
	@Before
	public void setUp() throws NamingException {
		Object ejb = container.getContext().lookup("java:global/classes/CompileState");
		instance =  (CompileStateLocal)ejb;
	}

	@Test
	public void testGetSources() throws Exception {
		System.out.println("getSources");
		Map<String, Source> result = instance.getSources();
		assertNotNull(result);
	}

	@Test
	public void testGetExecutor() throws Exception {
		System.out.println("getExecutor");
		ExecutorService result = instance.getExecutor();
		assertNotNull(result);
	}

	@Test
	public void testSetBinary() throws Exception {
		System.out.println("setBinary");
		String name = "SomeBinary";
		Binary binary = new Binary(new Source(name));
		Binary result = instance.setBinary(name, binary);
		assertNull(result);
	}
	
	@Test
	public void testSaveSource() throws Exception {
		System.out.println("saveSource");
		String name = "Sample";
		boolean result = saveSource(name);
		assertTrue(result);
	}

	private boolean saveSource(String name) throws Exception {
		String source = IO.readSample();
		boolean result = instance.saveSource(name, source);
		return result;
	}

	@Test
	public void testGetCompletions() throws Exception {
		System.out.println("getCompletions");
		int row = 33;
		int column = 19;
		String name = "Sample";
		saveSource(name);
		Stream<Completion> result = instance.getCompletions(name, "", row, column);
		assertNotNull(result);
		List<Completion> rl = result.collect(Collectors.toList());
		assertTrue(rl.size()> 0);
	}

	@Test
	public void testUpdateRow() throws Exception {
		System.out.println("updateRow");
		String name = "Sample";
		saveSource(name);
		int row = 0;
		String value = "";
		instance.updateRow(name, row, value);
	}

	@Test
	public void testInsertRow() throws Exception {
		System.out.println("insertRow");
		String name = "Sample";
		saveSource(name);
		int row = 0;
		String value = "";
		instance.insertRow(name, row, value);
	}
	
}
