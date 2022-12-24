package org.eclipse.epsilon.etl.chain.selection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
//import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.math.NumberUtils;
//import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;
import org.eclipse.epsilon.examples.staticanalyser.eol.EolPreExecuteConfiguration;

class Serialize_IdentifyETL implements java.io.Serializable
{
    public String s;
    public String t;
    ArrayList<String> etl;
  
    // Default constructor
    public Serialize_IdentifyETL(String s, String t, ArrayList<String> etl)
    {
        this.s = s;
        this.t = t;
        this.etl = etl;
    }
  
}

public class Chaining_MT {

	Path modelsRoot = Paths.get("models");
	Path metamodelsRoot = Paths.get("metamodels");
	Path scriptRoot = Paths.get("scripts");
	Path genmodelsRoot = Paths.get("models/generatedmodels");

	File modelPath = new File("models");
	String modelscontents[] = modelPath.list();
	
	File metamodelPath = new File("metamodels");
	String contents[] = metamodelPath.list();

	File scriptPath = new File("scripts");
	String scriptcontents[] = scriptPath.list();
	static HashMap<String, Boolean> findetl = new HashMap<String, Boolean>();
	static HashMap<String, Double> allmtcoverage = new HashMap<String, Double>();
	
	final static String outputFilePath_identifyETL = "C:\\Users\\sahay\\git\\repository\\org.eclipse.epsilon.etl.chain.optimisation\\writeETL.txt";

	ModelProperties modelProperties = new ModelProperties();

	StringProperties sourceProperties, targetProperties;
	Path etlscript;
	
	final static String outputFilePath2 = "writeComplexity.txt";
	
//	Chain_MT cm = new Chain_MT();
	final static String outputFilePath = "C:/Users/sahay/OneDrive/Documents/chain optimization_momot 27032022/org.eclipse.epsilon.etl.chain.optimisation/write_complexity.txt";
	
	ArrayList<String> modelsuse1 = new ArrayList<String>();
	
	public void chainMT(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception {
		System.out.println(sourceMM.substring(11) + " -> " + targetMM.substring(11));

	}
	
//	public List<ArrayList<ArrayList<String>>> multiple_identifyChain(String sourceModel, String sourceMM, String targetModel,
//			String targetMM) throws Exception {
//		
//		List<ArrayList<ArrayList<String>>> multiple_chain = new ArrayList<ArrayList<ArrayList<String>>>();
//		List<ArrayList<String>> identify = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		//System.out.println(identifyETL(sourceMM,targetMM));
//		for(int e=0;e<identify.size();e++)
//		{	
//			if(e<identify.size())
//			{
//				for(int id=0;id<identifyETL(identify.get(e),identify.get(e+1)).size();id++)
//			}
//			
//			multiple_chain.get(e).addAll((ArrayList<ArrayList<String>>) identify);
//			
//			System.out.println(multiple_chain);
//			
//		}
//			
//			return multiple_chain;
//		
//	}

	public ArrayList<String> identifyChain_two(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception {
		//List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		ArrayList<String> modelsuse0 = new ArrayList<String>();
//		registerMM(sourceMM);
//		registerMM(targetMM);
		boolean etl1 = false;
		//if(findETL(sourceMM,targetMM)==true)
		etl1 = findETL(sourceMM, targetMM);
		
		if (etl1) {
			
			for(int id=0;id<identifyETL(sourceMM, targetMM).size();id++) {
			chainMT(sourceModel, sourceMM, targetModel, targetMM);
			ArrayList<String> x = identifyETL(sourceMM, targetMM);
			//System.out.println(x.get(id));
			modelsuse0.add(sourceMM.substring(11));
			modelsuse0.add(targetMM.substring(11));
		
//			Files.deleteIfExists(scriptRoot.resolve(x.get(id)));
//				          
//		    System.out.println("Deletion successful.");
			
			Files.move(scriptRoot.resolve(x.get(id)),modelsRoot.resolve(x.get(id)));
		     // break;
			}
			
		}
//		else
//			modelsuse0=null;
		//System.out.println("dbkjsghsh");
		System.out.println("Direct Transformation: "+modelsuse0);
		return modelsuse0;
	}
	
	public List<ArrayList<String>> identifyChain(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		List<String> modelsuse4 = new ArrayList<String>();
		List<String> modelsuse40 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();

//		registerMM(sourceMM);
//		registerMM(targetMM);
		boolean etl1 = findETL(sourceMM, targetMM);
		

		if (etl1) {
			for(int id=0;id<identifyETL(sourceMM, targetMM).size();id++) {
			modelsuse0.add(sourceMM.substring(11));
			modelsuse0.add(targetMM.substring(11));
			chainMT(sourceModel, sourceMM, targetModel, targetMM);

			}
		}

		else {
			for (int i = 0; i < 2; i++) {
			 for (int j = 0; j < contents.length; j++) {
				String intermetamodel = metamodelsRoot.resolve(contents[j]).toString();
				
				Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "") + "2"
						+ contents[j].replaceFirst("[.][^.]+$", "") + ".xmi");
				String intermodel = intermodelpath.toAbsolutePath().toUri().toString();
				
				boolean s1 = findETL(sourceMM, intermetamodel);
				boolean s2 = findETL(intermetamodel, targetMM);
				boolean s3 = findETL(targetMM, intermetamodel);
				
				if (findETL(sourceMM, intermetamodel)) {
					
					//System.out.println(intermetamodel);
					
					for(int id=0;id<identifyETL(sourceMM, intermetamodel).size();id++) {
					identifyChain(sourceModel, sourceMM, intermodel, intermetamodel);
					//identifyChain(intermodel, intermetamodel, targetModel, targetMM);
					
					//System.out.println(s1+" "+s2);
					//if(modelsuse1.size()>0 && !modelsuse1.get(modelsuse1.size()-1).equals(targetMM.substring(11)))
					
					if(s1) {
						modelsuse1.add(sourceMM.substring(11));	
						modelsuse1.add(intermetamodel.substring(11));
					}
//					if(s2) {					
//						modelsuse1.add(intermetamodel.substring(11));
//						modelsuse1.add(targetMM.substring(11));
//						break;
//					}
					
					sourceModel = intermodel;
					sourceMM = intermetamodel;
					
					//modelsuse1.remove(intermetamodel.substring(11));
					//identifyChain(intermodel, intermetamodel, targetModel, targetMM);
//					identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//					if (s2) {	
//						modelsuse1.add(targetMM.substring(11));
//						System.out.println("123");
//						//break outer;
//					}
					
					}	
					
				}	
				
			}
			}
			//System.out.println("List "+modelsuse1);
//			if(modelsuse1.size()>0 && modelsuse1.get(modelsuse1.size()-1).equals(targetMM.substring(11))) {
//				for (String element : modelsuse1) {
//					if (!modelsuse3.contains(element))
//						modelsuse3.add(element);
//				}
//			}
			System.out.println(modelsuse1);
			int index = 0;
			if(modelsuse1.size()>0) {
				for (int i=0;i<modelsuse1.size();i++) {
					if(modelsuse1.get(i).equals(targetMM.substring(11))) {
						index=i;		
					}		
				}	
			}
			for(int j=0;j<=index;j++) {
				if (modelsuse1.size()>0 && !modelsuse3.contains(modelsuse1.get(j)))
					modelsuse3.add(modelsuse1.get(j));
				//System.out.println(index+" "+modelsuse1.get(j));
			}

		}

		if (!modelsuse0.isEmpty() && modelsuse0.get(modelsuse0.size()-1).equals(targetMM.substring(11)))
			newmodelsuse.add(modelsuse0);

		for (int k = 0; k < modelsuse3.size(); k++) {

			for (int l = k + 2; l < modelsuse3.size(); l++) {
				boolean sc1 = findETL(metamodelsRoot.resolve(modelsuse3.get(k)).toString(), metamodelsRoot.resolve(modelsuse3.get(l)).toString());
				if (sc1) {
					modelsuse40 = modelsuse3.subList(0, l - 1);
					modelsuse4 = modelsuse3.subList(l, modelsuse3.size());

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
		//if (modelsuse3.size()>0 && modelsuse3.get(modelsuse3.size()-1).equals(targetMM.substring(11)))
		if (!modelsuse3.isEmpty())
			newmodelsuse.add(modelsuse3);

		//if (modelsuse5.size()>0 && modelsuse5.get(modelsuse5.size()-1).equals(targetMM.substring(11)))
		if (!modelsuse5.isEmpty())
			newmodelsuse.add(modelsuse5);

		for(ArrayList<String> list:newmodelsuse)
			if(list.size()>0 && !list.get(list.size()-1).equals(targetMM.substring(11)))
				list.remove(list.size()-1);
//		System.out.println(newmodelsuse);
		return newmodelsuse;

	}
	
	public ArrayList<ArrayList<String>> identifyChain_all(ArrayList<String> chain, String targetMM) throws Exception {
		ArrayList<ArrayList<String>> totalchain = new ArrayList<ArrayList<String>>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		for(int i = 0; i < chain.size()-1; i++) {
			for(int j = i+1; j < chain.size(); j++) {
				if(findETL(metamodelsRoot.resolve(chain.get(i)).toString(), metamodelsRoot.resolve(chain.get(j)).toString())) {
					modelsuse1.add(chain.get(i));
					modelsuse1.add(chain.get(j));
				}
			}
		}
		return null;
		
	}
	
	public ArrayList<String> identifyPossibleChain(String sourceMM, String targetMM) throws Exception {
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();

		Chain_MT cm = new Chain_MT();
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		List<String> modelsuse4 = new ArrayList<String>();
		List<String> modelsuse40 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();
		
		Stack<String> stack1 = new Stack<String>();
		boolean[] isVisited = new boolean[11];

		boolean etl1 = findETL(sourceMM, targetMM);
		

		if (etl1) {
			for(int id=0;id<identifyETL(sourceMM, targetMM).size();id++) {
			modelsuse0.add(sourceMM.substring(11));
			modelsuse0.add(targetMM.substring(11));
			System.out.println(sourceMM + "->"+targetMM);
			}
		}

		else {
			for (int i = 0; i < 2; i++) {
			 for (int j = 0; j < contents.length; j++) {
				 //for (int k = 0; k < contents.length; k++) {
				String intermetamodel = metamodelsRoot.resolve(contents[j]).toString();
				//String intermetamodel2 = metamodelsRoot.resolve(contents[k]).toString();
				
				boolean s1 = findETL(sourceMM, intermetamodel);
				boolean s2 = findETL(intermetamodel, targetMM);
				//boolean s3 = findETL(intermetamodel, intermetamodel2);
				
				//if (findETL(sourceMM, intermetamodel)) {
					
					//System.out.println(intermetamodel);
					
					for(int id=0;id<identifyETL(sourceMM, intermetamodel).size();id++) {
						identifyPossibleChain(sourceMM, intermetamodel);
					
					if(s1) {
						modelsuse1.add(sourceMM.substring(11));	
						modelsuse1.add(intermetamodel.substring(11));
					}
//					if(s3) {
//						modelsuse1.add(intermetamodel.substring(11));
//						modelsuse1.add(intermetamodel2.substring(11));					
//					}
					if(s2) {					
						//modelsuse1.add(intermetamodel.substring(11));
						//modelsuse1.add(targetMM.substring(11));
						break;
					}
//					if(intermetamodel.equals(targetMM))
//						break;
					
					//sourceMM = intermetamodel;
					
//					if(sourceMM.equals(targetMM))
//						break;
					
					//}	
				//}
					
				}

			 }
			}
		}
			System.out.println("List0 "+modelsuse0);
			System.out.println("List "+modelsuse1+" has size "+modelsuse1.size());
			return modelsuse1;

	}
	
	public ArrayList<String> identifyChain_source(String sourceMM) throws Exception {
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();

		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		List<String> modelsuse4 = new ArrayList<String>();
		List<String> modelsuse40 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();

			for (int i = 0; i < 2; i++) {
			 for (int j = 0; j < contents.length; j++) {
				 
				// for (int k = 0; k < contents.length; k++) {
				String intermetamodel1 = metamodelsRoot.resolve(contents[j]).toString();				
//				Path intermodelpath1 = genmodelsRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "") + "2"
//						+ contents[j].replaceFirst("[.][^.]+$", "") + ".xmi");
//				String intermodel1 = intermodelpath1.toAbsolutePath().toUri().toString();
				
				boolean s1 = findETL(sourceMM, intermetamodel1);
				//boolean s2 = findETL(intermetamodel1, intermetamodel2);
				
				if (s1) {
					//System.out.println(intermetamodel1);
					//for(int id=0;id<identifyETL(sourceMM, intermetamodel1).size();id++) {
						
//					identifyChain_source(intermodel1, intermetamodel1);
					
					modelsuse1.add(sourceMM.substring(11));	
					for(String middle:contents)
						if(findETL(sourceMM, metamodelsRoot.resolve(middle).toString()))
						{
							modelsuse1.add(middle);
//							System.out.println(identifyETL(sourceMM, metamodelsRoot.resolve(middle).toString())
//									+" transforms "+sourceMM+" -> "+metamodelsRoot.resolve(middle).toString());
						}
							
					
					sourceMM = intermetamodel1;
					//sourceModel = intermetamodel1;
					
				}
				//newmodelsuse.add(modelsuse1);

			 }
			}
			
			System.out.println(modelsuse1);
			
		return (ArrayList<String>) modelsuse1.stream().distinct().collect(Collectors.toList());

	}
	
	public ArrayList<String> identifyAllChain(String sourceMM, String targetMM) throws Exception {
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();

		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<String> modelsuse3 = new ArrayList<String>();
		List<String> modelsuse4 = new ArrayList<String>();
		List<String> modelsuse40 = new ArrayList<String>();
		ArrayList<String> modelsuse5 = new ArrayList<String>();

			for (int i = 0; i < 2; i++) {
			 for (int j = 0; j < contents.length; j++) {
				 
				// for (int k = 0; k < contents.length; k++) {
				String intermetamodel1 = metamodelsRoot.resolve(contents[j]).toString();				
//				Path intermodelpath1 = genmodelsRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "") + "2"
//						+ contents[j].replaceFirst("[.][^.]+$", "") + ".xmi");
//				String intermodel1 = intermodelpath1.toAbsolutePath().toUri().toString();
				
				boolean s1 = findETL(sourceMM, intermetamodel1);
				boolean s2 = findETL(intermetamodel1, targetMM);
				
				if (s1) {
					//System.out.println(intermetamodel1);
					//for(int id=0;id<identifyETL(sourceMM, intermetamodel1).size();id++) {
						
//					identifyChain_source(intermodel1, intermetamodel1);
					
					modelsuse1.add(sourceMM.substring(11));	
					for(String middle:contents) {
						if(findETL(sourceMM, metamodelsRoot.resolve(middle).toString()))
						{
							modelsuse1.add(middle);
//							System.out.println(identifyETL(sourceMM, metamodelsRoot.resolve(middle).toString())
//									+" transforms "+sourceMM+" -> "+metamodelsRoot.resolve(middle).toString());
						}
						
					}	
				//}
//				if (s2) {
//					
//					for(String middle:contents) {
//						if(findETL(metamodelsRoot.resolve(middle).toString(), targetMM))
//						{
//							modelsuse3.add(middle);
////							System.out.println(identifyETL(sourceMM, metamodelsRoot.resolve(middle).toString())
////									+" transforms "+sourceMM+" -> "+metamodelsRoot.resolve(middle).toString());
//						}
//						
//					}	
//					modelsuse3.add(targetMM.substring(11));	
					
//					if(findETL(intermetamodel1,targetMM)) {
//						modelsuse3.add(intermetamodel1);
//					}
					
					sourceMM = intermetamodel1;
					//sourceModel = intermetamodel1;
					
				}
				//newmodelsuse.add(modelsuse1);

			 }
			}
			//System.out.println(modelsuse3);
			System.out.println(modelsuse1);
			
		return (ArrayList<String>) modelsuse1.stream().distinct().collect(Collectors.toList());

	}
	
	
	
	
	public ArrayList<String> identifyChain_target(String targetMM) throws Exception {

		//ArrayList<String> modelsuse1 = new ArrayList<String>();
		
		//for (int i = 0; i < 2; i++) {
		for(String middle:contents) {
			if(findETL(metamodelsRoot.resolve(middle).toString(),targetMM))
			{
				modelsuse1.add(middle);
				//modelsuse1.add(targetMM.substring(11));
				//System.out.println(modelsuse1);
				System.out.println(identifyETL(metamodelsRoot.resolve(middle).toString(),targetMM)
						+" transforms "+metamodelsRoot.resolve(middle).toString()+" -> "+targetMM);
				//modelsuse1.add(middle);
				identifyChain_target(metamodelsRoot.resolve(middle).toString());
			}
		}
		//}
		modelsuse1.add(targetMM.substring(11));
		return (ArrayList<String>) modelsuse1.stream().distinct().collect(Collectors.toList());

	}


	public EtlRunConfiguration executeETL(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		for(int e=0;e<identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$",""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size();e++)
			etlscript = scriptRoot
				.resolve(identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
						metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).get(e));

		
		sourceProperties = modelProperties.properties(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), sourceMM,
				sourceModel, "true", "false");
		targetProperties = modelProperties.properties(targetMM.substring(11).replaceFirst("[.][^.]+$", ""), targetMM,
				targetModel, "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		System.out.println("Time taken to execute transformation "+ sourceMM+" -> "+targetMM+ " = "+(System.currentTimeMillis()-start)/1000+" seconds.");
		return runConfig;

	}
	
	public HashMap<EtlRunConfiguration, Double> executeETL_time(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//System.out.println(sourceModel+" "+sourceMM+" "+targetModel+" "+targetMM);
//		registerMM(sourceMM);
//		registerMM(targetMM);
		for(int e=0;e<identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$",""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size();e++)
			etlscript = scriptRoot
				.resolve(identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
						metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).get(e));

		
		sourceProperties = modelProperties.properties(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), sourceMM,
				sourceModel, "true", "false");
		targetProperties = modelProperties.properties(targetMM.substring(11).replaceFirst("[.][^.]+$", ""), targetMM,
				targetModel, "false", "true");

		
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		//EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		double start = System.currentTimeMillis();
		//sm1.run();
		runConfig.run();
		double endtime = (System.currentTimeMillis()-start)/1000;
		runConfig.dispose();
		
		System.out.println("Time taken to execute transformation "+ sourceMM+" -> "+targetMM+ " = "+endtime+" seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}
	
	public HashMap<EtlRunConfiguration, Double> executeETL_time2(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//System.out.println(sourceModel+" "+sourceMM+" "+targetModel+" "+targetMM);
		for(int e=0;e<identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$",""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size();e++)
			etlscript = scriptRoot
				.resolve(identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
						metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).get(e));

		double start = System.currentTimeMillis();
		sourceProperties = modelProperties.properties(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), sourceMM,
				sourceModel, "true", "false");
		targetProperties = modelProperties.properties(targetMM.substring(11).replaceFirst("[.][^.]+$", ""), targetMM,
				targetModel, "false", "true");

		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

//		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
//		
//		sm1.run();
		runConfig.run();
		runConfig.dispose();
		double endtime = (System.currentTimeMillis()-start)/1000;
		System.out.println("Time taken to execute transformation "+ sourceMM+" -> "+targetMM+ " = "+endtime+" seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}
	
	public EtlRunConfiguration executeETL(EtlModule module)
			throws Exception {
		Path etlscript = null;
		
		ModelProperties modelProperties = new ModelProperties();
		
		//module.getContext().setModule(module);
		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		//System.out.println(module);
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}
		
		staticAnalyser.validate(module);
		
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();
		
		//System.out.println(mm1);
		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();
		
//		for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//				targetMetamodel+".ecore").size();e++)
//			etlscript = scriptRoot
//				.resolve(chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//						metamodelPath + "/" + targetMetamodel+".ecore").get(e));

		StringProperties sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
				modelsRoot.resolve(sourceMetamodel+".xmi").toString(), "true", "false");
		StringProperties targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
				modelsRoot.resolve(targetMetamodel+".xmi").toString(), "false", "true");

		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(scriptRoot.resolve(module.getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		return runConfig;

	}
	
	public EtlRunConfiguration executeETL1(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		for(int e=0;e<identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$",""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size();e++)
			etlscript = scriptRoot
				.resolve(identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
						metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).get(e));

		sourceProperties = modelProperties.properties1(sourceMM.substring(11).replaceFirst("[.][^.]+$", ""), sourceMM,
				sourceModel, "true", "false");
		targetProperties = modelProperties.properties1(targetMM.substring(11).replaceFirst("[.][^.]+$", ""), targetMM,
				targetModel, "false", "true");

		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(etlscript)
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		return runConfig;

	}
	
	public String executeEOL1(String sourceModel, String sourceMM, String code)
			throws Exception {
		
		registerMM(sourceMM);
		EolModule module = new EolModule();
		module.parse(code);
		
		sourceProperties = modelProperties.properties1("M1", sourceMM,
				sourceModel, "true", "false");
		
		OutputStream outputStream = new ByteArrayOutputStream();
        EmfModel emfModel = new EmfModel();

       // System.out.println("Prop: "+sourceProperties);
        if(sourceProperties!=null)
        emfModel.load(sourceProperties);
        module.getContext().setOutputStream(new PrintStream(outputStream));
        module.getContext().getModelRepository().addModel(emfModel);

        module.execute();
        emfModel.dispose();
        String out = outputStream.toString();

        return out;
//		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
//				.withScript(code)
//				.withModel(new EmfModel(), sourceProperties)
//				.withParameter("parameterPassedFromJava", "Hello from EOL code")
//				.withProfiling()
//				.build();
		
//		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
//		sm.run();
//		runConfig.run();
//		
//		runConfig.dispose();
//		return runConfig;

	}
	
	public EolRunConfiguration executeEOL4(String sourceModel, String sourceMM, String code)
			throws Exception {
		
		registerMM(sourceMM);
		EolModule module = new EolModule();
		module.parse(code);
		
		sourceProperties = modelProperties.properties("M1", sourceMM,
				sourceModel, "true", "false");
		
//		OutputStream outputStream = new ByteArrayOutputStream();
//        EmfModel emfModel = new EmfModel();
//
//        emfModel.load(sourceProperties);
//        module.getContext().setOutputStream(new PrintStream(outputStream));
//        module.getContext().getModelRepository().addModel(emfModel);
//
//        module.execute();
//        emfModel.dispose();
//        String out = outputStream.toString();

		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
		.withScript(code)
		.withModel(new EmfModel(), sourceProperties)
		.withParameter("parameterPassedFromJava", "Hello from EOL code")
		.withProfiling()
		.build();

		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();
		runConfig.run();

		runConfig.dispose();
		return runConfig;
	}
	
	public EolRunConfiguration executeEOL3(String sourceModel, String sourceMM, Path code)
			throws Exception {
		
//		registerMM(sourceMM);
//		EolModule module = new EolModule();
//		module.parse(code);
//		module.getContext().setModule(module);
		
		sourceProperties = modelProperties.properties("M1", sourceMM,
				sourceModel, "true", "false");
		
//		OutputStream outputStream = new ByteArrayOutputStream();
//        EmfModel emfModel = new EmfModel();
//
//        emfModel.load(sourceProperties);
//        module.getContext().setOutputStream(new PrintStream(outputStream));
//        module.getContext().getModelRepository().addModel(emfModel);
//
//        module.execute();
//        emfModel.dispose();
//        String out = outputStream.toString();

        //return out;
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code")
				.withProfiling()
				.build();
		
		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();
		//runConfig.run();
		
		runConfig.dispose();
		return runConfig;

	}
	
	public EolRunConfiguration executeEOL2(String sourceModel, String sourceMM, Path code)
			throws Exception {
		
		EolModule module = new EolModule();
		module.parse(code);
		 
		sourceProperties = modelProperties.properties1("M1", sourceMM,
				sourceModel, "true", "false");
		
		OutputStream outputStream = new ByteArrayOutputStream();
        EmfModel emfModel = new EmfModel();

        emfModel.load(sourceProperties);
        module.getContext().setOutputStream(new PrintStream(outputStream));
        module.getContext().getModelRepository().addModel(emfModel);

        module.execute();
        emfModel.dispose();
        String out = outputStream.toString();

       // return out;
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code")
				.withProfiling()
				.build();
		
		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();
		runConfig.run();
		
		runConfig.dispose();
		return runConfig;

	}
	
	public EolRunConfiguration executeEOL(String sourceModel, String sourceMM, Path code)
			throws Exception {
		
		EolModule module = new EolModule();
		module.parse(code);
		 
		sourceProperties = modelProperties.properties("m", sourceMM,
				sourceModel, "true", "false");
		
		OutputStream outputStream = new ByteArrayOutputStream();
        EmfModel emfModel = new EmfModel();

//        emfModel.load(sourceProperties, (IRelativePathResolver) null);
//        module.getContext().setOutputStream(new PrintStream(outputStream));
//        module.getContext().getModelRepository().addModel(emfModel);
//
//        module.execute();
//        emfModel.dispose();
//        String out = outputStream.toString();
//
//        return out;
		
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code")
				.withProfiling()
				.build();
		
		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();
//		runConfig.run();
		
//		runConfig.dispose();
		return runConfig;

	}
	

	public int calculateMTChain(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			
//			ArrayList<String> ex_source = new ArrayList<String>();
//			ArrayList<String> ex_target = new ArrayList<String>();
			
			//System.out.println(staticAnalyser.getResolvedType(ElementWhoseTypeIsToBeDetermined));
			
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				
//				EolModelElementType newtype = null;
//				int p = 0;
//				while(p!=i)
//				{
//					newtype = (EolModelElementType) staticAnalyser
//							.getType(((EtlModule) module).getTransformationRules().get(p).getSourceParameter());
//					System.out.println("Source transformation rules are "+newtype.getTypeName()+" , "+type.getTypeName()+"\n");
//					
////					System.out.println(type.getModelName());
//					
////					String eolcode = "model "+ "Ecore"+" driver EMF {\r\n"
////							+ "nsuri = \""+ "http://www.eclipse.org/emf/2002/Ecore"+"\"\r\n"
////							+ "};\r\n" + "var type = EClass.all.select(ec|ec.name = \""+ newtype.getTypeName()+"\").first();\r\n"
////							+ "var ref = EReference.all.select(a|a.eType = type);\r\n"
////							+ "ref.containment.println();";
//					
////					String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\").first();\r\n"
////							+ "var ref = EReference.all.select(a|a.eType = type);\r\n"
////							+ "ref.containment.println();\r\n"+"if(ref.first.containment) {\r\n"+"ref.first.eContainer().name.println();\r\n type.name.println();}";
//					
////					String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
////							+ "for(cl in type) {\r\n"+ "var ref = EReference.all.select(a|a.eType = cl);\r\n"
////							+ "ref.containment.println();\r\n if(ref.eOpposite.notEmpty())\r\n ref.eOpposite.eType.name.println();\r\n"+ "for(i in ref) {\r\n"+ "if(i.containment==true) {\r\n"+ "i.eContainer().name.println();\r\n cl.name.println();}}}";
//					
//					String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
//							+ "for(cl in type) {\r\n"+ "var ref = EReference.all.select(a|a.eType = cl);\r\n"
//							+ "ref.containment.println();\r\n"+ "for(i in ref) {\r\n"+ "i.name.print();\r\n \" : \".print();\r\n i.eContainer().name.println();}}";
//					
//					FileWriter fw = new FileWriter(scriptRoot.resolve("Dependency" + ".eol").toString());
//					fw.write(eolcode);
//					fw.close();
//					
//					String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
////					String str = URI.createURI(metaMM).toFileString();
////					System.out.println(uri.getPath());
////					File file = new File(uri.getPath());
////					System.out.println(file.getParent());
//					
//					String sourceMM = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//					
//					EolRunConfiguration ex = executeEOL1(sourceMM, metaMM, scriptRoot.resolve("Dependency" + ".eol").toString());
////					System.out.println(ex.getResult());
////					ex.dispose();
//					scriptRoot.resolve("Dependency" + ".eol").toFile().delete();
//					
//					p++;
//				}
				
//				if(i+1 < ((EtlModule) module).getTransformationRules().size())
//				{
//					newtype = (EolModelElementType) staticAnalyser
//							.getType(((EtlModule) module).getTransformationRules().get(i+1).getSourceParameter());
//					System.out.println("Source transformation rule "+type.getTypeName()+"to"+newtype.getTypeName());
//					
//				}
				
				
				//staticAnalyser.getResolvedType(module.getMain().getStatements().get(0).getExpression());
//				System.out.println(type.getTypeName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
					
//					String metaMM1x = null;
//					int sum3x=0, sum4x=0;
					
					
//					System.out.println(((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
//						.size());
//					int q=0;
//					EolModelElementType newtype1 = null;
					
//					while(q!=j)
//					{
//						System.out.println("vjhbgkj");
//						newtype1 = (EolModelElementType) staticAnalyser
//								.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(q));
						
//						System.out.println("Target transformation rules are "+newtype1.getTypeName()+" , "+type1.getTypeName()+"\n");
//						System.out.println("Target rules types "+type1.getTypeName());
						
					System.out.println("Transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
							+ type1.getTypeName() + "\n");
					
//						String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
//								+ "for(cl in type) {\r\n"+ "var ref = EReference.all.select(a|a.eType = cl);\r\n"
//								+ "ref.containment.println();\r\n if(not ref.eOpposite.isDefined)ref.eOpposite.eType.name.println();\r\n"+ "for(i in ref) {\r\n"+ "if(i.containment==true) {\r\n"+ "i.eContainer().name.println();\r\n cl.name.println();}}}";
//					try {
						System.out.println("Source Type: "+type.getTypeName());	
						String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\"); //EClass.all.size.println();EAttribute.all.size.println();EReference.all.size.println();\r\n"
									+ "for(cl in type) {\r\n"+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
									+ "for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
							
//							FileWriter fw = new FileWriter(scriptRoot.resolve("Dependency" + ".eol").toString());
//							fw.write(eolcode);
//							fw.close();
							
							String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
							
//							String ex = executeEOL1(sourceMM, metaMM, scriptRoot.resolve("Dependency" + ".eol"));
							String ex = executeEOL1(sourceMM, metaMM, eolcode);
							
							System.out.println(ex);
							
//							scriptRoot.resolve("Dependency" + ".eol").toFile().delete();
							
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						//e.printStackTrace();
//						System.out.println(e);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						//e.printStackTrace();
//						System.out.println(e);
//					}
					
					
					String eolcode_cl = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");"
							+ "for(cl in type) {\r\n"+ "var ref = EReference.all.select(a|a.eType = cl);\r\n var cont; "
							+ "for(i in ref) {"+ "cont=i.eContainer().name;\r\n} cont.print();}\r\n";

//					FileWriter fw_cl = new FileWriter(scriptRoot.resolve("Dependency_cl" + ".eol").toString());
//					fw_cl.write(eolcode_cl);
//					fw_cl.close();
					
					String metaMM_cl = "http://www.eclipse.org/emf/2002/Ecore";
					
					String sourceMM_cl = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
					
//					ex_cl = executeEOL1(sourceMM_cl, metaMM_cl, scriptRoot.resolve("Dependency_cl" + ".eol"));
					ex_cl = executeEOL1(sourceMM_cl, metaMM_cl, eolcode_cl);
					
					System.out.println(ex_cl);
					
//					scriptRoot.resolve("Dependency_cl" + ".eol").toFile().delete();
					
					//System.out.println(staticAnalyser.getResolvedType(ex_cl.split("\n)));
						
					//Graph graph = new Graph(ex_cl);
					
//						try {
							System.out.println("Target Type: "+type1.getTypeName());
							String eolcode1 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
									+ "for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println();\r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
							
//							FileWriter fw1 = new FileWriter(scriptRoot.resolve("Dependency1" + ".eol").toString());
//							fw1.write(eolcode1);
//							fw1.close();
							
							String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
							
//							String ex1 = executeEOL1(sourceMM1, metaMM1, scriptRoot.resolve("Dependency1" + ".eol"));
							String ex1 = executeEOL1(sourceMM1, metaMM1, eolcode1);

							
							System.out.println(ex1);
							
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							//e.printStackTrace();
//							System.out.println(e);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							//e.printStackTrace();
//							System.out.println(e);
//						}
//						scriptRoot.resolve("Dependency1" + ".eol").toFile().delete();
						
						String eolcode_cl1 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\"); \r\n"
								+ "for(cl in type) {\r\n"+ "var ref = EReference.all.select(a|a.eType = cl);\r\n var cont;"
								+ "for(i in ref) {"+ "cont=i.eContainer().name;\r\n} cont.println(); }\r\n";

//						FileWriter fw_cl1 = new FileWriter(scriptRoot.resolve("Dependency_cl1" + ".eol").toString());
//						fw_cl1.write(eolcode_cl1);
//						fw_cl1.close();
						
						String metaMM1_cl1 = "http://www.eclipse.org/emf/2002/Ecore";
						
						String sourceMM1_cl1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
						
//						String ex_cl1 = executeEOL1(sourceMM1_cl1, metaMM1_cl1, scriptRoot.resolve("Dependency_cl1" + ".eol"));
						String ex_cl1 = executeEOL1(sourceMM1_cl1, metaMM1_cl1, eolcode_cl1);
						
						System.out.println(ex_cl1);
//						scriptRoot.resolve("Dependency_cl1" + ".eol").toFile().delete();
						
//						q++;
//					}
					
//					if(j+1 < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
//						.size())
//					{
//						EolModelElementType newtype1 = (EolModelElementType) staticAnalyser
//								.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j+1));
//						System.out.println("123");
//						
//						System.out.println("Target transformation rule "+type1+"to"+newtype1);
//						
//					}
					
//					System.out.println("Transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName() + "\n");
//					System.out.println(type.getModelName()+" "+type1.getModelName());
//					System.out.println(type +" "+type1);
				//}

				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				int c = 0;
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println("ExpName: "+expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					System.out.println("Statement number " + c);
					System.out.println(statementName + "\n" + expressionName + "\n");
					System.out.println(stName.getChildren());
					System.out.println(statementName.split(" ")[0]);
					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {
//						exp = expName.get(l).toString();
						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
//								System.out.println(totexpName.get(m));
//								System.out.println(totexpName.get(m).toString().split(" ")[0].substring(1));
								sumofoperation = sumofoperation + totexpSize;
								//System.out.println(totexpSize);
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();
									String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
									//if(n<1)
									System.out.println("xgfchg "+totexpName.get(m).get(n));
									if(!totexpName.get(m).get(n).getChildren().isEmpty())
										System.out.println("child "+totexpName.get(m).get(n).getChildren());
								//	System.out.println(x.equals("select"));
									//if(!x.equals("select")) {
//									EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
//									expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
//									//System.out.println("fcgsxxfz "+staticAnalyser.getResolvedType((Expression) ));
//									System.out.println("Eol Type: "+expr);
//									System.out.println("Resolved Type: "+expr_str);
//									System.out.println("Container value: "+ex_cl);
									//}
									
									if(expname.indexOf("name=")>0)
									{
										System.out.println("3454 "+x);
										
										
//										String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\"); //EClass.all.size.println();EAttribute.all.size.println();EReference.all.size.println();\r\n"
//												+ "for(cl in type) {\r\n"+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
//												+ "for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
										
										//String eolcode_s = ex_cl+".eAllReferences.println();\r\n";
										//try {
//											String eolcode_s = "var type=EClass.all.select(ec|ec.name!=\""+type.getTypeName()+"\");\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" etyped as \".print();"+"cl.name.print();" + "\" is dependent feature in \".print();"
//													+"i.eContainer().name.println();}\r\n}}";
											
//											String eolcode_s = "var type=EClass.all.select(ec|ec.name!=\""+type.getTypeName()+"\");\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+"i.eContainer().name.println();}\r\n}}";
											
//											String eolcode_s = "var type=EClass.all;\r\n //type.eSuperTypes.println();\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n //if(ref.name==type.eSuperTypes.name)"+"\"111\".println();\r\n"
//													+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+"i.eContainer().name.println();}\r\n}}";
//											
////											
//											FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//											fw_s.write(eolcode_s);
//											fw_s.close();
//											
//											String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//											
//											String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//											
//											System.out.println(ex_s);
//											
//											if(!ex_s.isEmpty())
//												ex_source.add(ex_s);
											
//											String[] split_s = ex_s.split("\\s+");
											
//											if(split_s.length>1)
//											{
//												for(int sp_s=0;sp_s<split_s.length;sp_s++)
//													System.out.println(split_s[sp_s]);
//												
//												Node a1 = new Node(split_s[0]);
//												Node b1 = new Node(split_s[1]);
//												Node c1 = new Node(split_s[2]);
//												
//												ArrayList<Node> list = new ArrayList<Node>();
//												list.add(a1);
//												list.add(b1);
//												list.add(c1);
//												
//												Graph g = new Graph(list);
//												g.addEdge(c1, b1);
//												g.addEdge(c1, a1);
//												
//												System.out.println("\nSource Adjacency List: ");
//												g.printAdjList();
//											}
											

//										scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
										
//										if(!type1.getModelName().isEmpty())
//										{
//											String eolcode_t = "var type=EClass.all.select(ec|ec.name!=\""+type1.getTypeName()+"\");\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" etyped as \".print();"+"cl.name.print();" + "\" is dependent feature in \".print();"
//													+"i.eContainer().name.println();}\r\n}}";
											
//											String eolcode_t = "var type=EClass.all.select(ec|ec.name!=\""+type1.getTypeName()+"\");\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+"i.eContainer().name.println();}\r\n}}";
										
//										String eolcode_t = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+"i.eContainer().name.println();}\r\n}}";
//											
////											
//											FileWriter fw_t = new FileWriter(scriptRoot.resolve("Dependency_t" + ".eol").toString());
//											fw_t.write(eolcode_t);
//											fw_t.close();
//											
//											String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//											
//											String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
//											
//											System.out.println(ex_t);
//											
//											if(!ex_t.isEmpty())
//												ex_target.add(ex_t);
											
//											String[] split_t = ex_t.split("\\s+");
											
//											if(split_t.length>1)
//											{
//												for(int sp_t=0;sp_t<split_t.length;sp_t++)
//													System.out.println(split_t[sp_t]);
//												
//												Node a1 = new Node(split_t[0]);
//												Node b1 = new Node(split_t[1]);
//												Node c1 = new Node(split_t[2]);
//												
//												ArrayList<Node> list = new ArrayList<Node>();
//												list.add(a1);
//												list.add(b1);
//												list.add(c1);
//												
//												Graph g = new Graph(list);
//												g.addEdge(c1, b1);
//												g.addEdge(c1, a1);
//												
//												System.out.println("\nTarget Adjacency List: ");
//												g.printAdjList();
//											}
											
//											for(int sp_t=0;sp_t<split_t.length;sp_t++)
//												System.out.println(split_t[sp_t]);
											
//											scriptRoot.resolve("Dependency_t" + ".eol").toFile().delete();
										//}
										
									}
									}
										
									//System.out.println("2123");
									//System.out.println(staticAnalyser.getResolvedType((Expression) x));
									
//									System.out.println("bjsbj "+staticAnalyser.getResolvedType(((Expression)((ExpressionStatement) module.getMain().getStatements().get(0)).getExpression())));
									//System.out.println("vdkjnd "+module.getDeclaredModelDeclarations().get(0));
									
									// use eol code to find reference of 2nd expression
									
									//System.out.println(expr.isKind(ex_cl));
//									if(expr_str!=null)
//									if(ex_cl.replaceAll("\\s","").equals(expr_str.replaceAll("\\s","")))
//										System.out.print(type.getTypeName()+" depends on "+ expr_str+"\n");
									
//									if(!totexpName.get(m).get(n).getChildren().isEmpty())
//									{
//										System.out.println(totexpName.get(m).get(n).getChildren());
//										for(int n_ch=0;n_ch<totexpName.get(m).get(n).getChildren().size();n_ch++)
//										{
//											String expname1 = totexpName.get(m).get(n).getChildren().toString();
//											EolType expr_child = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n).getChildren().get(n_ch));
//											String expr_str_child = expr_child.getName().substring(expr_child.getName().indexOf("!")+1);
//											if(expname1.indexOf("name=")>0)
//												System.out.println(expname1.substring(expname1.lastIndexOf("name=")+5).split(",")[0]);
//											System.out.println("Resolved Type: "+expr_str_child);
//											System.out.println("Container value: "+ex_cl);
//											//System.out.println(expr.isKind(ex_cl));
//											if(ex_cl.replaceAll("\\s","").equals(expr_str_child.replaceAll("\\s","")))
//												System.out.print(type.getTypeName()+" depends on "+ expr_str_child+"\n");
//											
////											if(!totexpName.get(m).get(n).getChildren().get(n_ch).getChildren().isEmpty())
////											{
////												System.out.println(totexpName.get(m).get(n).getChildren().get(n_ch).getChildren());
////												for(int n_ch1=0;n_ch1<totexpName.get(m).get(n).getChildren().get(n_ch).getChildren().size();n_ch1++)
////												{
////													String expname11 = totexpName.get(m).get(n).getChildren().get(n_ch).getChildren().toString();
////													EolType expr_child1 = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n).getChildren().get(n_ch).getChildren().get(n_ch1));
////													String expr_str_child1 = expr_child1.getName().substring(expr_child1.getName().indexOf("!")+1);
////													if(expname11.indexOf("name=")>0)
////														System.out.println(expname11.substring(expname11.lastIndexOf("name=")+5).split(",")[0]);
////													System.out.println("Resolved Type: "+expr_str_child1);
////													System.out.println("Container value: "+ex_cl);
////													//System.out.println(expr.isKind(ex_cl));
////													if(ex_cl.replaceAll("\\s","").equals(expr_str_child1.replaceAll("\\s","")))
////														System.out.print(type.getTypeName()+" depends on "+ expr_str_child1+"\n");
////													
////												}
////												
////											}
//											
//										}
//										
//									}
										
								}
								
									
							}

						}
						
					}
					
					
					System.out.println("\nNumber of expressions and operations: " + sumofoperation + "\n");
					totalfeatures = totalfeatures + sumofoperation;
				}
				totalstatement = totalstatement + totalfeatures;
				for (int j1 = 0; j1 < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j1++) {
					EolModelElementType type11 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j1));
					System.out.println("Total expressions/operators used in the transformation rule "
							+ type.getTypeName() + " to " + type11.getTypeName() + ":" + totalstatement + "\n");
				}
				totalstructuratlfeatures = totalstructuratlfeatures + totalstatement;
			}
				
			}
//			if(ex_source.size()>0)
//				System.out.println(ex_source);
//			if(ex_target.size()>0)
//				System.out.println(ex_target);
//			
//			if(ex_source.size()>0)
//				System.out.println("\nSource Dependency List: (Metaclass->Etype feature) ");
//			for(int ss=0;ss<ex_source.size();ss++)
//			{
//				String[] split_src = ex_source.get(ss).split("\\s+");
//				
//				if(split_src.length>1)
//				{
////					for(int sp_s=0;sp_s<split_src.length;sp_s++)
////						System.out.println(split_src[sp_s]);
//					
//					Node a1 = new Node(split_src[0]);
//					Node b1 = new Node(split_src[1]);
//					Node c1 = new Node(split_src[2]);
//					
//					ArrayList<Node> list = new ArrayList<Node>();
//					list.add(a1);
//					list.add(b1);
//					list.add(c1);
//					
//					Graph g = new Graph(list);
//					g.addEdge(c1, b1);
//					g.addEdge(c1, a1);
//					
//					g.printAdjList();
//				}
//			}
//			
//			if(ex_target.size()>0)
//				System.out.println("\nTarget Dependency List: (Metaclass->Etype feature) ");
//			for(int ss=0;ss<ex_target.size();ss++)
//			{
//				String[] split_trg = ex_target.get(ss).split("\\s+");
//				
//				if(split_trg.length>1)
//				{
////					for(int sp_s=0;sp_s<split_trg.length;sp_s++)
////						System.out.println(split_trg[sp_s]);
//					
//					Node a1 = new Node(split_trg[0]);
//					Node b1 = new Node(split_trg[1]);
//					Node c1 = new Node(split_trg[2]);
//					
//					ArrayList<Node> list = new ArrayList<Node>();
//					list.add(a1);
//					list.add(b1);
//					list.add(c1);
//					
//					Graph g = new Graph(list);
//					g.addEdge(c1, b1);
//					g.addEdge(c1, a1);
//				
//					g.printAdjList();
//				}
//				
//			}
//			System.out.println();
			
		}
		return totalstructuratlfeatures;

	}
	
	public ArrayList<String> srcDependency(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = new ArrayList<String>();
		//ArrayList<String> ex_target = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);
		
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
		
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				int c = 0;
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
									//System.out.println("1234 "+expname);
									EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
									expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									if(expname.indexOf("name=")>0)
									{
										//String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
										//System.out.println("ghfh "+x1);
										String eolcode_s = "var type=EClass.all;\r\n"
												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
												+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
										
//										FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//										fw_s.write(eolcode_s);
//										fw_s.close();
										
										String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
										
										String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
										
//										String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
										String ex_s = executeEOL1(sourceMM_s, metaMM_s, eolcode_s);
										
										//System.out.println("vjhfd "+ex_s);
										
										String[] line_ex_s = ex_s.split(System.lineSeparator());
										
										if(!ex_s.isEmpty())
											for(String e : line_ex_s)
												ex_source.add(e);
									
//									scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
									
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.println();}\r\n}}";
									
//									FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//									fw_s1.write(eolcode_s1);
//									fw_s1.close();
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
									
									//System.out.println("vjhfd "+ex_s);
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
										for(String e : line_ex_s1)
											ex_source.add(e);
								
//								scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
								
									}
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												//System.out.println("ghfh "+x1);
												String eolcode_s = "var type=EClass.all;\r\n"
														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
																						
//												FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//												fw_s.write(eolcode_s);
//												fw_s.close();
												
												String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
												
												String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
												
//												String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
												String ex_s = executeEOL1(sourceMM_s, metaMM_s, eolcode_s);
												
												
												String[] line_ex_s = ex_s.split(System.lineSeparator());
												
												if(!ex_s.isEmpty())
													for(String e : line_ex_s)
														ex_source.add(e);
											
//											scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
											
											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.println();}\r\n}}";
											
//											FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//											fw_s1.write(eolcode_s1);
//											fw_s1.close();
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
											
											//System.out.println("vjhfd "+ex_s);
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
												for(String e : line_ex_s1)
													ex_source.add(e);
										
//										scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
											}
										}
									}
									
//									if(expname.indexOf("name=")>0)
//									{
//
//											String eolcode_s = "var type=EClass.all;\r\n //type.eSuperTypes.println();\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n //if(ref.name==type.eSuperTypes.name)"+"\"111\".println();\r\n"
//													+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+"i.eContainer().name.println();}\r\n}}";
//																					
//											FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//											fw_s.write(eolcode_s);
//											fw_s.close();
//											
//											String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//											
//											String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//											
//											
//											if(!ex_s.isEmpty())
//												ex_source.add(ex_s);
//										
//										scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
//										
//										
//									}
									
//									expName_ch = totexpName.get(m).get(n).getChildren();
//									if(!expName_ch.isEmpty())
//									{
//										for(int ch=0;ch<expName_ch.size();ch++)
//										{
//											System.out.println("5678 "+expName_ch.get(ch));
//										}
//									}
										
									}
									

										
								}
								
									
							}

						}
						
					}
					
					
				}
//				if(ex_source.size()>0)
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ex_source.stream().distinct().collect(Collectors.toList()));
				

			}
				
			}
			if(ex_source.size()>0)
				System.out.println(ex_source);
			
		}
		return (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
		

	}
	
	public ArrayList<String> srcDependency1(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();
		//ArrayList<String> ex_target = new ArrayList<String>();

		
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			EolModelElementType type = null; 
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				EolModelElementType type1 = null;
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
				
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

				for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).size();ele++) 
				{
//					registerMM(metamodelsRoot.resolve(type.getModelName()+".ecore").toAbsolutePath().toString());
//					registerMM(metamodelsRoot.resolve(type1.getModelName()+".ecore").toAbsolutePath().toString());
					int c = 0;
					module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).get(ele)));
					
				//}
//				System.out.println("Source Type: "+type.getModelName());
//				System.out.println("Target Target: "+type1.getModelName());
				
				//exs.add(identifyETL(metamodelPath+"/"+type.getModelName(), metamodelPath+"/"+expr_str))
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									
									//System.out.println("1234 "+expname);
									//EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
									//expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("x_src "+x);
										//String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
										
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") { if(cl1.name!=i1.eContainer().name) { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();}\r\n else { i1.name.print();"+ "\" \".print();" +"cl1.name.println();}}\r\n}}}}\r\n";
										//System.out.println("sghtde "+x);
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+  "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();\r\n}\r\n}}}}\r\n";
										
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\r\n}\r\n}}}}\r\n";
//										
//										FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//										fw_s.write(eolcode_s);
//										fw_s.close();
//										
//										String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//										
//										String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//										//System.out.println("ghfh ");
//										String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//										
//										//System.out.println("vjhfd "+ex_s);
//										
//										String[] line_ex_s = ex_s.split(System.lineSeparator());
//										
//										if(!ex_s.isEmpty())
//											for(String e : line_ex_s)
//												ex_source.add(e);
//									
//									scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
									
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") { if(cl.name!=i.eContainer().name) { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println();} else { i.name.print();"+ "\" \".print();"+"cl.name.println();}}\r\n}}";
									
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println(); }\r\n}}";
										
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//									fw_s1.write(eolcode_s1);
//									fw_s1.close();
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
									
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e+" "+module.getFile().getName());
											System.out.println(e);
										}
											
									}
										
								
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e);
											//System.out.println("attr: "+e);
										}
											
									}
										
//									scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
								
									}
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												//System.out.println("x1_src_ch "+x1);
//												String eolcode_s = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
//												String eolcode_s = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"}\r\n}}}}\r\n";
//																						
//												FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//												fw_s.write(eolcode_s);
//												fw_s.close();
//												
//												String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//												
//												String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//												
//												String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//												
//												
//												String[] line_ex_s = ex_s.split(System.lineSeparator());
//												
//												if(!ex_s.isEmpty())
//													for(String e : line_ex_s)
//														ex_source.add(e);
//											
//											scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
											
//											String eolcode_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.println();}\r\n}}";
											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//											fw_s1.write(eolcode_s1);
//											fw_s1.close();
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
											
//											if(!ex_s1.isEmpty())
//												System.out.println("print1: "+ex_s1);
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
											{
												for(String e : line_ex_s1)
												{
													ex_source.add(e+" "+module.getFile().getName());
													System.out.println(e);
												}
													
											}
										
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//											fw__attr_s1.write(eolcode_attr_s1);
//											fw__attr_s1.close();
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print: "+ex_s1);
											//System.out.println("vjhvhjfd "+ex_s1);
											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e);
													//System.out.println("attr: "+e);
												}
													
											}
												
//											scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
											}
										}
									}
									
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}
//				System.out.println("fbkdnf "+(module.getFile().getName()));
//				System.out.println("bvksjdgbks: "+identifyETL(metamodelPath+"/"+type.getModelName()+".ecore", metamodelPath+"/"+expr_str+".ecore").get(ele));
//				ex_source.add(identifyETL(metamodelPath+"/"+type.getModelName()+".ecore", metamodelPath+"/"+expr_str+".ecore").get(ele));
				//ex_source.add(module.getFile().getName());
				ex_source.addAll(ex_attr_source);
				ex_source=(ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
				exs = (ArrayList<String>) ex_source;
//				if(ex_source.size()>0)
//				{
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+exs);
//					
//				}
					
		
			}
					
			}
//			if(ex_source.size()>0)
//				System.out.println("Source Dependency List: "+exs);
			//System.out.println(exs.get(1));
			
		}
		}
		return exs;
		

	}
	
//	public List<List<ModuleElement>> calculateExpressions1(OperationList list) {
//
//		List<ModuleElement> opName = null;
//		List<List<ModuleElement>> op = new ArrayList<List<ModuleElement>>();
//		for (int i = 0; i < list.size(); i++) {
//			opName = list.get(i).getChildren();
//			if (opName.isEmpty()) {
//				//if(expName==expName.get(i))
//				op.add(list);
////				if(expName!=opName)
////					System.out.println(i+" "+expName);
//				return op;
//				//continue;
//			} else {
//				op.add(opName);
//				calculateExpressions1(opName);
////				System.out.println(i+" "+opName);
//			}
//		}
////		System.out.println("123"+op);
//		
//		return op;
//	}
	
	public ArrayList<String> srcDependency1_new(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName = null, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();

		
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			EolModelElementType type = null; 
			EolModelElementType type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				//System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
				
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

//				for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
//						metamodelPath + "/" + type1.getModelName()).size();ele++) 
//				{
					int c = 0, c1=0;
					module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).get(0)));
					
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				//module.getDeclaredOperations().get(0).getBody()	
				if(((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
				List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard().getBody().getChildren();
				List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
				System.out.println("Guard: "+dec_guard);
				}
				
				
				for(int dop=0;dop<module.getDeclaredOperations().size();dop++) {
					//System.out.println("Operations");
					//System.out.println(module.getDeclaredOperations().get(dop).getBody());
					//System.out.println(module.getDeclaredOperations().get(dop).getBody().getChildren());
//					Operation dec_op = module.getDeclaredOperations().get(dop);
//					List<ModuleElement> dec_op_child = dec_op.getChildren();
					//System.out.println(dop+" "+module.getDeclaredOperations().get(dop)+module.getDeclaredOperations().get(dop).getChildren());
					List<List<ModuleElement>> dec_op = calculateExpressions(module.getDeclaredOperations().get(dop).getBody().getChildren());
					//System.out.println(dec_op);
					c1++;
					for (int m = 0; m < dec_op.size(); m++) {
						int totexpSize = dec_op.get(m).size();
						//System.out.println(totexpSize);
						for(int n=0;n<dec_op.get(m).size();n++)
						{
							if(dec_op.get(m).size()>0)
							{
				
							String expname = dec_op.get(m).get(n).toString();
							//System.out.println(dec_op+" "+expname);
							//checkattr_ref(totexpName.get(m).get(n));
							
							expName_ch = dec_op.get(m).get(n).getChildren();
					
							//ex_source.add(String.valueOf(i + 1));
							if(expname.indexOf("name=")>0)
							{
								//System.out.println(expname);
								String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
								//System.out.println("x source: "+x);
								
//								String eolcode_s1 = "var type=EClass.all;\r\n"
//										+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//										+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//										+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
								
							String eolcode_s1 = "var type=EClass.all;\r\n"
									+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
									+ "i.eContainer().name.print();"+"\" \".print();"
									+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
							
							String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
							
//							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
							//System.out.println(ex_s1);
							String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
							
							if(!ex_s1.isEmpty())
							{
								for(String e : line_ex_s1)
								{
									ex_source.add(e+" "+module.getFile().getName());
//									if(x.equals("equivalent".trim()) || x.equals("equivalents".trim()))
//										ex_source.add(e+" "+module.getFile().getName()+" "+"true");
//									else
//										ex_source.add(e+" "+module.getFile().getName()+" "+"false");
									//System.out.println("list array "+e);
								}
									
							}
								
						
//							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//									+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//									+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
							
							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
									+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
									+ "i.eAttributeType.name.print();"+"\" \".print();"
									+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
							
//							FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//							fw__attr_s1.write(eolcode_attr_s1);
//							fw__attr_s1.close();
							
							String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
							
//							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
							
//							String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
//							System.out.println(src1);
//							System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
//							System.out.println("x: "+x);
//							System.out.println("Attr: "+ex_attr_s1);
							
							String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
							
							if(!ex_attr_s1.isEmpty())
							{
								for(String e : line_ex_attr_s1)
								{
									ex_attr_source.add(e+" "+module.getFile().getName());
//									if(x.equals("equivalent".trim()) || x.equals("equivalents".trim()))
//										ex_attr_source.add(e+" "+module.getFile().getName()+" "+"true");
//									else
//										ex_attr_source.add(e+" "+module.getFile().getName()+" "+"false");
									//System.out.println("attr: "+e);
								}
									
							}
								
						
							}
							
							
							ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
							
							if(!expName_ch.isEmpty())
							{
								for(int ch=0;ch<expch.size();ch++)
								{
									if(expch.get(ch).toString().indexOf("name=")>0)
									{
										String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
									
										//System.out.println("x1 source: "+x1);
										
//										String eolcode_s1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
									
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e+" "+module.getFile().getName());
//											if(x1.equals("equivalent".trim()) || x1.equals("equivalents".trim()))
//												ex_source.add(e+" "+module.getFile().getName()+" "+"true");
//											else
//												ex_source.add(e+" "+module.getFile().getName()+" "+"false");
											//System.out.println("array: "+e);
										}
											
									}
								
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e+" "+module.getFile().getName());
//											if(x1.equals("equivalent".trim()) || x1.equals("equivalents".trim()))
//												ex_attr_source.add(e+" "+module.getFile().getName()+" "+"true");
//											else
//												ex_attr_source.add(e+" "+module.getFile().getName()+" "+"false");
											//System.out.println("attr: "+e);
										}
											
									}
										
									}
								}
							}
	
								
							//}
							
						}
						
							
					}
				}
				
				
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									//System.out.println(expname);
									//checkattr_ref(totexpName.get(m).get(n));
									
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("source: "+x);
										
//										String eolcode_s1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
									//System.out.println(ex_s1);
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e+" "+module.getFile().getName());
											//System.out.println(e);
										}
											
									}
										
								
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
									
//									String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
//									System.out.println(src1);
//									System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
//									System.out.println("x: "+x);
//									System.out.println("Attr: "+ex_attr_s1);
									
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e+" "+module.getFile().getName());
											//System.out.println("attr: "+e);
										}
											
									}
										
								
									}
									
									
									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expch.size();ch++)
										{
											if(expch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
											
												//System.out.println("x1 source: "+x1);
												
//												String eolcode_s1 = "var type=EClass.all;\r\n"
//														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
												
											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"
													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
											
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
											
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
											{
												for(String e : line_ex_s1)
												{
													ex_source.add(e+" "+module.getFile().getName());
													//System.out.println(e);
												}
													
											}
										
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eAttributeType.name.print();"+"\" \".print();"
													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
											
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e+" "+module.getFile().getName());
													//System.out.println("attr: "+e);
												}
													
											}
												
											}
										}
									}
			
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}

				ex_source.addAll(ex_attr_source);
				//exs=(ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
				//exs = (ArrayList<String>) ex_source;
				
//				if(ex_source.size()>0)
//				{
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ex_source);
//					
//				}
					
		
			}
					
			}
				//System.out.println(ex_source);
//			if(ex_source.size()>0)
//				System.out.println("Source Dependency List: "+ex_source);
			//System.out.println(exs.get(1));
			
		}
		}
		return ex_source;
		

	}
	
	public ArrayList<String> srcDependency1_new2(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName = null, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();

		
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			EolModelElementType type = null; 
			EolModelElementType type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				//System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
				
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

				for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).size();ele++) 
				{
					int c = 0, c1=0;
					module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).get(ele)));
					
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				//module.getDeclaredOperations().get(0).getBody()	
				if(((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
				List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard().getBody().getChildren();
				List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
				System.out.println("Guard: "+dec_guard);
				}
				
				
				for(int dop=0;dop<module.getDeclaredOperations().size();dop++) {
					//System.out.println("Operations");
					//System.out.println(module.getDeclaredOperations().get(dop).getBody());
					//System.out.println(module.getDeclaredOperations().get(dop).getBody().getChildren());
//					Operation dec_op = module.getDeclaredOperations().get(dop);
//					List<ModuleElement> dec_op_child = dec_op.getChildren();
					//System.out.println(dop+" "+module.getDeclaredOperations().get(dop)+module.getDeclaredOperations().get(dop).getChildren());
					List<List<ModuleElement>> dec_op = calculateExpressions(module.getDeclaredOperations().get(dop).getBody().getChildren());
					//System.out.println(dec_op);
					c1++;
					for (int m = 0; m < dec_op.size(); m++) {
						int totexpSize = dec_op.get(m).size();
						//System.out.println(totexpSize);
						for(int n=0;n<dec_op.get(m).size();n++)
						{
							if(dec_op.get(m).size()>0)
							{
				
							String expname = dec_op.get(m).get(n).toString();
							System.out.println(dec_op+" "+expname);
							//checkattr_ref(totexpName.get(m).get(n));
							
							expName_ch = dec_op.get(m).get(n).getChildren();
					
							//ex_source.add(String.valueOf(i + 1));
							if(expname.indexOf("name=")>0)
							{
								//System.out.println(expname);
								String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
								System.out.println("x source: "+x);
								
//								String eolcode_s1 = "var type=EClass.all;\r\n"
//										+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//										+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//										+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
								
							String eolcode_s1 = "var type=EClass.all;\r\n"
									+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
									+ "i.eContainer().name.print();"+"\" \".print();"
									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
							
							String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
							
//							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
							//System.out.println(ex_s1);
							String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
							
							if(!ex_s1.isEmpty())
							{
								for(String e : line_ex_s1)
								{
									ex_source.add(e+" "+module.getFile().getName());
//									if(x.equals("equivalent".trim()) || x.equals("equivalents".trim()))
//										ex_source.add(e+" "+module.getFile().getName()+" "+"true");
//									else
//										ex_source.add(e+" "+module.getFile().getName()+" "+"false");
									//System.out.println("list array "+e);
								}
									
							}
								
						
//							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//									+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//									+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
							
							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
									+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
									+ "i.eAttributeType.name.print();"+"\" \".print();"
									+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
							
//							FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//							fw__attr_s1.write(eolcode_attr_s1);
//							fw__attr_s1.close();
							
							String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
							
//							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
							
//							String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
//							System.out.println(src1);
//							System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
//							System.out.println("x: "+x);
//							System.out.println("Attr: "+ex_attr_s1);
							
							String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
							
							if(!ex_attr_s1.isEmpty())
							{
								for(String e : line_ex_attr_s1)
								{
									ex_attr_source.add(e+" "+module.getFile().getName());
//									if(x.equals("equivalent".trim()) || x.equals("equivalents".trim()))
//										ex_attr_source.add(e+" "+module.getFile().getName()+" "+"true");
//									else
//										ex_attr_source.add(e+" "+module.getFile().getName()+" "+"false");
									//System.out.println("attr: "+e);
								}
									
							}
								
						
							}
							
							
							ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
							
							if(!expName_ch.isEmpty())
							{
								for(int ch=0;ch<expch.size();ch++)
								{
									if(expch.get(ch).toString().indexOf("name=")>0)
									{
										String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
									
										System.out.println("x1 source: "+x1);
										
//										String eolcode_s1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"
											
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
									
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e+" "+module.getFile().getName());
//											if(x1.equals("equivalent".trim()) || x1.equals("equivalents".trim()))
//												ex_source.add(e+" "+module.getFile().getName()+" "+"true");
//											else
//												ex_source.add(e+" "+module.getFile().getName()+" "+"false");
											//System.out.println("array: "+e);
										}
											
									}
								
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e+" "+module.getFile().getName());
//											if(x1.equals("equivalent".trim()) || x1.equals("equivalents".trim()))
//												ex_attr_source.add(e+" "+module.getFile().getName()+" "+"true");
//											else
//												ex_attr_source.add(e+" "+module.getFile().getName()+" "+"false");
											//System.out.println("attr: "+e);
										}
											
									}
										
									}
								}
							}
	
								
							}
							
						}
						
							
					}
				}
				
				
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									//System.out.println(expname);
									//checkattr_ref(totexpName.get(m).get(n));
									
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("source: "+x);
										
//										String eolcode_s1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"
											
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
									//System.out.println(ex_s1);
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e+" "+module.getFile().getName());
											//System.out.println(e);
										}
											
									}
										
								
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
									
//									String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
//									System.out.println(src1);
//									System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
//									System.out.println("x: "+x);
//									System.out.println("Attr: "+ex_attr_s1);
									
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e+" "+module.getFile().getName());
											//System.out.println("attr: "+e);
										}
											
									}
										
								
									}
									
									
									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expch.size();ch++)
										{
											if(expch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
											
												//System.out.println("x1 source: "+x1);
												
//												String eolcode_s1 = "var type=EClass.all;\r\n"
//														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
												
											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"
													
													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
											
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
											
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
											{
												for(String e : line_ex_s1)
												{
													ex_source.add(e+" "+module.getFile().getName());
													//System.out.println(e);
												}
													
											}
										
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eAttributeType.name.print();"+"\" \".print();"
													
													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
											
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e+" "+module.getFile().getName());
													//System.out.println("attr: "+e);
												}
													
											}
												
											}
										}
									}
			
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}

				ex_source.addAll(ex_attr_source);
				exs=(ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
				//exs = (ArrayList<String>) ex_source;
				
//				if(ex_source.size()>0)
//				{
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+exs);
//					
//				}
					
		
			}
					
			}
				System.out.println(exs);
//			if(ex_source.size()>0)
//				System.out.println("Source Dependency List: "+exs);
			//System.out.println(exs.get(1));
			
		}
		}
		return exs;
		

	}
	
	
	public ArrayList<String> srcDependency1_new_attr(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();

		
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			System.out.println(module);
			//staticAnalyser.validate(module);
			EolModelElementType type = null; 
			EolModelElementType type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				//System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
				
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

				for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).size();ele++) 
				{
					int c = 0;
					module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
						metamodelPath + "/" + type1.getModelName()).get(ele)));
					
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									//System.out.println(expname);
									//checkattr_ref(totexpName.get(m).get(n));
									
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										System.out.println("source: "+x);
										
//										String eolcode_s1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"
//											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//									
//									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//									
////									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//									//System.out.println(ex_s1);
//									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
//									
//									if(!ex_s1.isEmpty())
//									{
//										for(String e : line_ex_s1)
//										{
//											ex_source.add(e+" "+module.getFile().getName());
//											//System.out.println(e);
//										}
//											
//									}
										
								
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
									
//									String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
//									System.out.println(src1);
//									System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
//									System.out.println("x: "+x);
//									System.out.println("Attr: "+ex_attr_s1);
									
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e+" "+module.getFile().getName());
											//System.out.println("attr: "+e);
										}
											
									}
										
								
									}
									
									
									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
									
									if(!expch.isEmpty())
									{
										for(int ch=0;ch<expch.size();ch++)
										{
											if(expch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
											
												System.out.println("x1 source: "+x1);
												
//												String eolcode_s1 = "var type=EClass.all;\r\n"
//														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
												
//											String eolcode_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"
//													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//											
//											
//											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//											
////											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//											
//											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
//											
//											if(!ex_s1.isEmpty())
//											{
//												for(String e : line_ex_s1)
//												{
//													ex_source.add(e+" "+module.getFile().getName());
//													//System.out.println(e);
//												}
//													
//											}
										
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eAttributeType.name.print();"+"\" \".print();"
													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
											
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e+" "+module.getFile().getName());
													//System.out.println("attr: "+e);
												}
													
											}
												
											}
										}
									}
									
									ex_source.addAll(ex_attr_source);
									ex_source=(ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
									exs = (ArrayList<String>) ex_source;
									
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}

				
//				if(ex_source.size()>0)
//				{
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+exs);
//					
//				}
					
		
			}
					
			}
				System.out.println(exs);
//			if(ex_source.size()>0)
//				System.out.println("Source Dependency List: "+exs);
			//System.out.println(exs.get(1));
			
		}
		}
		return exs;
		

	}
	
	public ArrayList<ModuleElement> checkattr_ref(List<ModuleElement> expName_ch) {
		
		ArrayList<ModuleElement> mod = new ArrayList<ModuleElement>();
		if(!expName_ch.isEmpty())
		{
			for(ModuleElement exp : expName_ch) {
				checkattr_ref(exp.getChildren());
				mod.add(exp);
			}
			
		}

		return mod;

	}
	
	public ArrayList<String> srcDependency2(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = null;
		ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();
		ArrayList<String> exattr = new ArrayList<String>();
		//ArrayList<String> ex_target = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);
			
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				int c = 0;
				EolModelElementType type1 = null;
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									
									//System.out.println("1234 "+expname);
//									EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
//									expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("x_src "+x);
										//String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
										
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") { if(cl1.name!=i1.eContainer().name) { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();}\r\n else { i1.name.print();"+ "\" \".print();" +"cl1.name.println();}}\r\n}}}}\r\n";
										//System.out.println("sghtde "+x);
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+  "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();\r\n}\r\n}}}}\r\n";
										
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\r\n}\r\n}}}}\r\n";
//										
//										FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//										fw_s.write(eolcode_s);
//										fw_s.close();
//										
//										String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//										
//										String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//										//System.out.println("ghfh ");
//										String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//										
//										//System.out.println("vjhfd "+ex_s);
//										
//										String[] line_ex_s = ex_s.split(System.lineSeparator());
//										
//										if(!ex_s.isEmpty())
//											for(String e : line_ex_s)
//												ex_source.add(e);
//									
//									scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
									
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") { if(cl.name!=i.eContainer().name) { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println();} else { i.name.print();"+ "\" \".print();"+"cl.name.println();}}\r\n}}";
									
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println(); }\r\n}}";
										
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//									fw_s1.write(eolcode_s1);
//									fw_s1.close();
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e);
											//System.out.println(e);
										}
											
									}
										
								
//									scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
									
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e);
											//System.out.println("attr111: "+e);
										}
											
									}
										
								
//									scriptRoot.resolve("Dependency_attr_s1" + ".eol").toFile().delete();
								
									}
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												//System.out.println("x1_src "+x1);
//												String eolcode_s = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
//												String eolcode_s = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"}\r\n}}}}\r\n";
//																						
//												FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//												fw_s.write(eolcode_s);
//												fw_s.close();
//												
//												String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//												
//												String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//												
//												String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//												
//												
//												String[] line_ex_s = ex_s.split(System.lineSeparator());
//												
//												if(!ex_s.isEmpty())
//													for(String e : line_ex_s)
//														ex_source.add(e);
//											
//											scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
											
//											String eolcode_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.println();}\r\n}}";
//											String eolcode_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//											fw_s1.write(eolcode_s1);
//											fw_s1.close();
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print1: "+ex_s1);
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
											{
												for(String e : line_ex_s1)
												{
													ex_source.add(e);
													//System.out.println(e);
												}
													
											}
										
//											scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//											fw__attr_s1.write(eolcode_attr_s1);
//											fw__attr_s1.close();
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print: "+ex_s1);
											//System.out.println("vjhvhjfd "+ex_s1);
											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e);
													//System.out.println("attr: "+e);
												}
													
											}
												
										
//											scriptRoot.resolve("Dependency_attr_s1" + ".eol").toFile().delete();
											}
										}
									}
									
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}
				
				exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
				exattr = (ArrayList<String>) ex_attr_source.stream().distinct().collect(Collectors.toList());
				exs.addAll(exattr);
//				if(ex_source.size()>0)
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+exs);
		
				
			}
					
			}
			
//			if(ex_source.size()>0)
//				System.out.println(exs.addAll(exattr));
			//System.out.println(exs.get(1));
			
		}
		exs.stream().distinct().collect(Collectors.toList());
		return (ArrayList<String>) exs;
		

	}
	
	public ArrayList<String> srcDependency3(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = null;
		ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();
		ArrayList<String> exattr = new ArrayList<String>();
		//ArrayList<String> ex_target = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);
			
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				int c = 0;
				EolModelElementType type1 = null;
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
									
									
									String eolcode_s1 = "var type=EReference.all.name.asSet();\r\n"
											+"for(i in type) { if(i==\""+x+"\") {"+"i.println();}}";
									
//									FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//									fw_s1.write(eolcode_s1);
//									fw_s1.close();
									
									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e);
											//System.out.println(e);
										}
											
									}
										
								
//									scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
									
									
									String eolcode_attr_s1 = "var type=EAttribute.all.name.asSet();\r\n"
											+"for(i in type) { if(i==\""+x+"\") {"+"i.println();}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e);
											//System.out.println("attr111: "+e);
										}
											
									}
										
								
//									scriptRoot.resolve("Dependency_attr_s1" + ".eol").toFile().delete();
								
									}
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);			
											
											String eolcode_s1 = "var type=EReference.all.name.asSet();\r\n"
													+"for(i in type) { if(i==\""+x1+"\") {"+"i.println();}}";
											
//											FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//											fw_s1.write(eolcode_s1);
//											fw_s1.close();
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print1: "+ex_s1);
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
											{
												for(String e : line_ex_s1)
												{
													ex_source.add(e);
													//System.out.println(e);
												}
													
											}
										
//											scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
											
											String eolcode_attr_s1 = "var type=EAttribute.all.name.asSet();\r\n"
													+"for(i in type) { if(i==\""+x1+"\") {"+"i.println();}}";
											
//											FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//											fw__attr_s1.write(eolcode_attr_s1);
//											fw__attr_s1.close();
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e);
													//System.out.println("attr: "+e);
												}
													
											}
												
										
//											scriptRoot.resolve("Dependency_attr_s1" + ".eol").toFile().delete();
											}
										}
									}
									
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}
				
				exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
				exattr = (ArrayList<String>) ex_attr_source.stream().distinct().collect(Collectors.toList());
				exs.addAll(exattr);
//				if(ex_source.size()>0)
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+exs);
		
				
			}
					
			}
			
			
		}
		exs.stream().distinct().collect(Collectors.toList());
		return (ArrayList<String>) exs;
		

	}
	
	
	
	public ArrayList<Integer> createList(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = new ArrayList<String>();
		ArrayList<String> ex_target_etl = new ArrayList<String>();
		String ex_t = null;

//		ArrayList<String> ex_target = null;
//		ex_target = new ArrayList<String>();
		ArrayList<String> ex_attr_target = new ArrayList<String>();
		ArrayList<String> ext = new ArrayList<String>();
		
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
		
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
		
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
						
				

				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				int c = 0;
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
										
				String eolcode_t = "var type=EClass.all.name+EReference.all.name+EAttribute.all.name;\r\n"
									+ "for(t in type) t.println();";
										
				String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
										
				String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
										
//				String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
				ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);
										
										//System.out.println("List: "+ex_t);
				String[] line_ex_t = ex_t.split(System.lineSeparator());
										
				if(!ex_t.isEmpty())
					for(String e : line_ex_t)
						ex_target.add(e);	
				
				
				// check chain list for etl
				
//				int sumofoperation, totexpSize = 0;
//				totalstatement = 0;
//				totalfeatures = 0;
//				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

//						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								//sumofoperation = sumofoperation + totexpSize;
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();

									expName_ch = totexpName.get(m).get(n).getChildren();
									
									//ex_target.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("Target: "+x);
//										String eolcode_t1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_t1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"
											//+"\""+ c +"\".print();"+ "\" \".print();"
											+ "i.name.print();"+ "\" \".print();"
											+"cl.name.print();" + "\" \".println();"
											//+ "i.eContainer().name.print();"+"\" \".print();"										
											//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
											//+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											//+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"
											+"}\r\n}}";
									
									
									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
									
									String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
									
									if(!ex_t1.isEmpty())
									{
										for(String e : line_ex_t1)
										{
//											ex_target_etl.add(e+" "+module.getFile().getName());
											ex_target_etl.add(e.trim());
											//System.out.println(e);
										}
											
									}
									
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"
											//+"\""+ c +"\".print();"+ "\" \".print();"
											+ "i.name.print();"+ "\" \".print();"
											+"cl.name.print();" + "\" \".println();"
											//+ "i.eAttributeType.name.print();"+"\" \".print();"											
											//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
											//+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											//+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"
											+"}\r\n}}";
									
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
//											ex_attr_target.add(e+" "+module.getFile().getName());
											ex_attr_target.add(e.trim());
											//System.out.println("attr: "+e);
										}
											
									}
						
									}
									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
									
									if(!expch.isEmpty())
									{
										for(int ch=0;ch<expch.size();ch++)
										{
											if(expch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												System.out.println("x1 target: "+x1);
												
//											String eolcode_t1 = "var type=EClass.all;\r\n"
//														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
												
											String eolcode_t1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"
													//+"\""+ c +"\".print();"+ "\" \".print();"
													+ "i.name.print();"+ "\" \".print();"
													+"cl.name.print();" + "\" \".println();"
													//+ "i.eContainer().name.print();"+"\" \".print();"													
													//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
													//+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													//+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"
													+"}\r\n}}";
	
											
											String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
											
//											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
											
											String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
											
											if(!ex_t1.isEmpty())
											{
												for(String e : line_ex_t1)
												{
//													ex_target_etl.add(e+" "+module.getFile().getName());
													ex_target_etl.add(e.trim());
													//System.out.println(e);
												}
													
											}
												
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"
													//+"\""+ c +"\".print();"+ "\" \".print();"
													+ "i.name.print();"+ "\" \".print();"
													+"cl.name.print();" + "\" \".println();"
													//+ "i.eAttributeType.name.print();"+"\" \".print();"													
													//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
													//+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													//+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"
													+"}\r\n}}";

											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
//													ex_attr_target.add(e+" "+module.getFile().getName());
													ex_attr_target.add(e.trim());
													//System.out.println("attr: "+e);
												}
													
											}
												
										
											}
										}
									}
									
									ex_target_etl.addAll(ex_attr_target);
									ext=(ArrayList<String>) ex_target_etl.stream().distinct().collect(Collectors.toList());
									//ext = (ArrayList<String>) ex_target;
									}
											
								}
								
									
							}

						}
						
					}
					
				}
				
				
//				if(ex_target.size()>0)
//				{
//					System.out.println("Target dependency list of "+"transformation rule " + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ext);
//					
//				}
					
				}

			}
				
			}

		ex_target=(ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
		
		System.out.println("\nTransformation whole chain list: "+ext);
		System.out.println("\nMetamodel chain list: "+ex_target);
		
		ArrayList<String> list_etl = new ArrayList<String>();
		for(String x : ext) {
			list_etl.add(x.split(" ")[0]);
			list_etl.add(x.split(" ")[1]);
		}
		
		list_etl=(ArrayList<String>) list_etl.stream().distinct().collect(Collectors.toList());
		System.out.println("\nTransformation chain list: "+list_etl);
		
		ArrayList<Integer> newlist = new ArrayList<Integer>();
		for(String mcl : ex_target) {
			//newlist = new ArrayList<Integer>();
			int c=0;
			for(String tcl : list_etl) {
				c++;
				if(mcl.equals(tcl)) {
					newlist.add(1);
					break;
				}
				else if(!mcl.equals(tcl) && c==list_etl.size()) {
					newlist.add(0);
				}
			}
		}
		
		System.out.println("\n"+newlist);
		
		return newlist;
		//return ex_t;
		//return ex_target;
	}
	
	
	public ArrayList<String> createListInETL(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = new ArrayList<String>();
		String ex_t = null;

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
		
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
		
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
						
				

				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				int c = 0;
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				List<ModuleElement> me;
				Statement st;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					st = ruleblock.getStatements().get(k);
					me = st.getChildren();
				}
										
				String eolcode_t = "var type=EClass.all.name+EReference.all.name+EAttribute.all.name;\r\n"
									+ "for(t in type) t.println();";
										
				String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
										
				String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
										
//				String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
				ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);
										
										//System.out.println("List: "+ex_t);
				String[] line_ex_t = ex_t.split(System.lineSeparator());
										
				if(!ex_t.isEmpty())
					for(String e : line_ex_t)
						ex_target.add(e);						
					
				}

			}
				
			}

		return (ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
		//return ex_t;
		//return ex_target;
	}
	
	
	public ArrayList<String> trgDependency(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);
		
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
		
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
						
				

				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				int c = 0;
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

//						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								//sumofoperation = sumofoperation + totexpSize;
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();
									String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];

									EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
									expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
									expName_ch = totexpName.get(m).get(n).getChildren();
									
									if(expname.indexOf("name=")>0)
									{
										//String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
										//System.out.println("ghfh "+x1);
										String eolcode_t = "var type=EClass.all;\r\n"
												+ "for(cl1 in type) {if(cl1.eSuperTypes.notEmpty()){ {for(sup in cl1.eSuperTypes)var ref1=EReference.all.select(a|a.eType = sup);\r\n"
												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
												+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
																				
//										FileWriter fw_t = new FileWriter(scriptRoot.resolve("Dependency_t" + ".eol").toString());
//										fw_t.write(eolcode_t);
//										fw_t.close();
										
										String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
										
										String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
										
//										String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
										String ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);
										
										String[] line_ex_t = ex_t.split(System.lineSeparator());
										
										if(!ex_t.isEmpty())
											for(String e : line_ex_t)
												ex_target.add(e);
									
//									scriptRoot.resolve("Dependency_t" + ".eol").toFile().delete();
									
									String eolcode_t1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.println();}\r\n}}";
									
//									FileWriter fw_t1 = new FileWriter(scriptRoot.resolve("Dependency_t1" + ".eol").toString());
//									fw_t1.write(eolcode_t1);
//									fw_t1.close();
									
									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
									
									String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
									
									if(!ex_t1.isEmpty())
										for(String e : line_ex_t1)
											ex_target.add(e);
								
//								scriptRoot.resolve("Dependency_t1" + ".eol").toFile().delete();
									}
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												//System.out.println("ghfh "+x1);
												String eolcode_t = "var type=EClass.all;\r\n"
														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
																						
//												FileWriter fw_t = new FileWriter(scriptRoot.resolve("Dependency_t" + ".eol").toString());
//												fw_t.write(eolcode_t);
//												fw_t.close();
												
												String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
												
												String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
												
//												String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
												String ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);
												
												String[] line_ex_t = ex_t.split(System.lineSeparator());
												
												if(!ex_t.isEmpty())
													for(String e : line_ex_t)
														ex_target.add(e);
											
//											scriptRoot.resolve("Dependency_t" + ".eol").toFile().delete();
											
											String eolcode_t1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.println();}\r\n}}";
											
//											FileWriter fw_t1 = new FileWriter(scriptRoot.resolve("Dependency_t1" + ".eol").toString());
//											fw_t1.write(eolcode_t1);
//											fw_t1.close();
											
											String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
											
//											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
											
											String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
											
											if(!ex_t1.isEmpty())
												for(String e : line_ex_t1)
													ex_target.add(e);
										
//										scriptRoot.resolve("Dependency_t1" + ".eol").toFile().delete();
											}
										}
									}
									}
									
								}
								
									
							}

						}
						
					}
					
				}
//				if(ex_target.size()>0)
//					System.out.println("Target dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ex_target.stream().distinct().collect(Collectors.toList()));

			}
				
			}
			if(ex_target.size()>0)
				System.out.println(ex_target);
			
		}
		return (ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());

	}
	
	public ArrayList<String> trgDependency1(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = null;
		ex_target = new ArrayList<String>();
		ArrayList<String> ex_attr_target = new ArrayList<String>();
		ArrayList<String> ext = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
					for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size();ele++) 
					{
						int c = 0;
						module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).get(ele)));
						
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

//						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								//sumofoperation = sumofoperation + totexpSize;
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();
								
//									EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
//									expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
									expName_ch = totexpName.get(m).get(n).getChildren();
									
									//ex_target.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("x_trg "+x);
										//String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
										//System.out.println("ghfh "+x1);
//										String eolcode_t = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) {if(cl1.eSuperTypes.notEmpty()){ {for(sup in cl1.eSuperTypes)var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") { if(cl1.name!=i1.eContainer().name) {i.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();}else {i.name.print();"+ "\" \".print();"+"cl1.name.println();}}\r\n}}}}\r\n";
										
//										String eolcode_t = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) {if(cl1.eSuperTypes.notEmpty()){ {for(sup in cl1.eSuperTypes)var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
										
//										String eolcode_t = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) {if(cl1.eSuperTypes.notEmpty()){ for(sup in cl1.eSuperTypes){ var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"}\r\n}}}}\r\n";
//																				
//										FileWriter fw_t = new FileWriter(scriptRoot.resolve("Dependency_t" + ".eol").toString());
//										fw_t.write(eolcode_t);
//										fw_t.close();
//										
//										String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
//										
//										String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//										
//										String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
//										
//										String[] line_ex_t = ex_t.split(System.lineSeparator());
//										
//										if(!ex_t.isEmpty())
//											for(String e : line_ex_t)
//												ex_target.add(e);
//									
//									scriptRoot.resolve("Dependency_t" + ".eol").toFile().delete();
//									
//									String eolcode_t1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") { if(cl.name!=i.eContainer().name){i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println();}else {i.name.print();"+ "\" \".print();"+"cl.name.println();}}\r\n}}";
									
//									String eolcode_t1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println(); }\r\n}}";
									String eolcode_t1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"
											+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw_t1 = new FileWriter(scriptRoot.resolve("Dependency_t1" + ".eol").toString());
//									fw_t1.write(eolcode_t1);
//									fw_t1.close();
									
									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
									
									String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
									
									if(!ex_t1.isEmpty())
									{
										for(String e : line_ex_t1)
										{
											ex_target.add(e+" "+module.getFile().getName());
											System.out.println(e);
										}
											
									}
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_target.add(e);
											//System.out.println("attr: "+e);
										}
											
									}
										
										
								
//								scriptRoot.resolve("Dependency_t1" + ".eol").toFile().delete();
									}
									//ArrayList<ModuleElement> expch = checkattr_ref(totexpName.get(m).get(n));
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												//System.out.println("x1_trg "+x1);
//												String eolcode_t = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") { if(cl1.name!=i1.eContainer().name){i.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.println();}else {i.name.print();"+ "\" \".print();"+"cl1.name.println();}}\r\n}}}}\r\n";
																						
//												String eolcode_t = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.println(); }\r\n}}}}\r\n";
												
//												String eolcode_t = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"}\r\n}}}}\r\n";
//												
//												FileWriter fw_t = new FileWriter(scriptRoot.resolve("Dependency_t" + ".eol").toString());
//												fw_t.write(eolcode_t);
//												fw_t.close();
//												
//												String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";
//												
//												String sourceMM_t = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//												
//												String ex_t = executeEOL1(sourceMM_t, metaMM_t, scriptRoot.resolve("Dependency_t" + ".eol"));
//												
//												
//												String[] line_ex_t = ex_t.split(System.lineSeparator());
//												
//												if(!ex_t.isEmpty())
//													for(String e : line_ex_t)
//														ex_target.add(e);
//											
//											scriptRoot.resolve("Dependency_t" + ".eol").toFile().delete();
											
//											String eolcode_t1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") { if(cl.name!=i.eContainer().name){ i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.println();}else {i.name.print();"+ "\" \".print();"+"cl.name.println();}}\r\n}}";
											
//											String eolcode_t1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.println();}\r\n}}";
											String eolcode_t1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw_t1 = new FileWriter(scriptRoot.resolve("Dependency_t1" + ".eol").toString());
//											fw_t1.write(eolcode_t1);
//											fw_t1.close();
											
											String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
											
//											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
											
											String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
											
											if(!ex_t1.isEmpty())
											{
												for(String e : line_ex_t1)
												{
													ex_target.add(e+" "+module.getFile().getName());
													System.out.println(e);
												}
													
											}
												
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//											fw__attr_s1.write(eolcode_attr_s1);
//											fw__attr_s1.close();
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print: "+ex_s1);
											//System.out.println("vjhvhjfd "+ex_s1);
											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_target.add(e);
													//System.out.println("attr: "+e);
												}
													
											}
												
										
//										scriptRoot.resolve("Dependency_t1" + ".eol").toFile().delete();
											}
										}
									}
									}
											
								}
								
									
							}

						}
						
					}
					
				}
				//ex_target.add(identifyETL(metamodelPath+"/"+type.getModelName()+".ecore", metamodelPath+"/"+expr_str+".ecore").get(ele));
				//ex_target.add(module.getFile().getName());
				ex_target.addAll(ex_attr_target);
				ex_target=(ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
				ext = (ArrayList<String>) ex_target;
//				if(ex_target.size()>0)
//				{
//					System.out.println("Target dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ext);
//					
//				}
					

			}
				
			}
//			if(ex_target.size()>0)
//				System.out.println("Target Dependency List: "+ext);
			}	
		}
		return ext;

	}
	
	public ArrayList<String> trgDependency1_new(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = null;
		ex_target = new ArrayList<String>();
		ArrayList<String> ex_attr_target = new ArrayList<String>();
		ArrayList<String> ext = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			
			EolModelElementType type=null, type1=null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
//					for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
//							metamodelPath + "/" + type1.getModelName()).size();ele++) 
//					{
						int c = 0, c1 =0;
						module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).get(0)));
						
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				
				//guard (optional)
//				if(((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
//				List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard().getBody().getChildren();
//				List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
//				System.out.println("Guard: "+dec_guard);
//				}
				//
				
//				for(int dop=0;dop<module.getDeclaredOperations().size();dop++) {
//					//System.out.println("Operations");
////					Operation dec_op = module.getDeclaredOperations().get(dop);
////					List<ModuleElement> dec_op_child = dec_op.getChildren();
//					//System.out.println(dop+" "+module.getDeclaredOperations().get(dop)+module.getDeclaredOperations().get(dop).getChildren());
//					List<List<ModuleElement>> dec_op = calculateExpressions(module.getDeclaredOperations().get(dop).getChildren());
//					c1++;
//					for (int m = 0; m < dec_op.size(); m++) {
//						int totexpSize = dec_op.get(m).size();
//						
//						for(int n=0;n<dec_op.get(m).size();n++)
//						{
//							if(dec_op.get(m).size()>0)
//							{
//				
//							String expname = dec_op.get(m).get(n).toString();
//							//System.out.println(expname);
//							//checkattr_ref(totexpName.get(m).get(n));
//							
//							expName_ch = dec_op.get(m).get(n).getChildren();
//					
//							//ex_source.add(String.valueOf(i + 1));
//							if(expname.indexOf("name=")>0)
//							{
//								String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
//								//System.out.println("source: "+x);
//								
////								String eolcode_s1 = "var type=EClass.all;\r\n"
////										+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
////										+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////										+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//								
//							String eolcode_s1 = "var type=EClass.all;\r\n"
//									+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
//									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//									+ "i.eContainer().name.print();"+"\" \".print();"
//									+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//							
//							String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//							
//							String sourceMM_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//							
////							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
//							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//							//System.out.println(ex_s1);
//							String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
//							
//							if(!ex_s1.isEmpty())
//							{
//								for(String e : line_ex_s1)
//								{
//									ex_target.add(e+" "+module.getFile().getName());
//									//System.out.println(e);
//								}
//									
//							}
//								
//						
////							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
////									+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
////									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////									+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
////									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//							
//							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//									+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
//									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//									+ "i.eAttributeType.name.print();"+"\" \".print();"
//									+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//							
////							FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
////							fw__attr_s1.write(eolcode_attr_s1);
////							fw__attr_s1.close();
//							
//							String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//							
//							String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//							
////							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
//							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//							
////							String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
////							System.out.println(src1);
////							System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
////							System.out.println("x: "+x);
////							System.out.println("Attr: "+ex_attr_s1);
//							
//							String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
//							
//							if(!ex_attr_s1.isEmpty())
//							{
//								for(String e : line_ex_attr_s1)
//								{
//									ex_attr_target.add(e+" "+module.getFile().getName());
//									//System.out.println("attr: "+e);
//								}
//									
//							}
//								
//						
//							}
//							
//							
//							ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
//							
//							if(!expName_ch.isEmpty())
//							{
//								for(int ch=0;ch<expch.size();ch++)
//								{
//									if(expch.get(ch).toString().indexOf("name=")>0)
//									{
//										String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
//									
//										//System.out.println("x1 source: "+x1);
//										
////										String eolcode_s1 = "var type=EClass.all;\r\n"
////												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
////												+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//										
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
//											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"
//											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//									
//									
//									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//									
////									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//									
//									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
//									
//									if(!ex_s1.isEmpty())
//									{
//										for(String e : line_ex_s1)
//										{
//											ex_target.add(e+" "+module.getFile().getName());
//											//System.out.println(e);
//										}
//											
//									}
//								
////									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
////											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
////											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
////											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//									
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
//											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eAttributeType.name.print();"+"\" \".print();"
//											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//									
//									
//									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//									
////									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//
//									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
//									
//									if(!ex_attr_s1.isEmpty())
//									{
//										for(String e : line_ex_attr_s1)
//										{
//											ex_attr_target.add(e+" "+module.getFile().getName());
//											//System.out.println("attr: "+e);
//										}
//											
//									}
//										
//									}
//								}
//							}
//	
//								
//							}
//							
//						}
//						
//							
//					}
//				}
				
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

//						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								//sumofoperation = sumofoperation + totexpSize;
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();

									expName_ch = totexpName.get(m).get(n).getChildren();
									
									//ex_target.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("Target: "+x);
//										String eolcode_t1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_t1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
									
									String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
									
									if(!ex_t1.isEmpty())
									{
										for(String e : line_ex_t1)
										{
											ex_target.add(e+" "+module.getFile().getName());
											//System.out.println(e);
										}
											
									}
									
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_target.add(e+" "+module.getFile().getName());
											//System.out.println("attr: "+e);
										}
											
									}
						
									}
//									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
//									
//									if(!expch.isEmpty())
//									{
//										for(int ch=0;ch<expch.size();ch++)
//										{
//											if(expch.get(ch).toString().indexOf("name=")>0)
//											{
//												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
//												System.out.println("x1 target: "+x1);
//												
////											String eolcode_t1 = "var type=EClass.all;\r\n"
////														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
////														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//												
//											String eolcode_t1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"
//													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//	
//											
//											String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//											
////											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
//											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
//											
//											String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
//											
//											if(!ex_t1.isEmpty())
//											{
//												for(String e : line_ex_t1)
//												{
//													ex_target.add(e+" "+module.getFile().getName());
//													//System.out.println(e);
//												}
//													
//											}
//												
////											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
////													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
////													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
////													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//											
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eAttributeType.name.print();"+"\" \".print();"
//													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//
//											
//											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//											
////											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//
//											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
//											
//											if(!ex_attr_s1.isEmpty())
//											{
//												for(String e : line_ex_attr_s1)
//												{
//													ex_attr_target.add(e+" "+module.getFile().getName());
//													//System.out.println("attr: "+e);
//												}
//													
//											}
//												
//										
//											}
//										}
//									}
									
									ex_target.addAll(ex_attr_target);
									//ext=(ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
									//ext = (ArrayList<String>) ex_target;
									}
											
								}
								
									
							//}

						}
						
					}
					
				}
				
				
//				if(ex_target.size()>0)
//				{
//					System.out.println("Target dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ex_target);
//					
//				}
					

			}
				
			}
//			if(ex_target.size()>0)
//				System.out.println("Target Dependency List: "+ex_target);
			}	
		}
		return ex_target;

	}
	
	
	public ArrayList<String> trgDependency1_new2(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = null;
		ex_target = new ArrayList<String>();
		ArrayList<String> ex_attr_target = new ArrayList<String>();
		ArrayList<String> ext = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			
			EolModelElementType type=null, type1=null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
					for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size();ele++) 
					{
						int c = 0, c1 =0;
						module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).get(ele)));
						
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				
				if(((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
				List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard().getBody().getChildren();
				List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
				System.out.println("Guard: "+dec_guard);
				}
				
				
				for(int dop=0;dop<module.getDeclaredOperations().size();dop++) {
					//System.out.println("Operations");
//					Operation dec_op = module.getDeclaredOperations().get(dop);
//					List<ModuleElement> dec_op_child = dec_op.getChildren();
					//System.out.println(dop+" "+module.getDeclaredOperations().get(dop)+module.getDeclaredOperations().get(dop).getChildren());
					List<List<ModuleElement>> dec_op = calculateExpressions(module.getDeclaredOperations().get(dop).getChildren());
					c1++;
					for (int m = 0; m < dec_op.size(); m++) {
						int totexpSize = dec_op.get(m).size();
						
						for(int n=0;n<dec_op.get(m).size();n++)
						{
							if(dec_op.get(m).size()>0)
							{
				
							String expname = dec_op.get(m).get(n).toString();
							//System.out.println(expname);
							//checkattr_ref(totexpName.get(m).get(n));
							
							expName_ch = dec_op.get(m).get(n).getChildren();
					
							//ex_source.add(String.valueOf(i + 1));
							if(expname.indexOf("name=")>0)
							{
								String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
								//System.out.println("source: "+x);
								
//								String eolcode_s1 = "var type=EClass.all;\r\n"
//										+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//										+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//										+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
								
							String eolcode_s1 = "var type=EClass.all;\r\n"
									+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
									+ "for(i in ref) { if(i.name==\""+x+"\") {"
									//+"\""+ c1 +"\".print();"+ "\" \".print();"
									+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
									//+ "i.eContainer().name.print();"+"\" \".print();"							
									//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
									+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
							
							String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
							
//							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
							String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
							//System.out.println(ex_s1);
							String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
							
							if(!ex_s1.isEmpty())
							{
								for(String e : line_ex_s1)
								{
									ex_target.add(e+" "+module.getFile().getName());
									//System.out.println(e);
								}
									
							}
								
						
//							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//									+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//									+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//									+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//									+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
							
							String eolcode_attr_s1 = "var type=EClass.all;\r\n"
									+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
									+ "for(i in ref) { if(i.name==\""+x+"\") {"
									//+"\""+ c1 +"\".print();"+ "\" \".print();"
									+ "i.name.print();"+ "\" \".print();"
									+"cl.name.print();" + "\" \".print();"
									//+ "i.eAttributeType.name.print();"+"\" \".print();"									
									//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
									+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
									+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
							
//							FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//							fw__attr_s1.write(eolcode_attr_s1);
//							fw__attr_s1.close();
							
							String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
							
//							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
							String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
							
//							String src1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, "var type=EClass.all;for(cl in type) {var ref=cl.eAllAttributes.println();}");
//							System.out.println(src1);
//							System.out.println(type.getModelName()+".ecore"+"   "+sourceMM_attr_s1);
//							System.out.println("x: "+x);
//							System.out.println("Attr: "+ex_attr_s1);
							
							String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
							
							if(!ex_attr_s1.isEmpty())
							{
								for(String e : line_ex_attr_s1)
								{
									ex_attr_target.add(e+" "+module.getFile().getName());
									//System.out.println("attr: "+e);
								}
									
							}
								
						
							}
							
							
							ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
							
//							if(!expName_ch.isEmpty())
//							{
//								for(int ch=0;ch<expch.size();ch++)
//								{
//									if(expch.get(ch).toString().indexOf("name=")>0)
//									{
//										String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
//									
//										//System.out.println("x1 source: "+x1);
//										
////										String eolcode_s1 = "var type=EClass.all;\r\n"
////												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
////												+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//										
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
//											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"
//											
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//									
//									
//									String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//									
////									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//									
//									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
//									
//									if(!ex_s1.isEmpty())
//									{
//										for(String e : line_ex_s1)
//										{
//											ex_target.add(e+" "+module.getFile().getName());
//											//System.out.println(e);
//										}
//											
//									}
//								
////									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
////											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
////											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
////											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//									
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
//											+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c1 +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eAttributeType.name.print();"+"\" \".print();"
//											
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//									
//									
//									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//									
////									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//
//									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
//									
//									if(!ex_attr_s1.isEmpty())
//									{
//										for(String e : line_ex_attr_s1)
//										{
//											ex_attr_target.add(e+" "+module.getFile().getName());
//											//System.out.println("attr: "+e);
//										}
//											
//									}
//										
//									}
//								}
//							}
	
								
							}
							
						}
						
							
					}
				}
				
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

//						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								//sumofoperation = sumofoperation + totexpSize;
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();

									expName_ch = totexpName.get(m).get(n).getChildren();
									
									//ex_target.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("Target: "+x);
//										String eolcode_t1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
									String eolcode_t1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"
											//+"\""+ c +"\".print();"+ "\" \".print();"
											+ "i.name.print();"+ "\" \".print();"
											+"cl.name.print();" + "\" \".print();"
											//+ "i.eContainer().name.print();"+"\" \".print();"										
											//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
											+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
									
									String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
									
									if(!ex_t1.isEmpty())
									{
										for(String e : line_ex_t1)
										{
											ex_target.add(e+" "+module.getFile().getName());
											//System.out.println(e);
										}
											
									}
									
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"
											//+"\""+ c +"\".print();"+ "\" \".print();"
											+ "i.name.print();"+ "\" \".print();"
											+"cl.name.print();" + "\" \".print();"
											//+ "i.eAttributeType.name.print();"+"\" \".print();"											
											//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
											+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_target.add(e+" "+module.getFile().getName());
											//System.out.println("attr: "+e);
										}
											
									}
						
									}
									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
									
									if(!expch.isEmpty())
									{
										for(int ch=0;ch<expch.size();ch++)
										{
											if(expch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												System.out.println("x1 target: "+x1);
												
//											String eolcode_t1 = "var type=EClass.all;\r\n"
//														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
												
											String eolcode_t1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"
													//+"\""+ c +"\".print();"+ "\" \".print();"
													+ "i.name.print();"+ "\" \".print();"
													+"cl.name.print();" + "\" \".print();"
													//+ "i.eContainer().name.print();"+"\" \".print();"													
													//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
													+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
	
											
											String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
											
//											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
											
											String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
											
											if(!ex_t1.isEmpty())
											{
												for(String e : line_ex_t1)
												{
													ex_target.add(e+" "+module.getFile().getName());
													//System.out.println(e);
												}
													
											}
												
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"
													//+"\""+ c +"\".print();"+ "\" \".print();"
													+ "i.name.print();"+ "\" \".print();"
													+"cl.name.print();" + "\" \".print();"
													//+ "i.eAttributeType.name.print();"+"\" \".print();"													
													//+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"
													+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";

											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_target.add(e+" "+module.getFile().getName());
													//System.out.println("attr: "+e);
												}
													
											}
												
										
											}
										}
									}
									
									ex_target.addAll(ex_attr_target);
									ext=(ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
									//ext = (ArrayList<String>) ex_target;
									}
											
								}
								
									
							}

						}
						
					}
					
				}
				
				
//				if(ex_target.size()>0)
//				{
//					System.out.println("Target dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ext);
//					
//				}
					

			}
				
			}
//			if(ex_target.size()>0)
//				System.out.println("\nTarget Dependency List: "+ext);
			}	
		}
		ext=(ArrayList<String>) ext.stream().distinct().collect(Collectors.toList());
		return ext;

	}
	
	public ArrayList<String> trgDependency1_new_attr(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		//ArrayList<String> ex_source = new ArrayList<String>();
		ArrayList<String> ex_target = null;
		ex_target = new ArrayList<String>();
		ArrayList<String> ex_attr_target = new ArrayList<String>();
		ArrayList<String> ext = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			staticAnalyser.validate(module);
			
			EolModelElementType type=null, type1=null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
					for(int ele=0;ele<identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size();ele++) 
					{
						int c = 0;
						module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).get(ele)));
						
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				//System.out.println(ruleblock);
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

//						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								//sumofoperation = sumofoperation + totexpSize;
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
										
									expname = totexpName.get(m).get(n).toString();

									expName_ch = totexpName.get(m).get(n).getChildren();
									
									//ex_target.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										System.out.println("Target: "+x);
//										String eolcode_t1 = "var type=EClass.all;\r\n"
//												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
										
//									String eolcode_t1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"
//											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//									
//									
//									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//									
////									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
//									String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
//									
//									String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
//									
//									if(!ex_t1.isEmpty())
//									{
//										for(String e : line_ex_t1)
//										{
//											ex_target.add(e+" "+module.getFile().getName());
//											//System.out.println(e);
//										}
//											
//									}
									
//									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
//											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eAttributeType.name.print();"+"\" \".print();"
											+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
											+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
											+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
									
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);

									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_target.add(e+" "+module.getFile().getName());
											//System.out.println("attr: "+e);
										}
											
									}
						
									}
//									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);
//									
//									if(!expch.isEmpty())
//									{
//										for(int ch=0;ch<expch.size();ch++)
//										{
//											if(expch.get(ch).toString().indexOf("name=")>0)
//											{
//												String x1 = expch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
//												System.out.println("x1 target: "+x1);
//												
////											String eolcode_t1 = "var type=EClass.all;\r\n"
////														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
////														+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////														+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//												
//											String eolcode_t1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"
//													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//	
//											
//											String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_t1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//											
////											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, scriptRoot.resolve("Dependency_t1" + ".eol"));
//											String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
//											
//											String[] line_ex_t1 = ex_t1.split(System.lineSeparator());
//											
//											if(!ex_t1.isEmpty())
//											{
//												for(String e : line_ex_t1)
//												{
//													ex_target.add(e+" "+module.getFile().getName());
//													//System.out.println(e);
//												}
//													
//											}
//												
////											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
////													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
////													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
////													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"
////													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
//											
//											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eAttributeType.name.print();"+"\" \".print();"
//													+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"
//													+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\" \".print();"
//													+"\""+((EtlModule) module).getTransformationRules().get(i).getName()+"\".println();"+"}\r\n}}";
//
//											
//											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
//											
//											String sourceMM_attr_s1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//											
////											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//
//											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
//											
//											if(!ex_attr_s1.isEmpty())
//											{
//												for(String e : line_ex_attr_s1)
//												{
//													ex_attr_target.add(e+" "+module.getFile().getName());
//													//System.out.println("attr: "+e);
//												}
//													
//											}
//												
//										
//											}
//										}
//									}
									
									ex_target.addAll(ex_attr_target);
									ex_target=(ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
									ext = (ArrayList<String>) ex_target;
									}
											
								}
								
									
							}

						}
						
					}
					
				}
				
				
//				if(ex_target.size()>0)
//				{
//					System.out.println("Target dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+ext);
//					
//				}
					

			}
				
			}
//			if(ex_target.size()>0)
//				System.out.println("Target Dependency List: "+ext);
			}	
		}
		return ext;

	}

	public ArrayList<String> trgDependency2(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName, expName_ch;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		String ex_cl = null;
		String expr_str = null;
		ArrayList<String> ex_source = null;
		ex_source = new ArrayList<String>();
		ArrayList<String> ex_attr_source = null;
		ex_attr_source = new ArrayList<String>();
		ArrayList<String> exs = new ArrayList<String>();
		ArrayList<String> exattr = new ArrayList<String>();
		//ArrayList<String> ex_target = new ArrayList<String>();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);
			
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				int c = 0;
				EolModelElementType type1 = null;
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					
				type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
			
				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;

				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					//System.out.println(expName);
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;

					String expname = null;
					for (int l = 0; l < numberofexpression; l++) {

						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									if(totexpName.get(m).size()>0)
									{
						
									expname = totexpName.get(m).get(n).toString();
									
									//System.out.println("1234 "+expname);
//									EolType expr = staticAnalyser.getResolvedType((Expression) totexpName.get(m).get(n));
//									expr_str = expr.getName().substring(expr.getName().indexOf("!")+1);
									expName_ch = totexpName.get(m).get(n).getChildren();
							
									//ex_source.add(String.valueOf(i + 1));
									if(expname.indexOf("name=")>0)
									{
										String x = expname.substring(expname.lastIndexOf("name=")+5).split(",")[0];
										//System.out.println("x_src "+x);
										//String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
										
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") { if(cl1.name!=i1.eContainer().name) { i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();}\r\n else { i1.name.print();"+ "\" \".print();" +"cl1.name.println();}}\r\n}}}}\r\n";
										//System.out.println("sghtde "+x);
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+  "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.println();\r\n}\r\n}}}}\r\n";
										
//										String eolcode_s = "var type=EClass.all;\r\n"
//												+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//												+ "for(i1 in ref1) { if(i1.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//												+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"\r\n}\r\n}}}}\r\n";
//										
//										FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//										fw_s.write(eolcode_s);
//										fw_s.close();
//										
//										String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//										
//										String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//										//System.out.println("ghfh ");
//										String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//										
//										//System.out.println("vjhfd "+ex_s);
//										
//										String[] line_ex_s = ex_s.split(System.lineSeparator());
//										
//										if(!ex_s.isEmpty())
//											for(String e : line_ex_s)
//												ex_source.add(e);
//									
//									scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
									
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") { if(cl.name!=i.eContainer().name) { i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println();} else { i.name.print();"+ "\" \".print();"+"cl.name.println();}}\r\n}}";
									
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.println(); }\r\n}}";
										
//									String eolcode_s1 = "var type=EClass.all;\r\n"
//											+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
										String eolcode_t1 = "var type=EClass.all;\r\n"
												+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
												+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
												+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//									fw_s1.write(eolcode_s1);
//									fw_s1.close();
									
									String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_t1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
									String ex_s1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
									
									if(!ex_s1.isEmpty())
									{
										for(String e : line_ex_s1)
										{
											ex_source.add(e);
											//System.out.println(e);
										}
											
									}
										
								
//									scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();

									
									String eolcode_attr_s1 = "var type=EClass.all;\r\n"
											+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
											+ "for(i in ref) { if(i.name==\""+x+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
											+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
									
//									FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//									fw__attr_s1.write(eolcode_attr_s1);
//									fw__attr_s1.close();
									
									String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
									
//									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
									String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//									if(!ex_s1.isEmpty())
//										System.out.println("print: "+ex_s1);
									//System.out.println("vjhvhjfd "+ex_s1);
									String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
									
									if(!ex_attr_s1.isEmpty())
									{
										for(String e : line_ex_attr_s1)
										{
											ex_attr_source.add(e);
											//System.out.println("attr111: "+e);
										}
											
									}
										
								
//									scriptRoot.resolve("Dependency_attr_s1" + ".eol").toFile().delete();
								
									}
									
									if(!expName_ch.isEmpty())
									{
										for(int ch=0;ch<expName_ch.size();ch++)
										{
											if(expName_ch.get(ch).toString().indexOf("name=")>0)
											{
												String x1 = expName_ch.get(ch).toString().substring(expname.lastIndexOf("name=")+5).split(",")[0].substring(17);
												//System.out.println("x1_src "+x1);
//												String eolcode_s = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";
//												String eolcode_s = "var type=EClass.all;\r\n"
//														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
//														+ "for(i1 in ref1) { if(i1.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i1.name.print();"+ "\" \".print();"+"cl1.name.print();" + "\" \".print();"
//														+ "i1.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\".print();"+"\" \".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".print();"+"}\r\n}}}}\r\n";
//																						
//												FileWriter fw_s = new FileWriter(scriptRoot.resolve("Dependency_s" + ".eol").toString());
//												fw_s.write(eolcode_s);
//												fw_s.close();
//												
//												String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";
//												
//												String sourceMM_s = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
//												
//												String ex_s = executeEOL1(sourceMM_s, metaMM_s, scriptRoot.resolve("Dependency_s" + ".eol"));
//												
//												
//												String[] line_ex_s = ex_s.split(System.lineSeparator());
//												
//												if(!ex_s.isEmpty())
//													for(String e : line_ex_s)
//														ex_source.add(e);
//											
//											scriptRoot.resolve("Dependency_s" + ".eol").toFile().delete();
											
//											String eolcode_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {\""+ (i+1)+"\".print();"+"\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.println();}\r\n}}";
//											String eolcode_s1 = "var type=EClass.all;\r\n"
//													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
//													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
//													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw_s1 = new FileWriter(scriptRoot.resolve("Dependency_s1" + ".eol").toString());
//											fw_s1.write(eolcode_s1);
//											fw_s1.close();
											
											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, scriptRoot.resolve("Dependency_s1" + ".eol"));
											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print1: "+ex_s1);
											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());
											
											if(!ex_s1.isEmpty())
											{
												for(String e : line_ex_s1)
												{
													ex_source.add(e);
													//System.out.println(e);
												}
													
											}
										
//											scriptRoot.resolve("Dependency_s1" + ".eol").toFile().delete();
											
											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
													+ "for(i in ref) { if(i.name==\""+x1+"\") {"+"\""+ c +"\".print();"+ "\" \".print();"+ "i.name.print();"+ "\" \".print();"+"cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();"+"\" \".print();"+"\""+type.getTypeName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getTypeName()+"\" \".print();"+"\".print();"+"\""+type.getModelName()+"\".print();"+"\""+2+"\".print();"+"\""+type1.getModelName()+"\".println();"+"}\r\n}}";
											
//											FileWriter fw__attr_s1 = new FileWriter(scriptRoot.resolve("Dependency_attr_s1" + ".eol").toString());
//											fw__attr_s1.write(eolcode_attr_s1);
//											fw__attr_s1.close();
											
											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";
											
											String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
											
//											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, scriptRoot.resolve("Dependency_attr_s1" + ".eol"));
											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1, eolcode_attr_s1);
//											if(!ex_s1.isEmpty())
//												System.out.println("print: "+ex_s1);
											//System.out.println("vjhvhjfd "+ex_s1);
											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());
											
											if(!ex_attr_s1.isEmpty())
											{
												for(String e : line_ex_attr_s1)
												{
													ex_attr_source.add(e);
													//System.out.println("attr: "+e);
												}
													
											}
												
										
//											scriptRoot.resolve("Dependency_attr_s1" + ".eol").toFile().delete();
											}
										}
									}
									
										
									}
									
								}
								
									
							}

						}
						
					}
				
				}
				
				exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
				exattr = (ArrayList<String>) ex_attr_source.stream().distinct().collect(Collectors.toList());
				exs.addAll(exattr);
//				if(ex_source.size()>0)
//					System.out.println("Source dependency list of "+"transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
//							+ type1.getTypeName()+" is "+exs);
		
				
			}
					
			}
			
//			if(ex_source.size()>0)
//				System.out.println(exs.addAll(exattr));
			//System.out.println(exs.get(1));
			
		}
		exs.stream().distinct().collect(Collectors.toList());
		return (ArrayList<String>) exs;
		

	}
	

	public List<List<ModuleElement>> calculateExpressions(List<ModuleElement> expName) {

		List<ModuleElement> opName = null;
		List<List<ModuleElement>> op = new ArrayList<List<ModuleElement>>();
		for (int i = 0; i < expName.size(); i++) {
			opName = expName.get(i).getChildren();
			if (opName.isEmpty()) {
				//if(expName==expName.get(i))
				op.add(expName);
//				if(expName!=opName)
//					System.out.println(i+" "+expName);
				return op;
				//continue;
			} else {
				op.add(opName);
				calculateExpressions(opName);
//				System.out.println(i+" "+opName);
			}
		}
//		System.out.println("123"+op);
		
		return op;
	}
	
//	public ArrayList<ArrayList<String>> multiple_identifyBestChain(String sourceModel, String sourceMM, String targetModel, String targetMM)
//			throws Exception {
//		
//		ArrayList<ArrayList<String>> identifybest = new ArrayList<ArrayList<String>>();
//		List<ArrayList<ArrayList<String>>> l1 = multiple_identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		System.out.println("vhvkj");
//		System.out.println("kjdvbsk "+l1);
//		for(int ch=0;ch<l1.size();ch++)
//		{
//			identifybest.add(identifyBestChain(sourceModel, sourceMM, targetModel, targetMM));
//		}
//		
//			return identifybest;
//		
//	}
	
//	public int deleteTransformationRule(String sourceModel, String sourceMM, String targetModel, String targetMM)
//			throws Exception {
//		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//
//		int tr = 0;
//		System.out.println("\n");
//		
//		for (int i = 0; i < l.size(); i++) {
//			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
//			int total = 0, total1 = 0;
//
//			for (int j = 0; j < l.get(i).size(); j++) {
//
//				
//				EtlModule module1 = new EtlModule();
//				EtlModule module2 = new EtlModule();
//
//				if (j + 1 < l.get(i).size()) {
//
//					//for(int id=0;id<identifyETL(metamodelPath + "/" + l.get(i).get(j), metamodelPath + "/" + l.get(i).get(j+1)).size();id++) {
//					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
//					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
//							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++)
//						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
//							metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));
//					//}
//					if (j + 1 < l.get(i).size()-1)
//					{
//						//for(int id1=0;id1<identifyETL(metamodelPath + "/" + l.get(i).get(j+1), metamodelPath + "/" + l.get(i).get(j+2)).size();id1++) {
//						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
//								metamodelPath + "/" + l.get(i).get(j + 2)).size();e1++)
//							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
//								metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));
//					//}
//						
//					total = calculateMTChain1(module1);
//				
////					total1 = calculateMTChain(module2);
//
//						
//					EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
//					
//					staticAnalyser.validate(module1);
//					staticAnalyser.validate(module2);
//					EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
//					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
//						type = (EolModelElementType) staticAnalyser
//								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
//					
//						//try {
//							for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
//									.size(); n++) {
//								
//								
//								type1 = (EolModelElementType) staticAnalyser
//										.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
//							
//								ArrayList<String> src_dep = srcDependency1(module1);
//								ArrayList<String> trg_dep = trgDependency1(module1);
//								
//								for(int gtr=0;gtr<module1.getTransformationRules().size();gtr++) {
//									for(int td=0;td<trg_dep.size();td++) {
//										System.out.println("\nThis Target Dependency: "+module1.getTransformationRules().get(gtr)+" "+trg_dep.get(td));
//									}
//									break;
//								}
//									
//							
//								if(src_dep.size()>0)
//								System.out.println("\nSource Dependency List: (Metaclass->Etype feature rule) ");
//							for(int ss=0;ss<src_dep.size();ss++)
//							{
//								String[] split_src = src_dep.get(ss).split("\\s+");
//								
//								if(split_src.length>1)
//								{
//
//									Node a1 = new Node(split_src[0]);
//									Node b1 = new Node(split_src[1]);
//									Node c1 = new Node(split_src[2]);
//									Node d1 = new Node(split_src[3]);
//									
//									ArrayList<Node> list = new ArrayList<Node>();
//									list.add(a1);
//									list.add(b1);
//									list.add(c1);
//									list.add(d1);
//									
//									Graph g = new Graph(list);
//									g.addEdge(d1, c1);
//									g.addEdge(d1, b1);
//									g.addEdge(d1, a1);
//									
//									g.printAdjList();
//								}
//							}
//							
//							if(trg_dep.size()>0)
//								System.out.println("\nTarget Dependency List: (Metaclass->Etype feature rule) ");
//							for(int ss=0;ss<trg_dep.size();ss++)
//							{
//								String[] split_trg = trg_dep.get(ss).split("\\s+");
//								
//								if(split_trg.length>1)
//								{
//
//									Node a1 = new Node(split_trg[0]);
//									Node b1 = new Node(split_trg[1]);
//									Node c1 = new Node(split_trg[2]);
//									Node d1 = new Node(split_trg[3]);
//									
//									ArrayList<Node> list = new ArrayList<Node>();
//									list.add(a1);
//									list.add(b1);
//									list.add(c1);
//									list.add(d1);
//									
//									Graph g = new Graph(list);
//									g.addEdge(d1, c1);
//									g.addEdge(d1, b1);
//									g.addEdge(d1, a1);
//								
//									g.printAdjList();
//								}
//								
//							}
//							System.out.println();
//
//							ArrayList<String> src_dep1 = srcDependency1(module2);
//							ArrayList<String> trg_dep1 = trgDependency1(module2);
//							
//							for(int gtr=0;gtr<module2.getTransformationRules().size();gtr++) {
//								for(int sd=0;sd<src_dep1.size();sd++) {
//									System.out.println("\nNext Source Dependency: "+module2.getTransformationRules().get(gtr)+" "+src_dep1.get(sd));
//								}
//								break;
//							}
//								
//							
//							if(src_dep1.size()>0)
//							System.out.println("\nSource Dependency List: (Metaclass->Etype feature rule) ");
//							for(int ss=0;ss<src_dep1.size();ss++)
//							{
//							String[] split_src = src_dep1.get(ss).split("\\s+");
//							
//							if(split_src.length>1)
//							{
//
//								Node a1 = new Node(split_src[0]);
//								Node b1 = new Node(split_src[1]);
//								Node c1 = new Node(split_src[2]);
//								Node d1 = new Node(split_src[3]);
//								
//								ArrayList<Node> list = new ArrayList<Node>();
//								list.add(a1);
//								list.add(b1);
//								list.add(c1);
//								list.add(d1);
//								
//								Graph g = new Graph(list);
//								g.addEdge(d1, c1);
//								g.addEdge(d1, b1);
//								g.addEdge(d1, a1);
//								
//								g.printAdjList();
//							}
//							}
//
//							if(trg_dep1.size()>0)
//							System.out.println("\nTarget Dependency List: (Metaclass->Etype feature rule) ");
//							for(int ss=0;ss<trg_dep1.size();ss++)
//							{
//							String[] split_trg = trg_dep1.get(ss).split("\\s+");
//							
//							if(split_trg.length>1)
//							{
//
//								Node a1 = new Node(split_trg[0]);
//								Node b1 = new Node(split_trg[1]);
//								Node c1 = new Node(split_trg[2]);
//								Node d1 = new Node(split_trg[3]);
//								
//								ArrayList<Node> list = new ArrayList<Node>();
//								list.add(d1);
//								list.add(a1);
//								list.add(b1);
//								list.add(c1);
//								
//								Graph g = new Graph(list);
//								g.addEdge(d1, c1);
//								g.addEdge(d1, b1);
//								g.addEdge(d1, a1);
//							
//								g.printAdjList();
//							}
//							
//							}
//							System.out.println();
//
//							boolean flag=false;
//							for(tr=0;tr<trg_dep.size();tr++) {
//							for(int sr=0;sr<src_dep1.size();sr++) {
//								String[] split_trg = trg_dep.get(tr).split("\\s+");
//								String[] split_src = src_dep1.get(sr).split("\\s+");
//								if((split_trg[1]+split_trg[2]+split_trg[3]).trim().equals((split_src[1]+split_src[2]+split_src[3]).trim())) {
//									//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
//									flag=false;
//									
//								}
//								else
//									//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
//									flag=true;
//							}
//							if(flag==true)
//							{
//								System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
//								module1.getTransformationRules().remove(tr);
//							}
//								
//							else
//								System.out.println("Keep "+trg_dep.get(tr)+" for next transormation.\n");
//							}
//
//							}
////						} catch (Exception e) {
////							// TODO Auto-generated catch block
////							//e.printStackTrace();
////							System.err.println(e.getMessage()+"\n");
////						}
//						
//					}
//					
//					
//					}
//					
//				}
//				
//			}
//			
//			}
//			
//		return tr;
//		}
	
	
	public ArrayList<ArrayList<Integer>> createListChain(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
//		ArrayList<String> showChainFromSourceMM = identifyChain_source(sourceMM);
//		System.out.println("Chain from source metamodel "+sourceMM+" is "+showChainFromSourceMM);
//		List<ArrayList<String>> l = EtlChainOptimiser.showAllChain(targetMM);
		
		List<ArrayList<Integer>> indexlist = new ArrayList<ArrayList<Integer>>();
		List<ArrayList<EtlModule>> totalmodulelist = new ArrayList<ArrayList<EtlModule>>();
		
				
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		System.out.println("\nChains: "+l);
		String getlist = null;
		
		for (int i = 0; i < l.size(); i++) {
			
			for(int j = 0; j < l.get(i).size();j++) {
				EtlModule module1 = new EtlModule();
				ArrayList<Integer> index = new ArrayList<Integer>();
				ArrayList<EtlModule> modulelist = new ArrayList<EtlModule>();
				if((j+1)<l.get(i).size()) {
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
						metamodelPath + "/" + l.get(i).get(j + 1)).size();e++) {
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));
						//if(module1!=null)
							index=createList(module1);
							modulelist.add(module1);
					}
				}
				//index.add(getlist);
				indexlist.add(index);
				totalmodulelist.add(modulelist);
			}
			
		}
		indexlist.removeIf(p -> p.isEmpty());
		totalmodulelist.removeIf(p -> p.isEmpty());
		indexlist = indexlist.stream().distinct().collect(Collectors.toList());
		totalmodulelist = totalmodulelist.stream().distinct().collect(Collectors.toList());
		System.out.println("\n"+totalmodulelist);
		System.out.println("\n"+indexlist);
		
		Integer[][] arr = new Integer[indexlist.size()][];
		 
        int i = 0;
        for (ArrayList<Integer> lt : indexlist) {
            arr[i++] = lt.toArray(new Integer[lt.size()]);
        }
 
        System.out.println("\nArray chain List :"+Arrays.deepToString(arr));
        
        
		return (ArrayList<ArrayList<Integer>>) indexlist;
	}
	
	public ArrayList<ArrayList<String>> createListChainETL(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> indexlist = new ArrayList<ArrayList<String>>();
		
				
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		System.out.println("Chains: "+l);
		String getlist = null;
		
		for (int i = 0; i < l.size(); i++) {
			
			for(int j = 0; j < l.get(i).size();j++) {
				EtlModule module1 = new EtlModule();
				ArrayList<String> index = new ArrayList<String>();
				if((j+1)<l.get(i).size()) {
//					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
//						metamodelPath + "/" + l.get(i).get(j + 1)).size();e++) {
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(0)));
						//if(module1!=null)
							index=trgDependency1_new2(module1);
					//}
				}
				//index.add(getlist);
				indexlist.add(index);
			}
			
		}
		indexlist.removeIf(p -> p.isEmpty());
		indexlist = indexlist.stream().distinct().collect(Collectors.toList());
		System.out.println(indexlist);
		return (ArrayList<ArrayList<String>>) indexlist;
	}

	public ArrayList<String> identifyBestChain(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		System.out.println("Chains: "+l);
		
		//}
//		double coverage = calculateMTCoverage(sourceMM, targetMM);
//		System.out.println("Coverage: "+coverage);
		int min = 99999;
		float max_cov=0;
		ArrayList<String> index = null;
		ArrayList<String> index1 = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		float[] sum_cov_chain = new float[l.size()];
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			int total = 0, total1 = 0;
			int sum_source_attributeMM = 0;
			int sum_source_referenceMM = 0;
			int sum_target_attributeMM = 0;
			int sum_target_referenceMM = 0;
			int sum_source_attributeModel = 0;
			int sum_source_referenceModel = 0;
			int sum_target_attributeModel = 0;
			int sum_target_referenceModel = 0;
			//int sum3x=0, sum4x=0;
			float cov=0;
			float prod=1, sum_cov=1;
			float tot1=0, tot2=0;
//			int sum_src1=0;
//			int sum_src2=0;
//			int sum_src3=0;
//			int sum_src4=0;
			for (int j = 0; j < l.get(i).size(); j++) {

				
				EtlModule module1 = new EtlModule();
				EtlModule module2 = new EtlModule();
				
				if(j+1<l.get(i).size()) {
					registerMM(metamodelsRoot.resolve(l.get(i).get(j)).toString());
					registerMM(metamodelsRoot.resolve(l.get(i).get(j+1)).toString());
				}
				

				if (j + 1 < l.get(i).size()) {

					//for(int id=0;id<identifyETL(metamodelPath + "/" + l.get(i).get(j), metamodelPath + "/" + l.get(i).get(j+1)).size();id++) {
					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));
					//}
					if (j + 1 < l.get(i).size()-1)
					{
						//for(int id1=0;id1<identifyETL(metamodelPath + "/" + l.get(i).get(j+1), metamodelPath + "/" + l.get(i).get(j+2)).size();id1++) {
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
								metamodelPath + "/" + l.get(i).get(j + 2)).size();e1++)
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
								metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));
						//total1 = calculateMTChain(module2);
						//}
					//}
					
		
					total = calculateMTChain(module1);
					
					//total1 = calculateMTChain(module2);
				
					sum[i] = sum[i] + total;
					
//					String model1 = modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")).toString().concat(".xmi");
//					String model2 = modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")).toString() + ".xmi";
					
					//from model, retrieve its corresponding metamodel. Use EOL for metamodel and model separately.
					
//					EolModule module_m1 = new EolModule();
//					
//					//Limitation - model name should be same as that of its metamodel name
//					
//					module_m1.parse(scriptRoot.resolve(identifyETL(metamodelsRoot.resolve(model1).toString().concat(".ecore")
//							, metamodelsRoot.resolve(model2).toString().concat(".ecore"))));
//					
//					if (module_m1 instanceof EolModule) {
//						EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
//						for (ModelDeclaration modelDeclaration : module_m1.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
//						
//					
//					
//					staticAnalyser.validate(module_m1);
//					}
					
					
					int sum1 = 0, sum2 = 0,sum3 = 0,sum4 = 0, sum5 = 0,sum6 = 0, sum7 = 0,sum8 = 0;
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					EtlStaticAnalyser staticAnalyser1 = new EtlStaticAnalyser();
					
					module1.getContext().setModule(module1);
					module2.getContext().setModule(module2);
					
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser1.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					//System.out.println(module1);
					staticAnalyser.validate(module1);
					staticAnalyser1.validate(module2);
					EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						
//						if(m<((EtlModule) module1).getTransformationRules().size()-1)
//						{
//							for (int n1 = 0; n1 < ((EtlModule) module1).getTransformationRules().get(m+1).getTargetParameters()
//									.size(); n1++) {
//							EolModelElementType type100 = (EolModelElementType) staticAnalyser
//									.getType(((EtlModule) module1).getTransformationRules().get(m+1).getSourceParameter());
//							System.out.println("jhgkugukf "+type100.getTypeName());
//							}
//						}
							
						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
						
						String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
						
						String code2 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
								+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size().asString().println();\r\n}";
						
						//"for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
						
						String code20 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
								+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size().asString().println();\r\n}";
								
//						FileWriter fw21 = new FileWriter(scriptRoot.resolve("newDep2" + ".eol").toString());
//						fw21.write(code2);
//						fw21.close();
//						
//						FileWriter fw210 = new FileWriter(scriptRoot.resolve("newDep20" + ".eol").toString());
//						fw210.write(code20);
//						fw210.close();
						
//						String x2 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep2" + ".eol"));
//						String x20 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep20" + ".eol"));
						
						String x2 = executeEOL1(sourceMM111, metaMM, code2);
						String x20 = executeEOL1(sourceMM111, metaMM, code20);

						
						int i1 = NumberUtils.toInt(x2.trim());
						int i2 = NumberUtils.toInt(x20.trim());
						
						sum1=sum1+i1; 
						sum2=sum2+i2; 
						
//						sum_src1 = sum1;
//						sum_src2 = sum2;
						
						System.out.println("No. of attributes in source "+type.getTypeName()+" is "+x2);
						System.out.println("No. of references in source "+type.getTypeName()+" is "+x20);
						
//						System.out.println("Total attributes in source "+type.getTypeName()+" in "+type.getModelName()+" metamodel is "+sum1);
//						System.out.println("Total references in source "+type.getTypeName()+" in "+type.getModelName()+" metamodel is "+sum2);
						
//						scriptRoot.resolve("newDep2" + ".eol").toFile().delete();
//						scriptRoot.resolve("newDep20" + ".eol").toFile().delete();
						
						
						
						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
//							type1 = (EolModelElementType) staticAnalyser
//									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
							
						
							ArrayList<String> src_dep = srcDependency1(module1);
							ArrayList<String> trg_dep = trgDependency1(module1);
							
							for(int gtr=0;gtr<((EtlModule) module1).getTransformationRules().size();gtr++) {
								for(int td=0;td<trg_dep.size();td++) {
									System.out.println("\nThis Target Dependency: "+((EtlModule) module1).getTransformationRules().get(gtr)+" "+trg_dep.get(td));
								}
								break;
							}
								
						
							if(src_dep.size()>0)
							System.out.println("\nSource Dependency List: (Metaclass->Etype feature rule) ");
						for(int ss=0;ss<src_dep.size();ss++)
						{
							String[] split_src = src_dep.get(ss).split("\\s+");
							
							if(split_src.length>1)
							{
			
								Node a1 = new Node(split_src[0]);
								Node b1 = new Node(split_src[1]);
								Node c1 = new Node(split_src[2]);
								Node d1 = new Node(split_src[3]);
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								
								Graph g = new Graph(list);
								g.addEdge(d1, c1);
								g.addEdge(d1, b1);
								g.addEdge(d1, a1);
								
								//g.printAdjList();
							}
						}
						
						if(trg_dep.size()>0)
							System.out.println("\nTarget Dependency List: (Metaclass->Etype feature rule) ");
						for(int ss=0;ss<trg_dep.size();ss++)
						{
							String[] split_trg = trg_dep.get(ss).split("\\s+");
							
							if(split_trg.length>1)
							{
			
								Node a1 = new Node(split_trg[0]);
								Node b1 = new Node(split_trg[1]);
								Node c1 = new Node(split_trg[2]);
								Node d1 = new Node(split_trg[3]);
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								
								Graph g = new Graph(list);
								g.addEdge(d1, c1);
								g.addEdge(d1, b1);
								g.addEdge(d1, a1);
							
								//g.printAdjList();
							}
							
						}
						System.out.println();
						
						ArrayList<String> src_dep1 = srcDependency1(module2);
						ArrayList<String> trg_dep1 = trgDependency1(module2);
						
						for(int gtr=0;gtr<((EtlModule) module2).getTransformationRules().size();gtr++) {
							for(int sd=0;sd<src_dep1.size();sd++) {
								System.out.println("\nNext Source Dependency: "+((EtlModule) module2).getTransformationRules().get(gtr)+" "+src_dep1.get(sd));
							}
							break;
						}
							
						
						if(src_dep1.size()>0)
						System.out.println("\nSource Dependency List: (Metaclass->Etype feature rule) ");
					for(int ss=0;ss<src_dep1.size();ss++)
					{
						String[] split_src = src_dep1.get(ss).split("\\s+");
						
						if(split_src.length>1)
						{

							Node a1 = new Node(split_src[0]);
							Node b1 = new Node(split_src[1]);
							Node c1 = new Node(split_src[2]);
							Node d1 = new Node(split_src[3]);
							
							ArrayList<Node> list = new ArrayList<Node>();
							list.add(a1);
							list.add(b1);
							list.add(c1);
							list.add(d1);
							
							Graph g = new Graph(list);
							g.addEdge(d1, c1);
							g.addEdge(d1, b1);
							g.addEdge(d1, a1);
							
							//g.printAdjList();
						}
					}
					
					if(trg_dep1.size()>0)
						System.out.println("\nTarget Dependency List: (Metaclass->Etype feature rule) ");
					for(int ss=0;ss<trg_dep1.size();ss++)
					{
						String[] split_trg = trg_dep1.get(ss).split("\\s+");
						
						if(split_trg.length>1)
						{

							Node a1 = new Node(split_trg[0]);
							Node b1 = new Node(split_trg[1]);
							Node c1 = new Node(split_trg[2]);
							Node d1 = new Node(split_trg[3]);
							
							ArrayList<Node> list = new ArrayList<Node>();
							list.add(d1);
							list.add(a1);
							list.add(b1);
							list.add(c1);
							
							Graph g = new Graph(list);
							g.addEdge(d1, c1);
							g.addEdge(d1, b1);
							g.addEdge(d1, a1);
						
							//g.printAdjList();
						}
						
					}
					System.out.println();
					
					boolean flag=false;
					for(int tr=0;tr<trg_dep.size();tr++) {
						for(int sr=0;sr<src_dep1.size();sr++) {
							String[] split_trg = trg_dep.get(tr).split("\\s+");
							String[] split_src = src_dep1.get(sr).split("\\s+");
							if(!(split_trg[1]+split_trg[2]+split_trg[3]).trim().equals((split_src[1]+split_src[2]+split_src[3]).trim())) {
								//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
								flag=true;
								
							}
							else
								//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
								flag=false;
						}
						if(flag==true)
							System.out.println("Keep "+trg_dep.get(tr)+" for next transormation.\n");	
						else
							System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
					}
					
					
							String code = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var attr = cl.eAllAttributes.name;\r\n"
									+ "for(at in attr){at.println();}\r\n}";
							
							String code0 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var ref = cl.eAllReferences;\r\n"
									+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";
							
//							+ "for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n "
//									+ "\"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
							
//							FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//							fw11.write(code);
//							fw11.close();
//							
//							FileWriter fw110 = new FileWriter(scriptRoot.resolve("newDep0" + ".eol").toString());
//							fw110.write(code0);
//							fw110.close();
					
							
//							String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
//							String x0 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep0" + ".eol"));
							String x = executeEOL1(sourceMM111, metaMM, code);
							String x0 = executeEOL1(sourceMM111, metaMM, code0);
							
							String[] arrOfStr = x.split("\n");
							//String arr_old = x0.split("\n").toString().substring(0, x0.indexOf(":"));
							String[] arrOfStr0 = x0.trim().split("\n");
							//System.out.println(x0.trim().substring(0,x0.indexOf(":")));
							//System.out.println("hdsh"+arrOfStr0);
//							for (String r : arrOfStr0)
//					            System.out.println(r);
							
							System.out.println("Attribute of source "+type.getTypeName()+": "+x);
							System.out.println("Reference of source "+type.getTypeName()+": "+x0);
							
//							scriptRoot.resolve("newDep" + ".eol").toFile().delete();
//							scriptRoot.resolve("newDep0" + ".eol").toFile().delete();
							
							String eolcode = null, eolcode0 = null, eolcode5=null, eolcode50=null;;
							try {
//								eolcode = "if("+x+".notEmpty)"+ type.getTypeName() +".all."+x+".println();\r\n else \"No attribute\".println()";
								//if(type.getTypeName().isEmpty())
//								if(x==null)
//								{
//									eolcode = type.getTypeName() +".all."+x+".println();";
//									eolcode0 = type.getTypeName() +".all."+x0+".println();";
								String ref_MM, ref_metaclass_MM;
								for (String r0 : arrOfStr0)
								{
//										eolcode = r + ".eAllAttributes.println()";
//										eolcode0 = r + ".eAllReferences.println()";
										ref_MM = r0.trim().substring(0,r0.indexOf(":"));
										System.out.println(r0.trim().substring(0,r0.indexOf(":")));
										ref_metaclass_MM = r0.substring(r0.indexOf(":")+1,r0.length());
										System.out.println(r0.substring(r0.indexOf(":")+1,r0.length()));
//										eolcode = type.getTypeName() +".all."+x+".println();";
										eolcode0 = type.getTypeName() +".all."+ref_MM+".println();";
										eolcode50 = type.getTypeName() +".all."+ref_MM+".size().println();";
								}	
								for (String r : arrOfStr)
								{
									eolcode = type.getTypeName() +".all."+r+".println();";
									eolcode5 = type.getTypeName() +".all."+r+".size().println();";
								}
								
//								FileWriter fw51 = new FileWriter(scriptRoot.resolve("newDep5" + ".eol").toString());
//								fw51.write(eolcode5);
//								fw51.close();
//								
//								FileWriter fw510 = new FileWriter(scriptRoot.resolve("newDep50" + ".eol").toString());
//								fw510.write(eolcode50);
//								fw510.close();
								
								String x5 = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode);
								String x50 = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode5);
								
								int i5 = NumberUtils.toInt(x5.trim());
								int i6 = NumberUtils.toInt(x50.trim());
								
								sum5=sum5+i5; 
								sum6=sum6+i6; 
								
								
								
//										FileWriter fw = new FileWriter(scriptRoot.resolve("SourceDependency" + ".eol").toString());
//										fw.write(eolcode);
//										fw.close();
//										
//										FileWriter fw0 = new FileWriter(scriptRoot.resolve("SourceDependency0" + ".eol").toString());
//										fw0.write(eolcode0);
//										fw0.close();
										
//										System.out.println(type.getTypeName());
										String ex = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode);
										String ex0 = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode0);
										
										System.out.println("Source model's attributes: "+ex);
										System.out.println("Source model's references: "+ex0);
										
//										scriptRoot.resolve("SourceDependency" + ".eol").toFile().delete();
//										scriptRoot.resolve("SourceDependency0" + ".eol").toFile().delete();
										
//								}
							
							} 
							catch (Exception e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
								System.out.println("EAttribute not given: "+e);
							}
							
							String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";
							
						//registerMM(metamodelsRoot.resolve(type1.getModelName()+".ecore").toString());
							
							String sourceMM1111 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
							
							String code3 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size.println();\r\n}";
							
							String code30 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size.println();\r\n}";
									
//							FileWriter fw31 = new FileWriter(scriptRoot.resolve("newDep3" + ".eol").toString());
//							fw31.write(code3);
//							fw31.close();
//							
//							FileWriter fw310 = new FileWriter(scriptRoot.resolve("newDep30" + ".eol").toString());
//							fw310.write(code30);
//							fw310.close();
							
//							String x3 = executeEOL1(sourceMM1111, metaMM1, scriptRoot.resolve("newDep3" + ".eol"));
//							String x30 = executeEOL1(sourceMM1111, metaMM1, scriptRoot.resolve("newDep30" + ".eol"));
							
							String x3 = executeEOL1(sourceMM1111, metaMM1, code3);
							String x30 = executeEOL1(sourceMM1111, metaMM1, code30);
							
							int i3 = NumberUtils.toInt(x3.trim());
							int i4 = NumberUtils.toInt(x30.trim());
							sum3=sum3+i3; 
							sum4=sum4+i4; 
							
							System.out.println("No. of attributes in target "+type1.getTypeName()+" is "+x3);
							System.out.println("No. of references in target "+type1.getTypeName()+" is "+x30);
							
							//if(m<((EtlModule) module1).getTransformationRules().size()-1)
//								if(m>0)
//								{
//									type_next = (EolModelElementType) staticAnalyser
//											.getType(((EtlModule) module1).getTransformationRules().get(m-1).getSourceParameter());
//									System.out.println("123 "+type_next.getTypeName());
//									System.out.println("456 "+type1.getTypeName());
//								}
//							System.out.println("Total attributes in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum3);
//							System.out.println("Total references in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum4);
							
							scriptRoot.resolve("newDep3" + ".eol").toFile().delete();
							scriptRoot.resolve("newDep30" + ".eol").toFile().delete();
							
							//use try catch
							String code1 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var attr = cl.eAllAttributes.name;\r\n"
									+ "for(at in attr){at.println();}\r\n}";
							
							String code10 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var ref = cl.eAllReferences;\r\n"
									+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";
							
//							FileWriter fw111 = new FileWriter(scriptRoot.resolve("newDep1" + ".eol").toString());
//							fw111.write(code1);
//							fw111.close();
//							
//							FileWriter fw1110 = new FileWriter(scriptRoot.resolve("newDep10" + ".eol").toString());
//							fw1110.write(code10);
//							fw1110.close();
							
							
//							String x1 = executeEOL1(sourceMM1111, metaMM1, scriptRoot.resolve("newDep1" + ".eol"));
//							String x10 = executeEOL1(sourceMM1111, metaMM1, scriptRoot.resolve("newDep10" + ".eol"));
							
							String x1 = executeEOL1(sourceMM1111, metaMM1, code1);
							String x10 = executeEOL1(sourceMM1111, metaMM1, code10);
							
							String[] arrOfStr1 = x1.split("\n");
							String[] arrOfStr10 = x10.trim().split("\n");
							
							System.out.println("Attribute of target "+type1.getTypeName()+": "+x1);
							System.out.println("Reference of target "+type1.getTypeName()+": "+x10);
//							scriptRoot.resolve("newDep1" + ".eol").toFile().delete();
//							scriptRoot.resolve("newDep10" + ".eol").toFile().delete();
							
							
							String eolcode1 = null, eolcode10 = null, eolcode6=null, eolcode60=null;
							try {
//								eolcode1 = "if("+x1+".notEmpty)"+type1.getTypeName()+".all."+x1+".println();\r\n else \"No attribute\".println()";
								//if(type1.getTypeName().isEmpty())
//								if(x1==null)
//								{
//									eolcode1 = type1.getTypeName() +".all."+x1+".println();";
//									eolcode10 = type1.getTypeName() +".all."+x10+".println();";
								String ref_MM1, ref_metaclass_MM1;
								for (String r10 : arrOfStr10)
								{
										ref_MM1 = r10.trim().substring(0,r10.indexOf(":"));
										System.out.println(r10.trim().substring(0,r10.indexOf(":")));
										ref_metaclass_MM1 = r10.substring(r10.indexOf(":")+1,r10.length());
										System.out.println(r10.substring(r10.indexOf(":")+1,r10.length()));
										eolcode10 = type1.getTypeName() +".all."+ref_MM1+".println();";
										eolcode60 = type1.getTypeName() +".all."+ref_MM1+".size().println();";
								}	
								for (String r1 : arrOfStr1)
								{
									eolcode1 = type1.getTypeName() +".all."+r1+".println();";
									eolcode6 = type1.getTypeName() +".all."+r1+".size().println();";
								}
								
//								FileWriter fw61 = new FileWriter(scriptRoot.resolve("newDep6" + ".eol").toString());
//								fw61.write(eolcode6);
//								fw61.close();
//								
//								FileWriter fw610 = new FileWriter(scriptRoot.resolve("newDep60" + ".eol").toString());
//								fw610.write(eolcode60);
//								fw610.close();
								
//								String x6 = executeEOL(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), scriptRoot.resolve("newDep6" + ".eol"));
//								String x60 = executeEOL(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), scriptRoot.resolve("newDep60" + ".eol"));
								
								String x6 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode6);
								String x60 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode60);
								
								int i7 = NumberUtils.toInt(x6.trim());
								int i8 = NumberUtils.toInt(x60.trim());
								
								sum7=sum7+i7; 
								sum8=sum8+i8; 
								
//									FileWriter fw1 = new FileWriter(scriptRoot.resolve("TargetDependency" + ".eol").toString());
//									fw1.write(eolcode1);
//									fw1.close();
//									
//									FileWriter fw10 = new FileWriter(scriptRoot.resolve("TargetDependency0" + ".eol").toString());
//									fw10.write(eolcode10);
//									fw10.close();
									
//									System.out.println(type1.getTypeName());

									String ex1 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode1);
									String ex10 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode10);
									
									System.out.println("Target model's attributes: "+ex1);
									System.out.println("Target model's references: "+ex10);
//									scriptRoot.resolve("TargetDependency" + ".eol").toFile().delete();
//									scriptRoot.resolve("TargetDependency0" + ".eol").toFile().delete();
//									scriptRoot.resolve("newDep6" + ".eol").toFile().delete();
//									scriptRoot.resolve("newDep60" + ".eol").toFile().delete();
//								}
							
							}
							catch (Exception e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
								System.out.println("New EAttribute not given: "+e);
							}
							// information loss can be calculated: if the above code runs successfully, then no information loss
//							System.out.println("Total attributes in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum3);
//							System.out.println("Total references in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum4);
							
							System.out.println("gfghf "+sum1+" "+sum2);
//							
////								System.out.println("1235");
//								
//								if(m<((EtlModule) module1).getTransformationRules().size()-1)
//								{
////									for (int n1 = 0; n1 < ((EtlModule) module1).getTransformationRules().get(m+1).getTargetParameters()
////											.size(); n1++) {
//									type_next = (EolModelElementType) staticAnalyser
//											.getType(((EtlModule) module1).getTransformationRules().get(m+1).getSourceParameter());
////									System.out.println(type.getTypeName());
////									System.out.println("jhgkugukf "+type_next.getTypeName());
////									System.out.println("hjvjh "+type1.getTypeName());
//									
////									if(type_next.getTypeName()==type.getTypeName())
////									{
//										String metaMM1x = "http://www.eclipse.org/emf/2002/Ecore";
//										
//										String sourceMM1111x = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
//										
//										String code3x = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
//												+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size.println();\r\n}";
//										
//										String code30x = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
//												+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size.println();\r\n}";
//												
//										FileWriter fw31x = new FileWriter(scriptRoot.resolve("newDep3x" + ".eol").toString());
//										fw31x.write(code3x);
//										fw31x.close();
//										
//										FileWriter fw310x = new FileWriter(scriptRoot.resolve("newDep30x" + ".eol").toString());
//										fw310x.write(code30x);
//										fw310x.close();
//										
//										String x3x = executeEOL1(sourceMM1111x, metaMM1x, scriptRoot.resolve("newDep3x" + ".eol"));
//										String x30x = executeEOL1(sourceMM1111x, metaMM1x, scriptRoot.resolve("newDep30x" + ".eol"));
//										
//										int i3x = NumberUtils.toInt(x3x.trim());
//										int i4x = NumberUtils.toInt(x30x.trim());
//										System.out.println("cvhgfhf"+i3x);
//										sum3x=sum3x+i3x; 
//										sum4x=sum4x+i4x; 
//										
//										System.out.println("No. of attributes in target "+type1.getTypeName()+" is "+x3x);
//										System.out.println("No. of references in target "+type1.getTypeName()+" is "+x30x);
//										
////										System.out.println("Total attributes in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum3);
////										System.out.println("Total references in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum4);
//										
//										scriptRoot.resolve("newDep3x" + ".eol").toFile().delete();
//										scriptRoot.resolve("newDep30x" + ".eol").toFile().delete();
//										
//										
////									}
//									
////									}
//								}
								
								
//								cov = (sum3x+sum4x)/(sum1+sum2);
//							
//								System.out.println("Tranformation coverage for "+type.getModelName()+" -> "+type1.getModelName()+" is "+cov);
//								prod = prod * cov;
							
//							if(m<((EtlModule) module1).getTransformationRules().size()-1)
//							//if(m>0)
//							{
//								type_next = (EolModelElementType) staticAnalyser
//										.getType(((EtlModule) module1).getTransformationRules().get(m+1).getSourceParameter());
//								System.out.println(type_next.getTypeName());
//								System.out.println(type1.getTypeName());
//								
//								String metaMM1x = null;
//								if(type_next.getTypeName()==type1.getTypeName())
//								{
//									metaMM1x = "http://www.eclipse.org/emf/2002/Ecore";
//									
//									String sourceMM1111x = metamodelsRoot.resolve(type_next.getModelName()+".ecore").toString();
//									
//									String code3x = "var type = EClass.all.select(ec|ec.name = \""+ type_next.getTypeName()+"\");\r\n"
//											+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size.println();\r\n}";
//									
//									String code30x = "var type = EClass.all.select(ec|ec.name = \""+ type_next.getTypeName()+"\");\r\n"
//											+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size.println();\r\n}";
//											
//									FileWriter fw31x = new FileWriter(scriptRoot.resolve("newDep3x" + ".eol").toString());
//									fw31x.write(code3x);
//									fw31x.close();
//									
//									FileWriter fw310x = new FileWriter(scriptRoot.resolve("newDep30x" + ".eol").toString());
//									fw310x.write(code30x);
//									fw310x.close();
//									
//									String x3x = executeEOL1(sourceMM1111x, metaMM1x, scriptRoot.resolve("newDep3x" + ".eol"));
//									String x30x = executeEOL1(sourceMM1111x, metaMM1x, scriptRoot.resolve("newDep30x" + ".eol"));
//									
//									int i3x = NumberUtils.toInt(x3x.trim());
//									int i4x = NumberUtils.toInt(x30x.trim());
//									System.out.println("cvhgfhf "+i3x);
//									System.out.println("gfyr "+i4x);
//									sum3x = sum3x+i3x; 
//									sum4x=sum4x+i4x; 
//									
//									System.out.println("No. of attributes in target "+type_next.getTypeName()+" is "+x3x);
//									System.out.println("No. of references in target "+type_next.getTypeName()+" is "+x30x);
//									
////									System.out.println("Total attributes in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum3);
////									System.out.println("Total references in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum4);
//									
//									scriptRoot.resolve("newDep3x" + ".eol").toFile().delete();
//									scriptRoot.resolve("newDep30x" + ".eol").toFile().delete();
//									
//									
//								}
									
							//}
//							cov = (sum3x+sum4x)/(sum1+sum2);
//							
//							System.out.println("Tranformation coverage for "+type.getModelName()+" -> "+type1.getModelName()+" is "+cov);
//							prod = prod * cov;
							
					
//						System.out.println("Tranformation coverage for chain "+prod);
						String metaMM1x = null;
						
						float sumtot1=0, sumtot2=0;
//						float tot1=0, tot2=0;
						int sum3x=0, sum4x=0, i3x=0, i4x=0;
						if(j+1 < l.get(i).size()-1)
						{ 
							
							for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
							{
								//float tot1=0, tot2=0;
								
								type_next = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
								System.out.println("1 "+type_next.getTypeName());
								System.out.println("2 "+type1.getTypeName());
								if(type_next.getTypeName().equals(type1.getTypeName()))
								{
									
									metaMM1x = "http://www.eclipse.org/emf/2002/Ecore";
//									
									String sourceMM1111x = metamodelsRoot.resolve(type_next.getModelName()+".ecore").toString();
									
									String code3x = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
											+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size.println();\r\n}";
									
									String code30x = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
											+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size.println();\r\n}";
											
//									FileWriter fw31x = new FileWriter(scriptRoot.resolve("newDep3x" + ".eol").toString());
//									fw31x.write(code3x);
//									fw31x.close();
//									
//									FileWriter fw310x = new FileWriter(scriptRoot.resolve("newDep30x" + ".eol").toString());
//									fw310x.write(code30x);
//									fw310x.close();
									
//									String x3x = executeEOL1(sourceMM1111x, metaMM1x, scriptRoot.resolve("newDep3x" + ".eol"));
//									String x30x = executeEOL1(sourceMM1111x, metaMM1x, scriptRoot.resolve("newDep30x" + ".eol"));
									
									String x3x = executeEOL1(sourceMM1111x, metaMM1x, code3x);
									String x30x = executeEOL1(sourceMM1111x, metaMM1x, code30x);
									
									i3x = NumberUtils.toInt(x3x.trim());
									i4x = NumberUtils.toInt(x30x.trim());
									System.out.println("cvhgfhf "+i3x);
									System.out.println("gfyr "+i4x);
									sum3x = sum3x+i3x; 
									sum4x=sum4x+i4x; 
									
									System.out.println("No. of attributes in target "+type1.getTypeName()+" is "+x3x);
									System.out.println("No. of references in target "+type1.getTypeName()+" is "+x30x);
									
//									System.out.println("Total attributes in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum3);
//									System.out.println("Total references in target "+type1.getTypeName()+" in "+type1.getModelName()+" metamodel is "+sum4);
									System.out.println("sum "+(sum3x+sum4x));
									System.out.println("sum total "+(sum1+sum2));
									
//									scriptRoot.resolve("newDep3x" + ".eol").toFile().delete();
//									scriptRoot.resolve("newDep30x" + ".eol").toFile().delete();
									//cov = tot1/tot2;
									
									tot1=sum3x+sum4x;
									System.out.println("Tot1 "+tot1);
									
//									sumtot1=sumtot1+tot1;
//									sumtot2=sumtot2+tot2;
									//break;
									
								}
						
							}
							
							tot2=sum1+sum2;
							System.out.println("Tot2 "+tot2);
							sumtot1=sumtot1+tot1;
							sumtot2=sumtot2+tot2;
							cov = sumtot1/sumtot2;
							sum_cov=cov;
//							sum_cov=sum_cov*cov;
//							System.out.println("Tranformation coverage for rule "+type.getTypeName()+" to "+type1.getTypeName()+" is "+cov);
							
						}
						else
							sum_cov=1;
					
						}
						//sum_cov=sum_cov*cov;
					}
					System.out.println("Tranformation coverage for "+type.getModelName()+" -> "+type1.getModelName()+" is "+sum_cov);
					prod = prod * sum_cov;
					sum_cov_chain[i]=prod;
					
					System.out.println("Total attributes in source transformation type "+"in "+type.getModelName()+" metamodel is "+sum1);
					System.out.println("Total references in source transformation type "+"in "+type.getModelName()+" metamodel is "+sum2);
					System.out.println("Total attributes in target transformation type "+"in "+type1.getModelName()+" metamodel is "+sum3);
					System.out.println("Total references in target transformation type "+"in "+type1.getModelName()+" metamodel is "+sum4);
					System.out.println("\n");
					System.out.println("Total attributes in source transformation type "+"in "+type.getModelName()+" model is "+sum5);
					System.out.println("Total references in source transformation type "+"in "+type.getModelName()+" model is "+sum6);
					System.out.println("Total attributes in target transformation type "+"in "+type1.getModelName()+" model is "+sum7);
					System.out.println("Total references in target transformation type "+"in "+type1.getModelName()+" model is "+sum8);
					
					sum_source_attributeMM = sum_source_attributeMM + sum1;
					sum_source_referenceMM = sum_source_referenceMM + sum2;
					sum_target_attributeMM = sum_target_attributeMM + sum3;
					sum_target_referenceMM = sum_target_referenceMM + sum4;
					
					sum_source_attributeModel = sum_source_attributeModel + sum5;
					sum_source_referenceModel = sum_source_referenceModel + sum6;
					sum_target_attributeModel = sum_target_attributeModel + sum7;
					sum_target_referenceModel = sum_target_referenceModel + sum8;
					
					
					
					System.out.println("Total expressions/operators used in the transformation " + l.get(i).get(j)
							+ " -> " + l.get(i).get(j + 1) + ": " + total + "\n");
				  }
				}
				
			}
			if (sum[i] < min) {
				min = sum[i];
				index = l.get(i);
			}
			if(sum_cov_chain[i]>max_cov)
			{
				max_cov=sum_cov_chain[i];
				index1=l.get(i);
			}
			
			System.out.println("Tranformation coverage for chain "+ l.get(i)+" is "+prod);
			
			System.out.println("Total transformed source structural features in metamodel = "+ (sum_source_attributeMM+sum_source_referenceMM));
			System.out.println("Total transformed target structural features in metamodel = "+ (sum_target_attributeMM+sum_target_referenceMM));
			System.out.println("Total transformed source structural features in model = "+ (sum_source_attributeModel+sum_source_referenceModel));
			System.out.println("Total transformed target structural features in model = "+ (sum_target_attributeModel+sum_target_referenceModel));
			System.out.println("\n");
			System.out.println("Total expressions/operators used in the chain: " + sum[i]);
			System.out.println("---------------------------------------------------------\n");

		}
		
		System.out.println("\nMT Chain " + index1 + " has maximum transformation coverage of " + max_cov);
		System.out.println("\nMT Chain " + index + " has minimum structural features of " + min);
		
		// This code requires for executing the best transformation chain.
//		System.out.println("------------------Executing best chain--------------------");
//		for (int k = 0; k < index.size(); k++) {
//			if (k + 1 < index.size()) {
//
//				Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "") + ".xmi");
//				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
//
//				Path newtargetmodelpath = modelsRoot.resolve(index.get(k + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
//				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
//
//				executeETL(newsourcemodel, metamodelPath + "/" + index.get(k), newtargetmodel,
//						metamodelPath + "/" + index.get(k + 1));
//
//			}
//
//		}
		return index;

	}

	
	public List<ArrayList<String>> deletetrindex1_single(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
//		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
//		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		Chain_MT cm = new Chain_MT();
//		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
//		//System.out.println(identifyETLinModels(sourceMM, targetMM));
//		if (etl1) {
//		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
//			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
//			//System.out.println("qwerty: "+x);
//			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
//			//break;
//		}
//		}
//		if (!two.isEmpty())
//			l.add(two);

		ArrayList<String> bestchain = identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		ArrayList<String> tr_list = null;
		
		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;
		
		//for (int i = 0; i < l.size(); i++) {
			//System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			//int total = 0;
			
			tr_list = new ArrayList<String>();
			
			
			for (int j =bestchain.size()-1; j >=0; j--) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
				ArrayList<String> src_dep = null, trg_dep = null, src_dep1 = null, trg_dep1 = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				
				if (j > 0) {
					
					System.out.println(bestchain.get(j-1) + " -> " + bestchain.get(j) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + bestchain.get(j-1),
							metamodelPath + "/" + bestchain.get(j)).size();e++) 
					{
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j-1),
							metamodelPath + "/" + bestchain.get(j)).get(e)));
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");
					
					System.out.println("src_dep:"+src_dep);
					System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
							
							for(int gtr=0;gtr<module1.getTransformationRules().size();gtr++) {
								for(int td=0;td<src_dep.size();td++) {
									System.out.println("\nThis Source Dependency: "+module1.getTransformationRules().get(gtr)+" "+src_dep.get(td));
								}
								//break;
							}
								
						
							if(src_dep.size()>0)
								System.out.println("\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
						for(int ss=0;ss<src_dep.size();ss++)
						{
							String[] split_src = src_dep.get(ss).split("\\s+");
							
							if(split_src.length>1)
							{
			
								Node a1 = new Node(split_src[0]);
								Node b1 = new Node(split_src[1]);
								Node c1 = new Node(split_src[2]);
								Node d1 = new Node(split_src[3]);
								Node e1 = new Node(split_src[4]);
								Node f1 = new Node(split_src[6]);
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								list.add(e1);
								list.add(f1);
								
								Graph g = new Graph(list);
								g.addEdge(d1, a1);
								g.addEdge(d1, b1);
								g.addEdge(d1, c1);
								g.addEdge(d1, e1);
								g.addEdge(d1, f1);
								
								g.printAdjList();
							}
						}
						
						if(trg_dep.size()>0)
							System.out.println("\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
						for(int ss=0;ss<trg_dep.size();ss++)
						{
							String[] split_trg = trg_dep.get(ss).split("\\s+");
							
							if(split_trg.length>1)
							{
			
								Node a1 = new Node(split_trg[0]);
								Node b1 = new Node(split_trg[1]);
								Node c1 = new Node(split_trg[2]);
								Node d1 = new Node(split_trg[3]);
								Node e1 = new Node(split_trg[4]);
								Node f1 = new Node(split_trg[6]);
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								list.add(e1);
								list.add(f1);
								
								Graph g = new Graph(list);
								g.addEdge(d1, a1);
								g.addEdge(d1, b1);
								g.addEdge(d1, c1);
								g.addEdge(d1, e1);
								g.addEdge(d1, f1);
							
								g.printAdjList();
							}
							
						}
						System.out.println();
						
						}
					}
				 }
				}
				
					if (j - 1 > 0)
					{

						
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + bestchain.get(j-2),
								metamodelPath + "/" + bestchain.get(j - 1)).size();e1++)
						{
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" +bestchain.get(j-2),
								metamodelPath + "/" + bestchain.get(j - 1)).get(e1)));
					
					src_dep1 = srcDependency1(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1(module2);
					System.out.println("\n");
					
					System.out.println("src_dep_prev:"+src_dep1);
					System.out.println("trg_dep_prev:"+trg_dep1);
					
					for(int gtr=0;gtr<module2.getTransformationRules().size();gtr++) {
						for(int sd=0;sd<trg_dep1.size();sd++) {
							System.out.println("\nPrevious Target Dependency: "+module2.getTransformationRules().get(gtr)+" "+trg_dep1.get(sd));
						}
						//break;
					}
					//}
						
					
					if(src_dep1.size()>0)
					System.out.println("\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
				for(int ss=0;ss<src_dep1.size();ss++)
				{
					String[] split_src = src_dep1.get(ss).split("\\s+");

					
					if(split_src.length>1)
					{

						Node a1 = new Node(split_src[0]);
						Node b1 = new Node(split_src[1]);
						Node c1 = new Node(split_src[2]);
						Node d1 = new Node(split_src[3]);
						Node e11 = new Node(split_src[4]);
						Node f1 = new Node(split_src[6]);

						ArrayList<Node> list = new ArrayList<Node>();
						list.add(a1);
						list.add(b1);
						list.add(c1);
						list.add(d1);
						list.add(e11);
						list.add(f1);
						
						Graph g = new Graph(list);
						g.addEdge(d1, a1);
						g.addEdge(d1, b1);
						g.addEdge(d1, c1);
						g.addEdge(d1, e11);
						g.addEdge(d1, f1);

						
						g.printAdjList();
					}
				}
	
					
					if(j-1 > 0)
					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_prev = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

//						}
//					
//					}
					
				if(trg_dep1.size()>0)
					System.out.println("\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
				for(int ss1=0;ss1<trg_dep1.size();ss1++)
				{
					String[] split_trg = trg_dep1.get(ss1).split("\\s+");
					
					if(split_trg.length>1)
					{

						Node a1 = new Node(split_trg[0]);
						Node b1 = new Node(split_trg[1]);
						Node c1 = new Node(split_trg[2]);
						Node d1 = new Node(split_trg[3]);
						Node e11 = new Node(split_trg[4]);
						Node f1 = new Node(split_trg[6]);
//				
						ArrayList<Node> list = new ArrayList<Node>();
						list.add(d1);
						list.add(a1);
						list.add(b1);
						list.add(c1);
						list.add(e11);
						list.add(f1);
			
						
						Graph g = new Graph(list);
						g.addEdge(d1, a1);
						g.addEdge(d1, b1);
						g.addEdge(d1, c1);
						g.addEdge(d1, e11);
						g.addEdge(d1, f1);
		
					
						g.printAdjList();
					}
					
				}
				System.out.println();
				
						}
					}
			
		
					Boolean[] boolflag = new Boolean[trg_dep1.size()];
					//boolean flag = false;
					if(!src_dep.isEmpty()) {
					
					for(tr=0;tr<trg_dep1.size();tr++) {
						
						String[] split_trg = trg_dep1.get(tr).split("\\s+");
						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
						for(int sr=0;sr<src_dep.size();sr++) {
						
							String[] split_src = src_dep.get(sr).split("\\s+");
							System.out.println("split_src_prev "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]);
							System.out.println("split_trg "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]);
							
							String[] split_trg1 = null;
							if(tr+1<trg_dep1.size())
								split_trg1 = trg_dep1.get(tr+1).split("\\s+");
							//{
							//if(tr+1<trg_dep.size())
								if(tr+1<trg_dep1.size()) {
								if(trg_dep1.get(tr).substring(0, 1).equals(trg_dep1.get(tr+1).substring(0, 1)))
								{
									if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()) && 
											!((split_trg1[1]+split_trg1[2]+split_trg1[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
									//if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
									{
										boolflag[tr]=true;
									
									}
										
									else
										boolflag[tr]=false;
										
								}
							}
							
								else {
									if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim())) {
									
										boolflag[tr]=true;
									
										
									}
									
									else {
										
										boolflag[tr]=false;
									}
								}
				
					}
						}
					}
					

					for(int tr0=0;tr0<trg_dep1.size();tr0++) {
						//if(flag==false)
						if(boolflag[tr0]!=null)
						{
							if(boolflag[tr0]==true)
							{
								System.out.println(trg_dep1.get(tr0)+" needs to be deleted "+"in "+type.getTypeName()+" to "+type1.getTypeName()+" transformation rule from "+type1.getModelName()+" metamodel."+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep1.get(tr0));
								
							}
								
					
							else
							{
								System.out.println("Keep "+trg_dep1.get(tr0)+" for previous transformation "+type_prev+"\n");
							}
						}
						
					}
							
				
					}
				}
				//}
				
			}
			
			tr_list_chain.add(tr_list);
		//}
		return tr_list_chain;

	}
	
	public List<ArrayList<String>> deletetrindex2(String sourceModel, String sourceMM, String targetModel, String targetMM, List<ArrayList<String>> l)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
//		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
//		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		Chain_MT cm = new Chain_MT();
//		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
//		//System.out.println(identifyETLinModels(sourceMM, targetMM));
//		if (etl1) {
//		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
//			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
//			//System.out.println("qwerty: "+x);
//			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
//			//break;
//		}
//		}
//		if (!two.isEmpty())
//			l.add(two);
		
		//ArrayList<Integer> tr_list = new ArrayList<Integer>();
		
		//List<ArrayList<String>> tr_list = null;
		ArrayList<String> tr_list = null, line_kept = null;
		
		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> line_kept_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			//int total = 0;
			
			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();
			
			for (int j = l.get(i).size()-1; j >=0; j--) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep = null, src_dep1 = null, src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				
				if (j > 0) {

					
					System.out.println(l.get(i).get(j-1) + " -> " + l.get(i).get(j) + "\n");
//					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j-1),
//							metamodelPath + "/" + l.get(i).get(j)).size();e++) 
//					{
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j-1),
							metamodelPath + "/" + l.get(i).get(j)).get(0)));
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
//					src_dep_attr = srcDependency1_new_attr(module1);
//					System.out.println("\n");
					
//					trg_dep = trgDependency1_new(module1);
//					System.out.println("\n");

					
					System.out.println("src_dep:"+src_dep);
					//System.out.println("src_dep_attr:"+src_dep_attr);
					//System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
						
						}
					}
				 //}
				}
				
					if (j - 1 > 0)
					{
					
//						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j-2),
//								metamodelPath + "/" + l.get(i).get(j - 1)).size();e1++)
//						{
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j-2),
								metamodelPath + "/" + l.get(i).get(j - 1)).get(0)));
					
					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");
					
//					src_dep1_attr = srcDependency1_new_attr(module2);
//					System.out.println("\n");
//					trg_dep1_attr = trgDependency1_new_attr(module2);
//					System.out.println("\n");
					
					
//					System.out.println("src_dep_prev:"+src_dep1);
					System.out.println("trg_dep1_prev:"+trg_dep1);
					//System.out.println("trg_dep1_attr_prev:"+trg_dep1_attr);
					
					if(j-1 > 0)
					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_prev = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
							
							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters()
									.size(); n++) {
								
								type_prev1 = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters().get(n));
								
		
						//}
						}
					}
			
					//ArrayList<Boolean> boolflag = new ArrayList<Boolean>();
					Boolean[] boolflag = new Boolean[trg_dep1.size()];
					//boolean flag = false;
					if(!src_dep.isEmpty()) {
					
					for(tr=0;tr<trg_dep1.size();tr++) {
						
						String[] split_trg = trg_dep1.get(tr).split("\\s+");
						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
						for(int sr=0;sr<src_dep.size();sr++) {
	
							String[] split_src = src_dep.get(sr).split("\\s+");
//							System.out.println("split_src "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]+split_src[6]);
//							System.out.println("split_trg_prev "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]+split_trg[6]);
							
							if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim().equals((split_src[1]+split_src[2]+split_src[3]).trim()))) {
								//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
								//flag=false;
								boolflag[tr]=true;
								//boolflag.add(true);
										
							}
								//boolflag[tr]=false;
							else {
								//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
								//tr_list.add(trg_dep.get(tr));
								//flag=true;
								boolflag[tr]=false;
								//boolflag.add(false);
								break;
							}
								
//						}
					}
						}
					}
					
//					}
//				}
//					for(Boolean b : boolflag)
//						System.out.println(b);
					for(int tr0=0;tr0<trg_dep1.size();tr0++) {
						//if(flag==false)
						if(boolflag[tr0]!=null)
						{
							if(boolflag[tr0]==true)
							//if(boolflag.get(tr0)==true)
							{
								System.out.println(trg_dep1.get(tr0)+" needs to be deleted "+"in "+type_prev.getTypeName()+" to "+type_prev1.getTypeName()+" transformation rule"+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep1.get(tr0));
								
							}
								
							//if(flag==true)
							//if(boolflag[tr0]==true)
							else
							{
								System.out.println("Keep "+trg_dep1.get(tr0)+" for transformation "+type_prev.getModelName()+"2"+type_prev1.getModelName()+"\n");
								line_kept.add(trg_dep1.get(tr0));
							}
						}
						
					}
							
//					  }
//					}
				
					}
				}
				//}
				
			}
			line_kept_chain.add(line_kept);
			tr_list_chain.add(tr_list);
		}
		return tr_list_chain;

	}
	
	public ArrayList<String> deletetrindex2_single(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		ArrayList<String> bestchain = identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		
//		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
//		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		Chain_MT cm = new Chain_MT();
//		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
//		//System.out.println(identifyETLinModels(sourceMM, targetMM));
//		if (etl1) {
//		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
//			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
//			//System.out.println("qwerty: "+x);
//			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
//			//break;
//		}
//		}
//		if (!two.isEmpty())
//			l.add(two);
		
		//ArrayList<Integer> tr_list = new ArrayList<Integer>();
		
		//List<ArrayList<String>> tr_list = null;
		
		ArrayList<String> tr_list_chain = null;
		tr_list_chain = new ArrayList<String>();
		ArrayList<String> line_kept_chain = new ArrayList<String>();
		
		
		for (int j = bestchain.size()-1; j >= 0; j--) {
			//System.out.println("Chain" + (j) + " " + bestchain.get(j-1) + "\n");
			//int total = 0;
			ArrayList<String> tr_list = null, line_kept = null;
			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();
			int tr = 0;
			int k;
			//for (int j = l.get(i).size()-1; j >=0; j--) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				//ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep_attr = null, trg_dep = null, src_dep1 = null, src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

					
				ArrayList<String> src_dep = new ArrayList<String>();
				ArrayList<String> trg_dep = new ArrayList<String>();
				ArrayList<String> src_dep1 = new ArrayList<String>();
				ArrayList<String> trg_dep1 = new ArrayList<String>();
				
				
				
				if (j > 0) {
					
					System.out.println(bestchain.get(j-1) + " -> " + bestchain.get(j) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + bestchain.get(j-1),
							metamodelPath + "/" + bestchain.get(j)).size();e++) 
					{
						EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j-1),
							metamodelPath + "/" + bestchain.get(j)).get(e)));
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
//					src_dep_attr = srcDependency1_new_attr(module1);
//					System.out.println("\n");
					
					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");
//					trg_dep_attr = trgDependency1_new_attr(module1);
//					System.out.println("\n");

					
					System.out.println("src_dep:"+src_dep);
					//System.out.println("src_dep_attr:"+src_dep_attr);
					//System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
						
						}
					}
				 
					}
				}
				
					if (j - 1 > 0)
					{
					
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + bestchain.get(j-2),
								metamodelPath + "/" + bestchain.get(j - 1)).size();e1++)
						{
							EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j-2),
								metamodelPath + "/" + bestchain.get(j - 1)).get(e1)));
					
					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");
					
//					src_dep1_attr = srcDependency1_new_attr(module2);
//					System.out.println("\n");
//					trg_dep1_attr = trgDependency1_new_attr(module2);
//					System.out.println("\n");
					
					
//					System.out.println("src_dep_prev:"+src_dep1);
					System.out.println("trg_dep1_prev:"+trg_dep1);
					//System.out.println("trg_dep1_attr_prev:"+trg_dep1_attr);
					
//					if(j-1 > 0)
//					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_prev = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
							
							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters()
									.size(); n++) {
								
								type_prev1 = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters().get(n));
								
		
						}
						}
					}
						}
//					}
					
					//ArrayList<Boolean> boolflag = new ArrayList<Boolean>();
					//if(!trg_dep1.isEmpty()) {
					
					//boolean flag = false;
					if(!src_dep.isEmpty()) {
					
					for(tr=0;tr<trg_dep1.size();tr++) {
						Boolean[] boolflag = new Boolean[trg_dep1.size()];
						int delete_size = 0;
						String[] split_trg = trg_dep1.get(tr).split("\\s+");
						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
						inner:for(int sr=0;sr<src_dep.size();sr++) {
	
							
							String[] split_src = src_dep.get(sr).split("\\s+");
//							System.out.println("split_src "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]+split_src[6]);
//							System.out.println("split_trg_prev "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]+split_trg[6]);
							
							//if(((split_trg[1]+split_trg[3]).trim().equals((split_src[1]+split_src[3]).trim()))) {
							if((split_trg[1].equals(split_src[1])) && 
								//(split_trg[2].equals(split_src[2])) && 
								(split_trg[3].equals(split_src[3])) //&&
								//split_trg[4].split("2")[1].equals(split_src[4].split("2")[0]) &&
								//split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])
								) {
								//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
								//flag=false;
								boolflag[tr]=false;
								//sr++;
								//boolflag.add(true);
								break inner;
										
							}
								//boolflag[tr]=false;
							//else if(!((split_trg[1]+split_trg[3]).equals((split_src[1]+split_src[3])))) {
							else if(!(split_trg[1].equals(split_src[1])) && 
									!(split_trg[2].equals(split_src[2])) && 
									!(split_trg[3].equals(split_src[3])) &&
									split_trg[4].split("2")[1].equals(split_src[4].split("2")[0]) &&
									split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])
									) {
								//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
								//tr_list.add(trg_dep.get(tr));
								//flag=true;
								boolflag[tr]=true;
								delete_size++;
								//boolflag.add(false);
								//break inner;
							}
							else {
								boolflag[tr]=false;
								//break;
							}
//							else if(!(split_trg[1].equals(split_src[1])) && !(split_trg[2].equals(split_src[2])) && !(split_trg[3].equals(split_src[3])) 
//									&& sr==(src_dep.size()-1) && split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])) {
//			
//								boolflag[tr]=true;
//								
//							}
								
//						}
					}
//						}
//					}
					//}
					
//					Boolean[] boolflag_attr = new Boolean[trg_dep1_attr.size()];
//					//boolean flag = false;
//					if(!src_dep_attr.isEmpty()) {
//					
//					for(tr=0;tr<trg_dep1_attr.size();tr++) {
//						
//						String[] split_trg = trg_dep1_attr.get(tr).split("\\s+");
//						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
//						for(int sr=0;sr<src_dep_attr.size();sr++) {
//	
//							
//							String[] split_src = src_dep_attr.get(sr).split("\\s+");
////							System.out.println("split_src "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]+split_src[6]);
////							System.out.println("split_trg_prev "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]+split_trg[6]);
//							
//							//if(!((split_trg[1]+split_trg[2]+split_trg[3]).equals((split_src[1]+split_src[2]+split_src[3])))) {
//							if(!split_trg[1].equals(split_src[1]) && !split_trg[2].equals(split_src[2]) && !split_trg[3].equals(split_src[3])) {
//								//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
//								//flag=false;
//								boolflag_attr[tr]=true;
//								//boolflag.add(true);
//										
//							}
//								//boolflag[tr]=false;
//							else if(split_trg[1].equals(split_src[1]) && split_trg[2].equals(split_src[2]) && split_trg[3].equals(split_src[3])) {
//								//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
//								//tr_list.add(trg_dep.get(tr));
//								//flag=true;
//								boolflag_attr[tr]=false;
//								//boolflag.add(false);
//								break;
//							}
//								
////						}
//					}
//						}
//					}
					
//					}
//				}
//					for(Boolean b : boolflag)
//						System.out.println(b);
					//for(int tr0=0;tr0<boolflag.length;tr0++) {
						//if(flag==false)
						if(boolflag[tr]!=null)
						{
							if(boolflag[tr]==true)
							//if(boolflag.get(tr0)==true)
							{
								System.out.println(trg_dep1.get(tr)+" needs to be deleted "+"in "+type_prev.getTypeName()+" to "+type_prev1.getTypeName()+" transformation rule"+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep1.get(tr));
								
							}
								
							//if(flag==true)
							//if(boolflag[tr0]==true)
							else
							{
								System.out.println("Keep "+trg_dep1.get(tr)+" for transformation "+type_prev.getModelName()+"2"+type_prev1.getModelName()+"\n");
								line_kept.add(trg_dep1.get(tr));
							}
						}
						
					}
					}
		//}
					
//					for(int tr0=0;tr0<trg_dep1_attr.size();tr0++) {
//						//if(flag==false)
//						if(boolflag_attr[tr0]!=null)
//						{
//							if(boolflag_attr[tr0]==true)
//							//if(boolflag.get(tr0)==true)
//							{
//								System.out.println(trg_dep1_attr.get(tr0)+" needs to be deleted "+"in "+type_prev.getTypeName()+" to "+type_prev1.getTypeName()+" transformation rule"+"\n");
//								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
//								tr_list.add(trg_dep1_attr.get(tr0));
//								
//							}
//								
//							//if(flag==true)
//							//if(boolflag[tr0]==true)
//							else
//							{
//								System.out.println("Keep "+trg_dep1_attr.get(tr0)+" for transformation "+type_prev.getModelName()+"2"+type_prev1.getModelName()+"\n");
//								line_kept.add(trg_dep1_attr.get(tr0));
//							}
//						}
//						
//					}
							
					  //}
					//}
				
					//}
				//}
				//}
				
			//}
			//line_kept_chain.add(line_kept);
			//tr_list_chain.add(tr_list);
			line_kept_chain.addAll(line_kept);
			tr_list_chain.addAll(tr_list);
		}
		return tr_list_chain;

	}
	
	public List<ArrayList<String>> keeptrindex2(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			//System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		
		//ArrayList<Integer> tr_list = new ArrayList<Integer>();
		
		//List<ArrayList<String>> tr_list = null;
		ArrayList<String> tr_list = null, line_kept = null;
		
		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> line_kept_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			//int total = 0;
			
			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();
			
			for (int j = l.get(i).size()-1; j >=0; j--) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep = null, src_dep1 = null, src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				
				if (j > 0) {

					
					System.out.println(l.get(i).get(j-1) + " -> " + l.get(i).get(j) + "\n");
//					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j-1),
//							metamodelPath + "/" + l.get(i).get(j)).size();e++) 
//					{
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j-1),
							metamodelPath + "/" + l.get(i).get(j)).get(0)));
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
//					src_dep_attr = srcDependency1_new_attr(module1);
//					System.out.println("\n");
					
//					trg_dep = trgDependency1_new(module1);
//					System.out.println("\n");

					
					System.out.println("src_dep:"+src_dep);
					//System.out.println("src_dep_attr:"+src_dep_attr);
					//System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
						
						}
					}
				 //}
				}
				
					if (j - 1 > 0)
					{
					
//						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j-2),
//								metamodelPath + "/" + l.get(i).get(j - 1)).size();e1++)
//						{
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j-2),
								metamodelPath + "/" + l.get(i).get(j - 1)).get(0)));
					
					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");
					
//					src_dep1_attr = srcDependency1_new_attr(module2);
//					System.out.println("\n");
//					trg_dep1_attr = trgDependency1_new_attr(module2);
//					System.out.println("\n");
					
					
//					System.out.println("src_dep_prev:"+src_dep1);
					System.out.println("trg_dep1_prev:"+trg_dep1);
					//System.out.println("trg_dep1_attr_prev:"+trg_dep1_attr);
					
					if(j-1 > 0)
					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_prev = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
							
							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters()
									.size(); n++) {
								
								type_prev1 = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters().get(n));
								
		
						//}
						}
					}
			
					//ArrayList<Boolean> boolflag = new ArrayList<Boolean>();
					Boolean[] boolflag = new Boolean[trg_dep1.size()];
					//boolean flag = false;
					if(!src_dep.isEmpty()) {
					
					for(tr=0;tr<trg_dep1.size();tr++) {
						
						String[] split_trg = trg_dep1.get(tr).split("\\s+");
						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
						for(int sr=0;sr<src_dep.size();sr++) {
	
							String[] split_src = src_dep.get(sr).split("\\s+");
//							System.out.println("split_src "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]+split_src[6]);
//							System.out.println("split_trg_prev "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]+split_trg[6]);
							
							if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim().equals((split_src[1]+split_src[2]+split_src[3]).trim()))) {
								//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
								//flag=false;
								boolflag[tr]=true;
								//boolflag.add(true);
										
							}
								//boolflag[tr]=false;
							else {
								//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
								//tr_list.add(trg_dep.get(tr));
								//flag=true;
								boolflag[tr]=false;
								//boolflag.add(false);
								break;
							}
								
//						}
					}
						}
					}
					
//					}
//				}
//					for(Boolean b : boolflag)
//						System.out.println(b);
					for(int tr0=0;tr0<trg_dep1.size();tr0++) {
						//if(flag==false)
						if(boolflag[tr0]!=null)
						{
							if(boolflag[tr0]==true)
							//if(boolflag.get(tr0)==true)
							{
								System.out.println(trg_dep1.get(tr0)+" needs to be deleted "+"in "+type_prev.getTypeName()+" to "+type_prev1.getTypeName()+" transformation rule"+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep1.get(tr0));
								
							}
								
							//if(flag==true)
							//if(boolflag[tr0]==true)
							else
							{
								System.out.println("Keep "+trg_dep1.get(tr0)+" for transformation "+type_prev.getModelName()+"2"+type_prev1.getModelName()+"\n");
								line_kept.add(trg_dep1.get(tr0));
							}
						}
						
					}
							
//					  }
//					}
				
					}
				}
				//}
				
			}
			line_kept_chain.add(line_kept);
			tr_list_chain.add(tr_list);
		}
		return line_kept_chain;

	}
	
	
	public ArrayList<String> keeptrindex2_single(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		ArrayList<String> bestchain = identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		
//		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
//		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		Chain_MT cm = new Chain_MT();
//		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
//		//System.out.println(identifyETLinModels(sourceMM, targetMM));
//		if (etl1) {
//		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
//			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
//			//System.out.println("qwerty: "+x);
//			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
//			//break;
//		}
//		}
//		if (!two.isEmpty())
//			l.add(two);
		
		//ArrayList<Integer> tr_list = new ArrayList<Integer>();
		
		//List<ArrayList<String>> tr_list = null;
		ArrayList<String> tr_list = null, line_kept = null;
		
		ArrayList<String> tr_list_chain = null;
		tr_list_chain = new ArrayList<String>();
		ArrayList<String> line_kept_chain = new ArrayList<String>();
		int tr = 0;
		int k;
		
		//for (int i = 0; i < l.size(); i++) {
			//System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			//int total = 0;
			
			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();
			
			for (int j = bestchain.size()-1; j >=0; j--) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep = null, src_dep1 = null, src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				
				src_dep = new ArrayList<String>();
				trg_dep = new ArrayList<String>();
				src_dep1 = new ArrayList<String>();
				trg_dep1 = new ArrayList<String>();
				
				if (j > 0) {

					
					System.out.println(bestchain.get(j-1) + " -> " + bestchain.get(j) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + bestchain.get(j-1),
							metamodelPath + "/" + bestchain.get(j)).size();e++) 
					{
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j-1),
							metamodelPath + "/" + bestchain.get(j)).get(e)));
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
//					src_dep_attr = srcDependency1_new_attr(module1);
//					System.out.println("\n");
					
					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");

					
					System.out.println("src_dep:"+src_dep);
					//System.out.println("src_dep_attr:"+src_dep_attr);
					//System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
						
						}
					}
				 }
				}
				
					if (j - 1 > 0)
					{
					
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + bestchain.get(j-2),
								metamodelPath + "/" + bestchain.get(j - 1)).size();e1++)
						{
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j-2),
								metamodelPath + "/" + bestchain.get(j - 1)).get(e1)));
					
					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");
					
//					src_dep1_attr = srcDependency1_new_attr(module2);
//					System.out.println("\n");
//					trg_dep1_attr = trgDependency1_new_attr(module2);
//					System.out.println("\n");
					
					
//					System.out.println("src_dep_prev:"+src_dep1);
					System.out.println("trg_dep1_prev:"+trg_dep1);
					//System.out.println("trg_dep1_attr_prev:"+trg_dep1_attr);
					
					if(j-1 > 0)
					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_prev = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
							
							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters()
									.size(); n++) {
								
								type_prev1 = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters().get(n));
								
		
						}
						}
					}
						}
					}
			
					//ArrayList<Boolean> boolflag = new ArrayList<Boolean>();
					if(trg_dep1.size()>0) {
					Boolean[] boolflag = new Boolean[trg_dep1.size()];
					//boolean flag = false;
					if(!src_dep.isEmpty()) {
						
						for(tr=0;tr<trg_dep1.size();tr++) {
							
							String[] split_trg = trg_dep1.get(tr).split("\\s+");
							//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
							inner:for(int sr=0;sr<src_dep.size();sr++) {
		
								
								String[] split_src = src_dep.get(sr).split("\\s+");
//								System.out.println("split_src "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]+split_src[6]);
//								System.out.println("split_trg_prev "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]+split_trg[6]);
								
								//if(!((split_trg[1]+split_trg[2]+split_trg[3]).equals((split_src[1]+split_src[2]+split_src[3])))) {
								if(split_trg[1].equals(split_src[1]) && 
										split_trg[2].equals(split_src[2]) && 
										split_trg[3].equals(split_src[3]) &&
										split_trg[4].split("2")[1].equals(split_src[4].split("2")[0]) &&
										split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])
										) {
									//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
									//flag=false;
									boolflag[tr]=false;
									//boolflag.add(true);
									break inner;
											
								}
									//boolflag[tr]=false;
								else if(!split_trg[1].equals(split_src[1]) && 
										!split_trg[2].equals(split_src[2]) && 
										!split_trg[3].equals(split_src[3]) &&
										split_trg[4].split("2")[1].equals(split_src[4].split("2")[0]) &&
										split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])
										) {
									//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
									//tr_list.add(trg_dep.get(tr));
									//flag=true;
									boolflag[tr]=true;
									//boolflag.add(false);
									//break;
								}
								else
									boolflag[tr]=false;
//								else if(!(split_trg[1].equals(split_src[1])) && !(split_trg[2].equals(split_src[2])) && !(split_trg[3].equals(split_src[3])) 
//										&& sr==(src_dep.size()-1) && split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])) {
//				
//									boolflag[tr]=true;
//									
//								}
									
//							}
						}
							}
						}
					
//					}
//				}
//					for(Boolean b : boolflag)
//						System.out.println(b);
					for(int tr0=0;tr0<trg_dep1.size();tr0++) {
						//if(flag==false)
						if(boolflag[tr0]!=null)
						{
							if(boolflag[tr0]==true)
							//if(boolflag.get(tr0)==true)
							{
								System.out.println(trg_dep1.get(tr0)+" needs to be deleted "+"in "+type_prev.getTypeName()+" to "+type_prev1.getTypeName()+" transformation rule"+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep1.get(tr0));
								
							}
								
							//if(flag==true)
							//if(boolflag[tr0]==true)
							else
							{
								System.out.println("Keep "+trg_dep1.get(tr0)+" for transformation "+type_prev.getModelName()+"2"+type_prev1.getModelName()+"\n");
								line_kept.add(trg_dep1.get(tr0));
							}
						}
						
					}
					}
							
//					  }
//					}
				
//					}
//				}
				//}
				
			}
			line_kept_chain.addAll(line_kept);
			tr_list_chain.addAll(tr_list);
		//}
		return line_kept_chain;

	}
	
	public List<ArrayList<String>> deletetrindex1(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			//System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		
		//ArrayList<Integer> tr_list = new ArrayList<Integer>();
		
		//List<ArrayList<String>> tr_list = null;
		ArrayList<String> tr_list = null;
		
		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			//int total = 0;
			
			tr_list = new ArrayList<String>();
			
			
			for (int j = l.get(i).size()-1; j >=0; j--) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, trg_dep = null, src_dep1 = null, trg_dep1 = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				
				if (j > 0) {

//					registerMM(metamodelsRoot.resolve(l.get(i).get(j)).toString());
//					registerMM(metamodelsRoot.resolve(l.get(i).get(j+1)).toString());
					
					System.out.println(l.get(i).get(j-1) + " -> " + l.get(i).get(j) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j-1),
							metamodelPath + "/" + l.get(i).get(j)).size();e++) 
					{
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j-1),
							metamodelPath + "/" + l.get(i).get(j)).get(e)));
					//}
					
					
					//total = calculateMTChain(module1);
//						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
						
//						for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
					
					
//					staticAnalyser.validate(module1);
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");
					
//					try {
//						src_dep.set(5, identifyETL(metamodelPath + "/" +src_dep.get(5).split("2")[0]+".ecore", 
//											metamodelPath + "/" +src_dep.get(5).split("2")[1]+".ecore").get(e));
//						trg_dep.set(5, identifyETL(metamodelPath + "/" +trg_dep.get(5).split("2")[0]+".ecore", 
//								metamodelPath + "/" +trg_dep.get(5).split("2")[1]+".ecore").get(e));
//					} catch (Exception e2) {
//						// TODO Auto-generated catch block
//						//e2.printStackTrace();
//						System.out.println(e2.getMessage());
//					}
					
					System.out.println("src_dep:"+src_dep);
					//System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
							
//							for(int gtr=0;gtr<module1.getTransformationRules().size();gtr++) {
//								for(int td=0;td<src_dep.size();td++) {
//									System.out.println("\nThis Source Dependency: "+module1.getTransformationRules().get(gtr)+" "+src_dep.get(td));
//								}
//								//break;
//							}
								
						
							if(src_dep.size()>0)
								System.out.println("\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
						for(int ss=0;ss<src_dep.size();ss++)
						{
							String[] split_src = src_dep.get(ss).split("\\s+");
							
							if(split_src.length>1)
							{
			
								Node a1 = new Node(split_src[0]);
								Node b1 = new Node(split_src[1]);
								Node c1 = new Node(split_src[2]);
								Node d1 = new Node(split_src[3]);
								Node e1 = new Node(split_src[4]);
								Node f1 = new Node(split_src[6]);
//								Node f1 = new Node(identifyETL(metamodelPath + "/" +split_src[5].split("2")[0]+".ecore", 
//										metamodelPath + "/" +split_src[5].split("2")[1]+".ecore").get(e));
//								Node sourceMTrule = new Node(type.getTypeName());
//								Node targetMTrule = new Node(type1.getTypeName());
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								list.add(e1);
								list.add(f1);
//								list.add(sourceMTrule);
//								list.add(targetMTrule);
								
								Graph g = new Graph(list);
								g.addEdge(d1, a1);
								g.addEdge(d1, b1);
								g.addEdge(d1, c1);
								g.addEdge(d1, e1);
								g.addEdge(d1, f1);
//								g.addEdge(d1, sourceMTrule);
//								g.addEdge(d1, targetMTrule);
								
								//g.printAdjList();
							}
						}
						
//						if(trg_dep.size()>0)
//							System.out.println("\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
//						for(int ss=0;ss<trg_dep.size();ss++)
//						{
//							String[] split_trg = trg_dep.get(ss).split("\\s+");
//							
//							if(split_trg.length>1)
//							{
//			
//								Node a1 = new Node(split_trg[0]);
//								Node b1 = new Node(split_trg[1]);
//								Node c1 = new Node(split_trg[2]);
//								Node d1 = new Node(split_trg[3]);
//								Node e1 = new Node(split_trg[4]);
//								Node f1 = new Node(split_trg[6]);
//								Node g1 = new Node(split_trg[7]);
////								Node f1 = new Node(identifyETL(metamodelPath + "/" +split_trg[5].split("2")[0]+".ecore", 
////										metamodelPath + "/" +split_trg[5].split("2")[1]+".ecore").get(e));
//								//Node f1 = new Node(split_trg[5]);
////								Node sourceMTrule = new Node(type.getTypeName());
////								Node targetMTrule = new Node(type1.getTypeName());
//								
//								ArrayList<Node> list = new ArrayList<Node>();
//								list.add(a1);
//								list.add(b1);
//								list.add(c1);
//								list.add(d1);
//								list.add(e1);
//								list.add(f1);
//								list.add(g1);
////								list.add(sourceMTrule);
////								list.add(targetMTrule);
//								
//								Graph g = new Graph(list);
//								g.addEdge(d1, a1);
//								g.addEdge(d1, b1);
//								g.addEdge(d1, c1);
//								g.addEdge(d1, e1);
//								g.addEdge(d1, f1);
//								g.addEdge(d1, g1);
////								g.addEdge(d1, sourceMTrule);
////								g.addEdge(d1, targetMTrule);
//							
//								g.printAdjList();
//							}
//							
//						}
//						System.out.println();
						
						}
					}
				 }
				}
				
					if (j - 1 > 0)
					{
					
//						registerMM(metamodelsRoot.resolve(l.get(i).get(j)).toAbsolutePath().toString());
//						registerMM(metamodelsRoot.resolve(l.get(i).get(j+1)).toAbsolutePath().toString());
						
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j-2),
								metamodelPath + "/" + l.get(i).get(j - 1)).size();e1++)
						{
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j-2),
								metamodelPath + "/" + l.get(i).get(j - 1)).get(e1)));
						//}
//							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
//								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//									staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//								}
//							}
//							staticAnalyser.validate(module2);
					
					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");
					
//					try {
//						src_dep1.set(5, identifyETL(metamodelPath + "/" +src_dep1.get(5).split("2")[0]+".ecore", 
//								metamodelPath + "/" +src_dep1.get(5).split("2")[1]+".ecore").get(e1));
//						trg_dep1.set(5, identifyETL(metamodelPath + "/" +trg_dep1.get(5).split("2")[0]+".ecore", 
//								metamodelPath + "/" +trg_dep1.get(5).split("2")[1]+".ecore").get(e1));
//					} catch (Exception e2) {
//						// TODO Auto-generated catch block
//						//e2.printStackTrace();
//						System.out.println(e2.getMessage());
//					}
					
//					System.out.println("src_dep_prev:"+src_dep1);
					System.out.println("trg_dep_prev:"+trg_dep1);
					
//					for(int gtr=0;gtr<module2.getTransformationRules().size();gtr++) {
//						for(int sd=0;sd<trg_dep1.size();sd++) {
//							System.out.println("\nPrevious Target Dependency: "+module2.getTransformationRules().get(gtr)+" "+trg_dep1.get(sd));
//						}
//						//break;
//					}
					//}
						
					
//					if(src_dep1.size()>0)
//					System.out.println("\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
//				for(int ss=0;ss<src_dep1.size();ss++)
//				{
//					String[] split_src = src_dep1.get(ss).split("\\s+");
////					split_src[5]=identifyETL(metamodelPath + "/" +split_src[5].split("2")[0]+".ecore", 
////							metamodelPath + "/" +split_src[5].split("2")[1]+".ecore").get(e1);
//					
//					if(split_src.length>1)
//					{
//
//						Node a1 = new Node(split_src[0]);
//						Node b1 = new Node(split_src[1]);
//						Node c1 = new Node(split_src[2]);
//						Node d1 = new Node(split_src[3]);
//						Node e11 = new Node(split_src[4]);
//						Node f1 = new Node(split_src[6]);
//						Node g1 = new Node(split_src[7]);
////						Node f1 = new Node(identifyETL(metamodelPath + "/" +split_src[5].split("2")[0]+".ecore", 
////								metamodelPath + "/" +split_src[5].split("2")[1]+".ecore").get(e1));
//						
////						Node sourceMTrule = new Node(type.getTypeName());
////						Node targetMTrule = new Node(type1.getTypeName());
//						
//						ArrayList<Node> list = new ArrayList<Node>();
//						list.add(a1);
//						list.add(b1);
//						list.add(c1);
//						list.add(d1);
//						list.add(e11);
//						list.add(f1);
//						list.add(g1);
////						list.add(sourceMTrule);
////						list.add(targetMTrule);
//						
//						Graph g = new Graph(list);
//						g.addEdge(d1, a1);
//						g.addEdge(d1, b1);
//						g.addEdge(d1, c1);
//						g.addEdge(d1, e11);
//						g.addEdge(d1, f1);
//						g.addEdge(d1, g1);
////						g.addEdge(d1, sourceMTrule);
////						g.addEdge(d1, targetMTrule);
//						
//						g.printAdjList();
//					}
//				}
						//}
//				
//					}
					
					if(j-1 > 0)
					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_prev = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
							
							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters()
									.size(); n++) {
								
								type_prev1 = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters().get(n));
								

//						}
//					
//					}
					
				if(trg_dep1.size()>0)
					System.out.println("\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
				for(int ss1=0;ss1<trg_dep1.size();ss1++)
				{
					String[] split_trg = trg_dep1.get(ss1).split("\\s+");
					
					if(split_trg.length>1)
					{

						Node a1 = new Node(split_trg[0]);
						Node b1 = new Node(split_trg[1]);
						Node c1 = new Node(split_trg[2]);
						Node d1 = new Node(split_trg[3]);
						Node e11 = new Node(split_trg[4]);
						Node f1 = new Node(split_trg[6]);
//						Node f1 = new Node(identifyETL(metamodelPath + "/" +split_trg[5].split("2")[0]+".ecore", 
//								metamodelPath + "/" +split_trg[5].split("2")[1]+".ecore").get(e1));
						
//						Node sourceMTrule = new Node(type.getTypeName());
//						Node targetMTrule = new Node(type1.getTypeName());
						
						ArrayList<Node> list = new ArrayList<Node>();
						list.add(d1);
						list.add(a1);
						list.add(b1);
						list.add(c1);
						list.add(e11);
						list.add(f1);
						
//						list.add(sourceMTrule);
//						list.add(targetMTrule);
						
						Graph g = new Graph(list);
						g.addEdge(d1, a1);
						g.addEdge(d1, b1);
						g.addEdge(d1, c1);
						g.addEdge(d1, e11);
						g.addEdge(d1, f1);
						
//						g.addEdge(d1, sourceMTrule);
//						g.addEdge(d1, targetMTrule);
					
						//g.printAdjList();
					}
					
				}
				System.out.println();
				
						}
						}
					}
			
					//}
//				}
					//ArrayList<Boolean> boolflag = new ArrayList<Boolean>();
					Boolean[] boolflag = new Boolean[trg_dep1.size()];
					//boolean flag = false;
					if(!src_dep.isEmpty()) {
					
					for(tr=0;tr<trg_dep1.size();tr++) {
						
						String[] split_trg = trg_dep1.get(tr).split("\\s+");
						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
						for(int sr=0;sr<src_dep.size();sr++) {
	
							String[] split_src = src_dep.get(sr).split("\\s+");
							System.out.println("split_src "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]+split_src[6]+split_src[7]);
							System.out.println("split_trg_prev "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]+split_trg[6]+split_trg[7]);
							
//							String[] split_trg1 = null;
//							if(tr+1<trg_dep1.size())
//								split_trg1 = trg_dep1.get(tr+1).split("\\s+");
//							//{
//							//if(tr+1<trg_dep.size())
//								if(tr+1<trg_dep1.size()) {
//								//if(trg_dep.get(tr).substring(0, 1).equals(trg_dep.get(tr+1).substring(0, 1)))
//								//{
////									if(((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()) && 
////											!((split_trg1[1]+split_trg1[2]+split_trg1[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim())) {
////										boolflag[tr]=false;
////									}
////									else if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()) && 
////											((split_trg1[1]+split_trg1[2]+split_trg1[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
////									{
////										boolflag[tr]=false;
////									}
//									
//									if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()) && 
//											!((split_trg1[1]+split_trg1[2]+split_trg1[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
//									//if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
//									{
//										boolflag[tr]=true;
//										//boolflag[tr+1]=true;
//									}
//										
//									else
//										boolflag[tr]=false;
//										
//								//}
//							}
//							
//								else {
									if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim().equals((split_src[1]+split_src[2]+split_src[3]).trim()))) {
										//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
										//flag=false;
										boolflag[tr]=true;
										//boolflag.add(true);
										//continue;
										
									}
									//boolflag[tr]=false;
									else {
										//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
										//tr_list.add(trg_dep.get(tr));
										//flag=true;
										boolflag[tr]=false;
										//boolflag.add(false);
									}
								//}
							//}
								
//						}
					}
						}
					}
					
//					}
//				}
//					for(Boolean b : boolflag)
//						System.out.println(b);
					for(int tr0=0;tr0<trg_dep1.size();tr0++) {
						//if(flag==false)
						//if(boolflag[tr0]!=null)
						//{
							if(boolflag[tr0]==true)
							//if(boolflag.get(tr0)==true)
							{
								System.out.println(trg_dep1.get(tr0)+" needs to be deleted "+"in "+type_prev.getTypeName()+" to "+type_prev1.getTypeName()+" transformation rule"+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep1.get(tr0));
								
							}
								
							//if(flag==true)
							//if(boolflag[tr0]==true)
							else
							{
								System.out.println("Keep "+trg_dep1.get(tr0)+" for transformation "+type_prev.getModelName()+"2"+type_prev1.getModelName()+"\n");
							}
						//}
						
					}
							
//					  }
//					}
				
					}
				}
				//}
				
			}
			
			tr_list_chain.add(tr_list);
		}
		return tr_list_chain;

	}
	
	public List<ArrayList<String>> deletetrindex(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		//List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			//System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		
		//ArrayList<Integer> tr_list = new ArrayList<Integer>();
		
		//List<ArrayList<String>> tr_list = null;
		ArrayList<String> tr_list = null;
		
		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			//int total = 0;
			
			tr_list = new ArrayList<String>();
			
			
			for (int j = 0; j < l.get(i).size(); j++) {

				
				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
				ArrayList<String> src_dep = null, trg_dep = null, src_dep1 = null, trg_dep1 = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				
				if (j + 1 < l.get(i).size()) {

//					registerMM(metamodelsRoot.resolve(l.get(i).get(j)).toString());
//					registerMM(metamodelsRoot.resolve(l.get(i).get(j+1)).toString());
					
					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++) 
					{
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));
					//}
					
					
					//total = calculateMTChain(module1);
//						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
						
//						for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
					
					
//					staticAnalyser.validate(module1);
					
					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");
					
//					try {
//						src_dep.set(5, identifyETL(metamodelPath + "/" +src_dep.get(5).split("2")[0]+".ecore", 
//											metamodelPath + "/" +src_dep.get(5).split("2")[1]+".ecore").get(e));
//						trg_dep.set(5, identifyETL(metamodelPath + "/" +trg_dep.get(5).split("2")[0]+".ecore", 
//								metamodelPath + "/" +trg_dep.get(5).split("2")[1]+".ecore").get(e));
//					} catch (Exception e2) {
//						// TODO Auto-generated catch block
//						//e2.printStackTrace();
//						System.out.println(e2.getMessage());
//					}
					
					System.out.println("src_dep:"+src_dep);
					System.out.println("trg_dep:"+trg_dep);
					
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
						
							
							for(int gtr=0;gtr<module1.getTransformationRules().size();gtr++) {
								for(int td=0;td<trg_dep.size();td++) {
									System.out.println("\nThis Target Dependency: "+module1.getTransformationRules().get(gtr)+" "+trg_dep.get(td));
								}
								//break;
							}
								
						
							if(src_dep.size()>0)
								System.out.println("\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
						for(int ss=0;ss<src_dep.size();ss++)
						{
							String[] split_src = src_dep.get(ss).split("\\s+");
							
							if(split_src.length>1)
							{
			
								Node a1 = new Node(split_src[0]);
								Node b1 = new Node(split_src[1]);
								Node c1 = new Node(split_src[2]);
								Node d1 = new Node(split_src[3]);
								Node e1 = new Node(split_src[4]);
								Node f1 = new Node(split_src[6]);
//								Node f1 = new Node(identifyETL(metamodelPath + "/" +split_src[5].split("2")[0]+".ecore", 
//										metamodelPath + "/" +split_src[5].split("2")[1]+".ecore").get(e));
//								Node sourceMTrule = new Node(type.getTypeName());
//								Node targetMTrule = new Node(type1.getTypeName());
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								list.add(e1);
								list.add(f1);
//								list.add(sourceMTrule);
//								list.add(targetMTrule);
								
								Graph g = new Graph(list);
								g.addEdge(d1, a1);
								g.addEdge(d1, b1);
								g.addEdge(d1, c1);
								g.addEdge(d1, e1);
								g.addEdge(d1, f1);
//								g.addEdge(d1, sourceMTrule);
//								g.addEdge(d1, targetMTrule);
								
								g.printAdjList();
							}
						}
						
						if(trg_dep.size()>0)
							System.out.println("\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
						for(int ss=0;ss<trg_dep.size();ss++)
						{
							String[] split_trg = trg_dep.get(ss).split("\\s+");
							
							if(split_trg.length>1)
							{
			
								Node a1 = new Node(split_trg[0]);
								Node b1 = new Node(split_trg[1]);
								Node c1 = new Node(split_trg[2]);
								Node d1 = new Node(split_trg[3]);
								Node e1 = new Node(split_trg[4]);
								Node f1 = new Node(split_trg[6]);
//								Node f1 = new Node(identifyETL(metamodelPath + "/" +split_trg[5].split("2")[0]+".ecore", 
//										metamodelPath + "/" +split_trg[5].split("2")[1]+".ecore").get(e));
								//Node f1 = new Node(split_trg[5]);
//								Node sourceMTrule = new Node(type.getTypeName());
//								Node targetMTrule = new Node(type1.getTypeName());
								
								ArrayList<Node> list = new ArrayList<Node>();
								list.add(a1);
								list.add(b1);
								list.add(c1);
								list.add(d1);
								list.add(e1);
								list.add(f1);
//								list.add(sourceMTrule);
//								list.add(targetMTrule);
								
								Graph g = new Graph(list);
								g.addEdge(d1, a1);
								g.addEdge(d1, b1);
								g.addEdge(d1, c1);
								g.addEdge(d1, e1);
								g.addEdge(d1, f1);
//								g.addEdge(d1, sourceMTrule);
//								g.addEdge(d1, targetMTrule);
							
								g.printAdjList();
							}
							
						}
						System.out.println();
						
						}
					}
				 }
				}
				
					if (j + 1 < l.get(i).size()-1)
					{
					
//						registerMM(metamodelsRoot.resolve(l.get(i).get(j)).toAbsolutePath().toString());
//						registerMM(metamodelsRoot.resolve(l.get(i).get(j+1)).toAbsolutePath().toString());
						
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
								metamodelPath + "/" + l.get(i).get(j + 2)).size();e1++)
						{
							EtlModule module2 = new EtlModule();
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
								metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));
						//}
//							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
//								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//									staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//								}
//							}
//							staticAnalyser.validate(module2);
					
					src_dep1 = srcDependency1(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1(module2);
					System.out.println("\n");
					
//					try {
//						src_dep1.set(5, identifyETL(metamodelPath + "/" +src_dep1.get(5).split("2")[0]+".ecore", 
//								metamodelPath + "/" +src_dep1.get(5).split("2")[1]+".ecore").get(e1));
//						trg_dep1.set(5, identifyETL(metamodelPath + "/" +trg_dep1.get(5).split("2")[0]+".ecore", 
//								metamodelPath + "/" +trg_dep1.get(5).split("2")[1]+".ecore").get(e1));
//					} catch (Exception e2) {
//						// TODO Auto-generated catch block
//						//e2.printStackTrace();
//						System.out.println(e2.getMessage());
//					}
					
					System.out.println("src_dep_next:"+src_dep1);
					System.out.println("trg_dep_next:"+trg_dep1);
					
					for(int gtr=0;gtr<module2.getTransformationRules().size();gtr++) {
						for(int sd=0;sd<src_dep1.size();sd++) {
							System.out.println("\nNext Source Dependency: "+module2.getTransformationRules().get(gtr)+" "+src_dep1.get(sd));
						}
						//break;
					}
					//}
						
					
					if(src_dep1.size()>0)
					System.out.println("\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
				for(int ss=0;ss<src_dep1.size();ss++)
				{
					String[] split_src = src_dep1.get(ss).split("\\s+");
//					split_src[5]=identifyETL(metamodelPath + "/" +split_src[5].split("2")[0]+".ecore", 
//							metamodelPath + "/" +split_src[5].split("2")[1]+".ecore").get(e1);
					
					if(split_src.length>1)
					{

						Node a1 = new Node(split_src[0]);
						Node b1 = new Node(split_src[1]);
						Node c1 = new Node(split_src[2]);
						Node d1 = new Node(split_src[3]);
						Node e11 = new Node(split_src[4]);
						Node f1 = new Node(split_src[6]);
//						Node f1 = new Node(identifyETL(metamodelPath + "/" +split_src[5].split("2")[0]+".ecore", 
//								metamodelPath + "/" +split_src[5].split("2")[1]+".ecore").get(e1));
						
//						Node sourceMTrule = new Node(type.getTypeName());
//						Node targetMTrule = new Node(type1.getTypeName());
						
						ArrayList<Node> list = new ArrayList<Node>();
						list.add(a1);
						list.add(b1);
						list.add(c1);
						list.add(d1);
						list.add(e11);
						list.add(f1);
//						list.add(sourceMTrule);
//						list.add(targetMTrule);
						
						Graph g = new Graph(list);
						g.addEdge(d1, a1);
						g.addEdge(d1, b1);
						g.addEdge(d1, c1);
						g.addEdge(d1, e11);
						g.addEdge(d1, f1);
//						g.addEdge(d1, sourceMTrule);
//						g.addEdge(d1, targetMTrule);
						
						g.printAdjList();
					}
				}
						//}
//				
//					}
					
					if(j+1 < l.get(i).size()-1)
					{ 
						
						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
						{
							
							type_next = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

//						}
//					
//					}
					
				if(trg_dep1.size()>0)
					System.out.println("\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
				for(int ss1=0;ss1<trg_dep1.size();ss1++)
				{
					String[] split_trg = trg_dep1.get(ss1).split("\\s+");
					
					if(split_trg.length>1)
					{

						Node a1 = new Node(split_trg[0]);
						Node b1 = new Node(split_trg[1]);
						Node c1 = new Node(split_trg[2]);
						Node d1 = new Node(split_trg[3]);
						Node e11 = new Node(split_trg[4]);
						Node f1 = new Node(split_trg[6]);
//						Node f1 = new Node(identifyETL(metamodelPath + "/" +split_trg[5].split("2")[0]+".ecore", 
//								metamodelPath + "/" +split_trg[5].split("2")[1]+".ecore").get(e1));
						
//						Node sourceMTrule = new Node(type.getTypeName());
//						Node targetMTrule = new Node(type1.getTypeName());
						
						ArrayList<Node> list = new ArrayList<Node>();
						list.add(d1);
						list.add(a1);
						list.add(b1);
						list.add(c1);
						list.add(e11);
						list.add(f1);
						
//						list.add(sourceMTrule);
//						list.add(targetMTrule);
						
						Graph g = new Graph(list);
						g.addEdge(d1, a1);
						g.addEdge(d1, b1);
						g.addEdge(d1, c1);
						g.addEdge(d1, e11);
						g.addEdge(d1, f1);
						
//						g.addEdge(d1, sourceMTrule);
//						g.addEdge(d1, targetMTrule);
					
						g.printAdjList();
					}
					
				}
				System.out.println();
				
						}
					}
			
					//}
//				}
					Boolean[] boolflag = new Boolean[trg_dep.size()];
					//boolean flag = false;
					if(!src_dep1.isEmpty()) {
					
					for(tr=0;tr<trg_dep.size();tr++) {
						
						String[] split_trg = trg_dep.get(tr).split("\\s+");
						//System.out.println("split_trg1111 "+trg_dep.get(tr).substring(0, 1));
						for(int sr=0;sr<src_dep1.size();sr++) {
						
							String[] split_src = src_dep1.get(sr).split("\\s+");
//							System.out.println("split_src_next "+split_src[0]+split_src[1]+split_src[2]+split_src[3]+split_src[4]+split_src[5]);
//							System.out.println("split_trg "+split_trg[0]+split_trg[1]+split_trg[2]+split_trg[3]+split_trg[4]+split_trg[5]);
							
							String[] split_trg1 = null;
							if(tr+1<trg_dep.size())
								split_trg1 = trg_dep.get(tr+1).split("\\s+");
							//{
							//if(tr+1<trg_dep.size())
								if(tr+1<trg_dep.size()) {
								if(trg_dep.get(tr).substring(0, 1).equals(trg_dep.get(tr+1).substring(0, 1)))
								{
//									if(((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()) && 
//											!((split_trg1[1]+split_trg1[2]+split_trg1[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim())) {
//										boolflag[tr]=false;
//									}
//									else if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()) && 
//											((split_trg1[1]+split_trg1[2]+split_trg1[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
//									{
//										boolflag[tr]=false;
//									}
									
									if(!((split_trg[2]+split_trg[3]).trim()).equals((split_src[2]+split_src[3]).trim()) && 
											!((split_trg1[2]+split_trg1[3]).trim()).equals((split_src[2]+split_src[3]).trim()))
									//if(!((split_trg[1]+split_trg[2]+split_trg[3]).trim()).equals((split_src[1]+split_src[2]+split_src[3]).trim()))
									{
										boolflag[tr]=true;
										//boolflag[tr+1]=true;
									}
										
									else
										boolflag[tr]=false;
										
								}
							}
							
								else {
									if(!((split_trg[2]+split_trg[3]).trim()).equals((split_src[2]+split_src[3]).trim())) {
										//System.out.println(trg_dep.get(tr)+" is required in the next transformation\n");
										//flag=false;
										boolflag[tr]=true;
										
										//continue;
										
									}
									//boolflag[tr]=false;
									else {
										//System.out.println(trg_dep.get(tr)+" needs to be deleted.\n");
										//tr_list.add(trg_dep.get(tr));
										//flag=true;
										boolflag[tr]=false;
									}
								}
							//}
								
//						}
					}
						}
					}
					
//					}
//				}
					for(int tr0=0;tr0<trg_dep.size();tr0++) {
						//if(flag==false)
						if(boolflag[tr0]!=null)
						{
							if(boolflag[tr0]==true)
							{
								System.out.println(trg_dep.get(tr0)+" needs to be deleted "+"in "+type.getTypeName()+" to "+type1.getTypeName()+" transformation rule from "+type1.getModelName()+" metamodel."+"\n");
								//tr_list.add(Integer.parseInt(trg_dep.get(tr).trim().split("\\s+")[0]));
								tr_list.add(trg_dep.get(tr0));
								
							}
								
							//if(flag==true)
							//if(boolflag[tr0]==true)
							else
							{
								System.out.println("Keep "+trg_dep.get(tr0)+" for next transformation "+type_next+"\n");
							}
						}
						
					}
							
//					  }
//					}
				
					}
				}
				//}
				
			}
			
			tr_list_chain.add(tr_list);
		}
		return tr_list_chain;

	}
	
	public boolean findETL(String sourceMM, String targetMM) throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;
		boolean flag = false;
		registerMM(sourceMM);
		registerMM(targetMM);
		for (int i = 0; i < scriptcontents.length; i++) {	
			//System.out.println(scriptRoot.resolve(scriptcontents[i]));
			if(scriptRoot.resolve(scriptcontents[i]).toFile().exists()) {
				EtlModule module = new EtlModule();
				//IEolModule module = new EtlModule();
				//System.out.println("Script: "+ scriptcontents[i]);
				module.parse(scriptRoot.resolve(scriptcontents[i]));
			//module.getContext().setModule(module);
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			//System.out.println("1: "+module);
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			//System.out.println(module);
			staticAnalyser.validate(module);
			//System.out.println("static "+module);
			mm = ((EtlModule) module).getDeclaredModelDeclarations();
			//System.out.println(mm);
			//System.out.println(mm);
			sourceMetamodel = mm.get(0).getModel().getName();
			targetMetamodel = mm.get(1).getModel().getName();
			
			//System.out.println(sourceMM+" "+sourceMetamodel);
//			if (sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel)
//					&& targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel)) {
			if (sourceMM.substring(sourceMM.indexOf("\\")+1,sourceMM.length()).equals(sourceMetamodel+".ecore")
					&& targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()).equals(targetMetamodel+".ecore")) {
				flag = true;
				break;
			}
		}

		}
		return flag;

	}
	
	public HashMap<String, Boolean> findetlflag() throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm1;
		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();
			
			
		module.parse(scriptcontents[i]);

		module.getContext().setModule(module);
		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		//System.out.println(module);
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}
		
		staticAnalyser.validate(module);
		
		mm1 = module.getDeclaredModelDeclarations();
		//System.out.println(mm);
		sourceMetamodel = mm1.get(0).getModel().getName();
		targetMetamodel = mm1.get(1).getModel().getName();
		//boolean s2 = findETL(metamodelsRoot+sourceMetamodel+"ecore", metamodelsRoot+targetMetamodel+".ecore");
         findetl.put(scriptcontents[i]+" "+sourceMetamodel+".ecore"+" "+targetMetamodel+".ecore", 
        		 findETL(metamodelsRoot+sourceMetamodel+".ecore", metamodelsRoot+targetMetamodel+".ecore"));  
//         System.out.println(sourceMetamodel+".ecore"+" "+targetMetamodel+".ecore"+" "+ 
//        		 findETL(metamodelsRoot+sourceMetamodel+".ecore", metamodelsRoot+targetMetamodel+".ecore"));
		}
		
		return findetl;
	}
	
	public boolean findETLinModels(String sourceMM, String targetMM) throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;
		boolean flag = false;
		for (int i = 0; i < modelscontents.length; i++) {
			if(modelscontents[i].endsWith(".etl"))
			{
			
			//System.out.println(scriptRoot.resolve(scriptcontents[i]));
			if(modelsRoot.resolve(modelscontents[i]).toFile().exists()) {
				EtlModule module = new EtlModule();
			module.parse(modelsRoot.resolve(modelscontents[i]));
			module.getContext().setModule(module);
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			//System.out.println(module);
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			
			staticAnalyser.validate(module);
			
			mm = ((EtlModule) module).getDeclaredModelDeclarations();
			//System.out.println(mm);
			sourceMetamodel = mm.get(0).getModel().getName();
			targetMetamodel = mm.get(1).getModel().getName();
			if (sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel)
					&& targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel)) {
				flag = true;
				break;
			}
		}
			}

		}
		return flag;

	}
	
	public static Map<String, String> HashMap_IdETL()
    {
  
        Map<String, String> map
            = new HashMap<String, String>();
        BufferedReader br = null;
  
        try {
  
            // create file object
            File file = new File(outputFilePath_identifyETL);
  
            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(file));
  
            String line = null;
  
            // read file line by line
            while ((line = br.readLine()) != null) {
  
                // split the line by :
                String[] parts = line.split(":");
  
                // first part is name, second is number
                String name = parts[0].trim();
                String mm1 = name.split(" ")[0].trim();
                String mm2 = name.split(" ")[1].trim();
                String etlname = parts[1].trim();
  
                // put name, number in HashMap if they are
                // not empty
                if (!name.equals("") && !etlname.equals(""))
                    map.put(name, etlname);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
  
            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }
  
        return map;
    }


	public ArrayList<String> identifyETL(String sourceMM, String targetMM) throws Exception {
		ArrayList<String> etlname = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		registerMM(sourceMM);
		registerMM(targetMM);
		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();
			if(scriptRoot.resolve(scriptcontents[i]).toFile().exists()) {
			
			module.parse(scriptRoot.resolve(scriptcontents[i]));
			module.getContext().setModule(module);
			
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			//System.out.println(module);
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}
			//System.out.println(module);
			staticAnalyser.validate(module);
			//System.out.println("static "+module);
			mm = ((EtlModule) module).getDeclaredModelDeclarations();
			
			sourceMetamodel = mm.get(0).getModel().getName();
			targetMetamodel = mm.get(1).getModel().getName();
			//System.out.println(sourceMetamodel+" "+targetMetamodel);
//			registerMM(metamodelsRoot.resolve(sourceMetamodel+".ecore").toString());
//			registerMM(metamodelsRoot.resolve(targetMetamodel+".ecore").toString());
			
			if (sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel)
					&& targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel)) {
				
				etlname.add(((EtlModule) module).getSourceFile().getName());
				//break;
		
			}
			}

		}
//		for(int e=0;e<etlname.size();e++)
//			System.out.println(etlname.get(e)+"\t");
		return etlname;
	}

	public ArrayList<String> identifyETLinModels(String sourceMM, String targetMM) throws Exception {
		ArrayList<String> etlname = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		for (int i = 0; i < modelscontents.length; i++) {
			if(modelscontents[i].endsWith(".etl"))
			{
				
				if(modelsRoot.resolve(modelscontents[i]).toFile().exists()) {
					EtlModule module = new EtlModule();
				module.parse(modelsRoot.resolve(modelscontents[i]));
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

				if (sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel)
						&& targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel)) {
					
					etlname.add(((EtlModule) module).getSourceFile().getName());
					//break;
			
				}
			}
			}

		}
//		for(int e=0;e<etlname.size();e++)
//			System.out.println(etlname.get(e)+"\t");
		return etlname;
	}
	
	public String identifyETL1(String sourceMM, String targetMM) throws Exception {
		String etlname = null;
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();
			System.out.println(scriptRoot.resolve(scriptcontents[i]));
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

			if (sourceMM.substring(11).replaceFirst("[.][^.]+$", "").equals(sourceMetamodel)
					&& targetMM.substring(11).replaceFirst("[.][^.]+$", "").equals(targetMetamodel)) {
				
				etlname = ((EtlModule) module).getSourceFile().getName();
				//break;
				

			}

		}

		return etlname;
	}
	
//-----------repeated similar codes for EtlChainOptimiser---------------

	public int calculateMTChain1(IEolModule module) throws Exception {
		@SuppressWarnings("unused")
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		double start123 = System.currentTimeMillis();

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//
//				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//
//					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//
//				}
//
//			}
//			staticAnalyser.validate(module);

			EolModelElementType type = null, type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type=(EolModelElementType) staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					type1=(EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

				//}

				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					expressionName = expName.toString();
					//System.out.println("Expression name: "+expressionName);
					numberofexpression = expName.size();
					for (int l = 0; l < numberofexpression; l++) {
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
								sumofoperation = sumofoperation + totexpSize;
							}

						}

					}
					totalfeatures = totalfeatures + sumofoperation;
					//System.out.println("Total features: "+totalfeatures);
				}
				totalstatement = totalstatement + totalfeatures;
				
				
//				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
//						.size(); j++) {
//					staticAnalyser
//							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
//				}
				}
				totalstructuratlfeatures = totalstructuratlfeatures + totalstatement;
				//System.out.println("Total feature of MT "+type.getModelName()+"->"+type1.getModelName()+" = "+totalstructuratlfeatures);
			//}
		}
			
			//System.out.println("Total feature of MT "+type.getModelName()+"->"+type1.getModelName()+" = "+totalstatement);
		}
		//System.out.println("Time taken to compute complexity in transformation "+module.getFile().getName()+" is "+(System.currentTimeMillis()-start123)/1000+" seconds.");
		return totalstructuratlfeatures;

	}

	public HashMap<String, Integer> mt_complexity() throws Exception
	{
		LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
		for(String mtl : scriptcontents) {
			if(scriptRoot.resolve(mtl).toFile().exists()) {
				EtlModule module = new EtlModule();
				module.parse(scriptRoot.resolve(mtl));
				module.getContext().setModule(module);
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				//System.out.println(module);
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}
				//System.out.println(module);
				staticAnalyser.validate(module);
			
				List<ModelDeclaration> mm = ((EtlModule) module).getDeclaredModelDeclarations();
			//	System.out.println(mm);
//				String sourceMetamodel = mm.get(0).getModel().getName();
//				String targetMetamodel = mm.get(1).getModel().getName();
			
			
				//System.out.println(sourceMetamodel+" "+targetMetamodel);
				
				linkedHashMap.put(mtl, calculateMTChain1(module));
		}
		}
		
		File file = new File(outputFilePath2);
		BufferedWriter bf = null;
		
        try {
        	
            
//        	 if (!file.exists()) {
//        	     file.createNewFile();
//        	  }
        	  
            // create new BufferedWriter for the output file
        	//if(!file.exists()) 
        	//FileWriter fw = new FileWriter(outputFilePath, false);
        	bf = new BufferedWriter(new FileWriter(file));
  
            // iterate map entries
            for (Entry<String, Integer> entry :
            	linkedHashMap.entrySet()) {
  
                // put key and value separated by a colon
                bf.write(entry.getKey() + ":"
                         + entry.getValue());
  
                // new line
                bf.newLine();
            }
  
          //  bf.flush();
            bf.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
  
            try {
  
                // always close the writer
               // bf.close();
            }
            catch (Exception e) {
            }
        }
        
		return linkedHashMap;
		
	}
	
	public double calculateMTChain12(IEolModule module) throws Exception {
		@SuppressWarnings("unused")
		String statementName, expressionName;
		float numberofexpression = 0, totalstatement = 0;
		float totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName;
		ModuleElement source_st = null;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;
		StatementBlock ruleblock = null;

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);

			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				staticAnalyser.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
					//System.out.println(((EtlModule) module).getTransformationRules().get(0));

				}
				
//				System.out.println(module.getChildren().get(0).getChildren());
//				
//				for(int s=0;s<module.getChildren().get(0).getChildren().size();s++)
//				{
//					source_st = module.getChildren().get(0).getChildren().get(s);
//					System.out.println(source_st);
//					
//				}
//				System.out.println("source_st size="+module.getChildren().get(0).getChildren().size());
//				
				ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(0)
						.getBody().getBody();
				float sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					expressionName = expName.toString();
					//System.out.println("Expression name: "+expressionName);
					numberofexpression = (float) expName.size();
					for (int l = 0; l < numberofexpression; l++) {
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = (float) totexpName.get(m).size();
								sumofoperation = (float) (sumofoperation + totexpSize);
								
							}

						}
							
						
					}
					
//					System.out.println("Expr name: "+totexpName);
//					System.out.println("Total op: "+sumofoperation);
					
					totalfeatures = (float) (totalfeatures + sumofoperation);
					//System.out.println("Total source features: "+totalfeatures);
				}
				totalstatement = (float) (totalstatement + totalfeatures);
				
//				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
//						.size(); j++) {
//					staticAnalyser
//							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
//				}
				totalstructuratlfeatures = (float) (totalstructuratlfeatures + totalstatement);
				
			}
			
		}
		//System.out.println("Total source expression: "+ruleblock.getStatements().size());
		
		return totalstructuratlfeatures;
		
	}

	
	public ArrayList<String> identifyBestchain1(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		int min = 9999;
		
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];

		for (int i = 0; i < l.size(); i++) {
			int total = 0;
			for (int j = 0; j < l.get(i).size(); j++) {

				EtlModule module1 = new EtlModule();

				if (j + 1 < l.get(i).size()) {

					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

					total = calculateMTChain1(module1);
					sum[i] = sum[i] + total;

//					System.out.println("Total operators used in the transformation " + l.get(i).get(j) + " -> "
//							+ l.get(i).get(j + 1) + ": " + total + "\n");
				}

			}
			if (sum[i] < min) {
				min = sum[i];
				index = l.get(i);
			}

			System.out.println("Total operators used in the chain: " + sum[i]);
//			System.out.println("---------------------------------------------------------\n");

		}
		return index;

	}

	public ArrayList<String> identifyBestChain2(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			l.add(two);
		System.out.println("Chains: "+l);

		int min = 99999;
		ArrayList<String> index = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];

		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			int total = 0;
			for (int j = 0; j < l.get(i).size(); j++) {

				EtlModule module1 = new EtlModule();

				if (j + 1 < l.get(i).size()) {

					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

					total = calculateMTChain(module1);
					sum[i] = sum[i] + total;

					System.out.println("Total expressions/operators used in the transformation " + l.get(i).get(j)
							+ " -> " + l.get(i).get(j + 1) + ": " + total + "\n");
				}

			}
			if (sum[i] < min) {
				min = sum[i];
				index = l.get(i);
			}

			System.out.println("Total expressions/operators used in the chain: " + sum[i]);
//			System.out.println("---------------------------------------------------------\n");

		}

		System.out.println("\nMT Chain " + index + " has minimum structural features of " + min);
		return index;

	}

	
	public ArrayList<Double> calculateMTCoverage(String sourceMM, String targetMM)
				throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();
		
		for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
		{
			EtlModule module1 = new EtlModule();
			
			module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
			System.out.println("\n"+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		//}
			
		
		//double total = calculateMTChain12(module1);
		//System.out.println("123");
		EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
		ArrayList<String> src_dep = srcDependency2(module1);
		//ArrayList<String> trg_dep = trgDependency1(module1);
		
		//System.out.println("Source Dependency: "+src_dep);
		
		staticAnalyser.validate(module1);
		EolModelElementType type = null, type1 = null;
		double coverage = 0;
		
		int src_no_of_features=0;
		double cov=0;
		int count_feature=0;
		double count=0;
		int c=0;
		
			for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
				
				
				c++;
				//System.out.println("Source type: "+type.getTypeName());
				
				
				count_feature = src_dep.size()+c;
				
				//double sum1=0,sum2=0, sum3=0;
				
//				src_no_of_features=src_dep.size();
//				
//				//src_dep_string = src_dep.flatMap(Collection::stream)
//				
//				String listString = "";
//
//				for (String s : src_dep)
//				{
//				    listString += s + " ";
//				}
//				listString = listString.substring(0, listString.length()-1);
//				//System.out.println(listString.substring(0, listString.length()));
//
//				System.out.println(listString);
//				
//				ArrayList<String> strList = new ArrayList<String>(Arrays.asList(listString));
//			    System.out.println("list1 "+strList);
////				for(String f : strList)
////				{
//					String[] splited = listString.split("\\s+");
//					
//					splited = new HashSet<String>(Arrays.asList(splited)).toArray(new String[0]);
//					count_feature = splited.length;
					//System.out.println(count_feature);
					//System.out.println(splited);

				for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
						.size(); n++) {
					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
				//}
					
//					registerMM(metamodelsRoot.resolve(type.getModelName()+".ecore").toAbsolutePath().toString());
//					registerMM(metamodelsRoot.resolve(type1.getModelName()+".ecore").toAbsolutePath().toString());
					
					System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());
					
//					String code2 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
//							+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size().asString().println();\r\n}";
//					
//					
//					String code20 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
//							+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size().asString().println();\r\n}";
//							
//					FileWriter fw21 = new FileWriter(scriptRoot.resolve("newDep2" + ".eol").toString());
//					fw21.write(code2);
//					fw21.close();
//					
//					FileWriter fw210 = new FileWriter(scriptRoot.resolve("newDep20" + ".eol").toString());
//					fw210.write(code20);
//					fw210.close();
//					
//					String x2 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep2" + ".eol"));
//					String x20 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep20" + ".eol"));
//					
//					double i1 = NumberUtils.toDouble(x2.trim());
//					double i2 = NumberUtils.toDouble(x20.trim());
//					//System.out.println(i1 +" "+i2);
//					sum1 = sum1+i1; 
//					sum2 = sum2+i2; 
//					sum3 = i1+i2;
//					scriptRoot.resolve("newDep2" + ".eol").toFile().delete();
//					scriptRoot.resolve("newDep20" + ".eol").toFile().delete();
				//}
		
//			System.out.println("No. of attributes in source "+type.getTypeName()+" is "+x2);
//			System.out.println("No. of references in source "+type.getTypeName()+" is "+x20);
					
			System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
					+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
					
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
		
				String code = "(EClass.all.size().asInteger()+EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";
				
//					FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//					fw11.write(code);
//					fw11.close();
//					System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
//					String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
					String x = executeEOL1(sourceMM111, metaMM,code);
					
					count = NumberUtils.toDouble(x.trim());
					System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
					
//					scriptRoot.resolve("newDep" + ".eol").toFile().delete();
				
					//cov1*=tot_cov;
					
				}	
			}
				//coverage = (sum3/count);
				//coverage = (float) (count_feature/count);
				//System.out.println("Coverage of the transformation rule "+type.getTypeName()+"2"+type1.getTypeName()+" is "+coverage);
				//tot_cov += coverage;
			cov = (float) count_feature/count;
			tot_cov.add(cov);
				
			//}
			//}
			//tot_cov.add(cov);
			//System.out.println(cov);
		
		}
		
		//System.out.println(module1.getModule().getClass());
		//System.out.println("Transformation coverage of model transformation is "+tot_cov+"\n");
		//tot_cov += coverage;
		
		//return cov1;
		//return coverage;
		return tot_cov;

			
	}
	
	public HashMap<String, Double> allmtcoverage() throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm1;
		//ArrayList<Double> tot_cov = new ArrayList<Double>();
		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();
			
			ArrayList<EolModelElementType> rules = new ArrayList<EolModelElementType>();
		module.parse(scriptRoot.resolve(scriptcontents[i]));
		
		module.getContext().setModule(module);
		ArrayList<String> src_dep = srcDependency2(module);
		EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
		//System.out.println(module);
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}
		
		staticAnalyser.validate(module);
		
		mm1 = module.getDeclaredModelDeclarations();
		//System.out.println(mm);
		sourceMetamodel = mm1.get(0).getModel().getName();
		targetMetamodel = mm1.get(1).getModel().getName();
		
		//EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		

		staticAnalyser.validate(module);
		EolModelElementType type = null, type1 = null;
		double coverage = 0;

		int src_no_of_features=0;
		double cov=0;
		double count_feature=0;
		double count=0;
		int c=0;

		for (int m = 0; m < ((EtlModule) module).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module).getTransformationRules().get(m).getSourceParameter());

			c++;
			rules.add(type);
			
			
			//count_feature = (src_dep.size()*0.5)+(1*c);
			
			for (int n = 0; n < ((EtlModule) module).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(m).getTargetParameters().get(n));
			
				
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asDouble()+0.5*EAttribute.all.size().asDouble()+0.5*EReference.all.size().asDouble()).asString().println();\r\n";
			

				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				//System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
				
			}	
			rules.stream().distinct().collect(Collectors.toList());
			HashSet<EolModelElementType> hset = new HashSet<EolModelElementType>(rules);
			//System.out.println("Rules: "+rules);
			count_feature = (double) (src_dep.size()*0.5)+(1*hset.size());
		}
		cov = (float) count_feature/count;
		//tot_cov.add(cov);
		allmtcoverage.put(scriptcontents[i]+" "+sourceMetamodel+".ecore"+" "+targetMetamodel+".ecore", cov);  
		}
		
		return allmtcoverage;
	}
	
	public double calculateMTCoverage_new(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
	Set<String> keys = findetl.keySet();
	
	for(String key : keys) {
		if(key.split("\\s+")[1].equals(sourceMM.substring(11)) && key.split("\\s+")[2].equals(targetMM.substring(11))) {
			EtlModule module1 = new EtlModule();
			module1.parse(scriptRoot.resolve(key.split("\\s+")[0]));
			System.out.println("\n ETL file: "+scriptRoot.resolve(key.split("\\s+")[0]));
		//}
	//}
//	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
//	{
//		EtlModule module1 = new EtlModule();
//		
//		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
//		System.out.println("\n ETL file: "+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

		//module1.parse(scriptRoot.resolve(identifyETL1(sourceMM, targetMM)));
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			ArrayList<String> src_dep = srcDependency2(module1);

			staticAnalyser.validate(module1);
			EolModelElementType type = null, type1 = null;
			double coverage = 0;
	
			int src_no_of_features=0;
			double cov=0;
			double count_feature=0;
			double count=0;
			int c=0;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

			c++;
			count_feature = src_dep.size()+(0.5*c);

			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
			
				System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());

				
				System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
						+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
				
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asInteger()+0.5*EAttribute.all.size().asInteger()+0.5*EReference.all.size().asInteger()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
//				System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
			
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
				cov = (float) count_feature/count;
				tot_cov.add(cov);
			}	
			}
		}
			
			
			}
	
	double coverage = Collections.max(tot_cov);
//	double coverage=0, max=0;
//	for(double tc : tot_cov) {
//		if(tc>max) {
//			max=tc;
//			coverage=max;
//		}	
//	}
	
	
	return coverage;

		
}
	
public double calculateMTCoverage_new_combined(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
	{
		EtlModule module1 = new EtlModule();
		
		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		System.out.println("\n ETL file: "+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

		//module1.parse(scriptRoot.resolve(identifyETL1(sourceMM, targetMM)));
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
	ArrayList<String> src_dep = srcDependency2(module1);

	staticAnalyser.validate(module1);
	EolModelElementType type = null, type1 = null;
	double coverage = 0;
	
	int src_no_of_features=0;
	double cov=0;
	double count_feature=0;
	double count=0;
	int c=0;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

			c++;
			count_feature = src_dep.size()+(0.5*c);

			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
			
				System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());

				
				System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
						+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
				
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asInteger()+0.5*EAttribute.all.size().asInteger()+0.5*EReference.all.size().asInteger()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
//				System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
				cov = (float) count_feature/count;
				tot_cov.add(cov);
			}	
		}
			
			
			}
	Double coverage = Collections.max(tot_cov);
	
	return coverage;

		
}
	
	public double calculateMTCoverage_new1(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
//	registerMM(sourceMM);
//	registerMM(targetMM);
	
	double count_feature=0;
	double count=0;
	double cov=0;
	
	double start = System.currentTimeMillis();
	Set<String> keys = allmtcoverage.keySet();
	
	//System.out.println(sourceMM+" "+targetMM);
	
	
//	for(String key : keys) {
//		if(key.split("\\s+")[1].equals(sourceMM.substring(11)) && key.split("\\s+")[2].equals(targetMM.substring(11))) {
//			EtlModule module1 = new EtlModule();
//			module1.parse(scriptRoot.resolve(key.split("\\s+")[0]));
//			System.out.println("\n ETL file: "+scriptRoot.resolve(key.split("\\s+")[0]));
			
	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
	{
		EtlModule module1 = new EtlModule();
		ArrayList<EolModelElementType> rules = new ArrayList<EolModelElementType>();
		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		//System.out.println("\nETL file: "+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

		//module1.parse(scriptRoot.resolve(identifyETL1(sourceMM, targetMM)));
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
	ArrayList<String> src_dep = srcDependency3(module1);
	
	staticAnalyser.validate(module1);
	EolModelElementType type = null, type1 = null;
	//double coverage = 0;
	
	//int src_no_of_features=0;
	
	
	int c=0;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

			c++;
			rules.add(type);
			
			
			//count_feature = (src_dep.size()*0.5)+(1*c);
			
			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
			
				//System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());

				
//				System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
//						+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
				
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asDouble()+0.5*EAttribute.all.size().asDouble()+0.5*EReference.all.size().asDouble()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
//				System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				//System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
				
			}	
			rules.stream().distinct().collect(Collectors.toList());
			HashSet<EolModelElementType> hset = new HashSet<EolModelElementType>(rules);
			//System.out.println("Rules: "+rules);
			count_feature = (double) (src_dep.size()*0.5)+(1*hset.size());
		}
			//}	
			}
	//System.out.println(count_feature+" "+count);
	cov = (float) count_feature/count;
	tot_cov.add(cov);
	Double coverage = Collections.max(tot_cov);
	
	System.out.println("Time taken to compute the coverage from "+sourceMM+" to "+targetMM+" is "+(System.currentTimeMillis()-start)/1000+" seconds.");
	
	return coverage;

		
}
	
	public double calculateMTCoverage_new2(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
	double count_feature=0;
	double count=0;
	double cov=0;
	
	Set<String> keys = allmtcoverage.keySet();
	//System.out.println("Keys: "+keys );
	for(String key : keys) {
		if(key.split("\\s+")[1].equals(sourceMM.substring(11)) && key.split("\\s+")[2].equals(targetMM.substring(11))) {
			EtlModule module1 = new EtlModule();
			module1.parse(scriptRoot.resolve(key.split("\\s+")[0]));
//			System.out.println("\n ETL file: "+scriptRoot.resolve(key.split("\\s+")[0]));
			
//	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
//	{
//		EtlModule module1 = new EtlModule();
		ArrayList<EolModelElementType> rules = new ArrayList<EolModelElementType>();
//		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		//System.out.println("\nETL file: "+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

		//module1.parse(scriptRoot.resolve(identifyETL1(sourceMM, targetMM)));
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
	ArrayList<String> src_dep = srcDependency3(module1);
	
	staticAnalyser.validate(module1);
	EolModelElementType type = null, type1 = null;
	//double coverage = 0;
	
	//int src_no_of_features=0;
	
	
	int c=0;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

			c++;
			rules.add(type);
			
			
			//count_feature = (src_dep.size()*0.5)+(1*c);
			
			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
			
				//System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());

				
//				System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
//						+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
				
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asDouble()+0.5*EAttribute.all.size().asDouble()+0.5*EReference.all.size().asDouble()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
//				System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				//System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
				
			}	
			rules.stream().distinct().collect(Collectors.toList());
			HashSet<EolModelElementType> hset = new HashSet<EolModelElementType>(rules);
			//System.out.println("Rules: "+rules);
			count_feature = (double) (src_dep.size()*0.5)+(1*hset.size());
		}
			}	
			}
	//System.out.println(count_feature+" "+count);
	cov = (float) count_feature/count;
	tot_cov.add(cov);
	Double coverage = Collections.max(tot_cov);
	
	return coverage;

		
}
	
	public LinkedHashMap<String, Double> mt_coverage() throws Exception
	{
		LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<>();
		for(String mtl : scriptcontents) {
			if(scriptRoot.resolve(mtl).toFile().exists()) {
				EtlModule module = new EtlModule();
				module.parse(scriptRoot.resolve(mtl));
				module.getContext().setModule(module);
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				//System.out.println(module);
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}
			
				staticAnalyser.validate(module);
			
				List<ModelDeclaration> mm = ((EtlModule) module).getDeclaredModelDeclarations();
				//System.out.println(mm);
				String sourceMetamodel = mm.get(0).getModel().getName();
				String targetMetamodel = mm.get(1).getModel().getName();
			
			
				//System.out.println(sourceMetamodel+" "+targetMetamodel);
				
				linkedHashMap.put(mtl, calculateMTCoverage_new1(metamodelPath + "/" + sourceMetamodel+".ecore", metamodelPath + "/" + targetMetamodel+".ecore"));
				//linkedHashMap.put(mtl, calculateMTCoverage_new(metamodelPath + "/" + sourceMetamodel+".ecore", metamodelPath + "/" + targetMetamodel+".ecore"));
		}
		}
		
		File file = new File(outputFilePath);
        BufferedWriter bf = null;
        
        try {
        	  
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));
  
            // iterate map entries
            for (Map.Entry<String, Double> entry :
            	linkedHashMap.entrySet()) {
  
                // put key and value separated by a colon
                bf.write(entry.getKey() + ":"
                         + entry.getValue());
  
                // new line
                bf.newLine();
            }
  
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
  
            try {
  
                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }
        
		return linkedHashMap;
		
	}
	
	public ArrayList<Double> calculateMTCoverage_opt(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
	//EtlChainOptimiser.calculateTransformationCoverageOnOptimizedTransformation_opt();
	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
	{
		if(identifyETL(sourceMM,targetMM).get(e).startsWith("Optimized1_")) {
		EtlModule module1 = new EtlModule();
		
		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		System.out.println("\n"+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
	
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
	ArrayList<String> src_dep = srcDependency2(module1);
	
	staticAnalyser.validate(module1);
	EolModelElementType type = null, type1 = null;
	double coverage = 0;
	
	int src_no_of_features=0;
	double cov=0;
	int count_feature=0;
	double count=0;
	int c=0;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
			
			
			c++;
		
			count_feature = src_dep.size()+c;
			
		
			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
			
				System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());

		System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
				+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
				
		String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
		String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asInteger()+EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
//				System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
				
			}	
		}
		
			cov = (float) count_feature/count;
			tot_cov.add(cov);
			
	}
	}
	

	return tot_cov;

		
}
	
	
	public void countinstancesMM() throws Exception {
		
		String MM = "metamodels/KM3.ecore";
		String m = "models/sample-km3.xmi";
		
		EolRunConfiguration x = executeEOL(m, MM, Paths.get("size.eol"));
		System.out.println(x);
		
	}
	
	public ArrayList<Double> calculateMTCoverage_File(File f)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
//	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
//	{
		EtlModule module1 = new EtlModule();
		
//		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
//		System.out.println("\n"+scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		
		module1.parse(f.getName());
	
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
	ArrayList<String> src_dep = srcDependency2(module1);

	staticAnalyser.validate(module1);
	EolModelElementType type = null, type1 = null;
	double coverage = 0;
	
	int src_no_of_features=0;
	double cov=0;
	int count_feature=0;
	double count=0;
	int c=0;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

			c++;

			count_feature = src_dep.size()+c;

			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
			
				System.out.println("No. of structural features in "+type.getTypeName()+" to "+type1.getTypeName()+" rule is "+src_dep.size());
				
		System.out.println("No. of structural features and metaclasses in source metamodel used in the transformation "
				+type.getModelName()+" to "+type1.getModelName()+" is "+count_feature);
				
		String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
		String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
	
			String code = "(EClass.all.size().asInteger()+EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
			
			}	
		}
			cov = (float) count_feature/count;
			tot_cov.add(cov);
	
	//}
	return tot_cov;
}
	
	public double calculateMTCoverage1(String sourceMM, String targetMM)
			throws Exception {
	
	EtlModule module1 = new EtlModule();
	double mt_coverage=0;
//	for (int i = 0; i < scriptcontents.length; i++) {
//		module1.parse(scriptRoot.resolve(identifyETL1(sourceMM, targetMM)));
//		
//		
//	}
	for(int e=0;e<identifyETL(sourceMM,targetMM).size();e++)
		module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
	mt_coverage=calculateMTCoverage2(module1);
	return mt_coverage;

	}
	
	public int calculateModelEClass(String targetMetamodel, String targetModel) throws Exception {
		EolModelElementType sourceMM, targetMM;
		int i1 = 0;
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";			
			String sourceMM111 = targetMetamodel;			
			String code0 = "var type = EClass.all.println();\r\n";			
			String mmEClass = executeEOL1(sourceMM111, metaMM, code0);
			System.out.println(mmEClass);
			
			String mm = targetMetamodel;			
			String model = targetModel;			
			String code2 = "var type = targetMM.getTypeName().all.println().size();\r\n";			
			String modelEClass = executeEOL1(model, mm, code2);
			System.out.println(modelEClass);
			
			i1 = NumberUtils.toInt(modelEClass.trim());
			
		return i1;
		
	}
	
	public double calculateMTCoverage2(EtlModule module1)
			throws Exception {
	
	double coverage = 0, tot_cov=0;
	//EtlModule module1 = new EtlModule();
	//module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM)));
	
	double total = calculateMTChain12(module1);
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

	
	staticAnalyser.validate(module1);
	EolModelElementType type;
	
		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {	
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
			double count=0;
			double sum1=0,sum2=0, sum3=0;
				
			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
		
			String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
			
			String code2 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
					+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size().asString().println();\r\n}";
			
			
			String code20 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
					+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size().asString().println();\r\n}";
					
//			FileWriter fw21 = new FileWriter(scriptRoot.resolve("newDep2" + ".eol").toString());
//			fw21.write(code2);
//			fw21.close();
//			
//			FileWriter fw210 = new FileWriter(scriptRoot.resolve("newDep20" + ".eol").toString());
//			fw210.write(code20);
//			fw210.close();
			
//			String x2 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep2" + ".eol"));
//			String x20 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep20" + ".eol"));
			
			String x2 = executeEOL1(sourceMM111, metaMM, code2);
			String x20 = executeEOL1(sourceMM111, metaMM, code20);
			
			double i1 = NumberUtils.toDouble(x2.trim());
			double i2 = NumberUtils.toDouble(x20.trim());
			//System.out.println(i1 +" "+i2);
			sum1 = sum1+i1; 
			sum2 = sum2+i2; 
			sum3 = i1+i2;
			
//		System.out.println("No. of attributes in source "+type.getTypeName()+" is "+x2);
//		System.out.println("No. of references in source "+type.getTypeName()+" is "+x20);
			
//			scriptRoot.resolve("newDep2" + ".eol").toFile().delete();
//			scriptRoot.resolve("newDep20" + ".eol").toFile().delete();
		
			String code = "(EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";
			
//				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//				fw11.write(code);
//				fw11.close();
				System.out.println("Total features of source "+type.getTypeName()+" is "+sum3);
//				String x = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				String x = executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total structural feature in "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
				
				coverage = (sum3/count);
				System.out.println("Transformation coverage: "+coverage);
				tot_cov += coverage;
		}
	
	//System.out.println(module1.getModule().getClass());
	System.out.println("Total transforation coverage: "+tot_cov+"\n");
	//tot_cov += coverage;
	
	//return coverage;
	return tot_cov;
		
}
	
	public double calculateChainCoverage(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		int min = 99999;
		float max_cov=0;
		ArrayList<String> index = null;
		ArrayList<String> index1 = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		float[] sum_cov_chain = new float[l.size()];
		float prod=1;
		float sum_cov=1;
		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");
			int total = 0, total1 = 0;
			int sum_source_attributeMM = 0;
			int sum_source_referenceMM = 0;
			int sum_target_attributeMM = 0;
			int sum_target_referenceMM = 0;
			int sum_source_attributeModel = 0;
			int sum_source_referenceModel = 0;
			int sum_target_attributeModel = 0;
			int sum_target_referenceModel = 0;
			
			float cov=0;
			
			float tot1=0, tot2=0;
	
			for (int j = 0; j < l.get(i).size(); j++) {
	
				
				EtlModule module1 = new EtlModule();
				EtlModule module2 = new EtlModule();
				
				if (j + 1 < l.get(i).size()) {
	
					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for(int e=0;e<identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));
					
					if (j + 1 < l.get(i).size()-1)
					{
						for(int e1=0;e1<identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
								metamodelPath + "/" + l.get(i).get(j + 2)).size();e1++)
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j+1),
								metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));
						total1 = calculateMTChain1(module2);
						
					}
						
					total = calculateMTChain(module1);
					
					sum[i] = sum[i] + total;
					
					int sum1 = 0, sum2 = 0,sum3 = 0,sum4 = 0, sum5 = 0,sum6 = 0, sum7 = 0,sum8 = 0;
					EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
					staticAnalyser.validate(module1);
					staticAnalyser.validate(module2);
					EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
						
							
						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
						
						String sourceMM111 = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
						
						String code2 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
								+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size().asString().println();\r\n}";
						
						
						String code20 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
								+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size().asString().println();\r\n}";
								
//						FileWriter fw21 = new FileWriter(scriptRoot.resolve("newDep2" + ".eol").toString());
//						fw21.write(code2);
//						fw21.close();
//						
//						FileWriter fw210 = new FileWriter(scriptRoot.resolve("newDep20" + ".eol").toString());
//						fw210.write(code20);
//						fw210.close();
						
//						String x2 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep2" + ".eol"));
//						String x20 = executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep20" + ".eol"));
						String x2 = executeEOL1(sourceMM111, metaMM, code2);
						String x20 = executeEOL1(sourceMM111, metaMM, code2);
						
						int i1 = NumberUtils.toInt(x2.trim());
						int i2 = NumberUtils.toInt(x20.trim());
						
						sum1=sum1+i1; 
						sum2=sum2+i2; 
	
						
	//					System.out.println("No. of attributes in source "+type.getTypeName()+" is "+x2);
	//					System.out.println("No. of references in source "+type.getTypeName()+" is "+x20);
						
//						scriptRoot.resolve("newDep2" + ".eol").toFile().delete();
//						scriptRoot.resolve("newDep20" + ".eol").toFile().delete();
						
						
						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {
							
							type1 = (EolModelElementType) staticAnalyser
									.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));
	
							
							String code = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var attr = cl.eAllAttributes.name;\r\n"
									+ "for(at in attr){at.println();}\r\n}";
							
							String code0 = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var ref = cl.eAllReferences;\r\n"
									+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";
							
//							FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
//							fw11.write(code);
//							fw11.close();
//							
//							FileWriter fw110 = new FileWriter(scriptRoot.resolve("newDep0" + ".eol").toString());
//							fw110.write(code0);
//							fw110.close();
					
							
							String x = executeEOL1(sourceMM111, metaMM, code);
							String x0 = executeEOL1(sourceMM111, metaMM, code0);
							
							String[] arrOfStr = x.split("\n");
							String[] arrOfStr0 = x0.trim().split("\n");
							
	//						System.out.println("Attribute of source "+type.getTypeName()+": "+x);
	//						System.out.println("Reference of source "+type.getTypeName()+": "+x0);
							
//							scriptRoot.resolve("newDep" + ".eol").toFile().delete();
//							scriptRoot.resolve("newDep0" + ".eol").toFile().delete();
							
							String eolcode = null, eolcode0 = null, eolcode5=null, eolcode50=null;;
							try {
								String ref_MM, ref_metaclass_MM;
								for (String r0 : arrOfStr0)
								{
										ref_MM = r0.trim().substring(0,r0.indexOf(":"));
										//System.out.println(r0.trim().substring(0,r0.indexOf(":")));
										ref_metaclass_MM = r0.substring(r0.indexOf(":")+1,r0.length());
										//System.out.println(r0.substring(r0.indexOf(":")+1,r0.length()));
										eolcode0 = type.getTypeName() +".all."+ref_MM+".println();";
										eolcode50 = type.getTypeName() +".all."+ref_MM+".size().println();";
								}	
								for (String r : arrOfStr)
								{
									eolcode = type.getTypeName() +".all."+r+".println();";
									eolcode5 = type.getTypeName() +".all."+r+".size().println();";
								}
								
//								FileWriter fw51 = new FileWriter(scriptRoot.resolve("newDep5" + ".eol").toString());
//								fw51.write(eolcode5);
//								fw51.close();
//								
//								FileWriter fw510 = new FileWriter(scriptRoot.resolve("newDep50" + ".eol").toString());
//								fw510.write(eolcode50);
//								fw510.close();
								
								String x5 = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode5);
								String x50 = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode50);
								
								int i5 = NumberUtils.toInt(x5.trim());
								int i6 = NumberUtils.toInt(x50.trim());
								
								sum5=sum5+i5; 
								sum6=sum6+i6; 
								
//								FileWriter fw = new FileWriter(scriptRoot.resolve("SourceDependency" + ".eol").toString());
//								fw.write(eolcode);
//								fw.close();
//										
//								FileWriter fw0 = new FileWriter(scriptRoot.resolve("SourceDependency0" + ".eol").toString());
//								fw0.write(eolcode0);
//								fw0.close();
	
								String ex = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode);
								String ex0 = executeEOL1(modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j), eolcode0);
										
								//System.out.println("Source model's attributes: "+ex);
								//System.out.println("Source model's references: "+ex0);
										
//								scriptRoot.resolve("SourceDependency" + ".eol").toFile().delete();
//								scriptRoot.resolve("SourceDependency0" + ".eol").toFile().delete();
//								scriptRoot.resolve("newDep5" + ".eol").toFile().delete();
//								scriptRoot.resolve("newDep50" + ".eol").toFile().delete();
							
							} 
							catch (Exception e) {
								System.out.println("EAttribute not given: "+e);
							}
							
							String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM1111 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
							
							String code3 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size.println();\r\n}";
							
							String code30 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size.println();\r\n}";
									
//							FileWriter fw31 = new FileWriter(scriptRoot.resolve("newDep3" + ".eol").toString());
//							fw31.write(code3);
//							fw31.close();
//							
//							FileWriter fw310 = new FileWriter(scriptRoot.resolve("newDep30" + ".eol").toString());
//							fw310.write(code30);
//							fw310.close();
							
							String x3 = executeEOL1(sourceMM1111, metaMM1, code3);
							String x30 = executeEOL1(sourceMM1111, metaMM1, code30);
							
							int i3 = NumberUtils.toInt(x3.trim());
							int i4 = NumberUtils.toInt(x30.trim());
							sum3=sum3+i3; 
							sum4=sum4+i4; 
							
	//						System.out.println("No. of attributes in target "+type1.getTypeName()+" is "+x3);
	//						System.out.println("No. of references in target "+type1.getTypeName()+" is "+x30);
							
							
//							scriptRoot.resolve("newDep3" + ".eol").toFile().delete();
//							scriptRoot.resolve("newDep30" + ".eol").toFile().delete();
							
						
							String code1 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var attr = cl.eAllAttributes.name;\r\n"
									+ "for(at in attr){at.println();}\r\n}";
							
							String code10 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "var ref = cl.eAllReferences;\r\n"
									+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";
							
//							FileWriter fw111 = new FileWriter(scriptRoot.resolve("newDep1" + ".eol").toString());
//							fw111.write(code1);
//							fw111.close();
//							
//							FileWriter fw1110 = new FileWriter(scriptRoot.resolve("newDep10" + ".eol").toString());
//							fw1110.write(code10);
//							fw1110.close();
							
							
							
							String x1 = executeEOL1(sourceMM1111, metaMM1, code1);
							String x10 = executeEOL1(sourceMM1111, metaMM1, code10);
							
							String[] arrOfStr1 = x1.split("\n");
							String[] arrOfStr10 = x10.trim().split("\n");
							
	//						System.out.println("Attribute of target "+type1.getTypeName()+": "+x1);
	//						System.out.println("Reference of target "+type1.getTypeName()+": "+x10);
//							scriptRoot.resolve("newDep1" + ".eol").toFile().delete();
//							scriptRoot.resolve("newDep10" + ".eol").toFile().delete();
							
							
							String eolcode1 = null, eolcode10 = null, eolcode6=null, eolcode60=null;
							try {
	
								String ref_MM1, ref_metaclass_MM1;
								for (String r10 : arrOfStr10)
								{
										ref_MM1 = r10.trim().substring(0,r10.indexOf(":"));
										System.out.println(r10.trim().substring(0,r10.indexOf(":")));
										ref_metaclass_MM1 = r10.substring(r10.indexOf(":")+1,r10.length());
										System.out.println(r10.substring(r10.indexOf(":")+1,r10.length()));
										eolcode10 = type1.getTypeName() +".all."+ref_MM1+".println();";
										eolcode60 = type1.getTypeName() +".all."+ref_MM1+".size().println();";
								}	
								for (String r1 : arrOfStr1)
								{
									eolcode1 = type1.getTypeName() +".all."+r1+".println();";
									eolcode6 = type1.getTypeName() +".all."+r1+".size().println();";
								}
								
//								FileWriter fw61 = new FileWriter(scriptRoot.resolve("newDep6" + ".eol").toString());
//								fw61.write(eolcode6);
//								fw61.close();
//								
//								FileWriter fw610 = new FileWriter(scriptRoot.resolve("newDep60" + ".eol").toString());
//								fw610.write(eolcode60);
//								fw610.close();
								
								String x6 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode6);
								String x60 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode60);
								
								int i7 = NumberUtils.toInt(x6.trim());
								int i8 = NumberUtils.toInt(x60.trim());
								
								sum7=sum7+i7; 
								sum8=sum8+i8; 
								
//								FileWriter fw1 = new FileWriter(scriptRoot.resolve("TargetDependency" + ".eol").toString());
//								fw1.write(eolcode1);
//								fw1.close();
//									
//								FileWriter fw10 = new FileWriter(scriptRoot.resolve("TargetDependency0" + ".eol").toString());
//								fw10.write(eolcode10);
//								fw10.close();
	
								String ex1 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode1);
								String ex10 = executeEOL1(modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(), metamodelPath + "/" + l.get(i).get(j+1), eolcode10);
									
	//								System.out.println("Target model's attributes: "+ex1);
	//								System.out.println("Target model's references: "+ex10);
									
//								scriptRoot.resolve("TargetDependency" + ".eol").toFile().delete();
//								scriptRoot.resolve("TargetDependency0" + ".eol").toFile().delete();
//								scriptRoot.resolve("newDep6" + ".eol").toFile().delete();
//								scriptRoot.resolve("newDep60" + ".eol").toFile().delete();
	
							
							}
							catch (Exception e) {
	
								System.out.println("New EAttribute not given: "+e);
							}
	
							
	//						System.out.println("gfghf "+sum1+" "+sum2);
	
						String metaMM1x = null;
						
						float sumtot1=0, sumtot2=0;
	
						int sum3x=0, sum4x=0, i3x=0, i4x=0;
						if(j+1 < l.get(i).size()-1)
						{ 
							
							for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) 
							{
								
								
								type_next = (EolModelElementType) staticAnalyser
										.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());
	//							System.out.println("1 "+type_next.getTypeName());
	//							System.out.println("2 "+type1.getTypeName());
								if(type_next.getTypeName().equals(type1.getTypeName()))
								{
									
									metaMM1x = "http://www.eclipse.org/emf/2002/Ecore";
									
									String sourceMM1111x = metamodelsRoot.resolve(type_next.getModelName()+".ecore").toString();
									
									String code3x = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
											+ "for(cl in type) {\r\n"+ "cl.eAllAttributes.size.println();\r\n}";
									
									String code30x = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
											+ "for(cl in type) {\r\n"+ "cl.eAllReferences.size.println();\r\n}";
											
//									FileWriter fw31x = new FileWriter(scriptRoot.resolve("newDep3x" + ".eol").toString());
//									fw31x.write(code3x);
//									fw31x.close();
//									
//									FileWriter fw310x = new FileWriter(scriptRoot.resolve("newDep30x" + ".eol").toString());
//									fw310x.write(code30x);
//									fw310x.close();
									
									String x3x = executeEOL1(sourceMM1111x, metaMM1x, code3x);
									String x30x = executeEOL1(sourceMM1111x, metaMM1x, code30x);
									
									i3x = NumberUtils.toInt(x3x.trim());
									i4x = NumberUtils.toInt(x30x.trim());
	//								System.out.println("cvhgfhf "+i3x);
	//								System.out.println("gfyr "+i4x);
									sum3x = sum3x+i3x; 
									sum4x=sum4x+i4x; 
									
	//								System.out.println("No. of attributes in target "+type1.getTypeName()+" is "+x3x);
	//								System.out.println("No. of references in target "+type1.getTypeName()+" is "+x30x);
									
	
	//								System.out.println("sum "+(sum3x+sum4x));
	//								System.out.println("sum total "+(sum1+sum2));
									
//									scriptRoot.resolve("newDep3x" + ".eol").toFile().delete();
//									scriptRoot.resolve("newDep30x" + ".eol").toFile().delete();
									
									
									tot1=sum3x+sum4x;
									//System.out.println("Tot1 "+tot1);
					
								}
								
			
							}
							
							tot2=sum1+sum2;
							//System.out.println("Tot2 "+tot2);
							sumtot1=sumtot1+tot1;
							sumtot2=sumtot2+tot2;
							cov = sumtot1/sumtot2;
							sum_cov=cov;
	
						}
						else
							sum_cov=1;
					
						}
					
					}
					System.out.println("Tranformation coverage for "+type.getModelName()+" -> "+type1.getModelName()+" is "+sum_cov);
					prod = prod * sum_cov;
					sum_cov_chain[i]=prod;
					
	//				System.out.println("Total attributes in source transformation type "+"in "+type.getModelName()+" metamodel is "+sum1);
	//				System.out.println("Total references in source transformation type "+"in "+type.getModelName()+" metamodel is "+sum2);
	//				System.out.println("Total attributes in target transformation type "+"in "+type1.getModelName()+" metamodel is "+sum3);
	//				System.out.println("Total references in target transformation type "+"in "+type1.getModelName()+" metamodel is "+sum4);
	//				System.out.println("\n");
	//				System.out.println("Total attributes in source transformation type "+"in "+type.getModelName()+" model is "+sum5);
	//				System.out.println("Total references in source transformation type "+"in "+type.getModelName()+" model is "+sum6);
	//				System.out.println("Total attributes in target transformation type "+"in "+type1.getModelName()+" model is "+sum7);
	//				System.out.println("Total references in target transformation type "+"in "+type1.getModelName()+" model is "+sum8);
					
					sum_source_attributeMM = sum_source_attributeMM + sum1;
					sum_source_referenceMM = sum_source_referenceMM + sum2;
					sum_target_attributeMM = sum_target_attributeMM + sum3;
					sum_target_referenceMM = sum_target_referenceMM + sum4;
					
					sum_source_attributeModel = sum_source_attributeModel + sum5;
					sum_source_referenceModel = sum_source_referenceModel + sum6;
					sum_target_attributeModel = sum_target_attributeModel + sum7;
					sum_target_referenceModel = sum_target_referenceModel + sum8;
					
					
					
	//				System.out.println("Total expressions/operators used in the transformation " + l.get(i).get(j)
	//						+ " -> " + l.get(i).get(j + 1) + ": " + total + "\n");
				}
				
			}
			if (sum[i] < min) {
				min = sum[i];
				index = l.get(i);
			}
			if(sum_cov_chain[i]>max_cov)
			{
				max_cov=sum_cov_chain[i];
				index1=l.get(i);
			}
	
			//System.out.println("Tranformation coverage for chain "+ l.get(i)+" is "+prod);
			
	//		System.out.println("Total transformed source structural features in metamodel = "+ (sum_source_attributeMM+sum_source_referenceMM));
	//		System.out.println("Total transformed target structural features in metamodel = "+ (sum_target_attributeMM+sum_target_referenceMM));
	//		System.out.println("Total transformed source structural features in model = "+ (sum_source_attributeModel+sum_source_referenceModel));
	//		System.out.println("Total transformed target structural features in model = "+ (sum_target_attributeModel+sum_target_referenceModel));
	//		System.out.println("\n");
	//		System.out.println("Total expressions/operators used in the chain: " + sum[i]);
	//		System.out.println("---------------------------------------------------------\n");
	
		}
		//System.out.println("\nMT Chain " + index1 + " has maximum transformation coverage of " + max_cov);
	//	System.out.println("\nMT Chain " + index + " has minimum structural features of " + min);
		
		return sum_cov;
	
	}
	
	public int calculateMTChain22(IEolModule module) throws Exception {
		String statementName, expressionName;
		int numberofexpression = 0, totalstatement = 0;
		int totalfeatures = 0, totalstructuratlfeatures = 0;
		List<ModuleElement> opName, expName;
		List<List<ModuleElement>> totexpName = null;
		Statement stName;

		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {

				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {

					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());

				}

			}
			staticAnalyser.validate(module);
			
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());
				
				


				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
					

						
					System.out.println("Transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
							+ type1.getTypeName() + "\n");
					

					try {
						System.out.println("Source Type: "+type.getTypeName());	
						String eolcode = "var type = EClass.all.select(ec|ec.name = \""+ type.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
									+ "for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
							
//							FileWriter fw = new FileWriter(scriptRoot.resolve("Dependency" + ".eol").toString());
//							fw.write(eolcode);
//							fw.close();
							
							String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM = metamodelsRoot.resolve(type.getModelName()+".ecore").toString();
							
							String ex = executeEOL1(sourceMM, metaMM, eolcode);
							
							System.out.println(ex);
							
							
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println(e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println(e);
					}
//					scriptRoot.resolve("Dependency" + ".eol").toFile().delete();
						
						try {
							System.out.println("Target Type: "+type1.getTypeName());
							String eolcode1 = "var type = EClass.all.select(ec|ec.name = \""+ type1.getTypeName()+"\");\r\n"
									+ "for(cl in type) {\r\n"+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
									+ "for(i in ref) {"+"cl.name.print();"+ "\"'s etype \".print();"+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println();\r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";
							
//							FileWriter fw1 = new FileWriter(scriptRoot.resolve("Dependency1" + ".eol").toString());
//							fw1.write(eolcode1);
//							fw1.close();
							
							String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";
							
							String sourceMM1 = metamodelsRoot.resolve(type1.getModelName()+".ecore").toString();
							
							String ex1 = executeEOL1(sourceMM1, metaMM1, eolcode1);
							
							System.out.println(ex1);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							System.out.println(e);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							System.out.println(e);
						}
//						scriptRoot.resolve("Dependency1" + ".eol").toFile().delete();

				}

				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
						.getBody().getBody();
				int c = 0;
				int sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				String exp = null;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					expressionName = expName.toString();
					numberofexpression = expName.size();
					c++;
					System.out.println("Statement number " + c);
					System.out.println(statementName + "\n" + expressionName + "\n");
					System.out.println(statementName.split(" ")[0]);
					for (int l = 0; l < numberofexpression; l++) {
//						exp = expName.get(l).toString();
						System.out.println("\n"+expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();
//								System.out.println(totexpName.get(m).toString().split(" ")[0].substring(1));
								sumofoperation = sumofoperation + totexpSize;
//								System.out.println(totexpSize);
								for(int n=0;n<totexpName.get(m).size();n++)
								{
									System.out.println(totexpName.get(m).get(n));
//									System.out.println(totexpName.get(m).get(n).toString().split(" ")[0]);
								}
									
							}

						}
						
					}
					
					System.out.println("\nNumber of expressions and operations: " + sumofoperation + "\n");
					totalfeatures = totalfeatures + sumofoperation;
				}
				totalstatement = totalstatement + totalfeatures;
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));
					System.out.println("Total expressions/operators used in the transformation rule "
							+ type.getTypeName() + " to " + type1.getTypeName() + ":" + totalstatement + "\n");
				}
				totalstructuratlfeatures = totalstructuratlfeatures + totalstatement;
			}
		}
		return totalstructuratlfeatures;

	}
	
	
	
	public void registerMM(String mm)
	{
		try {
			// register globally the Ecore Resource Factory to the ".ecore" extension
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
			    "*", new EcoreResourceFactoryImpl());

			ResourceSet rs = new ResourceSetImpl();
			// enable extended metadata
			final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
			rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
			URI fileURI = URI.createFileURI(mm);
			Resource r = rs.getResource(fileURI, true);
			EObject eObject = r.getContents().get(0);
			if (eObject instanceof EPackage) {
			    EPackage p = (EPackage)eObject;
			    EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}


}
