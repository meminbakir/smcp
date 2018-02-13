/**
 * 
 */
package mchecking.translator.mct.mrmct;

import org.sbml.jsbml.SBMLDocument;

//import org.sbml.libsbml.SBMLDocument;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.mct.IMCT2;
import mchecking.translator.mct.prismt.PRISMT;
import mchecking.translator.qt.MCQuery;
import mchecking.translator.qt.PQuery2MRMC;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MRMCT extends PRISMT implements IMCT2 {

	/**
	 * @param targetMC
	 */
	public MRMCT(ModelChecker targetMC) {
		super(targetMC);
	}

	@Override
	public String translate(Inputs input, SBMLDocument sbml, MCQuery mcQuery) throws Exception {
		String mcModel = super.translate(input, sbml);
		return mcModel;
	}

	@Override
	public String translateQuery4Model(MCQuery mcQuery) {
		String labels = "";
		PQuery2MRMC mrmcQuery = (PQuery2MRMC) mcQuery;
		// key is expression and value is prop1 etc.
		for (String boolExpr : mrmcQuery.getBoolExprs().keySet()) {
			labels += "label \"" + mrmcQuery.getBoolExprs().get(boolExpr) + "\" = " + boolExpr + ";\n";
		}
		return labels;
	}

}
