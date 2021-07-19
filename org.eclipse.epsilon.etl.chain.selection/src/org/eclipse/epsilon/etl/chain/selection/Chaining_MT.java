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
	public void chainMT(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception {
		System.out.println(sourceMM.substring(11)+" -> "+targetMM.substring(11));

	}

	
	public List<ArrayList<String>> identifyChain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		List<String> modelsuse4 = new ArrayList<String>();
		List<String> modelsuse40 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();
		
		boolean etl1 = findETL(sourceMM, targetMM);
		if(etl1)
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(11));
			modelsuse0.add(targetMM.substring(11));
			
		}
		
		else {
			
			for(int j=0;j<contents.length;j++) 
			{
				String intermetamodel = metamodelPath+"/"+contents[j];
				Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
					if(findETL(sourceMM, intermetamodel))
					{	
						identifyChain(sourceModel, sourceMM, intermodel, intermetamodel);
						
						boolean s1 = findETL(sourceMM, intermetamodel);
						boolean s2 = findETL(intermetamodel, targetMM);
						
						modelsuse1.add(sourceMM.substring(11));
						
						if(s1) {
							modelsuse1.add(intermetamodel.substring(11));
							sourceModel=intermodel;
							sourceMM=intermetamodel;
							
						}
					
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
	{etlscript=scriptRoot.resolve(identifyETL(metamodelPath+"/"+sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), metamodelPath+"/"+targetMM.substring(11).replaceFirst("[.][^.]+$", "")));
		
		sourceProperties = modelProperties.properties(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), sourceMM, sourceModel,"true","false");
		targetProperties = modelProperties.properties(targetMM.substring(11).replaceFirst("[.][^.]+$", ""), targetMM, targetModel,"false","true");
		
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties)
				.withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15")
				.withProfiling()
				.build();
		
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		
		sm1.run();
		
		runConfig.dispose();
		return runConfig;
	
	}
	
	
	public int calculateMTChain(IEolModule module) throws Exception
	{
		String statementName, expressionName;
		int numberofexpression = 0,  totalstatement = 0;
		int totalfeatures = 0,  totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		
		if (module instanceof EtlModule)
		{
		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
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
			for(int k=0;k<ruleblock.getStatements().size();k++)
			{
				sumofoperation = 0;
				stName = ruleblock.getStatements().get(k);
				statementName=stName.toString();
				expName = stName.getChildren();
				expressionName=expName.toString();
				numberofexpression=expName.size();
				c++;
				System.out.println("Statement number "+c);
				System.out.println(statementName+"\n"+expressionName+"\n");
				for(int l=0;l<numberofexpression;l++)
				{
					opName=expName.get(l).getChildren();
					if(!opName.isEmpty())
					{
						totexpName = calculateExpressions(opName);
						for(int m=0;m<totexpName.size();m++)
						{
							totexpSize = totexpName.get(m).size();
							System.out.println(totexpName.get(m));
							sumofoperation=sumofoperation+totexpSize;
							System.out.println(totexpSize);
						}
						
					}
						

				}
				System.out.println("Number of expressions and operations: "+sumofoperation+"\n");
				totalfeatures=totalfeatures+sumofoperation;
			}
			totalstatement=totalstatement+totalfeatures;
			for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
			{
				EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
				System.out.println("Total expressions/operators used in the transformation rule "+type.getTypeName()+" to "+type1.getTypeName()+":"+totalstatement+"\n");
			}
		totalstructuratlfeatures=totalstructuratlfeatures+totalstatement;
		}
	}
	return totalstructuratlfeatures;
	
	}
	

public List<List<ModuleElement>> calculateExpressions(List<ModuleElement> expName) {

	List<ModuleElement> opName = null;
	List<List<ModuleElement>> op = new ArrayList<List<ModuleElement>>();
		for(int i=0;i<expName.size();i++)
		{
			opName = expName.get(i).getChildren();
				if(opName.isEmpty())
				{
					op.add(expName);
					return op;
				}
				else
				{
					calculateExpressions(opName);
					op.add(opName);
				}
				
			}
		return op;
	}

public ArrayList<String> identifyBestChain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
{
	List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		int min=99999;
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		
		for(int i=0;i<l.size();i++)
		{
			System.out.println("Chain"+(i+1)+" "+l.get(i)+"\n");
			int total=0;
			for(int j=0;j<l.get(i).size();j++)
			{
				
				EtlModule module1 = new EtlModule();
				
				if(j+1<l.get(i).size())
				{
				
					System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1)+"\n");
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
				
				Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
				
				Path newtargetmodelpath = modelsRoot.resolve(index.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
				
				executeETL(newsourcemodel, metamodelPath+"/"+index.get(k), newtargetmodel, metamodelPath+"/"+index.get(k+1));
				
			}
			
		}
		return index;

}

public boolean findETL(String sourceMM, String targetMM) throws Exception
{
	String sourceMetamodel = null, targetMetamodel = null;
	List<ModelDeclaration> mm;
	boolean flag = false;
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
					flag=true;
					break;
				}
				
	} 
	return flag;
	
	}


public String identifyETL(String sourceMM, String targetMM) throws Exception
{
	String etlname = null;
	String sourceMetamodel = null, targetMetamodel = null;
	List<ModelDeclaration> mm;
	
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
					break;

				}

	} 
	return etlname;
	}


//-----------repeated codes for EtlChainOptimiser---------------

public int calculateMTChain1(IEolModule module) throws Exception
{
	@SuppressWarnings("unused")
	String statementName, expressionName ;
	int numberofexpression = 0, totalstatement = 0;
	int totalfeatures = 0, totalstructuratlfeatures = 0;
	List<ModuleElement> opName, expName;
	List<List<ModuleElement>> totexpName = null;
	Statement stName;
	
	if (module instanceof EtlModule)
	{
	EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
	for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

		if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

			staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

		}

	}
	staticAnalyser.validate(module);
	
	for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
	{
		staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
					
		for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
		{
			staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
						
		}
					
		StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
		int sumofoperation, totexpSize = 0;
		totalstatement=0;
		totalfeatures=0;
		for(int k=0;k<ruleblock.getStatements().size();k++)
		{
			sumofoperation = 0;
			stName = ruleblock.getStatements().get(k);
			statementName=stName.toString();
			expName = stName.getChildren();
			expressionName=expName.toString();
			numberofexpression=expName.size();
			for(int l=0;l<numberofexpression;l++)
			{
				opName=expName.get(l).getChildren();
				if(!opName.isEmpty())
				{
					totexpName = calculateExpressions(opName);
					for(int m=0;m<totexpName.size();m++)
					{
						totexpSize = totexpName.get(m).size();
						sumofoperation=sumofoperation+totexpSize;
					}
					
				}
					

			}
			totalfeatures=totalfeatures+sumofoperation;
		}
		totalstatement=totalstatement+totalfeatures;
		for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
		{
			staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
		}
	totalstructuratlfeatures=totalstructuratlfeatures+totalstatement;
	}
}
return totalstructuratlfeatures;

}

public ArrayList<String> identifybestchain1(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
{
	List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		int min=9999;
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		
		for(int i=0;i<l.size();i++)
		{
			int total=0;
			for(int j=0;j<l.get(i).size();j++)
			{
				
				EtlModule module1 = new EtlModule();
				
				if(j+1<l.get(i).size())
				{
				
					System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1)+"\n");
					
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
		return index;

}

public ArrayList<String> identifyBestChain2(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
{
	List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
	
		int min=99999;
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		
		for(int i=0;i<l.size();i++)
		{
			System.out.println("Chain"+(i+1)+" "+l.get(i)+"\n");
			int total=0;
			for(int j=0;j<l.get(i).size();j++)
			{
				
				EtlModule module1 = new EtlModule();
				
				if(j+1<l.get(i).size())
				{
				
					System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1)+"\n");
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
		return index;

}


}
