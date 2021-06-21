

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Collector;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.IEtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;


public class Chaining_MT {
	
	//StringProperties sourceProperties, targetProperties;
	Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
	Path modelsRoot = root.getParent().resolve("models");
	Path scriptRoot = root.getParent().resolve("script");
	Path metamodelsRoot = root.getParent().resolve("metamodels");
	Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
	
	String targetmodel= genmodelsRoot.resolve("Gen_Graph10.xmi").toString();
	//String newtargetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toString();
	
	File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
	String contents[] = metamodelPath.list();
	
	File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
	String scriptcontents[] = scriptPath.list();
	
	ModelProperties modelProperties = new ModelProperties();
	
	StringProperties sourceProperties, targetProperties;
	Path etlscript;
	public void chainMT(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		
		int k;
		etlscript=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		
		System.out.println(sourceMM.substring(59)+" -> "+targetMM.substring(59));
		
		
		for(int i=0; i<contents.length; i++) 
		{
			
				
			if(((metamodelPath+"\\"+contents[i]).equals(sourceMM)))
			{
				
				sourceProperties = modelProperties.properties(sourceMM.substring(59).replaceFirst("[.][^.]+$", ""), sourceMM, sourceModel,"true","false");
				
				
				targetProperties = modelProperties.properties(targetMM.substring(59).replaceFirst("[.][^.]+$", ""), targetMM, targetModel,"false","true");
				
			}
				
			else
			{
				
				continue;
			}
	
		}
	
	}
	
	
	
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
		
		Path etlscript1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		if(etlscript1.toFile().exists())
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(59));
			modelsuse0.add(targetMM.substring(59));
			//System.out.println(modelsuse0);
			//System.out.println("Direct model transformation available.");
			
		}
		
		else {
		
			for(int j=0;j<contents.length;j++) 
			{
				Path etlscript2 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				Path etlscript3 = scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
				
				String intermetamodel = metamodelPath+"\\"+contents[j];
				Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
				
				
					if(etlscript2.toFile().exists())
					{
						identifychain(sourceModel, sourceMM, intermodel, intermetamodel);
						
						
						boolean s1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						boolean s2 = scriptRoot.resolve(intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						
						
						modelsuse1.add(sourceMM.substring(59));
						
						if(s1) {
							modelsuse1.add(intermetamodel.substring(59));
							sourceModel=intermodel;
							sourceMM=intermetamodel;
							
						}
							
						
						if(s2)
							modelsuse1.add(targetMM.substring(59));
					
						
						for (String element : modelsuse1) {
							if (!modelsuse3.contains(element))
								modelsuse3.add(element);
						}
						
					}
				
			}
			
		}

		if(!modelsuse0.isEmpty())
			newmodelsuse.add(modelsuse0);

			for(int k=0;k<modelsuse3.size();k++)
			{
				
				for(int l=k+2;l<modelsuse3.size();l++)
				{
				boolean sc1 = scriptRoot.resolve(modelsuse3.get(k).replaceFirst("[.][^.]+$", "")+"2"+modelsuse3.get(l).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
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
			
			
			if(!modelsuse3.isEmpty() && !modelsuse5.isEmpty())
			{
				newmodelsuse.add(modelsuse3);
				newmodelsuse.add(modelsuse5);
			}
				
			System.out.println(newmodelsuse);
			return newmodelsuse;
		
		
	}
	
	
	public EtlRunConfiguration executeETL(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		etlscript=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		sourceProperties = modelProperties.properties(sourceMM.substring(59).replaceFirst("[.][^.]+$", ""), sourceMM, sourceModel,"true","false");
		targetProperties = modelProperties.properties(targetMM.substring(59).replaceFirst("[.][^.]+$", ""), targetMM, targetModel,"false","true");
		
		String m1 = sourceProperties.getProperty(EmfModel.PROPERTY_MODEL_URI);
		//System.out.println(m1);
		
		
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties)
				.withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15")
				.withProfiling()
				.build();
		
		IEolModule module = runConfig.getModule();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		int total;

		sm1.run();
		
		
		runConfig.dispose();
		return runConfig;
		
		
	}

	
	public int calculateMTChain(IEolModule module) throws Exception
	{
		String statementName, expressionName;
		int numberofexpression, totalstatement = 0;
		if (module instanceof EtlModule)
		{
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			//staticAnalyser.validate(module);
		Chaining_MT chainingmt = new Chaining_MT();
		//List<ArrayList<String>> l = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
		int c=0, max=0;;
		
					for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
					{
						EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
						
						for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
						{
							EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
							System.out.println(type.getTypeName()+" to "+type1.getTypeName()+"\n");
							
						}
						
						StatementBlock ruleblock=(StatementBlock) ((EtlModule) module).getTransformationRules().get(i).getBody().getBody();
						c=0;
						for(int k=0;k<ruleblock.getStatements().size();k++)
						{
							statementName=ruleblock.getStatements().get(k).toString().split(" ")[0];
							expressionName=ruleblock.getStatements().get(k).getChildren().toString();
							numberofexpression=ruleblock.getStatements().get(k).getChildren().size();
							System.out.println(statementName+"\n"+expressionName);
							System.out.println("Number of expression: "+numberofexpression+"\n");
							c++;
							
						}
						
						System.out.println("The no. of statements used in the transformation rule are "+c+"\n");
						totalstatement=totalstatement+c;
					}
					
				//}
					System.out.println("Total statement in the transformation: "+totalstatement+"\n");
		
	}
		return totalstatement;
	
	}
	
	
}
