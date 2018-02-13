/**
 * 
 */
package mchecking.translator.mct.plasmat;

import org.sbml.jsbml.SBMLDocument;

//import org.sbml.libsbml.SBMLDocument;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.mct.prismt.PRISMT;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class PLASMAT extends PRISMT {

	public PLASMAT(ModelChecker targetMC) {
		super(targetMC);
	}

	/**
	 * @param stFilePath
	 */
	// public PLASMAT(String stFilePath) {
	// super(stFilePath);
	// }

	@Override
	public String translate(Inputs input, SBMLDocument sbmlDocument) throws Exception {
		String mcModel = super.translate(input, sbmlDocument);
		return mcModel;
	}

}
