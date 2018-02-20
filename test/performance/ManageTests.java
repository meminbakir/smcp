package performance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import mce.Inputs;
import mce.util.StringUtils;
import mce.util.Utils;
import mchecking.MCheck;
import mchecking.ModelChecker;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;

import org.sbml.jsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import output.Output;

/**
 * This class used for managing, testing and recording the performance tests.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class ManageTests {
	// if model does not run(has error) will be moved to the unsuccess folder
	private final static String UNSUCCESS = "unsuccess";
	private final static String unSuccessExplanationFile = "UNSuccessExplanations.txt";

	private static final Logger log = LoggerFactory.getLogger(ManageTests.class);

	/**
	 * @param input
	 * @param sbml
	 * @param pQuery
	 * @param patternPropsList
	 * @return
	 */
	public static Output runPerformanceTests(Inputs input, SBMLDocument sbml, PQuery pQuery,
			List<PatternProps> patternPropsList) {
		MCTypes[] mcTypes = null;
		// mcTypes = new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA,
		// MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 };
		mcTypes = new MCTypes[] { MCTypes.PRISM};//MCTypes.PRISM, MCTypes.PLASMA
		// mcTypes = new MCTypes[] { MCTypes.MRMC, MCTypes.MC2, MCTypes.YMER };
		// mcTypes = new MCTypes[] { MCTypes.PRISM, MCTypes.MC2 };

		// If there are some model checking test will be performed
		Output output = null;
		if (mcTypes != null) {
			for (MCTypes targetMC : mcTypes) {
				List<MCTypes> supportedMCs = ModelChecker.getSupportedModelCheckers(pQuery);
				if (supportedMCs.contains(targetMC)) {
					/***
					 * TODO : REMOVE THE FOLLOWING if (targetMC == MCTypes.MRMC) block, directory cleaning code.
					 */
					if (targetMC == MCTypes.MRMC) {
						removeMRMCExtras();
					}

					// repeat the model checking and eventually get their
					// average
					for (int i = 0; i < PerforRes.REPETATIONS; i++) {
						output = new MCheck().modelCheck(input, sbml, new ModelChecker(targetMC), pQuery);
						output.setVerificationResultFPath(input);
						PerforRes.addOutput(output);
						// Print detailed errors
						output.print();

						/*
						 * If error type is timeout, or fatal error(belongs MRMC execution) then we will not repeat the
						 * execution of this model, we will fill remaining output(performance) values with defaults only.
						 */
						try {
							if (shallErrorStopModelExecution(i, output)) {
								putNulls2RemainedOutputs();
								copyErrorFiles(input, output);// move
								break;
							}
							// If it is not timeout,copy file to unsuccess
							// folder, and
							// continue verification, e.g., prism error
							if (output.isError) {
								copyErrorFiles(input, output);// copy
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					// Save the results
					PerforRes.savePerforResults(input, targetMC, pQuery, patternPropsList);
					PerforRes.cleanOutputs();

					/***
					 * TODO : REMOVE THE FOLLOWING if (targetMC == MCTypes.MRMC) block, directory cleaning code. To guarantee
					 * free space, this need to be called here too.
					 */
					if (targetMC == MCTypes.MRMC) {
						removeMRMCExtras();
					}
				} else {
					System.err.println("The model checker " + targetMC + " CANNOT execute the pattern :"
							+ pQuery.getPQuery());
				}

			}
		} else {// Without model checking just for saving pattern results
			PerforRes.savePerforResults(input, null, pQuery, patternPropsList);
		}
		return output;

	}

	/**
	 * Remove the files which had timeout error, and write the error reasons to explanation file
	 * 
	 * @param input
	 * @param output
	 * @param isCopy
	 *            , if copy true, if move false
	 * @throws IOException
	 */

	private static void copyErrorFiles(Inputs input, Output output) throws IOException {

		Path sourceFile = Paths.get(input.getSbmlFilePath());

		String sbmlDirectoryPath = input.getSbmlDirectoryPath();
		if (StringUtils.isNullOrEmpty(sbmlDirectoryPath))
			sbmlDirectoryPath = "./";
		Path unSuccessDirPath = Paths.get(sbmlDirectoryPath + File.separator + UNSUCCESS);
		if (!unSuccessDirPath.toFile().exists())
			Files.createDirectories(unSuccessDirPath);
		Path unSuccessModel = Paths.get(unSuccessDirPath + File.separator + sourceFile.getFileName());

		try {
			Files.copy(sourceFile, unSuccessModel, StandardCopyOption.REPLACE_EXISTING);

			String explanation = "TIME: " + Utils.getDateandTime(true, true, null) + " The model '"
					+ input.getSbmlFilePath() + "' couldn't be verified.\n" + "REASON: " + output.error + "\n\n\n";

			Utils.write2File((unSuccessDirPath + File.separator + unSuccessExplanationFile), explanation, true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * If error type is timeout, or fatal error(belongs mrmc execution) then we will not repeat the execution of this model,
	 * we will fill remaining output(performance) values with defaults only.
	 * 
	 * @param i
	 * @param output
	 * @return
	 */

	private static boolean shallErrorStopModelExecution(int i, Output output) {
		if (output.isError && i == 0) {
			if (output.error.contains("TIMEOUT")) {
				return true;
			}
			if (output.error.contains("fatal error"))
				return true;
		}
		return false;
	}

	/**
	 * File result csv with null outputs, and move model to unsuccessful part
	 */
	private static void putNulls2RemainedOutputs() {
		for (int j = 1; j < PerforRes.REPETATIONS; j++) {
			Output output = new Output();
			output.isError = true;
			output.error = "Verification manually stopped for this model because of TIMEOUT limit\n";
			PerforRes.addOutput(output);
		}
	}

	/**
	 * TODO: Just for removing MRMC garbage, Delete function at the end.
	 */
	public static void removeMRMCExtras() {
		// TODO: MRMC will clean the previous translated and
		// verification folders, to not cause hardisc space
		// problem
		log.info("This is MRMC Model checker, I will clean previous translated and verification files first.");
		String translatedDir = System.getProperty("user.dir") + File.separator + "models" + File.separator + "translated";
		String verificationDir = System.getProperty("user.dir") + File.separator + "models" + File.separator
				+ "verification";
		try {
			// delete translated dir
			File directory = new File(translatedDir);
			if (directory.exists())
				Utils.deleteDirectory(directory);

			// delete verification dir
			directory = new File(verificationDir);
			if (directory.exists())
				Utils.deleteDirectory(directory);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
