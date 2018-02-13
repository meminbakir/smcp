package mchecking.translator;

import org.sbml.jsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.mct.MCT;
import mchecking.translator.qt.MCQuery;
import mchecking.translator.qt.PQuery;
import mchecking.translator.qt.PQueryManager;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class Translator {
	private static final Logger log = LoggerFactory.getLogger(Translator.class);

	public String translateMC(Inputs input, SBMLDocument sbml, ModelChecker targetMC) {
		String mcModelPath = null;
		try {
			mcModelPath = new MCT().translate(input, sbml, targetMC);
		} catch (Exception e) {
			log.error("SBML translation failed.\n" + e.getMessage() + "\n For more details open log file.");
			e.printStackTrace();
		}
		return mcModelPath;
	}

	/**
	 * If translated model needs query this method should be called, e.g., MRMC
	 * 
	 * @param sbml
	 * @param targetMC
	 * @param mcQuery
	 * @return
	 */
	public String translateMC(Inputs input, SBMLDocument sbml, ModelChecker targetMC, MCQuery mcQuery) {
		String mcModelPath = null;
		try {
			mcModelPath = new MCT().translate(input, sbml, targetMC, mcQuery);
		} catch (Exception e) {
			log.error("SBML translation failed.\n" + e.getMessage() + "\n For more details open log file.");
		}
		return mcModelPath;
	}

	public MCQuery translatePQuery(Inputs input, PQuery pQuery, ModelChecker targetMC) {
		return new PQueryManager().translate(input, pQuery, targetMC);
	}

}
