

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;

public class Chain_MT {
	public static void main(String[] args) throws Exception {
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path metamodelsRoot = root.getParent().resolve("metamodels");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		String contents[] = metamodelPath.list();
		
//		File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
//		String scriptcontents[] = scriptPath.list();
		
		Chaining_MT chainingmt = new Chaining_MT();
		ModelProperties modelProperties=new ModelProperties();
		
		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		
		List<ArrayList<String>> l = chainingmt.identifychain(sourcemodel, sourceMM, targetmodel, targetMM);
		//}
			EtlRunConfiguration exec=null;
			IEolModule module = null;
			int min=9999;
			ArrayList<String> index = null;
			System.out.println("\n");
			int[] sum = new int[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				System.out.println("Chain"+(i+1)+" "+l.get(i)+"\n");
				
				
//				chainingmt.calculateMTChain(sourcemodel, sourceMM, targetmodel, targetMM);
				//System.out.println(l.get(i).get(0));
				int total=0;
				for(int j=0;j<l.get(i).size();j++)
				{
					EtlModule module1 = new EtlModule();
					
					if(j+1<l.get(i).size())
					{
						
						//System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
						
//						Path newsourcemodelpath = modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")+".xmi");
//						String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
//						
//						Path newtargetmodelpath = modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")+".xmi");
//						String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
						module1.parse(scriptRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")+"2"+l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")+".etl"));
						//chainingmt.calculateMTChain(newsourcemodel, metamodelPath+"\\"+l.get(i).get(j), newtargetmodel, metamodelPath+"\\"+l.get(i).get(j+1));
						
						//exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"\\"+l.get(i).get(j), newtargetmodel, metamodelPath+"\\"+l.get(i).get(j+1));
						//module=exec.getModule();
						total = chainingmt.calculateMTChain(module1);
						sum[i]=sum[i]+total;
					}
					
					//else
						//System.out.println("No MM availabe for transformation");
					
					
				}
				if(sum[i]<min)
				{
					min=sum[i];
					index=l.get(i);
				}
				
				System.out.println("Total statements in a chain: "+sum[i]+"\n");
				
			}
			
			System.out.println("\n------------------Executing best chain--------------------");
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
			
			System.out.println("\nMT Chain "+index+" has minimum statements of " + min);
			
		
//		EmfModel emfmodel1 = new EmfModel();
//		EmfModel emfmodel2 = new EmfModel();
//		EmfModel emfmodel3 = new EmfModel();
		
//		String sourceMM = modelsRoot.resolve("Tree.ecore").toString();
//		String interTargetMM = modelsRoot.resolve("Graph.ecore").toString();
//		String targetMM = modelsRoot.resolve("SimpleTrace.ecore").toString();
		/*for(int i=0; i<contents.length; i++) {
			emfmodel1.setName(contents[i].replaceFirst("[.][^.]+$", ""));
			emfmodel2.setName(contents[i].replaceFirst("[.][^.]+$", ""));
			emfmodel3.setName(contents[i].replaceFirst("[.][^.]+$", ""));
		}*/
		//System.out.println(contents[0].replaceFirst("[.][^.]+$", ""));
		//Chaining_MT chainingmt = new Chaining_MT();
		
//		emfmodel1.setName("Tree");
//		emfmodel1.setModelFile(modelsRoot.resolve("Tree2.xmi").toString());
//		emfmodel1.setMetamodelFile(sourceMM);
//		emfmodel1.setReadOnLoad(true);
//		emfmodel1.setStoredOnDisposal(false);
//		
//		emfmodel2.setName("Graph");
//		emfmodel2.setModelFile(genmodelsRoot.resolve("Gen_Graph1.xmi").toString());
//		emfmodel2.setMetamodelFile(interTargetMM);
//		emfmodel2.setReadOnLoad(false);
//		emfmodel2.setStoredOnDisposal(true);
//		
//		emfmodel3.setName("SimpleTrace");
//		emfmodel3.setModelFile(genmodelsRoot.resolve("Gen_SimpleTrace1.xmi").toString());
//		emfmodel3.setMetamodelFile(targetMM);
//		emfmodel3.setReadOnLoad(false);
//		emfmodel3.setStoredOnDisposal(true);
//		
//		EtlModule module1 = new EtlModule();
//		EtlModule module2 = new EtlModule();
		
		
		
//		chainingmt.calculateMTChain(module1);
//		chainingmt.calculateMTChain(module2);
//		
//		module1.parse(scriptRoot.resolve("Tree2Graph.etl"));
//		
//		module2.parse(scriptRoot.resolve("Graph2SimpleTrace.etl"));
//		
//		emfmodel1.load();
//		emfmodel2.load();
//		emfmodel3.load();
//		
//		
//		
//		module1.getContext().getModelRepository().addModel(emfmodel1);
//		module1.getContext().getModelRepository().addModel(emfmodel2);
		//module1.getContext().setModule(module1);
		
//		IModel m1 = module1.getContext().getModelRepository().getModels().get(0);
//		IModel m2 = module1.getContext().getModelRepository().getModels().get(1);
//		
//		//emfmodel1.setName(m1.getName());
//		//emfmodel2.setName(m2.getName());
//		
//		System.out.println(m1.getName() + " -> "+m2.getName());
//		
//		module2.getContext().getModelRepository().addModel(emfmodel2);
//		module2.getContext().getModelRepository().addModel(emfmodel3);
//		
//		
//		IModel m3 = module2.getContext().getModelRepository().getModels().get(0);
//		IModel m4 = module2.getContext().getModelRepository().getModels().get(1);
//		System.out.println(m3.getName() + " -> "+m4.getName());
//		
//		module1.getContext().setModule(module1);
//		module2.getContext().setModule(module2);
//		//IModel m = module1.getContext().getModelRepository().getModels().get(0);
//		//System.out.println(m.getName());
////		int tot1 = chainingmt.calculateMTChain(module1);
////		int tot2 = chainingmt.calculateMTChain(module2);
//		
//		module1.execute();
//		
//		module2.execute();
		
//		emfmodel1.dispose();
//		emfmodel2.dispose();
//		emfmodel3.dispose();
//		
//		System.out.println("Chain Transformation Completed.");
		
	}

}
