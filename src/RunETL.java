

import java.nio.file.Path;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.launch.EtlRunConfiguration;
import org.eclipse.epsilon.etl.trace.TransformationTrace;

public class RunETL {
	
	public void execute(String sourcename, String targetname, String sourceMM, String targetMM, String sourcemodel, String targetmodel, Path script) throws Exception
	{
		StringProperties sourceProperties = new StringProperties();
		sourceProperties.setProperty(EmfModel.PROPERTY_NAME, sourcename);
		sourceProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, sourceMM);
		sourceProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,sourcemodel);
		sourceProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
		sourceProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "false");
		
		StringProperties targetProperties = new StringProperties();
		targetProperties.setProperty(EmfModel.PROPERTY_NAME, targetname);
		targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, targetMM);
		targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,targetmodel);
		targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "false");
		targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
		
	
		//if((m1.getName()==sourceProperties.getProperty(EmfModel.PROPERTY_NAME)) && (m2.getName()==targetProperties.getProperty(EmfModel.PROPERTY_NAME)))
		//{
		EtlRunConfiguration runConfig = EtlRunConfiguration.Builder()
				.withScript(script)
				.withModel(new EmfModel(), sourceProperties)
				.withModel(new EmfModel(), targetProperties)
				.withParameter("parameterPassedFromJava", "Hello from pre")
				.withProfiling()
				.build();
		//TransformationTrace transformationTrace = context.getTransformationTrace();
		//TransformModels t;
		
		EtlPreExecuteConfiguration sm = new EtlPreExecuteConfiguration(runConfig);
		sm.run();
		
		//IModel m1 = EtlPreExecuteConfiguration.getsourceMM();
		//IModel m2 = EtlPreExecuteConfiguration.gettargetMM();
		//System.out.println(m1.getName());
		//System.out.println(m2.getName());
		
		runConfig.dispose();
		//}
	}
}
