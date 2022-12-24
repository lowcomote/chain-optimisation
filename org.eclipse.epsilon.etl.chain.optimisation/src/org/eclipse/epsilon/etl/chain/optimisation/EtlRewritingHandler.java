package org.eclipse.epsilon.etl.chain.optimisation;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.staticanalyser.SubEmfModelFactory;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.chain.selection.ModelProperties;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.parse.EtlUnparser;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class EtlRewritingHandler {

	static Path modelsRoot = Paths.get("models");
	static Path scriptRoot = Paths.get("scripts");
	static Path metamodelsRoot = Paths.get("metamodels");
	static Path genmodelsRoot = Paths.get("models/generatedmodels");

	static File metamodelPath = new File("metamodels");
	static File scriptPath = new File("scripts");
	static String scriptcontents[] = scriptPath.list();
	
	static String sourceMM = metamodelsRoot.resolve("KM3.ecore").toString();
	static String targetMM = metamodelsRoot.resolve("XML.ecore").toString();
	static String sourceModel = modelsRoot.resolve("sample-km3.xmi").toAbsolutePath().toUri().toString();
	static String targetModel = genmodelsRoot.resolve("sample-xml.xmi").toAbsolutePath().toUri().toString();
	
	static Chaining_MT chainingMt = new Chaining_MT();
	static CreateMTCoverage createmtcoverage = new CreateMTCoverage();
	
	public int invokeRewriters(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers)
			throws Exception {
		int index = 0;
		List<EolType> sourceRules = new ArrayList<EolType>();
		List<EolType> targetRules = new ArrayList<EolType>();
		Chaining_MT chainingmt = new Chaining_MT();
		Path metamodelsRoot = Paths.get("metamodels");
		Path modelsRoot = Paths.get("models");
		Path scriptRoot = Paths.get("scripts");
		String sourceMetamodel = null, targetMetamodel = null, sourceModel, targetModel;
		List<ModelDeclaration> mm;
		
		for (EtlModule module1 : modules) {
			EolType type = null;
			int c = 0;
			EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
			
			mm = ((EtlModule) module1).getDeclaredModelDeclarations();
			sourceMetamodel = metamodelsRoot.resolve(mm.get(0).getModel().getName() + ".ecore").toString();
			targetMetamodel = metamodelsRoot.resolve(mm.get(1).getModel().getName() + ".ecore").toString();
			sourceModel = modelsRoot.resolve(mm.get(0).getModel().getName() + ".xmi").toAbsolutePath().toUri()
					.toString();
			targetModel = modelsRoot.resolve(mm.get(0).getModel().getName() + ".xmi").toAbsolutePath().toUri()
					.toString();

//			for (int i = 0; i < ((EtlModule) module1).getTransformationRules().size(); i++) {
//				EolModelElementType type_prev = (EolModelElementType) staticAnalyser
//						.getType(((EtlModule) module1).getTransformationRules().get(i).getSourceParameter());
//				
//		
//				for (int j = 0; j < ((EtlModule) module1).getTransformationRules().get(i).getTargetParameters()
//						.size(); j++) {
//					
//				
//					EolModelElementType type_next = (EolModelElementType) staticAnalyser
//							.getType(((EtlModule) module1).getTransformationRules().get(i).getTargetParameters().get(j));
//			
//				StatementBlock ruleblock = (StatementBlock) ((EtlModule) module1).getTransformationRules().get(i)
//						.getBody().getBody();
//				
//				for (int k = 0; k < ruleblock.getStatements().size(); k++) {
//					
//					List<ArrayList<String>> deleteIndex = chainingmt.deletetrindex(sourceModel, sourceModel, targetModel, targetModel);
//					String[] splitIndex = null;
//					for(int d=0;d<deleteIndex.size();d++)
//					{
//						for(int x=0;x<deleteIndex.get(d).size();x++)
//						{
//							splitIndex = deleteIndex.get(d).get(x).split("\\s+");
//							System.out.println("Delete statement "+splitIndex[0]+" in transformation rule "+splitIndex[4]);
//						}
//					}
					
//					Statement stName = ruleblock.getStatements().remove(k);
//					Statement delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0]));
//				}
//				}
//			}
			try {
				if (index == 0)
					for (TransformationRule tr : module1.getDeclaredTransformationRules()) {
						sourceRules.add(staticAnalyser.getType(tr.getSourceParameter()));
					}
				else
					for (TransformationRule tr : module1.getDeclaredTransformationRules()) {
						for (Parameter target : tr.getTargetParameters()) {
							type = staticAnalyser.getType(target);
							targetRules.add(type);
							
							//StatementBlock ruleblock = (StatementBlock) tr.getBody().getBody();
							if (!(sourceRules.contains(type)))
								module1.getTransformationRules().remove(index);
							else
								continue;
//								c++;

						}
//						if (c == 0)
//							System.out.println("No matched transformation rule available");

					}
//				index++;
			} catch (Exception e) {
				System.out.println(e);
//				System.out.println("No transformation rule to be removed!!!");
			}
			index++;

		}
		Collections.reverse(modules);

//		String sourceMetamodel = null, targetMetamodel = null, sourceModel, targetModel;
//		List<ModelDeclaration> mm;
		
		@SuppressWarnings("unused")
		StringProperties sourceProperties = new StringProperties();
		@SuppressWarnings("unused")
		StringProperties targetProperties = new StringProperties();

		ModelProperties modelProperties = new ModelProperties();
//		Path metamodelsRoot = Paths.get("metamodels");
//		Path modelsRoot = Paths.get("models");
//		Path scriptRoot = Paths.get("scripts");

		int calc, total = 0;

		for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------");
			System.err.println("\n" + new EtlUnparser().unparse(module));
			mm = ((EtlModule) module).getDeclaredModelDeclarations();
			sourceMetamodel = metamodelsRoot.resolve(mm.get(0).getModel().getName() + ".ecore").toString();
			targetMetamodel = metamodelsRoot.resolve(mm.get(1).getModel().getName() + ".ecore").toString();
			sourceModel = modelsRoot.resolve(mm.get(0).getModel().getName() + ".xmi").toAbsolutePath().toUri()
					.toString();
			targetModel = modelsRoot.resolve(mm.get(0).getModel().getName() + ".xmi").toAbsolutePath().toUri()
					.toString();
			
			
//			List<ArrayList<String>> deleteIndex = chainingmt.deletetrindex(sourceModel, sourceModel, targetModel, targetModel);
//			
//			for(int d=0;d<deleteIndex.size();d++)
//			{
//				for(int x=0;x<deleteIndex.get(d).size();x++)
//				{
//					String[] splitIndex = deleteIndex.get(d).get(x).split("\\s+");
//					System.out.println("Delete statement "+splitIndex[0]+" in transformation rule "+splitIndex[4]);
//				}
//			}
			
			FileWriter fw = new FileWriter(scriptRoot.resolve("Script" + ".etl").toString());
			if (new EtlUnparser().unparse(module).startsWith("pre"))
				fw.write(new EtlUnparser().unparse(module));
			fw.close();

			sourceProperties = modelProperties.properties(mm.get(0).getModel().getName(), sourceMetamodel, sourceModel,
					"true", "false");
			targetProperties = modelProperties.properties(mm.get(1).getModel().getName(), targetMetamodel, targetModel,
					"false", "true");

			calc = chainingmt.calculateMTChain(module);

			total += calc;

			scriptRoot.resolve("Script" + ".etl").toFile().delete();

			System.out.println("No. of operator/expression in model transformation " + module.getSourceFile().getName()
					+ " is " + calc);
			System.out.println(sourceMetamodel);
			System.out.println(targetMetamodel);
			System.out.println(module.getTransformationRules());
		}
		return total;
	}

	public void invokeRewriters_2(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers)
			throws Exception {
		int index = 0;
		List<EolType> sourceRules = new ArrayList<EolType>();
		List<EolType> targetRules = new ArrayList<EolType>();
		Chaining_MT chainingmt = new Chaining_MT();
		Path metamodelsRoot = Paths.get("metamodels");
		Path modelsRoot = Paths.get("models");
		Path scriptRoot = Paths.get("scripts");
		String sourceMetamodel = null, targetMetamodel = null, sourceModel, targetModel;
		List<ModelDeclaration> mm;
		StatementBlock ruleblock;
		Statement delete_stLine;
		
		for (EtlModule module1 : modules) {
			EolType type = null;
			int c = 0;
			EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
			
			mm = ((EtlModule) module1).getDeclaredModelDeclarations();
			sourceMetamodel = metamodelsRoot.resolve(mm.get(0).getModel().getName() + ".ecore").toString();
			targetMetamodel = metamodelsRoot.resolve(mm.get(1).getModel().getName() + ".ecore").toString();
			sourceModel = modelsRoot.resolve(mm.get(0).getModel().getName() + ".xmi").toAbsolutePath().toUri()
					.toString();
			targetModel = modelsRoot.resolve(mm.get(0).getModel().getName() + ".xmi").toAbsolutePath().toUri()
					.toString();
			
			List<ArrayList<String>> delrule_list = chainingmt.deletetrindex(sourceModel, sourceModel, targetModel, targetModel);

			String[] splitIndex = null;
//			int rule_no = 0;
			for(int d=0;d<delrule_list.size();d++)
			{
				for(int x=0;x<delrule_list.get(d).size();x++)
				{
					splitIndex = delrule_list.get(d).get(x).split("\\s+");
					System.out.println("Delete statement "+splitIndex[0]+" with element "+splitIndex[1]+" in transformation rule "+splitIndex[4]+" in transformation "+splitIndex[5]);
					//create array of module1
					//EtlModule module1 = new EtlModule();
					//EtlStaticAnalyser staticAnlayser = new EtlStaticAnalyser();
					System.out.println(scriptRoot.resolve(splitIndex[5]+".etl"));
					
					module1.parse(scriptRoot.resolve(splitIndex[5]+".etl"));
					for (ModelDeclaration modelDeclaration : module1.getDeclaredModelDeclarations()) {
						if (modelDeclaration.getDriverNameExpression().getName().equals("EMF")) {
							staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
						}
					}
					staticAnalyser.validate(module1);

					System.out.println(splitIndex[0]);
					String tp_target;
					for (TransformationRule tr : module1.getDeclaredTransformationRules()) {
						String tp_source = tr.getSourceParameter().getTypeName().split("!")[1];
						//System.out.println("Transformation source rules: "+tp_source);
						
						for(int tp=0;tp<tr.getTargetParameters().size();tp++)
						{
							tp_target = tr.getTargetParameters().get(tp).getTypeName().split("!")[1];
							//System.out.println("Transformation target rules: "+tp_target);
							if(tp_source.equals(splitIndex[4].split("2")[0]) && 
									tp_target.equals(splitIndex[4].split("2")[1]))
							{
							ruleblock = (StatementBlock) tr.getBody().getBody();
							//System.out.println(ruleblock.getStatements().get(0));
							delete_stLine = ruleblock.getStatements().remove(Integer.parseInt(splitIndex[0])-1);
							System.out.println("Delete line "+delete_stLine);
					}
						}
					}
						break;
					}
				}

		}
		Collections.reverse(modules);
		for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------");
			System.err.println(new EtlUnparser().unparse(module));
		}

	}

	public void invokeRewriters_3(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers) 
			throws Exception {
		int index = 0;
		List<EolType> sourceRules = new ArrayList<EolType>();
		List<EolType> targetRules = new ArrayList<EolType>();
	for(EtlModule module : modules) {
//		System.out.println("------------------");
//		System.out.println(module.getSourceFile().getName());
//		System.out.println("------------------");
		EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
		try {
			if(index == 0)
			for(TransformationRule tr: module.getDeclaredTransformationRules()) {
				sourceRules.add(staticAnalyser.getType(tr.getSourceParameter()));
			}
			else
			for(TransformationRule tr: module.getDeclaredTransformationRules()) {
				for(Parameter target: tr.getTargetParameters()) {
					EolType type = staticAnalyser.getType(target);
					
				targetRules.add(type);
//				System.out.println(target);
				if(!(sourceRules.contains(type)))
					module.getTransformationRules().remove(index);
				else
					continue;
				}
//				index++;
			}
//			index++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println(e);
//			System.out.println("No transformation rule to be removed.");
		}
		index++;
		
	}
	Collections.reverse(modules);
	for (EtlModule module : modules) {
		System.out.println("------------------");
		System.out.println(module.getSourceFile().getName());
		System.out.println("------------------");
		System.err.println(new EtlUnparser().unparse(module));
	}
	
//	sourceRules.toString();
//	targetRules.toString();
	}

//	public void invokeRewriters1(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers)
//			throws Exception {
//		
//		List<Integer> indicesToRemove = new ArrayList<Integer>();
//		List<EolType> sourceRules = new ArrayList<EolType>();
//		List<EolType> targetRules = new ArrayList<EolType>();
//		for (EtlModule module : modules) {
//			int index = 0;
//			EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
//			try {
//				if (module.equals(modules.get(0)))
//					for (TransformationRule tr : module.getDeclaredTransformationRules()) {
//						sourceRules.add(staticAnalyser.getType(tr.getSourceParameter()));
//					}
//				else
//					for (TransformationRule tr : module.getDeclaredTransformationRules()) {
//						for (Parameter target : tr.getTargetParameters()) {
//							EolType type = staticAnalyser.getType(target);
//							targetRules.add(type);
//							if (!(sourceRules.contains(type)))
//								indicesToRemove.add(index);
//						}
//						index++;
//
//					}
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//			Collections.reverse(indicesToRemove);
//			for(int one: indicesToRemove)
//			module.getTransformationRules().remove(one);
//			indicesToRemove.clear();
//			sourceRules.clear();
//			for (TransformationRule tr : module.getTransformationRules()) {
//				sourceRules.add(staticAnalyser.getType(tr.getSourceParameter()));
//			}
//			
//		}
//		Collections.reverse(modules);
//		for (EtlModule module : modules) {
//			System.out.println("------------------");
//			System.out.println(module.getSourceFile().getName());
//			System.out.println("------------------");
//			System.err.println(new EtlUnparser().unparse(module));
//		}
//	}

	public void invokeRewriters1(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers) 
			throws Exception {
		int index = 0;
		List<EolType> sourceRules = new ArrayList<EolType>();
		List<EolType> targetRules = new ArrayList<EolType>();
	for(EtlModule module : modules) {
//		System.out.println("------------------");
//		System.out.println(module.getSourceFile().getName());
//		System.out.println("------------------");
		EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
		try {
			if(index == 0)
			for(TransformationRule tr: module.getDeclaredTransformationRules()) {
				sourceRules.add(staticAnalyser.getType(tr.getSourceParameter()));
			}
			else
			for(TransformationRule tr: module.getDeclaredTransformationRules()) {
				for(Parameter target: tr.getTargetParameters()) {
					EolType type = staticAnalyser.getType(target);
					
				targetRules.add(type);
//				System.out.println(target);
				if(!(sourceRules.contains(type)))
					module.getTransformationRules().remove(index);
				else
					continue;
				}
//				index++;
			}
//			index++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println(e);
//			System.out.println("No transformation rule to be removed.");
		}
		index++;
		
	}
	Collections.reverse(modules);
	for (EtlModule module : modules) {
		System.out.println("------------------");
		System.out.println(module.getSourceFile().getName());
		System.out.println("------------------");
		System.err.println(new EtlUnparser().unparse(module));
	}
	
//	sourceRules.toString();
//	targetRules.toString();
	}
	

	
	public static double calculateTransformationCoverage(String sourceModel1, String sourceMM1, String targetModel1, String targetMM1) throws Exception {
		
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
						
							for(Double element : CreateMTCoverage.calculateMTCoverage_new(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)))
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
	
public static double calculateTransformationCoverage_Optimized() throws Exception {
	
	Chaining_MT chainingMt = new Chaining_MT();
	List<ArrayList<String>> chain = chainingMt.identifyChain(sourceModel, sourceMM, targetModel, targetMM);
	double coverage_chain = 0;
	
	for (int j = 0; j < chain.size(); j++) {
		coverage_chain = 1;
		System.out.println("\nChain" + (j + 1) + " " + chain.get(j) + "\n");
	for (int i = 0; i < chain.get(j).size(); i++) {
//			EtlModule module1 = new EtlModule();
		double max_cov_mt = 0;
			if (i + 1 < chain.get(j).size()) {
				ArrayList<String> x = chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),metamodelPath + "/" + chain.get(i).get(j+1));
				for(String arr : x)
				{
					if(arr.startsWith("Optimized1_"))
					{
//					for(Double element : chainingMt.calculateMTCoverage(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1)))
//					{
//						//if(element > max_cov_mt) {
						double element = chainingMt.calculateMTCoverage1(metamodelPath + "/" + chain.get(j).get(i),metamodelPath + "/" + chain.get(j).get(i+1));
				          //  }
						//for(int e=0;e<chainingMt.identifyETL(metamodelPath + "/" + chain.get(i).get(j),metamodelPath + "/" + chain.get(i).get(j+1)).size();e++)
							
							
									System.out.println("\n" + "Individual coverage of optimized MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
								              +" in "+arr + " is " + element);
									 max_cov_mt = element;
								//}
							//}
//						System.out.println("\n" + "Individual coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
//					               + " is " + element);
					//}
					
//					System.out.println("\n" + "Maximum coverage of a MT " + (metamodelPath + "/" + chain.get(j).get(i)) + " -> " + (metamodelPath + "/" + chain.get(j).get(i+1))
//				               + " is " + max_cov_mt);
				        coverage_chain *= max_cov_mt;
					}
				}
		
			}

		}
//	System.out.println("\nTotal coverage of chain is " + coverage_chain + "\n");
	}
	return coverage_chain;
}

	public static void calculatetime(String sourceModel1, String sourceMM1, String targetModel1, String targetMM1) throws Exception
	{
		EtlChainOptimiser.calculateTransformationCoverageOnOptimizedTransformation(sourceModel1, sourceMM1, targetModel1, targetMM1);
		//calculateTransformationCoverage(sourceModel1, sourceMM1, targetModel1, targetMM1);
		double start1 = System.currentTimeMillis();
		//System.out.println("Optimized1_"+sourceMM1+" "+"Optimized1_"+targetMM1);
		EtlChainOptimiser.executechainsofetl(sourceModel1, sourceMM1, targetModel1, targetMM1);
		System.out.println("Time = "+(System.currentTimeMillis()-start1)/1000+" seconds.");
	}

}