package mce;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.util.Config;
import mce.util.Utils;
import mchecking.translator.qt.PQuery;
import mchecking.translator.qt.PQueryManager;
import modify.Modifier;
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
		log.info("Application started.");
		MCE mce = new MCE();
		Inputs input = new Inputs(arguments);
		log.info("Model file {}\n", input.getFileName());
		mce.mce(input);
		log.info("For more execution details please check the log file.");
	}

	private void mce(Inputs input) {
		try {
			Output output = compose(input);
			// if (output != null) {
			// if (output.isError) {
			// // prints errors related to validation
			// output.printError();
			// } else {
			// if (input.isPredict()) {
			// output.printPredictionResult();
			// } else if (input.isVerify()) {
			// output.print();
			// }
			// }
			// }
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Load the queries and sbml model from file, and validate them
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private Output compose(Inputs input) throws Exception {
		Output output = null;
		// Load configurations first.
		Config.loadConfigurations();
		// Modify SBML, e.g. fix constant and species amount etc.
		sbml = getSBMLModel(input);
		if (sbml != null) {
			PQueryManager pQueryManager = new PQueryManager();
			// Read pattern queries from the file
			// TODO 04.Feb.2018 uncomment the following line and the line after which has
			// the @4test annotation.
			List<PQuery> pQueryList = pQueryManager.loadPQueriesFromFile(input.getpQueryFilePath());

			// List<PQuery> pQueryList = getPQueryList(input);// TODO @4test
			if (pQueryList != null) {
				for (PQuery pQuery : pQueryList) {
					if (pQuery != null) {
						log.info("Analysing Pattern Query: {}", pQuery.getPQuery());
						output = new Composer().compose(input, sbml, pQuery);
						if (output != null) {
							output.setVerificationResultFPath(input);
							if (output.isError) {
								// prints errors related to validation
								output.printError();
							} else {
								if (input.isPredict()) {
									output.printPredictionResult();
								} else if (input.isVerify()) {
									output.print();
								}
							}
						} else
							System.out.println(String.format("%s", Utils.lineSeparator()));
					} else {
						log.warn("A query in pattern query file is null.It is skipped.");
					}

				}
			}
		}
		return output;
	}

	/***
	 * Checks whether SBML has any error, If it is valid, then modify it, e.g.
	 * constant kinetic
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private SBMLDocument getSBMLModel(Inputs input) throws Exception {
		MySBMLReader reader = new MySBMLReader();
		SBMLDocument document = null;
		// First Modify the model
		String sbmlFilePath = input.getSbmlFilePath();
		if (Modifier.isApproriate4Translation(sbmlFilePath)) {
			// Appropriate for modification, now apply changes.
			File rawFile = new File(sbmlFilePath);
			// if user want to only get the prediction
			if (input.isPredict()) {
				sbmlFilePath = Modifier.modify(rawFile, input.getOutputDir());
				document = reader.readSBML(sbmlFilePath);
			} else if (input.isVerify()) { // if user wants verification
				// is file valid
				Validation validation = new Validation(input);
				if (validation.validateSBML(sbmlFilePath)) {
					document = reader.readSBML(sbmlFilePath);
				} else {
					output = validation.output;
				}
			}
		} else {
			exit();
		}
		return document;
	}

	private static void exit() {
		log.error("The application exits.");
		System.exit(1);
	}

	// TODO 04.Feb.2018 this method copied from the performance benchmarking
	// project, it can be deleted before production
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
					// follows
					// first
					// precedes
					// second
					//
					// // ALWAYS
					query = "with probability >=0.3 NEXT " + speciesLast + ">=" + (1);
					query = "with probability >=1 " + speciesLast + " >=3 FOLLOWS " + species0Str + ">=2"; // second
					query = "with probability >=1 " + speciesLast + " >=50 RELEASE " + species0Str + ">=50";
					query = "with probability >=1 " + species0Str + " >=50 PRECEDES " + speciesLast + ">=50";// first
					query = "with probability >=1 NEVER " + speciesLast + ">=50"; // NEVER
					query = "with probability >=1 " + species0Str + " >=50 WEAK-UNTIL " + speciesLast + ">=50"; // WEAK-UNTIL
					query = "with probability >=1 STEADY-STATE " + speciesLast + ">=1"; // STEADY-STATE
					query = "with probability >=1 INFINITELY-OFTEN " + speciesLast + ">=5"; // STEADY-STATE
					query = "with probability >=0.1 EVENTUALLY " + speciesLast + ">" + 10;
					query = "with probability >=1 ALWAYS " + speciesLast + ">=" + (0);
					query = "with probability >=1 " + species0Str + " >=50 UNTIL " + speciesLast + ">=50";

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
