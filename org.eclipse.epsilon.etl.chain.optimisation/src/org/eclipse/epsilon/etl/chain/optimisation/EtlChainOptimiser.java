package org.eclipse.epsilon.etl.chain.optimisation;

import java.io.File;
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
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

/**
 * This example demonstrates applying optimisation on ETL chains
 * 
 * @author Qurat ul ain Ali
 */
public class EtlChainOptimiser {

	public static void main(String[] args) throws Exception {
		Path modelsRoot = Paths.get("models");
		Path scriptRoot = Paths.get("scripts");
		Path metamodelsRoot = Paths.get("metamodels");
		Path genmodelsRoot = Paths.get("models/generatedmodels");

		File metamodelPath = new File("metamodels");

		Chaining_MT chainingmt = new Chaining_MT();

		String simpleTraceMM = metamodelsRoot.resolve("SimpleTrace.ecore").toString();
		String tmMM = modelsRoot.resolve("TM.ecore").toString();
		String dbMM = metamodelsRoot.resolve("DB.ecore").toString();
		String sourcemodel = modelsRoot.resolve("SimpleTrace.xmi").toAbsolutePath().toUri().toString();
		String targetmodel = genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();

		ArrayList<String> bestchain = chainingmt.identifybestchain1(sourcemodel, simpleTraceMM, targetmodel, dbMM);

		System.out.println("Best chain: " + bestchain);
		try (EmfModel sourceEmfModel = new EmfModel()) {
			sourceEmfModel.setName("SimpleTrace");
			sourceEmfModel.setModelFile(modelsRoot.resolve("SimpleTrace.xmi").toString());
			sourceEmfModel.setMetamodelFile(simpleTraceMM);
			sourceEmfModel.setReadOnLoad(true);
			sourceEmfModel.setStoredOnDisposal(false);
		}
		try (EmfModel intermediateEmfModel = new EmfModel()) {
			intermediateEmfModel.setName("TM");
			intermediateEmfModel.setModelFile(modelsRoot.resolve("Gen_SimpleTrace2TM.xmi").toString());
			intermediateEmfModel.setMetamodelFile(tmMM);
			intermediateEmfModel.setReadOnLoad(false);
			intermediateEmfModel.setStoredOnDisposal(true);
		}
		try (EmfModel targetEmfModel = new EmfModel()) {
			targetEmfModel.setName("DB");
			targetEmfModel.setModelFile(modelsRoot.resolve("Gen_TM2DB.xmi").toString());
			targetEmfModel.setMetamodelFile(dbMM);
			targetEmfModel.setReadOnLoad(false);
			targetEmfModel.setStoredOnDisposal(true);
		}
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();

		for (int i = 0; i < bestchain.size(); i++) {
			EtlModule module1 = new EtlModule();

			if (i + 1 < bestchain.size()) {

				System.out.println("\n" + bestchain.get(i) + " -> " + bestchain.get(i + 1) + "\n");

				EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
				module1.parse(scriptRoot.resolve(chainingmt.identifyETL(metamodelPath + "/" + bestchain.get(i),
						metamodelPath + "/" + (bestchain.get(i + 1)))));
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
		new EtlRewritingHandler().invokeRewriters(modules, staticAnalysers);

//		EtlModule module1 = new EtlModule();
//		EtlModule module2 = new EtlModule();
//		
//		module1.parse(scriptRoot.resolve("SimpleTrace2TM.etl"));
//		EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
//		for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
//			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//				staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
//			}
//		}
//		staticAnlayser.validate(module1);
//		
//		module2.parse(scriptRoot.resolve("TM2DB.etl"));
//		EtlStaticAnalyser staticAnlayser1 = new EtlStaticAnalyser();
//		for (ModelDeclaration modelDeclaration : module2.getDeclaredModelDeclarations()) {
//			if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//				staticAnlayser1.getContext().setModelFactory(new SubEmfModelFactory());
//			}
//		}
//		staticAnlayser1.validate(module2);
//		ArrayList<EtlModule> modules = new ArrayList<>();
//		
//		modules.add(module1);
//		modules.add(module2);
//		
//		System.out.println();
//		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
//		staticAnalysers.add(staticAnlayser);
//		staticAnalysers.add(staticAnlayser1);
//
//		new EtlRewritingHandler().invokeRewriters(modules, staticAnalysers);
//
//		sourceEmfModel.load();
//		intermediateEmfModel.load();
//		targetEmfModel.load();
//		
//		module1.getContext().getModelRepository().addModel(sourceEmfModel);
//		module1.getContext().getModelRepository().addModel(intermediateEmfModel);
//		module1.getContext().getModelRepository().addModel(targetEmfModel);
//		
//		module1.execute();
//		module2.execute();
//		
//		sourceEmfModel.dispose();
//		intermediateEmfModel.dispose();
//		targetEmfModel.dispose();	

	}
}
