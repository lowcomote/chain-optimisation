package org.eclipse.epsilon.etl.chain.selection;



import java.nio.file.Path;

import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;
import org.eclipse.epsilon.examples.staticanalyser.eol.SubEmfModelFactory;

public class GetMM {
	
	public IModel getsourceMM()
	{
		EtlModule module = new EtlModule();
		IModel sourceMM = null;
		//module.parse(script);
		module.getContext().setModule(module);
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF"))
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			}
		sourceMM = ((EtlModule) module).getContext().getModelRepository().getModels().get(0);
		//return sourceMM;
	}
		return sourceMM;
	}
	
	public IModel gettargetMM()
	{
		EtlModule module = new EtlModule();
		IModel targetMM = null;
		//module.parse(script);
		module.getContext().setModule(module);
		if (module instanceof EtlModule) {
			EtlStaticAnalyser staticAnalyser = new EtlStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals("EMF"))
					staticAnalyser.getContext().setModelFactory(new SubEmfModelFactory());
			
			}
		targetMM = ((EtlModule) module).getContext().getModelRepository().getModels().get(1);
		
	}
		return targetMM;
	}

}
