package mlearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.util.Config;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;
import mtopology.Prop;

class Predictor {
	private static final Logger log = LoggerFactory.getLogger(Predictor.class);

	/**
	 * @param patternPropsList
	 * @param pQuery
	 * @return
	 */
	public MCTypes pythonPredict(List<PatternProps> patternPropsList, PQuery pQuery) {
		// get pattern name, if with_prob and what_is_the_prob requires
		// different model types then you can modifyPquery here.
		String patternName = pQuery.getPatternsAsString();// e.g. WithProbALWAYS

		String properties = getProperties(patternPropsList);// i.e.
		MCTypes targetMC = null;
		BufferedReader stdInput = null;
		BufferedReader stdError = null;
		ProcessBuilder pb = null;
		Process process = null;
		long startTime = System.nanoTime();
		try {
			String svmPredictorPath = System.getProperty("user.dir") + File.separator + "pySMCPredictor"
					+ File.separator + "smcPredictor.py";
			// svmPredictorPath =
			// "C:\\Users\\mem_2\\OneDrive\\workspaces\\MeminSony\\ws\\mars2\\PySMCPredictor\\smcPredictor.py";
			String[] command = new String[] { Config.pythonInterpreter, svmPredictorPath, patternName, properties };

			pb = new ProcessBuilder(command);
			process = pb.start();
			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		} catch (Exception e) {
			String message = "Please make sure you set up Python interpreter correctly in the ./configs/python_config.properties file";
			log.error(message);
			log.error(e.getMessage(), e);
			System.exit(1);
		} finally {
			try {
				String line = "";
				process.waitFor();
				while ((line = stdInput.readLine()) != null) {
					try {
						targetMC = MCTypes.valueOf(line);
					} catch (Exception e) {
						log.error(line + e.getMessage(), e);
					}
				}
				// targetMC = MCTypes.valueOf(line);
				line = "";
				if ((line = stdError.readLine()) != null) {
					log.error("Error occured while trying to run python predictor. "
							+ "Please make sure you have python 3 installed, "
							+ "and you set the python path to config.properties correctly.");
					log.error(line);
					while ((line = stdError.readLine()) != null) {
						log.error(line);
					}
				}
				log.info("Total prediction time is: " + TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - startTime))
						+ " secs");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return targetMC;
	}

	/**
	 * Extract the value of properties to a list
	 * 
	 * @param patternPropsList
	 * @return list of properties value as Object type, they could be double or
	 *         string as well.
	 */
	private String getProperties(List<PatternProps> patternPropsList) {
		String properties = "";
		boolean first = true;
		for (PatternProps patternProps : patternPropsList) {
			for (Prop prop : patternProps.getProps()) {
				if (prop.isEnabled()) {
					if (first) {
						properties = prop.getValue();
						first = false;
					} else {
						properties += "," + prop.getValue();
					}
				}
			}
		}
		return properties;
	}
}