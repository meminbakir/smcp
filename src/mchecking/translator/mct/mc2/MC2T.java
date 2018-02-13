/**
 * 
 */
package mchecking.translator.mct.mc2;

import org.sbml.jsbml.SBMLDocument;

//import org.sbml.libsbml.SBMLDocument;

import mce.Inputs;
import mce.util.Utils;
import mchecking.ModelChecker;
import mchecking.enums.ExtTTypes;
import mchecking.translator.mct.prismt.PRISMT;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MC2T extends PRISMT {

	/**
	 * @param targetMC
	 */
	public MC2T(ModelChecker targetMC) {
		super(targetMC);
	}

	@Override
	public String translate(Inputs input, SBMLDocument sbmlDocument) throws Exception {
		String mcModel = null;
		// if external tool is gillespie2
		if (this.targetMC.getExternalTool().getExtTType().equals(ExtTTypes.GILLESPIE2)) {
			// if any of SBML simulator will be used
			mcModel = this.getContentOfSBML(input, sbmlDocument);
		} else if (this.targetMC.getExternalTool().getExtTType().equals(ExtTTypes.PRISM)) {
			// if PRISM simulator will be used.
			mcModel = this.getPrismModel(input, sbmlDocument);
		}
		return mcModel;
	}

	/**
	 * Simply reads the whole content of SBML Documents, including comments and returns as String.
	 * 
	 * @param input
	 * @param sbmlDocument
	 * @return
	 */
	private String getContentOfSBML(Inputs input, SBMLDocument sbmlDocument) {
		return Utils.readFileContent(input.getSbmlFilePath());
	}

	/**
	 * Enable it only If PRISM will be used as the simulator for MC2 model checker
	 * 
	 * @param input
	 * @param sbmlDocument
	 * @return
	 * @throws Exception
	 */
	private String getPrismModel(Inputs input, SBMLDocument sbmlDocument) throws Exception {
		return super.translate(input, sbmlDocument);
	}

}
