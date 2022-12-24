package org.eclipse.epsilon.etl.chain.selection;

import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;
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
			
			
			
			staticAnalyser.validate(module);
			
		
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
