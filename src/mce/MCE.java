package mce;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mce.util.Config;
import mce.util.Utils;
import mchecking.translator.qt.PQuery;
import mchecking.translator.qt.PQueryManager;
import modify.Modifier;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
//import org.sbml.libsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import output.Output;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MCE {

	private static final Logger log = LoggerFactory.getLogger(MCE.class);
	SBMLDocument sbml = null;
	Output output = null;

	public static void main(String[] arguments) {
		start(arguments);
	}

	/**
	 * @param arguments
	 */
	public static void start(String[] arguments) {
		MCE mce = new MCE();
		Inputs input = new Inputs(arguments);
		log.info("Reading model...");
		mce.mce(input);
	}

	private void mce(Inputs input) {
		try {
			compose(input);
		} catch (Exception e) {
			String error = e.getMessage();
			log.error(error);
			Utils.out(error);
			e.printStackTrace();
		}
	}

	/**
	 * Load the queries and sbml model from file, and validate them
	 * 
	 * @param input
	 * @throws Exception
	 */
	private void compose(Inputs input) throws Exception {
		// Load configurations first.
		Config.loadConfigurations();

		// Modify SBML, e.g. fix constant and species amount etc.
		sbml = getSBMLModel(input);
		if (sbml != null) {
			PQueryManager pQueryManager = new PQueryManager();
			// Read pattern queries from the file

			// TODO 04.Feb.2018 uncomment the following line and remove the @4test.
			// List<PQuery> pQueryList = pQueryManager.loadPQueriesFromFile(input.getpQueryFilePath());

			List<PQuery> pQueryList = getPQueryList(input);// TODO @4test
			if (pQueryList != null) {
				for (PQuery pQuery : pQueryList) {
					if (pQuery != null) {
						Utils.out("Pattern Query : " + pQuery.getPQuery());
						Output output = new Composer().compose(input, sbml, pQuery);
						if (output != null) {
							output.setVerificationResultFPath(input);
						}
					} else {
						log.warn("A query in pattern query file is null. We skipped it.");
					}
				}
			}
		}
		if (output != null) {
			if (output.isError) {
				// prints errors related to validation
				output.printError();
			} else {
				output.print();
			}
		}
	}

	/***
	 * Checks whether SBML has any error, If it is valid, then modify it, e.g. constant kinetic
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private SBMLDocument getSBMLModel(Inputs input) throws Exception {
		SBMLDocument document = null;
		// First Modify the model
		String sbmlFilePath = input.getSbmlFilePath();
		if (Modifier.isApproriate4Translation(sbmlFilePath)) {
			// Appropriate for modification, now apply changes.
			File rawFile = new File(sbmlFilePath);

			// TODO 04.Feb.2018 I need to have option to translate without modification
			// TODO 04.Feb.2018 sbmlFilePath = Modifier.modify(rawFile, input.getOutputDir());

			// If the file pass validation
			Validation validation = new Validation();
			if (validation.validateSBML(sbmlFilePath)) {

				SBMLReader reader = new SBMLReader();
				document = reader.readSBML(sbmlFilePath);
			} else {
				// Get the output of validation which contains error messages.
				// TODO: If the file cannot pass the validation than warn the user, use the modified version
				// and suggest the best MC

				output = validation.output;
			}
			Utils.out("Modification completed.\nThe modified file written out to : " + sbmlFilePath);
		} else {
			exit();
		}
		return document;
	}

	private static void exit() {
		log.error("The application exits.");
		System.exit(1);
	}

	// TODO 04.Feb.2018 this method copied from the performance benchmarking project, it can be deleted before production
	// TODO FIXME pQuery should be removed
	// TODO following code can be used during testing queries on real models
	private List<PQuery> getPQueryList(Inputs input) {
		List<PQuery> pQueryList = new ArrayList<PQuery>();
		if (sbml != null) {
			Model sbmlModel = sbml.getModel();
			long numSpecies = sbmlModel.getNumSpecies();
			if (numSpecies == 0) {
				log.warn("No species are defined");
			} else {
				String speciesLast = "";
				Species species = null;
				species = sbmlModel.getSpecies((int) (numSpecies - 1));// last species
				speciesLast = species.getId();
				PQuery pQuery = new PQuery();
				try {
					@SuppressWarnings("unused")
					String species0Str = "";
					Species species0 = null;
					species0 = sbmlModel.getSpecies(0);// first species
					species0Str = species0.getId();
					String query = "";

					// EVENTUALLY Query
					query = "with probability >=1 EVENTUALLY " + speciesLast + ">" + (50);
					query = "with probability >=1 " + species0Str + " >=50 UNTIL " + speciesLast + ">=50";
																												// follows
																												// first
					query = "with probability >=1 " + species0Str + " >=50 PRECEDES " + speciesLast + ">=50";// first
																												// precedes
																												// second
					query = "with probability >=1 STEADY-STATE " + speciesLast + ">=50"; // STEADY-STATE
					query = "with probability >=1 INFINITELY-OFTEN " + speciesLast + ">=50"; // STEADY-STATE
					query = "with probability >=1 NEVER " + speciesLast + ">=50"; // NEVER
					query = "with probability >=1 " + species0Str + " >=50 WEAK-UNTIL " + speciesLast + ">=50"; // WEAK-UNTIL
					// ALWAYS
					query = "with probability >=1 ALWAYS " + speciesLast + ">=" + (0);
					query = "with probability >=1 NEXT " + speciesLast + ">=" + (50);
					query = "with probability >=1 " + speciesLast + " >=50 FOLLOWS " + species0Str + ">=50"; // second

					pQuery.validateAndAssingPQuery(query);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pQueryList.add(pQuery);
			}
		}
		return pQueryList;
	}
}
