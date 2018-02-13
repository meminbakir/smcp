package mchecking.translator.mct;

import org.sbml.jsbml.SBMLDocument;

//import org.sbml.libsbml.SBMLDocument;

import mce.Inputs;

/**
 * This interface is for Model Checker translators which do not require Query objects for model translation.
 * 
 * @author Mehmet Emin BAKIR
 */
public interface IMCT {
	public String translate(Inputs input, SBMLDocument sbml) throws Exception;
	// public String write2File(String mcModel);
}
