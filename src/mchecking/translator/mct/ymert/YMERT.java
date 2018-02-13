/**
 * 
 */
package mchecking.translator.mct.ymert;

import org.sbml.jsbml.SBMLDocument;

//import org.sbml.libsbml.SBMLDocument;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.mct.prismt.PRISMT;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class YMERT extends PRISMT {

	public YMERT(ModelChecker targetMC) {
		super(targetMC);
	}

	// /**
	// * @param stFilePath
	// */
	// public YMERT(String stFilePath) {
	// super(stFilePath);
	// }

	@Override
	public String translate(Inputs input, SBMLDocument sbmlDocument) throws Exception {
		String mcModel = super.translate(input, sbmlDocument);
		return mcModel;
	}

}
