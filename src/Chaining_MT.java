

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
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
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
		
		//String sourceMM, targetMM;
		//EmfModel emfmodel1 = new EmfModel();
		//EmfModel emfmodel2 = new EmfModel();
		int k;
		etlscript=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		//List<String> modelsuse = new ArrayList<>();
		//System.out.println(sourceMM.substring(59));
		//System.out.println(sourceMM.substring(59).replaceFirst("[.][^.]+$", ""));
		//System.out.println(metamodelPath+"\\"+contents[18]);
		System.out.println(sourceMM.substring(59)+" -> "+targetMM.substring(59));
		
		//modelsuse.add(sourceMM.substring(59));
		//modelsuse.add(targetMM.substring(59));
		//System.out.println(modelsuse);
		//if(!sourceMM.equals(targetMM))
		//{
		//System.out.println(targetModel);
		//System.out.println(metamodelPath+"\\"+contents[0]);
		for(int i=0; i<contents.length; i++) 
		{
			//for(int j=0;j<contents.length;j++) {
				//sourceMM = modelsRoot.resolve(contents[i]).toString();
				//targetMM = modelsRoot.resolve(contents[j]).toString();
				
				//System.out.println(contents[i]);
			//System.out.println(sourceMM.substring(59)+" -> "+targetMM.substring(59));	
				
			//	Path etlscript=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				
			if(((metamodelPath+"\\"+contents[i]).equals(sourceMM)))
			{
				//k=i;
				//if((metamodelPath+"\\"+contents[k]).equals(targetMM))
				//{
				//	System.out.println(contents[k]+" -> "+contents[i]);
				//}
				//sourceProperties = modelProperties.srcproperties(sourceMM.substring(59).replaceFirst("[.][^.]+$", ""), sourceMM, sourceModel);
				
				//targetProperties = modelProperties.trgproperties(targetMM.substring(59).replaceFirst("[.][^.]+$", ""), targetMM, targetModel);
				sourceProperties = modelProperties.properties(sourceMM.substring(59).replaceFirst("[.][^.]+$", ""), sourceMM, sourceModel,"true","false");
				
				
				targetProperties = modelProperties.properties(targetMM.substring(59).replaceFirst("[.][^.]+$", ""), targetMM, targetModel,"false","true");
				//targetProperties = modelProperties.trgproperties(contents[j].replaceFirst("[.][^.]+$", ""), targetMM, targetmodel);
				
				
				//if(etlscript.toFile().exists())
				//{
				/*
				EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
						.withScript(etlscript)
						.withModel(new EmfModel(), sourceProperties)
						.withModel(new EmfModel(), targetProperties)
						.withParameter("parameterPassedFromJava", "Hello from pre5")
						.withProfiling()
						.build();
				
				EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
				sm1.run();
				//System.out.println("123");
				runConfig.dispose();*/
				//break;
				//}
				//else
				//	continue; 
			}
				
			else
			{
				//String newtargetMM = modelsRoot.resolve(contents[i]).toString();
				//chainMT(sourceModel, sourceMM, newtargetMM);
				//chainMT(targetmodel, newtargetMM, targetMM);
				continue;
			}
			
			
			
			
			//String intermetamodel = metamodelPath+"\\"+contents[i];
			//Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".xmi");
			//String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
			
			//chainMT(intermodel, intermetamodel, targetmodel, targetMM);
			//String newtargetMM = metamodelsRoot.resolve(contents[i]).toString();
			//chainMT(sourceModel, sourceMM, newtargetMM);
			//chainMT(targetmodel, newtargetMM, targetMM);	
		}
		//}
		
		
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
		//for(int i=0; i<contents.length; i++) 
		//{
			
			for(int j=0;j<contents.length;j++) 
			{
				Path etlscript2 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				Path etlscript3 = scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
				
				String intermetamodel = metamodelPath+"\\"+contents[j];
				Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
				
				//if(contents[j]!=targetMM.substring(59))
				//{
					if(etlscript2.toFile().exists())
					{
						identifychain(sourceModel, sourceMM, intermodel, intermetamodel);
						//identifychain(intermodel, intermetamodel, targetModel, targetMM);
						
						boolean s1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						boolean s2 = scriptRoot.resolve(intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						
						//modelsuse1.addAll(modelsuse0);
						modelsuse1.add(sourceMM.substring(59));
						
						if(s1) {
							modelsuse1.add(intermetamodel.substring(59));
							sourceModel=intermodel;
							sourceMM=intermetamodel;
							
						}
							
						
						
						
						//chainMT(sourceModel, sourceMM, targetModel, targetMM);
						if(s2)
							modelsuse1.add(targetMM.substring(59));
						/*
						if(etlscript3.toFile().exists())
						{
							identifychain(intermodel, intermetamodel, targetModel, targetMM);
							modelsuse2.add(sourceMM.substring(59));
							modelsuse2.add(intermetamodel.substring(59));
							modelsuse2.add(targetMM.substring(59));
							System.out.println(intermetamodel.substring(59));
						}
						*/
						
						for (String element : modelsuse1) {
							if (!modelsuse3.contains(element))
								modelsuse3.add(element);
						}
						//System.out.println(modelsuse3);
						//Spliterator<String> chain = modelsuse3.spliterator();
						//chain.forEachRemaining(null);
						
					}
					
					
					/*
					if(etlscript3.toFile().exists())
					{
						//identifychain(sourceModel, sourceMM, intermodel, intermetamodel);
						identifychain(intermodel, intermetamodel, targetModel, targetMM);
						
						//boolean s1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						boolean s2 = scriptRoot.resolve(intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						//modelsuse2.addAll(modelsuse0);
						//modelsuse2.add(sourceMM.substring(59));
						if(s2)
							modelsuse2.add(intermetamodel.substring(59));
						modelsuse2.add(targetMM.substring(59));
						//System.out.println(intermetamodel.substring(59));
					}
					*/
					
				//}
				
				//System.out.println(modelsuse1);
				
			
			}
			//modelsuse3.add(targetMM.substring(59));
		}
		//modelsuse3.add(targetMM.substring(59));
//		for (String element : modelsuse1) {
//			if (!modelsuse3.contains(element))
//				modelsuse3.add(element);
//		}
		
		//modelsuse2.add(targetMM.substring(59));
		//}
		if(!modelsuse0.isEmpty())
			newmodelsuse.add(modelsuse0);
//		if(!modelsuse2.isEmpty())
//			newmodelsuse.add(modelsuse2);
//		if(!modelsuse3.isEmpty())
//			newmodelsuse.add(modelsuse3);
		//System.out.println(newmodelsuse);
		//}		
			//modelsuse4.add(sourceMM.substring(59));
			for(int k=0;k<modelsuse3.size();k++)
			{
				//modelsuse4.add(modelsuse3.get(k));
				for(int l=k+2;l<modelsuse3.size();l++)
				{
				boolean sc1 = scriptRoot.resolve(modelsuse3.get(k).replaceFirst("[.][^.]+$", "")+"2"+modelsuse3.get(l).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
				if(sc1)
				{
					modelsuse40=modelsuse3.subList(0, l-1);
					modelsuse4=modelsuse3.subList(l,modelsuse3.size());
					//modelsuse4.add(modelsuse3.get(k));
					//modelsuse4.add(modelsuse3.get(l));
					//System.out.println(modelsuse40);
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
			
			//System.out.println(modelsuse5);
			if(!modelsuse3.isEmpty() && !modelsuse5.isEmpty())
			{
				newmodelsuse.add(modelsuse3);
				newmodelsuse.add(modelsuse5);
			}
				
			System.out.println(newmodelsuse);
			return newmodelsuse;
		
		
	}
	
	public void findchain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		Path etlscript1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		ArrayList<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse = new ArrayList<String>();
		
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		
		ArrayList<String> modelsuseA = new ArrayList<String>();
		ArrayList<String> modelsuseB = new ArrayList<String>();
		
		if(etlscript1.toFile().exists())
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(59));
			modelsuse0.add(targetMM.substring(59));
			System.out.println(modelsuse0);
			
		}
		
		boolean flag=false;
		
		outerloop:
		for(int i=0; i<contents.length; i++) 
		{
	
			for(int j=0;j<contents.length;j++) {
		
				if(i!=j) {
				String intermetamodel = metamodelPath+"\\"+contents[j];
				Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
				
				String intermetamodel1 = metamodelPath+"\\"+contents[i];
				Path intermodelpath1 = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel1 = intermodelpath1.toAbsolutePath().toUri().toString();
				
				String intermetamodel2 = metamodelPath+"\\"+contents[i];
				Path intermodelpath2 = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel2 = intermodelpath2.toAbsolutePath().toUri().toString();
				
				String intermetamodel3 = metamodelPath+"\\"+contents[j];
				Path intermodelpath3;
				String intermodel3 = null;
				
				Path etlscript2 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				Path etlscript3 = scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
				
				//Path interetlscript0, interetlscript1, interetlscript2, interetlscript3;
				
				if(etlscript2.toFile().exists())
				{
					chainMT(sourceModel, sourceMM, intermodel, intermetamodel);
					modelsuse.add(sourceMM.substring(59));
					if(etlscript3.toFile().exists())
					{
						System.out.println("0");
												
						sourceMM=intermetamodel;
						sourceModel=intermodel;
						
						chainMT(sourceModel, sourceMM, targetModel, targetMM);
						
						modelsuse.add(intermetamodel.substring(59));
						modelsuse.add(targetMM.substring(59));
						//System.out.println(intermetamodel.substring(59));
						
					}	
					
					if(!etlscript3.toFile().exists())
					{
						System.out.println("1");
												
						//chainMT(sourceModel, sourceMM, intermodel, intermetamodel);
					
						//modelsuse.add(sourceMM.substring(59));
						
						targetMM=intermetamodel2;
						targetModel=intermodel2;
						
						if(scriptRoot.resolve(intermetamodel.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists())
						{
							chainMT(intermodel, intermetamodel, intermodel2, intermetamodel2);
							modelsuse.add(intermetamodel2.substring(59));
						}
						
						//modelsuse.add(intermetamodel.substring(59));
						
					}
					
				}
				else
					continue;
				/*
				if(etlscript2.toFile().exists() && !etlscript3.toFile().exists())
				{
					
					System.out.println("1");
					
					chainMT(sourceModel, sourceMM, intermodel, intermetamodel);
				
					modelsuse.add(sourceMM.substring(59));
					
					targetMM=intermetamodel2;
					targetModel=intermodel2;
					
					if(etlscript3.toFile().exists())
					{
						chainMT(intermodel, intermetamodel, targetModel, targetMM);
					
						modelsuseB.add(intermetamodel.substring(59));
						modelsuseB.add(targetMM.substring(59));
					}
					
				
					if(!etlscript3.toFile().exists())
					{
						chainMT(intermodel, intermetamodel, intermodel2, intermetamodel2);
						//modelsuseB.add(intermetamodel.substring(59));
						//modelsuseB.add(intermetamodel2.substring(59));
					}
					//modelsuse.add(intermetamodel2.substring(59));
					//modelsuse.add(targetMM.substring(59));
				}*/
				/*
				if(!etlscript2.toFile().exists() && etlscript3.toFile().exists())
				{
					System.out.println("2");
					
					sourceMM=intermetamodel2;
					sourceModel=intermodel2;
					
					chainMT(sourceModel, sourceMM, intermodel1, intermetamodel1);
				
					modelsuse.add(sourceMM.substring(59));
					
					
					chainMT(intermodel1, intermetamodel1, targetModel, targetMM);
					
					modelsuse.add(intermetamodel1.substring(59));
					modelsuse.add(targetMM.substring(59));
					break;
				}
				
				if(!etlscript2.toFile().exists() && !etlscript3.toFile().exists())
				{
					System.out.println("3");
					
					sourceMM=intermetamodel2;
					sourceModel=intermodel2;
					
					for(int k=0;k<contents.length;k++) {
						if(j!=k)
						{
							intermodelpath3 = genmodelsRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+contents[k].replaceFirst("[.][^.]+$", "")+".xmi");
							intermodel3 = intermodelpath3.toAbsolutePath().toUri().toString();
						}
					}
					chainMT(sourceModel, sourceMM, intermodel3, intermetamodel3);
				
					modelsuse.add(sourceMM.substring(59));
					
					sourceMM=intermetamodel1;
					sourceModel=intermodel1;
					
					chainMT(sourceModel, sourceMM, targetModel, targetMM);
					
					modelsuse.add(intermetamodel.substring(59));
					modelsuse.add(targetMM.substring(59));
					break;
				}*/
				
				/*
				interetlscript0 = scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				
				
				if(interetlscript0.toFile().exists() && !etlscript3.toFile().exists())
				{
					System.out.println("1");
					sourceMM=intermetamodel2;
					sourceModel=intermodel2;
					targetMM=intermetamodel;
					targetModel=intermodel;
					chainMT(sourceModel, sourceMM, targetModel, targetMM);
					System.out.println(interetlscript0);
					
					modelsuse.add(intermetamodel2.substring(59));
					
				}
				
				
				//etlscript3 = scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
				if(etlscript3.toFile().exists())
				{
					System.out.println("2");
					sourceMM=intermetamodel1;
					sourceModel=intermodel1;
					chainMT(sourceModel, sourceMM, targetModel, targetMM);
					System.out.println(etlscript3);
					
					modelsuse.add(intermetamodel1.substring(59));
					modelsuse.add(targetMM.substring(59));
					break;
					//break outerloop;
				}
				*/
				//break;
				
				
				for (String element : modelsuse) {
					if (!modelsuse1.contains(element))
						modelsuse1.add(element);
				}
					
			
				
				/*
				if(etlscript2.toFile().exists() && etlscript3.toFile().exists())
				{
					System.out.println("1");
					targetMM=intermetamodel;
					targetModel=intermodel;
					sourceMM=intermetamodel1;
					sourceModel=intermodel1;
					chainMT(sourceModel, sourceMM, targetModel, targetMM);
					//System.out.println(targetMM);
					//i++;
					//j++;
					//continue;
				}
				
				
				//if(!((metamodelPath+"\\"+contents[i]).equals(sourceMM)) && (metamodelPath+"\\"+contents[j]).equals(targetMM))
				//if(etlscript3.toFile().exists() && !etlscript2.toFile().exists())
				if(etlscript3.toFile().exists())
				{
					System.out.println("2");
					sourceMM=intermetamodel1;
					sourceModel=intermodel1;
					chainMT(sourceModel, sourceMM, targetModel, targetMM);
					//i++;
					//continue;
				}
				*/
				//else
				//{
					//continue;
					//System.out.println("abcd");
					//break;
				//}
					
			}
			else
				continue;
			
		}
			//System.out.println(modelsuse);
			
		}
		
		
		if(!modelsuse0.isEmpty())
			newmodelsuse.add(modelsuse0);
		newmodelsuse.add(modelsuse1);
		//newmodelsuse.add(modelsuseB);
		
		System.out.println(newmodelsuse);
			
	}
	
	
	public ArrayList<ArrayList<String>> runChain(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		Path etlscript0 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		ArrayList<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse = new ArrayList<String>();
		
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		
		ArrayList<String> modelsuse2 = new ArrayList<String>();
		
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		
		ArrayList<String> modelsuse4 = new ArrayList<String>();
		
		ArrayList<String> modelsuse5 = new ArrayList<String>();
		
		if(etlscript0.toFile().exists())
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(59));
			modelsuse0.add(targetMM.substring(59));
			
			//executeETL(sourceModel, sourceMM, targetModel, targetMM);
		}
		
		for(int i=0; i<contents.length; i++) 
		{
			Path etlscript1=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
			Path etlscript2=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
			
			if((etlscript1.toFile().exists()))
			{
				String intermetamodel=metamodelPath+"\\"+contents[i];
				Path intermodelpath=genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermodel=intermodelpath.toAbsolutePath().toUri().toString();
				
				chainMT(sourceModel, sourceMM, intermodel, intermetamodel);
				modelsuse.add(sourceMM.substring(59));
				//System.out.println("MM"+i+":"+intermetamodel);
				if(etlscript2.toFile().exists())
				{
				
					chainMT(intermodel, intermetamodel, targetModel, targetMM);
					modelsuse.add(intermetamodel.substring(59));
					modelsuse.add(targetMM.substring(59));
					
					for (String element : modelsuse) {
						if (!modelsuse1.contains(element))
							modelsuse1.add(element);
					}
					break;
				}
				
				if(!etlscript2.toFile().exists())
				{
					//if(targetMM==contents[i])
					String intermetamodel1 = null, intermodel1 = null, intermetamodel2 = null, intermodel2 = null;
					Path intermodelpath1, intermodelpath2;
					modelsuse2.add(sourceMM.substring(59));
					
					for(int j=0;j<contents.length;j++)
					{
						intermetamodel1=metamodelPath+"\\"+contents[j];
						intermodelpath1=genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
						intermodel1=intermodelpath1.toAbsolutePath().toUri().toString();
						chainMT(intermodel, intermetamodel, intermodel1, intermetamodel1);
						modelsuse2.add(intermetamodel.substring(59));
						
						if(scriptRoot.resolve(intermetamodel1.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists())
						{
							modelsuse2.add(intermetamodel1.substring(59));
							//System.out.println(intermetamodel1.substring(59));
							//break;
						}
						
						/*
						if(!scriptRoot.resolve(intermetamodel1.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists())
						{
							//System.out.println(intermetamodel1);
							for(int k=0;k<contents.length;k++)
							{
								intermetamodel2=metamodelPath+"\\"+contents[k];
								intermodelpath2=genmodelsRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+contents[k].replaceFirst("[.][^.]+$", "")+".xmi");
								intermodel2=intermodelpath2.toAbsolutePath().toUri().toString();
								if(scriptRoot.resolve(intermetamodel2.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[k].replaceFirst("[.][^.]+$", "")+".etl").toFile().exists())
								{
									chainMT(intermodel1, intermetamodel1, intermodel2, intermetamodel2);
									modelsuse4.add(intermetamodel1.substring(59));
								}
								
								if(scriptRoot.resolve(intermetamodel2.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists())
								{
									modelsuse4.add(intermetamodel2.substring(59));
									//System.out.println(intermetamodel1.substring(59));
									//break;
								}
								
							
							
							chainMT(intermodel2, intermetamodel2, targetModel, targetMM);
							
							modelsuse4.add(targetMM.substring(59));
							
							for (String element : modelsuse4) {
								if (!modelsuse5.contains(element))
									modelsuse5.add(element);
						}
						 }
					}*/
					
					chainMT(intermodel1, intermetamodel1, targetModel, targetMM);
					
					modelsuse2.add(targetMM.substring(59));
					
					for (String element : modelsuse2) {
						if (!modelsuse3.contains(element))
							modelsuse3.add(element);
					}
					//break;
					}
				}
			}
		}
		if(!modelsuse0.isEmpty())
			newmodelsuse.add(modelsuse0);
		if(!modelsuse1.isEmpty())
			newmodelsuse.add(modelsuse1);
		if(!modelsuse3.isEmpty())
			newmodelsuse.add(modelsuse3);
		if(!modelsuse5.isEmpty())
			newmodelsuse.add(modelsuse5);
		System.out.println(newmodelsuse);
		return newmodelsuse;
		
	}
	
	
	public ArrayList<ArrayList<String>> chaintwo(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		Path etlscript0 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		//ArrayList<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> newmodelsuse1 = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse = new ArrayList<String>();
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		ArrayList<String> modelsuse4 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();
		ArrayList<String> modelsuse6 = new ArrayList<String>();
		ArrayList<String> modelsuse7 = new ArrayList<String>();
		ArrayList<String> modelsuse8 = new ArrayList<String>();
		ArrayList<String> modelsuse9 = new ArrayList<String>();
		ArrayList<String> modelsuse10 = new ArrayList<String>();
		ArrayList<String> modelsuse11 = new ArrayList<String>();
		ArrayList<String> modelsuse12 = new ArrayList<String>();
		ArrayList<String> modelsuse13 = new ArrayList<String>();
		
		String intermediateMM = null, intermediateModel = null, intermediateMM1 = null, intermediateModel1 = null;
		Path intermediateModelPath, intermediateModelPath1;
		
		if(etlscript0.toFile().exists())
		{
			
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(59));
			modelsuse0.add(targetMM.substring(59));
			System.out.println(sourceMM.substring(59));
			if(!modelsuse0.isEmpty())
				newmodelsuse.add(modelsuse0);
			
			System.out.println(modelsuse0);
		}
		//if(!etlscript0.toFile().exists())
		//{
			//System.out.println("123");
			outloop:
			for(int i=0; i<contents.length; i++) 
			{
				for(int j=0; j<contents.length; j++) 
				{
					Path etlscript1=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
					Path etlscript2=scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
					Path etlscript3=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
					
					intermediateMM = metamodelPath+"\\"+contents[j];
					intermediateModelPath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
					intermediateModel = intermediateModelPath.toAbsolutePath().toUri().toString();
					
					intermediateMM1 = metamodelPath+"\\"+contents[i];
					intermediateModelPath1 = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".xmi");
					intermediateModel1 = intermediateModelPath1.toAbsolutePath().toUri().toString();
					/*
					if(etlscript1.toFile().exists() && !etlscript2.toFile().exists() && !etlscript3.toFile().exists())
					{
						chainMT(sourceModel, sourceMM, intermediateModel, intermediateMM);
						System.out.println("MM11:"+sourceMM);
						modelsuse3.add(sourceMM.substring(59));
						modelsuse3.add(intermediateMM.substring(59));
						break;
					}
					*/
					/*
					if(etlscript2.toFile().exists() && !etlscript1.toFile().exists() && !etlscript3.toFile().exists())
					{
						chainMT(intermediateModel1, intermediateMM1, targetModel, targetMM);
						System.out.println("MM11:"+intermediateMM1);
						modelsuse4.add(sourceMM.substring(59));
						modelsuse4.add(intermediateMM.substring(59));
						break;
					}
					*/
					/*
					if(etlscript3.toFile().exists() && !etlscript1.toFile().exists() && !etlscript2.toFile().exists())
					{
						chainMT(intermediateModel1, intermediateMM1, intermediateModel, intermediateMM);
						modelsuse5.add(intermediateMM1.substring(59));
						modelsuse5.add(intermediateMM.substring(59));
						break;
						
					}
					*/
					if(etlscript1.toFile().exists() && etlscript2.toFile().exists() && !etlscript3.toFile().exists())
					{
						chainMT(sourceModel, sourceMM, intermediateModel, intermediateMM);
						chainMT(intermediateModel, intermediateMM, targetModel, targetMM);
						boolean s1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermediateMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						boolean s2 = scriptRoot.resolve(intermediateMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
						if(s1)
							modelsuse8.add(sourceMM.substring(59));
						//System.out.println("MM:"+intermediateMM);
						if(s1 && s2)
						{
							modelsuse8.add(intermediateMM.substring(59));
							modelsuse8.add(targetMM.substring(59));
						}
						
						//System.out.println("MMt:"+targetMM);
						//break;
					}
					
					
					if(etlscript1.toFile().exists() && etlscript3.toFile().exists() && etlscript2.toFile().exists())
					{
						chainMT(sourceModel, sourceMM, intermediateModel1, intermediateMM1);
						chainMT(intermediateModel1, intermediateMM1, intermediateModel, intermediateMM);
						chainMT(intermediateModel, intermediateMM, targetModel, targetMM);
						
						modelsuse10.add(sourceMM.substring(59));
						modelsuse10.add(intermediateMM1.substring(59));
						modelsuse10.add(intermediateMM.substring(59));
						modelsuse10.add(targetMM.substring(59));
						//break;
					}
					
					if(etlscript1.toFile().exists() && etlscript3.toFile().exists() && !etlscript2.toFile().exists())
					{
						for(int k=0; k<contents.length; k++) 
						{
							Path etlscript4=scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+contents[k].replaceFirst("[.][^.]+$", "")+".etl");
							Path etlscript5=scriptRoot.resolve(contents[k].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
							
							//String intermediateMM2 = metamodelPath+"\\"+contents[j];
							//Path intermediateModelPath2 = genmodelsRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+contents[k].replaceFirst("[.][^.]+$", "")+".xmi");
							//String intermediateModel2 = intermediateModelPath2.toAbsolutePath().toUri().toString();
							
							String intermediateMM3 = metamodelPath+"\\"+contents[k];
							Path intermediateModelPath3 = genmodelsRoot.resolve(contents[k].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".xmi");
							String intermediateModel3 = intermediateModelPath3.toAbsolutePath().toUri().toString();
							
							chainMT(sourceModel, sourceMM, intermediateModel1, intermediateMM1);
							chainMT(intermediateModel1, intermediateMM1, intermediateModel, intermediateMM);
							chainMT(intermediateModel, intermediateMM, intermediateModel3, intermediateMM3);
							chainMT(intermediateModel3, intermediateMM3, targetModel, targetMM);
							
							boolean s1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermediateMM1.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
							boolean s2 = scriptRoot.resolve(intermediateMM1.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermediateMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
							boolean s3 = scriptRoot.resolve(intermediateMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+intermediateMM3.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
							boolean s4 = scriptRoot.resolve(intermediateMM3.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
							
							if(s1 && s2 && s3 && s4)
							{
								modelsuse12.add(sourceMM.substring(59));
								//System.out.println("MM1111:"+intermediateMM);
								
								modelsuse12.add(intermediateMM1.substring(59));
							}
							
							if(s2 && s1 && s3 && s4)
							{
								modelsuse12.add(intermediateMM.substring(59));
								//System.out.println("dgsg"+intermediateMM);
							}
							
							if(s3 && s2 && s1 && s4)
							{
								modelsuse12.add(intermediateMM3.substring(59));
								modelsuse12.add(targetMM.substring(59));
								//System.out.println(intermediateMM3);
							}
							
							//break;
						}
						
					}
					
				}
				//if(intermediateModel1==targetMM)
				//{
					//chaintwo(intermediateModel, intermediateMM, intermediateModel1, intermediateMM1);
					//break;
				//}
				for (String element : modelsuse3) {
					if (!modelsuse1.contains(element))
						modelsuse1.add(element);
				}
				for (String element : modelsuse4) {
					if (!modelsuse6.contains(element))
						modelsuse6.add(element);
				}
				for (String element : modelsuse5) {
					if (!modelsuse7.contains(element))
						modelsuse7.add(element);
				}
				for (String element : modelsuse8) {
					if (!modelsuse9.contains(element))
						modelsuse9.add(element);
				}
				for (String element : modelsuse10) {
					if (!modelsuse11.contains(element))
						modelsuse11.add(element);
				}
				for (String element : modelsuse12) {
					if (!modelsuse13.contains(element))
						modelsuse13.add(element);
				}	
				
			}
			
			
			if(!modelsuse1.isEmpty())
				newmodelsuse.add(modelsuse1);
			if(!modelsuse6.isEmpty())
				newmodelsuse.add(modelsuse6);
			if(!modelsuse7.isEmpty())
				newmodelsuse.add(modelsuse7);
			if(!modelsuse9.isEmpty())
				newmodelsuse.add(modelsuse9);
			if(!modelsuse11.isEmpty())
				newmodelsuse.add(modelsuse11);
			if(!modelsuse13.isEmpty())
				newmodelsuse.add(modelsuse13);
			for (ArrayList<String> element : newmodelsuse) {
				if (!newmodelsuse1.contains(element))
					newmodelsuse1.add(element);
			}
			System.out.println(newmodelsuse1);
			return newmodelsuse1;
				
		//}
		
	}
	
	public void findchaining(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		ArrayList<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		Path etlscript0 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		if(etlscript0.toFile().exists())
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(59));
			modelsuse0.add(targetMM.substring(59));
			//System.out.println(sourceMM.substring(59));
			if(!modelsuse0.isEmpty())
				newmodelsuse.add(modelsuse0);
			
			System.out.println(modelsuse0);
		}
		
		for(int i=0; i<contents.length; i++) 
		{
			Path etlscript1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
			Path etlscript2 = scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
			
			if(etlscript1.toFile().exists())
			{
				String intermediateMM = metamodelPath+"\\"+contents[i];
				Path intermediateModelPath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".xmi");
				String intermediateModel = intermediateModelPath.toAbsolutePath().toUri().toString();
				chainMT(sourceModel, sourceMM, intermediateModel, intermediateMM);
			}
			
			if(etlscript2.toFile().exists())
			{
				String intermediateMM = metamodelPath+"\\"+contents[i];
				Path intermediateModelPath = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".xmi");
				String intermediateModel = intermediateModelPath.toAbsolutePath().toUri().toString();
				chainMT(sourceModel, sourceMM, intermediateModel, intermediateMM);
			}
			
			
			for(int j=0;j<contents.length;j++)
			{
				Path etlscript3 = scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				if(etlscript3.toFile().exists() && !etlscript1.toFile().exists() && !etlscript2.toFile().exists())
				{
					String intermediateMM = metamodelPath+"\\"+contents[i];
					Path intermediateModelPath = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
					String intermediateModel = intermediateModelPath.toAbsolutePath().toUri().toString();
					chainMT(sourceModel, sourceMM, intermediateModel, intermediateMM);
				}
				
			}
		
		}
	}
	
	public void chainN(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception
	{
		ArrayList<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		//ArrayList<ArrayList<String>> newmodelsuse1 = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse2 = new ArrayList<String>();
		
		Path etlscript0 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		
		String intermediateMM = null, intermediateModel = null;
		Path intermediateModelPath;
		String intermediateMM1 = null, intermediateModel1 = null;
		Path intermediateModelPath1;
		String intermediateMM2 = null, intermediateModel2 = null;
		Path intermediateModelPath2;
		Path etlscript1, etlscript2 = null;
		if(etlscript0.toFile().exists())
		{
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			
			modelsuse0.add(sourceMM.substring(59));
			modelsuse0.add(targetMM.substring(59));
			//System.out.println(sourceMM.substring(59));
			if(!modelsuse0.isEmpty())
				newmodelsuse.add(modelsuse0);
			
			System.out.println(modelsuse0);
		}
		
		
		for(int i=0; i<contents.length; i++) 
		{
			etlscript1=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
			etlscript2=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
			
			if(etlscript1.toFile().exists())
				modelsuse1.add(sourceMM.substring(59));
			
			for(int j=0; j<contents.length; j++) 
			{
				if(i!=j)
				{
					
					Path etlscript3=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
					
//					intermediateMM = metamodelPath+"\\"+contents[j];
//					intermediateModelPath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
//					intermediateModel = intermediateModelPath.toAbsolutePath().toUri().toString();
//					
//					intermediateMM1 = metamodelPath+"\\"+contents[i];
//					intermediateModelPath1 = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".xmi");
//					intermediateModel1 = intermediateModelPath1.toAbsolutePath().toUri().toString();
					
					boolean s1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
					boolean s2 = scriptRoot.resolve(contents[j].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
					
					intermediateMM2 = metamodelPath+"\\"+contents[i];
					intermediateModelPath2 = genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".xmi");
					intermediateModel2 = intermediateModelPath2.toAbsolutePath().toUri().toString();
					
					chainMT(sourceModel, sourceMM, intermediateModel2, intermediateMM2);
					chainMT(intermediateModel2, intermediateMM2, targetModel, targetMM);
					//chainN(sourceModel, sourceMM, intermediateModel, intermediateMM);
					
					if(etlscript3.toFile().exists())
						modelsuse1.add(intermediateMM2.substring(59));
					//System.out.println(intermediateMM2.substring(59));
					//if(s2 && etlscript3.toFile().exists())
						//modelsuse1.add(intermediateMM2.substring(59));
					
					//System.out.println(modelsuse1);
					//System.out.println(intermediateMM.substring(59));
					
				}
			
			}
			
			if(etlscript2.toFile().exists())
				modelsuse1.add(targetMM.substring(59));
			
			for (String element : modelsuse1) {
				if (!modelsuse2.contains(element))
					modelsuse2.add(element);
			}
			
			//System.out.println(modelsuse1);
			//if(!modelsuse1.isEmpty())
				//newmodelsuse1.add(modelsuse1);
			
			//intermediateMM = metamodelPath+"\\"+contents[i];
			//intermediateModelPath = genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".xmi");
			//intermediateModel = intermediateModelPath.toAbsolutePath().toUri().toString();
			//System.out.println(newmodelsuse1);
			
		}
		if(!modelsuse2.isEmpty())
			newmodelsuse.add(modelsuse2);
		//if(!newmodelsuse.isEmpty())
		System.out.println(modelsuse2);
		
		/*
		ArrayList<ArrayList<String>> newmodelsuse1 = new ArrayList<ArrayList<String>>();
		ArrayList<String> modelsuse4 = new ArrayList<String>();
		
		for(int k=1;k<modelsuse2.size()-1;k++)
		{
			for(int l=1;l<modelsuse2.size()-1;l++)
			{
				boolean sc1 = scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[k].replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
				boolean sc2 = scriptRoot.resolve(contents[k].replaceFirst("[.][^.]+$", "")+"2"+contents[l].replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
				boolean sc3 = scriptRoot.resolve(contents[l].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl").toFile().exists();
				
				if(sc2)
				{
					modelsuse4.add(modelsuse2.get(l));
				}
				
				//System.out.println(modelsuse2.get(l));
			}
			
		}
		System.out.println(modelsuse4);
		*/
			
		
		//chainN(sourceModel, sourceMM, intermediateModel, intermediateMM);
		//chainN(intermediateModel, intermediateMM, targetModel, targetMM);
		
				
		
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
		
		//IEolModule module = runConfig.getModule();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		//int total;
//		if(!sm1.hasRun)
//		{
//			sm1.run();
//			total = calculateMTChain(module);
//		}
		sm1.run();
		//total = calculateMTChain(module);
		//int n=sm1.preExecute1();
		//if()
		//sm1.run();
		//Object r = sm1.call();
		//System.out.println(module);
		//System.out.println(total);
		//System.out.println(r);
		
		
		runConfig.dispose();
		return runConfig;
		
		
	}
	
//	public void calculateChain(ArrayList<String> chain)
//	{
//		int sum=0;
//		for(int i=0;i<chain.size();i++)
//		{
//			
//		}
//	}
	
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
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

			}

		}
		for(int i=0;i<((EtlModule) module).getTransformationRules().size();i++)
		{
			EolModelElementType type =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
						
			for(int j=0;j<((EtlModule) module).getTransformationRules().get(i).getTargetParameters().size();j++)
			{
				EolModelElementType type1 =(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
				System.out.println("Transformation rule"+(i+1)+": "+type.getTypeName()+" to "+type1.getTypeName()+"\n");
							
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
					//return totalstatement;
			//}
			
			//if(totalstatement>max)
				//max=totalstatement;
		//}
		
		
	}
	return totalstatement;
	
	}
	
//	
//	public String gettargetmodel()
//	{
//		String model = targetProperties.getProperty(EmfModel.PROPERTY_MODEL_URI);
//		return model;
//	}
//	public String gettargetmetamodel()
//	{
//		String metamodel = targetProperties.getProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI);
//		return metamodel;
//	}
//	
//	public static float coverage(EtlRunConfiguration transformation) 
//	{
//		
//		return 0;
//
//	}

}
