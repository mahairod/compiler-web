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

import java.util.Map;
import java.util.stream.Stream;
import javax.ejb.Local;
import javax.enterprise.concurrent.ManagedExecutorService;
import me.astafiev.web.compiler.beans.Completion;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
@Local
public interface CompileStateLocal extends CompilationCtx {

	Stream<Completion> getCompletions(final String name, String prefix, final int row, final int column);

	ManagedExecutorService getExecutor();

	Map<String, Source> getSources();

	void insertRow(final String name, final int row, final String value) throws CompileStateException;

	boolean saveSource(final String name, final String source);

	Binary setBinary(String name, Binary binary);

	void updateRow(final String name, final int row, final String value) throws CompileStateException;
	
}
