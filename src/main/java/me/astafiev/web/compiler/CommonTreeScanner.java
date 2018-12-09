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

import com.sun.source.tree.*;
import com.sun.source.util.TreePathScanner;
import java.util.function.Predicate;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public abstract class CommonTreeScanner extends TreePathScanner<Boolean, Predicate<Tree>> {

	public abstract Boolean visitCommonTree(Tree node, Predicate<Tree> consumer);

	@Override
	public Boolean visitErroneous(ErroneousTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitErroneous(node, p);
	}

	@Override
	public Boolean visitOther(Tree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitOther(node, p);
	}

	@Override
	public Boolean visitAnnotatedType(AnnotatedTypeTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitAnnotatedType(node, p);
	}

	@Override
	public Boolean visitAnnotation(AnnotationTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitAnnotation(node, p);
	}

	@Override
	public Boolean visitModifiers(ModifiersTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitModifiers(node, p);
	}

	@Override
	public Boolean visitWildcard(WildcardTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitWildcard(node, p);
	}

	@Override
	public Boolean visitTypeParameter(TypeParameterTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitTypeParameter(node, p);
	}

	@Override
	public Boolean visitIntersectionType(IntersectionTypeTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitIntersectionType(node, p);
	}

	@Override
	public Boolean visitUnionType(UnionTypeTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitUnionType(node, p);
	}

	@Override
	public Boolean visitParameterizedType(ParameterizedTypeTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitParameterizedType(node, p);
	}

	@Override
	public Boolean visitArrayType(ArrayTypeTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitArrayType(node, p);
	}

	@Override
	public Boolean visitPrimitiveType(PrimitiveTypeTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitPrimitiveType(node, p);
	}

	@Override
	public Boolean visitLiteral(LiteralTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitLiteral(node, p);
	}

	@Override
	public Boolean visitIdentifier(IdentifierTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitIdentifier(node, p);
	}

	@Override
	public Boolean visitMemberReference(MemberReferenceTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitMemberReference(node, p);
	}

	@Override
	public Boolean visitMemberSelect(MemberSelectTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitMemberSelect(node, p);
	}

	@Override
	public Boolean visitArrayAccess(ArrayAccessTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitArrayAccess(node, p);
	}

	@Override
	public Boolean visitInstanceOf(InstanceOfTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitInstanceOf(node, p);
	}

	@Override
	public Boolean visitTypeCast(TypeCastTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitTypeCast(node, p);
	}

	@Override
	public Boolean visitBinary(BinaryTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitBinary(node, p);
	}

	@Override
	public Boolean visitUnary(UnaryTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitUnary(node, p);
	}

	@Override
	public Boolean visitCompoundAssignment(CompoundAssignmentTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitCompoundAssignment(node, p);
	}

	@Override
	public Boolean visitAssignment(AssignmentTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitAssignment(node, p);
	}

	@Override
	public Boolean visitParenthesized(ParenthesizedTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitParenthesized(node, p);
	}

	@Override
	public Boolean visitLambdaExpression(LambdaExpressionTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitLambdaExpression(node, p);
	}

	@Override
	public Boolean visitNewArray(NewArrayTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitNewArray(node, p);
	}

	@Override
	public Boolean visitNewClass(NewClassTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitNewClass(node, p);
	}

	@Override
	public Boolean visitMethodInvocation(MethodInvocationTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitMethodInvocation(node, p);
	}

	@Override
	public Boolean visitAssert(AssertTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitAssert(node, p);
	}

	@Override
	public Boolean visitThrow(ThrowTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitThrow(node, p);
	}

	@Override
	public Boolean visitReturn(ReturnTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitReturn(node, p);
	}

	@Override
	public Boolean visitContinue(ContinueTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitContinue(node, p);
	}

	@Override
	public Boolean visitBreak(BreakTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitBreak(node, p);
	}

	@Override
	public Boolean visitExpressionStatement(ExpressionStatementTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitExpressionStatement(node, p);
	}

	@Override
	public Boolean visitIf(IfTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitIf(node, p);
	}

	@Override
	public Boolean visitConditionalExpression(ConditionalExpressionTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitConditionalExpression(node, p);
	}

	@Override
	public Boolean visitCatch(CatchTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitCatch(node, p);
	}

	@Override
	public Boolean visitTry(TryTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitTry(node, p);
	}

	@Override
	public Boolean visitSynchronized(SynchronizedTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitSynchronized(node, p);
	}

	@Override
	public Boolean visitCase(CaseTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitCase(node, p);
	}

	@Override
	public Boolean visitSwitch(SwitchTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitSwitch(node, p);
	}

	@Override
	public Boolean visitLabeledStatement(LabeledStatementTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitLabeledStatement(node, p);
	}

	@Override
	public Boolean visitEnhancedForLoop(EnhancedForLoopTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitEnhancedForLoop(node, p);
	}

	@Override
	public Boolean visitForLoop(ForLoopTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitForLoop(node, p);
	}

	@Override
	public Boolean visitWhileLoop(WhileLoopTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitWhileLoop(node, p);
	}

	@Override
	public Boolean visitDoWhileLoop(DoWhileLoopTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitDoWhileLoop(node, p);
	}

	@Override
	public Boolean visitBlock(BlockTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitBlock(node, p);
	}

	@Override
	public Boolean visitEmptyStatement(EmptyStatementTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitEmptyStatement(node, p);
	}

	@Override
	public Boolean visitVariable(VariableTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitVariable(node, p);
	}

	@Override
	public Boolean visitMethod(MethodTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitMethod(node, p);
	}

	@Override
	public Boolean visitClass(ClassTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitClass(node, p);
	}

	@Override
	public Boolean visitImport(ImportTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitImport(node, p);
	}

	@Override
	public Boolean visitCompilationUnit(CompilationUnitTree node, Predicate<Tree> p) {
		return !p.test(node)? false : super.visitCompilationUnit(node, p);
	}

}
