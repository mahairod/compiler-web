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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static javax.tools.StandardLocation.SOURCE_PATH;
import javax.tools.ToolProvider;
import me.astafiev.web.compiler.state.Binary;
import me.astafiev.web.compiler.state.CompileState;
import me.astafiev.web.compiler.state.FileStore;
import me.astafiev.web.compiler.state.Source;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class ДиспетчерФайлов implements JavaFileManager {

	private static final String SRC_SUFFIX = Kind.SOURCE.extension;
	private static final String BIN_SUFFIX = Kind.CLASS.extension;

	private final JavaFileManager fileManager;
	private final FileStore fileStore;
	private List<ФайлЯва> sourceFiles = new ArrayList<>();

	public ДиспетчерФайлов(FileStore fileStore, DiagnosticCollector<JavaFileObject> diagnostics) {
		this.fileManager = ToolProvider.getSystemJavaCompiler().getStandardFileManager(diagnostics, Locale.ENGLISH, Charset.defaultCharset());
		this.fileStore = fileStore;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return null;
	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse) throws IOException {
		if (location == CLASS_OUTPUT) {
			return Collections.EMPTY_LIST;
		}
		if (location != SOURCE_PATH) {
			return fileManager.list(location, packageName, kinds, recurse);
		}
		if (packageName!=null && !packageName.isEmpty() || !kinds.contains(Kind.SOURCE)) {
			return Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableCollection(sourceFiles);
	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file) {
		if (location != CLASS_OUTPUT && location != SOURCE_PATH) {
			return fileManager.inferBinaryName(location, file);
		}
		String src = file.getName();
		if (!src.endsWith(SRC_SUFFIX) || src.length()-1 < SRC_SUFFIX.length()) {
			return null;
		}
		return src.substring(0, src.length() - SRC_SUFFIX.length()).concat(BIN_SUFFIX);
	}

	@Override
	public boolean isSameFile(FileObject a, FileObject b) {
		return Objects.equals(a.getName(), b.getName());
	}

	@Override
	public boolean handleOption(String current, Iterator<String> remaining) {
		return false;
	}

	@Override
	public boolean hasLocation(Location location) {
		return location == SOURCE_PATH || location == CLASS_OUTPUT ||
				fileManager.hasLocation(location);
	}

	@Override
	public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) {
		if (location != SOURCE_PATH || kind != Kind.SOURCE) {
			return null;
		}
		Source действие = findEntity(className);
		return new ИсходникЯва(className, действие);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
		if (location != CLASS_OUTPUT || kind != Kind.CLASS) {
			return null;
		}
		if (sibling instanceof ФайлЯва) {
			return new БинарникЯва((ФайлЯва) sibling);
		} else {
			Source действие = findEntity(className);
			Binary b = new Binary(действие);
			fileStore.setBinary(className, b);
			return new БинарникЯва(className, b);
		}
	}

	private Source findEntity(String name) {
		return fileStore.getSources().get(name);
	}

	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) {
		if (location != SOURCE_PATH) {
			return null;
		}
		return getJavaFileForInput(location, relativeName, Kind.SOURCE);
	}

	@Override
	public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
		if (location != CLASS_OUTPUT) {
			return null;
		}
		return getJavaFileForOutput(location, relativeName, Kind.CLASS, sibling);
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public int isSupportedOption(String option) {
		return -1;
	}

}
