

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.EtlModule;

public class Chain_MT {
	public static void main(String[] args) throws Exception {
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		String contents[] = metamodelPath.list();
		
//		File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
//		String scriptcontents[] = scriptPath.list();
		
		Chaining_MT chainingmt = new Chaining_MT();
		ModelProperties modelProperties=new ModelProperties();
		
		EmfModel emfmodel1 = new EmfModel();
		EmfModel emfmodel2 = new EmfModel();
		EmfModel emfmodel3 = new EmfModel();
		
		String sourceMM = modelsRoot.resolve("Tree.ecore").toString();
		String interTargetMM = modelsRoot.resolve("Graph.ecore").toString();
		String targetMM = modelsRoot.resolve("SimpleTrace.ecore").toString();
		/*for(int i=0; i<contents.length; i++) {
			emfmodel1.setName(contents[i].replaceFirst("[.][^.]+$", ""));
			emfmodel2.setName(contents[i].replaceFirst("[.][^.]+$", ""));
			emfmodel3.setName(contents[i].replaceFirst("[.][^.]+$", ""));
		}*/
		//System.out.println(contents[0].replaceFirst("[.][^.]+$", ""));
		//Chaining_MT chainingmt = new Chaining_MT();
		
		emfmodel1.setName("Tree");
		emfmodel1.setModelFile(modelsRoot.resolve("Tree2.xmi").toString());
		emfmodel1.setMetamodelFile(sourceMM);
		emfmodel1.setReadOnLoad(true);
		emfmodel1.setStoredOnDisposal(false);
		
		emfmodel2.setName("Graph");
		emfmodel2.setModelFile(genmodelsRoot.resolve("Gen_Graph1.xmi").toString());
		emfmodel2.setMetamodelFile(interTargetMM);
		emfmodel2.setReadOnLoad(false);
		emfmodel2.setStoredOnDisposal(true);
		
		emfmodel3.setName("SimpleTrace");
		emfmodel3.setModelFile(genmodelsRoot.resolve("Gen_SimpleTrace1.xmi").toString());
		emfmodel3.setMetamodelFile(targetMM);
		emfmodel3.setReadOnLoad(false);
		emfmodel3.setStoredOnDisposal(true);
		
		EtlModule module1 = new EtlModule();
		EtlModule module2 = new EtlModule();
		
		
		
		chainingmt.calculateMTChain(module1);
		chainingmt.calculateMTChain(module2);
		
		module1.parse(scriptRoot.resolve("Tree2Graph.etl"));
		
		module2.parse(scriptRoot.resolve("Graph2SimpleTrace.etl"));
		
		emfmodel1.load();
		emfmodel2.load();
		emfmodel3.load();
		
		
		
		module1.getContext().getModelRepository().addModel(emfmodel1);
		module1.getContext().getModelRepository().addModel(emfmodel2);
		//module1.getContext().setModule(module1);
		
		IModel m1 = module1.getContext().getModelRepository().getModels().get(0);
		IModel m2 = module1.getContext().getModelRepository().getModels().get(1);
		
		//emfmodel1.setName(m1.getName());
		//emfmodel2.setName(m2.getName());
		
		System.out.println(m1.getName() + " -> "+m2.getName());
		
		module2.getContext().getModelRepository().addModel(emfmodel2);
		module2.getContext().getModelRepository().addModel(emfmodel3);
		
		
		IModel m3 = module2.getContext().getModelRepository().getModels().get(0);
		IModel m4 = module2.getContext().getModelRepository().getModels().get(1);
		System.out.println(m3.getName() + " -> "+m4.getName());
		
		module1.getContext().setModule(module1);
		module2.getContext().setModule(module2);
		//IModel m = module1.getContext().getModelRepository().getModels().get(0);
		//System.out.println(m.getName());
//		int tot1 = chainingmt.calculateMTChain(module1);
//		int tot2 = chainingmt.calculateMTChain(module2);
		
		module1.execute();
		
		module2.execute();
		
		emfmodel1.dispose();
		emfmodel2.dispose();
		emfmodel3.dispose();
		
		System.out.println("Chain Transformation Completed.");
		
	}

}
