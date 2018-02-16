/**
 * 
 */
package mchecking.translator.mct.prismt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.TranslatorUtil;
import mchecking.translator.mct.IMCT;
import mchecking.translator.mct.Scale;
import mchecking.translator.mct.prismt.beans.Const;
import mchecking.translator.mct.prismt.beans.Molecule;
import mchecking.translator.mct.prismt.beans.Next;
import mchecking.translator.mct.prismt.beans.Next.Comparer;
import mchecking.translator.mct.prismt.beans.Next.Operator;
import mchecking.translator.mct.prismt.beans.Next.Type;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
//import org.sbml.libsbml.Compartment;
//import org.sbml.libsbml.KineticLaw;
//import org.sbml.libsbml.ListOfParameters;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.Parameter;
//import org.sbml.libsbml.Reaction;
//import org.sbml.libsbml.SBMLDocument;
//import org.sbml.libsbml.Species;
//import org.sbml.libsbml.SpeciesReference;
//import org.sbml.libsbml.libsbml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class PRISMT implements IMCT {
	/**
	 * 
	 */
	private static final DecimalFormatSymbols DECIMAL_FORMAT = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
	private static final Logger log = LoggerFactory.getLogger(PRISMT.class);
	private Inputs input = null;
	protected Scale scale;
	protected ModelChecker targetMC = null;
	String stFilePath = "./src/mchecking/translator/mct/prismt/prismST.stg";
	STGroup group = null;
	List<String> addedVars = new ArrayList<String>();

	public PRISMT(ModelChecker targetMC) {
		this.targetMC = targetMC;
		group = new STGroupFile(stFilePath);
	}

	//
	// public PRISMT(String stFilePath, ModelChecker targetMC) {
	// this.targetMC = targetMC;
	// this.stFilePath = stFilePath;
	// this.group = new STGroupFile(stFilePath);
	// }

	@Override
	public String translate(Inputs input, SBMLDocument sbmlDocument) throws Exception {
		this.input = input;
		String mcModel = null;
		ST modelST = group.getInstanceOf("model");
		setDetails(sbmlDocument, modelST);
		Model sbmlModel = sbmlDocument.getModel();
		scale = new Scale(input, targetMC);
		scaleSBMLModel(sbmlModel);

		setModel(sbmlModel, modelST);
		setConstants(sbmlModel, modelST);
		setMolecules(sbmlModel, modelST);
		setRules(sbmlModel, modelST);
		mcModel = modelST.render();
		if (mcModel != null)
			return mcModel;
		else
			throw new Exception("SBML model translation has failed.");

	}

	/**
	 * 1. If needed, scale constants to remain within integer bounds.<br/>
	 * 2. If needed, scale species initial amount from decimal to integer values.
	 * 
	 * @param sbmlModel
	 */
	private void scaleSBMLModel(Model sbmlModel) {
		// If constants exceeds integer limit, then they are rescaled.
		scale.scaleConstatsRemainWithinIntegerBounds(sbmlModel);
		scale.scaleSpeciesInitialValue(sbmlModel);
	}

	/**
	 * Converts reactions to PRISM rules
	 * 
	 * // [] (_E-1 >= 0) & (_S-1 >= 0) & (100 >= ES+1) -> (_E' = _E - 1) & (_S' // = _S - 1) & (ES' = ES + 1);
	 * 
	 * @param sbmlModel
	 * @param modelST
	 */
	private void setRules(Model sbmlModel, ST modelST) {
		ST ruleST = group.getInstanceOf("rule");
		long numReactions = sbmlModel.getNumReactions();
		Reaction reaction = null;
		if (numReactions == 0) {
			log.info("No Reactions are defined in " + input.getSbmlFilePath());
		} else {

			for (int n = 0; n < numReactions; n++) {
				reaction = sbmlModel.getReaction(n);
				// if reaction is bidirectional then separate each direction
				String direction = "forward";
				// if reaction(rule) is applicable, namely if there exist at least one species is not constant (not
				// constant=true, and/or boundaryCondition=true)
				if (ruleIsApplicable(reaction)) {
					composeRateAndRule(ruleST, reaction, direction);
					if (reaction.getReversible()) {
						direction = "backward";
						composeRateAndRule(ruleST, reaction, direction);
					}
				}
			}
		}
		modelST.add("rules", ruleST);
	}

	/**
	 * 1-) If all species are constant, namely if all species both in reactant or in product are constant where
	 * (constant=true, OR boundaryCondition=true), then nothing will be updated, so it is not applicable and we do not need
	 * to generate a PRISM rule for it.
	 * 
	 * @param reaction
	 * @param direction
	 * @return
	 */
	private boolean ruleIsApplicable(Reaction reaction) {
		boolean ruleIsApplicable = false;
		// if any product is not constant, then rule is applicable.
		for (int i = 0; i < reaction.getNumReactants(); i++) {
			SpeciesReference sr = reaction.getReactant(i);
			if (!this.isSpeciesConstant(sr)) {
				ruleIsApplicable = true;
				break;
			}
		}
		// if all reactants are constant species, then check products whether all are constant.
		if (!ruleIsApplicable) {
			for (int i = 0; i < reaction.getNumProducts(); i++) {
				SpeciesReference sr = reaction.getProduct(i);
				if (!this.isSpeciesConstant(sr)) {
					ruleIsApplicable = true;
					break;
				}
			}
		}
		return ruleIsApplicable;
	}

	/**
	 * 
	 * @param ruleST
	 * @param reaction
	 * @param direction
	 */
	private void composeRateAndRule(ST ruleST, Reaction reaction, String direction) {
		ST updateItemST = group.getInstanceOf("updateItem");
		ST guardOfNextST = group.getInstanceOf("guardOfNext");
		ST nextST = group.getInstanceOf("next");
		composeRule(guardOfNextST, nextST, reaction, direction);
		String rate = getReactionRate(reaction, direction);
		updateItemST.add("rate", rate);
		updateItemST.add("next", nextST);
		ruleST.add("guard", guardOfNextST);
		ruleST.add("updateItem", updateItemST);
	}

	/**
	 * @param guardOfNextST
	 * @param nextST
	 * @param reaction
	 * @param direction
	 */
	private void composeRule(ST guardOfNextST, ST nextST, Reaction reaction, String direction) {
		List<String> addedSpecies = new ArrayList<String>();
		for (int i = 0; i < reaction.getNumReactants(); i++) {
			SpeciesReference sr = reaction.getReactant(i);
			String species = sr.getSpecies();
			if (!addedSpecies.contains(species)) {
				// if species is constant, namely constant=true OR boundaryCondition=true
				if (this.isSpeciesConstant(sr)) {
					addConstantSpecies2Rule(guardOfNextST, nextST, sr, reaction, direction);
				} else {
					// if it is not constant
					addSpecies2Rule(guardOfNextST, nextST, species, reaction, direction);
				}
				addedSpecies.add(species);
			}
		}
		for (int i = 0; i < reaction.getNumProducts(); i++) {
			SpeciesReference sr = reaction.getProduct(i);
			String species = sr.getSpecies();
			if (!addedSpecies.contains(species)) {
				if (this.isSpeciesConstant(sr)) {
					addConstantSpecies2Rule(guardOfNextST, nextST, sr, reaction, direction);
				} else {
					// if it is not constant
					addSpecies2Rule(guardOfNextST, nextST, species, reaction, direction);
				}
				addedSpecies.add(species);
			}
		}
	}

	/**
	 * @param guardOfNextST
	 * @param nextST
	 * @param species
	 *            a constant species
	 * @param reaction
	 * @param direction
	 */
	private void addConstantSpecies2Rule(ST guardOfNextST, ST nextST, SpeciesReference sr, Reaction reaction,
			String direction) {
		boolean isReactant = false;
		int reactantStoichiometry = 0;

		for (int i = 0; i < reaction.getNumReactants(); i++) {
			SpeciesReference sr2 = reaction.getReactant(i);
			if (sr.getSpecies().equals(sr2.getSpecies())) {
				if (direction.equals("forward")) {
					reactantStoichiometry -= sr2.isSetStoichiometry() ? sr2.getStoichiometry() : 1;
					isReactant = true;
				}
			}
		}
		for (int i = 0; i < reaction.getNumProducts(); i++) {
			SpeciesReference sr2 = reaction.getProduct(i);
			if (sr.getSpecies().equals(sr2.getSpecies())) {
				if (direction.equals("backward")) {
					reactantStoichiometry -= sr2.isSetStoichiometry() ? sr2.getStoichiometry() : 1;
					isReactant = true;
				}
			}
		}
		// Only If a constant species in reactant we will use it in rule , for checking it satisfies the minimum amount
		// to
		// get reaction(boundary guard)
		// Otherwise if it is a product, since we can't update it, we will not use it at all
		if (isReactant) {
			Next guard = new Next();
			guard.setName(sr.getSpecies());
			guard.setBound("0");
			guard.setType(Type.REACTANT);
			guard.setOperator(Operator.SUM);
			guard.setComparer(Comparer.GEQ);
			guard.setStoichiometry(String.valueOf(reactantStoichiometry));
			guardOfNextST.add("reactant", guard);
		}
	}

	/**
	 * 
	 * 
	 */
	private void addSpecies2Rule(ST guardOfNextST, ST nextST, String speciesStr, Reaction reaction, String direction) {
		Next nextBean = new Next();
		nextBean.setName(speciesStr);
		boolean isReactant = false, isProduct = false, isBoth = false;
		int reactantStoichiometry = 0, productStoichiometry = 0, bothSideStoichiometry = 0;
		// If species exist in both sides, then its Stoichiometry will be product_value - reactant_value
		for (int i = 0; i < reaction.getNumReactants(); i++) {
			SpeciesReference sr = reaction.getReactant(i);
			if (speciesStr.equals(sr.getSpecies())) {
				isReactant = true;
				if (direction.equals("forward")) {
					reactantStoichiometry -= sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;

				} else if (direction.equals("backward")) {
					productStoichiometry += sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;
				}
			}
		}
		for (int i = 0; i < reaction.getNumProducts(); i++) {
			SpeciesReference sr = reaction.getProduct(i);
			if (speciesStr.equals(sr.getSpecies())) {
				isProduct = true;
				if (direction.equals("forward")) {
					productStoichiometry += sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;

				} else if (direction.equals("backward")) {
					reactantStoichiometry -= sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;
				}
			}
		}
		// if species occurs on both side, we need to add one extra guard for lowerBound and one guard for
		// bothSideStoichiometry
		if (isReactant && isProduct) {
			isBoth = true;
			Next guard1 = new Next();
			guard1.setName(speciesStr);
			guard1.setBound(input.getLowwerBound());
			guard1.setType(Type.REACTANT);
			guard1.setOperator(Operator.SUM);
			guard1.setComparer(Comparer.GEQ);
			guard1.setStoichiometry(String.valueOf(reactantStoichiometry));
			guardOfNextST.add("reactant", guard1);
		}

		bothSideStoichiometry = reactantStoichiometry + productStoichiometry;
		if (bothSideStoichiometry < 0) {
			nextBean.setBound(input.getLowwerBound());
			nextBean.setType(Type.REACTANT);
			nextBean.setOperator(Operator.SUM);
			nextBean.setComparer(Comparer.GEQ);
			nextBean.setStoichiometry(String.valueOf(bothSideStoichiometry));
		} else if (bothSideStoichiometry == 0 && isBoth) {
			nextBean.setBound(nextBean.getName());
			nextBean.setType(Type.BOTHSIDES);
			nextBean.setOperator(Operator.NA);
			nextBean.setComparer(Comparer.EQ);
			nextBean.setStoichiometry("");
		} else if (bothSideStoichiometry > 0) {
			nextBean.setBound(input.getUpperBound());
			nextBean.setType(Type.PRODUCT);
			nextBean.setOperator(Operator.SUM);
			nextBean.setComparer(Comparer.LEQ);
			nextBean.setStoichiometry(String.valueOf(bothSideStoichiometry));
		}

		guardOfNextST.add("reactant", nextBean);
		nextST.add("next", nextBean);
	}

	private String getReactionRate(Reaction reaction, String direction) {
		String rate = null;
		if (reaction.isSetKineticLaw()) {
			KineticLaw kineticLaw = reaction.getKineticLaw();
			if (kineticLaw.isSetMath()) {
				String[] result = JSBML.formulaToString(kineticLaw.getMath()).split("(?<=[-(+*/])|(?=[-)+*/])");
				rate = "";
				for (int i = 0; i < result.length; i++) {
					rate += TranslatorUtil.checkKeyword(result[i].trim());
				}
			}

			// TODO 05.02.2018 (rest of this function added) either local params has value or they inherit globally,
			// for local ones we need to replace the variable name with the value
			ListOf<LocalParameter> localParams = kineticLaw.getListOfLocalParameters();
			for (int i = 0; i < localParams.size(); i++) {
				LocalParameter localParam = localParams.get(i);

				String paramID = TranslatorUtil.checkKeyword(localParam.getId());

				if (rate.contains(paramID)) {
					String paramValue = convertFromScientificNotation(localParam.getValue());
					rate = rate.replaceAll(paramID, paramValue);
				} else {
					log.error("It seems kinetic formula:{} does not contain the param {}", rate, paramID);
				}

				// if (reaction.getReversible()) {
				// if (i == 0) {
				// System.out.println(
				// "\t Forward reaction: parameter id " + localParam.getId() + " value " + localParam.getValue());
				// } else if (i == 1) {
				// System.out.println("\t Backward reaction: parameter id " + localParam.getId() + " value "
				// + localParam.getValue());
				// }
				// } else {
				// System.out.println("The reaction is one-direction(non-reversible)");
				// System.out.println("\t Parameter id " + localParam.getId() + " value " + localParam.getValue());
				// }
			}
		}
		return rate;
	}

	private void setMolecules(Model sbmlModel, ST modelST) throws Exception {
		ST moleculesST = group.getInstanceOf("molecules");
		long numSpecies = sbmlModel.getNumSpecies();
		if (numSpecies == 0) {
			log.warn("No species are defined");
		} else {
			Species species = null;
			Molecule moleculeBean = null;
			for (int n = 0; n < numSpecies; n++) {
				species = sbmlModel.getSpecies(n);
				if (!addedVars.contains(species.getId())) {
					moleculeBean = getValidMolecule(species);

					moleculesST.add("molecules", moleculeBean);
					addedVars.add(species.getId());
				} else {
					// if model is constant, we had added to the addedVars list, otherwise name has been duplicated in
					// SBML model which is an error
					if (isSpeciesConstant(species))
						continue;
					else {
						String error = "The model has same name " + species.getId()
								+ " for a species and a constantant. Please provide different names.\n";
						throw new Exception(error);
					}
				}
			}
		}
		modelST.add("molecules", moleculesST);
	}

	private Molecule getValidMolecule(Species species) {
		Molecule moleculeBean = new Molecule();

		moleculeBean.setName(TranslatorUtil.checkKeyword(species.getId()));
		moleculeBean.setLowerBound(input.getLowwerBound());
		moleculeBean.setUpperBound(input.getUpperBound());
		// TODO 04.Feb.2018 change start, initially it was only getting only the getInitialAmount, but now
		// which of getInitialAmount or getInitialConcentration is set will be assigned.
		double init = getSpeciesInitialValue(species);
		//
		init = Double.parseDouble(convertFromScientificNotation(init));

		if (init < Double.parseDouble(input.getLowwerBound())) {
			init = Double.parseDouble(input.getLowwerBound());
			log.warn(moleculeBean.getName() + " has lower initial value '" + species.getInitialAmount()
					+ "' than the default one, so init value has been automatically set to the lowest bound'"
					+ input.getLowwerBound() + "'");
		}
		if (init > Double.parseDouble(input.getUpperBound())) {
			init = Double.parseDouble(input.getUpperBound());
			log.warn(moleculeBean.getName() + " has upper initial value '" + species.getInitialAmount()
					+ "' than the default one, so init value has been automatically set to the upper bound'"
					+ input.getUpperBound() + "'");
		}
		if (init % 1 != 0) {
			log.warn("Initial amount cannot be a double number for species: '" + species.getId() + "' is " + init
					+ "\nThe amount will automatically be casted to Integer!");
		}
		moleculeBean.setInit(String.valueOf((int) init));

		return moleculeBean;
	}

	private String convertFromScientificNotation(double number) {
		// Check if in scientific notation
		// if (isScientificNotation(number)) {
		// log.warn("The scientific notation number'" + number
		// + "' detected, it will be converted to normal notation.");
		NumberFormat formatter = new DecimalFormat("0", DECIMAL_FORMAT);
		formatter.setMaximumFractionDigits(340);
		// formatter.setMaximumFractionDigits(25); // = new
		// DecimalFormat("###.######");
		return formatter.format(number);
		// } else
		// return String.valueOf(number);
	}

	@SuppressWarnings("unused")
	// TODO might be removed
	private boolean isScientificNotation(double number) {
		return String.valueOf(number).toLowerCase().contains("e");
	}

	private void setDetails(SBMLDocument sbml, ST modelST) {
		String details = "SBML File: " + input.getSbmlFilePath() + "\n//SBML Level " + sbml.getLevel() + ", version "
				+ sbml.getVersion() + "\n";
		modelST.add("details", details);
	}

	private void setModel(Model sbmlModel, ST modelST) {
		String moduleName = "";
		moduleName = sbmlModel.getId();
		if (moduleName.isEmpty()) {
			moduleName = sbmlModel.getName();
		}
		modelST.add("name", TranslatorUtil.checkKeyword(moduleName));
	}

	private void setConstants(Model sbmlModel, ST modelST) {
		ST constsST = group.getInstanceOf("consts");
		Const constBean = null;

		// Compartment name as a constant
		long numCompartments = sbmlModel.getNumCompartments();
		for (int i = 0; i < numCompartments; i++) {
			Compartment comp = sbmlModel.getCompartment(i);
			String constName = comp.getId();
			if (!addedVars.contains(constName)) {
				double size = comp.getSize();
				if (!Double.isNaN(size)) {
					constBean = new Const("double", constName, convertFromScientificNotation(size));
					addedVars.add(constName);
					constsST.add("consts", constBean);
				}
			}
		}
		// Parameters which are declared in compartment level not in reaction level
		ListOf<Parameter> listOfParameters1 = sbmlModel.getListOfParameters();
		for (int i = 0; i < listOfParameters1.size(); i++) {
			Parameter parameter = listOfParameters1.get(i);
			String constName = parameter.getId();
			if (!addedVars.contains(constName)) {
				constBean = new Const("double", parameter.getId(), convertFromScientificNotation(parameter.getValue()));
				constsST.add("consts", constBean);
				addedVars.add(constName);
			}
		}
		// Add constant true, and/or boundaryConditions true, species
		long numSpecies = sbmlModel.getNumSpecies();
		if (numSpecies == 0) {
			log.warn("No species are defined");
		} else {
			Species species = null;
			for (int n = 0; n < numSpecies; n++) {
				species = sbmlModel.getSpecies(n);
				// if species if constant || or boundary condition is true
				if (isSpeciesConstant(species)) {
					String constName = species.getId();
					if (!addedVars.contains(constName)) {
						double init = getSpeciesInitialValue(species);
						if (init % 1 != 0) {
							log.warn("Initial amount cannot be a double number for species: '" + species.getId() + "' is "
									+ init + "\nThe amount will automatically be casted to Integer!");
						}
						constBean = new Const("double", constName, String.valueOf((int) init));
						addedVars.add(constName);
						constsST.add("consts", constBean);
					}
				}
			}
		}

		// TODO:05.Feb.2018 I am not sure I should use local params as constant
		// Since they are either have value at local which makes non-cons
		// or they are specified globally which are added above
		// I commented out the local names as constant, as we have to use their values in the rules,
		// we cannot use the local names as constant because name may duplicated and have different names,
		// sbml allows this but we cannot use it.

		// // Add reaction level local parameters as constant
		// for (int n = 0; n < sbmlModel.getNumReactions(); n++) {
		// Reaction reaction = sbmlModel.getReaction(n);
		// if (reaction.isSetKineticLaw()) {
		// KineticLaw kineticLaw = reaction.getKineticLaw();
		// //// TODO 04.02.2018 ListOf listOfParameters2 = kineticLaw.getListOfParameters();
		// ListOf<LocalParameter> listOfParameters2 = kineticLaw.getListOfLocalParameters();
		// for (int i = 0; i < listOfParameters2.size(); i++) {
		// LocalParameter parameter = listOfParameters2.get(i);
		// if (reaction.getReversible()) {
		// // forward rate
		// if (i == 0) {
		// constBean = new Const("double", parameter.getId(),
		// convertFromScientificNotation(parameter.getValue()));
		// }
		// // backward rate
		// else if (i == 1) {
		// constBean = new Const("double", parameter.getId(),
		// convertFromScientificNotation(parameter.getValue()));
		// }
		// }
		// // one-directional rate
		// else {
		// constBean = new Const("double", parameter.getId(),
		// convertFromScientificNotation(parameter.getValue()));
		// }
		// if (constBean != null) {
		// String constName = constBean.getName();
		// if (!addedVars.contains(constName)) {
		// constsST.add("consts", constBean);
		// addedVars.add(constName);
		// }
		// }
		// }
		// }
		// }
		modelST.add("consts", constsST);
	}

	/**
	 * A species is constant if it constant attribute set true, OR the boundaryCondition attribute set true, If it is on
	 * boundary that means its value cannot be changed by reactions but can be changed by other mechanisms such as rules,
	 * events.. In our translation since we ignore rules and events, and we only regard the reactions, so we can assume when
	 * boundary condition is true than its value will not change�
	 * 
	 * @param species
	 * @return
	 */
	public static boolean isSpeciesConstant(Species species) {
		boolean isConstant = false;
		if (species.getConstant() || species.getBoundaryCondition()) {
			isConstant = true;
		}
		return isConstant;
	}

	/**
	 * Finds the species of @param SpeciesReference and checks if the species has constant=true OR boundaryCondition=true
	 * 
	 * @param sr
	 *            SpeciesReference
	 */
	private boolean isSpeciesConstant(SpeciesReference sr) {
		boolean isConstant = false;
		Species species = null;
		species = getCorrespondingSpecies(sr);
		isConstant = isSpeciesConstant(species);
		return isConstant;
	}

	/**
	 * Get species corresponding species of a given species reference.
	 * 
	 * @param sr
	 * @param species
	 * @return
	 */
	private Species getCorrespondingSpecies(SpeciesReference sr) {
		Species species = null;
		Model model = sr.getModel();
		for (int i = 0; i < model.getNumSpecies(); i++) {
			if (sr.getSpecies().equals(model.getSpecies(i).getId())) {
				species = model.getSpecies(i);
				break;
			}
		}
		if (species == null) {
			try {
				throw new Exception("Corresponding species of species reference " + sr.getSpecies() + " not found!");
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return species;
	}

	/**
	 * Either the initial amount or the initial concentration of the species should be specified. It returns which ever one
	 * is set. If none it set, it is an error.
	 * 
	 * @param species
	 * @return
	 */
	public static double getSpeciesInitialValue(Species species) {
		double result = Double.isNaN(species.getInitialConcentration()) ? species.getInitialAmount() : species
				.getInitialConcentration();
		if (Double.isNaN(result)) {
			log.error("Please set the inital value for species {} ", species.getId());
		}
		return result;
	}
}
