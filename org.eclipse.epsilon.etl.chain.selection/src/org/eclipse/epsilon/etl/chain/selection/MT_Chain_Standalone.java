package org.eclipse.epsilon.etl.chain.selection;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.staticanalyser.IModelFactory;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class MT_Chain_Standalone {
	
	public static void main(String[] args) throws Exception {
		//Path root = Paths.get(EtlStaticAnalysisStandaloneExample.class.getResource("").toURI());
		//Path root = new File(System.getProperty("/")).toPath();
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		EtlModule module = new EtlModule();
		
		
		//File directoryPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		
		//String contents[] = directoryPath.list();
		
		File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
		String scriptcontents[] = scriptPath.list();
		//System.out.println(scriptcontents[0]);
		//System.out.println(scriptPath+"\\"+scriptcontents[0]);;
		//File sc=new File(scriptcontents[0]);
		//System.out.println(sc.getAbsolutePath());
		//Scanner sourceMM = new Scanner(new File("sourceMM"));
		//Scanner sourceModel = new Scanner(new File("sourceModel"));
		//Scanner targetMM = new Scanner(new File("targetMM"));
		
		String sourceMM = modelsRoot.resolve("Tree.ecore").toAbsolutePath().toUri().toString();
		String interTargetMM = modelsRoot.resolve("Graph.ecore").toAbsolutePath().toUri().toString();
		String targetMM = modelsRoot.resolve("SimpleTrace.ecore").toAbsolutePath().toUri().toString();
		
		//System.out.println(modelsRoot.resolve("SimpleTrace.ecore").toAbsolutePath());
		//System.out.println(targetMM);
		//RunETL runETL = new RunETL();
		
		StringProperties sourceProperties = new StringProperties();
		sourceProperties.setProperty(EmfModel.PROPERTY_NAME, "Tree");
		sourceProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, sourceMM);
		sourceProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
			modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString()
		);
		sourceProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
		sourceProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "false");
		
		String s = sourceProperties.getProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, sourceMM);
		
		StringProperties targetProperties = new StringProperties();
		targetProperties.setProperty(EmfModel.PROPERTY_NAME, "Graph");
		targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, interTargetMM);
		targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
			genmodelsRoot.resolve("Gen_Graph.xmi").toAbsolutePath().toUri().toString()
		);
		targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "false");
		targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
		
		String inter = targetProperties.getProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, interTargetMM);
		
		StringProperties targetProperties2 = new StringProperties();
		targetProperties2.setProperty(EmfModel.PROPERTY_NAME, "SimpleTrace");
		targetProperties2.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, targetMM);
		targetProperties2.setProperty(EmfModel.PROPERTY_MODEL_URI,
			genmodelsRoot.resolve("Gen_SimpleTrace.xmi").toAbsolutePath().toUri().toString()
		);
		targetProperties2.setProperty(EmfModel.PROPERTY_READONLOAD, "false");
		targetProperties2.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
		
		//String trg = targetProperties2.getProperty(EmfModel.PROPERTY_NAME);
		//System.out.println(trg);
		//EmfModel emfmodel;
		//if(emfmodel
		
		
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
			.withScript(scriptRoot.resolve("Tree2Graph.etl"))
			.withModel(new EmfModel(), sourceProperties)
			.withModel(new EmfModel(), targetProperties)
			.withParameter("parameterPassedFromJava", "Hello from pre")
			.withProfiling()
			.build();
		
		EtlPreExecuteConfiguration sm = new EtlPreExecuteConfiguration(runConfig);
		sm.run();
		
		//sm.calculateMTChain(modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString(), sourceMM, genmodelsRoot.resolve("Gen_SimpleTrace.xmi").toAbsolutePath().toUri().toString(), targetMM);
		
		EtlRunConfiguration runConfig2 = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve("Graph2SimpleTrace.etl"))
				.withModel(new EmfModel(), targetProperties)
				.withModel(new EmfModel(), targetProperties2)
				.withParameter("parameterPassedFromJava", "Hello from pre2")
				.withProfiling()
				.build();
			
		EtlPreExecuteConfiguration sm2 = new EtlPreExecuteConfiguration(runConfig2);
		
		sm2.run();
			
		//sm2.calculateMTChain(modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString(), sourceMM, genmodelsRoot.resolve("Gen_SimpleTrace.xmi").toAbsolutePath().toUri().toString(), targetMM);
		
		runConfig.dispose();	
		runConfig2.dispose();

	}
	
	public static void calculateMTChain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		IEolModule module = null;
		//module.getContext().setModule(module);
		if (module instanceof EtlModule)
		{
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		
		Chaining_MT chainingmt = new Chaining_MT();
		List<ArrayList<String>> l = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
		int c=0, max=0;;
		String statementName, expressionName;
		int numberofexpression, totalstatement = 0;
//		for(int m=0;m<l.size();m++)
//		{
//			
//			System.out.println("Chain"+(m+1)+" "+l.get(m)+"\n");
//			for(int n=0;n<l.get(m).size();n++)
//			{
//				
//				if(n+1<l.get(m).size())
//				{
//					System.out.println(l.get(m).get(n)+" -> "+l.get(m).get(n+1));
					for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
					{
						EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
						
						for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
						{
							EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
							System.out.println(type.getTypeName()+" to "+type1.getTypeName()+"\n");
							
						}
						
						StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
						for(int k=0;k<ruleblock.getStatements().size();k++)
						{
							statementName=ruleblock.getStatements().get(k).toString().split(" ")[0];
							expressionName=ruleblock.getStatements().get(k).getChildren().toString();
							numberofexpression=ruleblock.getStatements().get(k).getChildren().size();
							System.out.println(statementName+"\n"+expressionName);
							System.out.println("Number of expression: "+numberofexpression+"\n");
							c++;
						}
						System.out.println("The no. of statements used in the transformation are "+c);
						totalstatement=totalstatement+c;
					}
					
				//}
					System.out.println(totalstatement);
			//}
			
			//if(totalstatement>max)
				//max=totalstatement;
		//}
		
		
	}
	
	}

}
