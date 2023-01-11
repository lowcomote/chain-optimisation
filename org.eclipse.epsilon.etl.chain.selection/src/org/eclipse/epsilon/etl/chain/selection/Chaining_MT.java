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

	final static String outputFilePath = "C:/Users/sahay/OneDrive/Documents/chain optimization_momot 27032022/org.eclipse.epsilon.etl.chain.optimisation/write_complexity.txt";

	ArrayList<String> modelsuse1 = new ArrayList<String>();

	public void chainMT(String sourceModel, String sourceMM, String targetModel, String targetMM) throws Exception {
		System.out.println(sourceMM.substring(11) + " -> " + targetMM.substring(11));

	}

	public ArrayList<String> identifyChain_two(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		ArrayList<String> modelsuse0 = new ArrayList<String>();
		boolean etl1 = false;
		etl1 = findETL(sourceMM, targetMM);

		if (etl1) {

			for (int id = 0; id < identifyETL(sourceMM, targetMM).size(); id++) {
				chainMT(sourceModel, sourceMM, targetModel, targetMM);
				ArrayList<String> x = identifyETL(sourceMM, targetMM);

				modelsuse0.add(sourceMM.substring(11));
				modelsuse0.add(targetMM.substring(11));

				Files.move(scriptRoot.resolve(x.get(id)), modelsRoot.resolve(x.get(id)));

			}

		}

		System.out.println("Direct Transformation: " + modelsuse0);
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

		boolean etl1 = findETL(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < identifyETL(sourceMM, targetMM).size(); id++) {
				modelsuse0.add(sourceMM.substring(11));
				modelsuse0.add(targetMM.substring(11));
				chainMT(sourceModel, sourceMM, targetModel, targetMM);

			}
		}

		else {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < contents.length; j++) {
					String intermetamodel = metamodelsRoot.resolve(contents[j]).toString();

					Path intermodelpath = genmodelsRoot.resolve(sourceMM.substring(11).replaceFirst("[.][^.]+$", "")
							+ "2" + contents[j].replaceFirst("[.][^.]+$", "") + ".xmi");
					String intermodel = intermodelpath.toAbsolutePath().toUri().toString();

					boolean s1 = findETL(sourceMM, intermetamodel);
					boolean s2 = findETL(intermetamodel, targetMM);
					boolean s3 = findETL(targetMM, intermetamodel);

					if (findETL(sourceMM, intermetamodel)) {

						for (int id = 0; id < identifyETL(sourceMM, intermetamodel).size(); id++) {
							identifyChain(sourceModel, sourceMM, intermodel, intermetamodel);

							if (s1) {
								modelsuse1.add(sourceMM.substring(11));
								modelsuse1.add(intermetamodel.substring(11));
							}

							sourceModel = intermodel;
							sourceMM = intermetamodel;

						}

					}

				}
			}

			System.out.println(modelsuse1);
			int index = 0;
			if (modelsuse1.size() > 0) {
				for (int i = 0; i < modelsuse1.size(); i++) {
					if (modelsuse1.get(i).equals(targetMM.substring(11))) {
						index = i;
					}
				}
			}
			for (int j = 0; j <= index; j++) {
				if (modelsuse1.size() > 0 && !modelsuse3.contains(modelsuse1.get(j)))
					modelsuse3.add(modelsuse1.get(j));

			}

		}

		if (!modelsuse0.isEmpty() && modelsuse0.get(modelsuse0.size() - 1).equals(targetMM.substring(11)))
			newmodelsuse.add(modelsuse0);

		for (int k = 0; k < modelsuse3.size(); k++) {

			for (int l = k + 2; l < modelsuse3.size(); l++) {
				boolean sc1 = findETL(metamodelsRoot.resolve(modelsuse3.get(k)).toString(),
						metamodelsRoot.resolve(modelsuse3.get(l)).toString());
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

		if (!modelsuse3.isEmpty())
			newmodelsuse.add(modelsuse3);

		if (!modelsuse5.isEmpty())
			newmodelsuse.add(modelsuse5);

		for (ArrayList<String> list : newmodelsuse)
			if (list.size() > 0 && !list.get(list.size() - 1).equals(targetMM.substring(11)))
				list.remove(list.size() - 1);

		return newmodelsuse;

	}

	public ArrayList<ArrayList<String>> identifyChain_all(ArrayList<String> chain, String targetMM) throws Exception {
		ArrayList<ArrayList<String>> totalchain = new ArrayList<ArrayList<String>>();
		ArrayList<String> modelsuse1 = new ArrayList<String>();
		for (int i = 0; i < chain.size() - 1; i++) {
			for (int j = i + 1; j < chain.size(); j++) {
				if (findETL(metamodelsRoot.resolve(chain.get(i)).toString(),
						metamodelsRoot.resolve(chain.get(j)).toString())) {
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
			for (int id = 0; id < identifyETL(sourceMM, targetMM).size(); id++) {
				modelsuse0.add(sourceMM.substring(11));
				modelsuse0.add(targetMM.substring(11));
				System.out.println(sourceMM + "->" + targetMM);
			}
		}

		else {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < contents.length; j++) {

					String intermetamodel = metamodelsRoot.resolve(contents[j]).toString();

					boolean s1 = findETL(sourceMM, intermetamodel);
					boolean s2 = findETL(intermetamodel, targetMM);

					for (int id = 0; id < identifyETL(sourceMM, intermetamodel).size(); id++) {
						identifyPossibleChain(sourceMM, intermetamodel);

						if (s1) {
							modelsuse1.add(sourceMM.substring(11));
							modelsuse1.add(intermetamodel.substring(11));
						}

						if (s2) {

							break;
						}

					}

				}
			}
		}
		System.out.println("List0 " + modelsuse0);
		System.out.println("List " + modelsuse1 + " has size " + modelsuse1.size());
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

				String intermetamodel1 = metamodelsRoot.resolve(contents[j]).toString();

				boolean s1 = findETL(sourceMM, intermetamodel1);

				if (s1) {

					modelsuse1.add(sourceMM.substring(11));
					for (String middle : contents)
						if (findETL(sourceMM, metamodelsRoot.resolve(middle).toString())) {
							modelsuse1.add(middle);

						}

					sourceMM = intermetamodel1;

				}

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

				String intermetamodel1 = metamodelsRoot.resolve(contents[j]).toString();

				boolean s1 = findETL(sourceMM, intermetamodel1);
				boolean s2 = findETL(intermetamodel1, targetMM);

				if (s1) {

					modelsuse1.add(sourceMM.substring(11));
					for (String middle : contents) {
						if (findETL(sourceMM, metamodelsRoot.resolve(middle).toString())) {
							modelsuse1.add(middle);

						}

					}

					sourceMM = intermetamodel1;

				}

			}
		}

		System.out.println(modelsuse1);

		return (ArrayList<String>) modelsuse1.stream().distinct().collect(Collectors.toList());

	}

	public ArrayList<String> identifyChain_target(String targetMM) throws Exception {

		for (String middle : contents) {
			if (findETL(metamodelsRoot.resolve(middle).toString(), targetMM)) {
				modelsuse1.add(middle);

				System.out.println(identifyETL(metamodelsRoot.resolve(middle).toString(), targetMM) + " transforms "
						+ metamodelsRoot.resolve(middle).toString() + " -> " + targetMM);

				identifyChain_target(metamodelsRoot.resolve(middle).toString());
			}
		}

		modelsuse1.add(targetMM.substring(11));
		return (ArrayList<String>) modelsuse1.stream().distinct().collect(Collectors.toList());

	}

	public EtlRunConfiguration executeETL(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		for (int e = 0; e < identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size(); e++)
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
		System.out.println("Time taken to execute transformation " + sourceMM + " -> " + targetMM + " = "
				+ (System.currentTimeMillis() - start) / 1000 + " seconds.");
		return runConfig;

	}

	public HashMap<EtlRunConfiguration, Double> executeETL_time(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		for (int e = 0; e < identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size(); e++)
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

		double start = System.currentTimeMillis();

		runConfig.run();
		double endtime = (System.currentTimeMillis() - start) / 1000;
		runConfig.dispose();

		System.out.println(
				"Time taken to execute transformation " + sourceMM + " -> " + targetMM + " = " + endtime + " seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}

	public HashMap<EtlRunConfiguration, Double> executeETL_time2(String sourceModel, String sourceMM,
			String targetModel, String targetMM) throws Exception {

		for (int e = 0; e < identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size(); e++)
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

		runConfig.run();
		runConfig.dispose();
		double endtime = (System.currentTimeMillis() - start) / 1000;
		System.out.println(
				"Time taken to execute transformation " + sourceMM + " -> " + targetMM + " = " + endtime + " seconds.");
		Map<EtlRunConfiguration, Double> hash = new HashMap<EtlRunConfiguration, Double>();
		hash.put(runConfig, endtime);
		return (HashMap<EtlRunConfiguration, Double>) hash;

	}

	public EtlRunConfiguration executeETL(EtlModule module) throws Exception {
		Path etlscript = null;

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

		StringProperties sourceProperties = modelProperties.properties(sourceMetamodel,
				metamodelPath + "/" + sourceMetamodel + ".ecore",
				modelsRoot.resolve(sourceMetamodel + ".xmi").toString(), "true", "false");
		StringProperties targetProperties = modelProperties.properties(targetMetamodel,
				metamodelPath + "/" + targetMetamodel + ".ecore",
				modelsRoot.resolve(targetMetamodel + ".xmi").toString(), "false", "true");

		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(scriptRoot.resolve(module.getSourceFile().getName()))
				.withModel(new EmfModel(), sourceProperties).withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre15").withProfiling().build();

		EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);

		sm1.run();

		runConfig.dispose();
		return runConfig;

	}

	public EtlRunConfiguration executeETL1(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		for (int e = 0; e < identifyETL(metamodelPath + "/" + sourceMM.substring(11).replaceFirst("[.][^.]+$", ""),
				metamodelPath + "/" + targetMM.substring(11).replaceFirst("[.][^.]+$", "")).size(); e++)
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

	public String executeEOL1(String sourceModel, String sourceMM, String code) throws Exception {

		registerMM(sourceMM);
		EolModule module = new EolModule();
		module.parse(code);

		sourceProperties = modelProperties.properties1("M1", sourceMM, sourceModel, "true", "false");

		OutputStream outputStream = new ByteArrayOutputStream();
		EmfModel emfModel = new EmfModel();

		if (sourceProperties != null)
			emfModel.load(sourceProperties);
		module.getContext().setOutputStream(new PrintStream(outputStream));
		module.getContext().getModelRepository().addModel(emfModel);

		module.execute();
		emfModel.dispose();
		String out = outputStream.toString();

		return out;

	}

	public EolRunConfiguration executeEOL4(String sourceModel, String sourceMM, String code) throws Exception {

		registerMM(sourceMM);
		EolModule module = new EolModule();
		module.parse(code);

		sourceProperties = modelProperties.properties("M1", sourceMM, sourceModel, "true", "false");

		EolRunConfiguration runConfig = EolRunConfiguration.Builder().withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code").withProfiling().build();

		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();
		runConfig.run();

		runConfig.dispose();
		return runConfig;
	}

	public EolRunConfiguration executeEOL3(String sourceModel, String sourceMM, Path code) throws Exception {

		sourceProperties = modelProperties.properties("M1", sourceMM, sourceModel, "true", "false");

		EolRunConfiguration runConfig = EolRunConfiguration.Builder().withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code").withProfiling().build();

		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();

		runConfig.dispose();
		return runConfig;

	}

	public EolRunConfiguration executeEOL2(String sourceModel, String sourceMM, Path code) throws Exception {

		EolModule module = new EolModule();
		module.parse(code);

		sourceProperties = modelProperties.properties1("M1", sourceMM, sourceModel, "true", "false");

		OutputStream outputStream = new ByteArrayOutputStream();
		EmfModel emfModel = new EmfModel();

		emfModel.load(sourceProperties);
		module.getContext().setOutputStream(new PrintStream(outputStream));
		module.getContext().getModelRepository().addModel(emfModel);

		module.execute();
		emfModel.dispose();
		String out = outputStream.toString();

		EolRunConfiguration runConfig = EolRunConfiguration.Builder().withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code").withProfiling().build();

		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();
		runConfig.run();

		runConfig.dispose();
		return runConfig;

	}

	public EolRunConfiguration executeEOL(String sourceModel, String sourceMM, Path code) throws Exception {

		EolModule module = new EolModule();
		module.parse(code);

		sourceProperties = modelProperties.properties("m", sourceMM, sourceModel, "true", "false");

		OutputStream outputStream = new ByteArrayOutputStream();
		EmfModel emfModel = new EmfModel();

		EolRunConfiguration runConfig = EolRunConfiguration.Builder().withScript(code)
				.withModel(new EmfModel(), sourceProperties)
				.withParameter("parameterPassedFromJava", "Hello from EOL code").withProfiling().build();

		EolPreExecuteConfiguration sm = new EolPreExecuteConfiguration(runConfig);
		sm.run();

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

			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				EolModelElementType type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					EolModelElementType type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					System.out.println("Transformation rule" + (i + 1) + ": " + type.getTypeName() + " to "
							+ type1.getTypeName() + "\n");

					System.out.println("Source Type: " + type.getTypeName());
					String eolcode = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
					+ "\"); //EClass.all.size.println();EAttribute.all.size.println();EReference.all.size.println();\r\n"
					+ "for(cl in type) {\r\n"
					+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
					+ "for(i in ref) {" + "cl.name.print();" + "\"'s etype \".print();"
					+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";

					String metaMM = "http://www.eclipse.org/emf/2002/Ecore";

					String sourceMM = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

					String ex = executeEOL1(sourceMM, metaMM, eolcode);

					System.out.println(ex);

					String eolcode_cl = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName() + "\");"
							+ "for(cl in type) {\r\n"
							+ "var ref = EReference.all.select(a|a.eType = cl);\r\n var cont; " + "for(i in ref) {"
							+ "cont=i.eContainer().name;\r\n} cont.print();}\r\n";

					String metaMM_cl = "http://www.eclipse.org/emf/2002/Ecore";

					String sourceMM_cl = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

					ex_cl = executeEOL1(sourceMM_cl, metaMM_cl, eolcode_cl);

					System.out.println(ex_cl);

					System.out.println("Target Type: " + type1.getTypeName());
					String eolcode1 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName() + "\");\r\n"
							+ "for(cl in type) {\r\n"
							+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
							+ "for(i in ref) {" + "cl.name.print();" + "\"'s etype \".print();"
							+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println();\r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";

					String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";

					String sourceMM1 = metamodelsRoot.resolve(type1.getModelName() + ".ecore").toString();

					String ex1 = executeEOL1(sourceMM1, metaMM1, eolcode1);

					System.out.println(ex1);

					String eolcode_cl1 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
					+ "\"); \r\n" + "for(cl in type) {\r\n"
					+ "var ref = EReference.all.select(a|a.eType = cl);\r\n var cont;" + "for(i in ref) {"
					+ "cont=i.eContainer().name;\r\n} cont.println(); }\r\n";

					String metaMM1_cl1 = "http://www.eclipse.org/emf/2002/Ecore";

					String sourceMM1_cl1 = metamodelsRoot.resolve(type1.getModelName() + ".ecore").toString();

					String ex_cl1 = executeEOL1(sourceMM1_cl1, metaMM1_cl1, eolcode_cl1);

					System.out.println(ex_cl1);

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
						System.out.println(stName.getChildren());
						System.out.println(statementName.split(" ")[0]);
						String expname = null;
						for (int l = 0; l < numberofexpression; l++) {

							System.out.println("\n" + expName.get(l).toString().split(" ")[0]);
							opName = expName.get(l).getChildren();
							if (!opName.isEmpty()) {
								totexpName = calculateExpressions(opName);
								for (int m = 0; m < totexpName.size(); m++) {
									totexpSize = totexpName.get(m).size();

									sumofoperation = sumofoperation + totexpSize;

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();
											String x = expname.substring(expname.lastIndexOf("name=") + 5)
													.split(",")[0];

											System.out.println("xgfchg " + totexpName.get(m).get(n));
											if (!totexpName.get(m).get(n).getChildren().isEmpty())
												System.out.println("child " + totexpName.get(m).get(n).getChildren());

											if (expname.indexOf("name=") > 0) {
												System.out.println("3454 " + x);

											}
										}

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
						EolModelElementType type11 = (EolModelElementType) staticAnalyser.getType(
								((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j1));
						System.out.println("Total expressions/operators used in the transformation rule "
								+ type.getTypeName() + " to " + type11.getTypeName() + ":" + totalstatement + "\n");
					}
					totalstructuratlfeatures = totalstructuratlfeatures + totalstatement;
				}

			}

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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();
											String x = expname.substring(expname.lastIndexOf("name=") + 5)
													.split(",")[0];

											EolType expr = staticAnalyser
													.getResolvedType((Expression) totexpName.get(m).get(n));
											expr_str = expr.getName().substring(expr.getName().indexOf("!") + 1);
											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {

												String eolcode_s = "var type=EClass.all;\r\n"
														+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
														+ "for(i1 in ref1) { if(i1.name==\"" + x
														+ "\") { i1.name.print();" + "\" \".print();"
														+ "cl1.name.print();" + "\" \".print();"
														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";

												String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_s = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_s = executeEOL1(sourceMM_s, metaMM_s, eolcode_s);

												String[] line_ex_s = ex_s.split(System.lineSeparator());

												if (!ex_s.isEmpty())
													for (String e : line_ex_s)
														ex_source.add(e);

												String eolcode_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") { i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.println();}\r\n}}";

												String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

												String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

												if (!ex_s1.isEmpty())
													for (String e : line_ex_s1)
														ex_source.add(e);

											}

											if (!expName_ch.isEmpty()) {
												for (int ch = 0; ch < expName_ch.size(); ch++) {
													if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
														String x1 = expName_ch.get(ch).toString()
																.substring(expname.lastIndexOf("name=") + 5)
																.split(",")[0].substring(17);

														String eolcode_s = "var type=EClass.all;\r\n"
																+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
																+ "for(i1 in ref1) { if(i1.name==\"" + x1
																+ "\") { i1.name.print();" + "\" \".print();"
																+ "cl1.name.print();" + "\" \".print();"
																+ "i1.eContainer().name.println();}\r\n}}}}\r\n";

														String metaMM_s = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_s = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_s = executeEOL1(sourceMM_s, metaMM_s, eolcode_s);

														String[] line_ex_s = ex_s.split(System.lineSeparator());

														if (!ex_s.isEmpty())
															for (String e : line_ex_s)
																ex_source.add(e);

														String eolcode_s1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1
																+ "\") { i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".print();"
																+ "i.eContainer().name.println();}\r\n}}";

														String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

														String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

														if (!ex_s1.isEmpty())
															for (String e : line_ex_s1)
																ex_source.add(e);

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
			if (ex_source.size() > 0)
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

					for (int ele = 0; ele < identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size(); ele++) {

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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];

													String eolcode_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName() + "\".print();"
															+ "\" \".print();" + "\"" + type.getModelName()
															+ "\".print();" + "\"" + 2 + "\".print();" + "\""
															+ type1.getModelName() + "\".println();" + "}\r\n}}";

													String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

													String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

													if (!ex_s1.isEmpty()) {
														for (String e : line_ex_s1) {
															ex_source.add(e + " " + module.getFile().getName());
															System.out.println(e);
														}

													}

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName()
															+ "\" \".print();" + "\".print();" + "\""
															+ type.getModelName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getModelName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_source.add(e);

														}

													}

												}

												if (!expName_ch.isEmpty()) {
													for (int ch = 0; ch < expName_ch.size(); ch++) {
														if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
															String x1 = expName_ch.get(ch).toString()
																	.substring(expname.lastIndexOf("name=") + 5)
																	.split(",")[0].substring(17);

															String eolcode_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eContainer().name.print();" + "\" \".print();"
																	+ "\"" + type.getTypeName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getTypeName()
																	+ "\" \".print();" + "\".print();" + "\""
																	+ type.getModelName() + "\".print();" + "\"" + 2
																	+ "\".print();" + "\"" + type1.getModelName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1,
																	eolcode_s1);

															String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

															if (!ex_s1.isEmpty()) {
																for (String e : line_ex_s1) {
																	ex_source.add(e + " " + module.getFile().getName());
																	System.out.println(e);
																}

															}

															String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eContainer().name.print();" + "\" \".print();"
																	+ "\"" + type.getTypeName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getTypeName()
																	+ "\" \".print();" + "\".print();" + "\""
																	+ type.getModelName() + "\".print();" + "\"" + 2
																	+ "\".print();" + "\"" + type1.getModelName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_attr_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																	metaMM__attr_s1, eolcode_attr_s1);

															String[] line_ex_attr_s1 = ex_attr_s1
																	.split(System.lineSeparator());

															if (!ex_attr_s1.isEmpty()) {
																for (String e : line_ex_attr_s1) {
																	ex_attr_source.add(e);

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
						ex_source = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
						exs = (ArrayList<String>) ex_source;

					}

				}

			}
		}
		return exs;

	}

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
		Map<String, String> idetl = HashMap_IdETL();

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

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					int c = 0, c1 = 0;
					//					for (Map.Entry<String, String> me : idetl.entrySet()) {
					//
					//						System.out.println(me.getKey().split(" ")[0].replaceAll("\\.\\w+", "").trim().equals(type.getModelName().trim())
					//								&& me.getKey().split(" ")[1].replaceAll("\\.\\w+", "").trim().equals(type1.getModelName().trim()));
					//						if (me.getKey().split(" ")[0].replaceAll("\\.\\w+", "").trim().equals(type.getModelName().trim())
					//								&& me.getKey().split(" ")[1].replaceAll("\\.\\w+", "").trim().equals(type1.getModelName().trim())) {
					//							module.parse(scriptRoot.resolve(me.getValue()));
					//					module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
					//							metamodelPath + "/" + type1.getModelName()).get(0)));

					StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
							.getBody().getBody();

					if (((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
						List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard()
								.getBody().getChildren();
						List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
						System.out.println("Guard: " + dec_guard);
					}

					for (int dop = 0; dop < module.getDeclaredOperations().size(); dop++) {

						List<List<ModuleElement>> dec_op = calculateExpressions(
								module.getDeclaredOperations().get(dop).getBody().getChildren());

						c1++;
						for (int m = 0; m < dec_op.size(); m++) {
							int totexpSize = dec_op.get(m).size();

							for (int n = 0; n < dec_op.get(m).size(); n++) {
								if (dec_op.get(m).size() > 0) {

									String expname = dec_op.get(m).get(n).toString();

									expName_ch = dec_op.get(m).get(n).getChildren();

									if (expname.indexOf("name=") > 0) {

										String x = expname.substring(expname.lastIndexOf("name=") + 5).split(",")[0];

										String eolcode_s1 = "var type=EClass.all;\r\n"
												+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
												+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c1
												+ "\".print();" + "\" \".print();" + "i.name.print();"
												+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
												+ "i.eContainer().name.print();" + "\" \".print();" + "\""
												+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();" + "\""
												+ type1.getTypeName() + "\".print();" + "\" \".print();" + "\""
												+ type.getModelName() + "\".print();" + "\"" + 2 + "\".print();" + "\""
												+ type1.getModelName() + "\".print();" + "\" \".print();" + "\""
												+ ((EtlModule) module).getTransformationRules().get(i).getName()
												+ "\".println();" + "}\r\n}}";

										String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

										String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName() + ".ecore")
												.toString();

										String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

										String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

										if (!ex_s1.isEmpty()) {
											for (String e : line_ex_s1) {
												ex_source.add(e + " " + module.getFile().getName());

											}

										}

										String eolcode_attr_s1 = "var type=EClass.all;\r\n"
												+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
												+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c1
												+ "\".print();" + "\" \".print();" + "i.name.print();"
												+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
												+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
												+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();" + "\""
												+ type1.getTypeName() + "\".print();" + "\" \".print();" + "\""
												+ type.getModelName() + "\".print();" + "\"" + 2 + "\".print();" + "\""
												+ type1.getModelName() + "\".print();" + "\" \".print();" + "\""
												+ ((EtlModule) module).getTransformationRules().get(i).getName()
												+ "\".println();" + "}\r\n}}";

										String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

										String sourceMM_attr_s1 = metamodelsRoot.resolve(type.getModelName() + ".ecore")
												.toString();

										String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
												eolcode_attr_s1);

										String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

										if (!ex_attr_s1.isEmpty()) {
											for (String e : line_ex_attr_s1) {
												ex_attr_source.add(e + " " + module.getFile().getName());

											}

										}

									}

									ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

									if (!expName_ch.isEmpty()) {
										for (int ch = 0; ch < expch.size(); ch++) {
											if (expch.get(ch).toString().indexOf("name=") > 0) {
												String x1 = expch.get(ch).toString()
														.substring(expname.lastIndexOf("name=") + 5).split(",")[0]
																.substring(17);

												String eolcode_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
														+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\"" + c1
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\".print();" + "\" \".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".print();"
														+ "\" \".print();" + "\""
														+ ((EtlModule) module).getTransformationRules().get(i).getName()
														+ "\".println();" + "}\r\n}}";

												String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

												String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

												if (!ex_s1.isEmpty()) {
													for (String e : line_ex_s1) {
														ex_source.add(e + " " + module.getFile().getName());

													}

												}

												String eolcode_attr_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
														+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\"" + c1
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\".print();" + "\" \".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".print();"
														+ "\" \".print();" + "\""
														+ ((EtlModule) module).getTransformationRules().get(i).getName()
														+ "\".println();" + "}\r\n}}";

												String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_attr_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
														eolcode_attr_s1);

												String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

												if (!ex_attr_s1.isEmpty()) {
													for (String e : line_ex_attr_s1) {
														ex_attr_source.add(e + " " + module.getFile().getName());

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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];

													String eolcode_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName() + "\".print();"
															+ "\" \".print();" + "\"" + type.getModelName()
															+ "\".print();" + "\"" + 2 + "\".print();" + "\""
															+ type1.getModelName() + "\".print();" + "\" \".print();"
															+ "\"" + ((EtlModule) module).getTransformationRules()
															.get(i).getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

													String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

													if (!ex_s1.isEmpty()) {
														for (String e : line_ex_s1) {
															ex_source.add(e + " " + module.getFile().getName());

														}

													}

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName() + "\".print();"
															+ "\" \".print();" + "\"" + type.getModelName()
															+ "\".print();" + "\"" + 2 + "\".print();" + "\""
															+ type1.getModelName() + "\".print();" + "\" \".print();"
															+ "\"" + ((EtlModule) module).getTransformationRules()
															.get(i).getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_source.add(e + " " + module.getFile().getName());

														}

													}

												}

												ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

												if (!expName_ch.isEmpty()) {
													for (int ch = 0; ch < expch.size(); ch++) {
														if (expch.get(ch).toString().indexOf("name=") > 0) {
															String x1 = expch.get(ch).toString()
																	.substring(expname.lastIndexOf("name=") + 5)
																	.split(",")[0].substring(17);

															String eolcode_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eContainer().name.print();" + "\" \".print();"
																	+ "\"" + type.getTypeName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getTypeName()
																	+ "\".print();" + "\" \".print();" + "\""
																	+ type.getModelName() + "\".print();" + "\"" + 2
																	+ "\".print();" + "\"" + type1.getModelName()
																	+ "\".print();" + "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1,
																	eolcode_s1);

															String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

															if (!ex_s1.isEmpty()) {
																for (String e : line_ex_s1) {
																	ex_source.add(e + " " + module.getFile().getName());

																}

															}

															String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eAttributeType.name.print();"
																	+ "\" \".print();" + "\"" + type.getTypeName()
																	+ "\".print();" + "\"" + 2 + "\".print();" + "\""
																	+ type1.getTypeName() + "\".print();"
																	+ "\" \".print();" + "\"" + type.getModelName()
																	+ "\".print();" + "\"" + 2 + "\".print();" + "\""
																	+ type1.getModelName() + "\".print();"
																	+ "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_attr_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																	metaMM__attr_s1, eolcode_attr_s1);

															String[] line_ex_attr_s1 = ex_attr_s1
																	.split(System.lineSeparator());

															if (!ex_attr_s1.isEmpty()) {
																for (String e : line_ex_attr_s1) {
																	ex_attr_source
																	.add(e + " " + module.getFile().getName());

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

					}

				}
				//					}
				//				}

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

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					for (int ele = 0; ele < identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size(); ele++) {
						int c = 0, c1 = 0;
						module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
								metamodelPath + "/" + type1.getModelName()).get(ele)));

						StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
								.getBody().getBody();

						if (((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
							List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard()
									.getBody().getChildren();
							List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
							System.out.println("Guard: " + dec_guard);
						}

						for (int dop = 0; dop < module.getDeclaredOperations().size(); dop++) {

							List<List<ModuleElement>> dec_op = calculateExpressions(
									module.getDeclaredOperations().get(dop).getBody().getChildren());

							c1++;
							for (int m = 0; m < dec_op.size(); m++) {
								int totexpSize = dec_op.get(m).size();

								for (int n = 0; n < dec_op.get(m).size(); n++) {
									if (dec_op.get(m).size() > 0) {

										String expname = dec_op.get(m).get(n).toString();
										System.out.println(dec_op + " " + expname);

										expName_ch = dec_op.get(m).get(n).getChildren();

										if (expname.indexOf("name=") > 0) {

											String x = expname.substring(expname.lastIndexOf("name=") + 5)
													.split(",")[0];
											System.out.println("x source: " + x);

											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
													+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c1
													+ "\".print();" + "\" \".print();" + "i.name.print();"
													+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
													+ "i.eContainer().name.print();" + "\" \".print();" + "\""
													+ type.getModelName() + "\".print();" + "\"" + 2 + "\".print();"
													+ "\"" + type1.getModelName() + "\".print();" + "\" \".print();"
													+ "\""
													+ ((EtlModule) module).getTransformationRules().get(i).getName()
													+ "\".println();" + "}\r\n}}";

											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

											String sourceMM_s1 = metamodelsRoot.resolve(type.getModelName() + ".ecore")
													.toString();

											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

											if (!ex_s1.isEmpty()) {
												for (String e : line_ex_s1) {
													ex_source.add(e + " " + module.getFile().getName());

												}

											}

											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c1
													+ "\".print();" + "\" \".print();" + "i.name.print();"
													+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
													+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
													+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
													+ "\"" + type1.getTypeName() + "\".print();" + "\" \".print();"
													+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
													+ "\".print();" + "\"" + type1.getModelName() + "\".print();"
													+ "\" \".print();" + "\""
													+ ((EtlModule) module).getTransformationRules().get(i).getName()
													+ "\".println();" + "}\r\n}}";

											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

											String sourceMM_attr_s1 = metamodelsRoot
													.resolve(type.getModelName() + ".ecore").toString();

											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
													eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

											if (!ex_attr_s1.isEmpty()) {
												for (String e : line_ex_attr_s1) {
													ex_attr_source.add(e + " " + module.getFile().getName());

												}

											}

										}

										ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

										if (!expName_ch.isEmpty()) {
											for (int ch = 0; ch < expch.size(); ch++) {
												if (expch.get(ch).toString().indexOf("name=") > 0) {
													String x1 = expch.get(ch).toString()
															.substring(expname.lastIndexOf("name=") + 5).split(",")[0]
																	.substring(17);

													System.out.println("x1 source: " + x1);

													String eolcode_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\"" + c1
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();"

															+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getModelName()
															+ "\".print();" + "\" \".print();" + "\""
															+ ((EtlModule) module).getTransformationRules().get(i)
															.getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

													String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

													if (!ex_s1.isEmpty()) {
														for (String e : line_ex_s1) {
															ex_source.add(e + " " + module.getFile().getName());

														}

													}

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\"" + c1
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eAttributeType.name.print();" + "\" \".print();"

															+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getModelName()
															+ "\".print();" + "\" \".print();" + "\""
															+ ((EtlModule) module).getTransformationRules().get(i)
															.getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_source.add(e + " " + module.getFile().getName());

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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];

													String eolcode_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();"

															+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getModelName()
															+ "\".print();" + "\" \".print();" + "\""
															+ ((EtlModule) module).getTransformationRules().get(i)
															.getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

													String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

													if (!ex_s1.isEmpty()) {
														for (String e : line_ex_s1) {
															ex_source.add(e + " " + module.getFile().getName());

														}

													}

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eAttributeType.name.print();" + "\" \".print();"

															+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getModelName()
															+ "\".print();" + "\" \".print();" + "\""
															+ ((EtlModule) module).getTransformationRules().get(i)
															.getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_source.add(e + " " + module.getFile().getName());

														}

													}

												}

												ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

												if (!expName_ch.isEmpty()) {
													for (int ch = 0; ch < expch.size(); ch++) {
														if (expch.get(ch).toString().indexOf("name=") > 0) {
															String x1 = expch.get(ch).toString()
																	.substring(expname.lastIndexOf("name=") + 5)
																	.split(",")[0].substring(17);

															String eolcode_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eContainer().name.print();" + "\" \".print();"

																	+ "\"" + type.getModelName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getModelName()
																	+ "\".print();" + "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1,
																	eolcode_s1);

															String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

															if (!ex_s1.isEmpty()) {
																for (String e : line_ex_s1) {
																	ex_source.add(e + " " + module.getFile().getName());

																}

															}

															String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eAttributeType.name.print();"
																	+ "\" \".print();"

																	+ "\"" + type.getModelName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getModelName()
																	+ "\".print();" + "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_attr_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																	metaMM__attr_s1, eolcode_attr_s1);

															String[] line_ex_attr_s1 = ex_attr_s1
																	.split(System.lineSeparator());

															if (!ex_attr_s1.isEmpty()) {
																for (String e : line_ex_attr_s1) {
																	ex_attr_source
																	.add(e + " " + module.getFile().getName());

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
						exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());

					}

				}
				System.out.println(exs);

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

			EolModelElementType type = null;
			EolModelElementType type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					for (int ele = 0; ele < identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size(); ele++) {
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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];
													System.out.println("source: " + x);

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName() + "\".print();"
															+ "\" \".print();" + "\"" + type.getModelName()
															+ "\".print();" + "\"" + 2 + "\".print();" + "\""
															+ type1.getModelName() + "\".print();" + "\" \".print();"
															+ "\"" + ((EtlModule) module).getTransformationRules()
															.get(i).getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_source.add(e + " " + module.getFile().getName());

														}

													}

												}

												ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

												if (!expch.isEmpty()) {
													for (int ch = 0; ch < expch.size(); ch++) {
														if (expch.get(ch).toString().indexOf("name=") > 0) {
															String x1 = expch.get(ch).toString()
																	.substring(expname.lastIndexOf("name=") + 5)
																	.split(",")[0].substring(17);

															System.out.println("x1 source: " + x1);

															String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eAttributeType.name.print();"
																	+ "\" \".print();" + "\"" + type.getTypeName()
																	+ "\".print();" + "\"" + 2 + "\".print();" + "\""
																	+ type1.getTypeName() + "\".print();"
																	+ "\" \".print();" + "\"" + type.getModelName()
																	+ "\".print();" + "\"" + 2 + "\".print();" + "\""
																	+ type1.getModelName() + "\".print();"
																	+ "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_attr_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																	metaMM__attr_s1, eolcode_attr_s1);

															String[] line_ex_attr_s1 = ex_attr_s1
																	.split(System.lineSeparator());

															if (!ex_attr_s1.isEmpty()) {
																for (String e : line_ex_attr_s1) {
																	ex_attr_source
																	.add(e + " " + module.getFile().getName());

																}

															}

														}
													}
												}

												ex_source.addAll(ex_attr_source);
												ex_source = (ArrayList<String>) ex_source.stream().distinct()
														.collect(Collectors.toList());
												exs = (ArrayList<String>) ex_source;

											}

										}

									}

								}

							}

						}

					}

				}
				System.out.println(exs);

			}
		}
		return exs;

	}

	public ArrayList<ModuleElement> checkattr_ref(List<ModuleElement> expName_ch) {

		ArrayList<ModuleElement> mod = new ArrayList<ModuleElement>();
		if (!expName_ch.isEmpty()) {
			for (ModuleElement exp : expName_ch) {
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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();

											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {
												String x = expname.substring(expname.lastIndexOf("name=") + 5)
														.split(",")[0];

												String eolcode_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\" \".print();" + "\".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".println();"
														+ "}\r\n}}";

												String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

												String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

												if (!ex_s1.isEmpty()) {
													for (String e : line_ex_s1) {
														ex_source.add(e);

													}

												}

												String eolcode_attr_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\" \".print();" + "\".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".println();"
														+ "}\r\n}}";

												String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_attr_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
														eolcode_attr_s1);

												String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

												if (!ex_attr_s1.isEmpty()) {
													for (String e : line_ex_attr_s1) {
														ex_attr_source.add(e);

													}

												}

											}

											if (!expName_ch.isEmpty()) {
												for (int ch = 0; ch < expName_ch.size(); ch++) {
													if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
														String x1 = expName_ch.get(ch).toString()
																.substring(expname.lastIndexOf("name=") + 5)
																.split(",")[0].substring(17);

														String eolcode_s1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\""
																+ c + "\".print();" + "\" \".print();"
																+ "i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".print();"
																+ "i.eContainer().name.print();" + "\" \".print();"
																+ "\"" + type.getTypeName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getTypeName()
																+ "\" \".print();" + "\".print();" + "\""
																+ type.getModelName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getModelName()
																+ "\".println();" + "}\r\n}}";

														String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

														String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

														if (!ex_s1.isEmpty()) {
															for (String e : line_ex_s1) {
																ex_source.add(e);

															}

														}

														String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\""
																+ c + "\".print();" + "\" \".print();"
																+ "i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".print();"
																+ "i.eContainer().name.print();" + "\" \".print();"
																+ "\"" + type.getTypeName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getTypeName()
																+ "\" \".print();" + "\".print();" + "\""
																+ type.getModelName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getModelName()
																+ "\".println();" + "}\r\n}}";

														String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_attr_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																metaMM__attr_s1, eolcode_attr_s1);

														String[] line_ex_attr_s1 = ex_attr_s1
																.split(System.lineSeparator());

														if (!ex_attr_s1.isEmpty()) {
															for (String e : line_ex_attr_s1) {
																ex_attr_source.add(e);

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

					exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
					exattr = (ArrayList<String>) ex_attr_source.stream().distinct().collect(Collectors.toList());
					exs.addAll(exattr);

				}

			}

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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();

											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {
												String x = expname.substring(expname.lastIndexOf("name=") + 5)
														.split(",")[0];

												String eolcode_s1 = "var type=EReference.all.name.asSet();\r\n"
														+ "for(i in type) { if(i==\"" + x + "\") {" + "i.println();}}";

												String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

												String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

												if (!ex_s1.isEmpty()) {
													for (String e : line_ex_s1) {
														ex_source.add(e);

													}

												}

												String eolcode_attr_s1 = "var type=EAttribute.all.name.asSet();\r\n"
														+ "for(i in type) { if(i==\"" + x + "\") {" + "i.println();}}";

												String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_attr_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
														eolcode_attr_s1);

												String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

												if (!ex_attr_s1.isEmpty()) {
													for (String e : line_ex_attr_s1) {
														ex_attr_source.add(e);

													}

												}

											}

											if (!expName_ch.isEmpty()) {
												for (int ch = 0; ch < expName_ch.size(); ch++) {
													if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
														String x1 = expName_ch.get(ch).toString()
																.substring(expname.lastIndexOf("name=") + 5)
																.split(",")[0].substring(17);

														String eolcode_s1 = "var type=EReference.all.name.asSet();\r\n"
																+ "for(i in type) { if(i==\"" + x1 + "\") {"
																+ "i.println();}}";

														String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

														String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

														if (!ex_s1.isEmpty()) {
															for (String e : line_ex_s1) {
																ex_source.add(e);

															}

														}

														String eolcode_attr_s1 = "var type=EAttribute.all.name.asSet();\r\n"
																+ "for(i in type) { if(i==\"" + x1 + "\") {"
																+ "i.println();}}";

														String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_attr_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																metaMM__attr_s1, eolcode_attr_s1);

														String[] line_ex_attr_s1 = ex_attr_s1
																.split(System.lineSeparator());

														if (!ex_attr_s1.isEmpty()) {
															for (String e : line_ex_attr_s1) {
																ex_attr_source.add(e);

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

					exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
					exattr = (ArrayList<String>) ex_attr_source.stream().distinct().collect(Collectors.toList());
					exs.addAll(exattr);

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

		ArrayList<String> ex_target = new ArrayList<String>();
		ArrayList<String> ex_target_etl = new ArrayList<String>();
		String ex_t = null;

		ArrayList<String> ex_attr_target = new ArrayList<String>();
		ArrayList<String> ext = new ArrayList<String>();
		ArrayList<Integer> newlist = new ArrayList<Integer>();

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

					String eolcode_t = "var type=EClass.all.name+EReference.all.name+EAttribute.all.name;\r\n"
							+ "for(t in type) t.println();";

					String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";

					String sourceMM_t = metamodelsRoot.resolve(type1.getModelName() + ".ecore").toString();

					ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);

					String[] line_ex_t = ex_t.split(System.lineSeparator());

					if (!ex_t.isEmpty())
						for (String e : line_ex_t)
							ex_target.add(e);

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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();

											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {
												String x = expname.substring(expname.lastIndexOf("name=") + 5)
														.split(",")[0];

												String eolcode_t1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {"
														// +"\""+ c +"\".print();"+ "\" \".print();"
														+ "i.name.print();" + "\" \".print();" + "cl.name.print();"
														+ "\" \".println();"

														+ "}\r\n}}";

												String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_t1 = metamodelsRoot
														.resolve(type1.getModelName() + ".ecore").toString();

												String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

												String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

												if (!ex_t1.isEmpty()) {
													for (String e : line_ex_t1) {

														ex_target_etl.add(e.trim());

													}

												}

												String eolcode_attr_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {"

														+ "i.name.print();" + "\" \".print();" + "cl.name.print();"
														+ "\" \".println();"

														+ "}\r\n}}";

												String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_attr_s1 = metamodelsRoot
														.resolve(type1.getModelName() + ".ecore").toString();

												String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
														eolcode_attr_s1);

												String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

												if (!ex_attr_s1.isEmpty()) {
													for (String e : line_ex_attr_s1) {

														ex_attr_target.add(e.trim());

													}

												}

											}
											ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

											if (!expch.isEmpty()) {
												for (int ch = 0; ch < expch.size(); ch++) {
													if (expch.get(ch).toString().indexOf("name=") > 0) {
														String x1 = expch.get(ch).toString()
																.substring(expname.lastIndexOf("name=") + 5)
																.split(",")[0].substring(17);
														System.out.println("x1 target: " + x1);

														String eolcode_t1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"

																+ "i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".println();"

																+ "}\r\n}}";

														String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_t1 = metamodelsRoot
																.resolve(type1.getModelName() + ".ecore").toString();

														String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

														String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

														if (!ex_t1.isEmpty()) {
															for (String e : line_ex_t1) {

																ex_target_etl.add(e.trim());

															}

														}

														String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"

																+ "i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".println();"

																+ "}\r\n}}";

														String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_attr_s1 = metamodelsRoot
																.resolve(type1.getModelName() + ".ecore").toString();

														String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																metaMM__attr_s1, eolcode_attr_s1);

														String[] line_ex_attr_s1 = ex_attr_s1
																.split(System.lineSeparator());

														if (!ex_attr_s1.isEmpty()) {
															for (String e : line_ex_attr_s1) {

																ex_attr_target.add(e.trim());

															}

														}

													}
												}
											}

											ex_target_etl.addAll(ex_attr_target);
											ext = (ArrayList<String>) ex_target_etl.stream().distinct()
													.collect(Collectors.toList());

										}

									}

								}

							}

						}

					}

				}

			}

		}

		ex_target = (ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());

		System.out.println("\nTransformation whole chain list: " + ext);
		System.out.println("\nMetamodel chain list: " + ex_target);

		ArrayList<String> list_etl = new ArrayList<String>();
		for (String x : ext) {
			list_etl.add(x.split(" ")[0]);
			list_etl.add(x.split(" ")[1]);
		}

		list_etl = (ArrayList<String>) list_etl.stream().distinct().collect(Collectors.toList());
		System.out.println("\nTransformation chain list: " + list_etl);

		for (String mcl : ex_target) {

			int c = 0;
			for (String tcl : list_etl) {
				c++;
				if (mcl.equals(tcl)) {
					newlist.add(1);
					break;
				} else if (!mcl.equals(tcl) && c == list_etl.size()) {
					newlist.add(0);
				}
			}
		}

		System.out.println("\n" + newlist);

		return newlist;

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

					String sourceMM_t = metamodelsRoot.resolve(type1.getModelName() + ".ecore").toString();

					ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);

					String[] line_ex_t = ex_t.split(System.lineSeparator());

					if (!ex_t.isEmpty())
						for (String e : line_ex_t)
							ex_target.add(e);

				}

			}

		}

		return (ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());

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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();
											String x = expname.substring(expname.lastIndexOf("name=") + 5)
													.split(",")[0];

											EolType expr = staticAnalyser
													.getResolvedType((Expression) totexpName.get(m).get(n));
											expr_str = expr.getName().substring(expr.getName().indexOf("!") + 1);
											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {

												String eolcode_t = "var type=EClass.all;\r\n"
														+ "for(cl1 in type) {if(cl1.eSuperTypes.notEmpty()){ {for(sup in cl1.eSuperTypes)var ref1=EReference.all.select(a|a.eType = sup);\r\n"
														+ "for(i1 in ref1) { if(i1.name==\"" + x
														+ "\") { i1.name.print();" + "\" \".print();"
														+ "cl1.name.print();" + "\" \".print();"
														+ "i1.eContainer().name.println();}\r\n}}}}\r\n";

												String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_t = metamodelsRoot
														.resolve(type1.getModelName() + ".ecore").toString();

												String ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);

												String[] line_ex_t = ex_t.split(System.lineSeparator());

												if (!ex_t.isEmpty())
													for (String e : line_ex_t)
														ex_target.add(e);

												String eolcode_t1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") { i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.println();}\r\n}}";

												String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_t1 = metamodelsRoot
														.resolve(type1.getModelName() + ".ecore").toString();

												String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

												String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

												if (!ex_t1.isEmpty())
													for (String e : line_ex_t1)
														ex_target.add(e);

											}

											if (!expName_ch.isEmpty()) {
												for (int ch = 0; ch < expName_ch.size(); ch++) {
													if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
														String x1 = expName_ch.get(ch).toString()
																.substring(expname.lastIndexOf("name=") + 5)
																.split(",")[0].substring(17);

														String eolcode_t = "var type=EClass.all;\r\n"
																+ "for(cl1 in type) { if(cl1.eSuperTypes.notEmpty()){for(sup in cl1.eSuperTypes){var ref1=EReference.all.select(a|a.eType = sup);\r\n"
																+ "for(i1 in ref1) { if(i1.name==\"" + x1
																+ "\") { i1.name.print();" + "\" \".print();"
																+ "cl1.name.print();" + "\" \".print();"
																+ "i1.eContainer().name.println();}\r\n}}}}\r\n";

														String metaMM_t = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_t = metamodelsRoot
																.resolve(type1.getModelName() + ".ecore").toString();

														String ex_t = executeEOL1(sourceMM_t, metaMM_t, eolcode_t);

														String[] line_ex_t = ex_t.split(System.lineSeparator());

														if (!ex_t.isEmpty())
															for (String e : line_ex_t)
																ex_target.add(e);

														String eolcode_t1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1
																+ "\") { i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".print();"
																+ "i.eContainer().name.println();}\r\n}}";

														String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_t1 = metamodelsRoot
																.resolve(type1.getModelName() + ".ecore").toString();

														String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

														String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

														if (!ex_t1.isEmpty())
															for (String e : line_ex_t1)
																ex_target.add(e);

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
			if (ex_target.size() > 0)
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

					for (int ele = 0; ele < identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size(); ele++) {
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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];

													String eolcode_t1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName() + "\".print();"
															+ "\" \".print();" + "\"" + type.getModelName()
															+ "\".print();" + "\"" + 2 + "\".print();" + "\""
															+ type1.getModelName() + "\".println();" + "}\r\n}}";

													String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_t1 = metamodelsRoot
															.resolve(type1.getModelName() + ".ecore").toString();

													String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

													String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

													if (!ex_t1.isEmpty()) {
														for (String e : line_ex_t1) {
															ex_target.add(e + " " + module.getFile().getName());
															System.out.println(e);
														}

													}

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eContainer().name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName()
															+ "\" \".print();" + "\".print();" + "\""
															+ type.getModelName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getModelName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_target.add(e);

														}

													}

												}

												if (!expName_ch.isEmpty()) {
													for (int ch = 0; ch < expName_ch.size(); ch++) {
														if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
															String x1 = expName_ch.get(ch).toString()
																	.substring(expname.lastIndexOf("name=") + 5)
																	.split(",")[0].substring(17);

															String eolcode_t1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eContainer().name.print();" + "\" \".print();"
																	+ "\"" + type.getTypeName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getTypeName()
																	+ "\".print();" + "\" \".print();" + "\""
																	+ type.getModelName() + "\".print();" + "\"" + 2
																	+ "\".print();" + "\"" + type1.getModelName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_t1 = metamodelsRoot
																	.resolve(type1.getModelName() + ".ecore")
																	.toString();

															String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1,
																	eolcode_t1);

															String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

															if (!ex_t1.isEmpty()) {
																for (String e : line_ex_t1) {
																	ex_target.add(e + " " + module.getFile().getName());
																	System.out.println(e);
																}

															}

															String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"
																	+ "\"" + c + "\".print();" + "\" \".print();"
																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"
																	+ "i.eContainer().name.print();" + "\" \".print();"
																	+ "\"" + type.getTypeName() + "\".print();" + "\""
																	+ 2 + "\".print();" + "\"" + type1.getTypeName()
																	+ "\" \".print();" + "\".print();" + "\""
																	+ type.getModelName() + "\".print();" + "\"" + 2
																	+ "\".print();" + "\"" + type1.getModelName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_attr_s1 = metamodelsRoot
																	.resolve(type.getModelName() + ".ecore").toString();

															String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																	metaMM__attr_s1, eolcode_attr_s1);

															String[] line_ex_attr_s1 = ex_attr_s1
																	.split(System.lineSeparator());

															if (!ex_attr_s1.isEmpty()) {
																for (String e : line_ex_attr_s1) {
																	ex_attr_target.add(e);

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

						ex_target.addAll(ex_attr_target);
						ex_target = (ArrayList<String>) ex_target.stream().distinct().collect(Collectors.toList());
						ext = (ArrayList<String>) ex_target;

					}

				}

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
		Map<String, String> idetl = HashMap_IdETL();

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

			EolModelElementType type = null, type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					int c = 0, c1 = 0;
					//					for (Map.Entry<String, String> me : idetl.entrySet()) {
					//
					//						System.out.println(me.getKey().split(" ")[0].replaceAll("\\.\\w+", "").trim().equals(type.getModelName().trim())
					//								&& me.getKey().split(" ")[1].replaceAll("\\.\\w+", "").trim().equals(type1.getModelName().trim()));
					//						if (me.getKey().split(" ")[0].replaceAll("\\.\\w+", "").trim().equals(type.getModelName().trim())
					//								&& me.getKey().split(" ")[1].replaceAll("\\.\\w+", "").trim().equals(type1.getModelName().trim())) {
					//							module.parse(scriptRoot.resolve(me.getValue()));
					//					module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
					//							metamodelPath + "/" + type1.getModelName()).get(0)));

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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();

											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {
												String x = expname.substring(expname.lastIndexOf("name=") + 5)
														.split(",")[0];

												String eolcode_t1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\".print();" + "\" \".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".print();"
														+ "\" \".print();" + "\""
														+ ((EtlModule) module).getTransformationRules().get(i).getName()
														+ "\".println();" + "}\r\n}}";

												String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_t1 = metamodelsRoot
														.resolve(type1.getModelName() + ".ecore").toString();

												String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

												String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

												if (!ex_t1.isEmpty()) {
													for (String e : line_ex_t1) {
														ex_target.add(e + " " + module.getFile().getName());

													}

												}

												String eolcode_attr_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\".print();" + "\" \".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".print();"
														+ "\" \".print();" + "\""
														+ ((EtlModule) module).getTransformationRules().get(i).getName()
														+ "\".println();" + "}\r\n}}";

												String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_attr_s1 = metamodelsRoot
														.resolve(type1.getModelName() + ".ecore").toString();

												String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
														eolcode_attr_s1);

												String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

												if (!ex_attr_s1.isEmpty()) {
													for (String e : line_ex_attr_s1) {
														ex_attr_target.add(e + " " + module.getFile().getName());

													}

												}

											}

											ex_target.addAll(ex_attr_target);

										}

									}

								}

							}

						}

					}

				}
				//					}
				//				}

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

			EolModelElementType type = null, type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					for (int ele = 0; ele < identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size(); ele++) {
						int c = 0, c1 = 0;
						module.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + type.getModelName(),
								metamodelPath + "/" + type1.getModelName()).get(ele)));

						StatementBlock ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(i)
								.getBody().getBody();

						if (((EtlModule) module).getTransformationRules().get(i).getGuard() != null) {
							List<ModuleElement> guard = ((EtlModule) module).getTransformationRules().get(i).getGuard()
									.getBody().getChildren();
							List<List<ModuleElement>> dec_guard = calculateExpressions(guard);
							System.out.println("Guard: " + dec_guard);
						}

						for (int dop = 0; dop < module.getDeclaredOperations().size(); dop++) {

							List<List<ModuleElement>> dec_op = calculateExpressions(
									module.getDeclaredOperations().get(dop).getChildren());
							c1++;
							for (int m = 0; m < dec_op.size(); m++) {
								int totexpSize = dec_op.get(m).size();

								for (int n = 0; n < dec_op.get(m).size(); n++) {
									if (dec_op.get(m).size() > 0) {

										String expname = dec_op.get(m).get(n).toString();

										expName_ch = dec_op.get(m).get(n).getChildren();

										if (expname.indexOf("name=") > 0) {
											String x = expname.substring(expname.lastIndexOf("name=") + 5)
													.split(",")[0];

											String eolcode_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
													+ "for(i in ref) { if(i.name==\"" + x + "\") {"

													+ "i.name.print();" + "\" \".print();" + "cl.name.print();"
													+ "\" \".print();"

													+ "\"" + type1.getModelName() + "\".print();" + "\" \".print();"
													+ "\""
													+ ((EtlModule) module).getTransformationRules().get(i).getName()
													+ "\".println();" + "}\r\n}}";

											String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

											String sourceMM_s1 = metamodelsRoot.resolve(type1.getModelName() + ".ecore")
													.toString();

											String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

											String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

											if (!ex_s1.isEmpty()) {
												for (String e : line_ex_s1) {
													ex_target.add(e + " " + module.getFile().getName());

												}

											}

											String eolcode_attr_s1 = "var type=EClass.all;\r\n"
													+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
													+ "for(i in ref) { if(i.name==\"" + x + "\") {"

													+ "i.name.print();" + "\" \".print();" + "cl.name.print();"
													+ "\" \".print();"

													+ "\"" + type1.getModelName() + "\".print();" + "\" \".print();"
													+ "\""
													+ ((EtlModule) module).getTransformationRules().get(i).getName()
													+ "\".println();" + "}\r\n}}";

											String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

											String sourceMM_attr_s1 = metamodelsRoot
													.resolve(type1.getModelName() + ".ecore").toString();

											String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
													eolcode_attr_s1);

											String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

											if (!ex_attr_s1.isEmpty()) {
												for (String e : line_ex_attr_s1) {
													ex_attr_target.add(e + " " + module.getFile().getName());

												}

											}

										}

										ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];

													String eolcode_t1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {"

															+ "i.name.print();" + "\" \".print();" + "cl.name.print();"
															+ "\" \".print();"

															+ "\"" + type1.getModelName() + "\".print();"
															+ "\" \".print();" + "\"" + ((EtlModule) module)
															.getTransformationRules().get(i).getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_t1 = metamodelsRoot
															.resolve(type1.getModelName() + ".ecore").toString();

													String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

													String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

													if (!ex_t1.isEmpty()) {
														for (String e : line_ex_t1) {
															ex_target.add(e + " " + module.getFile().getName());

														}

													}

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {"

															+ "i.name.print();" + "\" \".print();" + "cl.name.print();"
															+ "\" \".print();"

															+ "\"" + type1.getModelName() + "\".print();"
															+ "\" \".print();" + "\"" + ((EtlModule) module)
															.getTransformationRules().get(i).getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type1.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_target.add(e + " " + module.getFile().getName());

														}

													}

												}
												ArrayList<ModuleElement> expch = checkattr_ref(expName_ch);

												if (!expch.isEmpty()) {
													for (int ch = 0; ch < expch.size(); ch++) {
														if (expch.get(ch).toString().indexOf("name=") > 0) {
															String x1 = expch.get(ch).toString()
																	.substring(expname.lastIndexOf("name=") + 5)
																	.split(",")[0].substring(17);
															System.out.println("x1 target: " + x1);

															String eolcode_t1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllReferences;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"

																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"

																	+ "\"" + type1.getModelName() + "\".print();"
																	+ "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_t1 = metamodelsRoot
																	.resolve(type1.getModelName() + ".ecore")
																	.toString();

															String ex_t1 = executeEOL1(sourceMM_t1, metaMM_t1,
																	eolcode_t1);

															String[] line_ex_t1 = ex_t1.split(System.lineSeparator());

															if (!ex_t1.isEmpty()) {
																for (String e : line_ex_t1) {
																	ex_target.add(e + " " + module.getFile().getName());

																}

															}

															String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																	+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
																	+ "for(i in ref) { if(i.name==\"" + x1 + "\") {"

																	+ "i.name.print();" + "\" \".print();"
																	+ "cl.name.print();" + "\" \".print();"

																	+ "\"" + type1.getModelName() + "\".print();"
																	+ "\" \".print();" + "\""
																	+ ((EtlModule) module).getTransformationRules()
																	.get(i).getName()
																	+ "\".println();" + "}\r\n}}";

															String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

															String sourceMM_attr_s1 = metamodelsRoot
																	.resolve(type1.getModelName() + ".ecore")
																	.toString();

															String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																	metaMM__attr_s1, eolcode_attr_s1);

															String[] line_ex_attr_s1 = ex_attr_s1
																	.split(System.lineSeparator());

															if (!ex_attr_s1.isEmpty()) {
																for (String e : line_ex_attr_s1) {
																	ex_attr_target
																	.add(e + " " + module.getFile().getName());

																}

															}

														}
													}
												}

												ex_target.addAll(ex_attr_target);
												ext = (ArrayList<String>) ex_target.stream().distinct()
														.collect(Collectors.toList());

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
		ext = (ArrayList<String>) ext.stream().distinct().collect(Collectors.toList());
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

			EolModelElementType type = null, type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				System.out.println(((EtlModule) module).getTransformationRules().get(i).getName());
				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {

					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

					for (int ele = 0; ele < identifyETL(metamodelPath + "/" + type.getModelName(),
							metamodelPath + "/" + type1.getModelName()).size(); ele++) {
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

										for (int n = 0; n < totexpName.get(m).size(); n++) {
											if (totexpName.get(m).size() > 0) {

												expname = totexpName.get(m).get(n).toString();

												expName_ch = totexpName.get(m).get(n).getChildren();

												if (expname.indexOf("name=") > 0) {
													String x = expname.substring(expname.lastIndexOf("name=") + 5)
															.split(",")[0];
													System.out.println("Target: " + x);

													String eolcode_attr_s1 = "var type=EClass.all;\r\n"
															+ "for(cl in type) {var ref=cl.eAllAttributes;\r\n"
															+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
															+ "\".print();" + "\" \".print();" + "i.name.print();"
															+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
															+ "i.eAttributeType.name.print();" + "\" \".print();" + "\""
															+ type.getTypeName() + "\".print();" + "\"" + 2
															+ "\".print();" + "\"" + type1.getTypeName() + "\".print();"
															+ "\" \".print();" + "\"" + type.getModelName()
															+ "\".print();" + "\"" + 2 + "\".print();" + "\""
															+ type1.getModelName() + "\".print();" + "\" \".print();"
															+ "\"" + ((EtlModule) module).getTransformationRules()
															.get(i).getName()
															+ "\".println();" + "}\r\n}}";

													String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

													String sourceMM_attr_s1 = metamodelsRoot
															.resolve(type1.getModelName() + ".ecore").toString();

													String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
															eolcode_attr_s1);

													String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

													if (!ex_attr_s1.isEmpty()) {
														for (String e : line_ex_attr_s1) {
															ex_attr_target.add(e + " " + module.getFile().getName());

														}

													}

												}

												ex_target.addAll(ex_attr_target);
												ex_target = (ArrayList<String>) ex_target.stream().distinct()
														.collect(Collectors.toList());
												ext = (ArrayList<String>) ex_target;
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

									for (int n = 0; n < totexpName.get(m).size(); n++) {
										if (totexpName.get(m).size() > 0) {

											expname = totexpName.get(m).get(n).toString();

											expName_ch = totexpName.get(m).get(n).getChildren();

											if (expname.indexOf("name=") > 0) {
												String x = expname.substring(expname.lastIndexOf("name=") + 5)
														.split(",")[0];

												String eolcode_t1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\" \".print();" + "\".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".println();"
														+ "}\r\n}}";

												String metaMM_t1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_t1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_s1 = executeEOL1(sourceMM_t1, metaMM_t1, eolcode_t1);

												String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

												if (!ex_s1.isEmpty()) {
													for (String e : line_ex_s1) {
														ex_source.add(e);

													}

												}

												String eolcode_attr_s1 = "var type=EClass.all;\r\n"
														+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
														+ "for(i in ref) { if(i.name==\"" + x + "\") {" + "\"" + c
														+ "\".print();" + "\" \".print();" + "i.name.print();"
														+ "\" \".print();" + "cl.name.print();" + "\" \".print();"
														+ "i.eContainer().name.print();" + "\" \".print();" + "\""
														+ type.getTypeName() + "\".print();" + "\"" + 2 + "\".print();"
														+ "\"" + type1.getTypeName() + "\" \".print();" + "\".print();"
														+ "\"" + type.getModelName() + "\".print();" + "\"" + 2
														+ "\".print();" + "\"" + type1.getModelName() + "\".println();"
														+ "}\r\n}}";

												String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

												String sourceMM_attr_s1 = metamodelsRoot
														.resolve(type.getModelName() + ".ecore").toString();

												String ex_attr_s1 = executeEOL1(sourceMM_attr_s1, metaMM__attr_s1,
														eolcode_attr_s1);

												String[] line_ex_attr_s1 = ex_attr_s1.split(System.lineSeparator());

												if (!ex_attr_s1.isEmpty()) {
													for (String e : line_ex_attr_s1) {
														ex_attr_source.add(e);

													}

												}

											}

											if (!expName_ch.isEmpty()) {
												for (int ch = 0; ch < expName_ch.size(); ch++) {
													if (expName_ch.get(ch).toString().indexOf("name=") > 0) {
														String x1 = expName_ch.get(ch).toString()
																.substring(expname.lastIndexOf("name=") + 5)
																.split(",")[0].substring(17);

														String eolcode_s1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=EReference.all.select(a|a.eType = cl);\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\""
																+ c + "\".print();" + "\" \".print();"
																+ "i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".print();"
																+ "i.eContainer().name.print();" + "\" \".print();"
																+ "\"" + type.getTypeName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getTypeName()
																+ "\" \".print();" + "\".print();" + "\""
																+ type.getModelName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getModelName()
																+ "\".println();" + "}\r\n}}";

														String metaMM_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_s1 = executeEOL1(sourceMM_s1, metaMM_s1, eolcode_s1);

														String[] line_ex_s1 = ex_s1.split(System.lineSeparator());

														if (!ex_s1.isEmpty()) {
															for (String e : line_ex_s1) {
																ex_source.add(e);

															}

														}

														String eolcode_attr_s1 = "var type=EClass.all;\r\n"
																+ "for(cl in type) {var ref=EAttribute.all.select(a|a.eType = cl);\r\n"
																+ "for(i in ref) { if(i.name==\"" + x1 + "\") {" + "\""
																+ c + "\".print();" + "\" \".print();"
																+ "i.name.print();" + "\" \".print();"
																+ "cl.name.print();" + "\" \".print();"
																+ "i.eContainer().name.print();" + "\" \".print();"
																+ "\"" + type.getTypeName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getTypeName()
																+ "\" \".print();" + "\".print();" + "\""
																+ type.getModelName() + "\".print();" + "\"" + 2
																+ "\".print();" + "\"" + type1.getModelName()
																+ "\".println();" + "}\r\n}}";

														String metaMM__attr_s1 = "http://www.eclipse.org/emf/2002/Ecore";

														String sourceMM_attr_s1 = metamodelsRoot
																.resolve(type.getModelName() + ".ecore").toString();

														String ex_attr_s1 = executeEOL1(sourceMM_attr_s1,
																metaMM__attr_s1, eolcode_attr_s1);

														String[] line_ex_attr_s1 = ex_attr_s1
																.split(System.lineSeparator());

														if (!ex_attr_s1.isEmpty()) {
															for (String e : line_ex_attr_s1) {
																ex_attr_source.add(e);

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

					exs = (ArrayList<String>) ex_source.stream().distinct().collect(Collectors.toList());
					exattr = (ArrayList<String>) ex_attr_source.stream().distinct().collect(Collectors.toList());
					exs.addAll(exattr);

				}

			}

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

				op.add(expName);

				return op;

			} else {
				op.add(opName);
				calculateExpressions(opName);

			}
		}

		return op;
	}

	public ArrayList<ArrayList<Integer>> createListChain(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);

		List<ArrayList<Integer>> indexlist = new ArrayList<ArrayList<Integer>>();
		List<ArrayList<EtlModule>> totalmodulelist = new ArrayList<ArrayList<EtlModule>>();

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
			l.add(two);
		System.out.println("\nChains: " + l);
		String getlist = null;

		for (int i = 0; i < l.size(); i++) {

			for (int j = 0; j < l.get(i).size(); j++) {
				EtlModule module1 = new EtlModule();
				ArrayList<Integer> index = new ArrayList<Integer>();
				ArrayList<EtlModule> modulelist = new ArrayList<EtlModule>();
				if ((j + 1) < l.get(i).size()) {
					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size(); e++) {
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

						index = createList(module1);
						modulelist.add(module1);
					}
				}

				indexlist.add(index);
				totalmodulelist.add(modulelist);
			}

		}
		indexlist.removeIf(p -> p.isEmpty());
		totalmodulelist.removeIf(p -> p.isEmpty());
		indexlist = indexlist.stream().distinct().collect(Collectors.toList());
		totalmodulelist = totalmodulelist.stream().distinct().collect(Collectors.toList());
		System.out.println("\n" + totalmodulelist);
		System.out.println("\n" + indexlist);

		Integer[][] arr = new Integer[indexlist.size()][];

		int i = 0;
		for (ArrayList<Integer> lt : indexlist) {
			arr[i++] = lt.toArray(new Integer[lt.size()]);
		}

		System.out.println("\nArray chain List :" + Arrays.deepToString(arr));

		return (ArrayList<ArrayList<Integer>>) indexlist;
	}

	public ArrayList<ArrayList<String>> createListChainETL(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> indexlist = new ArrayList<ArrayList<String>>();

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
			l.add(two);
		System.out.println("Chains: " + l);
		String getlist = null;

		for (int i = 0; i < l.size(); i++) {

			for (int j = 0; j < l.get(i).size(); j++) {
				EtlModule module1 = new EtlModule();
				ArrayList<String> index = new ArrayList<String>();
				if ((j + 1) < l.get(i).size()) {

					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).get(0)));

					index = trgDependency1_new2(module1);

				}

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

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);
				System.out.println("qwerty: " + x);
				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			l.add(two);
		System.out.println("Chains: " + l);

		int min = 99999;
		float max_cov = 0;
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

			float cov = 0;
			float prod = 1, sum_cov = 1;
			float tot1 = 0, tot2 = 0;

			for (int j = 0; j < l.get(i).size(); j++) {

				EtlModule module1 = new EtlModule();
				EtlModule module2 = new EtlModule();

				if (j + 1 < l.get(i).size()) {
					registerMM(metamodelsRoot.resolve(l.get(i).get(j)).toString());
					registerMM(metamodelsRoot.resolve(l.get(i).get(j + 1)).toString());
				}

				if (j + 1 < l.get(i).size()) {

					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size(); e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

					if (j + 1 < l.get(i).size() - 1) {

						for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + l.get(i).get(j + 1),
								metamodelPath + "/" + l.get(i).get(j + 2)).size(); e1++)
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j + 1),
									metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));

						total = calculateMTChain(module1);

						sum[i] = sum[i] + total;

						int sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0, sum5 = 0, sum6 = 0, sum7 = 0, sum8 = 0;
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

						staticAnalyser.validate(module1);
						staticAnalyser1.validate(module2);
						EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
						for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
							type = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

							String metaMM = "http://www.eclipse.org/emf/2002/Ecore";

							String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

							String code2 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n"
							+ "cl.eAllAttributes.size().asString().println();\r\n}";

							String code20 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n"
							+ "cl.eAllReferences.size().asString().println();\r\n}";

							String x2 = executeEOL1(sourceMM111, metaMM, code2);
							String x20 = executeEOL1(sourceMM111, metaMM, code20);

							int i1 = NumberUtils.toInt(x2.trim());
							int i2 = NumberUtils.toInt(x20.trim());

							sum1 = sum1 + i1;
							sum2 = sum2 + i2;

							System.out.println("No. of attributes in source " + type.getTypeName() + " is " + x2);
							System.out.println("No. of references in source " + type.getTypeName() + " is " + x20);

							for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m)
									.getTargetParameters().size(); n++) {

								ArrayList<String> src_dep = srcDependency1(module1);
								ArrayList<String> trg_dep = trgDependency1(module1);

								for (int gtr = 0; gtr < ((EtlModule) module1).getTransformationRules().size(); gtr++) {
									for (int td = 0; td < trg_dep.size(); td++) {
										System.out.println("\nThis Target Dependency: "
												+ ((EtlModule) module1).getTransformationRules().get(gtr) + " "
												+ trg_dep.get(td));
									}
									break;
								}

								if (src_dep.size() > 0)
									System.out.println("\nSource Dependency List: (Metaclass->Etype feature rule) ");
								for (int ss = 0; ss < src_dep.size(); ss++) {
									String[] split_src = src_dep.get(ss).split("\\s+");

									if (split_src.length > 1) {

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

									}
								}

								if (trg_dep.size() > 0)
									System.out.println("\nTarget Dependency List: (Metaclass->Etype feature rule) ");
								for (int ss = 0; ss < trg_dep.size(); ss++) {
									String[] split_trg = trg_dep.get(ss).split("\\s+");

									if (split_trg.length > 1) {

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

									}

								}
								System.out.println();

								ArrayList<String> src_dep1 = srcDependency1(module2);
								ArrayList<String> trg_dep1 = trgDependency1(module2);

								for (int gtr = 0; gtr < ((EtlModule) module2).getTransformationRules().size(); gtr++) {
									for (int sd = 0; sd < src_dep1.size(); sd++) {
										System.out.println("\nNext Source Dependency: "
												+ ((EtlModule) module2).getTransformationRules().get(gtr) + " "
												+ src_dep1.get(sd));
									}
									break;
								}

								if (src_dep1.size() > 0)
									System.out.println("\nSource Dependency List: (Metaclass->Etype feature rule) ");
								for (int ss = 0; ss < src_dep1.size(); ss++) {
									String[] split_src = src_dep1.get(ss).split("\\s+");

									if (split_src.length > 1) {

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

									}
								}

								if (trg_dep1.size() > 0)
									System.out.println("\nTarget Dependency List: (Metaclass->Etype feature rule) ");
								for (int ss = 0; ss < trg_dep1.size(); ss++) {
									String[] split_trg = trg_dep1.get(ss).split("\\s+");

									if (split_trg.length > 1) {

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

									}

								}
								System.out.println();

								boolean flag = false;
								for (int tr = 0; tr < trg_dep.size(); tr++) {
									for (int sr = 0; sr < src_dep1.size(); sr++) {
										String[] split_trg = trg_dep.get(tr).split("\\s+");
										String[] split_src = src_dep1.get(sr).split("\\s+");
										if (!(split_trg[1] + split_trg[2] + split_trg[3]).trim()
												.equals((split_src[1] + split_src[2] + split_src[3]).trim())) {

											flag = true;

										} else

											flag = false;
									}
									if (flag == true)
										System.out.println("Keep " + trg_dep.get(tr) + " for next transormation.\n");
									else
										System.out.println(trg_dep.get(tr) + " needs to be deleted.\n");
								}

								String code = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
								+ "\");\r\n" + "for(cl in type) {\r\n"
								+ "var attr = cl.eAllAttributes.name;\r\n"
								+ "for(at in attr){at.println();}\r\n}";

								String code0 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
								+ "\");\r\n" + "for(cl in type) {\r\n" + "var ref = cl.eAllReferences;\r\n"
								+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";

								String x = executeEOL1(sourceMM111, metaMM, code);
								String x0 = executeEOL1(sourceMM111, metaMM, code0);

								String[] arrOfStr = x.split("\n");

								String[] arrOfStr0 = x0.trim().split("\n");

								System.out.println("Attribute of source " + type.getTypeName() + ": " + x);
								System.out.println("Reference of source " + type.getTypeName() + ": " + x0);

								String eolcode = null, eolcode0 = null, eolcode5 = null, eolcode50 = null;
								;
								try {

									String ref_MM, ref_metaclass_MM;
									for (String r0 : arrOfStr0) {

										ref_MM = r0.trim().substring(0, r0.indexOf(":"));
										System.out.println(r0.trim().substring(0, r0.indexOf(":")));
										ref_metaclass_MM = r0.substring(r0.indexOf(":") + 1, r0.length());
										System.out.println(r0.substring(r0.indexOf(":") + 1, r0.length()));

										eolcode0 = type.getTypeName() + ".all." + ref_MM + ".println();";
										eolcode50 = type.getTypeName() + ".all." + ref_MM + ".size().println();";
									}
									for (String r : arrOfStr) {
										eolcode = type.getTypeName() + ".all." + r + ".println();";
										eolcode5 = type.getTypeName() + ".all." + r + ".size().println();";
									}

									String x5 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
											metamodelPath + "/" + l.get(i).get(j), eolcode);
									String x50 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
											metamodelPath + "/" + l.get(i).get(j), eolcode5);

									int i5 = NumberUtils.toInt(x5.trim());
									int i6 = NumberUtils.toInt(x50.trim());

									sum5 = sum5 + i5;
									sum6 = sum6 + i6;

									String ex = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
											metamodelPath + "/" + l.get(i).get(j), eolcode);
									String ex0 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
											metamodelPath + "/" + l.get(i).get(j), eolcode0);

									System.out.println("Source model's attributes: " + ex);
									System.out.println("Source model's references: " + ex0);

								} catch (Exception e) {

									System.out.println("EAttribute not given: " + e);
								}

								String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";

								String sourceMM1111 = metamodelsRoot.resolve(type1.getModelName() + ".ecore")
										.toString();

								String code3 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
								+ "\");\r\n" + "for(cl in type) {\r\n"
								+ "cl.eAllAttributes.size.println();\r\n}";

								String code30 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
								+ "\");\r\n" + "for(cl in type) {\r\n"
								+ "cl.eAllReferences.size.println();\r\n}";

								String x3 = executeEOL1(sourceMM1111, metaMM1, code3);
								String x30 = executeEOL1(sourceMM1111, metaMM1, code30);

								int i3 = NumberUtils.toInt(x3.trim());
								int i4 = NumberUtils.toInt(x30.trim());
								sum3 = sum3 + i3;
								sum4 = sum4 + i4;

								System.out.println("No. of attributes in target " + type1.getTypeName() + " is " + x3);
								System.out.println("No. of references in target " + type1.getTypeName() + " is " + x30);

								scriptRoot.resolve("newDep3" + ".eol").toFile().delete();
								scriptRoot.resolve("newDep30" + ".eol").toFile().delete();

								String code1 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
								+ "\");\r\n" + "for(cl in type) {\r\n"
								+ "var attr = cl.eAllAttributes.name;\r\n"
								+ "for(at in attr){at.println();}\r\n}";

								String code10 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
								+ "\");\r\n" + "for(cl in type) {\r\n" + "var ref = cl.eAllReferences;\r\n"
								+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";

								String x1 = executeEOL1(sourceMM1111, metaMM1, code1);
								String x10 = executeEOL1(sourceMM1111, metaMM1, code10);

								String[] arrOfStr1 = x1.split("\n");
								String[] arrOfStr10 = x10.trim().split("\n");

								System.out.println("Attribute of target " + type1.getTypeName() + ": " + x1);
								System.out.println("Reference of target " + type1.getTypeName() + ": " + x10);

								String eolcode1 = null, eolcode10 = null, eolcode6 = null, eolcode60 = null;
								try {

									String ref_MM1, ref_metaclass_MM1;
									for (String r10 : arrOfStr10) {
										ref_MM1 = r10.trim().substring(0, r10.indexOf(":"));
										System.out.println(r10.trim().substring(0, r10.indexOf(":")));
										ref_metaclass_MM1 = r10.substring(r10.indexOf(":") + 1, r10.length());
										System.out.println(r10.substring(r10.indexOf(":") + 1, r10.length()));
										eolcode10 = type1.getTypeName() + ".all." + ref_MM1 + ".println();";
										eolcode60 = type1.getTypeName() + ".all." + ref_MM1 + ".size().println();";
									}
									for (String r1 : arrOfStr1) {
										eolcode1 = type1.getTypeName() + ".all." + r1 + ".println();";
										eolcode6 = type1.getTypeName() + ".all." + r1 + ".size().println();";
									}

									String x6 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi")
											.toString(), metamodelPath + "/" + l.get(i).get(j + 1), eolcode6);
									String x60 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi")
											.toString(), metamodelPath + "/" + l.get(i).get(j + 1), eolcode60);

									int i7 = NumberUtils.toInt(x6.trim());
									int i8 = NumberUtils.toInt(x60.trim());

									sum7 = sum7 + i7;
									sum8 = sum8 + i8;

									String ex1 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi")
											.toString(), metamodelPath + "/" + l.get(i).get(j + 1), eolcode1);
									String ex10 = executeEOL1(modelsRoot
											.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi")
											.toString(), metamodelPath + "/" + l.get(i).get(j + 1), eolcode10);

									System.out.println("Target model's attributes: " + ex1);
									System.out.println("Target model's references: " + ex10);

								} catch (Exception e) {

									System.out.println("New EAttribute not given: " + e);
								}

								System.out.println("gfghf " + sum1 + " " + sum2);

								String metaMM1x = null;

								float sumtot1 = 0, sumtot2 = 0;

								int sum3x = 0, sum4x = 0, i3x = 0, i4x = 0;
								if (j + 1 < l.get(i).size() - 1) {

									for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

										type_next = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
												.getTransformationRules().get(m1).getSourceParameter());
										System.out.println("1 " + type_next.getTypeName());
										System.out.println("2 " + type1.getTypeName());
										if (type_next.getTypeName().equals(type1.getTypeName())) {

											metaMM1x = "http://www.eclipse.org/emf/2002/Ecore";

											String sourceMM1111x = metamodelsRoot
													.resolve(type_next.getModelName() + ".ecore").toString();

											String code3x = "var type = EClass.all.select(ec|ec.name = \""
													+ type1.getTypeName() + "\");\r\n" + "for(cl in type) {\r\n"
													+ "cl.eAllAttributes.size.println();\r\n}";

											String code30x = "var type = EClass.all.select(ec|ec.name = \""
													+ type1.getTypeName() + "\");\r\n" + "for(cl in type) {\r\n"
													+ "cl.eAllReferences.size.println();\r\n}";

											String x3x = executeEOL1(sourceMM1111x, metaMM1x, code3x);
											String x30x = executeEOL1(sourceMM1111x, metaMM1x, code30x);

											i3x = NumberUtils.toInt(x3x.trim());
											i4x = NumberUtils.toInt(x30x.trim());
											System.out.println("cvhgfhf " + i3x);
											System.out.println("gfyr " + i4x);
											sum3x = sum3x + i3x;
											sum4x = sum4x + i4x;

											System.out.println("No. of attributes in target " + type1.getTypeName()
											+ " is " + x3x);
											System.out.println("No. of references in target " + type1.getTypeName()
											+ " is " + x30x);

											System.out.println("sum " + (sum3x + sum4x));
											System.out.println("sum total " + (sum1 + sum2));

											tot1 = sum3x + sum4x;
											System.out.println("Tot1 " + tot1);

										}

									}

									tot2 = sum1 + sum2;
									System.out.println("Tot2 " + tot2);
									sumtot1 = sumtot1 + tot1;
									sumtot2 = sumtot2 + tot2;
									cov = sumtot1 / sumtot2;
									sum_cov = cov;

								} else
									sum_cov = 1;

							}

						}
						System.out.println("Tranformation coverage for " + type.getModelName() + " -> "
								+ type1.getModelName() + " is " + sum_cov);
						prod = prod * sum_cov;
						sum_cov_chain[i] = prod;

						System.out.println("Total attributes in source transformation type " + "in "
								+ type.getModelName() + " metamodel is " + sum1);
						System.out.println("Total references in source transformation type " + "in "
								+ type.getModelName() + " metamodel is " + sum2);
						System.out.println("Total attributes in target transformation type " + "in "
								+ type1.getModelName() + " metamodel is " + sum3);
						System.out.println("Total references in target transformation type " + "in "
								+ type1.getModelName() + " metamodel is " + sum4);
						System.out.println("\n");
						System.out.println("Total attributes in source transformation type " + "in "
								+ type.getModelName() + " model is " + sum5);
						System.out.println("Total references in source transformation type " + "in "
								+ type.getModelName() + " model is " + sum6);
						System.out.println("Total attributes in target transformation type " + "in "
								+ type1.getModelName() + " model is " + sum7);
						System.out.println("Total references in target transformation type " + "in "
								+ type1.getModelName() + " model is " + sum8);

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
			if (sum_cov_chain[i] > max_cov) {
				max_cov = sum_cov_chain[i];
				index1 = l.get(i);
			}

			System.out.println("Tranformation coverage for chain " + l.get(i) + " is " + prod);

			System.out.println("Total transformed source structural features in metamodel = "
					+ (sum_source_attributeMM + sum_source_referenceMM));
			System.out.println("Total transformed target structural features in metamodel = "
					+ (sum_target_attributeMM + sum_target_referenceMM));
			System.out.println("Total transformed source structural features in model = "
					+ (sum_source_attributeModel + sum_source_referenceModel));
			System.out.println("Total transformed target structural features in model = "
					+ (sum_target_attributeModel + sum_target_referenceModel));
			System.out.println("\n");
			System.out.println("Total expressions/operators used in the chain: " + sum[i]);
			System.out.println("---------------------------------------------------------\n");

		}

		System.out.println("\nMT Chain " + index1 + " has maximum transformation coverage of " + max_cov);
		System.out.println("\nMT Chain " + index + " has minimum structural features of " + min);

		return index;

	}

	public List<ArrayList<String>> deletetrindex1_single(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		ArrayList<String> bestchain = identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);
		ArrayList<String> tr_list = null;

		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;

		tr_list = new ArrayList<String>();

		for (int j = bestchain.size() - 1; j >= 0; j--) {

			EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
			ArrayList<String> src_dep = null, trg_dep = null, src_dep1 = null, trg_dep1 = null;

			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

			if (j > 0) {

				System.out.println(bestchain.get(j - 1) + " -> " + bestchain.get(j) + "\n");
				for (int e = 0; e < identifyETL(metamodelPath + "/" + bestchain.get(j - 1),
						metamodelPath + "/" + bestchain.get(j)).size(); e++) {

					EtlModule module1 = new EtlModule();
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j - 1),
							metamodelPath + "/" + bestchain.get(j)).get(e)));

					src_dep = srcDependency1_new(module1);
					System.out.println("\n");
					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");

					System.out.println("src_dep:" + src_dep);
					System.out.println("trg_dep:" + trg_dep);

					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {

							type1 = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

							for (int gtr = 0; gtr < module1.getTransformationRules().size(); gtr++) {
								for (int td = 0; td < src_dep.size(); td++) {
									System.out.println("\nThis Source Dependency: "
											+ module1.getTransformationRules().get(gtr) + " " + src_dep.get(td));
								}

							}

							if (src_dep.size() > 0)
								System.out.println(
										"\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
							for (int ss = 0; ss < src_dep.size(); ss++) {
								String[] split_src = src_dep.get(ss).split("\\s+");

								if (split_src.length > 1) {

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

							if (trg_dep.size() > 0)
								System.out.println(
										"\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
							for (int ss = 0; ss < trg_dep.size(); ss++) {
								String[] split_trg = trg_dep.get(ss).split("\\s+");

								if (split_trg.length > 1) {

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

			if (j - 1 > 0) {

				for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + bestchain.get(j - 2),
						metamodelPath + "/" + bestchain.get(j - 1)).size(); e1++) {
					EtlModule module2 = new EtlModule();
					module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j - 2),
							metamodelPath + "/" + bestchain.get(j - 1)).get(e1)));

					src_dep1 = srcDependency1(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1(module2);
					System.out.println("\n");

					System.out.println("src_dep_prev:" + src_dep1);
					System.out.println("trg_dep_prev:" + trg_dep1);

					for (int gtr = 0; gtr < module2.getTransformationRules().size(); gtr++) {
						for (int sd = 0; sd < trg_dep1.size(); sd++) {
							System.out.println("\nPrevious Target Dependency: "
									+ module2.getTransformationRules().get(gtr) + " " + trg_dep1.get(sd));
						}

					}

					if (src_dep1.size() > 0)
						System.out.println(
								"\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
					for (int ss = 0; ss < src_dep1.size(); ss++) {
						String[] split_src = src_dep1.get(ss).split("\\s+");

						if (split_src.length > 1) {

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

					if (j - 1 > 0) {

						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

							type_prev = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

							if (trg_dep1.size() > 0)
								System.out.println(
										"\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
							for (int ss1 = 0; ss1 < trg_dep1.size(); ss1++) {
								String[] split_trg = trg_dep1.get(ss1).split("\\s+");

								if (split_trg.length > 1) {

									Node a1 = new Node(split_trg[0]);
									Node b1 = new Node(split_trg[1]);
									Node c1 = new Node(split_trg[2]);
									Node d1 = new Node(split_trg[3]);
									Node e11 = new Node(split_trg[4]);
									Node f1 = new Node(split_trg[6]);

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

					if (!src_dep.isEmpty()) {

						for (tr = 0; tr < trg_dep1.size(); tr++) {

							String[] split_trg = trg_dep1.get(tr).split("\\s+");

							for (int sr = 0; sr < src_dep.size(); sr++) {

								String[] split_src = src_dep.get(sr).split("\\s+");
								System.out.println("split_src_prev " + split_src[0] + split_src[1] + split_src[2]
										+ split_src[3] + split_src[4] + split_src[5]);
								System.out.println("split_trg " + split_trg[0] + split_trg[1] + split_trg[2]
										+ split_trg[3] + split_trg[4] + split_trg[5]);

								String[] split_trg1 = null;
								if (tr + 1 < trg_dep1.size())
									split_trg1 = trg_dep1.get(tr + 1).split("\\s+");

								if (tr + 1 < trg_dep1.size()) {
									if (trg_dep1.get(tr).substring(0, 1).equals(trg_dep1.get(tr + 1).substring(0, 1))) {
										if (!((split_trg[1] + split_trg[2] + split_trg[3]).trim())
												.equals((split_src[1] + split_src[2] + split_src[3]).trim())
												&& !((split_trg1[1] + split_trg1[2] + split_trg1[3]).trim())
												.equals((split_src[1] + split_src[2] + split_src[3]).trim()))

										{
											boolflag[tr] = true;

										}

										else
											boolflag[tr] = false;

									}
								}

								else {
									if (!((split_trg[1] + split_trg[2] + split_trg[3]).trim())
											.equals((split_src[1] + split_src[2] + split_src[3]).trim())) {

										boolflag[tr] = true;

									}

									else {

										boolflag[tr] = false;
									}
								}

							}
						}
					}

					for (int tr0 = 0; tr0 < trg_dep1.size(); tr0++) {

						if (boolflag[tr0] != null) {
							if (boolflag[tr0] == true) {
								System.out.println(trg_dep1.get(tr0) + " needs to be deleted " + "in "
										+ type.getTypeName() + " to " + type1.getTypeName()
										+ " transformation rule from " + type1.getModelName() + " metamodel." + "\n");

								tr_list.add(trg_dep1.get(tr0));

							}

							else {
								System.out.println("Keep " + trg_dep1.get(tr0) + " for previous transformation "
										+ type_prev + "\n");
							}
						}

					}

				}
			}

		}

		tr_list_chain.add(tr_list);

		return tr_list_chain;

	}

	public List<ArrayList<String>> deletetrindex2(String sourceModel, String sourceMM, String targetModel,
			String targetMM, List<ArrayList<String>> l) throws Exception {

		ArrayList<String> tr_list = null, line_kept = null;
		Map<String, String> idetl = HashMap_IdETL();
		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> line_kept_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;

		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");

			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();

			for (int j = l.get(i).size() - 1; j >= 0; j--) {

				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep = null, src_dep1 = null,
						src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

				if (j > 0) {

					System.out.println(l.get(i).get(j - 1) + " -> " + l.get(i).get(j) + "\n");

					EtlModule module1 = new EtlModule();
					//					for (Map.Entry<String, String> me : idetl.entrySet()) {
					//
					//						System.out.println(me.getKey().split(" ")[0].trim().equals(l.get(i).get(j - 1).trim())
					//								&& me.getKey().split(" ")[1].trim().equals(l.get(i).get(j).trim()));
					//						if (me.getKey().split(" ")[0].trim().equals(l.get(i).get(j - 1).trim())
					//								&& me.getKey().split(" ")[1].trim().equals(l.get(i).get(j).trim())) {
					//							module1.parse(scriptRoot.resolve(me.getValue()));
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j - 1),
							metamodelPath + "/" + l.get(i).get(j)).get(0)));

					src_dep = srcDependency1_new(module1);
					System.out.println("\n");

					// System.out.println("src_dep:" + src_dep);

					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {

							type1 = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						}
					}

				}

				if (j - 1 > 0) {

					EtlModule module2 = new EtlModule();
					//							System.out.println(me.getKey().split(" ")[0].trim().equals(l.get(i).get(j - 2).trim())
					//									&& me.getKey().split(" ")[1].trim().equals(l.get(i).get(j - 1).trim()));
					//							if (me.getKey().split(" ")[0].trim().equals(l.get(i).get(j - 2).trim())
					//									&& me.getKey().split(" ")[1].trim().equals(l.get(i).get(j - 1).trim())) {
					//								module2.parse(scriptRoot.resolve(me.getValue()));
					module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j - 2),
							metamodelPath + "/" + l.get(i).get(j - 1)).get(0)));

					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");

					// System.out.println("trg_dep1_prev:" + trg_dep1);

					if (j - 1 > 0) {

						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

							type_prev = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1)
									.getTargetParameters().size(); n++) {

								type_prev1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
										.getTransformationRules().get(m1).getTargetParameters().get(n));

							}
						}

						Boolean[] boolflag = new Boolean[trg_dep1.size()];

						if (!src_dep.isEmpty()) {

							for (tr = 0; tr < trg_dep1.size(); tr++) {

								String[] split_trg = trg_dep1.get(tr).split("\\s+");

								for (int sr = 0; sr < src_dep.size(); sr++) {

									String[] split_src = src_dep.get(sr).split("\\s+");

									if (!((split_trg[1] + split_trg[2] + split_trg[3]).trim()
											.equals((split_src[1] + split_src[2] + split_src[3]).trim()))) {

										boolflag[tr] = true;

									}

									else {

										boolflag[tr] = false;

										break;
									}

								}
							}
						}

						for (int tr0 = 0; tr0 < trg_dep1.size(); tr0++) {

							if (boolflag[tr0] != null) {
								if (boolflag[tr0] == true)

								{
									System.out.println(trg_dep1.get(tr0) + " needs to be deleted " + "in "
											+ type_prev.getTypeName() + " to " + type_prev1.getTypeName()
											+ " transformation rule" + "\n");

									tr_list.add(trg_dep1.get(tr0));

								}

								else {
									System.out.println("Keep " + trg_dep1.get(tr0) + " for transformation "
											+ type_prev.getModelName() + "2" + type_prev1.getModelName() + "\n");
									line_kept.add(trg_dep1.get(tr0));
								}
							}

						}

						//								}
						//							}
						//						}

					}
				}
			}
			line_kept_chain.add(line_kept);
			tr_list_chain.add(tr_list);
		}
		return tr_list_chain;

	}

	public ArrayList<String> deletetrindex2_single(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		ArrayList<String> bestchain = identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);

		ArrayList<String> tr_list_chain = null;
		tr_list_chain = new ArrayList<String>();
		ArrayList<String> line_kept_chain = new ArrayList<String>();

		for (int j = bestchain.size() - 1; j >= 0; j--) {

			ArrayList<String> tr_list = null, line_kept = null;
			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();
			int tr = 0;
			int k;

			EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;

			ArrayList<String> src_dep = new ArrayList<String>();
			ArrayList<String> trg_dep = new ArrayList<String>();
			ArrayList<String> src_dep1 = new ArrayList<String>();
			ArrayList<String> trg_dep1 = new ArrayList<String>();

			if (j > 0) {

				System.out.println(bestchain.get(j - 1) + " -> " + bestchain.get(j) + "\n");
				for (int e = 0; e < identifyETL(metamodelPath + "/" + bestchain.get(j - 1),
						metamodelPath + "/" + bestchain.get(j)).size(); e++) {
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					EtlModule module1 = new EtlModule();
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j - 1),
							metamodelPath + "/" + bestchain.get(j)).get(e)));

					src_dep = srcDependency1_new(module1);
					System.out.println("\n");

					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");

					System.out.println("src_dep:" + src_dep);

					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {

							type1 = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						}
					}

				}
			}

			if (j - 1 > 0) {

				for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + bestchain.get(j - 2),
						metamodelPath + "/" + bestchain.get(j - 1)).size(); e1++) {
					EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
					EtlModule module2 = new EtlModule();
					module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j - 2),
							metamodelPath + "/" + bestchain.get(j - 1)).get(e1)));

					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");

					System.out.println("trg_dep1_prev:" + trg_dep1);

					for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

						type_prev = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

						for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1).getTargetParameters()
								.size(); n++) {

							type_prev1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
									.getTransformationRules().get(m1).getTargetParameters().get(n));

						}
					}
				}
			}

			if (!src_dep.isEmpty()) {

				for (tr = 0; tr < trg_dep1.size(); tr++) {
					Boolean[] boolflag = new Boolean[trg_dep1.size()];
					int delete_size = 0;
					String[] split_trg = trg_dep1.get(tr).split("\\s+");

					inner: for (int sr = 0; sr < src_dep.size(); sr++) {

						String[] split_src = src_dep.get(sr).split("\\s+");

						if ((split_trg[1].equals(split_src[1])) &&

								(split_trg[3].equals(split_src[3]))

								) {

							boolflag[tr] = false;

							break inner;

						}

						else if (!(split_trg[1].equals(split_src[1])) && !(split_trg[2].equals(split_src[2]))
								&& !(split_trg[3].equals(split_src[3]))
								&& split_trg[4].split("2")[1].equals(split_src[4].split("2")[0])
								&& split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])) {

							boolflag[tr] = true;
							delete_size++;

						} else {
							boolflag[tr] = false;

						}

					}

					if (boolflag[tr] != null) {
						if (boolflag[tr] == true)

						{
							System.out.println(
									trg_dep1.get(tr) + " needs to be deleted " + "in " + type_prev.getTypeName()
									+ " to " + type_prev1.getTypeName() + " transformation rule" + "\n");

							tr_list.add(trg_dep1.get(tr));

						}

						else {
							System.out.println("Keep " + trg_dep1.get(tr) + " for transformation "
									+ type_prev.getModelName() + "2" + type_prev1.getModelName() + "\n");
							line_kept.add(trg_dep1.get(tr));
						}
					}

				}
			}

			line_kept_chain.addAll(line_kept);
			tr_list_chain.addAll(tr_list);
		}
		return tr_list_chain;

	}

	public List<ArrayList<String>> keeptrindex2(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			l.add(two);

		ArrayList<String> tr_list = null, line_kept = null;

		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> line_kept_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;

		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");

			tr_list = new ArrayList<String>();
			line_kept = new ArrayList<String>();

			for (int j = l.get(i).size() - 1; j >= 0; j--) {

				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep = null, src_dep1 = null,
						src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

				if (j > 0) {

					System.out.println(l.get(i).get(j - 1) + " -> " + l.get(i).get(j) + "\n");

					EtlModule module1 = new EtlModule();
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j - 1),
							metamodelPath + "/" + l.get(i).get(j)).get(0)));

					src_dep = srcDependency1_new(module1);
					System.out.println("\n");

					System.out.println("src_dep:" + src_dep);

					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {

							type1 = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						}
					}

				}

				if (j - 1 > 0) {

					EtlModule module2 = new EtlModule();
					module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j - 2),
							metamodelPath + "/" + l.get(i).get(j - 1)).get(0)));

					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");

					System.out.println("trg_dep1_prev:" + trg_dep1);

					if (j - 1 > 0) {

						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

							type_prev = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1)
									.getTargetParameters().size(); n++) {

								type_prev1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
										.getTransformationRules().get(m1).getTargetParameters().get(n));

							}
						}

						Boolean[] boolflag = new Boolean[trg_dep1.size()];

						if (!src_dep.isEmpty()) {

							for (tr = 0; tr < trg_dep1.size(); tr++) {

								String[] split_trg = trg_dep1.get(tr).split("\\s+");

								for (int sr = 0; sr < src_dep.size(); sr++) {

									String[] split_src = src_dep.get(sr).split("\\s+");

									if (!((split_trg[1] + split_trg[2] + split_trg[3]).trim()
											.equals((split_src[1] + split_src[2] + split_src[3]).trim()))) {

										boolflag[tr] = true;

									}

									else {

										boolflag[tr] = false;

										break;
									}

								}
							}
						}

						for (int tr0 = 0; tr0 < trg_dep1.size(); tr0++) {

							if (boolflag[tr0] != null) {
								if (boolflag[tr0] == true)

								{
									System.out.println(trg_dep1.get(tr0) + " needs to be deleted " + "in "
											+ type_prev.getTypeName() + " to " + type_prev1.getTypeName()
											+ " transformation rule" + "\n");

									tr_list.add(trg_dep1.get(tr0));

								}

								else {
									System.out.println("Keep " + trg_dep1.get(tr0) + " for transformation "
											+ type_prev.getModelName() + "2" + type_prev1.getModelName() + "\n");
									line_kept.add(trg_dep1.get(tr0));
								}
							}

						}

					}
				}

			}
			line_kept_chain.add(line_kept);
			tr_list_chain.add(tr_list);
		}
		return line_kept_chain;

	}

	public ArrayList<String> keeptrindex2_single(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		ArrayList<String> bestchain = identifyBestChain2(sourceModel, sourceMM, targetModel, targetMM);

		ArrayList<String> tr_list = null, line_kept = null;

		ArrayList<String> tr_list_chain = null;
		tr_list_chain = new ArrayList<String>();
		ArrayList<String> line_kept_chain = new ArrayList<String>();
		int tr = 0;
		int k;

		tr_list = new ArrayList<String>();
		line_kept = new ArrayList<String>();

		for (int j = bestchain.size() - 1; j >= 0; j--) {

			EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
			ArrayList<String> src_dep = null, src_dep_attr = null, trg_dep = null, src_dep1 = null,
					src_dep1_attr = null, trg_dep1 = null, trg_dep1_attr = null;

			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

			src_dep = new ArrayList<String>();
			trg_dep = new ArrayList<String>();
			src_dep1 = new ArrayList<String>();
			trg_dep1 = new ArrayList<String>();

			if (j > 0) {

				System.out.println(bestchain.get(j - 1) + " -> " + bestchain.get(j) + "\n");
				for (int e = 0; e < identifyETL(metamodelPath + "/" + bestchain.get(j - 1),
						metamodelPath + "/" + bestchain.get(j)).size(); e++) {

					EtlModule module1 = new EtlModule();
					module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j - 1),
							metamodelPath + "/" + bestchain.get(j)).get(e)));

					src_dep = srcDependency1_new(module1);
					System.out.println("\n");

					trg_dep = trgDependency1_new(module1);
					System.out.println("\n");

					System.out.println("src_dep:" + src_dep);

					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {

							type1 = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						}
					}
				}
			}

			if (j - 1 > 0) {

				for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + bestchain.get(j - 2),
						metamodelPath + "/" + bestchain.get(j - 1)).size(); e1++) {
					EtlModule module2 = new EtlModule();
					module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + bestchain.get(j - 2),
							metamodelPath + "/" + bestchain.get(j - 1)).get(e1)));

					src_dep1 = srcDependency1_new(module2);
					System.out.println("\n");
					trg_dep1 = trgDependency1_new(module2);
					System.out.println("\n");

					System.out.println("trg_dep1_prev:" + trg_dep1);

					if (j - 1 > 0) {

						for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

							type_prev = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

							for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1)
									.getTargetParameters().size(); n++) {

								type_prev1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
										.getTransformationRules().get(m1).getTargetParameters().get(n));

							}
						}
					}
				}
			}

			if (trg_dep1.size() > 0) {
				Boolean[] boolflag = new Boolean[trg_dep1.size()];

				if (!src_dep.isEmpty()) {

					for (tr = 0; tr < trg_dep1.size(); tr++) {

						String[] split_trg = trg_dep1.get(tr).split("\\s+");

						inner: for (int sr = 0; sr < src_dep.size(); sr++) {

							String[] split_src = src_dep.get(sr).split("\\s+");

							if (split_trg[1].equals(split_src[1]) && split_trg[2].equals(split_src[2])
									&& split_trg[3].equals(split_src[3])
									&& split_trg[4].split("2")[1].equals(split_src[4].split("2")[0])
									&& split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])) {

								boolflag[tr] = false;

								break inner;

							}

							else if (!split_trg[1].equals(split_src[1]) && !split_trg[2].equals(split_src[2])
									&& !split_trg[3].equals(split_src[3])
									&& split_trg[4].split("2")[1].equals(split_src[4].split("2")[0])
									&& split_trg[5].split("2")[1].equals(split_src[5].split("2")[0])) {

								boolflag[tr] = true;

							} else
								boolflag[tr] = false;

						}
					}
				}

				for (int tr0 = 0; tr0 < trg_dep1.size(); tr0++) {

					if (boolflag[tr0] != null) {
						if (boolflag[tr0] == true)

						{
							System.out.println(
									trg_dep1.get(tr0) + " needs to be deleted " + "in " + type_prev.getTypeName()
									+ " to " + type_prev1.getTypeName() + " transformation rule" + "\n");

							tr_list.add(trg_dep1.get(tr0));

						}

						else {
							System.out.println("Keep " + trg_dep1.get(tr0) + " for transformation "
									+ type_prev.getModelName() + "2" + type_prev1.getModelName() + "\n");
							line_kept.add(trg_dep1.get(tr0));
						}
					}

				}
			}

		}
		line_kept_chain.addAll(line_kept);
		tr_list_chain.addAll(tr_list);

		return line_kept_chain;

	}

	public List<ArrayList<String>> deletetrindex1(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			l.add(two);

		ArrayList<String> tr_list = null;

		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;

		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");

			tr_list = new ArrayList<String>();

			for (int j = l.get(i).size() - 1; j >= 0; j--) {

				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null, type_prev1 = null;
				ArrayList<String> src_dep = null, trg_dep = null, src_dep1 = null, trg_dep1 = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

				if (j > 0) {

					System.out.println(l.get(i).get(j - 1) + " -> " + l.get(i).get(j) + "\n");
					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j - 1),
							metamodelPath + "/" + l.get(i).get(j)).size(); e++) {

						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j - 1),
								metamodelPath + "/" + l.get(i).get(j)).get(e)));

						src_dep = srcDependency1_new(module1);
						System.out.println("\n");
						trg_dep = trgDependency1_new(module1);
						System.out.println("\n");

						System.out.println("src_dep:" + src_dep);

						for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
							type = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

							for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m)
									.getTargetParameters().size(); n++) {

								type1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module1)
										.getTransformationRules().get(m).getTargetParameters().get(n));

								if (src_dep.size() > 0)
									System.out.println(
											"\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
								for (int ss = 0; ss < src_dep.size(); ss++) {
									String[] split_src = src_dep.get(ss).split("\\s+");

									if (split_src.length > 1) {

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

									}
								}

							}
						}
					}
				}

				if (j - 1 > 0) {

					for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + l.get(i).get(j - 2),
							metamodelPath + "/" + l.get(i).get(j - 1)).size(); e1++) {
						EtlModule module2 = new EtlModule();
						module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j - 2),
								metamodelPath + "/" + l.get(i).get(j - 1)).get(e1)));

						src_dep1 = srcDependency1_new(module2);
						System.out.println("\n");
						trg_dep1 = trgDependency1_new(module2);
						System.out.println("\n");

						System.out.println("trg_dep_prev:" + trg_dep1);

						if (j - 1 > 0) {

							for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

								type_prev = (EolModelElementType) staticAnalyser.getType(
										((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

								for (int n = 0; n < ((EtlModule) module2).getTransformationRules().get(m1)
										.getTargetParameters().size(); n++) {

									type_prev1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
											.getTransformationRules().get(m1).getTargetParameters().get(n));

									if (trg_dep1.size() > 0)
										System.out.println(
												"\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
									for (int ss1 = 0; ss1 < trg_dep1.size(); ss1++) {
										String[] split_trg = trg_dep1.get(ss1).split("\\s+");

										if (split_trg.length > 1) {

											Node a1 = new Node(split_trg[0]);
											Node b1 = new Node(split_trg[1]);
											Node c1 = new Node(split_trg[2]);
											Node d1 = new Node(split_trg[3]);
											Node e11 = new Node(split_trg[4]);
											Node f1 = new Node(split_trg[6]);

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

										}

									}
									System.out.println();

								}
							}
						}

						Boolean[] boolflag = new Boolean[trg_dep1.size()];

						if (!src_dep.isEmpty()) {

							for (tr = 0; tr < trg_dep1.size(); tr++) {

								String[] split_trg = trg_dep1.get(tr).split("\\s+");

								for (int sr = 0; sr < src_dep.size(); sr++) {

									String[] split_src = src_dep.get(sr).split("\\s+");
									System.out.println("split_src " + split_src[0] + split_src[1] + split_src[2]
											+ split_src[3] + split_src[4] + split_src[5] + split_src[6] + split_src[7]);
									System.out.println("split_trg_prev " + split_trg[0] + split_trg[1] + split_trg[2]
											+ split_trg[3] + split_trg[4] + split_trg[5] + split_trg[6] + split_trg[7]);

									if (!((split_trg[1] + split_trg[2] + split_trg[3]).trim()
											.equals((split_src[1] + split_src[2] + split_src[3]).trim()))) {

										boolflag[tr] = true;

									}

									else {

										boolflag[tr] = false;

									}

								}
							}
						}

						for (int tr0 = 0; tr0 < trg_dep1.size(); tr0++) {

							if (boolflag[tr0] == true)

							{
								System.out.println(
										trg_dep1.get(tr0) + " needs to be deleted " + "in " + type_prev.getTypeName()
										+ " to " + type_prev1.getTypeName() + " transformation rule" + "\n");

								tr_list.add(trg_dep1.get(tr0));

							}

							else {
								System.out.println("Keep " + trg_dep1.get(tr0) + " for transformation "
										+ type_prev.getModelName() + "2" + type_prev1.getModelName() + "\n");
							}

						}

					}
				}

			}

			tr_list_chain.add(tr_list);
		}
		return tr_list_chain;

	}

	public List<ArrayList<String>> deletetrindex(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {

		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		Chain_MT cm = new Chain_MT();
		boolean etl1 = cm.findETLinModels(sourceMM, targetMM);

		if (etl1) {
			for (int id = 0; id < cm.identifyETLinModels(sourceMM, targetMM).size(); id++) {
				ArrayList<String> x = cm.identifyETLinModels(sourceMM, targetMM);

				Files.move(modelsRoot.resolve(x.get(id)), scriptRoot.resolve(x.get(id)));

			}
		}
		if (!two.isEmpty())
			l.add(two);

		ArrayList<String> tr_list = null;

		List<ArrayList<String>> tr_list_chain = null;
		tr_list_chain = new ArrayList<ArrayList<String>>();
		int tr = 0;
		int k;

		for (int i = 0; i < l.size(); i++) {
			System.out.println("Chain" + (i + 1) + " " + l.get(i) + "\n");

			tr_list = new ArrayList<String>();

			for (int j = 0; j < l.get(i).size(); j++) {

				EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
				ArrayList<String> src_dep = null, trg_dep = null, src_dep1 = null, trg_dep1 = null;

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

				if (j + 1 < l.get(i).size()) {

					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size(); e++) {

						EtlModule module1 = new EtlModule();
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

						src_dep = srcDependency1_new(module1);
						System.out.println("\n");
						trg_dep = trgDependency1_new(module1);
						System.out.println("\n");

						System.out.println("src_dep:" + src_dep);
						System.out.println("trg_dep:" + trg_dep);

						for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
							type = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

							for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m)
									.getTargetParameters().size(); n++) {

								type1 = (EolModelElementType) staticAnalyser.getType(((EtlModule) module1)
										.getTransformationRules().get(m).getTargetParameters().get(n));

								for (int gtr = 0; gtr < module1.getTransformationRules().size(); gtr++) {
									for (int td = 0; td < trg_dep.size(); td++) {
										System.out.println("\nThis Target Dependency: "
												+ module1.getTransformationRules().get(gtr) + " " + trg_dep.get(td));
									}

								}

								if (src_dep.size() > 0)
									System.out.println(
											"\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
								for (int ss = 0; ss < src_dep.size(); ss++) {
									String[] split_src = src_dep.get(ss).split("\\s+");

									if (split_src.length > 1) {

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

								if (trg_dep.size() > 0)
									System.out.println(
											"\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
								for (int ss = 0; ss < trg_dep.size(); ss++) {
									String[] split_trg = trg_dep.get(ss).split("\\s+");

									if (split_trg.length > 1) {

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

				if (j + 1 < l.get(i).size() - 1) {

					for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + l.get(i).get(j + 1),
							metamodelPath + "/" + l.get(i).get(j + 2)).size(); e1++) {
						EtlModule module2 = new EtlModule();
						module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j + 1),
								metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));

						src_dep1 = srcDependency1(module2);
						System.out.println("\n");
						trg_dep1 = trgDependency1(module2);
						System.out.println("\n");

						System.out.println("src_dep_next:" + src_dep1);
						System.out.println("trg_dep_next:" + trg_dep1);

						for (int gtr = 0; gtr < module2.getTransformationRules().size(); gtr++) {
							for (int sd = 0; sd < src_dep1.size(); sd++) {
								System.out.println("\nNext Source Dependency: "
										+ module2.getTransformationRules().get(gtr) + " " + src_dep1.get(sd));
							}

						}

						if (src_dep1.size() > 0)
							System.out.println(
									"\nSource Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
						for (int ss = 0; ss < src_dep1.size(); ss++) {
							String[] split_src = src_dep1.get(ss).split("\\s+");

							if (split_src.length > 1) {

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

						if (j + 1 < l.get(i).size() - 1) {

							for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

								type_next = (EolModelElementType) staticAnalyser.getType(
										((EtlModule) module2).getTransformationRules().get(m1).getSourceParameter());

								if (trg_dep1.size() > 0)
									System.out.println(
											"\nTarget Dependency List: (Metaclass -> StatementIndex feature Etype rule transformation) ");
								for (int ss1 = 0; ss1 < trg_dep1.size(); ss1++) {
									String[] split_trg = trg_dep1.get(ss1).split("\\s+");

									if (split_trg.length > 1) {

										Node a1 = new Node(split_trg[0]);
										Node b1 = new Node(split_trg[1]);
										Node c1 = new Node(split_trg[2]);
										Node d1 = new Node(split_trg[3]);
										Node e11 = new Node(split_trg[4]);
										Node f1 = new Node(split_trg[6]);

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

						Boolean[] boolflag = new Boolean[trg_dep.size()];

						if (!src_dep1.isEmpty()) {

							for (tr = 0; tr < trg_dep.size(); tr++) {

								String[] split_trg = trg_dep.get(tr).split("\\s+");

								for (int sr = 0; sr < src_dep1.size(); sr++) {

									String[] split_src = src_dep1.get(sr).split("\\s+");

									String[] split_trg1 = null;
									if (tr + 1 < trg_dep.size())
										split_trg1 = trg_dep.get(tr + 1).split("\\s+");

									if (tr + 1 < trg_dep.size()) {
										if (trg_dep.get(tr).substring(0, 1)
												.equals(trg_dep.get(tr + 1).substring(0, 1))) {

											if (!((split_trg[2] + split_trg[3]).trim())
													.equals((split_src[2] + split_src[3]).trim())
													&& !((split_trg1[2] + split_trg1[3]).trim())
													.equals((split_src[2] + split_src[3]).trim()))

											{
												boolflag[tr] = true;

											}

											else
												boolflag[tr] = false;

										}
									}

									else {
										if (!((split_trg[2] + split_trg[3]).trim())
												.equals((split_src[2] + split_src[3]).trim())) {

											boolflag[tr] = true;

										}

										else {

											boolflag[tr] = false;
										}
									}

								}
							}
						}

						for (int tr0 = 0; tr0 < trg_dep.size(); tr0++) {

							if (boolflag[tr0] != null) {
								if (boolflag[tr0] == true) {
									System.out.println(
											trg_dep.get(tr0) + " needs to be deleted " + "in " + type.getTypeName()
											+ " to " + type1.getTypeName() + " transformation rule from "
											+ type1.getModelName() + " metamodel." + "\n");

									tr_list.add(trg_dep.get(tr0));

								}

								else {
									System.out.println("Keep " + trg_dep.get(tr0) + " for next transformation "
											+ type_next + "\n");
								}
							}

						}

					}
				}

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

			if (scriptRoot.resolve(scriptcontents[i]).toFile().exists()) {
				EtlModule module = new EtlModule();

				module.parse(scriptRoot.resolve(scriptcontents[i]));

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

				if (sourceMM.substring(sourceMM.indexOf("\\") + 1, sourceMM.length()).equals(sourceMetamodel + ".ecore")
						&& targetMM.substring(targetMM.indexOf("\\") + 1, targetMM.length())
						.equals(targetMetamodel + ".ecore")) {
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

			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}

			staticAnalyser.validate(module);

			mm1 = module.getDeclaredModelDeclarations();

			sourceMetamodel = mm1.get(0).getModel().getName();
			targetMetamodel = mm1.get(1).getModel().getName();

			findetl.put(scriptcontents[i] + " " + sourceMetamodel + ".ecore" + " " + targetMetamodel + ".ecore",
					findETL(metamodelsRoot + sourceMetamodel + ".ecore", metamodelsRoot + targetMetamodel + ".ecore"));

		}

		return findetl;
	}

	public boolean findETLinModels(String sourceMM, String targetMM) throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;
		boolean flag = false;
		for (int i = 0; i < modelscontents.length; i++) {
			if (modelscontents[i].endsWith(".etl")) {

				if (modelsRoot.resolve(modelscontents[i]).toFile().exists()) {
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
						flag = true;
						break;
					}
				}
			}

		}
		return flag;

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

	public ArrayList<String> identifyETL(String sourceMM, String targetMM) throws Exception {
		ArrayList<String> etlname = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		registerMM(sourceMM);
		registerMM(targetMM);
		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();
			if (scriptRoot.resolve(scriptcontents[i]).toFile().exists()) {

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

					etlname.add(((EtlModule) module).getSourceFile().getName());

				}
			}

		}

		return etlname;
	}

	public ArrayList<String> identifyETLinModels(String sourceMM, String targetMM) throws Exception {
		ArrayList<String> etlname = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		for (int i = 0; i < modelscontents.length; i++) {
			if (modelscontents[i].endsWith(".etl")) {

				if (modelsRoot.resolve(modelscontents[i]).toFile().exists()) {
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

					}
				}
			}

		}

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

			}

		}

		return etlname;
	}

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

			EolModelElementType type = null, type1 = null;
			for (int i = 0; i < ((EtlModule) module).getTransformationRules().size(); i++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(i).getSourceParameter());

				for (int j = 0; j < ((EtlModule) module).getTransformationRules().get(i).getTargetParameters()
						.size(); j++) {
					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(i).getTargetParameters().get(j));

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

					}
					totalstatement = totalstatement + totalfeatures;

				}
				totalstructuratlfeatures = totalstructuratlfeatures + totalstatement;

			}

		}

		return totalstructuratlfeatures;

	}

	public HashMap<String, Integer> mt_complexity() throws Exception {
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

				linkedHashMap.put(mtl, calculateMTChain1(module));
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

				}

				ruleblock = (StatementBlock) ((EtlModule) module).getTransformationRules().get(0).getBody().getBody();
				float sumofoperation, totexpSize = 0;
				totalstatement = 0;
				totalfeatures = 0;
				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
					sumofoperation = 0;
					stName = ruleblock.getStatements().get(k);
					statementName = stName.toString();
					expName = stName.getChildren();
					expressionName = expName.toString();

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

					totalfeatures = (float) (totalfeatures + sumofoperation);

				}
				totalstatement = (float) (totalstatement + totalfeatures);

				totalstructuratlfeatures = (float) (totalstructuratlfeatures + totalstatement);

			}

		}

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

					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size(); e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

					total = calculateMTChain1(module1);
					sum[i] = sum[i] + total;

				}

			}
			if (sum[i] < min) {
				min = sum[i];
				index = l.get(i);
			}

			System.out.println("Total operators used in the chain: " + sum[i]);

		}
		return index;

	}

	public ArrayList<String> identifyBestChain2(String sourceModel, String sourceMM, String targetModel,
			String targetMM) throws Exception {
		ArrayList<String> two = identifyChain_two(sourceModel, sourceMM, targetModel, targetMM);
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);

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
			l.add(two);
		System.out.println("Chains: " + l);

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
					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size(); e++)
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

		}

		System.out.println("\nMT Chain " + index + " has minimum structural features of " + min);
		return index;

	}

	public ArrayList<Double> calculateMTCoverage(String sourceMM, String targetMM) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		for (int e = 0; e < identifyETL(sourceMM, targetMM).size(); e++) {
			EtlModule module1 = new EtlModule();

			module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
			System.out.println("\n" + scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
			ArrayList<String> src_dep = srcDependency2(module1);

			staticAnalyser.validate(module1);
			EolModelElementType type = null, type1 = null;
			double coverage = 0;

			int src_no_of_features = 0;
			double cov = 0;
			int count_feature = 0;
			double count = 0;
			int c = 0;

			for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

				c++;

				count_feature = src_dep.size() + c;

				for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
						.size(); n++) {
					type1 = (EolModelElementType) staticAnalyser.getType(
							((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

					System.out.println("No. of structural features in " + type.getTypeName() + " to "
							+ type1.getTypeName() + " rule is " + src_dep.size());

					System.out.println(
							"No. of structural features and metaclasses in source metamodel used in the transformation "
									+ type.getModelName() + " to " + type1.getModelName() + " is " + count_feature);

					String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
					String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

					String code = "(EClass.all.size().asInteger()+EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";

					String x = executeEOL1(sourceMM111, metaMM, code);

					count = NumberUtils.toDouble(x.trim());
					System.out.println("Total metaclasses and structural feature in source " + type.getTypeName()
					+ " metamodel is " + count);

				}
			}

			cov = (float) count_feature / count;
			tot_cov.add(cov);

		}

		return tot_cov;

	}

	public HashMap<String, Double> allmtcoverage() throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm1;

		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();

			ArrayList<EolModelElementType> rules = new ArrayList<EolModelElementType>();
			module.parse(scriptRoot.resolve(scriptcontents[i]));

			module.getContext().setModule(module);
			ArrayList<String> src_dep = srcDependency2(module);
			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
				}
			}

			staticAnalyser.validate(module);

			mm1 = module.getDeclaredModelDeclarations();

			sourceMetamodel = mm1.get(0).getModel().getName();
			targetMetamodel = mm1.get(1).getModel().getName();

			staticAnalyser.validate(module);
			EolModelElementType type = null, type1 = null;
			double coverage = 0;

			int src_no_of_features = 0;
			double cov = 0;
			double count_feature = 0;
			double count = 0;
			int c = 0;

			for (int m = 0; m < ((EtlModule) module).getTransformationRules().size(); m++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module).getTransformationRules().get(m).getSourceParameter());

				c++;
				rules.add(type);

				for (int n = 0; n < ((EtlModule) module).getTransformationRules().get(m).getTargetParameters()
						.size(); n++) {
					type1 = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module).getTransformationRules().get(m).getTargetParameters().get(n));

					String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
					String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

					String code = "(EClass.all.size().asDouble()+0.5*EAttribute.all.size().asDouble()+0.5*EReference.all.size().asDouble()).asString().println();\r\n";

					String x = executeEOL1(sourceMM111, metaMM, code);

					count = NumberUtils.toDouble(x.trim());

				}
				rules.stream().distinct().collect(Collectors.toList());
				HashSet<EolModelElementType> hset = new HashSet<EolModelElementType>(rules);

				count_feature = (double) (src_dep.size() * 0.5) + (1 * hset.size());
			}
			cov = (float) count_feature / count;

			allmtcoverage.put(scriptcontents[i] + " " + sourceMetamodel + ".ecore" + " " + targetMetamodel + ".ecore",
					cov);
		}

		return allmtcoverage;
	}

	public double calculateMTCoverage_new(String sourceMM, String targetMM) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		Set<String> keys = findetl.keySet();

		for (String key : keys) {
			if (key.split("\\s+")[1].equals(sourceMM.substring(11))
					&& key.split("\\s+")[2].equals(targetMM.substring(11))) {
				EtlModule module1 = new EtlModule();
				module1.parse(scriptRoot.resolve(key.split("\\s+")[0]));
				System.out.println("\n ETL file: " + scriptRoot.resolve(key.split("\\s+")[0]));

				EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
				ArrayList<String> src_dep = srcDependency2(module1);

				staticAnalyser.validate(module1);
				EolModelElementType type = null, type1 = null;
				double coverage = 0;

				int src_no_of_features = 0;
				double cov = 0;
				double count_feature = 0;
				double count = 0;
				int c = 0;

				for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
					type = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

					c++;
					count_feature = src_dep.size() + (0.5 * c);

					for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
							.size(); n++) {
						type1 = (EolModelElementType) staticAnalyser.getType(
								((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						System.out.println("No. of structural features in " + type.getTypeName() + " to "
								+ type1.getTypeName() + " rule is " + src_dep.size());

						System.out.println(
								"No. of structural features and metaclasses in source metamodel used in the transformation "
										+ type.getModelName() + " to " + type1.getModelName() + " is " + count_feature);

						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
						String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

						String code = "(EClass.all.size().asInteger()+0.5*EAttribute.all.size().asInteger()+0.5*EReference.all.size().asInteger()).asString().println();\r\n";

						String x = executeEOL1(sourceMM111, metaMM, code);

						count = NumberUtils.toDouble(x.trim());
						System.out.println("Total metaclasses and structural feature in source " + type.getTypeName()
						+ " metamodel is " + count);

						cov = (float) count_feature / count;
						tot_cov.add(cov);
					}
				}
			}

		}

		double coverage = Collections.max(tot_cov);

		return coverage;

	}

	public double calculateMTCoverage_new_combined(String sourceMM, String targetMM) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		for (int e = 0; e < identifyETL(sourceMM, targetMM).size(); e++) {
			EtlModule module1 = new EtlModule();

			module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
			System.out.println("\n ETL file: " + scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
			ArrayList<String> src_dep = srcDependency2(module1);

			staticAnalyser.validate(module1);
			EolModelElementType type = null, type1 = null;
			double coverage = 0;

			int src_no_of_features = 0;
			double cov = 0;
			double count_feature = 0;
			double count = 0;
			int c = 0;

			for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

				c++;
				count_feature = src_dep.size() + (0.5 * c);

				for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
						.size(); n++) {
					type1 = (EolModelElementType) staticAnalyser.getType(
							((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

					System.out.println("No. of structural features in " + type.getTypeName() + " to "
							+ type1.getTypeName() + " rule is " + src_dep.size());

					System.out.println(
							"No. of structural features and metaclasses in source metamodel used in the transformation "
									+ type.getModelName() + " to " + type1.getModelName() + " is " + count_feature);

					String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
					String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

					String code = "(EClass.all.size().asInteger()+0.5*EAttribute.all.size().asInteger()+0.5*EReference.all.size().asInteger()).asString().println();\r\n";

					String x = executeEOL1(sourceMM111, metaMM, code);

					count = NumberUtils.toDouble(x.trim());
					System.out.println("Total metaclasses and structural feature in source " + type.getTypeName()
					+ " metamodel is " + count);

					scriptRoot.resolve("newDep" + ".eol").toFile().delete();

					cov = (float) count_feature / count;
					tot_cov.add(cov);
				}
			}

		}
		Double coverage = Collections.max(tot_cov);

		return coverage;

	}

	public double calculateMTCoverage_new1(String sourceMM, String targetMM) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		double count_feature = 0;
		double count = 0;
		double cov = 0;

		double start = System.currentTimeMillis();
		Set<String> keys = allmtcoverage.keySet();

		for (int e = 0; e < identifyETL(sourceMM, targetMM).size(); e++) {
			EtlModule module1 = new EtlModule();
			ArrayList<EolModelElementType> rules = new ArrayList<EolModelElementType>();
			module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

			EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
			ArrayList<String> src_dep = srcDependency3(module1);

			staticAnalyser.validate(module1);
			EolModelElementType type = null, type1 = null;

			int c = 0;

			for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
				type = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

				c++;
				rules.add(type);

				for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
						.size(); n++) {
					type1 = (EolModelElementType) staticAnalyser.getType(
							((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

					String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
					String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

					String code = "(EClass.all.size().asDouble()+0.5*EAttribute.all.size().asDouble()+0.5*EReference.all.size().asDouble()).asString().println();\r\n";

					String x = executeEOL1(sourceMM111, metaMM, code);

					count = NumberUtils.toDouble(x.trim());

				}
				rules.stream().distinct().collect(Collectors.toList());
				HashSet<EolModelElementType> hset = new HashSet<EolModelElementType>(rules);

				count_feature = (double) (src_dep.size() * 0.5) + (1 * hset.size());
			}

		}

		cov = (float) count_feature / count;
		tot_cov.add(cov);
		Double coverage = Collections.max(tot_cov);

		System.out.println("Time taken to compute the coverage from " + sourceMM + " to " + targetMM + " is "
				+ (System.currentTimeMillis() - start) / 1000 + " seconds.");

		return coverage;

	}

	public double calculateMTCoverage_new2(String sourceMM, String targetMM) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		double count_feature = 0;
		double count = 0;
		double cov = 0;

		Set<String> keys = allmtcoverage.keySet();

		for (String key : keys) {
			if (key.split("\\s+")[1].equals(sourceMM.substring(11))
					&& key.split("\\s+")[2].equals(targetMM.substring(11))) {
				EtlModule module1 = new EtlModule();
				module1.parse(scriptRoot.resolve(key.split("\\s+")[0]));

				ArrayList<EolModelElementType> rules = new ArrayList<EolModelElementType>();

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				ArrayList<String> src_dep = srcDependency3(module1);

				staticAnalyser.validate(module1);
				EolModelElementType type = null, type1 = null;

				int c = 0;

				for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
					type = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

					c++;
					rules.add(type);

					for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
							.size(); n++) {
						type1 = (EolModelElementType) staticAnalyser.getType(
								((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
						String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

						String code = "(EClass.all.size().asDouble()+0.5*EAttribute.all.size().asDouble()+0.5*EReference.all.size().asDouble()).asString().println();\r\n";

						String x = executeEOL1(sourceMM111, metaMM, code);

						count = NumberUtils.toDouble(x.trim());

					}
					rules.stream().distinct().collect(Collectors.toList());
					HashSet<EolModelElementType> hset = new HashSet<EolModelElementType>(rules);

					count_feature = (double) (src_dep.size() * 0.5) + (1 * hset.size());
				}
			}
		}

		cov = (float) count_feature / count;
		tot_cov.add(cov);
		Double coverage = Collections.max(tot_cov);

		return coverage;

	}

	public LinkedHashMap<String, Double> mt_coverage() throws Exception {
		LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<>();
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

				linkedHashMap.put(mtl, calculateMTCoverage_new1(metamodelPath + "/" + sourceMetamodel + ".ecore",
						metamodelPath + "/" + targetMetamodel + ".ecore"));

			}
		}

		File file = new File(outputFilePath);
		BufferedWriter bf = null;

		try {

			bf = new BufferedWriter(new FileWriter(file));

			for (Map.Entry<String, Double> entry : linkedHashMap.entrySet()) {

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

		return linkedHashMap;

	}

	public ArrayList<Double> calculateMTCoverage_opt(String sourceMM, String targetMM) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		for (int e = 0; e < identifyETL(sourceMM, targetMM).size(); e++) {
			if (identifyETL(sourceMM, targetMM).get(e).startsWith("Optimized1_")) {
				EtlModule module1 = new EtlModule();

				module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
				System.out.println("\n" + scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));

				EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
				ArrayList<String> src_dep = srcDependency2(module1);

				staticAnalyser.validate(module1);
				EolModelElementType type = null, type1 = null;
				double coverage = 0;

				int src_no_of_features = 0;
				double cov = 0;
				int count_feature = 0;
				double count = 0;
				int c = 0;

				for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
					type = (EolModelElementType) staticAnalyser
							.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

					c++;

					count_feature = src_dep.size() + c;

					for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
							.size(); n++) {
						type1 = (EolModelElementType) staticAnalyser.getType(
								((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

						System.out.println("No. of structural features in " + type.getTypeName() + " to "
								+ type1.getTypeName() + " rule is " + src_dep.size());

						System.out.println(
								"No. of structural features and metaclasses in source metamodel used in the transformation "
										+ type.getModelName() + " to " + type1.getModelName() + " is " + count_feature);

						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
						String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

						String code = "(EClass.all.size().asInteger()+EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";

						String x = executeEOL1(sourceMM111, metaMM, code);

						count = NumberUtils.toDouble(x.trim());
						System.out.println("Total metaclasses and structural feature in source " + type.getTypeName()
						+ " metamodel is " + count);

					}
				}

				cov = (float) count_feature / count;
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

	public ArrayList<Double> calculateMTCoverage_File(File f) throws Exception {
		ArrayList<Double> tot_cov = new ArrayList<Double>();

		EtlModule module1 = new EtlModule();

		module1.parse(f.getName());

		EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
		ArrayList<String> src_dep = srcDependency2(module1);

		staticAnalyser.validate(module1);
		EolModelElementType type = null, type1 = null;
		double coverage = 0;

		int src_no_of_features = 0;
		double cov = 0;
		int count_feature = 0;
		double count = 0;
		int c = 0;

		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

			c++;

			count_feature = src_dep.size() + c;

			for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
					.size(); n++) {
				type1 = (EolModelElementType) staticAnalyser
						.getType(((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

				System.out.println("No. of structural features in " + type.getTypeName() + " to " + type1.getTypeName()
				+ " rule is " + src_dep.size());

				System.out.println(
						"No. of structural features and metaclasses in source metamodel used in the transformation "
								+ type.getModelName() + " to " + type1.getModelName() + " is " + count_feature);

				String metaMM = "http://www.eclipse.org/emf/2002/Ecore";
				String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

				String code = "(EClass.all.size().asInteger()+EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";

				String x = executeEOL1(sourceMM111, metaMM, code);

				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source " + type.getTypeName()
				+ " metamodel is " + count);

			}
		}
		cov = (float) count_feature / count;
		tot_cov.add(cov);

		return tot_cov;
	}

	public double calculateMTCoverage1(String sourceMM, String targetMM) throws Exception {

		EtlModule module1 = new EtlModule();
		double mt_coverage = 0;

		for (int e = 0; e < identifyETL(sourceMM, targetMM).size(); e++)
			module1.parse(scriptRoot.resolve(identifyETL(sourceMM, targetMM).get(e)));
		mt_coverage = calculateMTCoverage2(module1);
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

	public double calculateMTCoverage2(EtlModule module1) throws Exception {

		double coverage = 0, tot_cov = 0;

		double total = calculateMTChain12(module1);
		EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();

		staticAnalyser.validate(module1);
		EolModelElementType type;

		for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
			type = (EolModelElementType) staticAnalyser
					.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());
			double count = 0;
			double sum1 = 0, sum2 = 0, sum3 = 0;

			String metaMM = "http://www.eclipse.org/emf/2002/Ecore";

			String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

			String code2 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName() + "\");\r\n"
					+ "for(cl in type) {\r\n" + "cl.eAllAttributes.size().asString().println();\r\n}";

			String code20 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName() + "\");\r\n"
					+ "for(cl in type) {\r\n" + "cl.eAllReferences.size().asString().println();\r\n}";

			String x2 = executeEOL1(sourceMM111, metaMM, code2);
			String x20 = executeEOL1(sourceMM111, metaMM, code20);

			double i1 = NumberUtils.toDouble(x2.trim());
			double i2 = NumberUtils.toDouble(x20.trim());

			sum1 = sum1 + i1;
			sum2 = sum2 + i2;
			sum3 = i1 + i2;

			String code = "(EAttribute.all.size().asInteger()+EReference.all.size().asInteger()).asString().println();\r\n";

			System.out.println("Total features of source " + type.getTypeName() + " is " + sum3);

			String x = executeEOL1(sourceMM111, metaMM, code);

			count = NumberUtils.toDouble(x.trim());
			System.out.println("Total structural feature in " + type.getTypeName() + " metamodel is " + count);

			coverage = (sum3 / count);
			System.out.println("Transformation coverage: " + coverage);
			tot_cov += coverage;
		}

		System.out.println("Total transforation coverage: " + tot_cov + "\n");

		return tot_cov;

	}

	public double calculateChainCoverage(String sourceModel, String sourceMM, String targetModel, String targetMM)
			throws Exception {
		List<ArrayList<String>> l = identifyChain(sourceModel, sourceMM, targetModel, targetMM);
		int min = 99999;
		float max_cov = 0;
		ArrayList<String> index = null;
		ArrayList<String> index1 = null;
		System.out.println("\n");
		int[] sum = new int[l.size()];
		float[] sum_cov_chain = new float[l.size()];
		float prod = 1;
		float sum_cov = 1;
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

			float cov = 0;

			float tot1 = 0, tot2 = 0;

			for (int j = 0; j < l.get(i).size(); j++) {

				EtlModule module1 = new EtlModule();
				EtlModule module2 = new EtlModule();

				if (j + 1 < l.get(i).size()) {

					System.out.println(l.get(i).get(j) + " -> " + l.get(i).get(j + 1) + "\n");
					for (int e = 0; e < identifyETL(metamodelPath + "/" + l.get(i).get(j),
							metamodelPath + "/" + l.get(i).get(j + 1)).size(); e++)
						module1.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j),
								metamodelPath + "/" + l.get(i).get(j + 1)).get(e)));

					if (j + 1 < l.get(i).size() - 1) {
						for (int e1 = 0; e1 < identifyETL(metamodelPath + "/" + l.get(i).get(j + 1),
								metamodelPath + "/" + l.get(i).get(j + 2)).size(); e1++)
							module2.parse(scriptRoot.resolve(identifyETL(metamodelPath + "/" + l.get(i).get(j + 1),
									metamodelPath + "/" + l.get(i).get(j + 2)).get(e1)));
						total1 = calculateMTChain1(module2);

					}

					total = calculateMTChain(module1);

					sum[i] = sum[i] + total;

					int sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0, sum5 = 0, sum6 = 0, sum7 = 0, sum8 = 0;
					EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
					staticAnalyser.validate(module1);
					staticAnalyser.validate(module2);
					EolModelElementType type = null, type1 = null, type_next = null, type_prev = null;
					for (int m = 0; m < ((EtlModule) module1).getTransformationRules().size(); m++) {
						type = (EolModelElementType) staticAnalyser
								.getType(((EtlModule) module1).getTransformationRules().get(m).getSourceParameter());

						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";

						String sourceMM111 = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

						String code2 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName() + "\");\r\n"
								+ "for(cl in type) {\r\n" + "cl.eAllAttributes.size().asString().println();\r\n}";

						String code20 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName() + "\");\r\n"
								+ "for(cl in type) {\r\n" + "cl.eAllReferences.size().asString().println();\r\n}";

						String x2 = executeEOL1(sourceMM111, metaMM, code2);
						String x20 = executeEOL1(sourceMM111, metaMM, code2);

						int i1 = NumberUtils.toInt(x2.trim());
						int i2 = NumberUtils.toInt(x20.trim());

						sum1 = sum1 + i1;
						sum2 = sum2 + i2;

						for (int n = 0; n < ((EtlModule) module1).getTransformationRules().get(m).getTargetParameters()
								.size(); n++) {

							type1 = (EolModelElementType) staticAnalyser.getType(
									((EtlModule) module1).getTransformationRules().get(m).getTargetParameters().get(n));

							String code = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n" + "var attr = cl.eAllAttributes.name;\r\n"
							+ "for(at in attr){at.println();}\r\n}";

							String code0 = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n" + "var ref = cl.eAllReferences;\r\n"
							+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";

							String x = executeEOL1(sourceMM111, metaMM, code);
							String x0 = executeEOL1(sourceMM111, metaMM, code0);

							String[] arrOfStr = x.split("\n");
							String[] arrOfStr0 = x0.trim().split("\n");

							String eolcode = null, eolcode0 = null, eolcode5 = null, eolcode50 = null;
							;
							try {
								String ref_MM, ref_metaclass_MM;
								for (String r0 : arrOfStr0) {
									ref_MM = r0.trim().substring(0, r0.indexOf(":"));

									ref_metaclass_MM = r0.substring(r0.indexOf(":") + 1, r0.length());

									eolcode0 = type.getTypeName() + ".all." + ref_MM + ".println();";
									eolcode50 = type.getTypeName() + ".all." + ref_MM + ".size().println();";
								}
								for (String r : arrOfStr) {
									eolcode = type.getTypeName() + ".all." + r + ".println();";
									eolcode5 = type.getTypeName() + ".all." + r + ".size().println();";
								}

								String x5 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j), eolcode5);
								String x50 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j), eolcode50);

								int i5 = NumberUtils.toInt(x5.trim());
								int i6 = NumberUtils.toInt(x50.trim());

								sum5 = sum5 + i5;
								sum6 = sum6 + i6;

								String ex = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j), eolcode);
								String ex0 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j), eolcode0);

							} catch (Exception e) {
								System.out.println("EAttribute not given: " + e);
							}

							String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";

							String sourceMM1111 = metamodelsRoot.resolve(type1.getModelName() + ".ecore").toString();

							String code3 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n" + "cl.eAllAttributes.size.println();\r\n}";

							String code30 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n" + "cl.eAllReferences.size.println();\r\n}";

							String x3 = executeEOL1(sourceMM1111, metaMM1, code3);
							String x30 = executeEOL1(sourceMM1111, metaMM1, code30);

							int i3 = NumberUtils.toInt(x3.trim());
							int i4 = NumberUtils.toInt(x30.trim());
							sum3 = sum3 + i3;
							sum4 = sum4 + i4;

							String code1 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n" + "var attr = cl.eAllAttributes.name;\r\n"
							+ "for(at in attr){at.println();}\r\n}";

							String code10 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
							+ "\");\r\n" + "for(cl in type) {\r\n" + "var ref = cl.eAllReferences;\r\n"
							+ "for(a in ref){a.name.print() + \":\".print()+a.eContainer().name.println();}\r\n}";

							String x1 = executeEOL1(sourceMM1111, metaMM1, code1);
							String x10 = executeEOL1(sourceMM1111, metaMM1, code10);

							String[] arrOfStr1 = x1.split("\n");
							String[] arrOfStr10 = x10.trim().split("\n");

							String eolcode1 = null, eolcode10 = null, eolcode6 = null, eolcode60 = null;
							try {

								String ref_MM1, ref_metaclass_MM1;
								for (String r10 : arrOfStr10) {
									ref_MM1 = r10.trim().substring(0, r10.indexOf(":"));
									System.out.println(r10.trim().substring(0, r10.indexOf(":")));
									ref_metaclass_MM1 = r10.substring(r10.indexOf(":") + 1, r10.length());
									System.out.println(r10.substring(r10.indexOf(":") + 1, r10.length()));
									eolcode10 = type1.getTypeName() + ".all." + ref_MM1 + ".println();";
									eolcode60 = type1.getTypeName() + ".all." + ref_MM1 + ".size().println();";
								}
								for (String r1 : arrOfStr1) {
									eolcode1 = type1.getTypeName() + ".all." + r1 + ".println();";
									eolcode6 = type1.getTypeName() + ".all." + r1 + ".size().println();";
								}

								String x6 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j + 1), eolcode6);
								String x60 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j + 1), eolcode60);

								int i7 = NumberUtils.toInt(x6.trim());
								int i8 = NumberUtils.toInt(x60.trim());

								sum7 = sum7 + i7;
								sum8 = sum8 + i8;

								String ex1 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j + 1), eolcode1);
								String ex10 = executeEOL1(modelsRoot
										.resolve(l.get(i).get(j + 1).replaceFirst("[.][^.]+$", "") + ".xmi").toString(),
										metamodelPath + "/" + l.get(i).get(j + 1), eolcode10);

							} catch (Exception e) {

								System.out.println("New EAttribute not given: " + e);
							}

							String metaMM1x = null;

							float sumtot1 = 0, sumtot2 = 0;

							int sum3x = 0, sum4x = 0, i3x = 0, i4x = 0;
							if (j + 1 < l.get(i).size() - 1) {

								for (int m1 = 0; m1 < ((EtlModule) module2).getTransformationRules().size(); m1++) {

									type_next = (EolModelElementType) staticAnalyser.getType(((EtlModule) module2)
											.getTransformationRules().get(m1).getSourceParameter());

									if (type_next.getTypeName().equals(type1.getTypeName())) {

										metaMM1x = "http://www.eclipse.org/emf/2002/Ecore";

										String sourceMM1111x = metamodelsRoot
												.resolve(type_next.getModelName() + ".ecore").toString();

										String code3x = "var type = EClass.all.select(ec|ec.name = \""
												+ type1.getTypeName() + "\");\r\n" + "for(cl in type) {\r\n"
												+ "cl.eAllAttributes.size.println();\r\n}";

										String code30x = "var type = EClass.all.select(ec|ec.name = \""
												+ type1.getTypeName() + "\");\r\n" + "for(cl in type) {\r\n"
												+ "cl.eAllReferences.size.println();\r\n}";

										String x3x = executeEOL1(sourceMM1111x, metaMM1x, code3x);
										String x30x = executeEOL1(sourceMM1111x, metaMM1x, code30x);

										i3x = NumberUtils.toInt(x3x.trim());
										i4x = NumberUtils.toInt(x30x.trim());

										sum3x = sum3x + i3x;
										sum4x = sum4x + i4x;

										tot1 = sum3x + sum4x;

									}

								}

								tot2 = sum1 + sum2;

								sumtot1 = sumtot1 + tot1;
								sumtot2 = sumtot2 + tot2;
								cov = sumtot1 / sumtot2;
								sum_cov = cov;

							} else
								sum_cov = 1;

						}

					}
					System.out.println("Tranformation coverage for " + type.getModelName() + " -> "
							+ type1.getModelName() + " is " + sum_cov);
					prod = prod * sum_cov;
					sum_cov_chain[i] = prod;

					sum_source_attributeMM = sum_source_attributeMM + sum1;
					sum_source_referenceMM = sum_source_referenceMM + sum2;
					sum_target_attributeMM = sum_target_attributeMM + sum3;
					sum_target_referenceMM = sum_target_referenceMM + sum4;

					sum_source_attributeModel = sum_source_attributeModel + sum5;
					sum_source_referenceModel = sum_source_referenceModel + sum6;
					sum_target_attributeModel = sum_target_attributeModel + sum7;
					sum_target_referenceModel = sum_target_referenceModel + sum8;

				}

			}
			if (sum[i] < min) {
				min = sum[i];
				index = l.get(i);
			}
			if (sum_cov_chain[i] > max_cov) {
				max_cov = sum_cov_chain[i];
				index1 = l.get(i);
			}

		}

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
						System.out.println("Source Type: " + type.getTypeName());
						String eolcode = "var type = EClass.all.select(ec|ec.name = \"" + type.getTypeName()
						+ "\");\r\n" + "for(cl in type) {\r\n"
						+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
						+ "for(i in ref) {" + "cl.name.print();" + "\"'s etype \".print();"
						+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println(); \r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";

						String metaMM = "http://www.eclipse.org/emf/2002/Ecore";

						String sourceMM = metamodelsRoot.resolve(type.getModelName() + ".ecore").toString();

						String ex = executeEOL1(sourceMM, metaMM, eolcode);

						System.out.println(ex);

					} catch (IOException e) {

						System.out.println(e);
					} catch (Exception e) {

						System.out.println(e);
					}

					try {
						System.out.println("Target Type: " + type1.getTypeName());
						String eolcode1 = "var type = EClass.all.select(ec|ec.name = \"" + type1.getTypeName()
						+ "\");\r\n" + "for(cl in type) {\r\n"
						+ "\"References: \".print();\r\n var reference=cl.eAllReferences.name.println(); var ref = EReference.all.select(a|a.eType = cl);\r\n \"Attributes: \".print();\r\n var attr = cl.eAllAttributes.name.println();\r\n"
						+ "for(i in ref) {" + "cl.name.print();" + "\"'s etype \".print();"
						+ "i.name.print();\r\n \" is referenced within \".print();\r\n i.eContainer().name.println();\r\n \"Therefore, \".print()+ i.eContainer().name.print()+\" metaclass is dependent on \".print()+cl.name.print()+\" metaclass\".println();}}";

						String metaMM1 = "http://www.eclipse.org/emf/2002/Ecore";

						String sourceMM1 = metamodelsRoot.resolve(type1.getModelName() + ".ecore").toString();

						String ex1 = executeEOL1(sourceMM1, metaMM1, eolcode1);

						System.out.println(ex1);

					} catch (IOException e) {

						System.out.println(e);
					} catch (Exception e) {

						System.out.println(e);
					}

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

						System.out.println("\n" + expName.get(l).toString().split(" ")[0]);
						opName = expName.get(l).getChildren();
						if (!opName.isEmpty()) {
							totexpName = calculateExpressions(opName);
							for (int m = 0; m < totexpName.size(); m++) {
								totexpSize = totexpName.get(m).size();

								sumofoperation = sumofoperation + totexpSize;

								for (int n = 0; n < totexpName.get(m).size(); n++) {
									System.out.println(totexpName.get(m).get(n));

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

	public void registerMM(String mm) {
		try {

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
		} catch (Exception e) {

			System.err.println(e.getMessage());
		}
	}

}
