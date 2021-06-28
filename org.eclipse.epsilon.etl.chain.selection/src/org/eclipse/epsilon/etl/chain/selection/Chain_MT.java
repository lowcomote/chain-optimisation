package org.eclipse.epsilon.etl.chain.selection;

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
		Path modelsRoot = Paths.get("models");
		Path metamodelsRoot = Paths.get("metamodels");
		Path scriptRoot = Paths.get("scripts");
		Path genmodelsRoot = Paths.get("models/generatedmodels");
		
		File metamodelPath = new File("metamodels");
		String contents[] = metamodelPath.list();

		
		Chaining_MT chainingmt = new Chaining_MT();
		
		
		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toAbsolutePath().toString();
		String targetMM = metamodelsRoot.resolve("DB.ecore").toAbsolutePath().toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toString();
		
		List<ArrayList<String>> l = chainingmt.identifychain(sourcemodel, sourceMM, targetmodel, targetMM);
		
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

}
