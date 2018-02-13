/**
 * 
 */
package mchecking.translator.mct;

import java.io.File;

import org.sbml.jsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mce.util.Utils;
import mchecking.ModelChecker;
import mchecking.enums.MCTypes;
import mchecking.translator.mct.mc2.MC2T;
import mchecking.translator.mct.mrmct.MRMCT;
import mchecking.translator.mct.plasmat.PLASMAT;
import mchecking.translator.mct.prismt.PRISMT;
import mchecking.translator.mct.uppaalt.UPPAALT;
import mchecking.translator.mct.ymert.YMERT;
import mchecking.translator.qt.MCQuery;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MCT {
	private static final Logger log = LoggerFactory.getLogger(MCT.class);

	public String translate(Inputs input, SBMLDocument sbml, ModelChecker targetMC) throws Exception {

		String mcModelPath = null;
		// Initialise target mc translator
		IMCT imct = initializeMCT(targetMC);
		// check if and mct initialised
		if (imct != null) {
			String mcModel = imct.translate(input, sbml);
			if (mcModel != null) {
				log.info("Model successfully translated to '" + targetMC.getType() + "' input language.");
				mcModelPath = targetMC.getOutPutDir() + File.separator + input.getFileName();
				// if mc has external tool then use its model extension
				String modelExtesion = ".";
				if (targetMC.isHasExternalTool()) {
					modelExtesion += targetMC.getExternalTool().getModelExtension();
				} else {
					modelExtesion += targetMC.getModelExtension();
				}
				mcModelPath += modelExtesion;
				Utils.write2File(mcModelPath, mcModel, false);

			} else {
				Utils.out("Translation failed for SBML model:" + input.getFileName());
			}
		}
		return mcModelPath;
	}

	/**
	 * @param targetMC
	 * @param mct
	 * @return
	 */
	private static IMCT initializeMCT(ModelChecker targetMC) {
		MCTypes targetMCType = targetMC.getType();
		IMCT imct = null;
		switch (targetMCType) {
		case PRISM:
			imct = new PRISMT(targetMC);
			break;
		case PLASMA:
			imct = new PLASMAT(targetMC);
			break;
		case YMER:
			imct = new YMERT(targetMC);
			break;
		case MC2:
			imct = new MC2T(targetMC);
			break;
		case UPPAAL:
			imct = new UPPAALT(targetMC);
			break;
		default:
			break;
		}
		return imct;
	}

	/***
	 * Model Checker translators which uses query during model translation. Such as, MRMC
	 * 
	 * @param sbml
	 * @param targetMC
	 * @param mcQuery
	 * @return
	 * @throws Exception
	 */
	public String translate(Inputs input, SBMLDocument sbml, ModelChecker targetMC, MCQuery mcQuery) throws Exception {
		String mcModelPath = null;
		// Load the properties of target model checker from mcProb.xml file
		// MCPropsLoader.loadMCProps(targetMC); should be already loaded, we can remove them.
		// Initialise target mc translator
		IMCT2 imct2 = initializeMCT2(targetMC);
		// check if and imct2 initialised
		if (imct2 != null) {
			String mcModel = imct2.translate(input, sbml, mcQuery);
			if (mcModel != null) {
				// log.info("Model successfully translated to '" + targetMC + "' input language.");
				// mcModelPath = MCPropsLoader.getOutPutDir() + File.separator + input.getFileName() + "."
				// + MCPropsLoader.getModelExtension();
				//
				// Utils.write2File(mcModelPath, mcModel, false);
				log.info("Model successfully translated to '" + targetMC + "' input language.");
				mcModelPath = targetMC.getOutPutDir() + File.separator + input.getFileName();
				// if mc has external tool then use its model extension
				String modelExtesion = ".";
				if (targetMC.isHasExternalTool()) {
					modelExtesion += targetMC.getExternalTool().getModelExtension();
				} else {
					modelExtesion += targetMC.getModelExtension();
				}
				mcModelPath += modelExtesion;
				Utils.write2File(mcModelPath, mcModel, false);

				// Translate properties to labels.
				String mcLabels = imct2.translateQuery4Model(mcQuery);
				if (!mcLabels.isEmpty()) {
					Utils.write2File(mcModelPath.replace(modelExtesion, ".props"), mcLabels, false);
				}

			} else {
				Utils.out("Translation failed for SBML model:" + input.getFileName());
			}
		}
		return mcModelPath;
	}

	private static IMCT2 initializeMCT2(ModelChecker targetMC) {
		IMCT2 imct2 = null;
		switch (targetMC.getType()) {
		case MRMC:
			imct2 = new MRMCT(targetMC);
			break;
		default:
			break;
		}
		return imct2;
	}
}
