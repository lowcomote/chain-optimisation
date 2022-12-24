package org.eclipse.epsilon.etl.chain.optimisation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
//import java.net.URI;
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
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chain_MT;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.chain.selection.EtlPreExecuteConfiguration;
import org.eclipse.epsilon.etl.chain.selection.ModelProperties;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.optimisation.EtlDependencyMapGenerator;
import org.eclipse.epsilon.etl.parse.EtlUnparser;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;
import org.w3c.dom.Node;

/**
 * This example demonstrates applying optimisation on ETL chains
 * 
 * @author Qurat ul ain Ali, Apurvanand Sahay
 */

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

public class EtlChainOptimiser {

	static Path modelsRoot = Paths.get("models");
	static Path scriptRoot = Paths.get("scripts");
	//static Path scriptRoot = Paths.get("C:/Users/sahay/OneDrive/Documents/chain optimization_momot 27032022/org.eclipse.epsilon.etl.chain.optimisation/scripts");
	static Path metamodelsRoot = Paths.get("metamodels");
	static Path genmodelsRoot = Paths.get("models/generatedmodels");

	static File metamodelPath = new File("metamodels");
	static String metamodelscontents[] = metamodelPath.list();
	
	static File scriptPath = new File("scripts");
	static String scriptcontents[] = scriptPath.list();
	static HashMap<String, Boolean> findetl = new HashMap<String, Boolean>();
	static ArrayList<ArrayList<EtlModule>> alloptimizedmodules = new ArrayList<ArrayList<EtlModule>>();
//	static ArrayList<ArrayList<File>> newmodule = new ArrayList<ArrayList<File>>();
	static List<ArrayList<File>> alloptimizedmodules1 = new ArrayList<ArrayList<File>>();
	static ArrayList<ArrayList<String>> alloptimizedmodules2 = new ArrayList<ArrayList<String>>();
	//public static ArrayList<ArrayList<EtlModule>> getoptimizedList() { return alloptimizedmodules; }
	
	final static String outputFilePath_identifyETL = "C:\\Users\\sahay\\git\\repository\\org.eclipse.epsilon.etl.chain.optimisation\\writeETL.txt";
	
	
	static String filePath = "C:\\Users\\sahay\\git\\repository\\org.eclipse.epsilon.etl.chain.optimisation\\write.txt";
	//static String filePath1 = "C:/Users/sahay/OneDrive/Documents/chain optimization_momot 27032022/org.eclipse.epsilon.etl.chain.optimisation/write_complexity.txt";

// String targetModel = genmodelsRoot.resolve("DB.xmi").toAbsolutePath().toUri().toString();
	
//	static String sourceMM1 = metamodelsRoot.resolve("Tree.ecore").toString();
//	static String targetMM1 = metamodelsRoot.resolve("DB.ecore").toString();
//	static String sourceModel1 = modelsRoot.resolve("Tree2.xmi").toString();
//	static String targetModel1 = modelsRoot.resolve("DB.xmi").toString();
	
	static String sourceMM1 = metamodelsRoot.resolve("KM3.ecore").toString();
	static String targetMM1 = metamodelsRoot.resolve("XML.ecore").toString();
	static String sourceModel1 = modelsRoot.resolve("sample-km3.xmi").toString();
	static String targetModel1 = modelsRoot.resolve("sample-xml.xmi").toString();
	
//	static String sourceMM1 = metamodelsRoot.resolve("Java.ecore").toString();
//	static String targetMM1 = metamodelsRoot.resolve("Graph1.ecore").toString();
//	static String sourceModel1 = modelsRoot.resolve("eclipseModel-0.5.xmi").toString();
//	static String targetModel1 = modelsRoot.resolve("sample-graph1.xmi").toString();
	
//	static String sourceMM1 = metamodelsRoot.resolve("ABC.ecore").toString();
//	static String targetMM1 = metamodelsRoot.resolve("GH.ecore").toString();
//	static String sourceModel1 = modelsRoot.resolve("abc1.xmi").toString();
//	static String targetModel1 = genmodelsRoot.resolve("gh.model").toString();
	
//	static String sourceMM = metamodelsRoot.resolve("ABC.ecore").toString();
//	static String targetMM = metamodelsRoot.resolve("GH.ecore").toString();
//	static String sourceModel = modelsRoot.resolve("abc1.xmi").toAbsolutePath().toUri().toString();
//	static String targetModel = genmodelsRoot.resolve("gh.model").toAbsolutePath().toUri().toString();
	
//	static String sourceMM = metamodelsRoot.resolve("Java.ecore").toString();
//	static String targetMM = metamodelsRoot.resolve("Graph1.ecore").toString();
//	static String sourceModel = modelsRoot.resolve("eclipseModel-0.5.xmi").toAbsolutePath().toUri().toString();
//	static String targetModel = genmodelsRoot.resolve("sample-graph1.xmi").toAbsolutePath().toUri().toString();
	
//	static String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
//	static String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
//	static String sourceModel = modelsRoot.resolve("Tree2.xmi").toString();
//	static String targetModel = modelsRoot.resolve("DB1.xmi").toString();
	
	static String sourceMM = metamodelsRoot.resolve("KM3.ecore").toString();
	static String targetMM = metamodelsRoot.resolve("XML.ecore").toString();
	static String sourceModel = modelsRoot.resolve("sample-km3.xmi").toString();
	static String targetModel = modelsRoot.resolve("sample-xml1.xmi").toString();

	static Chaining_MT chainingMt = new Chaining_MT();
	
	static List<String> showChainFromSourceMM = new ArrayList<String>();
	
	static HashMap<String, ArrayList<String>> printadjlist = null;
	static Map<String, String> resultETL = new HashMap<>();
	
	public static <K, V> Map<K, V>
    copyMap(Map<K, V> original)
    {
 
        Map<K, V> second_Map = new HashMap<>();
 
        // Start the iteration and copy the Key and Value
        // for each Map to the other Map.
        for (Map.Entry<K, V> entry : original.entrySet()) {
 
            // using put method to copy one Map to Other
            second_Map.put(entry.getKey(),
                           entry.getValue());
        }
 
        return second_Map;
    }

	static Map<String, Double> second_Map;
	
	final static String outputFilePath = "write.txt";
	final static String outputFilePath2 = "writeComplexity.txt";
	 
	public static void main(String[] args) throws Exception {

		// User input about permutation combination of chain composition and
		// optimisation
		
		System.out.println("\n 0: Execute chains of ETL \n");
		System.out.println("\n 1: Select chain with minimum complexity \n");
		System.out.println("\n 2: Optimise each identified chain\n");
		System.out.println("\n 3: Select chain with minimum complexity -> Optimise \n");
		System.out.println("\n 4: Optimise -> Select chain with minimum complexity \n");
		System.out.println("\n 5: Find transformation rule index to delete unused statements and rewrite the optimized transformation \n");
		System.out.println("\n 6: Calculate transformation coverage of the original transformation chain \n");
		System.out.println("\n 7: Calculate transformation coverage of the maximum of optimized and original transformation chain \n");
		System.out.println("\n 8: Calculate the execution time of the optimized transformation chain \n");
		System.out.println("\n 11: Calculate possible transformations from source metamodel "+sourceMM1+" and print all possible chains \n");
		System.out.println("\n 12: Calculate possible transformations to target metamodel "+targetMM1+"\n");
		System.out.println("\n 13: Calculate and print coverage of all transformations and store it in a file "+"\n");
		System.out.println("\n 14: Print complexity of all transformations and store it in a file "+"\n");
		System.out.println("\n 15: Print coverage of all transformations "+"\n");
		System.out.println("\n 100: Execute direct model transformation from "+sourceMM1+" to "+targetMM1+"\n");
		System.out.println("\n 101: Execute normal chains of ETL \n");
		System.out.println("\n 102: Execute optimized chains of ETL \n");
		System.out.println("\n 1000: Find dependency graph for chain optimization (Rule [DependentRule] [ETL]) \n");
		System.out.println("\n 1002: Execute best normal chain \n");
		System.out.println("\n 1003: Execute best optimized chain \n");
		System.out.println("\n 1111: Create list \n");
		
		System.out.println("\nSelect the type of execution by inputting a number as described above ");

		
        
		Scanner scanner = new Scanner(System.in);
		int number = scanner.nextInt();
		scanner.close();
		
		ArrayList<ArrayList<String>> l;
		switch (number) {
		case 0: 
		//double start = System.currentTimeMillis();
		executechainsofetl(sourceModel1, sourceMM1, targetModel1, targetMM1);
		//System.out.println("Time = "+(System.currentTimeMillis()-start)/1000+" seconds.");
			//chainingMt.executeETL(sourceModel1, sourceMM1, targetModel1, targetMM1);
				break;
		case 1:
			ArrayList<String> bestchain = chainingMt.identifyBestChain(sourceModel, sourceMM, targetModel, targetMM);
//			ArrayList<ArrayList<String>> bestchain = chainingMt.multiple_identifyBestChain(sourceModel, sourceMM, targetModel, targetMM);
//			for(int b=0;b<bestchain.size();b++)
//				System.out.println("Chain: "+b);
			System.out.println("\n"+"Best chain: " + bestchain);
			break;
		case 101: //executenormalchain();
			executenormalchain2();
				break;
		case 102: executeoptimizedchain();
		break;
		case 111:
			ArrayList<String> two1 = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
			List<ArrayList<String>> chain1 = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
			Chain_MT cm1 = new Chain_MT();
			boolean etl11 = cm1.findETLinModels(sourceMM, targetMM);
			//System.out.println(identifyETLinModels(sourceMM, targetMM));
			if (etl11) {
			for(int id=0;id<cm1.identifyETLinModels(sourceMM, targetMM).size();id++) {
				ArrayList<String> x = cm1.identifyETLinModels(sourceMM, targetMM);
				//System.out.println("qwerty: "+x);
				Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
				//break;
				}
			}
			
			if (!two1.isEmpty())
				chain1.add(two1);
			ArrayList<String> samemm=new ArrayList<String>();
			if(chainingMt.findETL(targetMM, targetMM))
			{
				samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
				samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
				chain1.add(samemm);
			}
				
			System.out.println("Chains: "+chain1);
			break;
		case 2:
			optimizeOnly();
			break;
		case 3:
			System.out.println(resultETL);
			optimize();
			break;
		case 4:
			optimizeThenSelect();
			break;
		case 5:
			// operations in etl not considered
			findTransformationRuleIndex();
			//optimize_2();
			for(File f : scriptRoot.toFile().listFiles())
			{
				 if (f.getName().startsWith("Optimized_"))
					 f.delete();
			}
			break;
		case 500:
			// operations in etl not considered
			findTransformationRuleIndex_single();
			
			break;
		case 6:
			// operations in etl not considered
			calculateTransformationCoverage();
			//chainingMt.calculateMTCoverage_new(sourceMM, targetMM);
			break;
		case 7:
			//Thread t = new Thread();
			calculateTransformationCoverageOnOptimizedTransformation(sourceModel, sourceMM, targetModel, targetMM);
//			calculateTransformationCoverage();
			EtlRewritingHandler.calculateTransformationCoverage(sourceModel, sourceMM, targetModel, targetMM);
//			CreateMTCoverage cm = new CreateMTCoverage();
//			cm.calculateTransformationCoverage(sourceModel, sourceMM, targetModel, targetMM);
			for(File f : scriptRoot.toFile().listFiles())
			{
				 if (f.getName().startsWith("Optimized1_"))
					 f.delete();
			}
				
			break;
		case 70:
			EtlRewritingHandler.calculateTransformationCoverage(sourceModel, sourceMM, targetModel, targetMM);
			break;
		case 8: 
			EtlRewritingHandler.calculatetime(sourceModel1, sourceMM1, targetModel1, targetMM1);
			for(File f : scriptRoot.toFile().listFiles())
			{
				 if (f.getName().startsWith("Optimized1_"))
					 f.delete();
			}
			break;
		case 11:
			double start1 = System.currentTimeMillis();
			showChainFromSourceMM = chainingMt.identifyChain_source(sourceMM);
			System.out.println("Chain from source metamodel "+sourceMM1+" is "+showChainFromSourceMM);
			System.out.println("Time = "+(System.currentTimeMillis()-start1)/1000+" seconds.");
			showAllChain(targetMM);
			System.out.println(resultETL);
			break;
		case 12:
			double start12 = System.currentTimeMillis();
			ArrayList<String> chain2 = chainingMt.identifyChain_target(targetMM1);
			System.out.println("Chain to target metamodel "+targetMM1+" is "+chain2);
			System.out.println("Time = "+(System.currentTimeMillis()-start12)/1000+" seconds.");
			break;
		case 13:
			double start121 = System.currentTimeMillis();
			LinkedHashMap<String, Double> coveragemt = mt_coverage();
			System.out.println("\nCoverage of all transformations \n");
			for (Map.Entry<String, Double> entry : coveragemt.entrySet()) {
	            System.out.println(entry.getKey() + " : " + entry.getValue());
	        }
			System.out.println("Time = "+(System.currentTimeMillis()-start121)/1000+" seconds.");
			break;
		case 14:
			double start123 = System.currentTimeMillis();
			final HashMap<String, Integer> complexitymt = mt_complexity();
			System.out.println("\nComplexity of all transformations \n");
			for (Map.Entry<String, Integer> entry : complexitymt.entrySet()) {
	            System.out.println(entry.getKey() + " : " + entry.getValue());
	        }
			System.out.println("Time = "+(System.currentTimeMillis()-start123)/1000+" seconds.");
			break;
		case 15:
			 LinkedHashMap<String, Double> mapFromFile = HashMapFromTextFile();
			 System.out.println(mapFromFile);
			 for (Map.Entry<String, Double> entry : mapFromFile.entrySet()) {
		            System.out.println(entry.getKey() + " : " + entry.getValue());
		        }
			 break;
		case 100: 
//			for(int i=0;i<metamodelscontents.length;i++) {
//				chainingMt.registerMM(metamodelscontents[i]);
//			}
			
			chainingMt.registerMM(sourceMM1);
			chainingMt.registerMM(targetMM1);
			chainingMt.executeETL(sourceModel1, sourceMM1, targetModel1, targetMM1);
			break;
		case 700 :
			System.out.println("All MT coverage: "+chainingMt.allmtcoverage());
			break;
		case 1000: optimize_equivalent();
		for(File f : scriptRoot.toFile().listFiles())
		{
			 if (f.getName().startsWith("Optimized_"))
				 f.delete();
		}
		break;
		case 1001: optimize_equivalent_all();
		break;
		case 1002: 
			double time1=0;
			FileWriter fw = new FileWriter("C:\\Users\\sahay\\OneDrive\\Desktop\\normaltime_"+sourceModel.substring(11)+".txt", true);
			for(int i=0;i<5;i++) {
				double ntime = executebestchain(sourceModel, sourceMM, targetModel, targetMM);
				time1+=ntime;
				System.out.println("xbckjdbvkjdfbkjdfnbkdjfn "+String.valueOf(ntime));
				fw.write(String.valueOf(ntime)+"\t");
			}
			fw.close();
			System.out.println("Total normal time: "+time1);
				
		break;
		case 1003: 
			double time2=0;
			FileWriter fw1 = new FileWriter("C:\\Users\\sahay\\OneDrive\\Desktop\\opttime_"+sourceModel.substring(11)+".txt", true);
			for(int i=0;i<5;i++) {
				double ntime1 = executebestchain_opt(sourceModel, sourceMM, targetModel, targetMM);
				time2+=ntime1;
				fw1.write(String.valueOf(ntime1)+"\t");
			}
			fw1.close();
			System.out.println("Total optimized time: "+time2);	
		break;
		case 1005: chainingMt.countinstancesMM();
		break;
		case 1111: 
			showChainFromSourceMM = chainingMt.identifyChain_source(sourceMM);
			System.out.println("Chain from source metamodel "+sourceMM1+" is "+showChainFromSourceMM);
			l=showAllChain(targetMM);
			//chainingMt.createListChain(sourceModel, sourceMM, targetModel, targetMM);
			List<ArrayList<Integer>> indexlist = new ArrayList<ArrayList<Integer>>();
			List<ArrayList<String>> totalmodulelist = new ArrayList<ArrayList<String>>();
			ArrayList<Integer> complexitylist = new ArrayList<Integer>();
			ArrayList<Integer> mt_index = new ArrayList<Integer>();
			HashMap<String,ArrayList<Integer>> map=new HashMap<String,ArrayList<Integer>>();
			HashMap<String,Integer> map_strf=new HashMap<String,Integer>();
			for (int i = 0; i < l.size(); i++) {
				
				ArrayList<String> modulelist = new ArrayList<String>();
				
				int sum = 0;
				int max_index = 400;
				for(int j = 0; j < l.get(i).size();j++) {
					int total = 0;
				    int min_feature = 9999;
				    
					EtlModule module1 = new EtlModule();
					ArrayList<Integer> index = new ArrayList<Integer>();
					if((j+1)<l.get(i).size()) {
//						for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + l.get(i).get(j),
//							metamodelPath + "/" + l.get(i).get(j + 1)).size();e++) {
							module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + l.get(i).get(j),
									metamodelPath + "/" + l.get(i).get(j + 1)).get(0)));
							//if(module1!=null)
								index=chainingMt.createList(module1);
//								if(index.size()<max_index)
//								{
									for(int ind=0;ind<(max_index-index.size());ind++) {
										index.add(0);
									}
//								}
								modulelist.add(module1.getSourceFile().getName());
								total = chainingMt.calculateMTChain1(module1);
					            if(total < min_feature) {
					               min_feature = total;
					            }
					            sum = sum + min_feature;
					            // sum.add(total);
					            // sum[t] = sum[t] + total;
					            System.out.println(module1.getSourceFile().getName()+" "+index.size()+" "+(max_index-index.size()));
					            System.out.println("Total expressions/operators used in the transformation " 
					            		+ module1.getSourceFile().getName() + "= " + min_feature + "\n");
					            mt_index.add(min_feature);
					            map.put(module1.getSourceFile().getName(), index);
					            map_strf.put(module1.getSourceFile().getName(), min_feature);
					           // map.put(module1.getSourceFile().getName(), mt_index);
						//}
					}
						
					//index.add(getlist);
					indexlist.add(index);
					
					}
				
				if(sum>0) {
					complexitylist.add(sum);
						System.out.println("Minimum structural features of the chain: " + sum);
					}
				
					
				//indexlist.add(index);
				totalmodulelist.add(modulelist);

				
			}
			indexlist.removeIf(p -> p.isEmpty());
			totalmodulelist.removeIf(p -> p.isEmpty());
//			List<ArrayList<Integer>> indexlist1 = indexlist.stream().distinct().collect(Collectors.toList());
//			System.out.println("indexlist1" +indexlist1);
			//totalmodulelist = totalmodulelist.stream().distinct().collect(Collectors.toList());
			System.out.println("\n"+totalmodulelist+" has size "+totalmodulelist.size());
			//System.out.println("\n"+indexlist+" has size "+indexlist.size());
			
			System.out.println("\nComplexity list of transformation chain: "+complexitylist);
			
			int in=0;
			System.out.println("\nTransformation chain list used: compare transformation and metamodel elements.");
			//for(ArrayList<Integer> in : indexlist) {
				for(ArrayList<String> tm : totalmodulelist) {
					List<List<Integer>> inl = new ArrayList<List<Integer>>();
					//System.out.println(in+" "+(in+tm.size()));
					//if(in<tm.size()) {
//						inl.addAll(new ArrayList<ArrayList<Integer>>(indexlist.subList(in, in+tm.size())));
						inl.addAll(new ArrayList<ArrayList<Integer>>(indexlist.subList(in, in+tm.size())));
						System.out.println("\n"+inl);
					//if(indexlist.size()>0)
						//indexlist.remove(inl);
						//System.out.println("\nRemaining index: "+indexlist);
					//}
					//if((in+tm.size())<totalmodulelist.size())
						in=in+tm.size();				
				}
				System.out.println("\n");
			//}
				//mt_index=(ArrayList<Integer>) mt_index.stream().distinct().collect(Collectors.toList());
				System.out.println("\n"+mt_index);
				//indexlist=indexlist.stream().distinct().collect(Collectors.toList());
				System.out.println("\n"+indexlist);
				
				//System.out.println("\n"+map);
				 System.out.println("\nIterating Hashmap of vectors...");  
				   for(Map.Entry m : map.entrySet()){    
				    System.out.println(m.getKey()+" "+m.getValue());    
				   }  
				   
				   System.out.println("\nIterating Hashmap of structural features...");  
				   for(Map.Entry m : map_strf.entrySet()){    
				    System.out.println(m.getKey()+" "+m.getValue());    
				   }
			
			Integer[][] arr = new Integer[indexlist.size()][];
			 
	        int i = 0;
	        for (ArrayList<Integer> lt : indexlist) {
	            arr[i++] = lt.toArray(new Integer[lt.size()]);
	        }
	 
	        //System.out.println("\nArray chain List :"+Arrays.deepToString(arr));
		break;
		case 11110: chainingMt.createListChain(sourceModel, sourceMM, targetModel, targetMM);
		break;
		case 1010: showModelEClass();
		break;
		case 1015: //chainingMt.identifyPossibleChain(sourceMM, targetMM);
		
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<String> possibleChain = chainingMt.identifyPossibleChain(sourceMM, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		ArrayList<ArrayList<Integer>> countModelEClass = new ArrayList<ArrayList<Integer>>();
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
			//System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
//		if (!two.isEmpty())
//			possibleChain.add(two);
//		ArrayList<String> samemm=new ArrayList<String>();
//		if(chainingMt.findETL(targetMM, targetMM))
//		{
//			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//			possibleChain.add(samemm);
//		}
//		//chain.stream().distinct().collect(Collectors.toList());
//		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
//		chain_set.addAll(possibleChain);
//		possibleChain.clear();
//		possibleChain.addAll(chain_set);
		possibleChain=possibleChain.stream().distinct().collect(Collectors.toList());
		System.out.println("Possible Chains: "+possibleChain); 
		
		break;
		case 1020 : chainingMt.identifyAllChain(sourceMM, targetMM);
		break;
		case 1021 : showAllChain(targetMM);
		break;
		case 5000 : 
			double start5000 = System.currentTimeMillis();
			deserialize_identifyetl(sourceMM, targetMM);
			System.out.println("Time = "+(System.currentTimeMillis()-start5000)/1000+" seconds.");
		break;
		case 5001 : 
			double start5001 = System.currentTimeMillis();
			System.out.println(chainingMt.identifyETL(sourceMM, targetMM));
			System.out.println("Time = "+(System.currentTimeMillis()-start5001)/1000+" seconds.");
		break;
		case 5002 : System.out.println(HashMap_IdETL());
		break;
		default:
			System.out.println("Invalid number");
		}
//		for(File f : scriptRoot.toFile().listFiles())
//				 if (f.getName().startsWith("Optimized1_"))
//					 f.delete();
	}
	
	static String filename_serialize = "file.ser";
	public static Serialize_IdentifyETL serialize_identifyetl(String mm1, String mm2) throws Exception {
		Serialize_IdentifyETL object = new Serialize_IdentifyETL(sourceMM, targetMM, chainingMt.identifyETL(mm1, mm2));
        
        
        ArrayList<String> idetl = null;
        
     // Serialization 
        try
        {   
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename_serialize);
            ObjectOutputStream out = new ObjectOutputStream(file);
              
            // Method for serialization of object
            out.writeObject(object);
              
            out.close();
            file.close();
              
            System.out.println("Object has been serialized");
  
        }
          
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
		return object;
        
	}
	
	public static ArrayList<String> deserialize_identifyetl(String mm1, String mm2) throws Exception {
		
        
        Serialize_IdentifyETL object1 = null;
        
        ArrayList<String> idetl = null;
		// Deserialization
        try
        {   
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename_serialize);
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            object1 = (Serialize_IdentifyETL)in.readObject();
              
            in.close();
            file.close();
            idetl  = object1.etl;
            System.out.println("Object has been deserialized ");
            System.out.println("sourceMM = " + object1.s);
            System.out.println("targetMM = " + object1.t);
            System.out.println("Etl = "+object1.etl);
        }
          
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
          
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return idetl;
	}
	
//	public static ArrayList<ArrayList<String>> createListChain(String sourceModel, String sourceMM, String targetModel, String targetMM)
//			throws Exception {
////		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
////		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		
//		ArrayList<String> showChainFromSourceMM = chainingMt.identifyChain_source(sourceMM);
//		System.out.println("Chain from source metamodel "+sourceMM+" is "+showChainFromSourceMM);
//		List<ArrayList<String>> l = showAllChain(targetMM);
//		
//		List<ArrayList<String>> indexlist = new ArrayList<ArrayList<String>>();
//		
//				
////		Chain_MT cm = new Chain_MT();
////		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
////		//System.out.println(identifyETLinModels(sourceMM, targetMM));
////		if (etl1) {
////		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
////			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
////			System.out.println("qwerty: "+x);
////			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
////			//break;
////		}
////		}
////		if (!two.isEmpty())
////			l.add(two);
//		System.out.println("\nChains: "+l);
//		String getlist = null;
//		
//		for (int i = 0; i < l.size(); i++) {
//			
//			for(int j = 0; j < l.get(i).size();j++) {
//				EtlModule module1 = new EtlModule();
//				ArrayList<String> index = new ArrayList<String>();
//				if((j+1)<l.get(i).size()) {
//					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + l.get(i).get(j),
//						metamodelPath + "/" + l.get(i).get(j + 1)).size();e++) {
//						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + l.get(i).get(j),
//								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));
//						//if(module1!=null)
//							index=chainingMt.createList(module1);
//					}
//				}
//				//index.add(getlist);
//				indexlist.add(index);
//			}
//			
//		}
//		indexlist.removeIf(p -> p.isEmpty());
//		indexlist = indexlist.stream().distinct().collect(Collectors.toList());
//		System.out.println(indexlist);
//		return (ArrayList<ArrayList<String>>) indexlist;
//	}
	
	final static ArrayList<ArrayList<Integer>> chainlist = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<Integer>[] adjList;
	public static ArrayList<ArrayList<String>> showAllChain(String targetMM) throws Exception {
		
		List<ArrayList<String>> newmodelsuse = new ArrayList<ArrayList<String>>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		ArrayList<Node> nodelist = new ArrayList<Node>();
		showChainFromSourceMM = showChainFromSourceMM.stream().distinct().collect(Collectors.toList());
		System.out.println(showChainFromSourceMM);

		Graph g = new Graph(showChainFromSourceMM);
		
		adjList = new ArrayList[showChainFromSourceMM.size()];
	      for (int i = 0; i < showChainFromSourceMM.size(); i++) {
	          adjList[i] = new ArrayList<>();
	      }
				
	      int max = 0;
		for(int i=0;i<showChainFromSourceMM.size();i++) {
			
			for(int j=1;j<showChainFromSourceMM.size();j++) {
				//if(!(metamodelsRoot.resolve(showChainFromSourceMM.get(i)).toString()).equals(targetMM)) {
				if(chainingMt.findETL(metamodelsRoot.resolve(showChainFromSourceMM.get(i)).toString(), 
						metamodelsRoot.resolve(showChainFromSourceMM.get(j)).toString())) {
					modelsuse1.add(showChainFromSourceMM.get(i));
					modelsuse1.add(showChainFromSourceMM.get(j));
					// add i and j in a graph to find all possible path from sourceMM
//					Node a = new Node(showChainFromSourceMM.get(i));
//					Node b = new Node(showChainFromSourceMM.get(j));
//					nodelist.add(a);
//					nodelist.add(b);
					adjList[i].add(i);
					adjList[i].add(j);
					System.out.println(showChainFromSourceMM.get(i)+" "+showChainFromSourceMM.get(j));
					//System.out.println(a+" "+b);
					g.addEdge(showChainFromSourceMM.get(i), showChainFromSourceMM.get(j));
					if(j>max && metamodelsRoot.resolve(showChainFromSourceMM.get(j)).toString().equals(targetMM))
						max=j;
					
				}
				//}
			}		
		}
		System.out.println("\n");
//		HashMap<String, ArrayList<String>> printadjlist = null;
		printadjlist = g.printAdjList();
		//System.out.println(targetMM.substring(11));
		//if(!printadjlist.containsKey(targetMM.substring(11)))
		//printadjlist.remove(targetMM.substring(11));
		//System.out.println(printadjlist);
		//System.out.println(modelsuse1+" has size "+modelsuse1.size());
		//getChains(sourceMM.substring(11), printadjlist);
//		int count = 0;
		
		for(Map.Entry<String, ArrayList<String>> m : printadjlist.entrySet()) {
			for(String st : m.getValue()) {
				resultETL.put(m.getKey()+" "+st, chainingMt.identifyETL(metamodelPath+"/"+m.getKey(), metamodelPath+"/"+st).get(0));
			}
			
		}
		
		File file = new File(outputFilePath_identifyETL);
		  
        BufferedWriter bf = null;
  
        try {
  
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));
  
            // iterate map entries
            for (Map.Entry<String, String> entry :
            	resultETL.entrySet()) {
  
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
		System.out.println("\n");
		
		for(int i=0;i<adjList.length;i++) {
			for (int j = 0; j < adjList[i].size(); j++) {
				if(adjList[i].get(j)!=null) {
					System.out.print(adjList[i].get(j)+ " ");
					
				}					
				else 
					continue;
			}
			System.out.println(" ");
		}
		System.out.println("Max="+max);
		int source = 0;
	    //int dest = 8;
	    int dest = max;

	      System.out.println("All possible paths from " + source + " to " + dest + " are:");
	      ArrayList<ArrayList<Integer>> listchain = printAllPaths(source, dest);
	      
	      
	      //System.out.println(listchain);
	      
	      ArrayList<ArrayList<String>> listtotalchain = new ArrayList<ArrayList<String>>();
	      System.out.println("No. of transformation chains are "+listchain.size());
	      System.out.println("All possible paths from "+sourceMM.substring(11)+" to "+targetMM.substring(11)+" are: ");
	      for(int i=0;i<listchain.size();i++) {
	    	  ArrayList<String> newlist = new ArrayList<String>();
	    	  for(int j=0;j<listchain.get(i).size();j++) {
	    		
				newlist.add(showChainFromSourceMM.get(listchain.get(i).get(j)));
				
	    		  //System.out.print(showChainFromSourceMM.get(listchain.get(i).get(j)));
	    	  }
	    	  listtotalchain.add(newlist);
	    	 // System.out.println(" ");
	      }
	      System.out.println(listtotalchain);
		return listtotalchain;
		
	}
	
	
	
	 public static ArrayList<ArrayList<Integer>> printAllPathsHelper(int start, int end, boolean[] isVisited, List<Integer> tempPathList)
	  {
		 ArrayList<Integer> newlist = new ArrayList<Integer>();
//		 chainlist.add((ArrayList<Integer>) tempPathList);
//         System.out.println(chainlist);
		 
	      if (start == end) {
	          System.out.println(tempPathList);   
	          newlist.addAll((ArrayList<Integer>) tempPathList);
	          //return chainlist;
	      }

	      isVisited[start] = true;

	      for (Integer i : adjList[start]) {
	          if (!isVisited[i]) {
	              tempPathList.add(i);
	              printAllPathsHelper(i, end, isVisited, tempPathList);
	              //System.out.println(chainlist);
	              tempPathList.remove(i);//backtracking
	          }
	      }
	      isVisited[start] = false;
	      newlist=(ArrayList<Integer>) newlist.stream().distinct().collect(Collectors.toList());
	     
	      chainlist.add(newlist);
	      chainlist.removeIf(p -> p.isEmpty());
		return chainlist;
	  }

	  public static ArrayList<ArrayList<Integer>> printAllPaths(int source, int dest)
	  {
	      boolean[] isVisited = new boolean[showChainFromSourceMM.size()];
	      ArrayList<Integer> pathList = new ArrayList<>();
	      pathList.add(source);
	      //System.out.println(pathList);
	      ArrayList<ArrayList<Integer>> list = printAllPathsHelper(source, dest, isVisited, pathList);
	      //System.out.println(list);
	      return list;
	  }

	
	public static void optimize_equivalent() throws Exception {
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();
	//	EtlDependencyMapGenerator mapgen = new EtlDependencyMapGenerator();
		ArrayList<String> bestchain = chainingMt.identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		//HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
		ArrayList<ArrayList<String>> listOfHashMap = new ArrayList<ArrayList<String>>();
		//ArrayList<String> delrule_list;
		ArrayList<String> delrule_list = new ArrayList<String>(); 
		ArrayList<String> keeprule_list = new ArrayList<String>();
		if(chainingMt.findETL(targetMM, targetMM)) {
			delrule_list = chainingMt.deletetrindex2_single(targetModel, targetMM, targetModel, targetMM);
			//keeprule_list = chainingMt.keeptrindex2_single(targetModel, targetMM, targetModel, targetMM);
		}	
		else {
			delrule_list = chainingMt.deletetrindex2_single(sourceModel, sourceMM, targetModel, targetMM);
			//keeprule_list = chainingMt.keeptrindex2_single(sourceModel, sourceMM, targetModel, targetMM);
		}
			
		

		for (int i = 0; i < bestchain.size(); i++) {
			EtlModule module1 = new EtlModule();

			if (i + 1 < bestchain.size()) {

//				chainingMt.registerMM(metamodelsRoot.resolve(bestchain.get(i)).toString());
//				chainingMt.registerMM(metamodelsRoot.resolve(bestchain.get(i+1)).toString());
				
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				module1.parse(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + bestchain.get(i),
						metamodelPath + "/" + (bestchain.get(i + 1)))));
				EmfUtil.register(URI.createFileURI(metamodelsRoot.resolve(bestchain.get(i)).toString()),
						EPackage.Registry.INSTANCE);
				EmfUtil.register(URI.createFileURI(metamodelsRoot.resolve(bestchain.get(i+1)).toString()),
						EPackage.Registry.INSTANCE);
				
//				EmfUtil.register(URI.createFileURI(modelsRoot.resolve("TM.ecore").toAbsolutePath().toString()),
//						EPackage.Registry.INSTANCE);
//				EmfUtil.register(URI.createFileURI(modelsRoot.resolve("DB.ecore").toAbsolutePath().toString()),
//						EPackage.Registry.INSTANCE);
				for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnalyser.validate(module1);
				//System.out.println(module1);
				// key is the dependor rule while value is the dependent rule i.e. the value class(es) is dependent on execution of key rule
				HashMap<String, ArrayList<String>> hm = new EtlDependencyMapGenerator().optimiseTrace(module1, staticAnalyser);
				//HashMap<String, ArrayList<String>> hm = mapgen.optimiseTrace(module1, staticAnalyser);
				System.out.println(hm+" "+module1.getSourceFile().getName());
				Collection<String> k = hm.keySet();
				//ArrayList<String> list = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<>();
				for(String keys : k) {
					ArrayList<String> val_list = new ArrayList<String>();
					values = hm.get(keys);
					//val_list.addAll(k);
					
					if(!values.isEmpty()) {	
						val_list.add(keys);
						for(String val : values) {
							System.out.println(val);
								val_list.add(val);
						
						}
						val_list.add(module1.getSourceFile().getName());
						
					}
					
					
					//list.addAll(val_list);
					listOfHashMap.add(val_list);
					listOfHashMap.removeIf(p -> p.isEmpty());
				}
				
			
				//listOfHashMap.add(list);)
				modules.add(module1);
				staticAnalysers.add(staticAnalyser);
				
			}

		}
		Collections.reverse(modules);
		Collections.reverse(staticAnalysers);
		//System.out.println("HashMap: "+hashmap);
		System.out.println("Available list: "+listOfHashMap);
		
		System.out.println("\nDelete index rule in list: "+delrule_list+"\nwith size "+delrule_list.size()+"\n");
		//System.out.println("Keep index rule in list: "+keeprule_list+"\nwith size "+keeprule_list.size()+"\n");
		
		for(int d1=0;d1<listOfHashMap.size();d1++) {
		for(int dep=0;dep<listOfHashMap.get(d1).size();dep++) {
				
	outer:for(int d=0;d<delrule_list.size();d++)
		{		
			//ch++;
			//for(int x=0;x<delrule_list.get(d).size();x++)
			//{
				
				String[] splitKeepIndex = null; 
				
				
				splitKeepIndex = delrule_list.get(d).split("\\s+");
				
				
//				outer:for(int k=0;k<keeprule_list.size();k++) {
//					String[] splitKeepIndex222 = null; 
//					splitKeepIndex222 = keeprule_list.get(k).split("\\s+");
					//dep_list = keeprule_list.get(k).split("\\s+");
					
					
//							String[] dep_list = null;
//							dep_list = listOfHashMap.get(d1).get(dep).split("\\s+");
//							System.out.println(dep_list[6]+" "+listOfHashMap.get(d1).get(0));
							//if(dep_list[6].equals(listOfHashMap.get(d1).get(0)) && dep_list[7].equals(listOfHashMap.get(d1).get(listOfHashMap.get(d1).size()-1))) {
							//if(splitKeepIndex222[6].equals(listOfHashMap.get(d1).get(0))) {
//							for(int k=0;k<keeprule_list.size();k++) {
//								String[] splitKeepIndex222 = null; 
//								splitKeepIndex222 = keeprule_list.get(k).split("\\s+");
							if(dep>0 && dep<(listOfHashMap.get(d1).size()-1)) {
								if(//splitKeepIndex222[6].equals(listOfHashMap.get(d1).get(dep)) &&
										listOfHashMap.get(d1).get(0).equals(splitKeepIndex[6]) 
										&& !(splitKeepIndex[3].equals("EString")) && !(splitKeepIndex[3].equals("EInt"))
										&& !(splitKeepIndex[3].equals("EFloat")) 
										&& splitKeepIndex[7].equals(listOfHashMap.get(d1).get(listOfHashMap.get(d1).size()-1)) 
										//&& splitKeepIndex222[7].equals(listOfHashMap.get(d1).get(listOfHashMap.get(d1).size()-1)) 
										&& d>=0 && d<(delrule_list.size())
										) {
//								if(splitKeepIndex[6].equals(listOfHashMap.get(d1).get(dep)) && !splitKeepIndex[3].equals("EString")
//										&& !splitKeepIndex[3].equals("EInt") && !splitKeepIndex[3].equals("EFloat") 
//										&& splitKeepIndex[7].equals(listOfHashMap.get(d1).get(listOfHashMap.get(d1).size()-1)) && d>=0 && d<(delrule_list.size())) {	
								System.out.println("Remove "+delrule_list.get(d)+" from delete index array.");
									delrule_list.remove(d);
									break outer;
								}
							}
							//}
								//System.out.println(listOfHashMap.get(d1).get(dep));
						//}
							//}
					}
				}
				
				
				//System.out.println(splitKeepIndex[6]);
				//System.out.println(dep_list[1]);
				
			//}
		}
		System.out.println("\nCurrent Delete index rule in list: "+delrule_list+"\n");
		
		ArrayList<EtlModule> deletebinding = deleteBindingsAndRule(delrule_list, bestchain);
		
//		new EtlRewritingHandler().invokeRewriters1(modules, staticAnalysers);
//		for (int k = 0; k < bestchain.size(); k++) {
//			if (k + 1 < bestchain.size()) {
//				Path newsourcemodelpath = modelsRoot.resolve(bestchain.get(k).replaceFirst("[.][^.]+$", "") + ".xmi");
//				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
//
//				Path newtargetmodelpath = modelsRoot
//						.resolve(bestchain.get(k + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
//				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
//
//				chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + bestchain.get(k), newtargetmodel,
//						metamodelPath + "/" + bestchain.get(k + 1));
//			}
//
//		}
	}
	
	public static ArrayList<ArrayList<Integer>> showModelEClass() throws Exception {
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		ArrayList<ArrayList<Integer>> countModelEClass = new ArrayList<ArrayList<Integer>>();
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
			chain.add(two);
		ArrayList<String> samemm=new ArrayList<String>();
		if(chainingMt.findETL(targetMM, targetMM))
		{
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			chain.add(samemm);
		}
		//chain.stream().distinct().collect(Collectors.toList());
		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
		chain_set.addAll(chain);
		chain.clear();
		chain.addAll(chain_set);
		System.out.println("Chains: "+chain); 
		

		for (int i = 0; i < chain.size(); i++) {
			ArrayList<Integer> count = new ArrayList<Integer>();
			for(int j=0;j<chain.get(i).size();j++) {
				
			if(j+1<chain.get(i).size()) {
			EtlModule module1 = new EtlModule();		
				System.out.println(chain.get(i).get(j));
				System.out.println(chain.get(i).get(j+1));
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				module1.parse(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
						metamodelPath + "/" + (chain.get(i).get(j+1)))));
				
				String metamodelname = scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
						metamodelPath + "/" + (chain.get(i).get(j+1)))).toString();
				System.out.println(metamodelname);
				for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnalyser.validate(module1);
				String intermodelpath;
				if(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
						metamodelPath + "/" + (chain.get(i).get(j+1)))).toString()!=targetMM)
					intermodelpath = modelsRoot.resolve(chain.get(i).get(j+1).substring(0, chain.get(i).get(j+1).lastIndexOf('.')) + ".xmi").toString();
				else
					intermodelpath = targetModel;
				
				count.add(chainingMt.calculateModelEClass(metamodelname, intermodelpath));
			}
			}
			countModelEClass.add(count);
			}
		//}
				
		System.out.println(countModelEClass);
		return countModelEClass;
		
	}
	
	public static ArrayList<ArrayList<EtlModule>> findTransformationRuleIndex_single() throws Exception {
		//int delrule=0;
		//ArrayList<Integer> delrule_list;
		ArrayList<ArrayList<File>> newmodule = new ArrayList<ArrayList<File>>();
//		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
//		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		Chain_MT cm = new Chain_MT();
//		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
//		//System.out.println(identifyETLinModels(sourceMM, targetMM));
//		if (etl1) {
//		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
//			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
//			//System.out.println("qwerty: "+x);
//			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
//			//break;
//			}
//		}
//		
//		if (!two.isEmpty())
//			chain.add(two);
//		ArrayList<String> samemm=new ArrayList<String>();
//		if(chainingMt.findETL(targetMM, targetMM))
//		{
//			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//			chain.add(samemm);
//		}
//			
//		System.out.println("Chains: "+chain);
		
		ArrayList<String> bestchain = chainingMt.identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		
		List<EtlModule> listofmodule = new ArrayList<>();
		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = new ArrayList<ArrayList<String>>();
		
			if(chainingMt.findETL(targetMM, targetMM))
				delrule_list = chainingMt.deletetrindex1_single(targetModel, targetMM, targetModel, targetMM);
			else
				delrule_list = chainingMt.deletetrindex1_single(sourceModel, sourceMM, targetModel, targetMM);
			System.out.println("Delete index rule in list: "+delrule_list+"\n");
			
			delrule_list = delrule_list.stream().distinct().collect(Collectors.toList());
			System.out.println("Updated delete index array: "+delrule_list);
			int ch=0;
			for(int d=0;d<delrule_list.size();d++)
			{		
				ch++;
				for(int x=0;x<delrule_list.get(d).size();x++)
				{
					
					String[] splitIndex = null;
					
					splitIndex = delrule_list.get(d).get(x).split("\\s+");
					
					//for(int repeat=0;repeat<2;repeat++) {
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").size();e++)
					{
						EtlModule module1 = new EtlModule();
						EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",
								metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e)));
						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser.validate(module1);
						
						System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "
								+splitIndex[4]+" in transformation "+chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e));
						String tp_target;
						int noofrules=0;
						
						ArrayList<Integer> count_x = new ArrayList<Integer>();
						//System.out.println("cjbvskjvbsdjkvbjdsvsdkjhvbdskj");
//						System.out.println("abbjkaf: "+chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
//										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e)+"  "+splitIndex[7]);
						
						for (TransformationRule tr : module1.getTransformationRules()) {
							int count=0;
							noofrules++;
							String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
							
							for(int tp=0;tp<tr.getTargetParameters().size();tp++)
							{		
								StatementBlock ruleblock = null;
								ruleblock = (StatementBlock) tr.getBody().getBody();
								Statement delete_stLine = null;
								tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
								
								count=ruleblock.getStatements().size();
								
//								if(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
//										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e).equals(splitIndex[7])) {
									
								if(tp_source.equals(splitIndex[4].split("2")[0]) && 
										tp_target.equals(splitIndex[4].split("2")[1]))
								{
									if(!ruleblock.getStatements().isEmpty()) {
										System.out.println("Statements: "+ruleblock.getStatements());
										delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
										if(delete_stLine!=null)
											System.out.println("Delete line "+delete_stLine);
										count--;
										count_x.add(count);
									}
							
								}
								
							//}
							
							
						}
						
						for(int cx=0;cx<count_x.size();cx++)
						{
							System.out.println("Count remaining statements in "+splitIndex[4]+" rule is "+count_x.get(cx));
							if(count_x.get(cx)==0)
							{
								TransformationRule removerules = module1.getTransformationRules().remove(cx);
								System.out.println("Remove rules: "+removerules);
							}	
						}
						
					
//					}
//				}
				try {
					deleteTrans.add(ch+" "+module1.getSourceFile().getName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					e1.getMessage();
				}
				listofmodule.add(module1);
				listofmodule=listofmodule.stream().distinct().collect(Collectors.toList());
				}
					//}
			}
				}
		}
				for(String d:deleteTrans)
					System.out.println("Delete trans "+d);
				int c=0;
				ArrayList<EtlModule> modules = new ArrayList<EtlModule>();
			//for (int i = 0; i < chain.size(); i++) {
				c++;
				int c1=0;
				double totaltime=0;
				//for (int j = 0; j < chain.get(i).size(); j++) {
				for(int j=0;j<bestchain.size();j++) {
					HashMap<EtlRunConfiguration, Double> hash = null;
					double endt = 0;
					c1++;
					EtlModule module2 = new EtlModule();
					
					if (j + 1 < bestchain.size()) {
						
						
											
						for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(j),metamodelPath + "/" + bestchain.get(j+1)).size();e++)
						{
					
							
							module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(j),
									metamodelPath + "/" + (bestchain.get(j + 1))).get(e)));	
							double start1=System.currentTimeMillis();
							EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
									staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
								}
							}
						staticAnlayser2.validate(module2);
						endt = System.currentTimeMillis()-start1;
						
						for(String d:deleteTrans)
						{
						
							if(Integer.parseInt(d.split(" ")[0])==c && !d.equals(c+" "+module2.getSourceFile().getName()))
							{
							
								modules.add(module2);
							
							}
							else if(Integer.parseInt(d.split(" ")[0])==c && d.equals(c+" "+module2.getSourceFile().getName()))
							{
								for(EtlModule list : listofmodule)
								{
									System.out.println(c);
									System.out.println(list.getSourceFile().getName());
									if(d.equals(c+" "+list.getSourceFile().getName()))
									{
										modules.add(list);
									}
										
								}
									
								
							}
							
						}
						
						
						}
					}
					
					
				}

				
			//}
		
			int ct=0;
//			ArrayList<Integer> index = new ArrayList<Integer>();
//			String targetMetamodel;
//			for (int m=0;m<modules.size();m++) {
//				
//				List<ModelDeclaration> mm1, mm2;
//				String sourceMetamodel1 = null, targetMetamodel1 = null, sourceMetamodel2 = null, targetMetamodel2 = null;
//				if(m+1<modules.size()) {
//					mm1 = modules.get(m).getDeclaredModelDeclarations();
//					targetMetamodel1 = mm1.get(1).getModel().getName();
//					sourceMetamodel1 = mm1.get(0).getModel().getName();
//					mm2 = modules.get(m+1).getDeclaredModelDeclarations();
//					sourceMetamodel2 = mm2.get(0).getModel().getName();
//					targetMetamodel2 = mm2.get(1).getModel().getName();
//					
//				//}
//					int start=0;
//					ct++;
//					//System.out.println((metamodelsRoot.resolve(targetMetamodel+".ecore")).toString()+" "+targetMM);
//					if((metamodelsRoot.resolve(targetMetamodel2+".ecore")).toString().equals(targetMM)) {
//						if((metamodelsRoot.resolve(targetMetamodel1+".ecore")).toString().equals((metamodelsRoot.resolve(targetMetamodel2+".ecore")).toString()) &&
//								(metamodelsRoot.resolve(sourceMetamodel1+".ecore")).toString().equals((metamodelsRoot.resolve(sourceMetamodel2+".ecore")).toString())) {
//							if(index.size()>0)
//								index.remove(index.size()-1);
//							else
//								index.add(ct+1);
//						}
//						else
//							index.add(ct);
//						//index = {modules.indexOf(module)};
//					}
//				}
//				else if(!index.isEmpty() && !modules.isEmpty() && m==modules.size()-1 && index.get(index.size()-1)!=modules.size())
//					index.add(modules.size());
//				
//		
//			}
			
			for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------\n");
			System.err.println(new EtlUnparser().unparse(module));
			}
			
			//System.out.println(index);
//			int start=0;
//			for(int k=0;k<index.size();k++) {	
//				ArrayList<EtlModule> x=null;
//				List<ModelDeclaration> mm1 = modules.get(start).getDeclaredModelDeclarations();
//				String sourceMetamodel = mm1.get(0).getModel().getName();
//				if((metamodelsRoot.resolve(sourceMetamodel+".ecore")).toString().equals(sourceMM)) {
//					x = new ArrayList<EtlModule>(modules.subList(start, index.get(k)));
//					x=(ArrayList<EtlModule>) x.stream().distinct().collect(Collectors.toList());
//					alloptimizedmodules.add(x);
//				}
//				
//				start=index.get(k);
//			}
			
			List<EtlModule> alloptimizedmodules_single = modules.stream().distinct().collect(Collectors.toList());
			
			System.out.println(alloptimizedmodules_single);
			
			ArrayList<EtlModule> newmodule_single = new ArrayList<EtlModule>();
			ArrayList<File> newmodule_single1 = new ArrayList<File>();
			ArrayList<ArrayList<EtlModule>> newmodule0 = new ArrayList<ArrayList<EtlModule>>();
			ArrayList<EtlModule> newlist0 = new ArrayList<EtlModule>();
			ArrayList<ArrayList<URI>> newmodule1 = new ArrayList<ArrayList<URI>>();
			ArrayList<URI> newlist1 = new ArrayList<URI>();
			//for(int a=0;a<alloptimizedmodules.size();a++) {
				ArrayList<File> newlist = new ArrayList<File>();
				for(int b=0;b<alloptimizedmodules_single.size();b++) {
					
					List<ModelDeclaration> mm1 = alloptimizedmodules_single.get(b).getDeclaredModelDeclarations();
					
					newmodule_single1.add(alloptimizedmodules_single.get(b).getSourceFile());
//					newlist0.add(alloptimizedmodules_single.get(b));
//					newlist.add(alloptimizedmodules_single.get(b).getSourceFile());		
					//newlist1.add(alloptimizedmodules.get(a).get(b).getSourceUri());		
					}
			
				System.out.println(newmodule_single1);
//				newmodule0.add(newlist0);
//				newmodule.add(newlist);
//				newmodule1.add(newlist1);
				
				
			//}
//			System.out.println(newmodule0);

			
		//	for (ArrayList<String> modulechains : chain) {
				double totaltime1=0;
				for(int j=0;j<bestchain.size();j++) {
					if(j+1<bestchain.size()) {
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(j),metamodelPath + "/" + bestchain.get(j+1)).size();e++)
					{
							
					EtlModule module = new EtlModule();
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(j),
							metamodelPath + "/" + bestchain.get(j + 1)).get(e)));
					for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					
					staticAnalyser.validate(module);
					HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime1 += hash.get(key);
					}
					}
					}
				}
				System.out.println("Time taken to execute normal transformation chain \n"+bestchain+" is \n"+(totaltime1)+" seconds.\n");
				
			//}
			
			//for (ArrayList<File> modulechain : newmodule) {
				double totaltime2=0;
				for(File modulefile : newmodule_single1) {
					EtlModule module = new EtlModule();
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module.parse(modulefile);
					for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					
					staticAnalyser.validate(module);
					HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime2 += hash.get(key);
					}
				}
				System.out.println("Time taken to execute optimized transformation chain \n"+newmodule_single1+" is \n"+(totaltime2)+" seconds.\n");
				
			//}
			

//			try
//	        { //store it in a file
//	            FileOutputStream fos = new FileOutputStream("listData");
//	            ObjectOutputStream oos = new ObjectOutputStream(fos);
//	            oos.writeObject(newmodule);
//	            FileOutputStream fos1 = new FileOutputStream("listData1");
//	            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
//	            oos1.writeObject(chain);
//	            oos1.close();
//	            fos1.close();
//	            oos.close();
//	            fos.close();
//	        } 
//	        catch (IOException ioe) 
//	        {
//	            ioe.printStackTrace();
//	        }
			return alloptimizedmodules;
							
		}
	
	public static void optimize_equivalent_all() throws Exception {
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		//ArrayList<String> bestchain = chainingMt.identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
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
			chain.add(two);
		ArrayList<String> samemm=new ArrayList<String>();
		if(chainingMt.findETL(targetMM, targetMM))
		{
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			chain.add(samemm);
		}
		//chain.stream().distinct().collect(Collectors.toList());
		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
		chain_set.addAll(chain);
		chain.clear();
		chain.addAll(chain_set);
		System.out.println("Chains: "+chain); 
		

		for (int i = 0; i < chain.size(); i++) {
			for(int j=0;j<chain.get(i).size();j++) {
			EtlModule module1 = new EtlModule();

			if (j + 1 < chain.get(i).size()) {

				chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toString());
				chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j+1)).toString());
				
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				module1.parse(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
						metamodelPath + "/" + (chain.get(i).get(j+1)))));
//				EmfUtil.register(URI.createFileURI(metamodelsRoot.resolve(bestchain.get(i)).toString()),
//						EPackage.Registry.INSTANCE);
//				EmfUtil.register(URI.createFileURI(metamodelsRoot.resolve(bestchain.get(i+1)).toString()),
//						EPackage.Registry.INSTANCE);
				
//				EmfUtil.register(URI.createFileURI(modelsRoot.resolve("TM.ecore").toAbsolutePath().toString()),
//						EPackage.Registry.INSTANCE);
//				EmfUtil.register(URI.createFileURI(modelsRoot.resolve("DB.ecore").toAbsolutePath().toString()),
//						EPackage.Registry.INSTANCE);
				for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnalyser.validate(module1);
				System.out.println(module1);
				new EtlDependencyMapGenerator().optimiseTrace(module1, staticAnalyser);
				modules.add(module1);
				staticAnalysers.add(staticAnalyser);

			}
			}
		}
		Collections.reverse(modules);
		Collections.reverse(staticAnalysers);
		
//		new EtlRewritingHandler().invokeRewriters1(modules, staticAnalysers);
//		for(int i=0;i<chain.size();i++) {
//		for (int k = 0; k < chain.get(i).size(); k++) {
//			if (k + 1 < chain.get(i).size()) {
//				Path newsourcemodelpath = modelsRoot.resolve(chain.get(i).get(k).replaceFirst("[.][^.]+$", "") + ".xmi");
//				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
//
//				Path newtargetmodelpath = modelsRoot
//						.resolve(chain.get(i).get(k + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
//				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
//
//				chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + chain.get(i).get(k), newtargetmodel,
//						metamodelPath + "/" + chain.get(i).get(k + 1));
//			}
//		}
//		}
	}
	
	public static LinkedHashMap<String, Double> HashMapFromTextFile() {

	      final LinkedHashMap<String, Double> map = new LinkedHashMap<>();
	      BufferedReader br = null;

	      try {

	         // create file object
	         final File file = new File(filePath);

	         // create BufferedReader object from the File
	         br = new BufferedReader(new FileReader(file));

	         String line = null;

	         // read file line by line
	         while((line = br.readLine()) != null) {

	            // split the line by :
	            final String[] parts = line.split(":");

	            // first part is name, second is number
	            final String name = parts[0].trim();
	            final String number = parts[1].trim();
	            final double number1 = NumberUtils.toDouble(number);

	            // put name, number in HashMap if they are
	            // not empty
	            if(!name.equals("") && number1 >= 0) {
	               map.put(name, number1);
	            }
	         }
	      } catch(final Exception e) {
	         e.printStackTrace();
	      } finally {

	         // Always close the BufferedReader
	         if(br != null) {
	            try {
	               br.close();
	            } catch(final Exception e) {
	            }
	            ;
	         }
	      }

	      return map;
	   }
	
	public static final LinkedHashMap<String, Double> mt_coverage() throws Exception
	{
		final LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<String, Double>();
		//LinkedHashMap<String, Double> linkedHashMap=null;
		//linkedHashMap = new LinkedHashMap<>();
		long sumtime=0;
		double start121 = System.currentTimeMillis();
//		for(String mm : metamodelscontents) {
//			System.out.println(metamodelsRoot.resolve(mm));
//			chainingMt.registerMM(metamodelsRoot.resolve(mm).toString());
//		}
		for(String mtl : scriptcontents) {
			if(scriptRoot.resolve(mtl).toFile().exists()) {
				EtlModule module = new EtlModule();
				module.parse(scriptRoot.resolve(mtl));
				module.getContext().setModule(module);
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			//	System.out.println(module);
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}
			//	System.out.println(module);
				staticAnalyser.validate(module);
				
				List<ModelDeclaration> mm = ((EtlModule) module).getDeclaredModelDeclarations();
				//System.out.println(mm);
				String sourceMetamodel = mm.get(0).getModel().getName();
				String targetMetamodel = mm.get(1).getModel().getName();
			
			
//				registerMM(metamodelPath + "/" + sourceMetamodel+".ecore");
//				registerMM(metamodelPath + "/" + targetMetamodel+".ecore");
				
				//System.out.println(sourceMetamodel+" "+targetMetamodel);
//				long start = System.currentTimeMillis();
				double cov = chainingMt.calculateMTCoverage_new1(metamodelPath + "/" + sourceMetamodel+".ecore", metamodelPath + "/" + targetMetamodel+".ecore");
				linkedHashMap.put(mtl, cov);
//				sumtime+=(System.currentTimeMillis()-start);
//				System.out.println("time specific in ms:"+sumtime);
		}
		}
		second_Map = copyMap(linkedHashMap);
		 System.out.println(second_Map);
		//linkedHashMap1 = (LinkedHashMap<String, Double>) Collections.unmodifiableMap(linkedHashMap);
		System.out.println("Time taken for adding all elements to hashmap = "+(System.currentTimeMillis()-start121)/1000+" seconds.");
		
		File file = new File(outputFilePath);
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
            for (Map.Entry<String, Double> entry :
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
	
	public static final HashMap<String, Integer> mt_complexity() throws Exception
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
				//System.out.println("static: "+module);
			
				List<ModelDeclaration> mm = ((EtlModule) module).getDeclaredModelDeclarations();
			//	System.out.println(mm);
//				String sourceMetamodel = mm.get(0).getModel().getName();
//				String targetMetamodel = mm.get(1).getModel().getName();
			
			
				//System.out.println(sourceMetamodel+" "+targetMetamodel);
				
				linkedHashMap.put(mtl, chainingMt.calculateMTChain1(module));
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
	
	
	public static double executebestchain_opt(String sourceModel2, String sourceMM2, String targetModel2, String targetMM2) throws Exception
	{
		ArrayList<String> bestchain = new ArrayList<String>();
		bestchain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM).get(0);
		
		System.out.println("Chains: "+bestchain); 
		double totaltime = 0;
		//System.out.println(sourceModel1.substring(sourceModel1.indexOf("\\")+1,sourceModel1.length()));
		for(int j=0;j<bestchain.size();j++)
		{		
			//double start = System.currentTimeMillis();
//			for(int j=0;j<chain.get(i).size();j++)
//			{
//				double start = System.currentTimeMillis();
				Path newsourcemodelpath, newtargetmodelpath;
				String newsourcemodel = null, newtargetmodel = null;
				if(j+1<bestchain.size())
				{
					//System.out.println(chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)));
					for(int ch=0;ch<chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)).size();ch++) {
					
					
//					chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toString());
//					chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j+1)).toString());
					if(chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)).get(ch).startsWith("Optimized1_")) {
						System.out.println(scriptRoot.resolve(chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)).get(ch)));
					if(bestchain.get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\")+1,sourceMM2.length())) && !chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(sourceModel2.substring(sourceModel2.indexOf("\\")+1,sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(bestchain.get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}
					else if(!bestchain.get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\")+1,sourceMM2.length())) && !chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(bestchain.get(j).replaceFirst("[.][^.]+$", "") + ".xmi");
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(bestchain.get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

					}
					
					else if(chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(sourceModel2.substring(sourceModel2.indexOf("\\")+1,sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot.resolve(targetModel2.substring(targetModel2.indexOf("\\")+1,targetModel2.length()));
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}
			
//					chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + chain.get(i).get(j), newtargetmodel,
//								metamodelPath + "/" + chain.get(i).get(j+1));
					HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourcemodel, metamodelPath + "/" + bestchain.get(j), newtargetmodel,
							metamodelPath + "/" +bestchain.get(j+1));
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
					}
				}
				
//					System.out.println("Time taken to execute transformation "+ chain.get(i).get(j)+" -> "+chain.get(i).get(j+1)+ " = "+(System.currentTimeMillis()-start)/1000+" seconds.");
				}
				
			//}
		}
		System.out.println("Time taken to execute optimized transformation chain "+bestchain+" is "+totaltime+" seconds.");
		return totaltime;
	}
	
	public static double executebestchain(String sourceModel2, String sourceMM2, String targetModel2, String targetMM2) throws Exception
	{
		ArrayList<String> bestchain = new ArrayList<String>();
		bestchain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM).get(0);		
		System.out.println("Chains: "+bestchain); 
		double totaltime = 0;
		//System.out.println(sourceModel1.substring(sourceModel1.indexOf("\\")+1,sourceModel1.length()));
		for(int j=0;j<bestchain.size();j++)
		{		
			//double start = System.currentTimeMillis();
//			for(int j=0;j<chain.get(i).size();j++)
//			{
//				double start = System.currentTimeMillis();
				Path newsourcemodelpath, newtargetmodelpath;
				String newsourcemodel = null, newtargetmodel = null;
				if(j+1<bestchain.size())
				{
					//System.out.println(chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)));
					for(int ch=0;ch<chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)).size();ch++) {
					
					
					chainingMt.registerMM(metamodelsRoot.resolve(bestchain.get(j)).toString());
					chainingMt.registerMM(metamodelsRoot.resolve(bestchain.get(j+1)).toString());
					if(!chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)).get(ch).startsWith("Optimized1_")) {
						System.out.println(scriptRoot.resolve(chainingMt.identifyETL(metamodelsRoot+"/"+bestchain.get(j), metamodelsRoot+"/"+bestchain.get(j+1)).get(ch)));
					if(bestchain.get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\")+1,sourceMM2.length())) && !chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(sourceModel2.substring(sourceModel2.indexOf("\\")+1,sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(bestchain.get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}
					else if(!bestchain.get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\")+1,sourceMM2.length())) && !chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(bestchain.get(j).replaceFirst("[.][^.]+$", "") + ".xmi");
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(bestchain.get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

					}
					
					else if(chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(sourceModel2.substring(sourceModel2.indexOf("\\")+1,sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot.resolve(targetModel2.substring(targetModel2.indexOf("\\")+1,targetModel2.length()));
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}
			
//					chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + chain.get(i).get(j), newtargetmodel,
//								metamodelPath + "/" + chain.get(i).get(j+1));
					HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourcemodel, metamodelPath + "/" + bestchain.get(j), newtargetmodel,
							metamodelPath + "/" +bestchain.get(j+1));
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
					}
				}
				
//					System.out.println("Time taken to execute transformation "+ chain.get(i).get(j)+" -> "+chain.get(i).get(j+1)+ " = "+(System.currentTimeMillis()-start)/1000+" seconds.");
				}
				
			//}
				
		}
		System.out.println("Time taken to execute normal transformation chain "+bestchain+" is "+totaltime+" seconds.");
		return totaltime;
	}
	
	
	public static void executechainsofetl(String sourceModel2, String sourceMM2, String targetModel2, String targetMM2) throws Exception
	{
//		chainingMt.registerMM(sourceMM2);
//		chainingMt.registerMM(targetMM2);
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel2, sourceMM2, targetModel2, targetMM2);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel2, sourceMM2, targetModel2, targetMM2);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM2, targetMM2);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
		for(int id=0;id<cm.identifyETLinModels(sourceMM2, targetMM2).size();id++) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM2, targetMM2);
			//System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)));
			//break;
		}
		}
		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm=new ArrayList<String>();
		if(chainingMt.findETL(targetMM2, targetMM2))
		{
			samemm.add(targetMM2.substring(targetMM2.indexOf("\\")+1,targetMM2.length()));
			samemm.add(targetMM2.substring(targetMM2.indexOf("\\")+1,targetMM2.length()));
			chain.add(samemm);
		}
		//chain.stream().distinct().collect(Collectors.toList());
		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
		chain_set.addAll(chain);
		chain.clear();
		chain.addAll(chain_set);
		System.out.println("Chains: "+chain); 
		//System.out.println(sourceModel1.substring(sourceModel1.indexOf("\\")+1,sourceModel1.length()));
		for(int i=0;i<chain.size();i++)
		{
			double totaltime = 0;
			//double start = System.currentTimeMillis();
			for(int j=0;j<chain.get(i).size();j++)
			{
//				double start = System.currentTimeMillis();
				Path newsourcemodelpath, newtargetmodelpath;
				String newsourcemodel = null, newtargetmodel = null;
				if(j+1<chain.get(i).size())
				{
					
//					chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toString());
//					chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j+1)).toString());
					
					if(chain.get(i).get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\")+1,sourceMM2.length())) && !chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(sourceModel2.substring(sourceModel2.indexOf("\\")+1,sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(chain.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}
					else if(!chain.get(i).get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\")+1,sourceMM2.length())) && !chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(chain.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi");
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(chain.get(i).get(j+1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

					}
					
					else if(chainingMt.findETL(sourceMM2, targetMM2))
					{
						newsourcemodelpath = modelsRoot.resolve(sourceModel2.substring(sourceModel2.indexOf("\\")+1,sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot.resolve(targetModel2.substring(targetModel2.indexOf("\\")+1,targetModel2.length()));
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}
			
//					chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + chain.get(i).get(j), newtargetmodel,
//								metamodelPath + "/" + chain.get(i).get(j+1));
					HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourcemodel, metamodelPath + "/" + chain.get(i).get(j), newtargetmodel,
							metamodelPath + "/" + chain.get(i).get(j+1));
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
				
//					System.out.println("Time taken to execute transformation "+ chain.get(i).get(j)+" -> "+chain.get(i).get(j+1)+ " = "+(System.currentTimeMillis()-start)/1000+" seconds.");
				}
				
			}
				System.out.println("Time taken to execute transformation chain "+chain.get(i)+" is "+totaltime+" seconds.");
		}
	}
	public static void calculateTransformationCoverageOnOptimizedTransformation(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception {
		
//		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
//		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
//		Chain_MT cm = new Chain_MT();
//		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
//		//System.out.println(identifyETLinModels(sourceMM, targetMM));
//		if (etl1) {
//		for(int id=0;id<cm.identifyETLinModels(sourceMM, targetMM).size();id++) {
//			ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
//			//System.out.println("qwerty: "+x);
//			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)), StandardCopyOption.REPLACE_EXISTING);
//			//break;
//		}
//		}
//		
//		
//		if (!two.isEmpty())
//			chain.add(two);
//		ArrayList<String> samemm=new ArrayList<String>();
//		if(chainingMt.findETL(targetMM, targetMM))
//		{
//			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//			chain.add(samemm);
//		}
//		System.out.println("Chains: "+chain);
		
		List<ArrayList<String>> chain = createChain(sourceModel, sourceMM, targetModel, targetMM);
		
		//ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		
		
		List<EtlModule> listofmodule = new ArrayList<>();
		
//		for(int i=0;i<chain.size();i++)
//		{
//			for(int j=0;j<chain.get(i).size();j++)
//			{
//				chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toAbsolutePath().toString());
//			}
//		}
		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = null;
		//for (int ni = 0; ni < chain.size(); ni++) {
//		registerMM(metamodelsRoot.resolve(sourceMM).toAbsolutePath().toString());
//		registerMM(metamodelsRoot.resolve(targetMM).toAbsolutePath().toString());
		
		if(chainingMt.findETL(targetMM, targetMM))
			delrule_list = chainingMt.deletetrindex2(targetModel, targetMM, targetModel, targetMM, chain);
		else
			delrule_list = chainingMt.deletetrindex2(sourceModel, sourceMM, targetModel, targetMM, chain);
		
			delrule_list.stream().distinct().collect(Collectors.toList());
			System.out.println("Delete index rule in list: "+delrule_list+"\n");
			
//			int rule_no = 0;
			
			int ch=0;
			
			//ArrayList<EtlModule> listofmodule = new ArrayList<>();
			for(int d=0;d<delrule_list.size();d++)
			{
		
				ch++;
				for(int x=0;x<delrule_list.get(d).size();x++)
				{
//					EtlModule module1 = null;
					
					String[] splitIndex = null;
					
					splitIndex = delrule_list.get(d).get(x).split("\\s+");
									
					//System.out.println(scriptRoot.resolve(splitIndex[5]+".etl"));
					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",
							metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").size();e++)
					{
//						
						
						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",
								metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e)));
//						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
//						staticAnlayser.validate(module1);
						//System.out.println("chain "+ch+" "+module1.getSourceFile().getName());
							
						
//						for (int i = 0; i < ((EtlModule) module1).getTransformationRules().size(); i++) {
//							for (int j = 0; j < ((EtlModule) module1).getTransformationRules().get(i).getTargetParameters()
//									.size(); j++) {
//							rule_no++;
						
						
						System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "
								+splitIndex[4]+" in transformation "+chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]).get(e));
						String tp_target;
						int noofrules=0;
						
						ArrayList<Integer> count_x = new ArrayList<Integer>();
						for (TransformationRule tr : module1.getTransformationRules()) {
							int count=0;
							noofrules++;
							String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
							//System.out.println("Transformation source rules: "+tp_source);
//							//System.out.println(splitIndex[4].split("2")[0]);
							
							//for(int ts=0;ts<module1.getDeclaredTransformationRules().size();ts++)
							
							for(int tp=0;tp<tr.getTargetParameters().size();tp++)
							{	
								StatementBlock ruleblock = null;
								ruleblock = (StatementBlock) tr.getBody().getBody();
								Statement delete_stLine = null;
								tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
								//System.out.println("Transformation target rules: "+tp_target);
								
//								for (int k = 0; k < ruleblock.getStatements().size(); k++) {
//									count++;
//								}
								
								
								count=ruleblock.getStatements().size();
								if(tp_source.equals(splitIndex[4].split("2")[0]) && 
										tp_target.equals(splitIndex[4].split("2")[1]))
								{
									//count--;
									//System.out.println(splitIndex[0]);
									//for(TransformationRule ts : module1.getDeclaredTransformationRules())
									if(!ruleblock.getStatements().isEmpty()) {
										System.out.println("Statements: "+ruleblock.getStatements());
										delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
										System.out.println("Delete line "+delete_stLine);
										count--;
										count_x.add(count);
									}
									
									
								}
								
							}
							
//							if(ruleblock.getStatements().size()==0)
//							{
//								TransformationRule removerules = module1.getTransformationRules().remove(noofrules);
//								System.out.println("Remove rules: "+removerules);
//							}
							
						}
						
						for(int cx=0;cx<count_x.size();cx++)
						{
							System.out.println("Count remaining statements in "+splitIndex[4]+" rule is "+count_x.get(cx));
							if(count_x.get(cx)==0)
							{
								TransformationRule removerules = module1.getTransformationRules().remove(cx);
								System.out.println("Remove rules: "+removerules);
							}	
						}
						
					

				
				try {
					deleteTrans.add(ch+" "+module1.getSourceFile().getName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					e1.getMessage();
				}
					
				
				listofmodule.add(module1);
				listofmodule.stream().distinct().collect(Collectors.toList());
				}
				
				//}
			}
		}
			
			for(String d1:deleteTrans)
				System.out.println("Delete trans "+d1);
			
//			for(EtlModule l : listofmodule)
//			{
//				FileWriter fw = new FileWriter(scriptRoot.resolve("Script" + ".etl").toString());
//				if (new EtlUnparser().unparse(l).startsWith("pre"))
//					fw.write(new EtlUnparser().unparse(l));
//				fw.close();
//			}
			
			
			int c=0;
			List<EtlModule> modules = new ArrayList<>();
			for (int i = 0; i < chain.size(); i++) {
				c++;
				int c1=0;
				
				for (int j = 0; j < chain.get(i).size(); j++) {
					c1++;
					EtlModule module2 = new EtlModule();
					//System.out.println("fsfhdc "+chain.get(i).get(j));
					if (j + 1 < chain.get(i).size()) {
						
//						chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toAbsolutePath().toString());
//						chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j+1)).toAbsolutePath().toString());
						
						EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
//											
						for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),metamodelPath + "/" + chain.get(i).get(j+1)).size();e++)
						{
							
							module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
									metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e)));			
							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
									staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
								}
							}
						staticAnlayser2.validate(module2);
						
//						System.out.println("mm "+c+" "+chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
//									metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e));
						
						//System.out.println(c+" "+module2.getSourceFile().getName());
						
						
						//int[] arr=new int[chain.get(i).size()];
						for(String d:deleteTrans)
						{
							//System.out.println(d+" "+d.equals(c+" "+module2.getSourceFile().getName()));
							if(Integer.parseInt(d.split(" ")[0])==c && !d.equals(c+" "+module2.getSourceFile().getName()))
							{
								modules.add(module2);
								//arr[j]=c1;
								//System.out.println("Transformation "+c1+" at "+module2.getSourceFile().getName()+" in chain "+c);
							}
							else if(Integer.parseInt(d.split(" ")[0])==c && d.equals(c+" "+module2.getSourceFile().getName()))
							{
								for(EtlModule list : listofmodule)
								{
//									System.out.println(c);
//									System.out.println(list.getSourceFile().getName());
									if(d.equals(c+" "+list.getSourceFile().getName()))
										modules.add(list);
								}
									
								
							}
							
						}
						}
						
					}
					
				}
				
			}
		
			//scriptRoot.resolve("Script" + ".etl").toFile().delete();
			//modules.stream().distinct().collect(Collectors.toList());
			
			for (EtlModule module : modules) {
				System.out.println("------------------");
				System.out.println(module.getSourceFile().getName());
				System.out.println("------------------");
				System.err.println(new EtlUnparser().unparse(module));
				
			}
			
			ArrayList<Integer> noomoduleinchain = new ArrayList<Integer>();
			for(int x=0;x<chain.size();x++)
			{
				int noofmodule=0;
				for(int y=0;y<chain.get(x).size();y++)
				{
					noofmodule++;
					
				}
				//for (EtlModule module : modules) {
//					for(int i=0;i<module.getDeclaredModelDeclarations().size();i++)
//						System.out.println(module.getDeclaredModelDeclarations().get(i).getModel());
					//for(int n=0;n<noofmodule;n++)
					noomoduleinchain.add(noofmodule-1);
						//System.out.println(module.getSourceFile().getName());
					
				//}
			}
//			for(Integer no : noomoduleinchain)
//				System.out.println("Number: "+no);
//			for(EtlModule m : modules)
//				System.out.println(m);
			List<ArrayList<EtlModule>> optimizedchain = new ArrayList<ArrayList<EtlModule>>();
			
			ArrayList<EtlModule> mod = new ArrayList<EtlModule>();
			
//			optimizedchain.add(new ArrayList<EtlModule>(modules.subList(0, noomoduleinchain.get(0))));
//			optimizedchain.add(new ArrayList<EtlModule>(modules.subList(noomoduleinchain.get(0),modules.size())));
			
			for(Integer no : noomoduleinchain) {
				for(int m=0;m<modules.size();m=m+no)
				{
						mod = new ArrayList<EtlModule>(modules.subList(m, Math.min(modules.size(), m + no)));
						optimizedchain.add(mod);
					
					
					//continue;
				}
			}
			
		
			ArrayList<String> name = new ArrayList<String>();
		for (int j = 0; j < optimizedchain.size(); j++) {
			//double coverage_chain = 1;
			//System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
			
		for (int i = 0; i < optimizedchain.get(j).size(); i++) {
			
			name.add(optimizedchain.get(j).get(i).getSourceFile().getName());
			name.stream().distinct().collect(Collectors.toList());
			FileWriter fw;
//			for(String nm : name)
//			{
				fw = new FileWriter(scriptRoot.resolve("Optimized_"+optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
				fw.write(new EtlUnparser().unparse(optimizedchain.get(j).get(i)));
				fw.close();
			//}
				File file = null;
				File file1 = null;
				
				try {
					file = new File(scriptRoot.resolve("Optimized_"+optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
					file1 = new File(scriptRoot.resolve("Optimized1_"+optimizedchain.get(j).get(i).getSourceFile().getName()).toString());

					 	FileInputStream fstream = new FileInputStream(file);
					    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));
					    
	
					    String strLine;
					    int countlines=0;
					    //final String strLine1 = br.readLine();
					    while ((strLine = br.readLine()) != null)   {
					    	countlines++;
					    	if(file1.length()==0)
					    	{
					    		if(countlines<=2)
					        	{
					        		bw.write(strLine + ";");
						            //bw.newLine();
					        	}
					        	else
					        		bw.write(strLine);
					        	bw.newLine();
					    	}

					    
					    }
					   
					   // file1.createNewFile();
					    bw.flush();
					    bw.close();
					    br.close();
					    
					    fstream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				}
				}
		
		
		}
		
		for(String nm : name) {
			//System.out.println("Optimized_"+nm);
			scriptRoot.resolve("Optimized_"+nm).toFile().delete();
			//scriptRoot.resolve("Optimized1_"+nm).toFile().delete();
			//scriptRoot.resolve("Optimized1_"+nm).toFile().createNewFile();
			
		}
		//}
//		CreateMTCoverage cm = new CreateMTCoverage();
//		cm.calculateTransformationCoverage(sourceModel, sourceMM, targetModel, targetMM);
//		for(File f : scriptRoot.toFile().listFiles())
//		{
//			 if (f.getName().startsWith("Optimized1_"))
//				 f.delete();
//		}
		
		
	}
	
public static void calculateTransformationCoverageOnOptimizedTransformation_opt() throws Exception {
		
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
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
			chain.add(two);
		System.out.println("Chains: "+chain);
		//ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		List<EtlModule> modules = new ArrayList<>();
		
		
		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = null;
		//for (int i = 0; i < l.size(); i++) {
		
//		registerMM(metamodelsRoot.resolve(sourceMM).toAbsolutePath().toString());
//		registerMM(metamodelsRoot.resolve(targetMM).toAbsolutePath().toString());
		
			delrule_list = chainingMt.deletetrindex(sourceModel, sourceMM, targetModel, targetMM);
			System.out.println("Delete index rule in list: "+delrule_list+"\n");
			
//			int rule_no = 0;
			
			int ch=0;
			
			ArrayList<EtlModule> listofmodule = new ArrayList<>();
			for(int d=0;d<delrule_list.size();d++)
			{
				
				
				ch++;
				for(int x=0;x<delrule_list.get(d).size();x++)
				{
					EtlModule module1 = null;
					
					String[] splitIndex = null;
					
					splitIndex = delrule_list.get(d).get(x).split("\\s+");
									
					//System.out.println(scriptRoot.resolve(splitIndex[5]+".etl"));
					
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").size();e++)
					{
						
						EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
						module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",
								metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e)));
						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser.validate(module1);
						//System.out.println("chain "+ch+" "+module1.getSourceFile().getName());
							
						
//						for (int i = 0; i < ((EtlModule) module1).getTransformationRules().size(); i++) {
//							for (int j = 0; j < ((EtlModule) module1).getTransformationRules().get(i).getTargetParameters()
//									.size(); j++) {
//							rule_no++;
						
						
						System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "
								+splitIndex[4]+" in transformation "+chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e));
						String tp_target;
						int noofrules=0;
						
						ArrayList<Integer> count_x = new ArrayList<Integer>();
						for (TransformationRule tr : module1.getTransformationRules()) {
							int count=0;
							noofrules++;
							String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
							//System.out.println("Transformation source rules: "+tp_source);
//							//System.out.println(splitIndex[4].split("2")[0]);
							
							//for(int ts=0;ts<module1.getDeclaredTransformationRules().size();ts++)
							
							for(int tp=0;tp<tr.getTargetParameters().size();tp++)
							{	
								StatementBlock ruleblock = null;
								ruleblock = (StatementBlock) tr.getBody().getBody();
								Statement delete_stLine = null;
								tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
								//System.out.println("Transformation target rules: "+tp_target);
								
//								for (int k = 0; k < ruleblock.getStatements().size(); k++) {
//									count++;
//								}
								
								
								count=ruleblock.getStatements().size();
								if(tp_source.equals(splitIndex[4].split("2")[0]) && 
										tp_target.equals(splitIndex[4].split("2")[1]))
								{
									//count--;
									//System.out.println(splitIndex[0]);
									//for(TransformationRule ts : module1.getDeclaredTransformationRules())
									System.out.println("Statements: "+ruleblock.getStatements());
									delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
									System.out.println("Delete line "+delete_stLine);
									count--;
									count_x.add(count);
									
								}
								
							}
							
//							if(ruleblock.getStatements().size()==0)
//							{
//								TransformationRule removerules = module1.getTransformationRules().remove(noofrules);
//								System.out.println("Remove rules: "+removerules);
//							}
							
						}
						
						for(int cx=0;cx<count_x.size();cx++)
						{
							System.out.println("Count remaining statements in "+splitIndex[4]+" rule is "+count_x.get(cx));
							if(count_x.get(cx)==0)
							{
								TransformationRule removerules = module1.getTransformationRules().remove(cx);
								System.out.println("Remove rules: "+removerules);
							}	
						}
						
					

				
				try {
					deleteTrans.add(ch+" "+module1.getSourceFile().getName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					e1.getMessage();
				}
					
					
				listofmodule.add(module1);
				listofmodule.stream().distinct().collect(Collectors.toList());
				}
			}
		}
			
			
			for(String d:deleteTrans)
				System.out.println("Delete trans "+d);
//			for(EtlModule l : listofmodule)
//			{
//				FileWriter fw = new FileWriter(scriptRoot.resolve("Script" + ".etl").toString());
//				if (new EtlUnparser().unparse(l).startsWith("pre"))
//					fw.write(new EtlUnparser().unparse(l));
//				fw.close();
//			}
			
			
			int c=0;
			for (int i = 0; i < chain.size(); i++) {
				c++;
				int c1=0;
				
				for (int j = 0; j < chain.get(i).size(); j++) {
					c1++;
					EtlModule module2 = new EtlModule();
					//System.out.println("fsfhdc "+chain.get(i).get(j));
					if (j + 1 < chain.get(i).size()) {
						
						EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
											
						for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),metamodelPath + "/" + chain.get(i).get(j+1)).size();e++)
						{
							
							module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
									metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e)));			
							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
									staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
								}
							}
						staticAnlayser2.validate(module2);
						
//						System.out.println("mm "+c+" "+chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
//									metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e));
						
						//System.out.println(c+" "+module2.getSourceFile().getName());
						
						
						//int[] arr=new int[chain.get(i).size()];
						for(String d:deleteTrans)
						{
							//System.out.println(d+" "+d.equals(c+" "+module2.getSourceFile().getName()));
							if(Integer.parseInt(d.split(" ")[0])==c && !d.equals(c+" "+module2.getSourceFile().getName()))
							{
								modules.add(module2);
								//arr[j]=c1;
								//System.out.println("Transformation "+c1+" at "+module2.getSourceFile().getName()+" in chain "+c);
							}
							else if(Integer.parseInt(d.split(" ")[0])==c && d.equals(c+" "+module2.getSourceFile().getName()))
							{
								for(EtlModule list : listofmodule)
								{
									if(d.equals(c+" "+list.getSourceFile().getName()))
										modules.add(list);
								}
									
								
							}
							
						}
						}
						
					}
					
				}
				
			}
		
			//scriptRoot.resolve("Script" + ".etl").toFile().delete();
			
//			for (EtlModule module : modules) {
//				System.out.println("------------------");
//				System.out.println(module.getSourceFile().getName());
//				System.out.println("------------------");
//				System.err.println(new EtlUnparser().unparse(module));
//				
//			}
			
			ArrayList<Integer> noomoduleinchain = new ArrayList<Integer>();
			for(int x=0;x<chain.size();x++)
			{
				int noofmodule=0;
				for(int y=0;y<chain.get(x).size();y++)
				{
					noofmodule++;
					
				}
				//for (EtlModule module : modules) {
//					for(int i=0;i<module.getDeclaredModelDeclarations().size();i++)
//						System.out.println(module.getDeclaredModelDeclarations().get(i).getModel());
					//for(int n=0;n<noofmodule;n++)
					noomoduleinchain.add(noofmodule-1);
						//System.out.println(module.getSourceFile().getName());
					
				//}
			}
//			for(Integer no : noomoduleinchain)
//				System.out.println("Number: "+no);
//			for(EtlModule m : modules)
//				System.out.println(m);
			List<ArrayList<EtlModule>> optimizedchain = new ArrayList<ArrayList<EtlModule>>();
			
			ArrayList<EtlModule> mod = new ArrayList<EtlModule>();
			
//			optimizedchain.add(new ArrayList<EtlModule>(modules.subList(0, noomoduleinchain.get(0))));
//			optimizedchain.add(new ArrayList<EtlModule>(modules.subList(noomoduleinchain.get(0),modules.size())));
			
			for(Integer no : noomoduleinchain) {
				for(int m=0;m<modules.size();m=m+no)
				{
						mod = new ArrayList<EtlModule>(modules.subList(m, Math.min(modules.size(), m + no)));
						optimizedchain.add(mod);
					
					
					//continue;
				}
			}
			
		
			ArrayList<String> name = new ArrayList<String>();
		for (int j = 0; j < optimizedchain.size(); j++) {
			//double coverage_chain = 1;
			//System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
			
		for (int i = 0; i < optimizedchain.get(j).size(); i++) {
			
			name.add(optimizedchain.get(j).get(i).getSourceFile().getName());
			name.stream().distinct().collect(Collectors.toList());
			FileWriter fw;
//			for(String nm : name)
//			{
				fw = new FileWriter(scriptRoot.resolve("Optimized_"+optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
				fw.write(new EtlUnparser().unparse(optimizedchain.get(j).get(i)));
				fw.close();
			//}
				File file = null;
				File file1 = null;
				
				try {
					file = new File(scriptRoot.resolve("Optimized_"+optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
					file1 = new File(scriptRoot.resolve("Optimized1_"+optimizedchain.get(j).get(i).getSourceFile().getName()).toString());

					 	FileInputStream fstream = new FileInputStream(file);
					    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));
					    
	
					    String strLine;
					    int countlines=0;
					    //final String strLine1 = br.readLine();
					    while ((strLine = br.readLine()) != null)   {
					    	countlines++;
					    	if(file1.length()==0)
					    	{
					    		if(countlines<=2)
					        	{
					        		bw.write(strLine + ";");
						            //bw.newLine();
					        	}
					        	else
					        		bw.write(strLine);
					        	bw.newLine();
					    	}

					    
					    }
					   
					   // file1.createNewFile();
					    bw.flush();
					    bw.close();
					    br.close();
					    
					    fstream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				}
				}
		
		
		}
		
		for(String nm : name) {
			scriptRoot.resolve("Optimized_"+nm).toFile().delete();
			//scriptRoot.resolve("Optimized1_"+nm).toFile().delete();
			//scriptRoot.resolve("Optimized1_"+nm).toFile().createNewFile();
//			if(scriptRoot.resolve("Optimized_"+nm).toFile().exists())
//				executechainsofetl(sourceModel1, sourceMM1, targetModel1, targetMM1);
		}
		
		
	}
	
public static double calculateTransformationCoverage1(File f) throws Exception {
		
	ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
	List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
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
		chain.add(two);
	System.out.println("Chains: "+chain);
		double coverage_chain = 0;
		for (int j = 0; j < chain.size(); j++) {
			coverage_chain = 1;
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
		for (int i = 0; i < chain.get(j).size(); i++) {
				double max_cov_mt = 0;
				if (i + 1 < chain.get(j).size()) {

					
						for(Double element : chainingMt.calculateMTCoverage_File(f))
						{
							if(element > max_cov_mt) {
					               max_cov_mt = element;
					            }
							System.out.println("\n" + "Individual coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
						               + " is " + element);
						}
						
						System.out.println("\n" + "Maximum coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
					               + " is " + max_cov_mt);
					        coverage_chain *= max_cov_mt;
				}

			}
		System.out.println("\nTotal coverage of chain is " + coverage_chain + "\n");
		}
		return coverage_chain;
	}

	public static double calculateTransformationCoverage() throws Exception {
		
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
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
			chain.add(two);
		ArrayList<String> samemm=new ArrayList<String>();
		if(chainingMt.findETL(targetMM, targetMM))
		{
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			chain.add(samemm);
		}
		System.out.println("Chains: "+chain);
		double coverage_chain = 0;
		for (int j = 0; j < chain.size(); j++) {
			coverage_chain = 1;
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
		for (int i = 0; i < chain.get(j).size(); i++) {
//				EtlModule module1 = new EtlModule();
				double max_cov_mt = 0;
				if (i + 1 < chain.get(j).size()) {

//					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					
//					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)).size();e++)
//					{
//						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
//								metamodelPath + "/" + (chain.get(j).get(i + 1))).get(e)));
//					
//						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
//							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
//							}
//						}
//						staticAnlayser.validate(module1);
						
						for(Double element : chainingMt.calculateMTCoverage(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)))
						{
							if(element > max_cov_mt) {
					               max_cov_mt = element;
					            }
							System.out.println("\n" + "Individual coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
						               + " is " + element);
						}
						
						System.out.println("\n" + "Maximum coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
					               + " is " + max_cov_mt);
					        coverage_chain *= max_cov_mt;
						
					//}
			
				}

			}
		System.out.println("\nTotal coverage of chain is " + coverage_chain + "\n");
		}
		return coverage_chain;
	}
	
	public static ArrayList<EtlModule> deleteBindingsAndRule(ArrayList<String> delrule_list, ArrayList<String> chain) throws Exception {
		
		ArrayList<EtlModule> alloptimizedmodules = new ArrayList<EtlModule>();
		ArrayList<EtlModule> modules = new ArrayList<EtlModule>();
//		delrule_list = (ArrayList<String>) delrule_list.stream().distinct().collect(Collectors.toList());
//		System.out.println("Updated delete index array: "+delrule_list+"\n");
		
		System.out.println("Chains: "+chain);
		
		List<EtlModule> listofmodule = new ArrayList<EtlModule>();
		ArrayList<String> deleteTrans = new ArrayList<String>();
		//List<ArrayList<String>> delrule_list = null, keeprule_list = null;
		
//		int rule_no = 0;
		int ch=0;
//		for(int d=0;d<delrule_list.size();d++)
//		{		
			ch++;
		//	EtlModule module1 = null;
			
			for(int tc=0;tc<chain.size();tc++) {
				
				//System.out.println(chain.get(tc));
				int countdelete=0;
				if(tc+1<chain.size()) {
				for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc)
						,metamodelPath + "/" + chain.get(tc+1)).size();e++) {
					
//					System.out.println(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc)
//					,metamodelPath + "/" + chain.get(tc+1)).get(e)));
					
					EtlModule module1 = new EtlModule();
					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					
					module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc)
					,metamodelPath + "/" + chain.get(tc+1)).get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser.validate(module1);
				
				
			for(int x=0;x<delrule_list.size();x++)
			{
				
				String[] splitIndex = null;
				
				splitIndex = delrule_list.get(x).split("\\s+");
								
				//System.out.println(scriptRoot.resolve(splitIndex[5]+".etl"));
				
				if(chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc)
					,metamodelPath + "/" + chain.get(tc+1)).get(e).equals(splitIndex[7])) {
					
					countdelete++;
					
					
					
				//for(int repeat=0;repeat<2;repeat++) {
//				for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").size();e++)
//				{
//					module1 = new EtlModule();
//					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
//					module1.parse(scriptRoot.resolve(splitIndex[7]));
//					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
//						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
//						}
//					}
//					staticAnlayser.validate(module1);
					
//					for (int i = 0; i < ((EtlModule) module1).getTransformationRules().size(); i++) {
//						for (int j = 0; j < ((EtlModule) module1).getTransformationRules().get(i).getTargetParameters()
//								.size(); j++) {
//						rule_no++;
					
					
					
					System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "
							+splitIndex[6]+" in transformation "+splitIndex[7]);
					String tp_target;
					int noofrules=0;
					
					ArrayList<Integer> count_x = new ArrayList<Integer>();
//					if(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
//							+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e).equals(splitIndex[7])) {
					int noofdeletestatement = 0;
					StatementBlock ruleblock = null;
					for (TransformationRule tr : module1.getDeclaredTransformationRules()) {
						int count=0;
						noofrules++;
						//String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
						//System.out.println("Transformation source rules: "+tp_source);
//						//System.out.println(splitIndex[4].split("2")[0]);
						
						//for(int ts=0;ts<module1.getDeclaredTransformationRules().size();ts++)
						
						ruleblock = (StatementBlock) tr.getBody().getBody();
						
//						for(int tp=0;tp<tr.getTargetParameters().size();tp++)
//						{		
							count=ruleblock.getStatements().size();
							Statement delete_stLine = null;
//							tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
							//System.out.println("Transformation target rules: "+tp_target);
							
//							for (int k = 0; k < ruleblock.getStatements().size(); k++) {
//								count++;
//							}
							
							//System.out.println(tr.getName()+" "+splitIndex[6]);
							if(tr.getName().equals(splitIndex[6])) {
								noofdeletestatement++;
								System.out.println(tr.getName()+" "+splitIndex[6]);
								System.out.println("No. of delete statement in "+splitIndex[6]+" rule in "
								+splitIndex[7]+" transformation is "+noofdeletestatement);
//							if(tp_source.equals(splitIndex[4].split("2")[0]) && 
//									tp_target.equals(splitIndex[4].split("2")[1]))
//							{
								//count--;
								//System.out.println(splitIndex[0]);
								//for(TransformationRule ts : module1.getDeclaredTransformationRules())
								//if(!ruleblock.getStatements().isEmpty()) {
									//System.out.println("Statements: "+ruleblock.getStatements());
									//System.out.println(Integer.parseInt(splitIndex[0]));
									//System.out.println(ruleblock.getStatements());
									//if(Integer.parseInt(splitIndex[0])>0)
									if(Integer.parseInt(splitIndex[0])<=count)
										delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
									//if(delete_stLine!=null) {
										System.out.println("Delete line "+delete_stLine);
										count--;
										count_x.add(count);
									//break;
									//}
								//}
						
							//}
							
						}
						
							
						
					}
					
//					if(ruleblock.getStatements().size()==0)
//					{
//						TransformationRule removerules = null;
//						if(module1.getDeclaredTransformationRules().size()>0)
//							removerules = module1.getTransformationRules().remove(noofrules);
//						System.out.println("Remove rules: "+removerules);
//					}
					
					System.out.println(count_x);
					for(int cx=0;cx<count_x.size();cx++)
					{
						System.out.println("Count remaining statements in "+splitIndex[6]+" rule is "+count_x.get(cx));
						if(count_x.get(cx)==0)
						{
							TransformationRule removerules = module1.getDeclaredTransformationRules().remove(cx);
							System.out.println("Remove rules: "+removerules);
						}	
					}
					
				
//				}
//			}
			try {
				deleteTrans.add(ch+" "+module1.getSourceFile().getName());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				e1.getMessage();
			}
			
			//break;
			//System.out.println("List of modules: "+listofmodule);
			//listofmodule=listofmodule.stream().distinct().collect(Collectors.toList());
			}
				
			}
			listofmodule.add(module1);
			System.out.println("No. of delete statement in transformation "+chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc)
								,metamodelPath + "/" + chain.get(tc+1)).get(e)+ " is "+countdelete);
		}
				
			}
	}
			
			//listofmodule=(ArrayList<EtlModule>) listofmodule.stream().distinct().collect(Collectors.toList());
//			for(String d:deleteTrans)
//				System.out.println("Delete trans "+d);
			int c=0;
			
		//for (int i = 0; i < chain.size(); i++) {
			c++;
			int c1=0;
			double totaltime=0;
			for (int j = 0; j < chain.size(); j++) {
				HashMap<EtlRunConfiguration, Double> hash = null;
				double endt = 0;
				c1++;
				EtlModule module2 = new EtlModule();
				//System.out.println("fsfhdc "+chain.get(i).get(j));
				
				if (j + 1 < chain.size()) {

										
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(j),metamodelPath + "/" + chain.get(j+1)).size();e++)
					{
						//double endt;
						
						
						module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j),
								metamodelPath + "/" + (chain.get(j + 1))).get(e)));	
						//double start1=System.currentTimeMillis();
						EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
						for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
					staticAnlayser2.validate(module2);
					//endt = System.currentTimeMillis()-start1;
					
					for(String d:deleteTrans)
					{
						//System.out.println(d+" "+d.equals(c+" "+module2.getSourceFile().getName()));
						if(Integer.parseInt(d.split(" ")[0])==c && !d.equals(c+" "+module2.getSourceFile().getName()))
						{
							//System.out.println("a: "+module2.getSourceFile().getName());
							//LinkedHashSet<EtlModule> hashSet = new LinkedHashSet<EtlModule>();
							//if (hashSet.add(module2)) modules.add(module2);
							modules.add(module2);
							//arr[j]=c1;
							//System.out.println("Transformation "+c1+" at "+module2.getSourceFile().getName()+" in chain "+c);
						}
						else if(Integer.parseInt(d.split(" ")[0])==c && d.equals(c+" "+module2.getSourceFile().getName()))
						{
							for(EtlModule list : listofmodule)
							{	
								if(d.equals(c+" "+list.getSourceFile().getName()))
								{
									//System.out.println("b: "+list);
									modules.add(list);
								}
									
							}
								
							
						}
						
					}
					
			
					}

				
				}
				
				
			}
			
			modules=(ArrayList<EtlModule>) modules.stream().distinct().collect(Collectors.toList());
			for (EtlModule module : modules) {
				System.out.println("------------------");
				System.out.println(module.getSourceFile().getName());
				System.out.println("------------------\n");
			//	System.err.println(new EtlUnparser().unparse(module));
		
				}	
			
			
//		System.out.println(modules);
		int ct=0;
		ArrayList<Integer> index = new ArrayList<Integer>();
		String targetMetamodel;
		for (int m=0;m<modules.size();m++) {
			//System.out.println(module+"\n");
			
			List<ModelDeclaration> mm1, mm2;
			String sourceMetamodel1 = null, targetMetamodel1 = null, sourceMetamodel2 = null, targetMetamodel2 = null;
			if(m+1<modules.size()) {
				mm1 = modules.get(m).getDeclaredModelDeclarations();
				targetMetamodel1 = mm1.get(1).getModel().getName();
				sourceMetamodel1 = mm1.get(0).getModel().getName();
				mm2 = modules.get(m+1).getDeclaredModelDeclarations();
				targetMetamodel2 = mm2.get(1).getModel().getName();
				sourceMetamodel2 = mm2.get(0).getModel().getName();
			//}
				int start=0;
				ct++;
				//System.out.println((metamodelsRoot.resolve(targetMetamodel+".ecore")).toString()+" "+targetMM);
				if((metamodelsRoot.resolve(targetMetamodel1+".ecore")).toString().equals(targetMM)) {
					if((metamodelsRoot.resolve(targetMetamodel1+".ecore")).toString().equals((metamodelsRoot.resolve(targetMetamodel2+".ecore")).toString()) &&
							(metamodelsRoot.resolve(sourceMetamodel1+".ecore")).toString().equals((metamodelsRoot.resolve(sourceMetamodel2+".ecore")).toString())) {
						if(index.size()>0)
							index.remove(index.size()-1);
						index.add(ct+1);
					}
					else
						index.add(ct);
					//index = {modules.indexOf(module)};
				}
			}
			else if(!index.isEmpty() && !modules.isEmpty() && m==modules.size()-1 && index.get(index.size()-1)!=modules.size())
				index.add(modules.size());
			
			//System.out.println(mm1.get(1).getModel().getName());
			
	
		}
		
		//System.out.println(index);
		int start=0;
		for(int k=0;k<index.size();k++) {	
			ArrayList<EtlModule> x=null;
			List<ModelDeclaration> mm1 = modules.get(start).getDeclaredModelDeclarations();
			String sourceMetamodel = mm1.get(0).getModel().getName();
			if((metamodelsRoot.resolve(sourceMetamodel+".ecore")).toString().equals(sourceMM)) {
				x = new ArrayList<EtlModule>(modules.subList(start, index.get(k)));
				x=(ArrayList<EtlModule>) x.stream().distinct().collect(Collectors.toList());
				alloptimizedmodules.addAll(x);
			}
			start=index.get(k);
		}
		
		ArrayList<File> newmodule = new ArrayList<File>();
		ArrayList<ArrayList<EtlModule>> newmodule0 = new ArrayList<ArrayList<EtlModule>>();
		ArrayList<EtlModule> newlist0 = new ArrayList<EtlModule>();
		ArrayList<ArrayList<URI>> newmodule1 = new ArrayList<ArrayList<URI>>();
		ArrayList<URI> newlist1 = new ArrayList<URI>();
		//System.out.println("Optimized module: "+alloptimizedmodules);
		//for(int a=0;a<alloptimizedmodules.size();a++) {
			ArrayList<File> newlist = new ArrayList<File>();
			for(int b=0;b<listofmodule.size();b++) {
				
				//List<ModelDeclaration> mm1 = alloptimizedmodules.get(a).get(b).getDeclaredModelDeclarations();
				
				newlist0.add(listofmodule.get(b));
				newlist.add(listofmodule.get(b).getSourceFile());	
				//System.out.println("xbkjdbkjdfnvkjsb "+alloptimizedmodules.get(a).get(b).getSourceFile());
				//newlist1.add(alloptimizedmodules.get(a).get(b).getSourceUri());		
				}
			//newlist=(ArrayList<File>) newlist.stream().distinct().collect(Collectors.toList());
			//newlist1=(ArrayList<EtlModule>) newlist1.stream().distinct().collect(Collectors.toList());
			System.out.println(newlist);
			newmodule0.add(newlist0);
			newmodule.addAll(newlist);
			newmodule1.add(newlist1);
			
			
	//	}
		//System.out.println(newmodule0);
		System.out.println("New module: "+newmodule);
		//for(ArrayList<File> newmod : newmodule) {
			for(EtlModule modulefile : listofmodule) {
				EtlModule module = new EtlModule();
				module.parse(modulefile.getFile());
				FileWriter fw1 = new FileWriter(scriptRoot.resolve("Optimized_"+modulefile.getSourceFile().getName()).toString());
				fw1.write(new EtlUnparser().unparse(modulefile));
				fw1.close();
				
				File file = null;
				File file1 = null;
				
				try {
					file = new File(scriptRoot.resolve("Optimized_"+module.getSourceFile().getName()).toString());
					file1 = new File(scriptRoot.resolve("Optimized1_"+module.getSourceFile().getName()).toString());

					 	FileInputStream fstream = new FileInputStream(file);
					    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));
					    
	
					    String strLine;
					    int countlines=0;
					    //final String strLine1 = br.readLine();
					    while ((strLine = br.readLine()) != null)   {
					    	countlines++;
					    	if(file1.length()==0)
					    	{
					    		if(countlines<=2)
					        	{
					        		bw.write(strLine + ";");
						            //bw.newLine();
					        	}
					        	else
					        		bw.write(strLine);
					        	bw.newLine();
					    	}

					    
					    }
					   
					   // file1.createNewFile();
					    bw.flush();
					    bw.close();
					    br.close();
					    
					    fstream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				}
				
			//}
			
		}
		
			double totaltime11=0;
			for(EtlModule modulefile : listofmodule) {
				EtlModule module = new EtlModule();
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				module.parse(modulefile.getFile());
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}
				
				staticAnalyser.validate(module);
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
				for (EtlRunConfiguration key : hash.keySet()) {
					totaltime11 += hash.get(key);
				}
			}
			System.out.println("Time taken to execute optimized transformation chain \n"+listofmodule+" is \n"+(totaltime11)+" seconds.\n");

		//for (ArrayList<String> modulechains : chain) {
			double totaltime1=0;
			for(int j=0;j<chain.size();j++) {
				if(j+1<chain.size()) {
				for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(j),metamodelPath + "/" + chain.get(j+1)).size();e++)
				{
						
				EtlModule module = new EtlModule();
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				module.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j),
						metamodelPath + "/" + chain.get(j + 1)).get(e)));
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}
				
				staticAnalyser.validate(module);
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
				for (EtlRunConfiguration key : hash.keySet()) {
					totaltime1 += hash.get(key);
				}
				}
				}
			}
			System.out.println("Time taken to execute normal transformation chain \n"+chain+" is \n"+(totaltime1)+" seconds.\n");
			
		//}
		
		//for (ArrayList<File> modulechain : newmodule) {
			
			
		//}
		
		//System.out.println("Static analysis time: "+endt/1000+" seconds.");
		
		//System.out.println(newmodule);
		try
        { //store it in a file
            FileOutputStream fos = new FileOutputStream("listData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newmodule);
            FileOutputStream fos1 = new FileOutputStream("listData1");
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(chain);
            oos1.close();
            fos1.close();
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
		return alloptimizedmodules;
		
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

	
	public static ArrayList<ArrayList<EtlModule>> findTransformationRuleIndex() throws Exception {
		//int delrule=0;
		//ArrayList<Integer> delrule_list;
		Map<String, String> idetl = HashMap_IdETL();
		ArrayList<ArrayList<File>> newmodule = new ArrayList<ArrayList<File>>();
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
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
			chain.add(two);
		ArrayList<String> samemm=new ArrayList<String>();
		if(chainingMt.findETL(targetMM, targetMM))
		{
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
			chain.add(samemm);
		}
			
		System.out.println("Chains: "+chain);
		
		List<EtlModule> listofmodule = new ArrayList<>();
		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = null, keeprule_list = null;
		//for (int i = 0; i < l.size(); i++) {
//		for(int i=0;i<chain.size();i++)
//		{
//			for(int j=0;j<chain.get(i).size();j++)
//			{
//				chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toAbsolutePath().toString());
//			}
//		}
		
			if(chainingMt.findETL(targetMM, targetMM)) {
				delrule_list = chainingMt.deletetrindex2(targetModel, targetMM, targetModel, targetMM, chain);
				//keeprule_list = chainingMt.keeptrindex2(targetModel, targetMM, targetModel, targetMM);
			}	
			else {
				delrule_list = chainingMt.deletetrindex2(sourceModel, sourceMM, targetModel, targetMM, chain);
				//keeprule_list = chainingMt.keeptrindex2(sourceModel, sourceMM, targetModel, targetMM);
			}
				
			//System.out.println("\nDelete index rule in list: "+delrule_list+"\n");
			//System.out.println("\nKeep index rule in list: "+keeprule_list+"\n");
			
			
//			int rule_no = 0;
			int ch=0;
			for(int d=0;d<delrule_list.size();d++)
			{		
				ch++;
				for(int x=0;x<delrule_list.get(d).size();x++)
				{
					
					String[] splitIndex = null;
					
					splitIndex = delrule_list.get(d).get(x).split("\\s+");
									
					//System.out.println(scriptRoot.resolve(splitIndex[5]+".etl"));
					
					
					
//					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").size();e++)
//					{
						EtlModule module1 = new EtlModule();
						EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
						
						for (Map.Entry<String,String> me : idetl.entrySet()) {
						
						System.out.println(me.getKey().split(" ")[0].trim().equals(splitIndex[5].split("2")[0].trim()) &&
								me.getKey().split(" ")[0].trim().equals(splitIndex[5].split("2")[0].trim()));
						if(me.getKey().split(" ")[0].trim().equals(splitIndex[5].split("2")[0].trim()) &&
								me.getKey().split(" ")[0].trim().equals(splitIndex[5].split("2")[0].trim())) {
						module1.parse(scriptRoot.resolve(me.getValue()));
//						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",
//								metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(0)));
						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser.validate(module1);
						
//						for (int i = 0; i < ((EtlModule) module1).getTransformationRules().size(); i++) {
//							for (int j = 0; j < ((EtlModule) module1).getTransformationRules().get(i).getTargetParameters()
//									.size(); j++) {
//							rule_no++;
						
						
						
//						System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "
//								+splitIndex[4]+" in transformation "+chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
//										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(0));
						String tp_target;
						int noofrules=0;
						
						ArrayList<Integer> count_x = new ArrayList<Integer>();
						for (TransformationRule tr : module1.getTransformationRules()) {
							int count=0;
							noofrules++;
							String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
							//System.out.println("Transformation source rules: "+tp_source);
//							//System.out.println(splitIndex[4].split("2")[0]);
							
							//for(int ts=0;ts<module1.getDeclaredTransformationRules().size();ts++)
							
							for(int tp=0;tp<tr.getTargetParameters().size();tp++)
							{		
								StatementBlock ruleblock = null;
								ruleblock = (StatementBlock) tr.getBody().getBody();
								Statement delete_stLine = null;
								tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
								//System.out.println("Transformation target rules: "+tp_target);
								
//								for (int k = 0; k < ruleblock.getStatements().size(); k++) {
//									count++;
//								}
								
								
								count=ruleblock.getStatements().size();
								if(tp_source.equals(splitIndex[4].split("2")[0]) && 
										tp_target.equals(splitIndex[4].split("2")[1]))
								{
									//count--;
									//System.out.println(splitIndex[0]);
									//for(TransformationRule ts : module1.getDeclaredTransformationRules())
									if(!ruleblock.getStatements().isEmpty()) {
										//System.out.println("Statements: "+ruleblock.getStatements());
										//System.out.println(Integer.parseInt(splitIndex[0]));
										//System.out.println(ruleblock.getStatements());
										//if(Integer.parseInt(splitIndex[0])>0)
										if(Integer.parseInt(splitIndex[0])<=count)
											delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
										//System.out.println("Delete line "+delete_stLine);
										count--;
										count_x.add(count);
									}
							
								}
								
							}
							
//							if(ruleblock.getStatements().size()==0)
//							{
//								TransformationRule removerules = module1.getTransformationRules().remove(noofrules);
//								System.out.println("Remove rules: "+removerules);
//							}
							
						}
						
						for(int cx=0;cx<count_x.size();cx++)
						{
							//System.out.println("Count remaining statements in "+splitIndex[4]+" rule is "+count_x.get(cx));
							if(count_x.get(cx)==0)
							{
								TransformationRule removerules = module1.getTransformationRules().remove(cx);
								//System.out.println("Remove rules: "+removerules);
							}	
						}
						
					
//					}
//				}
				try {
					deleteTrans.add(ch+" "+module1.getSourceFile().getName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					e1.getMessage();
				}
				listofmodule.add(module1);
				listofmodule=listofmodule.stream().distinct().collect(Collectors.toList());
				}
						}
			}
		}
				//for(String d:deleteTrans)
					//System.out.println("Delete trans "+d);
				int c=0;
				ArrayList<EtlModule> modules = new ArrayList<EtlModule>();
			for (int i = 0; i < chain.size(); i++) {
				c++;
				int c1=0;
				double totaltime=0;
				for (int j = 0; j < chain.get(i).size(); j++) {
					HashMap<EtlRunConfiguration, Double> hash = null;
					double endt = 0;
					c1++;
					EtlModule module2 = new EtlModule();
					//System.out.println("fsfhdc "+chain.get(i).get(j));
					
					if (j + 1 < chain.get(i).size()) {
						
						
											
//						for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),metamodelPath + "/" + chain.get(i).get(j+1)).size();e++)
//						{
							//double endt;
							
							
							module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
									metamodelPath + "/" + (chain.get(i).get(j + 1))).get(0)));	
							double start1=System.currentTimeMillis();
							EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
									staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
								}
							}
						staticAnlayser2.validate(module2);
						endt = System.currentTimeMillis()-start1;
						
//						System.out.println("mm "+c+" "+chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
//									metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e));
						
						//System.out.println(c+" "+module2.getSourceFile().getName());
						
						
						//int[] arr=new int[chain.get(i).size()];
						for(String d:deleteTrans)
						{
							//System.out.println(d+" "+d.equals(c+" "+module2.getSourceFile().getName()));
							if(Integer.parseInt(d.split(" ")[0])==c && !d.equals(c+" "+module2.getSourceFile().getName()))
							{
								//LinkedHashSet<EtlModule> hashSet = new LinkedHashSet<EtlModule>();
								//if (hashSet.add(module2)) modules.add(module2);
								modules.add(module2);
								//arr[j]=c1;
								//System.out.println("Transformation "+c1+" at "+module2.getSourceFile().getName()+" in chain "+c);
							}
							else if(Integer.parseInt(d.split(" ")[0])==c && d.equals(c+" "+module2.getSourceFile().getName()))
							{
								for(EtlModule list : listofmodule)
								{
									//System.out.println(c);
									//System.out.println(list.getSourceFile().getName());
									if(d.equals(c+" "+list.getSourceFile().getName()))
									{
										//LinkedHashSet<EtlModule> hashSet = new LinkedHashSet<EtlModule>();
										//if (hashSet.add(list)) modules.add(list);
										modules.add(list);
									}
										
								}
									
								
							}
							
						}
						
						//String sourcemodel = null, targetmodel = null;
						
						//hash = executeETL_time1(module2);
						
//						if(metamodelsRoot.resolve(chain.get(i).get(j)).toString().equals(sourceMM) && 
//								metamodelsRoot.resolve(chain.get(i).get(j+1)).toString().equals(targetMM)) {
//							sourcemodel = sourceModel;
//							targetmodel = targetModel;
//						}
//						if(metamodelsRoot.resolve(chain.get(i).get(j)).toString().equals(sourceMM) && 
//								!metamodelsRoot.resolve(chain.get(i).get(j+1)).toString().equals(targetMM)) {
//							sourcemodel = sourceModel;
//							targetmodel = chain.get(i).get(j+1).substring(11)+".xmi";
//						}
//						if(!metamodelsRoot.resolve(chain.get(i).get(j)).toString().equals(sourceMM) && 
//								metamodelsRoot.resolve(chain.get(i).get(j+1)).toString().equals(targetMM)) {
//							sourcemodel = chain.get(i).get(j).substring(11)+".xmi";
//							targetmodel = targetModel;
//						}
//						if(metamodelsRoot.resolve(chain.get(i).get(j)).toString().equals(sourceMM) && 
//								metamodelsRoot.resolve(chain.get(i).get(j+1)).toString().equals(targetMM)) {
//							sourcemodel = chain.get(i).get(j).substring(11)+".xmi";
//							targetmodel = chain.get(i).get(j+1).substring(11)+".xmi";
//						}
//							
////						hash = chainingMt.executeETL_time(sourcemodel,metamodelPath + "/" + chain.get(i).get(j),
//													targetmodel,metamodelPath + "/" + chain.get(i).get(j+1));
						
						
						
						//}
						
//						for (EtlRunConfiguration key : hash.keySet()) {
//							System.out.println("Time taken to execute transformation "+chain.get(i).get(j)+" -> "+chain.get(i).get(j+1)+" = "+hash.get(key));
//						}
//						for (EtlRunConfiguration key : hash.keySet()) {
//							totaltime += (hash.get(key)-endt/1000);
//							//totaltime -= endt/1000;
//						}
					
					//write
					
					}
					
					
				}
				//System.out.println("Time taken to execute normal transformation chain \n"+chain.get(i)+" is \n"+totaltime+" seconds.\n");
				
			}
		
			//scriptRoot.resolve("Script" + ".etl").toFile().delete();
			//modules=(ArrayList<EtlModule>) modules.stream().distinct().collect(Collectors.toList());
//			for (EtlModule module : modules) {
//				System.out.println("------------------");
//				System.out.println(module.getSourceFile().getName());
//				System.out.println("------------------\n");
////				System.err.println(new EtlUnparser().unparse(module));
//				
//				
//				}	
			
//			System.out.println("Modules: "+"\n");
//			for (EtlModule module : modules) {
//				System.out.println(module);
//			}
			
//			System.out.println(modules);
			int ct=0;
			ArrayList<Integer> index = new ArrayList<Integer>();
			String targetMetamodel;
			for (int m=0;m<modules.size();m++) {
				//System.out.println(module+"\n");
				//module.getContext().setModule(module);
//				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//				//System.out.println(module);
//				for (ModelDeclaration modelDeclaration : modules.get(m).getDeclaredModelDeclarations()) {
//					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//					}
//				}
//				
//				staticAnalyser.validate(modules.get(m));
				
				List<ModelDeclaration> mm1, mm2;
				String sourceMetamodel1 = null, targetMetamodel1 = null, sourceMetamodel2 = null, targetMetamodel2 = null;
				if(m+1<modules.size()) {
					mm1 = modules.get(m).getDeclaredModelDeclarations();
					targetMetamodel1 = mm1.get(1).getModel().getName();
					sourceMetamodel1 = mm1.get(0).getModel().getName();
					mm2 = modules.get(m+1).getDeclaredModelDeclarations();
					targetMetamodel2 = mm2.get(1).getModel().getName();
					sourceMetamodel2 = mm2.get(0).getModel().getName();
				//}
					int start=0;
					ct++;
					//System.out.println((metamodelsRoot.resolve(targetMetamodel+".ecore")).toString()+" "+targetMM);
					if((metamodelsRoot.resolve(targetMetamodel1+".ecore")).toString().equals(targetMM)) {
						if((metamodelsRoot.resolve(targetMetamodel1+".ecore")).toString().equals((metamodelsRoot.resolve(targetMetamodel2+".ecore")).toString()) &&
								(metamodelsRoot.resolve(sourceMetamodel1+".ecore")).toString().equals((metamodelsRoot.resolve(sourceMetamodel2+".ecore")).toString())) {
							if(index.size()>0)
								index.remove(index.size()-1);
							index.add(ct+1);
						}
						else
							index.add(ct);
						//index = {modules.indexOf(module)};
					}
				}
				else if(!index.isEmpty() && !modules.isEmpty() && m==modules.size()-1 && index.get(index.size()-1)!=modules.size())
					index.add(modules.size());
				
				//System.out.println(mm1.get(1).getModel().getName());
				
		
			}
			
			//System.out.println(index);
			int start=0;
			for(int k=0;k<index.size();k++) {	
				ArrayList<EtlModule> x=null;
				List<ModelDeclaration> mm1 = modules.get(start).getDeclaredModelDeclarations();
				String sourceMetamodel = mm1.get(0).getModel().getName();
				if((metamodelsRoot.resolve(sourceMetamodel+".ecore")).toString().equals(sourceMM)) {
					x = new ArrayList<EtlModule>(modules.subList(start, index.get(k)));
					x=(ArrayList<EtlModule>) x.stream().distinct().collect(Collectors.toList());
					alloptimizedmodules.add(x);
				}
				
//				else if(sourceMetamodel==null)
//					continue;
				//x.removeAll(Collections.singleton(null));
				
				//alloptimizedmodules.removeAll(Collections.singleton(null));
				start=index.get(k);
			}
			
			
			//System.out.println(alloptimizedmodules);
			
//			for (EtlModule module : modules) {
////				double start = System.currentTimeMillis();
//				//System.out.println(module.getSourceFile().getName());
//				executeETL(module);
////				System.out.println("Time taken to execute transformation "+ module.getDeclaredModelDeclarations().get(0).getModel().getName()+" -> "+module.getDeclaredModelDeclarations().get(1).getModel().getName()+ " = "+(System.currentTimeMillis()-start)/1000+" seconds.");
//			}
			ArrayList<ArrayList<EtlModule>> newmodule0 = new ArrayList<ArrayList<EtlModule>>();
			ArrayList<EtlModule> newlist0 = new ArrayList<EtlModule>();
			ArrayList<ArrayList<URI>> newmodule1 = new ArrayList<ArrayList<URI>>();
			ArrayList<URI> newlist1 = new ArrayList<URI>();
			for(int a=0;a<alloptimizedmodules.size();a++) {
				ArrayList<File> newlist = new ArrayList<File>();
				for(int b=0;b<alloptimizedmodules.get(a).size();b++) {
//					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//					for (ModelDeclaration modelDeclaration : alloptimizedmodules.get(a).get(b).getDeclaredModelDeclarations()) {
//						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//						}
//					}
					
					//staticAnalyser.validate(alloptimizedmodules.get(a).get(b));
					
					List<ModelDeclaration> mm1 = alloptimizedmodules.get(a).get(b).getDeclaredModelDeclarations();
					
//					String sourceMetamodel1 = mm1.get(0).getModel().getName();
//					String targetMetamodel1 = mm1.get(1).getModel().getName();
//					newlist.add(sourceMetamodel1+".ecore");
//					newlist.add(targetMetamodel1+".ecore");
					newlist0.add(alloptimizedmodules.get(a).get(b));
					newlist.add(alloptimizedmodules.get(a).get(b).getSourceFile());		
					//newlist1.add(alloptimizedmodules.get(a).get(b).getSourceUri());		
					}
				//newlist=(ArrayList<File>) newlist.stream().distinct().collect(Collectors.toList());
				//newlist1=(ArrayList<EtlModule>) newlist1.stream().distinct().collect(Collectors.toList());
				//System.out.println(newlist);
				newmodule0.add(newlist0);
				newmodule.add(newlist);
				newmodule1.add(newlist1);
				
				
			}
			//System.out.println(newmodule0);
			//System.out.println("New module: "+newmodule);
			for(ArrayList<File> newmod : newmodule) {
				for(File modulefile : newmod) {
					EtlModule module = new EtlModule();
					module.parse(modulefile);
					FileWriter fw1 = new FileWriter(scriptRoot.resolve("Optimized_"+module.getSourceFile().getName()).toString());
					fw1.write(new EtlUnparser().unparse(module));
					fw1.close();
					
					File file = null;
					File file1 = null;
					
					try {
						file = new File(scriptRoot.resolve("Optimized_"+module.getSourceFile().getName()).toString());
						file1 = new File(scriptRoot.resolve("Optimized1_"+module.getSourceFile().getName()).toString());

						 	FileInputStream fstream = new FileInputStream(file);
						    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
						    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));
						    
		
						    String strLine;
						    int countlines=0;
						    //final String strLine1 = br.readLine();
						    while ((strLine = br.readLine()) != null)   {
						    	countlines++;
						    	if(file1.length()==0)
						    	{
						    		if(countlines<=2)
						        	{
						        		bw.write(strLine + ";");
							            //bw.newLine();
						        	}
						        	else
						        		bw.write(strLine);
						        	bw.newLine();
						    	}

						    
						    }
						   
						   // file1.createNewFile();
						    bw.flush();
						    bw.close();
						    br.close();
						    
						    fstream.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println(e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println(e.getMessage());
					}
					
				}
				
			}
			
//			for(ArrayList<File> newmod : newmodule) {
//				for(File modulefile : newmod) {
//					System.out.println(modulefile.getName());
//					if(modulefile.getName().startsWith("Optimized_"))
//						scriptRoot.resolve(modulefile.getName()).toFile().delete();
//				}
//			}
				
			
			//newlist0=(ArrayList<EtlModule>) newlist0.stream().distinct().collect(Collectors.toList());
			
//			for(ArrayList<String> ch1 : chain) {
//				double totaltime=0;
//				for(String newch : ch1) {
//					EtlModule module = new EtlModule();
//					module.parse(newch);
//					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//					for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//						}
//					}
//					
//					staticAnalyser.validate(module);
//					HashMap<EtlRunConfiguration, Double> hash = executeETL_time(module);
//					for (EtlRunConfiguration key : hash.keySet()) {
//						totaltime += hash.get(key);
//					}
//				}
//				System.out.println("Time taken to execute normal transformation chain \n"+ch1+" is \n"+totaltime+" seconds.\n");
//			}
			
			//save for later use
			for (ArrayList<String> modulechains : chain) {
				double totaltime=0;
				for(int j=0;j<modulechains.size();j++) {
					if(j+1<modulechains.size()) {
//					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + modulechains.get(j),metamodelPath + "/" + modulechains.get(j+1)).size();e++)
//					{
							
					EtlModule module = new EtlModule();
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + modulechains.get(j),
							metamodelPath + "/" + modulechains.get(j + 1)).get(0)));
					for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					
					staticAnalyser.validate(module);
					HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
					//}
					}
				}
				System.out.println("Time taken to execute normal transformation chain \n"+modulechains+" is \n"+(totaltime)+" seconds.\n");
				
			}
			//save end
			
			//save for later use
			for (ArrayList<File> modulechain : newmodule) {
				double totaltime=0;
				for(File modulefile : modulechain) {
					EtlModule module = new EtlModule();
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module.parse(modulefile);
					for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					
					staticAnalyser.validate(module);
					HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
				}
				System.out.println("Time taken to execute optimized transformation chain \n"+modulechain+" is \n"+(totaltime)+" seconds.\n");
				
			}
			//save end
			
			//System.out.println("Static analysis time: "+endt/1000+" seconds.");
			
			//System.out.println(newmodule);
			
			//for storing (optional)
//			try
//	        { //store it in a file
//	            FileOutputStream fos = new FileOutputStream("listData");
//	            ObjectOutputStream oos = new ObjectOutputStream(fos);
//	            oos.writeObject(newmodule);
//	            FileOutputStream fos1 = new FileOutputStream("listData1");
//	            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
//	            oos1.writeObject(chain);
//	            oos1.close();
//	            fos1.close();
//	            oos.close();
//	            fos.close();
//	        } 
//	        catch (IOException ioe) 
//	        {
//	            ioe.printStackTrace();
//	        }
			
			return alloptimizedmodules;
			
//			for (int i = 0; i < chain.size(); i++) {
//				for(int j=0;j<chain.get(i).size();j++) {
//					EtlModule module = new EtlModule();
//					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
//							chain.get(i).get(j+1)).size();e++) {
//						
//					}
//					
//				}
//			}
					
				//}
							
		}
	
	@SuppressWarnings("unchecked")
	public static void executeoptimizedchain() throws Exception {
		try
        {
            FileInputStream fis = new FileInputStream("listData");
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            alloptimizedmodules1 = (ArrayList<ArrayList<File>>) ois.readObject();
 
            ois.close();
            fis.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
            return;
        } 
        catch (ClassNotFoundException c) 
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
		System.out.println(alloptimizedmodules1);
		for(int i=0;i<alloptimizedmodules1.size();i++) {
			double totaltime=0;
			for(int j=0;j<alloptimizedmodules1.get(i).size();j++) {
				EtlModule module = new EtlModule();
				//System.out.println(alloptimizedmodules1.get(i).get(j));
				//module.getContext().setModule(module);
				
				module.parse(alloptimizedmodules1.get(i).get(j));
				
//				String newsourceModel=null, newtargetModel=null;
//				if(j+1<alloptimizedmodules1.get(i).size()) {
//					if(alloptimizedmodules1.get(i).get(j).equals(sourceMM.substring(11))) {
//						newsourceModel=sourceModel;
//						if(alloptimizedmodules1.get(i).get(j+1).equals(targetMM.substring(11)))
//							newtargetModel=targetModel;
//						else
//							newtargetModel=modelsRoot.resolve(alloptimizedmodules1.get(i).get(j+1).substring(0,alloptimizedmodules1.get(i).get(j+1).lastIndexOf('.'))+".xmi").toAbsolutePath().toUri().toString();
//					}
//					else {
//						newsourceModel=modelsRoot.resolve(alloptimizedmodules1.get(i).get(j).substring(0,alloptimizedmodules1.get(i).get(j).lastIndexOf('.'))+".xmi").toAbsolutePath().toUri().toString();
//						if(alloptimizedmodules1.get(i).get(j+1).equals(targetMM.substring(11)))
//							newtargetModel=targetModel;
//						else
//							newtargetModel=modelsRoot.resolve(alloptimizedmodules1.get(i).get(j+1).substring(0,alloptimizedmodules1.get(i).get(j+1).lastIndexOf('.'))+".xmi").toAbsolutePath().toUri().toString();
//					}
//					System.out.println(newsourceModel+" "+metamodelsRoot.resolve(alloptimizedmodules1.get(i).get(j))+" "+newtargetModel+" "+metamodelsRoot.resolve(alloptimizedmodules1.get(i).get(j+1)).toString());
//					HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourceModel, alloptimizedmodules1.get(i).get(j), 
//							newtargetModel, alloptimizedmodules1.get(i).get(j+1));
				
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				//System.out.println(module);
				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}
				System.out.println(module);
				staticAnalyser.validate(module);
				
				System.out.println(module.getDeclaredModelDeclarations());
				
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
				
			}
			System.out.println("Time taken to execute optimized transformation chain \n"+alloptimizedmodules1.get(i)+" is \n"+totaltime+" seconds.\n");
		}
	}
	
	public static void executenormalchain() throws Exception {
		
		try
        {
            FileInputStream fis = new FileInputStream("listData1");
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            alloptimizedmodules2 = (ArrayList) ois.readObject();
 
            ois.close();
            fis.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
            return;
        } 
        catch (ClassNotFoundException c) 
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
		System.out.println(alloptimizedmodules2);
		for(int i=0;i<alloptimizedmodules2.size();i++) {
			double totaltime=0;
			for(int j=0;j<alloptimizedmodules2.get(i).size();j++) {
				EtlModule module = new EtlModule();
				System.out.println(alloptimizedmodules2.get(i).get(j));
				//module.getContext().setModule(module);
				module.parse(alloptimizedmodules2.get(i).get(j));

				
//				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//				//System.out.println(module);
//				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//					}
//				}
//				
//				staticAnalyser.validate(module);
				System.out.println(module);
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
				
			}
			System.out.println("Time taken to execute normal transformation chain \n"+alloptimizedmodules1.get(i)+" is \n"+totaltime+" seconds.\n");
		}
	}
	
public static void executenormalchain2() throws Exception {
		ArrayList<String> normalchain = new ArrayList<String>();
		try
        {
            FileInputStream fis = new FileInputStream("listData1");
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            normalchain = (ArrayList) ois.readObject();
 
            ois.close();
            fis.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
            return;
        } 
        catch (ClassNotFoundException c) 
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
		System.out.println(normalchain);
		for(int i=0;i<normalchain.size();i++) {
			double totaltime=0;
			//for(int j=0;j<alloptimizedmodules2.get(i).size();j++) {
				if(i+1<normalchain.size()) {
				EtlModule module = new EtlModule();
				System.out.println(chainingMt.identifyETL(metamodelPath+"/"+normalchain.get(i), metamodelPath+"/"+normalchain.get(i+1)).get(0));
				//module.getContext().setModule(module);
				module.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath+"/"+normalchain.get(i), metamodelPath+"/"+normalchain.get(i+1)).get(0)).toString());

				
//				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//				//System.out.println(module);
//				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//					}
//				}
//				
//				staticAnalyser.validate(module);
				System.out.println(module);
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}
				
			}
			System.out.println("Time taken to execute normal transformation chain \n"+normalchain+" is \n"+totaltime+" seconds.\n");
		}
	}
	
	public static EtlRunConfiguration executeETL(IEolModule module)
			throws Exception {
		Path etlscript = null;
		
		ModelProperties modelProperties = new ModelProperties();
		
		//module.getContext().setModule(module);
//		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//		//System.out.println(module);
//		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//			}
//		}
//		
//		staticAnalyser.validate(module);
		
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();
		
		//System.out.println(mm1);
		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();
		
//		for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//				targetMetamodel+".ecore").size();e++)
//			etlscript = scriptRoot
//				.resolve(chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//						metamodelPath + "/" + targetMetamodel+".ecore").get(e));
		
		StringProperties sourceProperties, targetProperties;
		
		if(metamodelsRoot.resolve(sourceMetamodel+".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					modelsRoot.resolve(sourceMetamodel+".xmi").toString(), "true", "false");
		if(metamodelsRoot.resolve(targetMetamodel+".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					modelsRoot.resolve(targetMetamodel+".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();
		
		//double start1 = System.currentTimeMillis();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		//double end1 = System.currentTimeMillis()-start1;
		sm1.run();

		runConfig.dispose();
		System.out.println("Time taken to execute transformation "+ module.getDeclaredModelDeclarations().get(0).getModel().getName()+" -> "+module.getDeclaredModelDeclarations().get(1).getModel().getName()+ " = "+(System.currentTimeMillis()-start)/1000+" seconds.");
		return runConfig;

	}
	
	public static HashMap<EtlRunConfiguration, Double> executeETL_time(IEolModule module)
			throws Exception {
		//Path etlscript = null;
		
		ModelProperties modelProperties = new ModelProperties();
		
		double start1 = System.currentTimeMillis();
		//module.getContext().setModule(module);
		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		//System.out.println(module);
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}
		
		staticAnalyser.validate(module);
		
		double end1 = System.currentTimeMillis()-start1;
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();
		
		//System.out.println(mm1);
		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();
		
//		for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//				targetMetamodel+".ecore").size();e++)
//			etlscript = scriptRoot
//				.resolve(chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//						metamodelPath + "/" + targetMetamodel+".ecore").get(e));
		
		StringProperties sourceProperties, targetProperties;
		
		if(metamodelsRoot.resolve(sourceMetamodel+".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					modelsRoot.resolve(sourceMetamodel+".xmi").toString(), "true", "false");
		if(metamodelsRoot.resolve(targetMetamodel+".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					modelsRoot.resolve(targetMetamodel+".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		//double start1 = System.currentTimeMillis();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		
		sm1.run();
		//runConfig.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis()-start-end1)/1000;
		System.out.println("Time taken to execute transformation "+ module.getDeclaredModelDeclarations().get(0).getModel().getName()+" -> "+module.getDeclaredModelDeclarations().get(1).getModel().getName()+ " = "+(endtime)+" seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}
	
	public static HashMap<EtlRunConfiguration, Double> executeETL_time2(IEolModule module)
			throws Exception {
		//Path etlscript = null;
		
		ModelProperties modelProperties = new ModelProperties();
		
		//double start1 = System.currentTimeMillis();
		//module.getContext().setModule(module);
//		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		//System.out.println(module);
//		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//			}
//		}
//		
//		//staticAnalyser.validate(module);
		
	//	double end1 = System.currentTimeMillis()-start1;
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();
		
		//System.out.println(mm1);
		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();
		
//		for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//				targetMetamodel+".ecore").size();e++)
//			etlscript = scriptRoot
//				.resolve(chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//						metamodelPath + "/" + targetMetamodel+".ecore").get(e));
		
		StringProperties sourceProperties, targetProperties;
		
		if(metamodelsRoot.resolve(sourceMetamodel+".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					modelsRoot.resolve(sourceMetamodel+".xmi").toString(), "true", "false");
		if(metamodelsRoot.resolve(targetMetamodel+".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					modelsRoot.resolve(targetMetamodel+".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		//double start1 = System.currentTimeMillis();
		//EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		
		//sm1.run();
		runConfig.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis()-start)/1000;
		System.out.println("Time taken to execute transformation "+ module.getDeclaredModelDeclarations().get(0).getModel().getName()+" -> "+module.getDeclaredModelDeclarations().get(1).getModel().getName()+ " = "+(endtime)+" seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}
	
	public static HashMap<EtlRunConfiguration, Double> executeETL_time3(IEolModule module)
			throws Exception {
		//Path etlscript = null;
		
		ModelProperties modelProperties = new ModelProperties();
		
		//double start1 = System.currentTimeMillis();
		//module.getContext().setModule(module);
		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
		//System.out.println(module);
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}
		
		staticAnalyser.validate(module);
		
	//	double end1 = System.currentTimeMillis()-start1;
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();
		
		//System.out.println(mm1);
		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();
		
//		for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//				targetMetamodel+".ecore").size();e++)
//			etlscript = scriptRoot
//				.resolve(chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//						metamodelPath + "/" + targetMetamodel+".ecore").get(e));
		
		StringProperties sourceProperties, targetProperties;
		
		if(metamodelsRoot.resolve(sourceMetamodel+".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					modelsRoot.resolve(sourceMetamodel+".xmi").toString(), "true", "false");
		if(metamodelsRoot.resolve(targetMetamodel+".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					modelsRoot.resolve(targetMetamodel+".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		//double start1 = System.currentTimeMillis();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		
		sm1.run();
		runConfig.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis()-start)/1000;
		System.out.println("Time taken to execute transformation "+ module.getDeclaredModelDeclarations().get(0).getModel().getName()+" -> "+module.getDeclaredModelDeclarations().get(1).getModel().getName()+ " = "+(endtime)+" seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}
	
	public static HashMap<EtlRunConfiguration, Double> executeETL_time1(IEolModule module)
			throws Exception {
		//Path etlscript = null;
		
		ModelProperties modelProperties = new ModelProperties();
		
		//double start1 = System.currentTimeMillis();
		//module.getContext().setModule(module);
//		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
//		//System.out.println(module);
//		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
//			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
//			}
//		}
		
		//staticAnalyser.validate(module);
		
		//double end1 = System.currentTimeMillis()-start1;
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();
		
		//System.out.println(mm1);
		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();
		
//		for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//				targetMetamodel+".ecore").size();e++)
//			etlscript = scriptRoot
//				.resolve(chainingMt.identifyETL(metamodelPath + "/" + sourceMetamodel+".ecore",
//						metamodelPath + "/" + targetMetamodel+".ecore").get(e));
		
		StringProperties sourceProperties, targetProperties;
		
		if(metamodelsRoot.resolve(sourceMetamodel+".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel, metamodelPath + "/" + sourceMetamodel+".ecore",
					modelsRoot.resolve(sourceMetamodel+".xmi").toString(), "true", "false");
		if(metamodelsRoot.resolve(targetMetamodel+".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel, metamodelPath + "/" + targetMetamodel+".ecore",
					modelsRoot.resolve(targetMetamodel+".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder().withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		//double start1 = System.currentTimeMillis();
		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
		
		sm1.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis()-start)/1000;
		//System.out.println("Time taken to execute transformation "+ module.getDeclaredModelDeclarations().get(0).getModel().getName()+" -> "+module.getDeclaredModelDeclarations().get(1).getModel().getName()+ " = "+(endtime)+" seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}
	
	public static void findTransformationRuleIndex1() throws Exception {
		//int delrule=0;
		//ArrayList<Integer> delrule_list;
		//List<ArrayList<String>> l = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> delrule_list = null;
		//for (int i = 0; i < l.size(); i++) {
			delrule_list = chainingMt.deletetrindex(sourceModel, sourceMM, targetModel, targetMM);
			System.out.println("Delete index rule in list: "+delrule_list+"\n");
			
//			int rule_no = 0;
			
			for(int d=0;d<delrule_list.size();d++)
			{
				EtlModule module1 = new EtlModule();
				for(int x=0;x<delrule_list.get(d).size();x++)
				{
					
					
					StatementBlock ruleblock = null;
					String[] splitIndex = null;
					
					splitIndex = delrule_list.get(d).get(x).split("\\s+");
									
					//System.out.println(scriptRoot.resolve(splitIndex[5]+".etl"));
					
					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").size();e++)
					{
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]+".ecore",
								metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e)));
						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser.validate(module1);
						
//						for (int i = 0; i < ((EtlModule) module1).getTransformationRules().size(); i++) {
//							for (int j = 0; j < ((EtlModule) module1).getTransformationRules().get(i).getTargetParameters()
//									.size(); j++) {
//							rule_no++;
						
						
						System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "
								+splitIndex[4]+" in transformation "+chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0]
										+".ecore",metamodelPath + "/" + splitIndex[5].split("2")[1]+".ecore").get(e));
						String tp_target;
						int noofrules=0;
						
						ArrayList<Integer> count_x = new ArrayList<Integer>();
						for (TransformationRule tr : module1.getTransformationRules()) {
							int count=0;
							noofrules++;
							String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
							//System.out.println("Transformation source rules: "+tp_source);
//							//System.out.println(splitIndex[4].split("2")[0]);
							
							//for(int ts=0;ts<module1.getDeclaredTransformationRules().size();ts++)
							ruleblock = (StatementBlock) tr.getBody().getBody();
							Statement delete_stLine = null;
							for(int tp=0;tp<tr.getTargetParameters().size();tp++)
							{								
								tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
								//System.out.println("Transformation target rules: "+tp_target);
								
//								for (int k = 0; k < ruleblock.getStatements().size(); k++) {
//									count++;
//								}
								
								
								count=ruleblock.getStatements().size();
								if(tp_source.equals(splitIndex[4].split("2")[0]) && 
										tp_target.equals(splitIndex[4].split("2")[1]))
								{
									//count--;
									//System.out.println(splitIndex[0]);
									//for(TransformationRule ts : module1.getDeclaredTransformationRules())
									delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
									System.out.println("Delete line "+delete_stLine);
									count--;
									count_x.add(count);
									
								}
								
							}
							
//							if(ruleblock.getStatements().size()==0)
//							{
//								TransformationRule removerules = module1.getTransformationRules().remove(noofrules);
//								System.out.println("Remove rules: "+removerules);
//							}
							
						}
						
						for(int cx=0;cx<count_x.size();cx++)
						{
							System.out.println("Count remaining statements in "+splitIndex[4]+" rule is "+count_x.get(cx));
							if(count_x.get(cx)==0)
							{
								TransformationRule removerules = module1.getTransformationRules().remove(cx);
								System.out.println("Remove rules: "+removerules);
							}	
						}
						
					
					}
				}
				System.out.println(module1.getSourceFile().getName()+"\n");
				System.err.println(new EtlUnparser().unparse(module1));		
					
				}
							
		}

	public static void optimizeThenSelect() throws Exception {
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<EtlModule>();
		EtlRewritingHandler etlRewritingHandler = new EtlRewritingHandler();

		int min = 99999;
		int[] sum = new int[chain.size()];
		ArrayList<String> index = null;

		for (int j = 0; j < chain.size(); j++) {

			int rewritetotal = 0;
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");

			for (int i = 0; i < chain.get(j).size(); i++) {
				
				//chainingMt.registerMM(metamodelsRoot.resolve(chain.get(j).get(i)).toAbsolutePath().toString());
				EtlModule module1 = new EtlModule();

				if (i + 1 < chain.get(j).size()) {

					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)).size();e++)
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + (chain.get(j).get(i + 1))).get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}

					staticAnlayser.validate(module1);

					modules.add(module1);

					staticAnalysers.add(staticAnlayser);

				}

			}

			Collections.reverse(modules);
			Collections.reverse(staticAnalysers);

			rewritetotal = etlRewritingHandler.invokeRewriters(modules, staticAnalysers);
			modules.clear();
			staticAnalysers.clear();
			sum[j] = rewritetotal;
			System.out.println("\nTotal structural features of optimized " + "Chain" + (j + 1) + " " + chain.get(j)
					+ " is " + rewritetotal);
			if (sum[j] < min) {
				min = sum[j];
				index = chain.get(j);
			}
			continue;
		}

		System.out.println("\nOptmized MT Chain " + index + " has minimum structural features of " + min);
		System.out.println("------------------Executing best optimized chain--------------------");
		for (int k = 0; k < index.size(); k++) {
			if (k + 1 < index.size()) {
				Path newsourcemodelpath;
				String newsourcemodel = null;
				if(k>0) {
					newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "") + ".xmi");
					newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
				}
				else {
					newsourcemodel = sourceModel;
				}
				

				Path newtargetmodelpath = modelsRoot.resolve(index.get(k + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

				System.out.println(newsourcemodel+" "+ metamodelPath + "/" + index.get(k)+" "+ newtargetmodel+" "+
						metamodelPath + "/" + index.get(k + 1));
				chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + index.get(k), newtargetmodel,
						metamodelPath + "/" + index.get(k + 1));

			}

		}

	}

	public static void optimize() throws Exception {
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		ArrayList<String> bestchain = chainingMt.identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);

		for (int i = 0; i < bestchain.size(); i++) {
			EtlModule module1 = new EtlModule();

			if (i + 1 < bestchain.size()) {

				EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
				for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(i),metamodelPath + "/" + bestchain.get(i+1)).size();e++)
					module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(i),
						metamodelPath + "/" + (bestchain.get(i + 1))).get(e)));
				for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnlayser.validate(module1);

				modules.add(module1);
				staticAnalysers.add(staticAnlayser);

			}

		}
		Collections.reverse(modules);
		Collections.reverse(staticAnalysers);
		new EtlRewritingHandler().invokeRewriters1(modules, staticAnalysers);
		for (int k = 1; k < bestchain.size(); k++) {
			if (k + 1 < bestchain.size()) {
				Path newsourcemodelpath = modelsRoot.resolve(bestchain.get(k).replaceFirst("[.][^.]+$", "") + ".xmi");
				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();

				Path newtargetmodelpath = modelsRoot
						.resolve(bestchain.get(k + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

				chainingMt.executeETL(newsourcemodel, metamodelPath + "/" + bestchain.get(k), newtargetmodel,
						metamodelPath + "/" + bestchain.get(k + 1));
			}

		}
	}

	public static void optimizeOnly() throws Exception {

		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
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
			chain.add(two);
		System.out.println("Chains: "+chain);
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		for (int j = 0; j < chain.size(); j++) {
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");

			for (int i = 0; i < chain.get(j).size(); i++) {
				EtlModule module1 = new EtlModule();

				if (i + 1 < chain.get(j).size()) {

					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)).size();e++)
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + (chain.get(j).get(i + 1))).get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}

					staticAnlayser.validate(module1);

					modules.add(module1);

					staticAnalysers.add(staticAnlayser);

				}

			}

			Collections.reverse(modules);
			Collections.reverse(staticAnalysers);

			new EtlRewritingHandler().invokeRewriters1(modules, staticAnalysers);
			modules.clear();
			staticAnalysers.clear();
		}
	}
	
	public static void optimize_2() throws Exception {

		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);

		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		for (int j = 0; j < chain.size(); j++) {
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
		for (int i = 0; i < chain.get(j).size(); i++) {
				EtlModule module1 = new EtlModule();

				if (i + 1 < chain.get(j).size()) {

					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					
					for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)).size();e++)
						module1.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + (chain.get(j).get(i + 1))).get(e)));
				
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}

					staticAnlayser.validate(module1);
					modules.add(module1);
					staticAnalysers.add(staticAnlayser);

				}

			}

			Collections.reverse(modules);
			Collections.reverse(staticAnalysers);
			
			System.out.println("Rewrite Query \n");
			new EtlRewritingHandler().invokeRewriters_2(modules, staticAnalysers);
			modules.clear();
			staticAnalysers.clear();
		}
	}
	
	public static List<ArrayList<String>> createChain(String sourceModel1, String sourceMM1, String targetModel1, String targetMM1) throws Exception
	{
		//Chaining_MT chainingMt = new Chaining_MT();
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel1, sourceMM1, targetModel1, targetMM1);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel1, sourceMM1, targetModel1, targetMM1);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM1, targetMM1);
		//System.out.println(identifyETLinModels(sourceMM, targetMM));
		if (etl1) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM1, targetMM1);
		for(int id=0;id<cm.identifyETLinModels(sourceMM1, targetMM1).size();id++) {
			
			//System.out.println("qwerty: "+x);
			Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)), StandardCopyOption.REPLACE_EXISTING);
			//break;
		}
		}
		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm=new ArrayList<String>();
		if(chainingMt.findETL(targetMM1, targetMM1))
		{
			samemm.add(targetMM1.substring(targetMM1.indexOf("\\")+1,targetMM1.length()));
			samemm.add(targetMM1.substring(targetMM1.indexOf("\\")+1,targetMM1.length()));
			chain.add(samemm);
		}
		System.out.println("Chains: "+chain);
		return chain;
	}
	/*
	public static ArrayList<Double> calculateMTCoverage_new(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
	for(int e=0;e<chainingMt.identifyETL(sourceMM,targetMM).size();e++)
	{
		EtlModule module1 = new EtlModule();
		
		module1.parse(scriptRoot.resolve(chainingMt.identifyETL(sourceMM, targetMM).get(e)));
		System.out.println("\n"+scriptRoot.resolve(chainingMt.identifyETL(sourceMM, targetMM).get(e)));
	
	EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
	ArrayList<String> src_dep = chainingMt.srcDependency2(module1);
	
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
			
				FileWriter fw11 = new FileWriter(scriptRoot.resolve("newDep" + ".eol").toString());
				fw11.write(code);
				fw11.close();
//				System.out.println("Total covered features of source "+type.getTypeName()+" is "+sum3);
				String x = chainingMt.executeEOL1(sourceMM111, metaMM, scriptRoot.resolve("newDep" + ".eol"));
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
				
			}	
		}
	
		cov = (float) count_feature/count;
		tot_cov.add(cov);
	
	}
	
	return tot_cov;
		
}
*/
	
	public static void registerMM(String mm)
	{
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
	}
	

}