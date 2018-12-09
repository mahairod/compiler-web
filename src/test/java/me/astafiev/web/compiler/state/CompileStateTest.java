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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.embeddable.EJBContainer;
import javax.enterprise.concurrent.ManagedExecutorService;
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
	private CompileState instance;
	
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
		instance =  (CompileState)container.getContext().lookup("java:global/classes/CompileState");
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
		ManagedExecutorService result = instance.getExecutor();
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
		String name = "Sample";
		int row = 33;
		int column = 19;
		saveSource(name);
		Stream<Completion> result = instance.getCompletions(name, row, column);
		assertNotNull(result);
		List<Completion> rl = result.collect(Collectors.toList());
		assertTrue(rl.size()> 0);
	}

	@Test
	public void testUpdateRow() throws Exception {
		System.out.println("updateRow");
		String name = "";
		int row = 0;
		String value = "";
		instance.updateRow(name, row, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testInsertRow() throws Exception {
		System.out.println("insertRow");
		String name = "";
		int row = 0;
		String value = "";
		instance.insertRow(name, row, value);
		fail("The test case is a prototype.");
	}
	
}
