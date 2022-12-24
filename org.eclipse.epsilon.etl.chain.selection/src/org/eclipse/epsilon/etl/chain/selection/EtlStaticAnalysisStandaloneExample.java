/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.etl.chain.selection;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.m3.Metamodel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;

/**
 * This example demonstrates applying static analysis on ETL programs,
 * in a stand-alone manner 
 * 
 * @author Sina Madani
 * @author Dimitrios Kolovos
 */
public class EtlStaticAnalysisStandaloneExample {
	
	public static void main(String[] args) throws Exception {
		//Path root = Paths.get(EtlStaticAnalysisStandaloneExample.class.getResource("").toURI());
		//Path root = new File(System.getProperty("/")).toPath();
		Path root = Paths.get("src/org/eclipse/epsilon/examples/staticanalyser/models");
		Path modelsRoot = root.getParent().resolve("models");
		Path scriptRoot = root.getParent().resolve("script");
		Path genmodelsRoot = root.getParent().resolve("models/generatedmodels");
		
		//EtlModule module = new EtlModule();
		//Scanner sourceMM = new Scanner(new File("sourceMM"));
		//Scanner sourceModel = new Scanner(new File("sourceModel"));
		//Scanner targetMM = new Scanner(new File("targetMM"));
		
		String treeMM = modelsRoot.resolve("SimpleTrace.ecore").toAbsolutePath().toUri().toString();
		String simpleTraceMM = modelsRoot.resolve("TM.ecore").toAbsolutePath().toUri().toString();
		
		StringProperties sourceProperties = new StringProperties();
		sourceProperties.setProperty(EmfModel.PROPERTY_NAME, "SimpleTrace");
		sourceProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, treeMM);
		sourceProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
			genmodelsRoot.resolve("Gen_Tree2SimpleTrace.xmi").toAbsolutePath().toUri().toString()
		);
		sourceProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
		sourceProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "false");
		
		StringProperties targetProperties = new StringProperties();
		targetProperties.setProperty(EmfModel.PROPERTY_NAME, "TM");
		targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, simpleTraceMM);
		targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
			genmodelsRoot.resolve("Gen_SimpleTrace2TM.xmi").toAbsolutePath().toUri().toString()
		);
		targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "false");
		targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
		
		//module.parse(scriptRoot.resolve("Tree2SimpleTrace.etl"));
		
		//IModel m1 = module.getContext().getModelRepository().getModels().get(0);
		//IModel m2 = module.getContext().getModelRepository().getModels().get(1);
		//GetMM getmm = new GetMM();
		//IModel m1 = getmm.getsourceMM(scriptRoot.resolve("Tree2SimpleTrace.etl"));
		//System.out.println(getmm.getsourceMM());
		
		//Metamodel sourcemetamodel = m1.getMetamodel(sourceProperties, null);
		//Metamodel targetmetamodel = m2.getMetamodel(targetProperties, null);
		
		//System.out.println(sourcemetamodel.toString());
		//System.out.println(targetmetamodel.toString());
		
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
			.withScript(scriptRoot.resolve("SimpleTrace2TM.etl"))
			.withModel(new EmfModel(), sourceProperties)
			.withModel(new EmfModel(), targetProperties)
			.withParameter("parameterPassedFromJava", "Hello from pre")
			.withProfiling()
			.build();
		
		EtlPreExecuteConfiguration sm = new EtlPreExecuteConfiguration(runConfig);
		sm.run();
		//IModel m1 = EtlPreExecuteConfiguration.getsourceMM();
		//IModel m2 = EtlPreExecuteConfiguration.gettargetMM();
		//System.out.println(m1.getName());
		//System.out.println(m2.getName());
		
		runConfig.dispose();	
		
	}
}
