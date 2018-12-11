/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package me.astafiev.web.compiler.isol;

import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import java.util.Collection;
import java.util.stream.Stream;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.beans.CompletionType;
import net.elliptica.tools.Accessor;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class Completer {
	Stream<Completion> guessCompletionsFor(JavacTask task, TreePath treePath) {
//		Trees trees = Trees.instance(task);
		Tree parent = treePath.getParentPath().getLeaf();
		Tree leaf = treePath.getLeaf();
		switch (parent.getKind()) {
			case PARAMETERIZED_TYPE:
				return allClassTypes(task);
			case VARIABLE:
				if (((VariableTree)parent).getType() == leaf) {
					return allClassTypes(task);
				} else break;
		}
		return Stream.empty();
	}

	private Stream<Completion> allClassTypes(JavacTask task) {
		if (classes == null) {
			classes = new Accessor(task.getTypes()).getField("syms", Symtab.class).classes.values();
		}
		return classes.stream().map(cl -> cl.getSimpleName().toString())
			.filter(n -> !n.matches(".*\\$[0-9]+.*"))
			.distinct().map(n -> new Completion(CompletionType.CLASS, n));
	}

	private Collection<Symbol.ClassSymbol> classes;
}
