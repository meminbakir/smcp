package mchecking.translator.mct;

import org.sbml.jsbml.SBMLDocument;

//import org.sbml.libsbml.SBMLDocument;

import mce.Inputs;
import mchecking.translator.qt.MCQuery;

/**
 * This interface is for Model Checker translators which uses query during model translation. Such as, MRMC.
 * 
 * @author Mehmet Emin BAKIR
 */
public interface IMCT2 {
	public String translate(Inputs input, SBMLDocument sbml, MCQuery mcQuery) throws Exception;

	public String translateQuery4Model(MCQuery mcQuery);
}
