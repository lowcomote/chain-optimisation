package org.eclipse.epsilon.etl.chain.selection;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class Chain_MT {
	static Path modelsRoot = Paths.get("models");
	static Path metamodelsRoot = Paths.get("metamodels");
	static Path scriptRoot = Paths.get("scripts");
	static Path genmodelsRoot = Paths.get("models/generatedmodels");
	
	static File metamodelPath = new File("metamodels");
	String contents[] = metamodelPath.list();
	
	static File modelPath = new File("models");
	String modelscontents[] = modelPath.list();
	
	static File scriptPath = new File("scripts");
	String scriptcontents[] = scriptPath.list();
	
	static String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
	static String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
	static String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
	static String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
	
	public static void main(String[] args) throws Exception {
	
		Chaining_MT chainingmt = new Chaining_MT();
		
		
		List<ArrayList<String>> l = chainingmt.identifyChain(sourcemodel, sourceMM, targetmodel, targetMM);
		//ArrayList<String> bestchain = chainingMt.identifyBestChain(sourceModel, sourceMM, targetModel, targetMM);
		
			EtlRunConfiguration exec=null;
			IEolModule module = null;
			int min=9999;
			ArrayList<String> index = null;
			System.out.println("\n");
			int[] sum = new int[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				//int[] sum = new int[l.size()];
				System.out.println("Chain"+(i+1)+" "+l.get(i)+"\n");
				//sum[i]=0;
				
//				chainingmt.calculateMTChain(sourcemodel, sourceMM, targetmodel, targetMM);
				//System.out.println(l.get(i).get(0));
				int total=0;
				int tot1=0;
				for(int j=0;j<l.get(i).size();j++)
				{
					
					EtlModule module1 = new EtlModule();
					
					if(j+1<l.get(i).size())
					{
						
						
						System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1)+"\n");
						
						module1.parse(scriptRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")+"2"+l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")+".etl"));
						
						total = chainingmt.calculateMTChain(module1);
						sum[i]=sum[i]+total;

						System.out.println("Total operators used in the transformation "+l.get(i).get(j)+" -> "+l.get(i).get(j+1)+": "+total+"\n");
					}
					
			
				}
				if(sum[i]<min)
				{
					min=sum[i];
					index=l.get(i);
				}
				
				System.out.println("Total operators used in the chain: "+sum[i]);
				System.out.println("---------------------------------------------------------\n");
				
			}
			
			System.out.println("\nMT Chain "+index+" has minimum structural features of " + min);
			System.out.println("------------------Executing best chain--------------------");
			for(int k=0;k<index.size();k++)
			{
				if(k+1<index.size())
				{
					
					//System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
					
					Path newsourcemodelpath = modelsRoot.resolve(index.get(k).replaceFirst("[.][^.]+$", "")+".xmi");
					String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
					
					Path newtargetmodelpath = modelsRoot.resolve(index.get(k+1).replaceFirst("[.][^.]+$", "")+".xmi");
					String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					
					exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"\\"+index.get(k), newtargetmodel, metamodelPath+"\\"+index.get(k+1));
					
				}
				
			}
		

	}
	public boolean findETL(String sourceMM, String targetMM) throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;
		boolean flag = false;
		for (int i = 0; i < scriptcontents.length; i++) {
			EtlModule module = new EtlModule();
			//System.out.println(scriptRoot.resolve(scriptcontents[i]));
			module.parse(scriptRoot.resolve(scriptcontents[i]));
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
		return flag;

	}
	public ArrayList<String> identifyETLinModels(String sourceMM, String targetMM) throws Exception {
		ArrayList<String> etlname = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		for (int i = 0; i < modelscontents.length; i++) {
			if(modelscontents[i].endsWith(".etl"))
			{
				EtlModule module = new EtlModule();
				if(modelsRoot.resolve(modelscontents[i]).toFile().exists()) {
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
	
	public boolean findETLinModels(String sourceMM, String targetMM) throws Exception {
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;
		boolean flag = false;
		for (int i = 0; i < modelscontents.length; i++) {
			if(modelscontents[i].endsWith(".etl"))
			{
			EtlModule module = new EtlModule();
			//System.out.println(scriptRoot.resolve(scriptcontents[i]));
			if(modelsRoot.resolve(modelscontents[i]).toFile().exists()) {
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

}
