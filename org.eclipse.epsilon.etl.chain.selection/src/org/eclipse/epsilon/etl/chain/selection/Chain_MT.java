package org.eclipse.epsilon.etl.chain.selection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Chain_MT {
	public static void main(String[] args) throws Exception {
		
		Path modelsRoot = Paths.get("models");
		Path metamodelsRoot = Paths.get("metamodels");
		Path genmodelsRoot = Paths.get("models/generatedmodels");
		Chaining_MT chainingmt = new Chaining_MT();
		
		String sourcemodel=modelsRoot.resolve("Tree.xmi").toAbsolutePath().toUri().toString();
		String sourceMM = metamodelsRoot.resolve("Tree.ecore").toString();
		String targetMM = metamodelsRoot.resolve("DB.ecore").toString();
		String targetmodel= genmodelsRoot.resolve("Gen_Graph20.xmi").toAbsolutePath().toUri().toString();
		
		ArrayList<String> bestchain = chainingmt.identifyBestChain(sourcemodel, sourceMM, targetmodel, targetMM);
		System.out.println("Best chain: "+bestchain);
	}

}
