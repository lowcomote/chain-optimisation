package org.eclipse.epsilon.etl.optimisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.eol.dom.AbortStatement;
import org.eclipse.epsilon.eol.dom.AndOperatorExpression;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.BooleanLiteral;
import org.eclipse.epsilon.eol.dom.BreakStatement;
import org.eclipse.epsilon.eol.dom.Case;
import org.eclipse.epsilon.eol.dom.CollectionLiteralExpression;
import org.eclipse.epsilon.eol.dom.ComplexOperationCallExpression;
import org.eclipse.epsilon.eol.dom.ContinueStatement;
import org.eclipse.epsilon.eol.dom.DeleteStatement;
import org.eclipse.epsilon.eol.dom.DivOperatorExpression;
import org.eclipse.epsilon.eol.dom.DoubleEqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.ElvisOperatorExpression;
import org.eclipse.epsilon.eol.dom.EnumerationLiteralExpression;
import org.eclipse.epsilon.eol.dom.EqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.ExecutableAnnotation;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.ExpressionInBrackets;
import org.eclipse.epsilon.eol.dom.ExpressionStatement;
import org.eclipse.epsilon.eol.dom.FirstOrderOperationCallExpression;
import org.eclipse.epsilon.eol.dom.ForStatement;
import org.eclipse.epsilon.eol.dom.GreaterEqualOperatorExpression;
import org.eclipse.epsilon.eol.dom.GreaterThanOperatorExpression;
import org.eclipse.epsilon.eol.dom.IfStatement;
import org.eclipse.epsilon.eol.dom.ImpliesOperatorExpression;
import org.eclipse.epsilon.eol.dom.Import;
import org.eclipse.epsilon.eol.dom.IntegerLiteral;
import org.eclipse.epsilon.eol.dom.ItemSelectorExpression;
import org.eclipse.epsilon.eol.dom.LessEqualOperatorExpression;
import org.eclipse.epsilon.eol.dom.LessThanOperatorExpression;
import org.eclipse.epsilon.eol.dom.MapLiteralExpression;
import org.eclipse.epsilon.eol.dom.MinusOperatorExpression;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.ModelDeclarationParameter;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.NegativeOperatorExpression;
import org.eclipse.epsilon.eol.dom.NewInstanceExpression;
import org.eclipse.epsilon.eol.dom.NotEqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.NotOperatorExpression;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.dom.OperationCallExpression;
import org.eclipse.epsilon.eol.dom.OrOperatorExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.dom.PlusOperatorExpression;
import org.eclipse.epsilon.eol.dom.PostfixOperatorExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.dom.RealLiteral;
import org.eclipse.epsilon.eol.dom.ReturnStatement;
import org.eclipse.epsilon.eol.dom.SimpleAnnotation;
import org.eclipse.epsilon.eol.dom.SpecialAssignmentStatement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.dom.StringLiteral;
import org.eclipse.epsilon.eol.dom.SwitchStatement;
import org.eclipse.epsilon.eol.dom.TernaryExpression;
import org.eclipse.epsilon.eol.dom.ThrowStatement;
import org.eclipse.epsilon.eol.dom.TimesOperatorExpression;
import org.eclipse.epsilon.eol.dom.TransactionStatement;
import org.eclipse.epsilon.eol.dom.TypeExpression;
import org.eclipse.epsilon.eol.dom.VariableDeclaration;
import org.eclipse.epsilon.eol.dom.WhileStatement;
import org.eclipse.epsilon.eol.dom.XorOperatorExpression;
import org.eclipse.epsilon.eol.types.EolAnyType;
import org.eclipse.epsilon.eol.types.EolCollectionType;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolNoType;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.erl.dom.Post;
import org.eclipse.epsilon.erl.dom.Pre;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.IEtlModule;
import org.eclipse.epsilon.etl.chain.optimisation.ModuleElementRewriter;
import org.eclipse.epsilon.etl.dom.EquivalentAssignmentStatement;
import org.eclipse.epsilon.etl.dom.IEtlVisitor;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class EtlDependencyMapGenerator implements IEtlVisitor {

	EtlStaticAnalyser staticAnalyser;
	EtlModule module;
	TransformationRule tr;
	HashMap<String, ArrayList<String>> trace = new HashMap<String, ArrayList<String>>();

	@Override
	public void visit(Post post) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Pre pre) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbortStatement abortStatement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AndOperatorExpression andOperatorExpression) {
		if (andOperatorExpression.getFirstOperand() != null)
			andOperatorExpression.getFirstOperand().accept(this);
		if (andOperatorExpression.getSecondOperand() != null)
			andOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(DeleteStatement deleteStatement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AnnotationBlock annotationBlock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AssignmentStatement assignmentStatement) {
		if (assignmentStatement instanceof EquivalentAssignmentStatement)
			visit((EquivalentAssignmentStatement) assignmentStatement);
		else
			assignmentStatement.getTargetExpression().accept(this);
		assignmentStatement.getValueExpression().accept(this);
	}

	@Override
	public void visit(BooleanLiteral booleanLiteral) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BreakStatement breakStatement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Case case_) {
		if (case_.getCondition() != null) {
			case_.getCondition().accept(this);
		} else
			case_.getBody().accept(this);
	}

	@Override
	public void visit(CollectionLiteralExpression<?> collectionLiteralExpression) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ComplexOperationCallExpression complexOperationCallExpression) {
		if (complexOperationCallExpression.getTargetExpression() != null)
			complexOperationCallExpression.getTargetExpression().accept(this);
	}

	@Override
	public void visit(ContinueStatement continueStatement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DivOperatorExpression divOperatorExpression) {
		if (divOperatorExpression.getFirstOperand() != null)
			divOperatorExpression.getFirstOperand().accept(this);
		if (divOperatorExpression.getSecondOperand() != null)
			divOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(DoubleEqualsOperatorExpression doubleEqualsOperatorExpression) {
		if (doubleEqualsOperatorExpression.getFirstOperand() != null)
			doubleEqualsOperatorExpression.getFirstOperand().accept(this);
		if (doubleEqualsOperatorExpression.getSecondOperand() != null)
			doubleEqualsOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(ElvisOperatorExpression elvisOperatorExpression) {
		if (elvisOperatorExpression.getFirstOperand() != null)
			elvisOperatorExpression.getFirstOperand().accept(this);
		if (elvisOperatorExpression.getSecondOperand() != null)
			elvisOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(EnumerationLiteralExpression enumerationLiteralExpression) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EqualsOperatorExpression equalsOperatorExpression) {
		if (equalsOperatorExpression.getFirstOperand() != null)
			equalsOperatorExpression.getFirstOperand().accept(this);
		if (equalsOperatorExpression.getSecondOperand() != null)
			equalsOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(ExecutableAnnotation executableAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExecutableBlock<?> executableBlock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExpressionInBrackets expressionInBrackets) {
		expressionInBrackets.getExpression().accept(this);
	}

	@Override
	public void visit(ExpressionStatement expressionStatement) {
		expressionStatement.getExpression().accept(this);

	}

	@Override
	public void visit(FirstOrderOperationCallExpression firstOrderOperationCallExpression) {
		firstOrderOperationCallExpression.getTargetExpression().accept(this);
		Iterator<Parameter> pi = firstOrderOperationCallExpression.getParameters().iterator();
		while (pi.hasNext()) {
			pi.next().accept(this);
		}
		if (!firstOrderOperationCallExpression.getExpressions().isEmpty()) {
			Iterator<Expression> ei = firstOrderOperationCallExpression.getExpressions().iterator();

			while (ei.hasNext()) {
				ei.next().accept(this);
			}
		}

	}

	@Override
	public void visit(ForStatement forStatement) {
		forStatement.getIteratorParameter().accept(this);
		forStatement.getIteratedExpression().accept(this);
		forStatement.getBodyStatementBlock().accept(this);

	}

	@Override
	public void visit(GreaterEqualOperatorExpression greaterEqualOperatorExpression) {
		if (greaterEqualOperatorExpression.getFirstOperand() != null)
			greaterEqualOperatorExpression.getFirstOperand().accept(this);
		if (greaterEqualOperatorExpression.getSecondOperand() != null)
			greaterEqualOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(GreaterThanOperatorExpression greaterThanOperatorExpression) {
		if (greaterThanOperatorExpression.getFirstOperand() != null)
			greaterThanOperatorExpression.getFirstOperand().accept(this);
		if (greaterThanOperatorExpression.getSecondOperand() != null)
			greaterThanOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(IfStatement ifStatement) {
		ifStatement.getConditionExpression().accept(this);
		ifStatement.getThenStatementBlock().accept(this);
		if (ifStatement.getElseStatementBlock() != null) {
			StatementBlock elseStatementBlock = ifStatement.getElseStatementBlock();
			if (elseStatementBlock.getStatements().size() == 1
					&& elseStatementBlock.getStatements().get(0) instanceof IfStatement) {
				elseStatementBlock.getStatements().get(0).accept(this);
			} else {
				ifStatement.getElseStatementBlock().accept(this);
			}
		}
	}

	@Override
	public void visit(ImpliesOperatorExpression impliesOperatorExpression) {
		if (impliesOperatorExpression.getFirstOperand() != null)
			impliesOperatorExpression.getFirstOperand().accept(this);
		if (impliesOperatorExpression.getSecondOperand() != null)
			impliesOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(Import import_) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IntegerLiteral integerLiteral) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ItemSelectorExpression itemSelectorExpression) {
		itemSelectorExpression.getTargetExpression().accept(this);
		itemSelectorExpression.getIndexExpression().accept(this);
	}

	@Override
	public void visit(LessEqualOperatorExpression lessEqualOperatorExpression) {
		if (lessEqualOperatorExpression.getFirstOperand() != null)
			lessEqualOperatorExpression.getFirstOperand().accept(this);
		if (lessEqualOperatorExpression.getSecondOperand() != null)
			lessEqualOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(LessThanOperatorExpression lessThanOperatorExpression) {
		if (lessThanOperatorExpression.getFirstOperand() != null)
			lessThanOperatorExpression.getFirstOperand().accept(this);
		if (lessThanOperatorExpression.getSecondOperand() != null)
			lessThanOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(MapLiteralExpression<?, ?> mapLiteralExpression) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MinusOperatorExpression minusOperatorExpression) {
		if (minusOperatorExpression.getFirstOperand() != null)
			minusOperatorExpression.getFirstOperand().accept(this);
		if (minusOperatorExpression.getSecondOperand() != null)
			minusOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(ModelDeclaration modelDeclaration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModelDeclarationParameter modelDeclarationParameter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NameExpression nameExpression) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NegativeOperatorExpression negativeOperatorExpression) {
		if (negativeOperatorExpression.getFirstOperand() != null)
			negativeOperatorExpression.getFirstOperand().accept(this);
		if (negativeOperatorExpression.getSecondOperand() != null)
			negativeOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(NewInstanceExpression newInstanceExpression) {
		newInstanceExpression.getTypeExpression().accept(this);
		Iterator<Expression> pi = newInstanceExpression.getParameterExpressions().iterator();
		while (pi.hasNext()) {
			pi.next().accept(this);
		}

	}

	@Override
	public void visit(NotEqualsOperatorExpression notEqualsOperatorExpression) {
		if (notEqualsOperatorExpression.getFirstOperand() != null)
			notEqualsOperatorExpression.getFirstOperand().accept(this);
		if (notEqualsOperatorExpression.getSecondOperand() != null)
			notEqualsOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(NotOperatorExpression notOperatorExpression) {
		if (notOperatorExpression.getFirstOperand() != null)
			notOperatorExpression.getFirstOperand().accept(this);
		if (notOperatorExpression.getSecondOperand() != null)
			notOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(Operation operation) {
		operation.getBody().accept(this);

	}

	@Override
	public void visit(OperationCallExpression operationCallExpression) {
		Expression target = operationCallExpression.getTargetExpression();
		if (target != null)
			target.accept(this);

		// Case 1 & 2
		if (operationCallExpression.getName().equals("equivalent")) {
			System.out.println("Case 1 or 2");
			EolType type = staticAnalyser.getResolvedType(operationCallExpression.getTargetExpression());
			List<TransformationRule> rules = new ArrayList<TransformationRule>();

			if (operationCallExpression.getParameterExpressions().isEmpty())  {
				System.err.println(operationCallExpression.getTargetExpression());
				rules = getTransformationRuleContainingSourceElement(type);
			for (TransformationRule rule : rules) {
				StringLiteral param = new StringLiteral("rule_"+rule.getName());
				operationCallExpression.getParameterExpressions().add(param);
			}
			}

			else {
				Iterator<Expression> pi = operationCallExpression.getParameterExpressions().iterator();
				while (pi.hasNext()) {
					String ruleName = ((StringLiteral) pi.next()).getValue();
					rules.add(getTransformationRuleByName(ruleName));
				}
			}
			for (TransformationRule rule : rules)
				markTraceable(rule);

		}

		// Case 3
		else if (operationCallExpression.getName().equals("equivalents")
				&& !(staticAnalyser.getResolvedType(target) instanceof EolCollectionType)) {
			System.out.println("Case 3");
			EolType type = staticAnalyser.getResolvedType(operationCallExpression.getTargetExpression());
			List<TransformationRule> rules = new ArrayList<TransformationRule>();

			if (operationCallExpression.getParameterExpressions().isEmpty())
				rules = getTransformationRuleContainingSourceElement(type);

			else {
				Iterator<Expression> pi = operationCallExpression.getParameterExpressions().iterator();
				while (pi.hasNext()) {
					String ruleName = ((StringLiteral) pi.next()).getValue();
					rules.add(getTransformationRuleByName(ruleName));
				}
			}
			for (TransformationRule rule : rules)
				markTraceable(rule);
		}

		// Case 4
		else if (operationCallExpression.getName().equals("equivalents")
				&& staticAnalyser.getResolvedType(target) instanceof EolCollectionType) {
			System.out.println("Case 4");
			EolType type = staticAnalyser.getResolvedType(operationCallExpression.getTargetExpression());
			List<TransformationRule> rules = new ArrayList<TransformationRule>();

			if (operationCallExpression.getParameterExpressions().isEmpty())
				rules = getTransformationRuleContainingSourceElement(type);

			else {
				Iterator<Expression> pi = operationCallExpression.getParameterExpressions().iterator();
				while (pi.hasNext()) {
					String ruleName = ((StringLiteral) pi.next()).getValue();
					rules.add(getTransformationRuleByName(ruleName));
					NameExpression nam = new NameExpression("rule_" + ruleName);
					OperationCallExpression op = new OperationCallExpression(null, nam,
							operationCallExpression.getTargetExpression());
					new ModuleElementRewriter(operationCallExpression, op).rewrite();
				}
			}
			for (TransformationRule rule : rules)
				markTraceable(rule);
		} else {
			System.out.println(operationCallExpression.getName());
			Operation matched = staticAnalyser.getExactMatchedOperation(operationCallExpression);
			if(matched == null)
				matched = staticAnalyser.getMatchedOperations(operationCallExpression).get(0);
			System.out.println(matched.getName());
			visit(matched);
		}
		Iterator<Expression> pi = operationCallExpression.getParameterExpressions().iterator();
		while (pi.hasNext()) {
			pi.next().accept(this);
		}

	}

	@Override
	public void visit(OrOperatorExpression orOperatorExpression) {
		if (orOperatorExpression.getFirstOperand() != null)
			orOperatorExpression.getFirstOperand().accept(this);
		if (orOperatorExpression.getSecondOperand() != null)
			orOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(Parameter parameter) {
		if (parameter.getTypeExpression() != null) {
			parameter.getTypeExpression().accept(this);
		}

	}

	@Override
	public void visit(PlusOperatorExpression plusOperatorExpression) {
		if (plusOperatorExpression.getFirstOperand() != null)
			plusOperatorExpression.getFirstOperand().accept(this);
		if (plusOperatorExpression.getSecondOperand() != null)
			plusOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(PostfixOperatorExpression postfixOperatorExpression) {
		if (postfixOperatorExpression.getAssignmentStatement() != null)
			postfixOperatorExpression.getAssignmentStatement().accept(this);
		if (postfixOperatorExpression.getFirstOperand() != null)
			postfixOperatorExpression.getFirstOperand().accept(this);
		if (postfixOperatorExpression.getSecondOperand() != null)
			postfixOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(PropertyCallExpression propertyCallExpression) {
		propertyCallExpression.getTargetExpression().accept(this);
	}

	@Override
	public void visit(RealLiteral realLiteral) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReturnStatement returnStatement) {
		if (returnStatement.getReturnedExpression() != null) {
			returnStatement.getReturnedExpression().accept(this);
		}
	}

	@Override
	public void visit(SimpleAnnotation simpleAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(StatementBlock statementBlock) {

		statementBlock.getStatements().forEach(s -> s.accept(this));

	}

	@Override
	public void visit(StringLiteral stringLiteral) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SwitchStatement switchStatement) {
		switchStatement.getConditionExpression().accept(this);
		switchStatement.getCases().forEach(c -> {
			c.accept(this);
		});
		if (switchStatement.getDefault() != null) {
			switchStatement.getDefault().accept(this);
		}
	}

	@Override
	public void visit(TernaryExpression ternaryExpression) {
		if (ternaryExpression.getFirstOperand() != null)
			ternaryExpression.getFirstOperand().accept(this);
		if (ternaryExpression.getSecondOperand() != null)
			ternaryExpression.getSecondOperand().accept(this);
		if (ternaryExpression.getThirdOperand() != null)
			ternaryExpression.getThirdOperand().accept(this);

	}

	@Override
	public void visit(ThrowStatement throwStatement) {
		if (throwStatement.getThrown() != null)
			throwStatement.getThrown().accept(this);
	}

	@Override
	public void visit(TimesOperatorExpression timesOperatorExpression) {
		if (timesOperatorExpression.getFirstOperand() != null)
			timesOperatorExpression.getFirstOperand().accept(this);
		if (timesOperatorExpression.getSecondOperand() != null)
			timesOperatorExpression.getSecondOperand().accept(this);
	}

	@Override
	public void visit(TransactionStatement transactionStatement) {

	}

	@Override
	public void visit(TypeExpression typeExpression) {

	}

	@Override
	public void visit(VariableDeclaration variableDeclaration) {
		if (variableDeclaration.getTypeExpression() != null)
			variableDeclaration.getTypeExpression().accept(this);
	}

	@Override
	public void visit(WhileStatement whileStatement) {
		whileStatement.getConditionExpression().accept(this);
		whileStatement.getBodyStatementBlock().accept(this);
	}

	@Override
	public void visit(XorOperatorExpression xorOperatorExpression) {
		if (xorOperatorExpression.getFirstOperand() != null)
			xorOperatorExpression.getFirstOperand().accept(this);
		if (xorOperatorExpression.getSecondOperand() != null)
			xorOperatorExpression.getSecondOperand().accept(this);
	}

	public void visit(SpecialAssignmentStatement assignmentStatement) {
		assignmentStatement.getTargetExpression().accept(this);
		assignmentStatement.getValueExpression().accept(this);
	}

	public void visit(EquivalentAssignmentStatement assignmentStatement) {
		assignmentStatement.getTargetExpression().accept(this);
		assignmentStatement.getValueExpression().accept(this);
		EolType type = staticAnalyser.getResolvedType(assignmentStatement.getValueExpression());
		List<TransformationRule> rules = getTransformationRuleContainingSourceElement(type);

		for (TransformationRule rule : rules)
			markTraceable(rule);
	}

	@Override
	public void visit(TransformationRule transformationRule) {

	}

	public void rewriteTransformationRules(EtlModule etlModule) {
		StatementBlock statements;
		for (TransformationRule transformationRule : etlModule.getTransformationRules()) {

			this.tr = transformationRule;
			if (transformationRule.getBody().getBody() instanceof StatementBlock) {
				statements = (StatementBlock) (transformationRule.getBody().getBody());
				if(statements!=null)
					statements.accept(this);
			} else {
				((Expression) transformationRule.getBody().getBody()).accept(this);
			}

		}
	}

	public HashMap<String, ArrayList<String>> optimiseTrace(IEtlModule module, EtlStaticAnalyser staticAnalyser) {
		EtlModule etlModule = (EtlModule) module;
		this.module = etlModule;
		this.staticAnalyser = staticAnalyser;
		for (TransformationRule rule : etlModule.getTransformationRules()) {
			this.trace.put(rule.getName(), new ArrayList<String>());
		}

		rewriteTransformationRules(etlModule);
		
		return this.trace;

	}

	protected TransformationRule getTransformationRuleByName(String name) {
		TransformationRule tr = new TransformationRule();
		for (TransformationRule rule : module.getDeclaredTransformationRules())
			if (rule.getName().equals(name))
				tr = rule;
		return tr;
	}

	public void markTraceable(TransformationRule rule) {

		if (rule.getData().get("traceable") != null) {
			rule.getData().put("traceable", "many");
		} else
			rule.getData().put("traceable", "one");
		if(!(trace.get(rule.getName()).contains(this.tr.getName())))
		this.trace.get(rule.getName()).add(this.tr.getName());

	}

	public List<TransformationRule> getTransformationRuleContainingSourceElement(EolType type) {
		List<TransformationRule> rules = new ArrayList<TransformationRule>();
		if (type instanceof EolCollectionType)
			type = ((EolCollectionType) type).getContentType();
		for (TransformationRule rule : module.getTransformationRules()) {
			EolModelElementType sourceType = (EolModelElementType) staticAnalyser.getType(rule.getSourceParameter());
			String newSource = sourceType.getModelName();
			if(((EolModelElementType)type).getModelName().equals("")) {
			
				sourceType.setModelName("");
			}
			if (isCompatible(sourceType, type))
				rules.add(rule);
			else if (canBeCompatible(sourceType, type))
				rules.add(rule);
			sourceType.setModelName(newSource);
		}
		return rules;
	}

	public boolean isCompatible(EolType targetType, EolType valueType) {

		boolean ok = false;

		if (targetType.equals(EolNoType.Instance) || valueType.equals(EolNoType.Instance))
			return false;
		else

			while (!ok) {
				if (!(targetType.equals(valueType)) && !(targetType instanceof EolAnyType)) {

					if (valueType instanceof EolAnyType) {
						return false;
					}

					valueType = staticAnalyser.getParentType(valueType);

				} else if (targetType instanceof EolAnyType) {
					return true;
				} else if (valueType instanceof EolCollectionType
						&& !((((EolCollectionType) targetType).getContentType()) instanceof EolAnyType)) {

					EolType valueContentType = ((EolCollectionType) valueType).getContentType();
					EolType targetContentType = ((EolCollectionType) targetType).getContentType();

					while (targetContentType instanceof EolCollectionType
							&& valueContentType instanceof EolCollectionType) {
						if (targetContentType.equals(valueContentType)) {
							return isCompatible(((EolCollectionType) targetContentType).getContentType(),
									((EolCollectionType) valueContentType).getContentType());
						} else {
							valueContentType = staticAnalyser.getParentType(valueContentType);
							return isCompatible(targetContentType, valueContentType);

						}
					}
					while (!ok) {
						if (valueContentType instanceof EolAnyType) {
							return false;
						}
						if (!valueContentType.equals(targetContentType)) {
							valueContentType = staticAnalyser.getParentType(valueContentType);
						} else {
							return true;
						}
					}
				} else
					return true;
			}
		return false;
	}

	public boolean canBeCompatible(EolType targetType, EolType valueType) {

		boolean ok = false;
		if (targetType == null || valueType == null)
			return false;
		else
			while (!ok) {

				if (!(targetType.equals(valueType)) && !(valueType instanceof EolAnyType)) {

					targetType = staticAnalyser.getParentType(targetType);

					if (targetType instanceof EolAnyType) {
						return false;
					}

				} else if (valueType instanceof EolAnyType) {
					return false;
				} else if (targetType instanceof EolCollectionType
						&& !((((EolCollectionType) valueType).getContentType()) instanceof EolAnyType)) {

					EolType valueContentType = ((EolCollectionType) valueType).getContentType();
					EolType targetContentType = ((EolCollectionType) targetType).getContentType();

					while (targetContentType instanceof EolCollectionType
							&& valueContentType instanceof EolCollectionType) {
						if (targetContentType.equals(valueContentType)) {
							return canBeCompatible(((EolCollectionType) targetContentType).getContentType(),
									((EolCollectionType) valueContentType).getContentType());
						} else {
							valueContentType = staticAnalyser.getParentType(valueContentType);
							return canBeCompatible(targetContentType, valueContentType);

						}
					}
					while (!ok) {
						if (valueContentType instanceof EolAnyType || targetContentType instanceof EolAnyType) {
							return true;
						}
						if (!valueContentType.equals(targetContentType)) {
							targetContentType = staticAnalyser.getParentType(targetContentType);
							if (targetContentType instanceof EolAnyType)
								return false;
						} else {
							return true;
						}
					}
				} else
					return true;
			}
		return false;
	}

}
