/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */
package me.astafiev.web.compiler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.concurrent.ManagedExecutorService;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.state.Binary;
import me.astafiev.web.compiler.state.CompilationCtx;
import me.astafiev.web.compiler.state.IO;
import me.astafiev.web.compiler.state.Source;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class JCompilerTest {
	JCompiler instance;
	CompilationCtx ctx;
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@Before
	public void setUp() throws IOException {
		ExecutorService es = Executors.newSingleThreadExecutor();
		String sourceStr = IO.readSample();
		Source source = new Source("Sample", sourceStr);
		ctx = new CompilationCtx() {
			@Override
			public ExecutorService getExecutor() {
				return es;
			}
			@Override
			public Map<String, Source> getSources() {
				return Collections.singletonMap(source.getName(), source);
			}
			@Override
			public Binary setBinary(String name, Binary binary) {
				return null;
			}
		};
		instance = new JCompiler(ctx);
	}

	@Test
	public void testSuggestToken() throws Exception {
		System.out.println("suggestToken");
		int coord[] = {33, 19};
		Source source = ctx.getSources().entrySet().iterator().next().getValue();
		Stream<Completion> result = instance.suggestToken(source, coord[0], coord[1]);
		assertNotNull(result);
		List<Completion> rl = result.collect(Collectors.toList());
		assertTrue(rl.size()> 0);
	}
	
}
