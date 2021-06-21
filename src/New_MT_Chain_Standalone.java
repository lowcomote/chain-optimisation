

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;

public class New_MT_Chain_Standalone {
	
	public static void main(String[] args) throws Exception {
			Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
			Path modelsRoot = root.getParent().resolve("models");
			Path scriptRoot = root.getParent().resolve("script");
			Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
			
			
			String sourceMM = modelsRoot.resolve("Tree.ecore").toString();
			String interTargetMM = modelsRoot.resolve("Graph.ecore").toString();
			String targetMM = modelsRoot.resolve("SimpleTrace.ecore").toString();
			String sourcemodel=modelsRoot.resolve("Tree2.xmi").toString();
			String targetmodel=genmodelsRoot.resolve("Gen_SimpleTrace2.xmi").toString();
			String newtargetmodel=genmodelsRoot.resolve("Gen_Tree2SimpleTrace2.xmi").toString();
			String intertargetmodel=genmodelsRoot.resolve("Gen_interGraph2.xmi").toString();
			Path interscriptPath=scriptRoot.resolve("Tree2Graph.etl");
			Path scriptPath=scriptRoot.resolve("Graph2SimpleTrace.etl");
			Path newscriptPath=scriptRoot.resolve("Tree2SimpleTrace.etl");
			
			RunETL runETL = new RunETL();
			
			//String intermediateMM = chain(sourceMM, targetMM);
			
			try {
				chain(sourceMM, interTargetMM);
				chain(interTargetMM, targetMM);
				runETL.execute("Tree", "Graph", sourceMM, interTargetMM, sourcemodel, intertargetmodel, interscriptPath);
				
				runETL.execute("Graph", "SimpleTrace", interTargetMM, targetMM, intertargetmodel, targetmodel, scriptPath);
				
				chain(sourceMM,targetMM);
				runETL.execute("Tree", "SimpleTrace", sourceMM, targetMM, sourcemodel, newtargetmodel, newscriptPath);
			} catch (Exception e) {
				System.out.println("No chain found.");
				e.printStackTrace();
			}
			
	}
	
	public static void chain(String sourceMM, String targetMM)
	{
		//Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		//Path modelsRoot = root.getParent().resolve("models");
		//Path scriptRoot = root.getParent().resolve("script");
		//Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		//String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
		//String targetmodel=genmodelsRoot.resolve("Gen_SimpleTrace.xmi").toAbsolutePath().toUri().toString();
		
		File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		String contents[] = metamodelPath.list();
		
		File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
		String scriptcontents[] = scriptPath.list();
		
		Map<String, ArrayList<String>> hash = new HashMap<String,  ArrayList<String>>();
		
		//hash.put(sourceMM, targetMM);
		hash.put(sourceMM, new  ArrayList<String>());
		hash.get(sourceMM).add(targetMM);
		
		System.out.println(hash.keySet()+" -> "+hash.values());
		//System.out.println(hash.get(sourceMM));
		//Graph g= new Graph(contents.length);
		//int index=0;
		//String interMM = contents[0];
		//for(int sc=0; sc<scriptcontents.length; sc++) {
			//for(int i=0; i<contents.length; i++) {
				//for(int j=0;j<contents.length;j++) {
					//if(contents[j]==(metamodelPath+"\\"+sourceMM)) {
					//index=j;
						//chain(sourceMM, interMM);
						//chain(interMM, targetMM);
				//}
						//g.addEdge(j, "Graph.ecore");
						//g.addEdge(j, "SimpleTrace.ecore");
					//}
					//if(contents[j]=="Graph.ecore") {
						//g.addEdge(j, "SimpleTrace.ecore");
					//}
			
					//g.addEdge(index, metamodelPath+"\\"+"Tree.ecore");
					//g.addEdge(index, metamodelPath+"\\"+"Graph.ecore");
					//g.addEdge(index, metamodelPath+"\\"+"SimpleTrace.ecore");
					//}
				//}
				
				//g.printGraph();
				
			}
	
			//return interMM;
		//}
		//return interMM;
			
	}


