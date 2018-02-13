/**
 * 
 */
package modify;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLReader;

/**
 * Checks if sbml model has errors, and is it valid for our needs, e.g. should be only one compartment and do not
 * include function
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class SBMLValidate {

	/**
	 * Validate SBML file
	 * 
	 * @param sbmlFilePath
	 * @return
	 * @throws Exception
	 */
	public static boolean hasMultipleCompartment(String sbmlFilePath) {
		boolean hasMultipleCompartment = false;
		String errors = "";
		try {
			SBMLReader reader = new SBMLReader();
			SBMLDocument document = reader.readSBML(sbmlFilePath);
			Model sbmlModel = document.getModel();
			if (sbmlModel == null) {
				errors += "There is no a model in the SBML document!\n";
			}

			long numCompartments = sbmlModel.getNumCompartments();
			if (numCompartments != 1) {
				errors += sbmlFilePath + " Multi compartments are not supported! Total comp num: " + numCompartments
						+ "\n";
				hasMultipleCompartment = true;
			}
			if (hasMultipleCompartment)
				System.out.println(errors);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasMultipleCompartment;
	}

	public static boolean isApproriate4Translation(String sbmlFilePath) {
		boolean isValid = true;
		String errors = "";
		try {
			SBMLReader reader = new SBMLReader();
			SBMLDocument document = reader.readSBML(sbmlFilePath);
			if (document.getNumErrors() > 0) {
				isValid = false;
				errors += "SBML document has following errors, Please correct them!\n";
				for (int i = 0; i < document.getNumErrors(); i++) {
					errors += document.getError(i).getMessage() + "\n";
				}
				isValid = false;
			}
			Model sbmlModel = document.getModel();
			if (sbmlModel == null) {
				errors += "There is no a model in the SBML document!\n";
				isValid = false;
			}

			// long numCompartments = sbmlModel.getNumCompartments();
			// if (numCompartments != 1) {
			// errors += "Multi compartments are not supported! Total comp num:
			// " + numCompartments + "\n";
			// isValid = false;
			// }

			long numSpecies = sbmlModel.getNumSpecies();
			if (numSpecies == 0) {
				errors += "No species are defined.\n";
				isValid = false;
			}
			// Check if model includes rules
			// long numRules = sbmlModel.getListOfRules().size();
			// if (numRules > 0) {
			// errors += "Rules are not supported\n";
			// isValid = false;
			// }

			// Check if it includes functions,

			// Check if kinetic formula is okay
			long numReactions = sbmlModel.getNumReactions();
			if (numReactions == 0) {
				errors += "No Reactions are defined.\n";
				isValid = false;
			}
			if (isValid) {
				System.out.println(sbmlFilePath + " is OK");
			} else {
				System.out.println("NOT OK");
				System.out.println("SBML file '" + sbmlFilePath + "' contains following problems.\n" + errors);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}

	public static boolean validateSBML(String sbmlFilePath) throws Exception {
		boolean isValid = true;
		String errors = "";
		try {
			SBMLReader reader = new SBMLReader();
			SBMLDocument document = reader.readSBML(sbmlFilePath);
			if (document.getNumErrors() > 0) {
				isValid = false;
				errors += "SBML document has following errors, Please correct them!\n";
				for (int i = 0; i < document.getNumErrors(); i++) {
					errors += document.getError(i).getMessage() + "\n";
				}
				isValid = false;
			}
			Model sbmlModel = document.getModel();
			if (sbmlModel == null) {
				errors += "There is no a model in the SBML document!\n";
				isValid = false;
			}

			long numCompartments = sbmlModel.getNumCompartments();
			if (numCompartments != 1) {
				errors += "Multi compartments are not supported! Total comp num: " + numCompartments + "\n";
				isValid = false;
			}

			long numSpecies = sbmlModel.getNumSpecies();
			if (numSpecies == 0) {
				errors += "No species are defined.\n";
				isValid = false;
			}
			// Check if model includes rules
			long numRules = sbmlModel.getListOfRules().size();
			if (numRules > 0) {
				errors += "Rules are not supported\n";
				isValid = false;
			}

			// Check if it includes functions,

			// Check if kinetic formula is okay
			long numReactions = sbmlModel.getNumReactions();
			if (numReactions == 0) {
				errors += "No Reactions are defined.\n";
				isValid = false;
			} else {
				// First get the species which involves in reaction
				// for (int i = 0; i < numReactions; i++) {
				// Reaction reaction = sbmlModel.getReaction(i);
				// KineticLawEval kineticLawEval = new KineticLawEval();
				//
				// if (reaction.isSetKineticLaw()) {
				// KineticLaw kineticLaw = reaction.getKineticLaw();
				// ListOfParameters listOfParameters =
				// kineticLaw.getListOfParameters();
				// for (int j = 0; j < listOfParameters.size(); j++) {
				// Parameter parameter = listOfParameters.get(j);
				// kineticLawEval.getVariables().put(parameter.getId(),
				// parameter.getValue());
				// }
				// // Some models declares parameter in compartment level
				// listOfParameters = sbmlModel.getListOfParameters();
				// for (int j = 0; j < listOfParameters.size(); j++) {
				// Parameter parameter = listOfParameters.get(j);
				// kineticLawEval.getVariables().put(parameter.getId(),
				// parameter.getValue());
				// }
				// if (kineticLaw.isSetMath()) {
				// String mathExpression =
				// libsbml.formulaToString(kineticLaw.getMath());
				// if (!mathExpression.isEmpty()) {
				// ANTLRInputStream input = new
				// ANTLRInputStream(mathExpression);
				// KineticLawEvalLexer lexer = new KineticLawEvalLexer(input);
				// CommonTokenStream tokens = new CommonTokenStream(lexer);
				// KineticLawEvalParser parser = new
				// KineticLawEvalParser(tokens);
				// parser.eval();
				// if (parser.getNumberOfSyntaxErrors() > 0) {
				// errors += "Kinetic law formula problem ' " + mathExpression
				// + " ' , note that functions are not supported!\n";
				// isValid = false;
				// break;
				// }
				// }
				// }
				// }
			}
			if (!isValid) {
				System.err.println("SBML file '" + sbmlFilePath + "' contains following problems.\n" + errors);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}

	public static void pressAnyKeyToContinue() {
		System.out.println("Press any key to continue...");
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}
}
