package org.eclipse.epsilon.etl.chain.optimisation;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.List;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.chain.selection.ModelProperties;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
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
		
//		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
//		String targetmodel= modelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		
		Chaining_MT chainingmt = new Chaining_MT();
		
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String simpleTraceMM = metamodelsRoot.resolve("SimpleTrace.ecore").toString();
//		String tmMM = modelsRoot.resolve("TM.ecore").toString();
		String dbMM = metamodelsRoot.resolve("DB.ecore").toString();
//		System.out.println("123");
		String sourcemodel=modelsRoot.resolve("SimpleTrace.xmi").toAbsolutePath().toUri().toString();
		String sourceModel=modelsRoot.resolve("Tree.xmi").toAbsolutePath().toUri().toString();
		String targetModel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		
//		ArrayList<String> bestchain = chainingmt.identifybestchain(sourcemodel, simpleTraceMM, targetmodel, dbMM);
//		System.out.println("21245");
		List<ArrayList<String>> chain = chainingmt.identifychain(sourceModel, sourceMM, targetModel, dbMM);

//		System.out.println("Best chain: "+bestchain);
		
//		EmfModel sourceEmfModel = new EmfModel();
//		sourceEmfModel.setName("SimpleTrace");
//		sourceEmfModel.setModelFile(modelsRoot.resolve("SimpleTrace.xmi").toString());
//		sourceEmfModel.setMetamodelFile(simpleTraceMM);
//		sourceEmfModel.setReadOnLoad(true);
//		sourceEmfModel.setStoredOnDisposal(false);
//		
//		EmfModel intermediateEmfModel = new EmfModel();
//		intermediateEmfModel.setName("TM");
//		intermediateEmfModel.setModelFile(modelsRoot.resolve("Gen_SimpleTrace2TM.xmi").toString());
//		intermediateEmfModel.setMetamodelFile(tmMM);
//		intermediateEmfModel.setReadOnLoad(false);
//		intermediateEmfModel.setStoredOnDisposal(true);
//		
//		EmfModel targetEmfModel = new EmfModel();
//		targetEmfModel.setName("DB");
//		targetEmfModel.setModelFile(modelsRoot.resolve("Gen_TM2DB.xmi").toString());
//		targetEmfModel.setMetamodelFile(dbMM);
//		targetEmfModel.setReadOnLoad(false);
//		targetEmfModel.setStoredOnDisposal(true);
		
		//System.out.println(bestchain);
		
		
		ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
		ArrayList<EtlModule> modules = new ArrayList<>();
		EtlRewritingHandler etlRewritingHandler = new EtlRewritingHandler();
		
		EtlRunConfiguration exec=null;
		int min=99999;
		int[] sum = new int[chain.size()];
		ArrayList<String> index = null;
		
//		for (int i = 0; i < bestchain.size(); i++) {
		for (int j = 0; j < chain.size(); j++) {
			
			int rewritetotal = 0;
			System.out.println("\nChain"+(j+1)+" "+chain.get(j)+"\n");
			
			for(int i=0;i<chain.get(j).size();i++)
			{
			EtlModule module1 = new EtlModule();
			
//			System.out.println("123");
//			EmfModel emfmodel1 = new EmfModel();
//			EmfModel emfmodel2 = new EmfModel();
			
			if (i + 1 < chain.get(j).size()) {

//				System.out.println("\n" + chain.get(j).get(i) + " -> " + chain.get(j).get(i + 1) + "\n");

				EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
				module1.parse(scriptRoot.resolve(chainingmt.identifyETL(metamodelPath + "/" + chain.get(j).get(i),
						metamodelPath + "/" + (chain.get(j).get(i + 1)))));
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
//		new EtlRewritingHandler().invokeRewriters(modules, staticAnalysers);
		
		rewritetotal = etlRewritingHandler.invokeRewriters(modules, staticAnalysers);
		modules.clear();
		staticAnalysers.clear();
		sum[j]=rewritetotal;
		System.out.println("\nTotal structural features of optimized "+"Chain"+(j+1)+" "+chain.get(j)+" is "+rewritetotal);
//		System.out.println(rewrite);
		if(sum[j]<min)
		{
			min=sum[j];
			index=chain.get(j);
		}
		continue;
	}
		
		System.out.println("\nOptmized MT Chain "+index+" has minimum structural features of " + min);
		System.out.println("------------------Executing best optimized chain--------------------");
		for(int k=0;k<index.size();k++)
		{
			if(k+1<index.size())
			{
				
				//System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
				
				Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
				
				Path newtargetmodelpath = modelsRoot.resolve(index.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
				
				exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"/"+index.get(k), newtargetmodel, metamodelPath+"/"+index.get(k+1));
				
			}
			
		}
		
//		EtlRunConfiguration exec=null;
//		System.out.println(rewrite);
//		for(int k=0;k<rewrite.size();k++)
//		{
//			
//			try {
//				FileWriter fw=new FileWriter(scriptRoot.resolve("Script"+k+".etl").toString());
//				fw.write(rewrite.get(k));
//				fw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
////				e.printStackTrace();
//				System.out.println(e);
//			} 
//			
////			if(k+1<rewrite.size())
////			{
//				EtlModule newmodule = new EtlModule();
//				if(newmodule instanceof EtlModule)
//				{
//					newmodule.parse(scriptRoot.resolve("Script"+k+".etl"));
//					newmodule.getContext().setModule(newmodule);
//					System.out.println(newmodule);
//					EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
//					for (ModelDeclaration modelDeclaration : newmodule.getDeclaredModelDeclarations()) {
//						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
//							System.out.println("6556");
//							staticAnlayser.getContext().setModelFactory(new SubEmfModelFactory());
//							
//						}
//						System.out.println(modelDeclaration);	
//					}
//					staticAnlayser.validate(newmodule);
//					
//					List<ModelDeclaration> mm = ((EtlModule) newmodule).getDeclaredModelDeclarations();
//		
//					
//					String sourceMetamodel = mm.get(0).getModel().getName();
//					String targetMetamodel = mm.get(1).getModel().getName();
//					
//					System.out.println(sourceMetamodel);
//					System.out.println(targetMetamodel);
//				}
//				
//				
////				Path newsourcemodelpath = modelsRoot.resolve(rewrite.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
////				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
////				
////				Path newtargetmodelpath = modelsRoot.resolve(rewrite.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
////				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
////				
////				exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"/"+rewrite.get(k), newtargetmodel, metamodelPath+"/"+rewrite.get(k+1));
////			}
//			
//		
//			scriptRoot.resolve("Script "+k+".etl").toFile().delete();
//		}
//		System.out.println(exec);
//		System.out.println("2"+rewrite.get(1));
//		ArrayList<String> bestchain1 = chainingmt.identifybestchain(sourcemodel, simpleTraceMM, targetmodel, dbMM);
//		System.out.println(bestchain1);
//		
//		EtlRunConfiguration exec=null;
//		for(int k=0;k<bestchain1.size();k++)
//		{
//			if(k+1<bestchain1.size())
//			{
//				
//				//System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
//				
//				Path newsourcemodelpath = modelsRoot.resolve(bestchain1.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
//				String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
//				
//				Path newtargetmodelpath = modelsRoot.resolve(bestchain1.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
//				String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
//				
//				exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"/"+bestchain1.get(k), newtargetmodel, metamodelPath+"/"+bestchain1.get(k+1));
//				
//			}
//		}
		//System.out.println(modules);
		
		
		
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