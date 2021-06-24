

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;

public class MT_Engine {
	
	public static void main(String[] args) throws Exception {
	//public void getChain() throws Exception {
		//execute();
		//chaining();
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path metamodelsRoot = root.getParent().resolve("metamodels");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		
		File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		String contents[] = metamodelPath.list();
		
		Chaining_MT chainingmt = new Chaining_MT();
		//chainingmt.findchain(sourcemodel, sourceMM, targetmodel, targetMM);
		//ArrayList<ArrayList<String>> l = chainingmt.runChain(sourcemodel, sourceMM, targetmodel, targetMM);
		//chainingmt.chaintwo(sourcemodel, sourceMM, targetmodel, targetMM);
		//ArrayList<ArrayList<String>> l = chainingmt.chaintwo(sourcemodel, sourceMM, targetmodel, targetMM);
		
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
			
			
//			chainingmt.calculateMTChain(sourcemodel, sourceMM, targetmodel, targetMM);
			//System.out.println(l.get(i).get(0));
			int total=0;
			for(int j=0;j<l.get(i).size();j++)
			{
				
				if(j+1<l.get(i).size())
				{
					
					//System.out.println(l.get(i).get(j)+" -> "+l.get(i).get(j+1));
					
					Path newsourcemodelpath = modelsRoot.resolve(l.get(i).get(j).replaceFirst("[.][^.]+$", "")+".xmi");
					String newsourcemodel = newsourcemodelpath.toAbsolutePath().toUri().toString();
					
					Path newtargetmodelpath = modelsRoot.resolve(l.get(i).get(j+1).replaceFirst("[.][^.]+$", "")+".xmi");
					String newtargetmodel = newtargetmodelpath.toAbsolutePath().toUri().toString();
					
					//chainingmt.calculateMTChain(newsourcemodel, metamodelPath+"\\"+l.get(i).get(j), newtargetmodel, metamodelPath+"\\"+l.get(i).get(j+1));
					
					exec = chainingmt.executeETL(newsourcemodel, metamodelPath+"\\"+l.get(i).get(j), newtargetmodel, metamodelPath+"\\"+l.get(i).get(j+1));
					module=exec.getModule();
					total = chainingmt.calculateMTChain(module);
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
		
		System.out.println("\nMT Chain "+index+" has minimum statements of " + min);
		
	//public static void execute() throws Exception {
		/*
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path metamodelsRoot = root.getParent().resolve("metamodels");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		String contents[] = metamodelPath.list();
		
		//Chaining_MT chainingmt = new Chaining_MT();
		
		File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
		String scriptcontents[] = scriptPath.list();
		
		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String targetMM = metamodelsRoot.resolve("TM.ecore").toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		String intermodel;
		String intermetamodel = null;
		String intermetamodel1 = null;
		
		Chaining_MT chainingmt = new Chaining_MT();
		//String intertargetmetamodel = null;
		//Path intermodelpath=modelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".xmi");
		//intermodel=intermodelpath.toString();
		//String sourcemodel="Tree2.xmi";
		//String sourceMM="Tree.ecore";
		//String targetMM="SimpleTrace.ecore";
		System.out.println("Model Transformation " + sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+" -> "+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+" can be chained as:\n");
		//chainingmt.chainMT(sourcemodel, sourceMM, targetmodel, targetMM);
		//System.out.println("can be chained as:\n");
		//Path etlscript=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
		Path etlscript1 = null;
		Path etlscript2=null;
		Path etlscript3 = null;
		Path etlscript4 = null;
		Path intermodelpath;
		Path intermodelpath1;
		String intermodel1;
		
		//chainingmt.chainMT(sourcemodel, sourceMM, targetmodel, targetMM);
		List<String> modelsuse = new ArrayList<>();
		//int j=0;
		for(int i=0; i<contents.length; i++) {
			//j=i;
			//if(j!=i) {
			//if(((metamodelPath+"\\"+contents[i]).equals(sourceMM))) 
			//System.out.println(etlscript4);
			etlscript1=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
			/*
			for(int j=0;j<contents.length;j++) {
				
				//if((i+1)<contents.length)
				//{
				etlscript3=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[j].replaceFirst("[.][^.]+$", "")+".etl");
				//etlscript4=scriptRoot.resolve(contents[i+1].replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
				
			}*/
		/*
				etlscript2=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
			//if(((metamodelPath+"\\"+contents[i]).equals(sourceMM)) && ((metamodelPath+"\\"+contents[i]).equals(targetMM)))
			//}	
			
			if((etlscript1.toFile().exists() && etlscript2.toFile().exists()))
			//if(etlscript0.toFile().exists() && j!=i )
			{
				intermetamodel=metamodelPath+"\\"+contents[i];
				intermodelpath=genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".xmi");
				intermodel=intermodelpath.toAbsolutePath().toUri().toString();
				//intermetamodel1=metamodelPath+"\\"+contents[i+1];
				//intermodelpath1=genmodelsRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[i+1].replaceFirst("[.][^.]+$", "")+".xmi");
				//intermodel1=intermodelpath.toString();
				//System.out.println(intermodel);
				chainingmt.chainMT(sourcemodel, sourceMM, intermodel, intermetamodel);
				//chainingmt.executeETL();
				//sourcemodel=intermodel;
				//sourceMM=intermetamodel;
				//intermodel=targetmodel;
				//intermetamodel=targetMM;
				//chainingmt.chainMT(intermodel, intermetamodel, intermodel1, intermetamodel1);
				chainingmt.chainMT(intermodel, intermetamodel, targetmodel, targetMM);
				//System.out.println("\n");
				//chainingmt.executeETL();
				//execute();
				modelsuse.add(sourceMM.substring(59));
				modelsuse.add(intermetamodel.substring(59));
				modelsuse.add(targetMM.substring(59));
				System.out.println(modelsuse);
			}
			else
				continue;
			//}
			
			//}
		}*/
		
			//intermodel = chainingmt.gettargetmodel();
			//intermetamodel=chainingmt.gettargetmetamodel();
			
		
		//chainingmt.gettargetmodel();
		
		//for(int i=0; i<contents.length; i++) {
			//Path etlscript=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+contents[i+1].replaceFirst("[.][^.]+$", "")+".etl");
			
		//	String newtargetMM = metamodelsRoot.resolve(contents[i]).toString();
		//	chainingmt.chainMT(sourcemodel, sourceMM, newtargetMM);
		//	chainingmt.chainMT(targetmodel, newtargetMM, targetMM);
			
		//break;
		//}

	}
	
	
	public static void chaining() throws Exception
	{
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path metamodelsRoot = root.getParent().resolve("metamodels");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		File metamodelPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/metamodels");
		String contents[] = metamodelPath.list();
		
		File scriptPath = new File("src/org/eclipse/epsilon/examples/staticanalyser/script");
		String scriptcontents[] = scriptPath.list();
		
		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String targetMM = metamodelsRoot.resolve("TM.ecore").toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		String intermodel;
		String intermetamodel = null;
		String intermetamodel1 = null;
		
		Chaining_MT chainingmt = new Chaining_MT();
		
		System.out.println("Model Transformation " + sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+" -> "+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+" can be chained as:\n");
		
		Path etlscript1 = null;
		Path etlscript2=null;
		Path etlscript3 = null;
		Path etlscript4 = null;
		Path intermodelpath;
		Path intermodelpath1;
		String intermodel1;
		
		List<String> modelsuse = new ArrayList<>();
		
		for(int i=0; i<contents.length; i++) {
			
			etlscript1=scriptRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".etl");
			etlscript2=scriptRoot.resolve(contents[i].replaceFirst("[.][^.]+$", "")+"2"+targetMM.substring(59).replaceFirst("[.][^.]+$", "")+".etl");
			
			intermetamodel=metamodelPath+"\\"+contents[i];
			intermodelpath=genmodelsRoot.resolve(sourceMM.substring(59).replaceFirst("[.][^.]+$", "")+"2"+contents[i].replaceFirst("[.][^.]+$", "")+".xmi");
			intermodel=intermodelpath.toAbsolutePath().toUri().toString();
			
			//if((etlscript1.toFile().exists() && etlscript2.toFile().exists()))
			//{
				if(!(metamodelPath+"\\"+contents[i]).equals(targetMM))
				{
					targetMM=intermetamodel;
					targetmodel=intermodel;
				}
				
				if(!(metamodelPath+"\\"+contents[i]).equals(sourceMM))
				{
					sourceMM=intermetamodel;
					sourcemodel=intermodel;
				}
			//}
			
			
			chainingmt.chainMT(sourcemodel, sourceMM, targetMM, targetMM);
			/*
			if((etlscript1.toFile().exists() && etlscript2.toFile().exists()))
				{
					
					
					//chainingmt.chainMT(sourcemodel, sourceMM, intermodel, intermetamodel);
					
					//chainingmt.chainMT(intermodel, intermetamodel, targetmodel, targetMM);
					
					modelsuse.add(sourceMM.substring(59));
					modelsuse.add(intermetamodel.substring(59));
					modelsuse.add(targetMM.substring(59));
					System.out.println(modelsuse);
				}
				else
					continue;
			*/
			
			
		}
		
	}

}
