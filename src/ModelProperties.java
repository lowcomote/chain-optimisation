
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;

public class ModelProperties {
	
	public StringProperties properties(String sourcename, String sourceMM, String sourcemodel, String readonload, String storeindisposal)
	{
		StringProperties sourceProperties = new StringProperties();
		sourceProperties.setProperty(EmfModel.PROPERTY_NAME, sourcename);
		sourceProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, sourceMM);
		sourceProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,sourcemodel);
		sourceProperties.setProperty(EmfModel.PROPERTY_READONLOAD, readonload);
		sourceProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, storeindisposal);
		
		return sourceProperties;
	}
	
	public StringProperties srcproperties(String sourcename, String sourceMM, String sourcemodel)
	{
		StringProperties sourceProperties = new StringProperties();
		sourceProperties.setProperty(EmfModel.PROPERTY_NAME, sourcename);
		sourceProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, sourceMM);
		sourceProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,sourcemodel);
		sourceProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
		sourceProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "false");
		
		return sourceProperties;
	}
	
	public StringProperties trgproperties(String targetname, String targetMM, String targetmodel)
	{
		StringProperties targetProperties = new StringProperties();
		targetProperties.setProperty(EmfModel.PROPERTY_NAME, targetname);
		targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, targetMM);
		targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,targetmodel);
		targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "false");
		targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
		
		return targetProperties;
	}
	
	public String getModel(StringProperties properties)
	{
		String model = properties.getProperty(EmfModel.PROPERTY_MODEL_URI);
		return model;
	}

}
