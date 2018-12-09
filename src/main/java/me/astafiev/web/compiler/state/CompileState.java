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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.concurrent.ManagedExecutorService;
import me.astafiev.web.compiler.JCompiler;
import me.astafiev.web.compiler.beans.Completion;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
@Stateful
public class CompileState implements CompilationCtx {
	
	@Resource
	private ManagedExecutorService executorService; 

	private final Map<String,Source> sources = new HashMap<>();
	private final Map<String,Binary> binaries = new HashMap<>();
	private final JCompiler compiler = new JCompiler(this);

	@Override
	public Map<String, Source> getSources() {
		return sources;
	}

	@Override
	public ManagedExecutorService getExecutor() {
		return executorService;
	}
	
	@Override
	public Binary setBinary(String name, Binary binary) {
		return binaries.put(name, binary);
	}

	public boolean saveSource(final String name, final String source) {
		sources.put(name, new Source(name, source));
		return false;
	}

	public Stream<Completion> getCompletions(final String name, final int row, final int column) {
		return compiler.suggestToken(sources.get(name), row, column);
	}

	public void updateRow(final String name, final int row, final String value) throws CompileStateException {
		makeOp(value == null? Operation.DELETE : Operation.UPDATE, name, row, value);
	}

	public void insertRow(final String name, final int row, final String value) throws CompileStateException {
		makeOp(Operation.INSERT, name, row, value);
	}
	
	private enum Operation {
		INSERT, UPDATE, DELETE
	}
	
	private void makeOp(Operation op, final String name, final int row, final String value) throws CompileStateException {
		Source source = sources.get(name);
		if (source == null) {
			throw new CompileStateException("There is no source named " + name);
		}
		switch (op) {
			case INSERT: source.insert(row, value);
				break;
			case UPDATE: source.replace(row, value);
				break;
			case DELETE: source.remove(row);
				break;
		}
	}

}
