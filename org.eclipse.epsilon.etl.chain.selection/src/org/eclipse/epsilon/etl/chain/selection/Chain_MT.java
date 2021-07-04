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
		//Path scriptRoot = Paths.get("scripts");
		Path genmodelsRoot = Paths.get("models/generatedmodels");
		
		//File metamodelPath = new File("metamodels");
		//String contents[] = metamodelPath.list();

		Chaining_MT chainingmt = new Chaining_MT();
		
		String sourcemodel=modelsRoot.resolve("Tree2.xmi").toAbsolutePath().toUri().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		
		ArrayList<String> bestchain = chainingmt.identifybestchain(sourcemodel, sourceMM, targetmodel, targetMM);
		System.out.println("Best chain: "+bestchain);
	}
		
	//}

}
