package modify;

import java.io.File;
import java.io.PrintStream;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.ASTNode.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.MySBMLReader;

//import org.sbml.libsbml.ASTNode;
//import org.sbml.libsbml.Compartment;
//import org.sbml.libsbml.KineticLaw;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.Parameter;
//import org.sbml.libsbml.Reaction;
//import org.sbml.libsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLReader;
//import org.sbml.libsbml.SBMLWriter;
//import org.sbml.libsbml.Species;
//import org.sbml.libsbml.SpeciesReference;
//import org.sbml.libsbml.libsbml;

import mce.util.Utils;

public class Modifier {
	static int sbmlLevel, sbmlVersion;
	private static final Logger log = LoggerFactory.getLogger(Modifier.class);

	public static boolean isApproriate4Translation(String sbmlFilePath) throws Exception {
		boolean isValid = true;
		String errors = "";
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
			isValid = false;
		}
		long numSpecies = sbmlModel.getNumSpecies();
		if (numSpecies == 0) {
			errors += "No species are defined.\n";
			isValid = false;
		}
		// Check if kinetic formula is okay
		long numReactions = sbmlModel.getNumReactions();
		if (numReactions == 0) {
			errors += "No Reactions are defined.\n";
			isValid = false;
		}
		if (isValid) {
			log.debug(sbmlFilePath + " does not have error. Ready for modification.");
		} else {
			log.error("SBML file '" + sbmlFilePath + "' contains following problems.\n" + errors);
		}

		return isValid;
	}

	public static String modify(File rawFile, String targetDir) {
		log.info("Modifying the model for prediction.");
		String toPath = null;
		try {
			// If target directory is not exist, then create it.
			File directory = new File(String.valueOf(targetDir));
			if (!directory.exists()) {
				Utils.out("The " + targetDir + " directory is not exist, we are making it...");
				directory.mkdir();
			}
			toPath = targetDir + File.separator + rawFile.getName() + ".modified.sbml";
			MySBMLReader reader = new MySBMLReader();
			SBMLWriter writer = new SBMLWriter();

			// read document
			SBMLDocument from = reader.readSBML(rawFile.getPath());
			Model fromModel = from.getModel();
			// create target sbml
			sbmlLevel = from.getLevel();
			sbmlVersion = from.getVersion();
			SBMLDocument to = new SBMLDocument(sbmlLevel, sbmlVersion);
			Model toModel = to.createModel();
			toModel.setId("untitled");
			// modify and move compartments,
			addCompartment(toModel);
			// modify species
			addSpecies(fromModel, toModel);
			// modify and move reactions
			addReactions(fromModel, toModel);
			// write file back
			writer.writeSBMLToFile(to, toPath);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		log.debug("Modification completed.\nThe modified file written out to : " + toPath);
		return toPath;
	}

	private static void addReactions(Model fromModel, Model toModel) {
		String reactionName = "react";
		String kineticConstname = "c";
		for (int i = 0; i < fromModel.getNumReactions(); i++) {
			// read species
			Reaction fromReaction = fromModel.getReaction(i);
			// generate reaction and add to target
			Reaction toReaction = toModel.createReaction();
			// Set reaction specific features
			toReaction.setId(fromReaction.getId());
			toReaction.setName(reactionName + (i + 1));
			toReaction.setFast(fromReaction.getFast());
			toReaction.setReversible(fromReaction.getReversible());

			// get reactants
			SpeciesReference toSR;
			KineticLaw toKL;
			for (int j = 0; j < fromReaction.getNumReactants(); j++) {
				SpeciesReference fromReactantSR = fromReaction.getReactant(j);
				String speciesName = fromReactantSR.getSpecies();
				toSR = toReaction.createReactant();
				toSR.setSpecies(speciesName);
				// A species reference's stoichiometry is set by its
				// 'stoichiometry' attribute exactly once. If the
				// SpeciesReference object's 'constant' attribute has the
				// value true, then the stoichiometry is fixed and cannot be
				// changed @see
				// http://sbml.org/Software/libSBML/docs/java-api/org/sbml/libsbml/SpeciesReference.html
				if (sbmlLevel > 2)
					toSR.setConstant(fromReactantSR.getConstant());

				// stoichiometries can be float or very big number where prism
				// cannot handle them, hence we do not add it to translation
				// toSR.setStoichiometry(fromReactantSR.getStoichiometry());

				/**
				 * If there are reactants but no products which means there is degration, Then
				 * generate a degrade species, and add them to products
				 */
				if (!(fromReaction.getNumProducts() > 0)) { // no product exist
					String degradedID = speciesName + "_degraded";// degrades
																	// will be 0
																	// and stay
																	// 0
					createSpecies(toModel, degradedID, 0, true);
					// Then add to reaction product
					toSR = toReaction.createProduct();
					toSR.setSpecies(degradedID);
					// A species reference's stoichiometry is set by its
					// 'stoichiometry' attribute exactly once. If the
					// SpeciesReference object's 'constant' attribute has the
					// value true, then the stoichiometry is fixed and cannot be
					// changed @see
					// http://sbml.org/Software/libSBML/docs/java-api/org/sbml/libsbml/SpeciesReference.html
					if (sbmlLevel > 2)
						toSR.setConstant(true);// the generator and degraded are
												// const.
				}
			}
			// get products
			for (int j = 0; j < fromReaction.getNumProducts(); j++) {
				SpeciesReference fromProductSR = fromReaction.getProduct(j);
				String speciesName = fromProductSR.getSpecies();
				toSR = toReaction.createProduct();
				toSR.setSpecies(speciesName);
				// A species reference's stoichiometry is set by its
				// 'stoichiometry' attribute exactly once. If the
				// SpeciesReference object's 'constant' attribute has the
				// value true, then the stoichiometry is fixed and cannot be
				// changed @see
				// http://sbml.org/Software/libSBML/docs/java-api/org/sbml/libsbml/SpeciesReference.html
				if (sbmlLevel > 2)
					toSR.setConstant(fromProductSR.getConstant());
				// stoichiometries can be float or very big number where prism
				// cannot handle them, hence we do not add it to translation
				// toSR.setStoichiometry(fromProductSR.getStoichiometry());
				/**
				 * If there are products but no reactions which means we need generator species,
				 */
				if (!(fromReaction.getNumReactants() > 0)) { // no product exist
					String generatorID = speciesName + "_generator";
					createSpecies(toModel, generatorID, 1, true); // generators
																	// starts
																	// with 1
																	// and
																	// remains 1
					// Then add to reaction product
					toSR = toReaction.createReactant();
					toSR.setSpecies(generatorID);
					// A species reference's stoichiometry is set by its
					// 'stoichiometry' attribute exactly once. If the
					// SpeciesReference object's 'constant' attribute has the
					// value true, then the stoichiometry is fixed and cannot be
					// changed @see
					// http://sbml.org/Software/libSBML/docs/java-api/org/sbml/libsbml/SpeciesReference.html
					if (sbmlLevel > 2)
						toSR.setConstant(true);// the generator and degraded are
												// const.
				}
			}
			// generate kinetic law
			toKL = toReaction.createKineticLaw();
			// ASTNode c1 = new ASTNode();
			// c1.setName(kineticConstname + (i + 1));
			// toKL.setMath(c1);
			// // Creates a Parameter ("c1")
			// LocalParameter toParam = toKL.createLocalParameter();
			// toParam.setId(kineticConstname + (i + 1));
			// toParam.setValue(1);// Constant kinetic rate
			// toParam.setUnits("substance");
			ASTNode c1 = new ASTNode();
			c1.setType(Type.INTEGER);
			c1.setName(kineticConstname + (i + 1));
			c1.setValue(1);
			toKL.setMath(c1);
			
		}
	}

	private static void createSpecies(Model toModel, String spID, double initialAmount, boolean isConstant) {
		Species sp;
		if (!isSpeciesExist(toModel, spID)) {
			sp = toModel.createSpecies();
			sp.setId(spID);
			sp.setName(spID);
			sp.setInitialAmount(initialAmount);
			sp.setConstant(isConstant);
			sp.setBoundaryCondition(true);
			sp.setHasOnlySubstanceUnits(false);// the default for sbml v2 is
												// false,
			sp.setCompartment("default");
		}
	}

	private static boolean isSpeciesExist(Model toModel, String spID) {
		for (int i = 0; i < toModel.getNumSpecies(); i++) {
			if (toModel.getSpecies(i).getId().equals(spID))
				return true;
		}
		return false;
	}

	private static void addSpecies(Model fromModel, Model toModel) {
		int initialAmount = 50;// 100
		for (int i = 0; i < fromModel.getNumSpecies(); i++) {
			// read species
			Species fromSp = fromModel.getSpecies(i);
			// generate and add to target
			Species toSp = toModel.createSpecies();
			String id = fromSp.getId();
			toSp.setId(id);
			toSp.setName(id);
			toSp.setInitialAmount(initialAmount);
			toSp.setConstant(fromSp.getConstant());
			toSp.setBoundaryCondition(fromSp.getBoundaryCondition());
			toSp.setCompartment("default");
			// if (isLevel3()) {
			/*
			 * The role of the attribute 'hasOnlySubstanceUnits' is to indicate whether the
			 * units of the species, when the species identifier appears in mathematical
			 * formulas, are intended to be concentration or amount. The attribute takes on
			 * a boolean value. In SBML Level 3, the attribute has no default value and must
			 * always be set in a model; in SBML Level 2, it has a default value of false.
			 * We used the same as the source model, but in verification it will not change
			 * our mc model.
			 */
			toSp.setHasOnlySubstanceUnits(fromSp.getHasOnlySubstanceUnits());
			// }
		}
	}

	private static void addCompartment(Model toModel) {
		Compartment comp = toModel.createCompartment();
		String id = "default";
		double size = 1;
		String unit = "volume";
		comp.setId(id);
		comp.setName(id);
		comp.setSize(size);
		comp.setUnits(unit);
		// if (isLevel3()) {
		comp.setConstant(true);
		// }
	}

	// private static boolean isLevel3() {
	// return sbmlLevel == 3;
	// }
}
