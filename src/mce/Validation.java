/**
 * 
 */
package mce;

import java.util.HashMap;
import java.util.Map;

import mchecking.translator.mct.Scale;
import mtopology.KineticLawEval;
import mtopology.KineticLawEvalLexer;
import mtopology.KineticLawEvalParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.sbml.jsbml.JSBML;
//import org.sbml.libsbml.KineticLaw;
//import org.sbml.libsbml.ListOfParameters;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.Parameter;
//import org.sbml.libsbml.Reaction;
//import org.sbml.libsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLReader;
//import org.sbml.libsbml.libsbml;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import output.Output;

/**
 * Checks if sbml model has errors, and is it valid for our needs, e.g. should
 * be only one compartment and do not include function
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class Validation {
	private static final Logger log = LoggerFactory.getLogger(Validation.class);

	/**
	 * The ValidationError types are for test purpose
	 * 
	 * @author Mehmet Emin BAKIR
	 *
	 */
	enum ValidationError {
		HasMultiCompartment, HasRules, HasFunctions, HasDecimalAmount, // If species amount is not integer
		KineticFormulaProblem, Other
	}

	Output output = null;
	Inputs input;

	private Map<ValidationError, Integer> validationErrors = null;

	Validation() {
		setValidationError(new HashMap<>());
	}

	public Validation(Inputs input) {
		this.input = input;
	}

	/**
	 * Validate SBML file
	 * 
	 * @param sbmlFilePath
	 * @return
	 * @throws Exception
	 */
	public boolean validateSBML(String sbmlFilePath) throws Exception {
		boolean isValid = true;
		String errors = "";
		try {
			MySBMLReader reader = new MySBMLReader();
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
				addValidationError(ValidationError.Other);
				isValid = false;
			}

			long numCompartments = sbmlModel.getNumCompartments();
			if (numCompartments != 1) {
				errors += "Multi compartments are not supported! Total comp num: " + numCompartments + "\n";
				addValidationError(ValidationError.HasMultiCompartment);
				isValid = false;
			}

			long numSpecies = sbmlModel.getNumSpecies();
			if (numSpecies == 0) {
				errors += "No species are defined.\n";
				addValidationError(ValidationError.Other);
				isValid = false;
			}
			// Check if model includes rules
			long numRules = sbmlModel.getListOfRules().size();
			if (numRules > 0) {
				errors += "Model checkers does not support functions.\n" + "Therefore, SBML Rules are not supported \n";
				addValidationError(ValidationError.HasRules);
				isValid = false;
			}

			// Check if it includes functions,
			long numFunctions = sbmlModel.getListOfFunctionDefinitions().size();
			if (numFunctions > 0) {
				String message = "Model checkers does not support functions.\n"
						+ "Therefore, SMBL FunctionDefinitions are not supported \n";
				errors += message;
				log.warn(message);
				addValidationError(ValidationError.HasFunctions);
				isValid = false;
			}

			// Check if initial amount is decimal, if so, then rescale to integer
			// return false, if it is decimal but not possible to rescale it
			Scale scale = new Scale(input);
			if (!scale.isSpeciesScalable(sbmlModel)) {
				String message = scale.getScalableMessage();
				errors += message;
				addValidationError(ValidationError.HasDecimalAmount);
				isValid = false;
			}

			// Check if kinetic formula is okay
			long numReactions = sbmlModel.getNumReactions();
			if (numReactions == 0) {
				errors += "No Reactions are defined.\n";
				addValidationError(ValidationError.Other);
				isValid = false;
			} else {
				// First get the species which involves in reaction
				for (int i = 0; i < numReactions; i++) {
					Reaction reaction = sbmlModel.getReaction(i);
					KineticLawEval kineticLawEval = new KineticLawEval();

					if (reaction.isSetKineticLaw()) {
						KineticLaw kineticLaw = reaction.getKineticLaw();
						ListOf<LocalParameter> localParams = kineticLaw.getListOfLocalParameters();
						for (int j = 0; j < localParams.size(); j++) {
							LocalParameter parameter = localParams.get(j);
							kineticLawEval.getVariables().put(parameter.getId(), parameter.getValue());
						}
						// Some models declares parameter in compartment level
						ListOf<Parameter> globalParams = sbmlModel.getListOfParameters();
						for (int j = 0; j < globalParams.size(); j++) {
							Parameter parameter = globalParams.get(j);
							kineticLawEval.getVariables().put(parameter.getId(), parameter.getValue());
						}
						if (kineticLaw.isSetMath()) {
							String mathExpression = JSBML.formulaToString(kineticLaw.getMath());
							if (!mathExpression.isEmpty()) {
								ANTLRInputStream input = new ANTLRInputStream(mathExpression);
								KineticLawEvalLexer lexer = new KineticLawEvalLexer(input);
								CommonTokenStream tokens = new CommonTokenStream(lexer);
								KineticLawEvalParser parser = new KineticLawEvalParser(tokens);
								parser.eval();
								if (parser.getNumberOfSyntaxErrors() > 0) {
									String message = "Kinetic law formula has problem ' " + mathExpression + " '\n";
									message += "Please note that Model checkers does not allow custom function definitions.\n"
											+ "Therefore, It is not possible to translate SMBL FunctionDefinitions "
											+ "and Rules to Model Checkers' specifications. \n";

									errors += message;
									log.error(message);
									isValid = false;
									addValidationError(ValidationError.KineticFormulaProblem);

									break;
								}
							}
						}
					}
				}
			}
			if (!isValid) {
				output = new Output();
				output.isError = true;
				output.error = "SBML file '" + sbmlFilePath + "' contains following problems.\n" + errors;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}

		return isValid;
	}

	/**
	 * @return the validationErrors
	 */
	public Map<ValidationError, Integer> getValidationError() {
		if (validationErrors == null) {
			return new HashMap<>();
		}
		return validationErrors;
	}

	/**
	 * @param validationErrors
	 *            the validationErrors to set
	 */
	public void setValidationError(Map<ValidationError, Integer> validationError) {
		this.validationErrors = validationError;
	}

	/**
	 * Add new error to validation error map. Increment the error type by one.
	 * 
	 * @param validation
	 *            error type
	 */
	private void addValidationError(ValidationError errorKey) {
		// if the error type already exists, increment its count
		if (getValidationError().containsKey(errorKey)) {
			int count = getValidationError().get(errorKey);
			count += 1;// add new error
			getValidationError().put(errorKey, count);
		} else {
			// The error is new
			getValidationError().put(errorKey, 1);
		}
	}

	/**
	 * Check if any initial amount is in decimal, if so find the max coefficient so
	 * that all species initial amount can be rescaled to integer
	 * 
	 * @param sbmlModel
	 * @return
	 */
	// private Double hasAnyInitialAmountInDecimal(Model sbmlModel) {
	// boolean hasAnyInitialAmountInDecimal = false;
	// Species species = null;
	// for (int n = 0; n < sbmlModel.getNumSpecies(); n++) {
	// species = sbmlModel.getSpecies(n);
	// String speciesID = species.getId();
	// double initialValue = Double.isNaN(species.getInitialConcentration()) ?
	// species.getInitialAmount()
	// : species.getInitialConcentration();
	// if (!Double.isNaN(initialValue)) {
	// if (!isInteger(initialValue)) {// if it is decimal
	// hasAnyInitialAmountInDecimal = true;
	// }
	// } else {
	// log.error("The initial amount/concentration ofspecies {} is not defined. "
	// + "Model checker requires all species to have initial value.", speciesID);
	// }
	// }
	//
	// return coeff;
	// }

}
