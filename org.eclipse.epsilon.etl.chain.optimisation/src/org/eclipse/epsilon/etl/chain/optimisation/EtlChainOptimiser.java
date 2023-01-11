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
 * which is available at https:
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

class Serialize_IdentifyETL implements java.io.Serializable {
	public String s;
	public String t;
	ArrayList<String> etl;

	public Serialize_IdentifyETL(String s, String t, ArrayList<String> etl) {
		this.s = s;
		this.t = t;
		this.etl = etl;
	}

}

public class EtlChainOptimiser {

	static Path modelsRoot = Paths.get("models");
	static Path scriptRoot = Paths.get("scripts");

	static Path metamodelsRoot = Paths.get("metamodels");
	static Path genmodelsRoot = Paths.get("models/generatedmodels");

	static File metamodelPath = new File("metamodels");
	static String metamodelscontents[] = metamodelPath.list();

	static File scriptPath = new File("scripts");
	static String scriptcontents[] = scriptPath.list();
	static HashMap<String, Boolean> findetl = new HashMap<String, Boolean>();
	static ArrayList<ArrayList<EtlModule>> alloptimizedmodules = new ArrayList<ArrayList<EtlModule>>();

	static List<ArrayList<File>> alloptimizedmodules1 = new ArrayList<ArrayList<File>>();
	static ArrayList<ArrayList<String>> alloptimizedmodules2 = new ArrayList<ArrayList<String>>();

	final static String outputFilePath_identifyETL = "C:\\Users\\sahay\\git\\repository\\org.eclipse.epsilon.etl.chain.optimisation\\writeETL.txt";

	static String filePath = "C:\\Users\\sahay\\git\\repository\\org.eclipse.epsilon.etl.chain.optimisation\\write.txt";

	static String sourceMM1 = metamodelsRoot.resolve("KM3.ecore").toString();
	static String targetMM1 = metamodelsRoot.resolve("XML.ecore").toString();
	static String sourceModel1 = modelsRoot.resolve("sample-km3.xmi").toString();
	static String targetModel1 = modelsRoot.resolve("sample-xml.xmi").toString();

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

	public static <K, V> Map<K, V> copyMap(Map<K, V> original) {

		Map<K, V> second_Map = new HashMap<>();

		for (Map.Entry<K, V> entry : original.entrySet()) {

			second_Map.put(entry.getKey(), entry.getValue());
		}

		return second_Map;
	}

	static Map<String, Double> second_Map;

	final static String outputFilePath = "write.txt";
	final static String outputFilePath2 = "writeComplexity.txt";

	public static void main(String[] args) throws Exception {

		System.out.println("\n 0: Execute chains of ETL \n");
		System.out.println("\n 1: Select chain with minimum complexity \n");
		System.out.println("\n 2: Optimise each identified chain\n");
		System.out.println("\n 3: Select chain with minimum complexity -> Optimise \n");
		System.out.println("\n 4: Optimise -> Select chain with minimum complexity \n");
		System.out.println(
				"\n 5: Find transformation rule index to delete unused statements and rewrite the optimized transformation \n");
		System.out.println("\n 6: Calculate transformation coverage of the original transformation chain \n");
		System.out.println(
				"\n 7: Calculate transformation coverage of the maximum of optimized and original transformation chain \n");
		System.out.println("\n 8: Calculate the execution time of the optimized transformation chain \n");
		System.out.println("\n 11: Calculate possible transformations from source metamodel " + sourceMM
				+ " and print all possible chains \n");
		System.out.println("\n 12: Calculate possible transformations to target metamodel " + targetMM1 + "\n");
		System.out.println("\n 13: Calculate and print coverage of all transformations and store it in a file " + "\n");
		System.out.println("\n 14: Print complexity of all transformations and store it in a file " + "\n");
		System.out.println("\n 15: Print coverage of all transformations " + "\n");
		System.out.println("\n 100: Execute direct model transformation from " + sourceMM1 + " to " + targetMM1 + "\n");
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

			executechainsofetl(sourceModel1, sourceMM1, targetModel1, targetMM1);

			break;
		case 1:
			ArrayList<String> bestchain = chainingMt.identifyBestChain(sourceModel, sourceMM, targetModel, targetMM);

			System.out.println("\n" + "Best chain: " + bestchain);
			break;
		case 101:
			executenormalchain2();
			break;
		case 102:
			executeoptimizedchain();
			break;
		case 111:
			ArrayList<String> two1 = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
			List<ArrayList<String>> chain1 = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
			Chain_MT cm1 = new Chain_MT();
			boolean etl11 = cm1.findETLinModels(sourceMM, targetMM);

			if (etl11) {
				for (int id = 0; id < cm1.identifyETLinModels(sourceMM, targetMM).size(); id++) {
					ArrayList<String> x = cm1.identifyETLinModels(sourceMM, targetMM);

					Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

				}
			}

			if (!two1.isEmpty())
				chain1.add(two1);
			ArrayList<String> samemm = new ArrayList<String>();
			if (chainingMt.findETL(targetMM, targetMM)) {
				samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
				samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
				chain1.add(samemm);
			}

			System.out.println("Chains: " + chain1);
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

			findTransformationRuleIndex();

			for (File f : scriptRoot.toFile().listFiles()) {
				if (f.getName().startsWith("Optimized_"))
					f.delete();
			}
			break;
		case 500:

			findTransformationRuleIndex_single();

			break;
		case 6:

			calculateTransformationCoverage();

			break;
		case 7:

			calculateTransformationCoverageOnOptimizedTransformation(sourceModel, sourceMM, targetModel, targetMM);

			EtlRewritingHandler.calculateTransformationCoverage(sourceModel, sourceMM, targetModel, targetMM);

			for (File f : scriptRoot.toFile().listFiles()) {
				if (f.getName().startsWith("Optimized1_"))
					f.delete();
			}

			break;
		case 70:
			EtlRewritingHandler.calculateTransformationCoverage(sourceModel, sourceMM, targetModel, targetMM);
			break;
		case 8:
			EtlRewritingHandler.calculatetime(sourceModel1, sourceMM1, targetModel1, targetMM1);
			for (File f : scriptRoot.toFile().listFiles()) {
				if (f.getName().startsWith("Optimized1_"))
					f.delete();
			}
			break;
		case 11:
			double start1 = System.currentTimeMillis();
			showChainFromSourceMM = chainingMt.identifyChain_source(sourceMM);
			System.out.println("Chain from source metamodel " + sourceMM + " is " + showChainFromSourceMM);
			System.out.println("Time = " + (System.currentTimeMillis() - start1) / 1000 + " seconds.");
			showAllChain(targetMM);
			System.out.println(resultETL);
			break;
		case 12:
			double start12 = System.currentTimeMillis();
			ArrayList<String> chain2 = chainingMt.identifyChain_target(targetMM1);
			System.out.println("Chain to target metamodel " + targetMM1 + " is " + chain2);
			System.out.println("Time = " + (System.currentTimeMillis() - start12) / 1000 + " seconds.");
			break;
		case 13:
			double start121 = System.currentTimeMillis();
			LinkedHashMap<String, Double> coveragemt = mt_coverage();
			System.out.println("\nCoverage of all transformations \n");
			for (Map.Entry<String, Double> entry : coveragemt.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			System.out.println("Time = " + (System.currentTimeMillis() - start121) / 1000 + " seconds.");
			break;
		case 14:
			double start123 = System.currentTimeMillis();
			final HashMap<String, Integer> complexitymt = mt_complexity();
			System.out.println("\nComplexity of all transformations \n");
			for (Map.Entry<String, Integer> entry : complexitymt.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			System.out.println("Time = " + (System.currentTimeMillis() - start123) / 1000 + " seconds.");
			break;
		case 15:
			LinkedHashMap<String, Double> mapFromFile = HashMapFromTextFile();
			System.out.println(mapFromFile);
			for (Map.Entry<String, Double> entry : mapFromFile.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			break;
		case 100:

			chainingMt.registerMM(sourceMM1);
			chainingMt.registerMM(targetMM1);
			chainingMt.executeETL(sourceModel1, sourceMM1, targetModel1, targetMM1);
			break;
		case 700:
			System.out.println("All MT coverage: " + chainingMt.allmtcoverage());
			break;
		case 1000:
			optimize_equivalent();
			for (File f : scriptRoot.toFile().listFiles()) {
				if (f.getName().startsWith("Optimized_"))
					f.delete();
			}
			break;
		case 1001:
			optimize_equivalent_all();
			break;
		case 1002:
			double time1 = 0;
			FileWriter fw = new FileWriter(
					"C:\\Users\\sahay\\OneDrive\\Desktop\\normaltime_" + sourceModel.substring(11) + ".txt", true);
			for (int i = 0; i < 5; i++) {
				double ntime = executebestchain(sourceModel, sourceMM, targetModel, targetMM);
				time1 += ntime;
				System.out.println("xbckjdbvkjdfbkjdfnbkdjfn " + String.valueOf(ntime));
				fw.write(String.valueOf(ntime) + "\t");
			}
			fw.close();
			System.out.println("Total normal time: " + time1);

			break;
		case 1003:
			double time2 = 0;
			FileWriter fw1 = new FileWriter(
					"C:\\Users\\sahay\\OneDrive\\Desktop\\opttime_" + sourceModel.substring(11) + ".txt", true);
			for (int i = 0; i < 5; i++) {
				double ntime1 = executebestchain_opt(sourceModel, sourceMM, targetModel, targetMM);
				time2 += ntime1;
				fw1.write(String.valueOf(ntime1) + "\t");
			}
			fw1.close();
			System.out.println("Total optimized time: " + time2);
			break;
		case 1005:
			chainingMt.countinstancesMM();
			break;
		case 1111:
			showChainFromSourceMM = chainingMt.identifyChain_source(sourceMM);
			System.out.println("Chain from source metamodel " + sourceMM1 + " is " + showChainFromSourceMM);
			l = showAllChain(targetMM);

			List<ArrayList<Integer>> indexlist = new ArrayList<ArrayList<Integer>>();
			List<ArrayList<String>> totalmodulelist = new ArrayList<ArrayList<String>>();
			ArrayList<Integer> complexitylist = new ArrayList<Integer>();

			HashMap<String, ArrayList<ArrayList<Integer>>> map = new HashMap<String, ArrayList<ArrayList<Integer>>>();
			HashMap<String, Integer> map_strf = new HashMap<String, Integer>();
			for (int i = 0; i < l.size(); i++) {

				ArrayList<String> modulelist = new ArrayList<String>();

				int sum = 0;
				int max_index = 300;
				for (int j = 0; j < l.get(i).size(); j++) {
					int total = 0;
					int min_feature = 9999;

					EtlModule module1 = new EtlModule();

					if ((j + 1) < l.get(i).size()) {

						for (Map.Entry<String, String> me : HashMap_IdETL().entrySet()) {

							if (me.getKey().equals(l.get(i).get(j) + " " + l.get(i).get(j + 1))) {
								module1.parse(scriptRoot.resolve(me.getValue()));
								ArrayList<Integer> mt_index = new ArrayList<Integer>();
								ArrayList<Integer> index = new ArrayList<Integer>(max_index);

								index = chainingMt.createList(module1);
								System.out.println(module1.getSourceFile().getName() + " " + index.size() + " "
										+ (max_index - index.size()));

								for (int ind = index.size(); ind < max_index; ind++) {
									index.add(0);
								}

								System.out.println(index.size());
								modulelist.add(module1.getSourceFile().getName());
								total = chainingMt.calculateMTChain1(module1);
								if (total < min_feature) {
									min_feature = total;
								}
								sum = sum + min_feature;

								System.out.println("Total expressions/operators used in the transformation "
										+ module1.getSourceFile().getName() + "= " + min_feature + "\n");
								mt_index.add(min_feature);
								map.put(module1.getSourceFile().getName(), new ArrayList<ArrayList<Integer>>());
								map.get(module1.getSourceFile().getName()).add(mt_index);
								map.get(module1.getSourceFile().getName()).add(index);

								map_strf.put(module1.getSourceFile().getName(), min_feature);

								indexlist.add(index);

							}

						}
					}
				}

				if (sum > 0) {
					complexitylist.add(sum);
					System.out.println("Minimum structural features of the chain: " + sum);
				}

				totalmodulelist.add(modulelist);

			}
			indexlist.removeIf(p -> p.isEmpty());
			totalmodulelist.removeIf(p -> p.isEmpty());

			System.out.println("\n" + totalmodulelist + " has size " + totalmodulelist.size());

			System.out.println("\nComplexity list of transformation chain: " + complexitylist);

			int in = 0;
			System.out.println("\nTransformation chain list used: compare transformation and metamodel elements.");

			for (ArrayList<String> tm : totalmodulelist) {
				List<List<Integer>> inl = new ArrayList<List<Integer>>();

				inl.addAll(new ArrayList<ArrayList<Integer>>(indexlist.subList(in, in + tm.size())));
				System.out.println("\n" + inl);

				in = in + tm.size();
			}
			System.out.println("\n");

			System.out.println("\nIterating Hashmap of vectors...");
			for (Map.Entry m : map.entrySet()) {
				System.out.println(m.getKey() + " " + m.getValue());
			}

			System.out.println("\nIterating Hashmap of structural features...");
			for (Map.Entry m : map_strf.entrySet()) {
				System.out.println(m.getKey() + " " + m.getValue());
			}

			System.out.println("\n");
			for (Map.Entry<String, String> me : HashMap_IdETL().entrySet()) {
				for (Map.Entry m : map.entrySet()) {
					if (me.getValue().equals(m.getKey())) {
						System.out.println(me.getKey() + " " + m.getValue());
					}
				}
			}

			System.out.println("\n");
			for (Map.Entry<String, String> me : HashMap_IdETL().entrySet()) {
				for (Map.Entry m : map_strf.entrySet()) {
					if (me.getValue().equals(m.getKey())) {
						System.out.println(me.getKey() + " " + m.getValue());
					}
				}
			}

			Integer[][] arr = new Integer[indexlist.size()][];

			int i = 0;
			for (ArrayList<Integer> lt : indexlist) {
				arr[i++] = lt.toArray(new Integer[lt.size()]);
			}

			break;
		case 11110:
			chainingMt.createListChain(sourceModel, sourceMM, targetModel, targetMM);
			break;
		case 1010:
			showModelEClass();
			break;
		case 1015:

			ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
			List<String> possibleChain = chainingMt.identifyPossibleChain(sourceMM, targetMM);
			Chain_MT cm = new Chain_MT();
			boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
			ArrayList<ArrayList<Integer>> countModelEClass = new ArrayList<ArrayList<Integer>>();

			if (etl1) {
				for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
					ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

					Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

				}
			}

			possibleChain = possibleChain.stream().distinct().collect(Collectors.toList());
			System.out.println("Possible Chains: " + possibleChain);

			break;
		case 1020:
			chainingMt.identifyAllChain(sourceMM, targetMM);
			break;
		case 1021:
			showAllChain(targetMM);
			break;
		case 5000:
			double start5000 = System.currentTimeMillis();
			deserialize_identifyetl(sourceMM, targetMM);
			System.out.println("Time = " + (System.currentTimeMillis() - start5000) / 1000 + " seconds.");
			break;
		case 5001:
			double start5001 = System.currentTimeMillis();
			System.out.println(chainingMt.identifyETL(sourceMM, targetMM));
			System.out.println("Time = " + (System.currentTimeMillis() - start5001) / 1000 + " seconds.");
			break;
		case 5002:
			System.out.println(HashMap_IdETL());
			break;
		default:
			System.out.println("Invalid number");
		}

	}

	static String filename_serialize = "file.ser";

	public static Serialize_IdentifyETL serialize_identifyetl(String mm1, String mm2) throws Exception {
		Serialize_IdentifyETL object = new Serialize_IdentifyETL(sourceMM, targetMM, chainingMt.identifyETL(mm1, mm2));

		ArrayList<String> idetl = null;

		try {

			FileOutputStream file = new FileOutputStream(filename_serialize);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(object);

			out.close();
			file.close();

			System.out.println("Object has been serialized");

		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		}
		return object;

	}

	public static ArrayList<String> deserialize_identifyetl(String mm1, String mm2) throws Exception {

		Serialize_IdentifyETL object1 = null;

		ArrayList<String> idetl = null;

		try {

			FileInputStream file = new FileInputStream(filename_serialize);
			ObjectInputStream in = new ObjectInputStream(file);

			object1 = (Serialize_IdentifyETL) in.readObject();

			in.close();
			file.close();
			idetl = object1.etl;
			System.out.println("Object has been deserialized ");
			System.out.println("sourceMM = " + object1.s);
			System.out.println("targetMM = " + object1.t);
			System.out.println("Etl = " + object1.etl);
		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}
		return idetl;
	}

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
		for (int i = 0; i < showChainFromSourceMM.size(); i++) {

			for (int j = 1; j < showChainFromSourceMM.size(); j++) {

				if (chainingMt.findETL(metamodelsRoot.resolve(showChainFromSourceMM.get(i)).toString(),
						metamodelsRoot.resolve(showChainFromSourceMM.get(j)).toString())) {
					modelsuse1.add(showChainFromSourceMM.get(i));
					modelsuse1.add(showChainFromSourceMM.get(j));

					adjList[i].add(i);
					adjList[i].add(j);
					System.out.println(showChainFromSourceMM.get(i) + " " + showChainFromSourceMM.get(j));

					g.addEdge(showChainFromSourceMM.get(i), showChainFromSourceMM.get(j));
					if (j > max && metamodelsRoot.resolve(showChainFromSourceMM.get(j)).toString().equals(targetMM))
						max = j;

				}

			}
		}
		System.out.println("\n");

		printadjlist = g.printAdjList();

		for (Map.Entry<String, ArrayList<String>> m : printadjlist.entrySet()) {
			for (String st : m.getValue()) {
				resultETL.put(m.getKey() + " " + st,
						chainingMt.identifyETL(metamodelPath + "/" + m.getKey(), metamodelPath + "/" + st).get(0));
			}

		}

		File file = new File(outputFilePath_identifyETL);

		BufferedWriter bf = null;

		try {

			bf = new BufferedWriter(new FileWriter(file));

			for (Map.Entry<String, String> entry : resultETL.entrySet()) {

				bf.write(entry.getKey() + ":" + entry.getValue());

				bf.newLine();
			}

			bf.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

				bf.close();
			} catch (Exception e) {
			}
		}
		System.out.println("\n");

		for (int i = 0; i < adjList.length; i++) {
			for (int j = 0; j < adjList[i].size(); j++) {
				if (adjList[i].get(j) != null) {
					System.out.print(adjList[i].get(j) + " ");

				} else
					continue;
			}
		}
		System.out.println("\nMax=" + max);
		int source = 0;

		ArrayList<ArrayList<String>> listtotalchain = new ArrayList<ArrayList<String>>();
		if (max > 0) {
			int dest = max;

			System.out.println("All possible paths from " + source + " to " + dest + " are:");
			ArrayList<ArrayList<Integer>> listchain = printAllPaths(source, dest);

			System.out.println("No. of transformation chains are " + listchain.size());
			System.out.println(
					"All possible paths from " + sourceMM.substring(11) + " to " + targetMM.substring(11) + " are: ");
			for (int i = 0; i < listchain.size(); i++) {
				ArrayList<String> newlist = new ArrayList<String>();
				for (int j = 0; j < listchain.get(i).size(); j++) {

					newlist.add(showChainFromSourceMM.get(listchain.get(i).get(j)));

				}
				listtotalchain.add(newlist);

			}
			System.out.println(listtotalchain);
		} else {
			System.out.println("Chain not found.");
			listtotalchain = null;
		}
		return listtotalchain;

	}

	public static ArrayList<ArrayList<Integer>> printAllPathsHelper(int start, int end, boolean[] isVisited,
			List<Integer> tempPathList) {
		ArrayList<Integer> newlist = new ArrayList<Integer>();

		if (start == end) {
			System.out.println(tempPathList);
			newlist.addAll((ArrayList<Integer>) tempPathList);

		}

		isVisited[start] = true;

		for (Integer i : adjList[start]) {
			if (!isVisited[i]) {
				tempPathList.add(i);
				printAllPathsHelper(i, end, isVisited, tempPathList);

				tempPathList.remove(i);
			}
		}
		isVisited[start] = false;
		newlist = (ArrayList<Integer>) newlist.stream().distinct().collect(Collectors.toList());

		chainlist.add(newlist);
		chainlist.removeIf(p -> p.isEmpty());
		return chainlist;
	}

	public static ArrayList<ArrayList<Integer>> printAllPaths(int source, int dest) {
		boolean[] isVisited = new boolean[showChainFromSourceMM.size()];
		ArrayList<Integer> pathList = new ArrayList<>();
		pathList.add(source);

		ArrayList<ArrayList<Integer>> list = printAllPathsHelper(source, dest, isVisited, pathList);

		return list;
	}

	public static void optimize_equivalent() throws Exception {
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		ArrayList<String> bestchain = chainingMt.identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);

		ArrayList<ArrayList<String>> listOfHashMap = new ArrayList<ArrayList<String>>();

		ArrayList<String> delrule_list = new ArrayList<String>();
		ArrayList<String> keeprule_list = new ArrayList<String>();
		if (chainingMt.findETL(targetMM, targetMM)) {
			delrule_list = chainingMt.deletetrindex2_single(targetModel, targetMM, targetModel, targetMM);

		} else {
			delrule_list = chainingMt.deletetrindex2_single(sourceModel, sourceMM, targetModel, targetMM);

		}

		for (int i = 0; i < bestchain.size(); i++) {
			EtlModule module1 = new EtlModule();

			if (i + 1 < bestchain.size()) {

				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				module1.parse(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + bestchain.get(i),
						metamodelPath + "/" + (bestchain.get(i + 1)))));
				EmfUtil.register(URI.createFileURI(metamodelsRoot.resolve(bestchain.get(i)).toString()),
						EPackage.Registry.INSTANCE);
				EmfUtil.register(URI.createFileURI(metamodelsRoot.resolve(bestchain.get(i + 1)).toString()),
						EPackage.Registry.INSTANCE);

				for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnalyser.validate(module1);

				HashMap<String, ArrayList<String>> hm = new EtlDependencyMapGenerator().optimiseTrace(module1,
						staticAnalyser);

				System.out.println(hm + " " + module1.getSourceFile().getName());
				Collection<String> k = hm.keySet();

				ArrayList<String> values = new ArrayList<>();
				for (String keys : k) {
					ArrayList<String> val_list = new ArrayList<String>();
					values = hm.get(keys);

					if (!values.isEmpty()) {
						val_list.add(keys);
						for (String val : values) {
							System.out.println(val);
							val_list.add(val);

						}
						val_list.add(module1.getSourceFile().getName());

					}

					listOfHashMap.add(val_list);
					listOfHashMap.removeIf(p -> p.isEmpty());
				}

				modules.add(module1);
				staticAnalysers.add(staticAnalyser);

			}

		}
		Collections.reverse(modules);
		Collections.reverse(staticAnalysers);

		System.out.println("Available list: " + listOfHashMap);

		System.out
		.println("\nDelete index rule in list: " + delrule_list + "\nwith size " + delrule_list.size() + "\n");

		for (int d1 = 0; d1 < listOfHashMap.size(); d1++) {
			for (int dep = 0; dep < listOfHashMap.get(d1).size(); dep++) {

				outer: for (int d = 0; d < delrule_list.size(); d++) {

					String[] splitKeepIndex = null;

					splitKeepIndex = delrule_list.get(d).split("\\s+");

					if (dep > 0 && dep < (listOfHashMap.get(d1).size() - 1)) {
						if (listOfHashMap.get(d1).get(0).equals(splitKeepIndex[6])
								&& !(splitKeepIndex[3].equals("EString")) && !(splitKeepIndex[3].equals("EInt"))
								&& !(splitKeepIndex[3].equals("EFloat"))
								&& splitKeepIndex[7].equals(listOfHashMap.get(d1).get(listOfHashMap.get(d1).size() - 1))

								&& d >= 0 && d < (delrule_list.size())) {

							System.out.println("Remove " + delrule_list.get(d) + " from delete index array.");
							delrule_list.remove(d);
							break outer;
						}
					}

				}
			}

		}
		System.out.println("\nCurrent Delete index rule in list: " + delrule_list + "\n");

		ArrayList<EtlModule> deletebinding = deleteBindingsAndRule(delrule_list, bestchain);

	}

	public static ArrayList<ArrayList<Integer>> showModelEClass() throws Exception {
		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);
		ArrayList<ArrayList<Integer>> countModelEClass = new ArrayList<ArrayList<Integer>>();

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm = new ArrayList<String>();
		if (chainingMt.findETL(targetMM, targetMM)) {
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			chain.add(samemm);
		}

		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
		chain_set.addAll(chain);
		chain.clear();
		chain.addAll(chain_set);
		System.out.println("Chains: " + chain);

		for (int i = 0; i < chain.size(); i++) {
			ArrayList<Integer> count = new ArrayList<Integer>();
			for (int j = 0; j < chain.get(i).size(); j++) {

				if (j + 1 < chain.get(i).size()) {
					EtlModule module1 = new EtlModule();
					System.out.println(chain.get(i).get(j));
					System.out.println(chain.get(i).get(j + 1));
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module1.parse(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
							metamodelPath + "/" + (chain.get(i).get(j + 1)))));

					String metamodelname = scriptRoot
							.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
									metamodelPath + "/" + (chain.get(i).get(j + 1))))
							.toString();
					System.out.println(metamodelname);
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}

					staticAnalyser.validate(module1);
					String intermodelpath;
					if (scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
							metamodelPath + "/" + (chain.get(i).get(j + 1)))).toString() != targetMM)
						intermodelpath = modelsRoot.resolve(
								chain.get(i).get(j + 1).substring(0, chain.get(i).get(j + 1).lastIndexOf('.')) + ".xmi")
						.toString();
					else
						intermodelpath = targetModel;

					count.add(chainingMt.calculateModelEClass(metamodelname, intermodelpath));
				}
			}
			countModelEClass.add(count);
		}

		System.out.println(countModelEClass);
		return countModelEClass;

	}

	public static ArrayList<ArrayList<EtlModule>> findTransformationRuleIndex_single() throws Exception {

		ArrayList<ArrayList<File>> newmodule = new ArrayList<ArrayList<File>>();

		ArrayList<String> bestchain = chainingMt.identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);

		List<EtlModule> listofmodule = new ArrayList<>();
		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = new ArrayList<ArrayList<String>>();

		if (chainingMt.findETL(targetMM, targetMM))
			delrule_list = chainingMt.deletetrindex1_single(targetModel, targetMM, targetModel, targetMM);
		else
			delrule_list = chainingMt.deletetrindex1_single(sourceModel, sourceMM, targetModel, targetMM);
		System.out.println("Delete index rule in list: " + delrule_list + "\n");

		delrule_list = delrule_list.stream().distinct().collect(Collectors.toList());
		System.out.println("Updated delete index array: " + delrule_list);
		int ch = 0;
		for (int d = 0; d < delrule_list.size(); d++) {
			ch++;
			for (int x = 0; x < delrule_list.get(d).size(); x++) {

				String[] splitIndex = null;

				splitIndex = delrule_list.get(d).get(x).split("\\s+");

				for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
						metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore").size(); e++) {
					EtlModule module1 = new EtlModule();
					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					module1.parse(
							scriptRoot
							.resolve(chainingMt
									.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
											metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
									.get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser.validate(module1);

					System.out
					.println("Delete statement " + splitIndex[0] + " with element " + splitIndex[1]
							+ " in transformation rule " + splitIndex[4] + " in transformation "
							+ chainingMt
							.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
									metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
							.get(e));
					String tp_target;
					int noofrules = 0;

					ArrayList<Integer> count_x = new ArrayList<Integer>();

					for (TransformationRule tr : module1.getTransformationRules()) {
						int count = 0;
						noofrules++;
						String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];

						for (int tp = 0; tp < tr.getTargetParameters().size(); tp++) {
							StatementBlock ruleblock = null;
							ruleblock = (StatementBlock) tr.getBody().getBody();
							Statement delete_stLine = null;
							tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];

							count = ruleblock.getStatements().size();

							if (tp_source.equals(splitIndex[4].split("2")[0])
									&& tp_target.equals(splitIndex[4].split("2")[1])) {
								if (!ruleblock.getStatements().isEmpty()) {
									System.out.println("Statements: " + ruleblock.getStatements());
									delete_stLine = ruleblock.getStatements()
											.remove(Integer.parseInt(splitIndex[0]) - 1);
									if (delete_stLine != null)
										System.out.println("Delete line " + delete_stLine);
									count--;
									count_x.add(count);
								}

							}

						}

						for (int cx = 0; cx < count_x.size(); cx++) {
							System.out.println(
									"Count remaining statements in " + splitIndex[4] + " rule is " + count_x.get(cx));
							if (count_x.get(cx) == 0) {
								TransformationRule removerules = module1.getTransformationRules().remove(cx);
								System.out.println("Remove rules: " + removerules);
							}
						}

						try {
							deleteTrans.add(ch + " " + module1.getSourceFile().getName());
						} catch (Exception e1) {

							e1.getMessage();
						}
						listofmodule.add(module1);
						listofmodule = listofmodule.stream().distinct().collect(Collectors.toList());
					}

				}
			}
		}
		for (String d : deleteTrans)
			System.out.println("Delete trans " + d);
		int c = 0;
		ArrayList<EtlModule> modules = new ArrayList<EtlModule>();

		c++;
		int c1 = 0;
		double totaltime = 0;

		for (int j = 0; j < bestchain.size(); j++) {
			HashMap<EtlRunConfiguration, Double> hash = null;
			double endt = 0;
			c1++;
			EtlModule module2 = new EtlModule();

			if (j + 1 < bestchain.size()) {

				for (int e = 0; e < chainingMt
						.identifyETL(metamodelPath + "/" + bestchain.get(j), metamodelPath + "/" + bestchain.get(j + 1))
						.size(); e++) {

					module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + bestchain.get(j),
							metamodelPath + "/" + (bestchain.get(j + 1))).get(e)));
					double start1 = System.currentTimeMillis();
					EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
					for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser2.validate(module2);
					endt = System.currentTimeMillis() - start1;

					for (String d : deleteTrans) {

						if (Integer.parseInt(d.split(" ")[0]) == c
								&& !d.equals(c + " " + module2.getSourceFile().getName())) {

							modules.add(module2);

						} else if (Integer.parseInt(d.split(" ")[0]) == c
								&& d.equals(c + " " + module2.getSourceFile().getName())) {
							for (EtlModule list : listofmodule) {
								System.out.println(c);
								System.out.println(list.getSourceFile().getName());
								if (d.equals(c + " " + list.getSourceFile().getName())) {
									modules.add(list);
								}

							}

						}

					}

				}
			}

		}

		int ct = 0;

		for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------\n");
			System.err.println(new EtlUnparser().unparse(module));
		}

		List<EtlModule> alloptimizedmodules_single = modules.stream().distinct().collect(Collectors.toList());

		System.out.println(alloptimizedmodules_single);

		ArrayList<EtlModule> newmodule_single = new ArrayList<EtlModule>();
		ArrayList<File> newmodule_single1 = new ArrayList<File>();
		ArrayList<ArrayList<EtlModule>> newmodule0 = new ArrayList<ArrayList<EtlModule>>();
		ArrayList<EtlModule> newlist0 = new ArrayList<EtlModule>();
		ArrayList<ArrayList<URI>> newmodule1 = new ArrayList<ArrayList<URI>>();
		ArrayList<URI> newlist1 = new ArrayList<URI>();

		ArrayList<File> newlist = new ArrayList<File>();
		for (int b = 0; b < alloptimizedmodules_single.size(); b++) {

			List<ModelDeclaration> mm1 = alloptimizedmodules_single.get(b).getDeclaredModelDeclarations();

			newmodule_single1.add(alloptimizedmodules_single.get(b).getSourceFile());

		}

		System.out.println(newmodule_single1);

		double totaltime1 = 0;
		for (int j = 0; j < bestchain.size(); j++) {
			if (j + 1 < bestchain.size()) {
				for (int e = 0; e < chainingMt
						.identifyETL(metamodelPath + "/" + bestchain.get(j), metamodelPath + "/" + bestchain.get(j + 1))
						.size(); e++) {

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
		System.out.println("Time taken to execute normal transformation chain \n" + bestchain + " is \n" + (totaltime1)
				+ " seconds.\n");

		double totaltime2 = 0;
		for (File modulefile : newmodule_single1) {
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
		System.out.println("Time taken to execute optimized transformation chain \n" + newmodule_single1 + " is \n"
				+ (totaltime2) + " seconds.\n");

		return alloptimizedmodules;

	}

	public static void optimize_equivalent_all() throws Exception {
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm = new ArrayList<String>();
		if (chainingMt.findETL(targetMM, targetMM)) {
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			chain.add(samemm);
		}

		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
		chain_set.addAll(chain);
		chain.clear();
		chain.addAll(chain_set);
		System.out.println("Chains: " + chain);

		for (int i = 0; i < chain.size(); i++) {
			for (int j = 0; j < chain.get(i).size(); j++) {
				EtlModule module1 = new EtlModule();

				if (j + 1 < chain.get(i).size()) {

					chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j)).toString());
					chainingMt.registerMM(metamodelsRoot.resolve(chain.get(i).get(j + 1)).toString());

					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module1.parse(scriptRoot.resolve(chainingMt.identifyETL1(metamodelPath + "/" + chain.get(i).get(j),
							metamodelPath + "/" + (chain.get(i).get(j + 1)))));

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

	}

	public static LinkedHashMap<String, Double> HashMapFromTextFile() {

		final LinkedHashMap<String, Double> map = new LinkedHashMap<>();
		BufferedReader br = null;

		try {

			final File file = new File(filePath);

			br = new BufferedReader(new FileReader(file));

			String line = null;

			while ((line = br.readLine()) != null) {

				final String[] parts = line.split(":");

				final String name = parts[0].trim();
				final String number = parts[1].trim();
				final double number1 = NumberUtils.toDouble(number);

				if (!name.equals("") && number1 >= 0) {
					map.put(name, number1);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (final Exception e) {
				}
				;
			}
		}

		return map;
	}

	public static final LinkedHashMap<String, Double> mt_coverage() throws Exception {
		final LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<String, Double>();

		long sumtime = 0;
		double start121 = System.currentTimeMillis();

		for (String mtl : scriptcontents) {
			if (scriptRoot.resolve(mtl).toFile().exists()) {
				EtlModule module = new EtlModule();
				module.parse(scriptRoot.resolve(mtl));
				module.getContext().setModule(module);
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();

				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnalyser.validate(module);

				List<ModelDeclaration> mm = ((EtlModule) module).getDeclaredModelDeclarations();

				String sourceMetamodel = mm.get(0).getModel().getName();
				String targetMetamodel = mm.get(1).getModel().getName();

				double cov = chainingMt.calculateMTCoverage_new1(metamodelPath + "/" + sourceMetamodel + ".ecore",
						metamodelPath + "/" + targetMetamodel + ".ecore");
				linkedHashMap.put(mtl, cov);

			}
		}
		second_Map = copyMap(linkedHashMap);
		System.out.println(second_Map);

		System.out.println("Time taken for adding all elements to hashmap = "
				+ (System.currentTimeMillis() - start121) / 1000 + " seconds.");

		File file = new File(outputFilePath);
		BufferedWriter bf = null;

		try {

			bf = new BufferedWriter(new FileWriter(file));

			for (Map.Entry<String, Double> entry : linkedHashMap.entrySet()) {

				bf.write(entry.getKey() + ":" + entry.getValue());

				bf.newLine();
			}

			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

			} catch (Exception e) {
			}
		}

		return linkedHashMap;

	}

	public static final HashMap<String, Integer> mt_complexity() throws Exception {
		LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
		for (String mtl : scriptcontents) {
			if (scriptRoot.resolve(mtl).toFile().exists()) {
				EtlModule module = new EtlModule();
				module.parse(scriptRoot.resolve(mtl));
				module.getContext().setModule(module);
				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();

				for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
					if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
						staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
					}
				}

				staticAnalyser.validate(module);

				List<ModelDeclaration> mm = ((EtlModule) module).getDeclaredModelDeclarations();

				linkedHashMap.put(mtl, chainingMt.calculateMTChain1(module));
			}
		}

		File file = new File(outputFilePath2);
		BufferedWriter bf = null;

		try {

			bf = new BufferedWriter(new FileWriter(file));

			for (Entry<String, Integer> entry : linkedHashMap.entrySet()) {

				bf.write(entry.getKey() + ":" + entry.getValue());

				bf.newLine();
			}

			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

			} catch (Exception e) {
			}
		}

		return linkedHashMap;

	}

	public static double executebestchain_opt(String sourceModel2, String sourceMM2, String targetModel2,
			String targetMM2) throws Exception {
		ArrayList<String> bestchain = new ArrayList<String>();
		bestchain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM).get(0);

		System.out.println("Chains: " + bestchain);
		double totaltime = 0;

		for (int j = 0; j < bestchain.size(); j++) {

			Path newsourcemodelpath, newtargetmodelpath;
			String newsourcemodel = null, newtargetmodel = null;
			if (j + 1 < bestchain.size()) {

				for (int ch = 0; ch < chainingMt.identifyETL(metamodelsRoot + "/" + bestchain.get(j),
						metamodelsRoot + "/" + bestchain.get(j + 1)).size(); ch++) {

					if (chainingMt.identifyETL(metamodelsRoot + "/" + bestchain.get(j),
							metamodelsRoot + "/" + bestchain.get(j + 1)).get(ch).startsWith("Optimized1_")) {
						System.out.println(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelsRoot + "/" + bestchain.get(j),
										metamodelsRoot + "/" + bestchain.get(j + 1)).get(ch)));
						if (bestchain.get(j)
								.equals(sourceMM2.substring(sourceMM2.indexOf("\\") + 1, sourceMM2.length()))
								&& !chainingMt.findETL(sourceMM2, targetMM2)) {
							newsourcemodelpath = modelsRoot.resolve(
									sourceModel2.substring(sourceModel2.indexOf("\\") + 1, sourceModel2.length()));
							newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
							newtargetmodelpath = modelsRoot
									.resolve(bestchain.get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
							newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
						} else if (!bestchain.get(j)
								.equals(sourceMM2.substring(sourceMM2.indexOf("\\") + 1, sourceMM2.length()))
								&& !chainingMt.findETL(sourceMM2, targetMM2)) {
							newsourcemodelpath = modelsRoot
									.resolve(bestchain.get(j).replaceFirst("[.][^.]+$", "") + ".xmi");
							newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
							newtargetmodelpath = modelsRoot
									.resolve(bestchain.get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
							newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

						}

						else if (chainingMt.findETL(sourceMM2, targetMM2)) {
							newsourcemodelpath = modelsRoot.resolve(
									sourceModel2.substring(sourceModel2.indexOf("\\") + 1, sourceModel2.length()));
							newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
							newtargetmodelpath = modelsRoot.resolve(
									targetModel2.substring(targetModel2.indexOf("\\") + 1, targetModel2.length()));
							newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
						}

						HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourcemodel,
								metamodelPath + "/" + bestchain.get(j), newtargetmodel,
								metamodelPath + "/" + bestchain.get(j + 1));
						for (EtlRunConfiguration key : hash.keySet()) {
							totaltime += hash.get(key);
						}
					}
				}

			}

		}
		System.out.println(
				"Time taken to execute optimized transformation chain " + bestchain + " is " + totaltime + " seconds.");
		return totaltime;
	}

	public static double executebestchain(String sourceModel2, String sourceMM2, String targetModel2, String targetMM2)
			throws Exception {
		ArrayList<String> bestchain = new ArrayList<String>();
		bestchain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM).get(0);
		System.out.println("Chains: " + bestchain);
		double totaltime = 0;

		for (int j = 0; j < bestchain.size(); j++) {

			Path newsourcemodelpath, newtargetmodelpath;
			String newsourcemodel = null, newtargetmodel = null;
			if (j + 1 < bestchain.size()) {

				for (int ch = 0; ch < chainingMt.identifyETL(metamodelsRoot + "/" + bestchain.get(j),
						metamodelsRoot + "/" + bestchain.get(j + 1)).size(); ch++) {

					chainingMt.registerMM(metamodelsRoot.resolve(bestchain.get(j)).toString());
					chainingMt.registerMM(metamodelsRoot.resolve(bestchain.get(j + 1)).toString());
					if (!chainingMt.identifyETL(metamodelsRoot + "/" + bestchain.get(j),
							metamodelsRoot + "/" + bestchain.get(j + 1)).get(ch).startsWith("Optimized1_")) {
						System.out.println(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelsRoot + "/" + bestchain.get(j),
										metamodelsRoot + "/" + bestchain.get(j + 1)).get(ch)));
						if (bestchain.get(j)
								.equals(sourceMM2.substring(sourceMM2.indexOf("\\") + 1, sourceMM2.length()))
								&& !chainingMt.findETL(sourceMM2, targetMM2)) {
							newsourcemodelpath = modelsRoot.resolve(
									sourceModel2.substring(sourceModel2.indexOf("\\") + 1, sourceModel2.length()));
							newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
							newtargetmodelpath = modelsRoot
									.resolve(bestchain.get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
							newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
						} else if (!bestchain.get(j)
								.equals(sourceMM2.substring(sourceMM2.indexOf("\\") + 1, sourceMM2.length()))
								&& !chainingMt.findETL(sourceMM2, targetMM2)) {
							newsourcemodelpath = modelsRoot
									.resolve(bestchain.get(j).replaceFirst("[.][^.]+$", "") + ".xmi");
							newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
							newtargetmodelpath = modelsRoot
									.resolve(bestchain.get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
							newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

						}

						else if (chainingMt.findETL(sourceMM2, targetMM2)) {
							newsourcemodelpath = modelsRoot.resolve(
									sourceModel2.substring(sourceModel2.indexOf("\\") + 1, sourceModel2.length()));
							newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
							newtargetmodelpath = modelsRoot.resolve(
									targetModel2.substring(targetModel2.indexOf("\\") + 1, targetModel2.length()));
							newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
						}

						HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourcemodel,
								metamodelPath + "/" + bestchain.get(j), newtargetmodel,
								metamodelPath + "/" + bestchain.get(j + 1));
						for (EtlRunConfiguration key : hash.keySet()) {
							totaltime += hash.get(key);
						}
					}
				}

			}

		}
		System.out.println(
				"Time taken to execute normal transformation chain " + bestchain + " is " + totaltime + " seconds.");
		return totaltime;
	}

	public static void executechainsofetl(String sourceModel2, String sourceMM2, String targetModel2, String targetMM2)
			throws Exception {

		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel2, sourceMM2, targetModel2, targetMM2);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel2, sourceMM2, targetModel2, targetMM2);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM2, targetMM2);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM2, targetMM2).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM2, targetMM2);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm = new ArrayList<String>();
		if (chainingMt.findETL(targetMM2, targetMM2)) {
			samemm.add(targetMM2.substring(targetMM2.indexOf("\\") + 1, targetMM2.length()));
			samemm.add(targetMM2.substring(targetMM2.indexOf("\\") + 1, targetMM2.length()));
			chain.add(samemm);
		}

		Set<ArrayList<String>> chain_set = new LinkedHashSet<ArrayList<String>>();
		chain_set.addAll(chain);
		chain.clear();
		chain.addAll(chain_set);
		System.out.println("Chains: " + chain);

		for (int i = 0; i < chain.size(); i++) {
			double totaltime = 0;

			for (int j = 0; j < chain.get(i).size(); j++) {

				Path newsourcemodelpath, newtargetmodelpath;
				String newsourcemodel = null, newtargetmodel = null;
				if (j + 1 < chain.get(i).size()) {

					if (chain.get(i).get(j).equals(sourceMM2.substring(sourceMM2.indexOf("\\") + 1, sourceMM2.length()))
							&& !chainingMt.findETL(sourceMM2, targetMM2)) {
						newsourcemodelpath = modelsRoot
								.resolve(sourceModel2.substring(sourceModel2.indexOf("\\") + 1, sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(chain.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					} else if (!chain.get(i).get(j)
							.equals(sourceMM2.substring(sourceMM2.indexOf("\\") + 1, sourceMM2.length()))
							&& !chainingMt.findETL(sourceMM2, targetMM2)) {
						newsourcemodelpath = modelsRoot
								.resolve(chain.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi");
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(chain.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

					}

					else if (chainingMt.findETL(sourceMM2, targetMM2)) {
						newsourcemodelpath = modelsRoot
								.resolve(sourceModel2.substring(sourceModel2.indexOf("\\") + 1, sourceModel2.length()));
						newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
						newtargetmodelpath = modelsRoot
								.resolve(targetModel2.substring(targetModel2.indexOf("\\") + 1, targetModel2.length()));
						newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					}

					HashMap<EtlRunConfiguration, Double> hash = chainingMt.executeETL_time(newsourcemodel,
							metamodelPath + "/" + chain.get(i).get(j), newtargetmodel,
							metamodelPath + "/" + chain.get(i).get(j + 1));
					for (EtlRunConfiguration key : hash.keySet()) {
						totaltime += hash.get(key);
					}

				}

			}
			System.out.println(
					"Time taken to execute transformation chain " + chain.get(i) + " is " + totaltime + " seconds.");
		}
	}

	public static void calculateTransformationCoverageOnOptimizedTransformation(String sourceModel, String sourceMM,
			String targetModel, String targetMM) throws Exception {

		List<ArrayList<String>> chain = createChain(sourceModel, sourceMM, targetModel, targetMM);

		List<EtlModule> listofmodule = new ArrayList<>();

		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = null;

		if (chainingMt.findETL(targetMM, targetMM))
			delrule_list = chainingMt.deletetrindex2(targetModel, targetMM, targetModel, targetMM, chain);
		else
			delrule_list = chainingMt.deletetrindex2(sourceModel, sourceMM, targetModel, targetMM, chain);

		delrule_list.stream().distinct().collect(Collectors.toList());
		System.out.println("Delete index rule in list: " + delrule_list + "\n");

		int ch = 0;

		for (int d = 0; d < delrule_list.size(); d++) {

			ch++;
			for (int x = 0; x < delrule_list.get(d).size(); x++) {

				String[] splitIndex = null;

				splitIndex = delrule_list.get(d).get(x).split("\\s+");

				EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
				for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
						metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore").size(); e++) {

					EtlModule module1 = new EtlModule();
					module1.parse(
							scriptRoot
							.resolve(chainingMt
									.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
											metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
									.get(e)));

					System.out.println("Delete statement " + splitIndex[0] + " with element " + splitIndex[1]
							+ " in transformation rule " + splitIndex[4] + " in transformation "
							+ chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
									metamodelPath + "/" + splitIndex[5].split("2")[1]).get(e));
					String tp_target;
					int noofrules = 0;

					ArrayList<Integer> count_x = new ArrayList<Integer>();
					for (TransformationRule tr : module1.getTransformationRules()) {
						int count = 0;
						noofrules++;
						String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];

						for (int tp = 0; tp < tr.getTargetParameters().size(); tp++) {
							StatementBlock ruleblock = null;
							ruleblock = (StatementBlock) tr.getBody().getBody();
							Statement delete_stLine = null;
							tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];

							count = ruleblock.getStatements().size();
							if (tp_source.equals(splitIndex[4].split("2")[0])
									&& tp_target.equals(splitIndex[4].split("2")[1])) {

								if (!ruleblock.getStatements().isEmpty()) {
									System.out.println("Statements: " + ruleblock.getStatements());
									delete_stLine = ruleblock.getStatements()
											.remove(Integer.parseInt(splitIndex[0]) - 1);
									System.out.println("Delete line " + delete_stLine);
									count--;
									count_x.add(count);
								}

							}

						}

					}

					for (int cx = 0; cx < count_x.size(); cx++) {
						System.out.println(
								"Count remaining statements in " + splitIndex[4] + " rule is " + count_x.get(cx));
						if (count_x.get(cx) == 0) {
							TransformationRule removerules = module1.getTransformationRules().remove(cx);
							System.out.println("Remove rules: " + removerules);
						}
					}

					try {
						deleteTrans.add(ch + " " + module1.getSourceFile().getName());
					} catch (Exception e1) {

						e1.getMessage();
					}

					listofmodule.add(module1);
					listofmodule.stream().distinct().collect(Collectors.toList());
				}

			}
		}

		for (String d1 : deleteTrans)
			System.out.println("Delete trans " + d1);

		int c = 0;
		List<EtlModule> modules = new ArrayList<>();
		for (int i = 0; i < chain.size(); i++) {
			c++;
			int c1 = 0;

			for (int j = 0; j < chain.get(i).size(); j++) {
				c1++;
				EtlModule module2 = new EtlModule();

				if (j + 1 < chain.get(i).size()) {

					EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();

					for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
							metamodelPath + "/" + chain.get(i).get(j + 1)).size(); e++) {

						module2.parse(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
										metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e)));
						for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser2.validate(module2);

						for (String d : deleteTrans) {

							if (Integer.parseInt(d.split(" ")[0]) == c
									&& !d.equals(c + " " + module2.getSourceFile().getName())) {
								modules.add(module2);

							} else if (Integer.parseInt(d.split(" ")[0]) == c
									&& d.equals(c + " " + module2.getSourceFile().getName())) {
								for (EtlModule list : listofmodule) {

									if (d.equals(c + " " + list.getSourceFile().getName()))
										modules.add(list);
								}

							}

						}
					}

				}

			}

		}

		for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------");
			System.err.println(new EtlUnparser().unparse(module));

		}

		ArrayList<Integer> noomoduleinchain = new ArrayList<Integer>();
		for (int x = 0; x < chain.size(); x++) {
			int noofmodule = 0;
			for (int y = 0; y < chain.get(x).size(); y++) {
				noofmodule++;

			}

			noomoduleinchain.add(noofmodule - 1);

		}

		List<ArrayList<EtlModule>> optimizedchain = new ArrayList<ArrayList<EtlModule>>();

		ArrayList<EtlModule> mod = new ArrayList<EtlModule>();

		for (Integer no : noomoduleinchain) {
			for (int m = 0; m < modules.size(); m = m + no) {
				mod = new ArrayList<EtlModule>(modules.subList(m, Math.min(modules.size(), m + no)));
				optimizedchain.add(mod);

			}
		}

		ArrayList<String> name = new ArrayList<String>();
		for (int j = 0; j < optimizedchain.size(); j++) {

			for (int i = 0; i < optimizedchain.get(j).size(); i++) {

				name.add(optimizedchain.get(j).get(i).getSourceFile().getName());
				name.stream().distinct().collect(Collectors.toList());
				FileWriter fw;

				fw = new FileWriter(scriptRoot
						.resolve("Optimized_" + optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
				fw.write(new EtlUnparser().unparse(optimizedchain.get(j).get(i)));
				fw.close();

				File file = null;
				File file1 = null;

				try {
					file = new File(scriptRoot
							.resolve("Optimized_" + optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
					file1 = new File(
							scriptRoot.resolve("Optimized1_" + optimizedchain.get(j).get(i).getSourceFile().getName())
							.toString());

					FileInputStream fstream = new FileInputStream(file);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));

					String strLine;
					int countlines = 0;

					while ((strLine = br.readLine()) != null) {
						countlines++;
						if (file1.length() == 0) {
							if (countlines <= 2) {
								bw.write(strLine + ";");

							} else
								bw.write(strLine);
							bw.newLine();
						}

					}

					bw.flush();
					bw.close();
					br.close();

					fstream.close();
				} catch (FileNotFoundException e) {

					System.out.println(e.getMessage());
				} catch (IOException e) {

					System.out.println(e.getMessage());
				}
			}

		}

		for (String nm : name) {

			scriptRoot.resolve("Optimized_" + nm).toFile().delete();

		}

	}

	public static void calculateTransformationCoverageOnOptimizedTransformation_opt() throws Exception {

		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
				System.out.println("qwerty: " + x);
				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		System.out.println("Chains: " + chain);

		List<EtlModule> modules = new ArrayList<>();

		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = null;

		delrule_list = chainingMt.deletetrindex(sourceModel, sourceMM, targetModel, targetMM);
		System.out.println("Delete index rule in list: " + delrule_list + "\n");

		int ch = 0;

		ArrayList<EtlModule> listofmodule = new ArrayList<>();
		for (int d = 0; d < delrule_list.size(); d++) {

			ch++;
			for (int x = 0; x < delrule_list.get(d).size(); x++) {
				EtlModule module1 = null;

				String[] splitIndex = null;

				splitIndex = delrule_list.get(d).get(x).split("\\s+");

				for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
						metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore").size(); e++) {

					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					module1 = new EtlModule();
					module1.parse(
							scriptRoot
							.resolve(chainingMt
									.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
											metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
									.get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser.validate(module1);

					System.out
					.println("Delete statement " + splitIndex[0] + " with element " + splitIndex[1]
							+ " in transformation rule " + splitIndex[4] + " in transformation "
							+ chainingMt
							.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
									metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
							.get(e));
					String tp_target;
					int noofrules = 0;

					ArrayList<Integer> count_x = new ArrayList<Integer>();
					for (TransformationRule tr : module1.getTransformationRules()) {
						int count = 0;
						noofrules++;
						String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];

						for (int tp = 0; tp < tr.getTargetParameters().size(); tp++) {
							StatementBlock ruleblock = null;
							ruleblock = (StatementBlock) tr.getBody().getBody();
							Statement delete_stLine = null;
							tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];

							count = ruleblock.getStatements().size();
							if (tp_source.equals(splitIndex[4].split("2")[0])
									&& tp_target.equals(splitIndex[4].split("2")[1])) {

								System.out.println("Statements: " + ruleblock.getStatements());
								delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0]) - 1);
								System.out.println("Delete line " + delete_stLine);
								count--;
								count_x.add(count);

							}

						}

					}

					for (int cx = 0; cx < count_x.size(); cx++) {
						System.out.println(
								"Count remaining statements in " + splitIndex[4] + " rule is " + count_x.get(cx));
						if (count_x.get(cx) == 0) {
							TransformationRule removerules = module1.getTransformationRules().remove(cx);
							System.out.println("Remove rules: " + removerules);
						}
					}

					try {
						deleteTrans.add(ch + " " + module1.getSourceFile().getName());
					} catch (Exception e1) {

						e1.getMessage();
					}

					listofmodule.add(module1);
					listofmodule.stream().distinct().collect(Collectors.toList());
				}
			}
		}

		for (String d : deleteTrans)
			System.out.println("Delete trans " + d);

		int c = 0;
		for (int i = 0; i < chain.size(); i++) {
			c++;
			int c1 = 0;

			for (int j = 0; j < chain.get(i).size(); j++) {
				c1++;
				EtlModule module2 = new EtlModule();

				if (j + 1 < chain.get(i).size()) {

					EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();

					for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
							metamodelPath + "/" + chain.get(i).get(j + 1)).size(); e++) {

						module2.parse(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
										metamodelPath + "/" + (chain.get(i).get(j + 1))).get(e)));
						for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser2.validate(module2);

						for (String d : deleteTrans) {

							if (Integer.parseInt(d.split(" ")[0]) == c
									&& !d.equals(c + " " + module2.getSourceFile().getName())) {
								modules.add(module2);

							} else if (Integer.parseInt(d.split(" ")[0]) == c
									&& d.equals(c + " " + module2.getSourceFile().getName())) {
								for (EtlModule list : listofmodule) {
									if (d.equals(c + " " + list.getSourceFile().getName()))
										modules.add(list);
								}

							}

						}
					}

				}

			}

		}

		ArrayList<Integer> noomoduleinchain = new ArrayList<Integer>();
		for (int x = 0; x < chain.size(); x++) {
			int noofmodule = 0;
			for (int y = 0; y < chain.get(x).size(); y++) {
				noofmodule++;

			}

			noomoduleinchain.add(noofmodule - 1);

		}

		List<ArrayList<EtlModule>> optimizedchain = new ArrayList<ArrayList<EtlModule>>();

		ArrayList<EtlModule> mod = new ArrayList<EtlModule>();

		for (Integer no : noomoduleinchain) {
			for (int m = 0; m < modules.size(); m = m + no) {
				mod = new ArrayList<EtlModule>(modules.subList(m, Math.min(modules.size(), m + no)));
				optimizedchain.add(mod);

			}
		}

		ArrayList<String> name = new ArrayList<String>();
		for (int j = 0; j < optimizedchain.size(); j++) {

			for (int i = 0; i < optimizedchain.get(j).size(); i++) {

				name.add(optimizedchain.get(j).get(i).getSourceFile().getName());
				name.stream().distinct().collect(Collectors.toList());
				FileWriter fw;

				fw = new FileWriter(scriptRoot
						.resolve("Optimized_" + optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
				fw.write(new EtlUnparser().unparse(optimizedchain.get(j).get(i)));
				fw.close();

				File file = null;
				File file1 = null;

				try {
					file = new File(scriptRoot
							.resolve("Optimized_" + optimizedchain.get(j).get(i).getSourceFile().getName()).toString());
					file1 = new File(
							scriptRoot.resolve("Optimized1_" + optimizedchain.get(j).get(i).getSourceFile().getName())
							.toString());

					FileInputStream fstream = new FileInputStream(file);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));

					String strLine;
					int countlines = 0;

					while ((strLine = br.readLine()) != null) {
						countlines++;
						if (file1.length() == 0) {
							if (countlines <= 2) {
								bw.write(strLine + ";");

							} else
								bw.write(strLine);
							bw.newLine();
						}

					}

					bw.flush();
					bw.close();
					br.close();

					fstream.close();
				} catch (FileNotFoundException e) {

					System.out.println(e.getMessage());
				} catch (IOException e) {

					System.out.println(e.getMessage());
				}
			}

		}

		for (String nm : name) {
			scriptRoot.resolve("Optimized_" + nm).toFile().delete();

		}

	}

	public static double calculateTransformationCoverage1(File f) throws Exception {

		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
				System.out.println("qwerty: " + x);
				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		System.out.println("Chains: " + chain);
		double coverage_chain = 0;
		for (int j = 0; j < chain.size(); j++) {
			coverage_chain = 1;
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
			for (int i = 0; i < chain.get(j).size(); i++) {
				double max_cov_mt = 0;
				if (i + 1 < chain.get(j).size()) {

					for (Double element : chainingMt.calculateMTCoverage_File(f)) {
						if (element > max_cov_mt) {
							max_cov_mt = element;
						}
						System.out.println(
								"\n" + "Individual coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i))
								+ " -> " + (metamodelPath + "/" + chain.get(j).get(i + 1)) + " is " + element);
					}

					System.out.println("\n" + "Maximum coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i))
							+ " -> " + (metamodelPath + "/" + chain.get(j).get(i + 1)) + " is " + max_cov_mt);
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

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}

		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm = new ArrayList<String>();
		if (chainingMt.findETL(targetMM, targetMM)) {
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			chain.add(samemm);
		}
		System.out.println("Chains: " + chain);
		double coverage_chain = 0;
		for (int j = 0; j < chain.size(); j++) {
			coverage_chain = 1;
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
			for (int i = 0; i < chain.get(j).size(); i++) {

				double max_cov_mt = 0;
				if (i + 1 < chain.get(j).size()) {

					for (Double element : chainingMt.calculateMTCoverage(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + chain.get(j).get(i + 1))) {
						if (element > max_cov_mt) {
							max_cov_mt = element;
						}
						System.out.println(
								"\n" + "Individual coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i))
								+ " -> " + (metamodelPath + "/" + chain.get(j).get(i + 1)) + " is " + element);
					}

					System.out.println("\n" + "Maximum coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i))
							+ " -> " + (metamodelPath + "/" + chain.get(j).get(i + 1)) + " is " + max_cov_mt);
					coverage_chain *= max_cov_mt;

				}

			}
			System.out.println("\nTotal coverage of chain is " + coverage_chain + "\n");
		}
		return coverage_chain;
	}

	public static ArrayList<EtlModule> deleteBindingsAndRule(ArrayList<String> delrule_list, ArrayList<String> chain)
			throws Exception {

		ArrayList<EtlModule> alloptimizedmodules = new ArrayList<EtlModule>();
		ArrayList<EtlModule> modules = new ArrayList<EtlModule>();

		System.out.println("Chains: " + chain);

		List<EtlModule> listofmodule = new ArrayList<EtlModule>();
		ArrayList<String> deleteTrans = new ArrayList<String>();

		int ch = 0;

		ch++;

		for (int tc = 0; tc < chain.size(); tc++) {

			int countdelete = 0;
			if (tc + 1 < chain.size()) {
				for (int e = 0; e < chainingMt
						.identifyETL(metamodelPath + "/" + chain.get(tc), metamodelPath + "/" + chain.get(tc + 1))
						.size(); e++) {

					EtlModule module1 = new EtlModule();
					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();

					module1.parse(scriptRoot.resolve(chainingMt
							.identifyETL(metamodelPath + "/" + chain.get(tc), metamodelPath + "/" + chain.get(tc + 1))
							.get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser.validate(module1);

					for (int x = 0; x < delrule_list.size(); x++) {

						String[] splitIndex = null;

						splitIndex = delrule_list.get(x).split("\\s+");

						if (chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc),
								metamodelPath + "/" + chain.get(tc + 1)).get(e).equals(splitIndex[7])) {

							countdelete++;

							System.out.println("Delete statement " + splitIndex[0] + " with element " + splitIndex[1]
									+ " in transformation rule " + splitIndex[6] + " in transformation "
									+ splitIndex[7]);
							String tp_target;
							int noofrules = 0;

							ArrayList<Integer> count_x = new ArrayList<Integer>();

							int noofdeletestatement = 0;
							StatementBlock ruleblock = null;
							for (TransformationRule tr : module1.getDeclaredTransformationRules()) {
								int count = 0;
								noofrules++;

								ruleblock = (StatementBlock) tr.getBody().getBody();

								count = ruleblock.getStatements().size();
								Statement delete_stLine = null;

								if (tr.getName().equals(splitIndex[6])) {
									noofdeletestatement++;
									System.out.println(tr.getName() + " " + splitIndex[6]);
									System.out.println("No. of delete statement in " + splitIndex[6] + " rule in "
											+ splitIndex[7] + " transformation is " + noofdeletestatement);

									if (Integer.parseInt(splitIndex[0]) <= count)
										delete_stLine = ruleblock.getStatements()
										.remove(Integer.parseInt(splitIndex[0]) - 1);

									System.out.println("Delete line " + delete_stLine);
									count--;
									count_x.add(count);

								}

							}

							System.out.println(count_x);
							for (int cx = 0; cx < count_x.size(); cx++) {
								System.out.println("Count remaining statements in " + splitIndex[6] + " rule is "
										+ count_x.get(cx));
								if (count_x.get(cx) == 0) {
									TransformationRule removerules = module1.getDeclaredTransformationRules()
											.remove(cx);
									System.out.println("Remove rules: " + removerules);
								}
							}

							try {
								deleteTrans.add(ch + " " + module1.getSourceFile().getName());
							} catch (Exception e1) {

								e1.getMessage();
							}

						}

					}
					listofmodule.add(module1);
					System.out
					.println(
							"No. of delete statement in transformation "
									+ chainingMt.identifyETL(metamodelPath + "/" + chain.get(tc),
											metamodelPath + "/" + chain.get(tc + 1)).get(e)
									+ " is " + countdelete);
				}

			}
		}

		int c = 0;

		c++;
		int c1 = 0;
		double totaltime = 0;
		for (int j = 0; j < chain.size(); j++) {
			HashMap<EtlRunConfiguration, Double> hash = null;
			double endt = 0;
			c1++;
			EtlModule module2 = new EtlModule();

			if (j + 1 < chain.size()) {

				for (int e = 0; e < chainingMt
						.identifyETL(metamodelPath + "/" + chain.get(j), metamodelPath + "/" + chain.get(j + 1))
						.size(); e++) {

					module2.parse(scriptRoot.resolve(chainingMt
							.identifyETL(metamodelPath + "/" + chain.get(j), metamodelPath + "/" + (chain.get(j + 1)))
							.get(e)));

					EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
					for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser2.validate(module2);

					for (String d : deleteTrans) {

						if (Integer.parseInt(d.split(" ")[0]) == c
								&& !d.equals(c + " " + module2.getSourceFile().getName())) {

							modules.add(module2);

						} else if (Integer.parseInt(d.split(" ")[0]) == c
								&& d.equals(c + " " + module2.getSourceFile().getName())) {
							for (EtlModule list : listofmodule) {
								if (d.equals(c + " " + list.getSourceFile().getName())) {

									modules.add(list);
								}

							}

						}

					}

				}

			}

		}

		modules = (ArrayList<EtlModule>) modules.stream().distinct().collect(Collectors.toList());
		for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------\n");

		}

		int ct = 0;
		ArrayList<Integer> index = new ArrayList<Integer>();
		String targetMetamodel;
		for (int m = 0; m < modules.size(); m++) {

			List<ModelDeclaration> mm1, mm2;
			String sourceMetamodel1 = null, targetMetamodel1 = null, sourceMetamodel2 = null, targetMetamodel2 = null;
			if (m + 1 < modules.size()) {
				mm1 = modules.get(m).getDeclaredModelDeclarations();
				targetMetamodel1 = mm1.get(1).getModel().getName();
				sourceMetamodel1 = mm1.get(0).getModel().getName();
				mm2 = modules.get(m + 1).getDeclaredModelDeclarations();
				targetMetamodel2 = mm2.get(1).getModel().getName();
				sourceMetamodel2 = mm2.get(0).getModel().getName();

				int start = 0;
				ct++;

				if ((metamodelsRoot.resolve(targetMetamodel1 + ".ecore")).toString().equals(targetMM)) {
					if ((metamodelsRoot.resolve(targetMetamodel1 + ".ecore")).toString()
							.equals((metamodelsRoot.resolve(targetMetamodel2 + ".ecore")).toString())
							&& (metamodelsRoot.resolve(sourceMetamodel1 + ".ecore")).toString()
							.equals((metamodelsRoot.resolve(sourceMetamodel2 + ".ecore")).toString())) {
						if (index.size() > 0)
							index.remove(index.size() - 1);
						index.add(ct + 1);
					} else
						index.add(ct);

				}
			} else if (!index.isEmpty() && !modules.isEmpty() && m == modules.size() - 1
					&& index.get(index.size() - 1) != modules.size())
				index.add(modules.size());

		}

		int start = 0;
		for (int k = 0; k < index.size(); k++) {
			ArrayList<EtlModule> x = null;
			List<ModelDeclaration> mm1 = modules.get(start).getDeclaredModelDeclarations();
			String sourceMetamodel = mm1.get(0).getModel().getName();
			if ((metamodelsRoot.resolve(sourceMetamodel + ".ecore")).toString().equals(sourceMM)) {
				x = new ArrayList<EtlModule>(modules.subList(start, index.get(k)));
				x = (ArrayList<EtlModule>) x.stream().distinct().collect(Collectors.toList());
				alloptimizedmodules.addAll(x);
			}
			start = index.get(k);
		}

		ArrayList<File> newmodule = new ArrayList<File>();
		ArrayList<ArrayList<EtlModule>> newmodule0 = new ArrayList<ArrayList<EtlModule>>();
		ArrayList<EtlModule> newlist0 = new ArrayList<EtlModule>();
		ArrayList<ArrayList<URI>> newmodule1 = new ArrayList<ArrayList<URI>>();
		ArrayList<URI> newlist1 = new ArrayList<URI>();

		ArrayList<File> newlist = new ArrayList<File>();
		for (int b = 0; b < listofmodule.size(); b++) {

			newlist0.add(listofmodule.get(b));
			newlist.add(listofmodule.get(b).getSourceFile());

		}

		System.out.println(newlist);
		newmodule0.add(newlist0);
		newmodule.addAll(newlist);
		newmodule1.add(newlist1);

		System.out.println("New module: " + newmodule);

		for (EtlModule modulefile : listofmodule) {
			EtlModule module = new EtlModule();
			module.parse(modulefile.getFile());
			FileWriter fw1 = new FileWriter(
					scriptRoot.resolve("Optimized_" + modulefile.getSourceFile().getName()).toString());
			fw1.write(new EtlUnparser().unparse(modulefile));
			fw1.close();

			File file = null;
			File file1 = null;

			try {
				file = new File(scriptRoot.resolve("Optimized_" + module.getSourceFile().getName()).toString());
				file1 = new File(scriptRoot.resolve("Optimized1_" + module.getSourceFile().getName()).toString());

				FileInputStream fstream = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));

				String strLine;
				int countlines = 0;

				while ((strLine = br.readLine()) != null) {
					countlines++;
					if (file1.length() == 0) {
						if (countlines <= 2) {
							bw.write(strLine + ";");

						} else
							bw.write(strLine);
						bw.newLine();
					}

				}

				bw.flush();
				bw.close();
				br.close();

				fstream.close();
			} catch (FileNotFoundException e) {

				System.out.println(e.getMessage());
			} catch (IOException e) {

				System.out.println(e.getMessage());
			}

		}

		double totaltime11 = 0;
		for (EtlModule modulefile : listofmodule) {
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
		System.out.println("Time taken to execute optimized transformation chain \n" + listofmodule + " is \n"
				+ (totaltime11) + " seconds.\n");

		double totaltime1 = 0;
		for (int j = 0; j < chain.size(); j++) {
			if (j + 1 < chain.size()) {
				for (int e = 0; e < chainingMt
						.identifyETL(metamodelPath + "/" + chain.get(j), metamodelPath + "/" + chain.get(j + 1))
						.size(); e++) {

					EtlModule module = new EtlModule();
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					module.parse(scriptRoot.resolve(chainingMt
							.identifyETL(metamodelPath + "/" + chain.get(j), metamodelPath + "/" + chain.get(j + 1))
							.get(e)));
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
		System.out.println("Time taken to execute normal transformation chain \n" + chain + " is \n" + (totaltime1)
				+ " seconds.\n");

		try {
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
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return alloptimizedmodules;

	}

	public static Map<String, String> HashMap_IdETL() {

		Map<String, String> map = new HashMap<String, String>();
		BufferedReader br = null;

		try {

			File file = new File(outputFilePath_identifyETL);

			br = new BufferedReader(new FileReader(file));

			String line = null;

			while ((line = br.readLine()) != null) {

				String[] parts = line.split(":");

				String name = parts[0].trim();
				String mm1 = name.split(" ")[0].trim();
				String mm2 = name.split(" ")[1].trim();
				String etlname = parts[1].trim();

				if (!name.equals("") && !etlname.equals(""))
					map.put(name, etlname);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
				;
			}
		}

		return map;
	}

	public static ArrayList<ArrayList<EtlModule>> findTransformationRuleIndex() throws Exception {

		Map<String, String> idetl = HashMap_IdETL();
		ArrayList<ArrayList<File>> newmodule = new ArrayList<ArrayList<File>>();

		List<ArrayList<String>> chain = new ArrayList<ArrayList<String>>();
		showChainFromSourceMM = chainingMt.identifyChain_source(sourceMM);
		System.out.println("Chain from source metamodel " + sourceMM + " is " + showChainFromSourceMM);
		chain = showAllChain(targetMM);

		ArrayList<String> samemm = new ArrayList<String>();
		if (chainingMt.findETL(targetMM, targetMM)) {
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			samemm.add(targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length()));
			chain.add(samemm);
		}

		System.out.println("Chains: " + chain);

		List<EtlModule> listofmodule = new ArrayList<>();
		ArrayList<String> deleteTrans = new ArrayList<String>();
		List<ArrayList<String>> delrule_list = null, keeprule_list = null;

		if (chainingMt.findETL(targetMM, targetMM)) {
			delrule_list = chainingMt.deletetrindex2(targetModel, targetMM, targetModel, targetMM, chain);

		} else {
			delrule_list = chainingMt.deletetrindex2(sourceModel, sourceMM, targetModel, targetMM, chain);

		}

		int ch = 0;
		for (int d = 0; d < delrule_list.size(); d++) {
			ch++;
			for (int x = 0; x < delrule_list.get(d).size(); x++) {

				String[] splitIndex = null;

				splitIndex = delrule_list.get(d).get(x).split("\\s+");

				EtlModule module1 = new EtlModule();
				EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();

				for (Map.Entry<String, String> me : idetl.entrySet()) {
					//					System.out.println(me.getKey().split(" ")[0] + " " + splitIndex[5].split("2")[0]);
					//					System.out.println(me.getKey().split(" ")[0].replaceAll("\\.\\w+", "").trim()
					//							.equals(splitIndex[5].split("2")[0].trim())
					//							&& me.getKey().split(" ")[1].replaceAll("\\.\\w+", "").trim()
					//									.equals(splitIndex[5].split("2")[1].trim()));
					if (me.getKey().split(" ")[0].replaceAll("\\.\\w+", "").trim()
							.equals(splitIndex[5].split("2")[0].trim())
							&& me.getKey().split(" ")[1].replaceAll("\\.\\w+", "").trim()
							.equals(splitIndex[5].split("2")[1].trim())) {
						module1.parse(scriptRoot.resolve(me.getValue()));

						for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
							if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
								staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
							}
						}
						staticAnlayser.validate(module1);

						String tp_target;
						int noofrules = 0;

						ArrayList<Integer> count_x = new ArrayList<Integer>();
						for (TransformationRule tr : module1.getTransformationRules()) {
							int count = 0;
							noofrules++;
							String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];

							for (int tp = 0; tp < tr.getTargetParameters().size(); tp++) {
								StatementBlock ruleblock = null;
								ruleblock = (StatementBlock) tr.getBody().getBody();
								Statement delete_stLine = null;
								tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];

								count = ruleblock.getStatements().size();
								if (tp_source.equals(splitIndex[4].split("2")[0])
										&& tp_target.equals(splitIndex[4].split("2")[1])) {

									if (!ruleblock.getStatements().isEmpty()) {

										if (Integer.parseInt(splitIndex[0]) <= count)
											delete_stLine = ruleblock.getStatements()
											.remove(Integer.parseInt(splitIndex[0]) - 1);

										count--;
										count_x.add(count);
									}

								}

							}

						}

						for (int cx = 0; cx < count_x.size(); cx++) {

							if (count_x.get(cx) == 0) {
								TransformationRule removerules = module1.getTransformationRules().remove(cx);

							}
						}

						try {
							deleteTrans.add(ch + " " + module1.getSourceFile().getName());
						} catch (Exception e1) {

							e1.getMessage();
						}
						listofmodule.add(module1);
						listofmodule = listofmodule.stream().distinct().collect(Collectors.toList());
					}
				}
			}
		}

		int c = 0;
		ArrayList<EtlModule> modules = new ArrayList<EtlModule>();
		for (int i = 0; i < chain.size(); i++) {
			c++;
			int c1 = 0;
			double totaltime = 0;
			for (int j = 0; j < chain.get(i).size(); j++) {
				HashMap<EtlRunConfiguration, Double> hash = null;
				double endt = 0;
				c1++;
				EtlModule module2 = new EtlModule();

				if (j + 1 < chain.get(i).size()) {

					for (Map.Entry<String, String> me : idetl.entrySet()) {

						//						System.out.println(me.getKey().split(" ")[0] + " " + chain.get(i).get(j));
						//						System.out.println(me.getKey().split(" ")[0].trim().equals(chain.get(i).get(j).trim())
						//								&& me.getKey().split(" ")[1].trim().equals(chain.get(i).get(j + 1).trim()));
						if (me.getKey().split(" ")[0].trim().equals(chain.get(i).get(j).trim())
								&& me.getKey().split(" ")[1].trim().equals(chain.get(i).get(j + 1).trim())) {
							module2.parse(scriptRoot.resolve(me.getValue()));

							//					module2.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),
							//							metamodelPath + "/" + (chain.get(i).get(j + 1))).get(0)));
							double start1 = System.currentTimeMillis();
							EtlStaticAnalyser staticAnlayser2 = new EtlStaticAnalyser();
							for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
								if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
									staticAnlayser2.getContext().setModelFactory(new SubEmfModelFactory());
								}
							}
							staticAnlayser2.validate(module2);
							endt = System.currentTimeMillis() - start1;

							for (String d : deleteTrans) {

								if (Integer.parseInt(d.split(" ")[0]) == c
										&& !d.equals(c + " " + module2.getSourceFile().getName())) {

									modules.add(module2);

								} else if (Integer.parseInt(d.split(" ")[0]) == c
										&& d.equals(c + " " + module2.getSourceFile().getName())) {
									for (EtlModule list : listofmodule) {

										if (d.equals(c + " " + list.getSourceFile().getName())) {

											modules.add(list);
										}

									}

								}

							}

						}

					}
				}
			}

		}
		// System.out.println("modules:"+modules);
		int ct = 0;
		ArrayList<Integer> index = new ArrayList<Integer>();
		String targetMetamodel;
		for (int m = 0; m < modules.size(); m++) {

			List<ModelDeclaration> mm1, mm2;
			String sourceMetamodel1 = null, targetMetamodel1 = null, sourceMetamodel2 = null, targetMetamodel2 = null;
			if (m + 1 < modules.size()) {
				mm1 = modules.get(m).getDeclaredModelDeclarations();
				targetMetamodel1 = mm1.get(1).getModel().getName();
				sourceMetamodel1 = mm1.get(0).getModel().getName();
				mm2 = modules.get(m + 1).getDeclaredModelDeclarations();
				targetMetamodel2 = mm2.get(1).getModel().getName();
				sourceMetamodel2 = mm2.get(0).getModel().getName();

				int start = 0;
				ct++;

				if ((metamodelsRoot.resolve(targetMetamodel1 + ".ecore")).toString().equals(targetMM)) {
					if ((metamodelsRoot.resolve(targetMetamodel1 + ".ecore")).toString()
							.equals((metamodelsRoot.resolve(targetMetamodel2 + ".ecore")).toString())
							&& (metamodelsRoot.resolve(sourceMetamodel1 + ".ecore")).toString()
							.equals((metamodelsRoot.resolve(sourceMetamodel2 + ".ecore")).toString())) {
						if (index.size() > 0)
							index.remove(index.size() - 1);
						index.add(ct + 1);
					} else
						index.add(ct);

				}
			} else if (!index.isEmpty() && !modules.isEmpty() && m == modules.size() - 1
					&& index.get(index.size() - 1) != modules.size())
				index.add(modules.size());

		}

		int start = 0;
		for (int k = 0; k < index.size(); k++) {
			ArrayList<EtlModule> x = null;
			List<ModelDeclaration> mm1 = modules.get(start).getDeclaredModelDeclarations();
			String sourceMetamodel = mm1.get(0).getModel().getName();
			if ((metamodelsRoot.resolve(sourceMetamodel + ".ecore")).toString().equals(sourceMM)) {
				x = new ArrayList<EtlModule>(modules.subList(start, index.get(k)));
				x = (ArrayList<EtlModule>) x.stream().distinct().collect(Collectors.toList());
				alloptimizedmodules.add(x);
			}

			start = index.get(k);
		}

		System.out.println("alloptimizedmodules:" + alloptimizedmodules);
		ArrayList<ArrayList<EtlModule>> newmodule0 = new ArrayList<ArrayList<EtlModule>>();
		ArrayList<EtlModule> newlist0 = new ArrayList<EtlModule>();
		ArrayList<ArrayList<URI>> newmodule1 = new ArrayList<ArrayList<URI>>();
		ArrayList<URI> newlist1 = new ArrayList<URI>();
		for (int a = 0; a < alloptimizedmodules.size(); a++) {
			ArrayList<File> newlist = new ArrayList<File>();
			for (int b = 0; b < alloptimizedmodules.get(a).size(); b++) {

				List<ModelDeclaration> mm1 = alloptimizedmodules.get(a).get(b).getDeclaredModelDeclarations();

				newlist0.add(alloptimizedmodules.get(a).get(b));
				newlist.add(alloptimizedmodules.get(a).get(b).getSourceFile());

			}

			// System.out.println("123: "+newlist);
			newmodule0.add(newlist0);
			newmodule.add(newlist);
			newmodule1.add(newlist1);

		}

		for (ArrayList<File> newmod : newmodule) {
			for (File modulefile : newmod) {
				EtlModule module = new EtlModule();
				module.parse(modulefile);
				FileWriter fw1 = new FileWriter(
						scriptRoot.resolve("Optimized_" + module.getSourceFile().getName()).toString());
				fw1.write(new EtlUnparser().unparse(module));
				fw1.close();

				File file = null;
				File file1 = null;

				try {
					file = new File(scriptRoot.resolve("Optimized_" + module.getSourceFile().getName()).toString());
					file1 = new File(scriptRoot.resolve("Optimized1_" + module.getSourceFile().getName()).toString());
					// System.out.println("File created");
					FileInputStream fstream = new FileInputStream(file);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file1.toString()), true));

					String strLine;
					int countlines = 0;

					while ((strLine = br.readLine()) != null) {
						countlines++;
						if (file1.length() == 0) {
							if (countlines <= 2) {
								bw.write(strLine + ";");

							} else
								bw.write(strLine);
							bw.newLine();
						}

					}

					bw.flush();
					bw.close();
					br.close();

					fstream.close();
				} catch (FileNotFoundException e) {

					System.out.println(e.getMessage());
				} catch (IOException e) {

					System.out.println(e.getMessage());
				}

			}

		}

		for (ArrayList<String> modulechains : chain) {
			double totaltime = 0;
			for (int j = 0; j < modulechains.size(); j++) {
				if (j + 1 < modulechains.size()) {

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

				}
			}
			System.out.println("Time taken to execute normal transformation chain \n" + modulechains + " is \n"
					+ (totaltime) + " seconds.\n");

		}

		for (ArrayList<File> modulechain : newmodule) {
			double totaltime = 0;
			for (File modulefile : modulechain) {
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
			System.out.println("Time taken to execute optimized transformation chain \n" + modulechain + " is \n"
					+ (totaltime) + " seconds.\n");

		}

		return alloptimizedmodules;

	}

	@SuppressWarnings("unchecked")
	public static void executeoptimizedchain() throws Exception {
		try {
			FileInputStream fis = new FileInputStream("listData");
			ObjectInputStream ois = new ObjectInputStream(fis);

			alloptimizedmodules1 = (ArrayList<ArrayList<File>>) ois.readObject();

			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}
		System.out.println(alloptimizedmodules1);
		for (int i = 0; i < alloptimizedmodules1.size(); i++) {
			double totaltime = 0;
			for (int j = 0; j < alloptimizedmodules1.get(i).size(); j++) {
				EtlModule module = new EtlModule();

				module.parse(alloptimizedmodules1.get(i).get(j));

				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();

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
			System.out.println("Time taken to execute optimized transformation chain \n" + alloptimizedmodules1.get(i)
			+ " is \n" + totaltime + " seconds.\n");
		}
	}

	public static void executenormalchain() throws Exception {

		try {
			FileInputStream fis = new FileInputStream("listData1");
			ObjectInputStream ois = new ObjectInputStream(fis);

			alloptimizedmodules2 = (ArrayList) ois.readObject();

			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}
		System.out.println(alloptimizedmodules2);
		for (int i = 0; i < alloptimizedmodules2.size(); i++) {
			double totaltime = 0;
			for (int j = 0; j < alloptimizedmodules2.get(i).size(); j++) {
				EtlModule module = new EtlModule();
				System.out.println(alloptimizedmodules2.get(i).get(j));

				module.parse(alloptimizedmodules2.get(i).get(j));

				System.out.println(module);
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
				for (EtlRunConfiguration key : hash.keySet()) {
					totaltime += hash.get(key);
				}

			}
			System.out.println("Time taken to execute normal transformation chain \n" + alloptimizedmodules1.get(i)
			+ " is \n" + totaltime + " seconds.\n");
		}
	}

	public static void executenormalchain2() throws Exception {
		ArrayList<String> normalchain = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream("listData1");
			ObjectInputStream ois = new ObjectInputStream(fis);

			normalchain = (ArrayList) ois.readObject();

			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}
		System.out.println(normalchain);
		for (int i = 0; i < normalchain.size(); i++) {
			double totaltime = 0;

			if (i + 1 < normalchain.size()) {
				EtlModule module = new EtlModule();
				System.out.println(chainingMt.identifyETL(metamodelPath + "/" + normalchain.get(i),
						metamodelPath + "/" + normalchain.get(i + 1)).get(0));

				module.parse(scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + normalchain.get(i),
						metamodelPath + "/" + normalchain.get(i + 1)).get(0)).toString());

				System.out.println(module);
				HashMap<EtlRunConfiguration, Double> hash = executeETL_time2(module);
				for (EtlRunConfiguration key : hash.keySet()) {
					totaltime += hash.get(key);
				}

			}
			System.out.println("Time taken to execute normal transformation chain \n" + normalchain + " is \n"
					+ totaltime + " seconds.\n");
		}
	}

	public static EtlRunConfiguration executeETL(IEolModule module) throws Exception {
		Path etlscript = null;

		ModelProperties modelProperties = new ModelProperties();

		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();

		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();

		StringProperties sourceProperties, targetProperties;

		if (metamodelsRoot.resolve(sourceMetamodel + ".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore", sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore",
					modelsRoot.resolve(sourceMetamodel + ".xmi").toString(), "true", "false");
		if (metamodelsRoot.resolve(targetMetamodel + ".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore", targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore",
					modelsRoot.resolve(targetMetamodel + ".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		System.out.println("Time taken to execute transformation "
				+ module.getDeclaredModelDeclarations().get(0).getModel().getName() + " -> "
				+ module.getDeclaredModelDeclarations().get(1).getModel().getName() + " = "
				+ (System.currentTimeMillis() - start) / 1000 + " seconds.");
		return runConfig;

	}

	public static HashMap<EtlRunConfiguration, Double> executeETL_time(IEolModule module) throws Exception {

		ModelProperties modelProperties = new ModelProperties();

		double start1 = System.currentTimeMillis();

		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();

		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}

		staticAnalyser.validate(module);

		double end1 = System.currentTimeMillis() - start1;
		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();

		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();

		StringProperties sourceProperties, targetProperties;

		if (metamodelsRoot.resolve(sourceMetamodel + ".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore", sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore",
					modelsRoot.resolve(sourceMetamodel + ".xmi").toString(), "true", "false");
		if (metamodelsRoot.resolve(targetMetamodel + ".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore", targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore",
					modelsRoot.resolve(targetMetamodel + ".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis() - start - end1) / 1000;
		System.out.println("Time taken to execute transformation "
				+ module.getDeclaredModelDeclarations().get(0).getModel().getName() + " -> "
				+ module.getDeclaredModelDeclarations().get(1).getModel().getName() + " = " + (endtime) + " seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}

	public static HashMap<EtlRunConfiguration, Double> executeETL_time2(IEolModule module) throws Exception {

		ModelProperties modelProperties = new ModelProperties();

		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();

		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();

		StringProperties sourceProperties, targetProperties;

		if (metamodelsRoot.resolve(sourceMetamodel + ".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore", sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore",
					modelsRoot.resolve(sourceMetamodel + ".xmi").toString(), "true", "false");
		if (metamodelsRoot.resolve(targetMetamodel + ".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore", targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore",
					modelsRoot.resolve(targetMetamodel + ".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		runConfig.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Time taken to execute transformation "
				+ module.getDeclaredModelDeclarations().get(0).getModel().getName() + " -> "
				+ module.getDeclaredModelDeclarations().get(1).getModel().getName() + " = " + (endtime) + " seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}

	public static HashMap<EtlRunConfiguration, Double> executeETL_time3(IEolModule module) throws Exception {

		ModelProperties modelProperties = new ModelProperties();

		EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();

		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
				staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		}

		staticAnalyser.validate(module);

		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();

		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();

		StringProperties sourceProperties, targetProperties;

		if (metamodelsRoot.resolve(sourceMetamodel + ".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore", sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore",
					modelsRoot.resolve(sourceMetamodel + ".xmi").toString(), "true", "false");
		if (metamodelsRoot.resolve(targetMetamodel + ".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore", targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore",
					modelsRoot.resolve(targetMetamodel + ".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();
		runConfig.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Time taken to execute transformation "
				+ module.getDeclaredModelDeclarations().get(0).getModel().getName() + " -> "
				+ module.getDeclaredModelDeclarations().get(1).getModel().getName() + " = " + (endtime) + " seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}

	public static HashMap<EtlRunConfiguration, Double> executeETL_time1(IEolModule module) throws Exception {

		ModelProperties modelProperties = new ModelProperties();

		List<ModelDeclaration> mm1 = module.getDeclaredModelDeclarations();

		String sourceMetamodel = mm1.get(0).getModel().getName();
		String targetMetamodel = mm1.get(1).getModel().getName();

		StringProperties sourceProperties, targetProperties;

		if (metamodelsRoot.resolve(sourceMetamodel + ".ecore").toString().equals(sourceMM))
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore", sourceModel, "true", "false");
		else
			sourceProperties = modelProperties.properties(sourceMetamodel,
					metamodelPath + "/" + sourceMetamodel + ".ecore",
					modelsRoot.resolve(sourceMetamodel + ".xmi").toString(), "true", "false");
		if (metamodelsRoot.resolve(targetMetamodel + ".ecore").toString().equals(targetMM))
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore", targetModel, "false", "true");
		else
			targetProperties = modelProperties.properties(targetMetamodel,
					metamodelPath + "/" + targetMetamodel + ".ecore",
					modelsRoot.resolve(targetMetamodel + ".xmi").toString(), "false", "true");

		double start = System.currentTimeMillis();
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve(((EtlModule) module).getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		double endtime = (System.currentTimeMillis() - start) / 1000;

		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}

	public static void findTransformationRuleIndex1() throws Exception {

		List<ArrayList<String>> delrule_list = null;

		delrule_list = chainingMt.deletetrindex(sourceModel, sourceMM, targetModel, targetMM);
		System.out.println("Delete index rule in list: " + delrule_list + "\n");

		for (int d = 0; d < delrule_list.size(); d++) {
			EtlModule module1 = new EtlModule();
			for (int x = 0; x < delrule_list.get(d).size(); x++) {

				StatementBlock ruleblock = null;
				String[] splitIndex = null;

				splitIndex = delrule_list.get(d).get(x).split("\\s+");

				EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();

				for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
						metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore").size(); e++) {
					module1.parse(
							scriptRoot
							.resolve(chainingMt
									.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
											metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
									.get(e)));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnlayser.validate(module1);

					System.out
					.println("Delete statement " + splitIndex[0] + " with element " + splitIndex[1]
							+ " in transformation rule " + splitIndex[4] + " in transformation "
							+ chainingMt
							.identifyETL(metamodelPath + "/" + splitIndex[5].split("2")[0] + ".ecore",
									metamodelPath + "/" + splitIndex[5].split("2")[1] + ".ecore")
							.get(e));
					String tp_target;
					int noofrules = 0;

					ArrayList<Integer> count_x = new ArrayList<Integer>();
					for (TransformationRule tr : module1.getTransformationRules()) {
						int count = 0;
						noofrules++;
						String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];

						ruleblock = (StatementBlock) tr.getBody().getBody();
						Statement delete_stLine = null;
						for (int tp = 0; tp < tr.getTargetParameters().size(); tp++) {
							tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];

							count = ruleblock.getStatements().size();
							if (tp_source.equals(splitIndex[4].split("2")[0])
									&& tp_target.equals(splitIndex[4].split("2")[1])) {

								delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0]) - 1);
								System.out.println("Delete line " + delete_stLine);
								count--;
								count_x.add(count);

							}

						}

					}

					for (int cx = 0; cx < count_x.size(); cx++) {
						System.out.println(
								"Count remaining statements in " + splitIndex[4] + " rule is " + count_x.get(cx));
						if (count_x.get(cx) == 0) {
							TransformationRule removerules = module1.getTransformationRules().remove(cx);
							System.out.println("Remove rules: " + removerules);
						}
					}

				}
			}
			System.out.println(module1.getSourceFile().getName() + "\n");
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

				EtlModule module1 = new EtlModule();

				if (i + 1 < chain.get(j).size()) {

					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + chain.get(j).get(i + 1)).size(); e++)
						module1.parse(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
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
				if (k > 0) {
					newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "") + ".xmi");
					newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
				} else {
					newsourcemodel = sourceModel;
				}

				Path newtargetmodelpath = modelsRoot.resolve(index.get(k + 1).replaceFirst("[.][^.]+$", "") + ".xmi");
				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();

				System.out.println(newsourcemodel + " " + metamodelPath + "/" + index.get(k) + " " + newtargetmodel
						+ " " + metamodelPath + "/" + index.get(k + 1));
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
				for (int e = 0; e < chainingMt
						.identifyETL(metamodelPath + "/" + bestchain.get(i), metamodelPath + "/" + bestchain.get(i + 1))
						.size(); e++)
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

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
				System.out.println("qwerty: " + x);
				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		System.out.println("Chains: " + chain);
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		for (int j = 0; j < chain.size(); j++) {
			System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");

			for (int i = 0; i < chain.get(j).size(); i++) {
				EtlModule module1 = new EtlModule();

				if (i + 1 < chain.get(j).size()) {

					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();

					for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + chain.get(j).get(i + 1)).size(); e++)
						module1.parse(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
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

					for (int e = 0; e < chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
							metamodelPath + "/" + chain.get(j).get(i + 1)).size(); e++)
						module1.parse(
								scriptRoot.resolve(chainingMt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
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

	public static List<ArrayList<String>> createChain(String sourceModel1, String sourceMM1, String targetModel1,
			String targetMM1) throws Exception {

		ArrayList<String> two = chainingMt.identifyChain_two(sourceModel1, sourceMM1, targetModel1, targetMM1);
		List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel1, sourceMM1, targetModel1, targetMM1);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM1, targetMM1);

		if (etl1) {
			ArrayList<String> x = cm.identifyETLinModels(sourceMM1, targetMM1);
			for (int id = 0; id < cm.identifyETLinModels(sourceMM1, targetMM1).size(); id++) {

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)),
						StandardCopyOption.REPLACE_EXISTING);

			}
		}
		if (!two.isEmpty())
			chain.add(two);
		ArrayList<String> samemm = new ArrayList<String>();
		if (chainingMt.findETL(targetMM1, targetMM1)) {
			samemm.add(targetMM1.substring(targetMM1.indexOf("\\") + 1, targetMM1.length()));
			samemm.add(targetMM1.substring(targetMM1.indexOf("\\") + 1, targetMM1.length()));
			chain.add(samemm);
		}
		System.out.println("Chains: " + chain);
		return chain;
	}

	public static void registerMM(String mm) {

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl());

		ResourceSet rs = new ResourceSetImpl();

		final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
		rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
		URI fileURI = URI.createFileURI(mm);
		Resource r = rs.getResource(fileURI, true);
		EObject eObject = r.getContents().get(0);
		if (eObject instanceof EPackage) {
			EPackage p = (EPackage) eObject;
			EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
		}
	}

}
