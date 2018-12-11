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

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import java.util.function.BiPredicate;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public abstract class CommonTreeScanner extends TreePathScanner<Boolean, BiPredicate<Tree,TreePath>> {

	public abstract Boolean visitCommonTree(Tree node, BiPredicate<Tree,TreePath> consumer);

	@Override
	public Boolean visitErroneous(ErroneousTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitErroneous(node, p);
	}

	@Override
	public Boolean visitOther(Tree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitOther(node, p);
	}

	@Override
	public Boolean visitAnnotatedType(AnnotatedTypeTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitAnnotatedType(node, p);
	}

	@Override
	public Boolean visitAnnotation(AnnotationTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitAnnotation(node, p);
	}

	@Override
	public Boolean visitModifiers(ModifiersTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitModifiers(node, p);
	}

	@Override
	public Boolean visitWildcard(WildcardTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitWildcard(node, p);
	}

	@Override
	public Boolean visitTypeParameter(TypeParameterTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitTypeParameter(node, p);
	}

	@Override
	public Boolean visitIntersectionType(IntersectionTypeTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitIntersectionType(node, p);
	}

	@Override
	public Boolean visitUnionType(UnionTypeTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitUnionType(node, p);
	}

	@Override
	public Boolean visitParameterizedType(ParameterizedTypeTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitParameterizedType(node, p);
	}

	@Override
	public Boolean visitArrayType(ArrayTypeTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitArrayType(node, p);
	}

	@Override
	public Boolean visitPrimitiveType(PrimitiveTypeTree node, BiPredicate<Tree,TreePath> p) {
		return visitCommonTree(node,p);
	}

	@Override
	public Boolean visitLiteral(LiteralTree node, BiPredicate<Tree,TreePath> p) {
		return visitCommonTree(node,p);
	}

	@Override
	public Boolean visitIdentifier(IdentifierTree node, BiPredicate<Tree,TreePath> p) {
		return visitCommonTree(node,p);
	}

	@Override
	public Boolean visitMemberReference(MemberReferenceTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitMemberReference(node, p);
	}

	@Override
	public Boolean visitMemberSelect(MemberSelectTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitMemberSelect(node, p);
	}

	@Override
	public Boolean visitArrayAccess(ArrayAccessTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitArrayAccess(node, p);
	}

	@Override
	public Boolean visitInstanceOf(InstanceOfTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitInstanceOf(node, p);
	}

	@Override
	public Boolean visitTypeCast(TypeCastTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitTypeCast(node, p);
	}

	@Override
	public Boolean visitBinary(BinaryTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitBinary(node, p);
	}

	@Override
	public Boolean visitUnary(UnaryTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitUnary(node, p);
	}

	@Override
	public Boolean visitCompoundAssignment(CompoundAssignmentTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitCompoundAssignment(node, p);
	}

	@Override
	public Boolean visitAssignment(AssignmentTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitAssignment(node, p);
	}

	@Override
	public Boolean visitParenthesized(ParenthesizedTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitParenthesized(node, p);
	}

	@Override
	public Boolean visitLambdaExpression(LambdaExpressionTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitLambdaExpression(node, p);
	}

	@Override
	public Boolean visitNewArray(NewArrayTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitNewArray(node, p);
	}

	@Override
	public Boolean visitNewClass(NewClassTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitNewClass(node, p);
	}

	@Override
	public Boolean visitMethodInvocation(MethodInvocationTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitMethodInvocation(node, p);
	}

	@Override
	public Boolean visitAssert(AssertTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitAssert(node, p);
	}

	@Override
	public Boolean visitThrow(ThrowTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitThrow(node, p);
	}

	@Override
	public Boolean visitReturn(ReturnTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitReturn(node, p);
	}

	@Override
	public Boolean visitContinue(ContinueTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitContinue(node, p);
	}

	@Override
	public Boolean visitBreak(BreakTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitBreak(node, p);
	}

	@Override
	public Boolean visitExpressionStatement(ExpressionStatementTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitExpressionStatement(node, p);
	}

	@Override
	public Boolean visitIf(IfTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitIf(node, p);
	}

	@Override
	public Boolean visitConditionalExpression(ConditionalExpressionTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitConditionalExpression(node, p);
	}

	@Override
	public Boolean visitCatch(CatchTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitCatch(node, p);
	}

	@Override
	public Boolean visitTry(TryTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitTry(node, p);
	}

	@Override
	public Boolean visitSynchronized(SynchronizedTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitSynchronized(node, p);
	}

	@Override
	public Boolean visitCase(CaseTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitCase(node, p);
	}

	@Override
	public Boolean visitSwitch(SwitchTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitSwitch(node, p);
	}

	@Override
	public Boolean visitLabeledStatement(LabeledStatementTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitLabeledStatement(node, p);
	}

	@Override
	public Boolean visitEnhancedForLoop(EnhancedForLoopTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitEnhancedForLoop(node, p);
	}

	@Override
	public Boolean visitForLoop(ForLoopTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitForLoop(node, p);
	}

	@Override
	public Boolean visitWhileLoop(WhileLoopTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitWhileLoop(node, p);
	}

	@Override
	public Boolean visitDoWhileLoop(DoWhileLoopTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitDoWhileLoop(node, p);
	}

	@Override
	public Boolean visitBlock(BlockTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitBlock(node, p);
	}

	@Override
	public Boolean visitEmptyStatement(EmptyStatementTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitEmptyStatement(node, p);
	}

	@Override
	public Boolean visitVariable(VariableTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitVariable(node, p);
	}

	@Override
	public Boolean visitMethod(MethodTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitMethod(node, p);
	}

	@Override
	public Boolean visitClass(ClassTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitClass(node, p);
	}

	@Override
	public Boolean visitImport(ImportTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitImport(node, p);
	}

	@Override
	public Boolean visitCompilationUnit(CompilationUnitTree node, BiPredicate<Tree,TreePath> p) {
		return !visitCommonTree(node,p) ? false : super.visitCompilationUnit(node, p);
	}

}
