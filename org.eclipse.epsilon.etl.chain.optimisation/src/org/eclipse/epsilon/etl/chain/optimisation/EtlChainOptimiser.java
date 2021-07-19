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
import java.util.List;
import java.util.Scanner;

import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

/**
 * This example demonstrates applying optimisation on ETL chains
 * 
 * @author Qurat ul ain Ali
 */
public class EtlChainOptimiser {
	
	static Path modelsRoot = Paths.get("models");
	static Path scriptRoot = Paths.get("scripts");
	static Path metamodelsRoot = Paths.get("metamodels");
	static Path genmodelsRoot = Paths.get("models/generatedmodels");
	
	static File metamodelPath = new File("metamodels");
	
	static String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
	static String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
	static String sourceModel=modelsRoot.resolve("Tree.xmi").toAbsolutePath().toUri().toString();
	static String targetModel= genmodelsRoot.resolve("DB3.xmi").toAbsolutePath().toUri().toString();
	
	static Chaining_MT chainingmt = new Chaining_MT();
	
	public static void main(String[] args) throws Exception {
		
		Chaining_MT chainingmt = new Chaining_MT();
		
		
		System.out.println("\n 1: Select least complex chain with minimum structural features\n");
		System.out.println("\n 2: Select chain with required transformation rules only\n");
		System.out.println("\n 3: First select least complex chain with minimum structural features and then select chain with required transformation rules\n");
		System.out.println("\n 4: First select chain with required transformation rules only and then select least complex chain with minimum structural features\n");
		System.out.println("\nSelect the execute type number ");
		
		Scanner scanner = new Scanner(System.in);
		int number= scanner.nextInt();
		scanner.close();
		switch(number)
		{
		case 1: ArrayList<String> bestchain = chainingmt.identifybestchain(sourceModel, sourceMM, targetModel, targetMM);
				System.out.println("Best chain: "+bestchain);
				break;
		case 2: optimizeonly();
		break;
		case 3: optimize();
		break;
		case 4: newexecute();
		break;
		default: System.out.println("Wrong input");
		}
	}
	
public static void newexecute() throws Exception
{
	List<ArrayList<String>> chain = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
	ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
	ArrayList<EtlModule> modules = new ArrayList<>();
	EtlRewritingHandler etlRewritingHandler = new EtlRewritingHandler();
	
	int min=99999;
	int[] sum = new int[chain.size()];
	ArrayList<String> index = null;
	
	for (int j = 0; j < chain.size(); j++) {
		
		int rewritetotal = 0;
		System.out.println("\nChain"+(j+1)+" "+chain.get(j)+"\n");
		
		for(int i=0;i<chain.get(j).size();i++)
		{
		EtlModule module1 = new EtlModule();
		
		if (i + 1 < chain.get(j).size()) {

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
	
	rewritetotal = etlRewritingHandler.invokeRewriters(modules, staticAnalysers);
	modules.clear();
	staticAnalysers.clear();
	sum[j]=rewritetotal;
	System.out.println("\nTotal structural features of optimized "+"Chain"+(j+1)+" "+chain.get(j)+" is "+rewritetotal);
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
			
			Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
			String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
			
			Path newtargetmodelpath = modelsRoot.resolve(index.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
			String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
			
			chainingmt.executeETL(newsourcemodel, metamodelPath+"/"+index.get(k), newtargetmodel, metamodelPath+"/"+index.get(k+1));
			
		}
		
	}
	
}

public static void optimize() throws Exception
{
	ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
	ArrayList<EtlModule> modules = new ArrayList<>();

	ArrayList<String> bestchain = chainingmt.identifybestchain2(sourceModel, sourceMM, targetModel, targetMM);
	int rewritetotal = 0;
	for (int i = 0; i < bestchain.size(); i++) {
		EtlModule module1 = new EtlModule();

		if (i + 1 < bestchain.size()) {

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
	new EtlRewritingHandler().invokeRewriters1(modules, staticAnalysers);
	for (int k = 0; k < bestchain.size(); k++) {
		if(k+1<bestchain.size())
		{
			Path newsourcemodelpath = modelsRoot.resolve(bestchain.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
			String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
			
			Path newtargetmodelpath = modelsRoot.resolve(bestchain.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
			String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
			
			EtlRunConfiguration exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"/"+bestchain.get(k), newtargetmodel, metamodelPath+"/"+bestchain.get(k+1));
		}
		
	}
}


public static void optimizeonly() throws Exception
{
	List<ArrayList<String>> chain = chainingmt.identifychain(sourceModel, sourceMM, targetModel, targetMM);
	ArrayList<EtlStaticAnalyser> staticAnalysers = new ArrayList<>();
	ArrayList<EtlModule> modules = new ArrayList<>();
	EtlRewritingHandler etlRewritingHandler = new EtlRewritingHandler();
	
	for (int j = 0; j < chain.size(); j++) {
		System.out.println("\nChain"+(j+1)+" "+chain.get(j)+"\n");
		
		for(int i=0;i<chain.get(j).size();i++)
		{
		EtlModule module1 = new EtlModule();
		
		if (i + 1 < chain.get(j).size()) {

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
	
	new EtlRewritingHandler().invokeRewriters1(modules, staticAnalysers);
	modules.clear();
	staticAnalysers.clear();
	}
}
	
}