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

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javac.util.Names;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.lang.model.element.Name;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.beans.CompletionType;
import net.elliptica.tools.Accessor;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class Completer {
	Stream<Completion> guessCompletionsFor(Tree type, String prefix, JavacTask task, TreePath treePath) {
//		Trees trees = Trees.instance(task);
		Tree parent = treePath.getParentPath().getLeaf();
		Tree leaf = treePath.getLeaf();
		switch (parent.getKind()) {
			case PARAMETERIZED_TYPE:
				return allClassTypes(task, treePath);
			case VARIABLE:
				if (((VariableTree)parent).getType() == leaf) {
					return allClassTypes(task, treePath);
				} else break;
			case RETURN:
				return composeName(type, prefix, task, treePath);
		}
		return Stream.empty();
	}

	Stream<Completion> composeName(Tree type, String prefix, JavacTask task, TreePath treePath) {
		Stream.Builder<Completion> strBuilder = Stream.builder();
		if (!prefix.isEmpty()) {
			strBuilder.add(new Completion(CompletionType.IDENTIFIER, prefix));
		}
		if (type instanceof IdentifierTree) {
			Name n = ((IdentifierTree)type).getName();
			String name = Character.toLowerCase(n.charAt(0)) + n.toString().substring(1);
			strBuilder.add(new Completion(CompletionType.IDENTIFIER, name));
		}
		if (type == null) {
			Symtab syms = new Accessor(task.getTypes()).getField("syms", Symtab.class);
			Names names = new Accessor(syms).getField("names", Names.class);
			Table nameTable = new Accessor(names).getField("table", Table.class);
		}

		return strBuilder.build();
	}

	private Stream<Completion> allClassTypes(JavacTask task, TreePath treePath) {
		if (classes == null) {
			classes = new Accessor(task.getTypes()).getField("syms", Symtab.class).classes.values();
			imports = treePath.getCompilationUnit().getImports();
		}

		Stream<Name> impNames = imports.stream()
				.map(imp -> imp.getQualifiedIdentifier())
				.filter(imp -> imp.getKind() == Kind.MEMBER_SELECT)
				.map(imp -> ((MemberSelectTree)imp).getIdentifier());

		Stream<Name> symNames = classes.stream().map(cl -> cl.getSimpleName());

		return Stream.concat(symNames, impNames).map(Name::toString)
			.filter(n -> !n.matches(".*\\$[0-9]+.*"))
			.distinct().map(n -> new Completion(CompletionType.CLASS, n));
	}

	private Collection<Symbol.ClassSymbol> classes;
	private List<? extends ImportTree> imports;
}
