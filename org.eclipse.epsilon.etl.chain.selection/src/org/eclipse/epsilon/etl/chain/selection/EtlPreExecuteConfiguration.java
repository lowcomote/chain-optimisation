package org.eclipse.epsilon.etl.chain.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.module.ModuleMarker;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.dom.OperationList;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.eol.m3.MetaClass;
import org.eclipse.epsilon.eol.m3.Metamodel;
import org.eclipse.epsilon.eol.m3.StructuralFeature;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.execute.context.IEtlContext;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;
import org.eclipse.epsilon.etl.trace.TransformationTrace;
import org.eclipse.epsilon.examples.staticanalyser.eol.SubEmfModelFactory;

public class EtlPreExecuteConfiguration extends EolRunConfiguration {
	IEolModule module;
	//long sumtotal=0;
	Chaining_MT calculate;
	int total, totalstatement = 0;
	public EtlPreExecuteConfiguration(EolRunConfiguration other) throws Exception {
		super(other);
		module = super.getModule();
		//total=calculate.calculateMTChain(module);
	}


	@Override
	protected void preExecute() throws Exception {
		
		super.preExecute();
		
		//Metamodel mm1 = null;
		//Map<String, Object> mm1 = null;
		module.getContext().setModule(module);
		long startTime = System.currentTimeMillis();
		
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF"))
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				//mm1 = modelDeclaration.getData();
				//System.out.println("MetaModel: "+mm1);
			}
			
			//OperationList declaredOperations = module.getDeclaredOperations();
			//Operation operation = declaredOperations.get(0);
			//ArrayList<Object> parameters = new ArrayList<Object>();
			//Object result = operation.execute(null, parameters, module.getContext());
			//System.out.println(result);
			
			
			
					
			//List<IModel> mm = ((EtlModule) module).getContext().getModelRepository().getModels();
			//IModel sourceMM = ((EtlModule) module).getContext().getModelRepository().getModels().get(0);
			//IModel targetMM = ((EtlModule) module).getContext().getModelRepository().getModels().get(1);
			
			//System.out.println(sourceMM.getName());
			//System.out.println(targetMM.getName());
			
			//mm1 = modelDeclaration.getMetamodel();
			//System.out.println("Model: "+mm1.keySet());
			
			staticAnalyser.validate(module);
			
			//total=calculate.calculateMTChain(module);
			//System.out.println(v);
			//List<TransformationRule> rules = ((EtlModule) module).getTransformationRules();
			//System.out.println(rules.get(0).getBody());
			
			//for(TransformationRule rule:rules)
			//{
				//EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(0).getSourceParameter());
				//System.err.println(type.getTypeName());
				//ruleblock = rule.getBody();
				//EolType name = staticAnalyser.getResolvedType(rule.getSourceParameter().getTypeExpression());
				//System.out.println(ruleblock);
				//System.out.println(type.getTypeName());
			//}
			
			//ExecutableBlock<Void> ruleblock;
			
			
//			String statementName, expressionName;
//			int numberofexpression;
//			for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
//			{
//				EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
//				//System.err.println(type);
//				//EolModelElementType modelname = (EolModelElementType) staticAnalyser.getResolvedType(((EtlModule) module).getTransformationRules().get(i));
//				//MetaClass mc = type.getMetaClass();
//				//System.out.println(modelname);
//				for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
//				{
//					EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
//					System.out.println(type.getTypeName()+" to "+type1.getTypeName()+"\n");
//					
//				}
//				int c=0;
//				StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
//				for(int k=0;k<ruleblock.getStatements().size();k++)
//				{
//					//for(int m=0;m<ruleblock.getStatements().get(i).getChildren().size();m++)
//					//{
//						//System.out.println(ruleblock.getStatements().get(k).toString().split(" ")[0]+"\n"+ruleblock.getStatements().get(k).getChildren().get(m).toString()+"\n");
//					//}
//					//String statement=ruleblock.getStatements().get(k).toString() - ruleblock.getStatements().get(k).getModule().toString();
//					statementName=ruleblock.getStatements().get(k).toString().split(" ")[0];
//					expressionName=ruleblock.getStatements().get(k).getChildren().toString();
//					numberofexpression=ruleblock.getStatements().get(k).getChildren().size();
//					System.out.println(statementName+"\n"+expressionName);
//					System.out.println("Number of expression: "+numberofexpression+"\n");
//					c++;
//					
//				}
//				System.out.println("The no. of statement(s) used in the transformation rule are "+c);
//				totalstatement=totalstatement+c;
//			}
//			System.out.println("Total statement in the transformation: "+totalstatement);
//				
			
		
			//EolModelElementType type1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(1).getSourceParameter());
			//System.err.println(type1.getTypeName());
			
			//System.out.println("\n");
			long stopTime = System.currentTimeMillis();
			long staticanalysistime = stopTime - startTime;
			//sumtotal = sumtotal + staticanalysistime;
			System.out.println("Static Analysis Took : " + staticanalysistime +" ms");
			//return totalstatement;
			
		}
		//System.out.println("Total static analysis time took "+sumtotal+" ms");
		//return totalstatement;

	}
	
//	public int calculateMTChain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
//	{
//		//module = super.getModule();
//		//super.preExecute();
//		//IEolModule module = null;
//		//module.getContext().setModule(module);
//		String statementName, expressionName;
//		int numberofexpression, totalstatement = 0;
//		if (module instanceof EtlModule)
//		{
//			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//			//staticAnalyser.validate(module);
//		Chaining_MT chainingmt = new Chaining_MT();
//		List<ArrayList<String>> l = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
//		int c=0, max=0;;
//		
//					for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
//					{
//						EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
//						
//						for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
//						{
//							EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
//							System.out.println(type.getTypeName()+" to "+type1.getTypeName()+"\n");
//							
//						}
//						
//						StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
//						c=0;
//						for(int k=0;k<ruleblock.getStatements().size();k++)
//						{
//							statementName=ruleblock.getStatements().get(k).toString().split(" ")[0];
//							expressionName=ruleblock.getStatements().get(k).getChildren().toString();
//							numberofexpression=ruleblock.getStatements().get(k).getChildren().size();
//							System.out.println(statementName+"\n"+expressionName);
//							System.out.println("Number of expression: "+numberofexpression+"\n");
//							c++;
//							
//						}
//						
//						System.out.println("The no. of statements used in the transformation rule are "+c+"\n");
//						totalstatement=totalstatement+c;
//					}
//					
//				//}
//					System.out.println("Total statement in the transformation: "+totalstatement+"\n");
//					//return totalstatement;
//			//}
//			
//			//if(totalstatement>max)
//				//max=totalstatement;
//		//}
//		
//		
//	}
//		return totalstatement;
//	
//	}
	
	/*
	public static IModel getsourceMM()
	{
		IModel sourceMM = null;
		if (module instanceof EtlModule) {
			sourceMM = ((EtlModule) module).getContext().getModelRepository().getModels().get(0);
			
		}
		return sourceMM;
	}
	public static IModel gettargetMM()
	{
		IModel targetMM = null;
		if (module instanceof EtlModule) {
			targetMM = ((EtlModule) module).getContext().getModelRepository().getModels().get(1);
			
		}
		return targetMM;
	}*/
}
