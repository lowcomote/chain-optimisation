package org.eclipse.epsilon.etl.chain.optimisation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chain_MT;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class CreateMTCoverage {
	
	static Path modelsRoot = Paths.get("models");
	static Path scriptRoot = Paths.get("scripts");
	static Path metamodelsRoot = Paths.get("metamodels");
	Path genmodelsRoot = Paths.get("models/generatedmodels");
	
	static File scriptPath = new File("scripts");
	static String scriptcontents[] = scriptPath.list();

	File metamodelPath = new File("metamodels");
//	static File scriptPath = new File("scripts");
//	static String scriptcontents[] = scriptPath.list();
	
//	static String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
//	static String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
//	static String sourceModel = modelsRoot.resolve("Tree.xmi").toAbsolutePath().toUri().toString();
//	static String targetModel = genmodelsRoot.resolve("DB.xmi").toAbsolutePath().toUri().toString();
	
	static Chaining_MT chainingMt = new Chaining_MT();
	
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
	
	public static ArrayList<String> identifyETL(String sourceMM, String targetMM) throws Exception {
		ArrayList<String> etlname = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null;
		List<ModelDeclaration> mm;

		for (int i = 0; i < scriptcontents.length; i++) {
			
			if(scriptRoot.resolve(scriptcontents[i]).toFile().exists()) {
			
			EtlModule module = new EtlModule();
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

//			sourceMetamodel = mm.get(0).getModel().getName();
//			targetMetamodel = mm.get(1).getModel().getName();

//			registerMM(metamodelsRoot.resolve(sourceMetamodel+".ecore").toAbsolutePath().toString());
//			registerMM(metamodelsRoot.resolve(targetMetamodel+".ecore").toAbsolutePath().toString());
			
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
	
	public static ArrayList<Double> calculateMTCoverage_new(String sourceMM, String targetMM)
			throws Exception {
	ArrayList<Double> tot_cov = new ArrayList<Double>();
	
	for(int e=0;e<chainingMt.identifyETL(sourceMM,targetMM).size();e++)
	{
		EtlModule module1 = new EtlModule();
		
		module1.parse(scriptRoot.resolve(chainingMt.identifyETL(sourceMM, targetMM).get(e)));
		System.out.println("\n"+scriptRoot.resolve(chainingMt.identifyETL(sourceMM, targetMM).get(e)));
	
	EolStaticAnalyser staticAnalyser = new EolStaticAnalyser();
	staticAnalyser.validate(module1);
	ArrayList<String> src_dep = chainingMt.srcDependency2(module1);
	
	
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
				String x = chainingMt.executeEOL1(sourceMM111, metaMM, code);
				
				count = NumberUtils.toDouble(x.trim());
				System.out.println("Total metaclasses and structural feature in source "+type.getTypeName()+ " metamodel is "+count);
				
//				scriptRoot.resolve("newDep" + ".eol").toFile().delete();
				
			}	
		}
	
		cov = (float) count_feature/count;
		tot_cov.add(cov);
	
	}
	
	return tot_cov;
		
}


	
public double calculateTransformationCoverage(String sourceModel1, String sourceMM1, String targetModel1, String targetMM1) throws Exception {
		
//		for (int i = 0; i < scriptcontents.length; i++) {
//			if(scriptRoot.resolve(scriptcontents[i]).toFile().exists()) {
//				System.out.println(scriptcontents[i]);
//			}
//		}
			
//			chainingMt.registerMM(sourceMM1);
//			chainingMt.registerMM(targetMM1);
//			ArrayList<String> two = chainingMt.identifyChain_two(sourceModel1, sourceMM1, targetModel1, targetMM1);
//			List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel1, sourceMM1, targetModel1, targetMM1);
//			Chain_MT cm = new Chain_MT();
//			boolean etl1 = cm.findETLinModels(sourceMM1, targetMM1);
//			//System.out.println(identifyETLinModels(sourceMM, targetMM));
//			if (etl1) {
//				ArrayList<String> x = cm.identifyETLinModels(sourceMM1, targetMM1);
//			for(int id=0;id<cm.identifyETLinModels(sourceMM1, targetMM1).size();id++) {
//				
//				//System.out.println("qwerty: "+x);
//				Files.move(modelsRoot.resolve(x.get(id)),scriptRoot.resolve(x.get(id)), StandardCopyOption.REPLACE_EXISTING);
//				//break;
//			}
//			}
//			if (!two.isEmpty())
//				chain.add(two);
//			ArrayList<String> samemm=new ArrayList<String>();
//			if(chainingMt.findETL(targetMM, targetMM))
//			{
//				samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//				samemm.add(targetMM.substring(targetMM.indexOf("\\")+1,targetMM.length()));
//				chain.add(samemm);
//			}
//			System.out.println("Chains: "+chain);
//			EtlChainOptimiser eco = new EtlChainOptimiser();
			List<ArrayList<String>> chain = EtlChainOptimiser.createChain(sourceModel1, sourceMM1, targetModel1, targetMM1);
			
			double coverage_chain = 0;
			for (int j = 0; j < chain.size(); j++) {
				coverage_chain = 1;
				System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
			for (int i = 0; i < chain.get(j).size(); i++) {
//					EtlModule module1 = new EtlModule();
					double max_cov_mt = 0;
					if (i + 1 < chain.get(j).size()) {

						chainingMt.registerMM(metamodelsRoot.resolve(chain.get(j).get(i)).toAbsolutePath().toString());
						chainingMt.registerMM(metamodelsRoot.resolve(chain.get(j).get(i+1)).toAbsolutePath().toString());
						
							for(Double element : calculateMTCoverage_new(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)))
							//for(Double element : chainingMt.calculateMTCoverage(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)))
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


}
