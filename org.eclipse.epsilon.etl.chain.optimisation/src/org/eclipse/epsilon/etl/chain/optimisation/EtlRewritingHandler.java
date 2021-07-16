package org.eclipse.epsilon.etl.chain.optimisation;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.chain.selection.Chaining_MT;
import org.eclipse.epsilon.etl.chain.selection.EtlPreExecuteConfiguration;
import org.eclipse.epsilon.etl.chain.selection.ModelProperties;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.parse.EtlUnparser;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class EtlRewritingHandler {

	public int invokeRewriters(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers) throws Exception {
		int index = 0;
		List<EolType> sourceRules = new ArrayList<EolType>();
		List<EolType> targetRules = new ArrayList<EolType>();
		for (EtlModule module1 : modules) {
			EolType type = null;
			int c=0;
			EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
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
							if (!(sourceRules.contains(type)))
								module1.getTransformationRules().remove(index);
							else
								c++;

						}
//					if(tr.getTargetParameters().size()>0)
						if(c==0)
							System.out.println("No transformation rule available");
							
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println(e);
			}
			index++;

		}
		Collections.reverse(modules);
		
		ArrayList<String> str = new ArrayList<String>();
		String sourceMetamodel = null, targetMetamodel = null, sourceModel, targetModel;
		List<ModelDeclaration> mm;
		Chaining_MT chainingmt = new Chaining_MT();
		StringProperties sourceProperties, targetProperties;
		ModelProperties modelProperties = new ModelProperties();
		Path metamodelsRoot = Paths.get("metamodels");
		Path modelsRoot = Paths.get("models");
		Path scriptRoot = Paths.get("scripts");
		
		EtlRunConfiguration exec=null;
		
		int calc, total=0;
//		ArrayList<Integer> total = null;
		
		for (EtlModule module : modules) {
			System.out.println("------------------");
			System.out.println(module.getSourceFile().getName());
			System.out.println("------------------");
			System.err.println("\n"+new EtlUnparser().unparse(module));
//			str.add(new EtlUnparser().unparse(module));
			//System.out.println(modules);
			mm = ((EtlModule) module).getDeclaredModelDeclarations();
			sourceMetamodel =  metamodelsRoot.resolve(mm.get(0).getModel().getName()+".ecore").toString();
			targetMetamodel =  metamodelsRoot.resolve(mm.get(1).getModel().getName()+".ecore").toString();
			sourceModel = modelsRoot.resolve(mm.get(0).getModel().getName()+".xmi").toAbsolutePath().toUri().toString();
			targetModel = modelsRoot.resolve(mm.get(0).getModel().getName()+".xmi").toAbsolutePath().toUri().toString();;
			
			FileWriter fw=new FileWriter(scriptRoot.resolve("Script"+".etl").toString());
			if(new EtlUnparser().unparse(module).startsWith("pre"))
				fw.write(new EtlUnparser().unparse(module));
			fw.close();
			
//			EtlModule etlscript = module;
			
			sourceProperties = modelProperties.properties(mm.get(0).getModel().getName(), sourceMetamodel, sourceModel,"true","false");
			targetProperties = modelProperties.properties(mm.get(1).getModel().getName(), targetMetamodel, targetModel,"false","true");
			
//			exec = chainingmt.executeETL(sourceModel, sourceMetamodel, targetModel, targetMetamodel);
//			module.parse(scriptRoot.resolve("Script"+".etl"));
			calc = chainingmt.calculateMTChain(module);
			
			total+=calc;
//			total.add(calc);
			
//			EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
//					.withScript(scriptRoot.resolve("Script"+".etl"))
//					.withModel(new EmfModel(), sourceProperties)
//					.withModel(new EmfModel(), targetProperties)
//					.withParameter("parameterPassedFromJava", "Hello from pre15")
//					.withProfiling()
//					.build();
//			
//			EtlPreExecuteConfiguration sm1 = new EtlPreExecuteConfiguration(runConfig);
//			
//			sm1.run();
			
			scriptRoot.resolve("Script"+".etl").toFile().delete();
			
//			runConfig.dispose();
			
			System.out.println("No. of operator/expression in model transformation "+module.getSourceFile().getName()+" is "+calc);
			System.out.println(sourceMetamodel);
			System.out.println(targetMetamodel);
			System.out.println(module.getTransformationRules());
//			System.out.println(module.getSourceFile());
//			System.out.println(module);
		}
//		System.out.println("\nTotal structural features in an optimized chain "+total);
//		sourceRules.toString();
//		targetRules.toString();
		return total;
	}
	
	public void invokeRewriters1(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers) throws Exception {
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
				if(!(sourceRules.contains(type)))
					module.getTransformationRules().remove(index);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println(e);
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

}