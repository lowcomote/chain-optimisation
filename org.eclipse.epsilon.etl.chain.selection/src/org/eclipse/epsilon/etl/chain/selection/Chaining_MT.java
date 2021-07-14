package org.eclipse.epsilon.etl.chain.selection;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;


public class Chaining_MT {

	Path modelsRoot = Paths.get("models");
	Path metamodelsRoot = Paths.get("metamodels");
	Path scriptRoot = Paths.get("scripts");
	Path genmodelsRoot = Paths.get("models/generatedmodels");
	
	File metamodelPath = new File("metamodels");
	String contents[] = metamodelPath.list();
	
	File scriptPath = new File("scripts");
	String scriptcontents[] = scriptPath.list();
	
	ModelProperties modelProperties = new ModelProperties();
	
	StringProperties sourceProperties, targetProperties;
	Path etlscript;
	public void chainMT(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		
		int k;
		//etlscript=scriptRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")+".etl");
		
		System.out.println(sourceMM.substring(11)+" -> "+targetMM.substring(11));

	}
	
//	public List<ArrayList<String>> identifyMT(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
//	{
//		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
//		ArrayList<String> modelsuse0 = new ArrayList<String>();
//		Path etlscript1 = scriptRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")+".etl");
//		if(etlscript1.toFile().exists())
//		{
//			chainMT(sourceModel, sourceMM, targetModel, targetMM);
//			
//			modelsuse0.add(sourceMM.substring(11));
//			modelsuse0.add(targetMM.substring(11));
//			newmodelsuse.add(modelsuse0);
//			//System.out.println(newmodelsuse);
//			return newmodelsuse;
//		}
//		return null;
//	}
	
	public List<ArrayList<String>> identifychain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> newmodelsuse1 = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse2 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		List<String> modelsuse4 = new ArrayList<String>();
		List<String> modelsuse40 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();
		
		boolean etl1 = findETL(sourceMM, targetMM);
//		//if(!etl1.isEmpty())
//		System.out.println(etl1);
//		//Path etlscript1 = scriptRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")+".etl");
//		Path etlscript1 = scriptRoot.resolve(findETL(sourceMM, targetMM));
//		System.out.println("fhgjhf");
		//if(etlscript1.toFile().exists())
		if(etl1)
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(11));
			modelsuse0.add(targetMM.substring(11));
			
		}
		
		else {
			
			for(int j=0;j<contents.length;j++) 
			{
				//Path etlscript2 = scriptRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				//Path etlscript3 = scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")+".etl");
				
				String intermetamodel = metamodelPath+"/"+contents[j];
				Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
			
//				System.out.println("1"+sourceMM);
////			System.out.println(contents[j]);
//				System.out.println("2"+intermetamodel);
//				System.out.println(findETL(sourceMM, intermetamodel));
					//if(etlscript2.toFile().exists())
					if(findETL(sourceMM, intermetamodel))
					{	
//						System.out.println("123");
						identifychain(sourceModel, sourceMM, intermodel, intermetamodel);
						
						//boolean s1 = scriptRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+intermetamodel.substring(11).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						boolean s1 = findETL(sourceMM, intermetamodel);
						//boolean s2 = scriptRoot.resolve(intermetamodel.substring(11).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						boolean s2 = findETL(intermetamodel, targetMM);
						
						modelsuse1.add(sourceMM.substring(11));
						
						if(s1) {
							modelsuse1.add(intermetamodel.substring(11));
							sourceModel=intermodel;
							sourceMM=intermetamodel;
							
						}
					
						//chainMT(sourceModel, sourceMM, targetModel, targetMM);
						if(s2)
							modelsuse1.add(targetMM.substring(11));
	
					}
			
					for (String element : modelsuse1) {
						if (!modelsuse3.contains(element))
							modelsuse3.add(element);
					}
			}

			
		}

		if(!modelsuse0.isEmpty())
			newmodelsuse.add(modelsuse0);

			for(int k=0;k<modelsuse3.size();k++)
			{
				
				for(int l=k+2;l<modelsuse3.size();l++)
				{
				//boolean sc1 = scriptRoot.resolve(modelsuse3.get(k).replaceFirst("[.][^.]+$", "")+"2"+modelsuse3.get(l).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
				//System.out.println(modelsuse3.get(k));
				boolean sc1 = findETL(metamodelPath+"/"+modelsuse3.get(k), metamodelPath+"/"+modelsuse3.get(l));
				if(sc1)
				{
					modelsuse40=modelsuse3.subList(0, l-1);
					modelsuse4=modelsuse3.subList(l,modelsuse3.size());
					
					for (String element : modelsuse40) {
						if (!modelsuse5.contains(element))
							modelsuse5.add(element);
					}
					for (String element : modelsuse4) {
						if (!modelsuse5.contains(element))
							modelsuse5.add(element);
					}
					//System.out.println(modelsuse5);
					
				}
				
			}
				
		
			}
			
			
			if(!modelsuse3.isEmpty())
				newmodelsuse.add(modelsuse3);
				
			
			if(!modelsuse5.isEmpty())
				newmodelsuse.add(modelsuse5);
			
			System.out.println(newmodelsuse);
			return newmodelsuse;
	
	}
	
	
	public EtlRunConfiguration executeETL(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
//		etlscript=scriptRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")+".etl");
		etlscript=scriptRoot.resolve(identifyETL(metamodelPath+"/"+sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), metamodelPath+"/"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")));
		
		sourceProperties = modelProperties.properties(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), sourceMM, sourceModel,"true","false");
		targetProperties = modelProperties.properties(targetMM.substring(11).replaceFirst("[.][^.]+$", ""), targetMM, targetModel,"false","true");
		
		String m1 = sourceProperties.getProperty(EmfModel.PROPERTY_MODEL_URI);
		//System.out.println(m1);
		
		
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties)
				.withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15")
				.withProfiling()
				.build();
		
		//IEolModule module = runConfig.getModule();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		
		sm1.run();
		
		runConfig.dispose();
		return runConfig;
	
	}
	
	
	public int calculateMTChain(IEolModule module) throws Exception
	{
		String statementName, expressionName, operationName = null;
		int numberofexpression = 0, numberofoperation = 0, totalstatement = 0;
		int totalfeatures = 0, totalexpressionandoperation, totalstructuratlfeatures = 0;
//		int sumofoperation = 0;
		String newOp = null;
		List<ModuleElement> opName, expName;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		
		if (module instanceof EtlModule)
		{
		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			
		//Chaining_MT chainingmt = new Chaining_MT();
		//List<ArrayList<String>> l = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
		int max=0;
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

			}

		}
		staticAnalyser.validate(module);
		
		for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
		{
			EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
						
			for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
			{
				EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
				System.out.println("Transformation rule"+(i+1)+": "+type.getTypeName()+" to "+type1.getTypeName()+"\n");
							
			}
						
			StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
			int c=0;
			int sumofoperation, totexpSize = 0;
			totalstatement=0;
			totalfeatures=0;
			//int sumofoperation = 0;
			for(int k=0;k<ruleblock.getStatements().size();k++)
			{
				sumofoperation = 0;
				//statementName=ruleblock.getStatements().get(k).toString().split(" ")[0];
				stName = ruleblock.getStatements().get(k);
				statementName=stName.toString();
				expName = stName.getChildren();
				expressionName=expName.toString();
				numberofexpression=expName.size();
				c++;
				System.out.println("Statement number "+c);
				System.out.println(statementName+"\n"+expressionName+"\n");
				
				//calculateExpressions(expName);
				for(int l=0;l<numberofexpression;l++)
				{
					opName=expName.get(l).getChildren();
//					for(int m=0;m<opName.size();m++)
//					{
					if(!opName.isEmpty())
					{
						totexpName = calculateExpressions(opName);
//						System.out.println(totexpName);
						for(int m=0;m<totexpName.size();m++)
						{
							totexpSize = totexpName.get(m).size();
							System.out.println(totexpName.get(m));
							sumofoperation=sumofoperation+totexpSize;
							System.out.println(totexpSize);
						}
						
						//totexpSize++;
						
					}
						

				}
	
				//System.out.println(newOp);
				System.out.println("Number of expressions and operations: "+sumofoperation+"\n");
				
				
				//System.out.println("Number of expression: "+numberofexpression+"\n");
				
				
				//totalexpressionandoperation = numberofexpression+numberofoperation;
				totalfeatures=totalfeatures+sumofoperation;
				//System.out.println("Total no. of expression and operation: "+totalexpressionandoperation+"\n");
			}
						
			//System.out.println("The no. of statements used in the transformation rule are "+c+"\n");
			//totalstatement=totalstatement+sumofoperation;
			totalstatement=totalstatement+totalfeatures;
			//System.out.println("Total statements and expressions in the transformation: "+totalstatement+"\n");
			//System.out.println(totalfeatures);
					
				//}
			for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
			{
				EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
				System.out.println("Total expressions/operators used in the transformation rule "+type.getTypeName()+" to "+type1.getTypeName()+":"+totalstatement+"\n");
			}
				//return totalstatement;
			//}
			
			//if(totalstatement>max)
				//max=totalstatement;
		//}
		totalstructuratlfeatures=totalstructuratlfeatures+totalstatement;
		}
		//return totalstatement;
	}
	return totalstructuratlfeatures;
	
	}
	

public List<List<ModuleElement>> calculateExpressions(List<ModuleElement> expName) {
		
	int c = 0;
	List<ModuleElement> opName = null;
	List<List<ModuleElement>> op = new ArrayList<List<ModuleElement>>();
		for(int i=0;i<expName.size();i++)
		{
			opName = expName.get(i).getChildren();
		//}
//			for(int j=0;j<opName.size();j++)
//			{
				if(opName.isEmpty())
				{
					op.add(expName);
					//System.out.println(expName);
					//i++;
					//return expName;
					return op;
					//continue;
					//break x;
				}
				else
				{
					c++;
					//for(int j=0;j<opName.size();j++)
					calculateExpressions(opName);
//					for(int j=0;j<opName.size();j++)
//						calculateExpressions(opName.get(j).getChildren());
					op.add(opName);
					//return expName;
//					for(int j=0;j<opName.size();j++)
//					System.out.println("123 "+expName.get(j));
					//return opName;
					//System.out.println(opName);
					//return op;
				}
				
			}
//		System.out.println("123 "+op);
//		System.out.println("count "+c);
			//return opName;
		//}
		//return expName;
		//return opName;
		return op;
		//return null;
	}

public ArrayList<String> identifybestchain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
{
	
	//List<ArrayList<String>> l1 = chainingmt.identifyMT(sourcemodel, sourceMM, targetmodel, targetMM);
	List<ArrayList<String>> l = identifychain(sourceModel, sourceMM, targetModel, targetMM);
	
		EtlRunConfiguration exec=null;
//		IEolModule module = null;
		int min=99999;
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		
		for(int i=0;i<l.size();i++)
		{
			//int[] sum = new int[l.size()];
			System.out.println("Chain"+(i+1)+" "+l.get(i)+"\n");
			//sum[i]=0;
			
//			chainingmt.calculateMTChain(sourcemodel, sourceMM, targetmodel, targetMM);
			//System.out.println(l.get(i).get(0));
			int total=0;
			int tot1=0;
			for(int j=0;j<l.get(i).size();j++)
			{
				
				EtlModule module1 = new EtlModule();
				
				if(j+1<l.get(i).size())
				{
				
					System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1)+"\n");
					
					//module1.parse(scriptRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")+"2"+l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")+".etl"));
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath+"/"+l.get(i).get(j), metamodelPath+"/"+l.get(i).get(j+1))));
					
					total = calculateMTChain(module1);
					sum[i]=sum[i]+total;

					System.out.println("Total expressions/operators used in the transformation "+l.get(i).get(j)+" -> "+l.get(i).get(j+1)+": "+total+"\n");
				}
				
		
			}
			if(sum[i]<min)
			{
				min=sum[i];
				index=l.get(i);
			}
			
			System.out.println("Total expressions/operators used in the chain: "+sum[i]);
			System.out.println("---------------------------------------------------------\n");
			
		}
		
		System.out.println("\nMT Chain "+index+" has minimum structural features of " + min);
		System.out.println("------------------Executing best chain--------------------");
		for(int k=0;k<index.size();k++)
		{
			if(k+1<index.size())
			{
				
				//System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
				
				Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
				
				Path newtargetmodelpath = modelsRoot.resolve(index.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
				
				exec = executeETL(newsourcemodel, metamodelPath+"/"+index.get(k), newtargetmodel, metamodelPath+"/"+index.get(k+1));
				
			}
			
		}
		return index;

}

public boolean findETL(String sourceMM, String targetMM) throws Exception
{
	String etlname = null;
	String sourceMetamodel = null, targetMetamodel = null;
	List<ModelDeclaration> mm;
	//String x = null;
	boolean flag = false;;
	
//	EtlModule module = new EtlModule();
//	System.out.println(sourceMM);
//	System.out.println(targetMM);
	for(int i=0;i<scriptcontents.length;i++)
	{
		EtlModule module = new EtlModule();
		module.parse(scriptRoot.resolve(scriptcontents[i]));
		module.getContext().setModule(module);
//		System.out.println("123");
		//if (module instanceof EtlModule) {
//			System.out.println("abc");
//			System.out.println(scriptcontents[i]);
//			System.out.println(module);
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			//module.parse(scriptcontents[i]);
			
			
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//				System.out.println(modelDeclaration);	
					}
				}
				staticAnalyser.validate(module);
				mm = ((EtlModule) module).getDeclaredModelDeclarations();
				//mm = modelDeclaration.getModel().getName();
				//String[] input = mm.split("\n"); 
				//System.out.println(input[0]);
				
				
//			for (ModelDeclaration modelDeclaration1 : mm)
//			{
//				x = modelDeclaration1.getModel().getName();
//				System.out.println(x);
//			}
//				System.out.println(mm.get(0));
				
				sourceMetamodel = mm.get(0).getModel().getName();
				targetMetamodel = mm.get(1).getModel().getName();
				
				
//				System.out.println(sourceMetamodel);
//				System.out.println(targetMetamodel);
////				System.out.println(sourceMM.substring(11));
				//System.out.println(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""));
//				System.out.println(targetMM.substring(11).replaceFirst("[.][^.]+$", ""));
				//System.out.println(i+" "+sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel));
				if(sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel) && targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel))
				{
//					System.out.println("gfjhgfj");
//					etlname = ((EtlModule) module).getSourceFile().getName();
//					System.out.println(etlname);
					//return etlname;
					flag=true;
					//return true;
					break;
				}
				
				
//				else
//					return false;
					
				//System.out.println(i);
//				else
//				{
//					System.out.println(i);
////					System.out.println(scriptcontents.length-1);
//					if(i==(scriptcontents.length-1))
//						return false;
//				}
				
	} 
	return flag;
	//return flag;
	
	}


public String identifyETL(String sourceMM, String targetMM) throws Exception
{
	String etlname = null;
	String sourceMetamodel = null, targetMetamodel = null;
	List<ModelDeclaration> mm;
	//String x = null;
	//boolean flag = false;
	

	
	for(int i=0;i<scriptcontents.length;i++)
	{
		EtlModule module = new EtlModule();
		module.parse(scriptRoot.resolve(scriptcontents[i]));
		module.getContext().setModule(module);

			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			
			
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
	
					}
				}
				staticAnalyser.validate(module);
				mm = ((EtlModule) module).getDeclaredModelDeclarations();
	
				
				sourceMetamodel = mm.get(0).getModel().getName();
				targetMetamodel = mm.get(1).getModel().getName();
				
				if(sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel) && targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel))
				{
					etlname = ((EtlModule) module).getSourceFile().getName();
//					System.out.println(etlname);
					//return etlname;
					break;
					//return true;

				}
				
//				
//				else
//					return null;
					

	} 
	return etlname;
	//return flag;
	
	}


//-----------repeated codes for EtlChainOptimiser---------------

public int calculateMTChain1(IEolModule module) throws Exception
{
	String statementName, expressionName, operationName = null;
	int numberofexpression = 0, numberofoperation = 0, totalstatement = 0;
	int totalfeatures = 0, totalexpressionandoperation, totalstructuratlfeatures = 0;
//	int sumofoperation = 0;
	String newOp = null;
	List<ModuleElement> opName, expName;
	List<List<ModuleElement>> totexpName = null;
	Statement stName;
	
	if (module instanceof EtlModule)
	{
	EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		
	//Chaining_MT chainingmt = new Chaining_MT();
	//List<ArrayList<String>> l = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
	int max=0;
	for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

		if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

			staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

		}

	}
	staticAnalyser.validate(module);
	
	for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
	{
		EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
					
		for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
		{
			EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
//			System.out.println("Transformation rule"+(i+1)+": "+type.getTypeName()+" to "+type1.getTypeName()+"\n");
						
		}
					
		StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
		int c=0;
		int sumofoperation, totexpSize = 0;
		totalstatement=0;
		totalfeatures=0;
		//int sumofoperation = 0;
		for(int k=0;k<ruleblock.getStatements().size();k++)
		{
			sumofoperation = 0;
			//statementName=ruleblock.getStatements().get(k).toString().split(" ")[0];
			stName = ruleblock.getStatements().get(k);
			statementName=stName.toString();
			expName = stName.getChildren();
			expressionName=expName.toString();
			numberofexpression=expName.size();
			c++;
//			System.out.println("Statement number "+c);
//			System.out.println(statementName+"\n"+expressionName+"\n");
			
			//calculateExpressions(expName);
			for(int l=0;l<numberofexpression;l++)
			{
				opName=expName.get(l).getChildren();
//				for(int m=0;m<opName.size();m++)
//				{
				if(!opName.isEmpty())
				{
					totexpName = calculateExpressions(opName);
//					System.out.println(totexpName);
					for(int m=0;m<totexpName.size();m++)
					{
						totexpSize = totexpName.get(m).size();
//						System.out.println(totexpName.get(m));
						sumofoperation=sumofoperation+totexpSize;
//						System.out.println(totexpSize);
					}
					
					//totexpSize++;
					
				}
					

			}

			//System.out.println(newOp);
//			System.out.println("Number of expressions and operations: "+sumofoperation+"\n");
			
			
			//System.out.println("Number of expression: "+numberofexpression+"\n");
			
			
			//totalexpressionandoperation = numberofexpression+numberofoperation;
			totalfeatures=totalfeatures+sumofoperation;
			//System.out.println("Total no. of expression and operation: "+totalexpressionandoperation+"\n");
		}
					
		//System.out.println("The no. of statements used in the transformation rule are "+c+"\n");
		//totalstatement=totalstatement+sumofoperation;
		totalstatement=totalstatement+totalfeatures;
		//System.out.println("Total statements and expressions in the transformation: "+totalstatement+"\n");
		//System.out.println(totalfeatures);
				
			//}
		for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
		{
			EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
//			System.out.println("Total operators used in the transformation rule "+type.getTypeName()+" to "+type1.getTypeName()+":"+totalstatement+"\n");
		}
			//return totalstatement;
		//}
		
		//if(totalstatement>max)
			//max=totalstatement;
	//}
	totalstructuratlfeatures=totalstructuratlfeatures+totalstatement;
	}
	//return totalstatement;
}
return totalstructuratlfeatures;

}

public ArrayList<String> identifybestchain1(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
{
//	System.out.println("123");
	//List<ArrayList<String>> l1 = chainingmt.identifyMT(sourcemodel, sourceMM, targetmodel, targetMM);
	List<ArrayList<String>> l = identifychain(sourceModel, sourceMM, targetModel, targetMM);
	
		EtlRunConfiguration exec=null;
//		IEolModule module = null;
		int min=9999;
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		
		for(int i=0;i<l.size();i++)
		{
			//int[] sum = new int[l.size()];
//			System.out.println("Chain"+(i+1)+" "+l.get(i)+"\n");
			//sum[i]=0;
			
//			chainingmt.calculateMTChain(sourcemodel, sourceMM, targetmodel, targetMM);
			//System.out.println(l.get(i).get(0));
			int total=0;
			int tot1=0;
			for(int j=0;j<l.get(i).size();j++)
			{
				
				EtlModule module1 = new EtlModule();
				
				if(j+1<l.get(i).size())
				{
				
					System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1)+"\n");
					
//					module1.parse(scriptRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")+"2"+l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")+".etl"));
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath+"/"+l.get(i).get(j), metamodelPath+"/"+l.get(i).get(j+1))));
					
					total = calculateMTChain1(module1);
					sum[i]=sum[i]+total;

					System.out.println("Total operators used in the transformation "+l.get(i).get(j)+" -> "+l.get(i).get(j+1)+": "+total+"\n");
				}
				
		
			}
			if(sum[i]<min)
			{
				min=sum[i];
				index=l.get(i);
			}
			
			System.out.println("Total operators used in the chain: "+sum[i]);
			System.out.println("---------------------------------------------------------\n");
			
		}
		
//		System.out.println("\nMT Chain "+index+" has minimum structural features of " + min);
//		System.out.println("------------------Executing best chain--------------------");
//		for(int k=0;k<index.size();k++)
//		{
//			if(k+1<index.size())
//			{
//				
////				System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
//				
//				Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
//				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
//				
//				Path newtargetmodelpath = modelsRoot.resolve(index.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
//				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
//				
//				exec = executeETL(newsourcemodel, metamodelPath+"/"+index.get(k), newtargetmodel, metamodelPath+"/"+index.get(k+1));
//				
//			}
//			
//		}
		return index;

}


}
