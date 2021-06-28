package org.eclipse.epsilon.etl.chain.optimisation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.parse.EtlUnparser;
import org.eclipse.epsilon.etl.staticanalyser.EtlStaticAnalyser;

public class EtlRewritingHandler {

	public void invokeRewriters(ArrayList<EtlModule> modules, ArrayList<EtlStaticAnalyser> staticAnlaysers) {
		int index = 0;
		List<EolType> sourceRules = new ArrayList<EolType>();
		List<EolType> targetRules = new ArrayList<EolType>();
	for(EtlModule module : modules) {
		System.out.println("------------------");
		System.out.println(module.getSourceFile().getName());
		System.out.println("------------------");
		EtlStaticAnalyser staticAnalyser = staticAnlaysers.get(index);
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
		index++;
		System.err.println(new EtlUnparser().unparse(module));
	}
	sourceRules.toString();
	targetRules.toString();
	}

}
