/**
 * 
 */
package mchecking;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mchecking.enums.ExtTTypes;
import mchecking.enums.MCTypes;
import mchecking.enums.Pattern;
import mchecking.toolprops.ExternalTool;
import mchecking.translator.qt.PQuery;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class ModelChecker {
	private static Logger log = LoggerFactory.getLogger(ModelChecker.class);
	private String modelExtension = null;

	private MCTypes type = null;
	private String name = null;
	private String appPath = null;
	private String outPutDir = null;
	private boolean hasExternalTool = false;
	private ExternalTool externalTool = null;
	private boolean usesPrism = true;

	/**
	 * @param type2
	 */
	public ModelChecker(MCTypes type) {
		this.type = type;
	}

	/**
	 * Returns the list of supported patterns by the @param type model
	 * 
	 * @param type
	 *            the model checker type
	 * @return list of patterns supported by this model checker
	 */
	public static List<Pattern> loadSupportedPatterns(MCTypes type) {
		List<Pattern> supportedPatterns = null;
		supportedPatterns = new ArrayList<>();
		// Add all patterns first, then remove the unsupported ones.
		supportedPatterns.addAll(Arrays.asList(Pattern.values()));
		// remove unsupportedPatterns
		Pattern[] unSupportedPatterns = null;
		if (type == MCTypes.PRISM) {
			unSupportedPatterns = new Pattern[] { Pattern.INFINITELY_OFTEN, Pattern.STEADY_STATE, Pattern.FOLLOWS };
		} else if (type == MCTypes.PLASMA) {
			// PLASMA ignores P>=number, and P=? operators. But supports all patterns.
		} else if (type == MCTypes.YMER) {
			unSupportedPatterns = new Pattern[] { Pattern.NEXT, Pattern.INFINITELY_OFTEN, Pattern.STEADY_STATE,
					Pattern.FOLLOWS };
		} else if (type == MCTypes.MRMC) {
			unSupportedPatterns = new Pattern[] { Pattern.WHAT_PROBABILITY, Pattern.INFINITELY_OFTEN };
		} else if (type == MCTypes.MC2) {
			// MC2 supports all current patterns.
		}
		if (unSupportedPatterns != null)
			supportedPatterns.removeAll(Arrays.asList(unSupportedPatterns));
		return supportedPatterns;
	}

	public MCTypes getType() {
		return type;
	}

	public void setType(MCTypes type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		if (name.isEmpty()) {
			name = "NoNameMC";
			log.warn(
					"Model checker name has not been specified. Check mcProp.xml file. 'NoName4MC' has been set as defaul model checker name.");
		}
		this.name = name;
	}

	/**
	 * @return the appPath
	 */
	public String getAppPath() {
		return appPath;
	}

	/**
	 * @param appPath
	 *            the appPath to set
	 */
	public void setAppPath(String appPath) {
		this.appPath = appPath.trim();
	}

	/**
	 * @return the outPutDir
	 */
	public String getOutPutDir() {
		return outPutDir;
	}

	/**
	 * @param outPutDir
	 *            the outPutDir to set
	 */
	public void setOutPutDir(String outPutDir) {
		if (outPutDir.isEmpty() || outPutDir == null) {
			outPutDir = System.getProperty("user.dir") + File.separator + "models" + File.separator + "translated"
					+ File.separator + getName();
		}
		this.outPutDir = outPutDir;
	}

	/**
	 * @param modelExtension
	 *            the modelExtension to set
	 */
	public void setModelExtension(String modelExtension) {
		if (modelExtension.isEmpty()) {
			modelExtension = "";
			log.warn(
					"MC model extension (eg. sm for prism), has not been specified. Check mcProp.xml file. No extension has been set as default model checker name.");
		}
		this.modelExtension = modelExtension;
	}

	public String getModelExtension() {

		return modelExtension;
	}

	/**
	 * @return the hasExternalTool
	 */
	public boolean isHasExternalTool() {
		return hasExternalTool;
	}

	/**
	 * @param hasExternalTool
	 *            the hasExternalTool to set
	 */
	public void setHasExternalTool(boolean hasExternalTool) {
		this.hasExternalTool = hasExternalTool;
	}

	/**
	 * @return the externalTool
	 */
	public ExternalTool getExternalTool() {
		return externalTool;
	}

	/**
	 * @param externalTool
	 *            the externalTool to set
	 */
	public void setExternalTool(ExternalTool externalTool) {
		this.externalTool = externalTool;
	}

	/**
	 * Check either MChecker it self or its External tool uses PRISM, If it is so, we will check prism keywords. Currently
	 * only MC2 has choice to not use PRISM.
	 * 
	 * @return the usesPrism
	 */
	public boolean isUsesPrism() {
		usesPrism = true;
		if (type == MCTypes.MC2) {
			if (externalTool != null)
			if (externalTool.getExtTType() != ExtTTypes.PRISM) {
				usesPrism = false;
			}
		}
		return usesPrism;
	}

	/**
	 * @param usesPrism
	 *            the usesPrism to set
	 */
	public void setUsesPrism(boolean usesPrism) {
		this.usesPrism = usesPrism;
	}

	/**
	 * Returns the List of Model Checkers which supports the @pQuery
	 * 
	 * @param pQuery
	 * @return
	 */
	public static List<MCTypes> getSupportedModelCheckers(PQuery pQuery) {
		List<MCTypes> supportedMCs = new ArrayList<MCTypes>();
		for (MCTypes mcType : MCTypes.values()) {
			List<Pattern> supportedPatterns = ModelChecker.loadSupportedPatterns(mcType);
			if (supportedPatterns.containsAll(pQuery.getPatterns())) {
				supportedMCs.add(mcType);
			}
		}
		return supportedMCs;
	}
}
