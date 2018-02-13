package performance;

import java.io.File;
import java.util.List;

import mce.Inputs;
import mce.util.Utils;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;
import mtopology.Prop;
import output.Output;

/**
 * Manages the performance results output
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class PerforRes {
	public static String csvFilePath = "output";// TODO:
												// "models/results/";
	static String columnNames = "";
	public static final int REPETATIONS = 1;
	static Output[] outputs = new Output[REPETATIONS];

	/**
	 * Save the performance of model checker depending model and pattern into a
	 * csv file
	 * 
	 * @param input
	 * @param targetMC
	 * @param pQuery
	 * @param patternPropsList
	 */
	public static void savePerforResults(Inputs input, MCTypes targetMC, PQuery pQuery,
			List<PatternProps> patternPropsList) {
		if (columnNames.isEmpty()) {
			createCSVFile(targetMC, pQuery, patternPropsList);
		}
		String row = generateContent(input, targetMC, pQuery, patternPropsList);
		Utils.write2File(csvFilePath, row, true);

	}

	/**
	 * @param input
	 * @param targetMC
	 * @param pQuery
	 * @param patternPropsList
	 * @return
	 */
	private static String generateContent(Inputs input, MCTypes targetMC, PQuery pQuery,
			List<PatternProps> patternPropsList) {
		String row = input.getFileName() + "," + input.getSbmlFilePath();
		if (targetMC != null)
			row += "," + targetMC;
		if (pQuery != null)
			row += "," + pQuery.getPatternsAsString();

		row += getExecutionOutputs();

		if (patternPropsList != null)
			row += getGraphProperties(patternPropsList);
		// add newline character to the last column
		row += "\n";
		return row;
	}

	/**
	 * @param pQuery
	 * @param patternPropsList
	 */
	private static void createCSVFile(MCTypes targetMC, PQuery pQuery, List<PatternProps> patternPropsList) {
		String fileName = "";
		if (patternPropsList != null) {
			fileName += "DPandNG";
			csvFilePath += "topologies/" + fileName + "Time_465.csv";
		} else {
			fileName += pQuery.getPatternsAsString();
			csvFilePath += pQuery.getPatternsAsString() + "/" + fileName + ".csv";
		}

		// For each 50 model create a new csv file

		// Generate header
		columnNames = "ModelName,ModelPath";
		if (targetMC != null)
			columnNames += ",ModelChecker";
		if (pQuery != null) {
			columnNames += ",PatternType";
		}

		// Execution results headers
		if (targetMC != null)
			generateExecutionResultsColumnNames();

		// Put to CSV header the enabled properties of graph
		if (patternPropsList != null)
			generateGraphPropertiesColumnNames(patternPropsList);
		// finish column names
		columnNames += "\n";
		// if file does not exist then create one
		if (!(new File(csvFilePath).exists()))
			Utils.write2File(csvFilePath, columnNames, false);
	}

	/**
	 * Add the execution results to the header of CSV file
	 */
	private static void generateExecutionResultsColumnNames() {
		// if at least one output(execution) is exist
		if (outputs.length > 0) {
			// External tool time, The MC verification time, Total Consumed time
			for (int i = 0; i < outputs.length; i++) {
				columnNames += "," + (i + 1) + "_NoError" + "," + (i + 1) + "_ExtTT" + "," + (i + 1) + "_VerifT" + ","
						+ (i + 1) + "_TotalT";
			}
			columnNames += ",Avg_ExtTT,Avg_VerifT,Avg_TotalT";
			// The probability of 'app can run without error'
			columnNames += ",PropOfNoError";
		}
	}

	/**
	 * Add the names of graph properties to the header of CSV file
	 * 
	 * @param patternPropsList
	 */
	private static void generateGraphPropertiesColumnNames(List<PatternProps> patternPropsList) {
		for (PatternProps patternProps : patternPropsList) {
			for (Prop prop : patternProps.getProps()) {
				if (prop.isEnabled()) {
					columnNames += "," + patternProps.getPrefix() + prop.getProp();
				}
			}
		}
	}

	/**
	 * Concatenate the execution part which is true if they all executed without
	 * error , the performance repetition, and their average.
	 * 
	 * @param row
	 * @return
	 */
	private static String getExecutionOutputs() {
		String performance = "";
		// if at least one output is exists
		if (outputs[0] != null) {
			// get Outputs
			Double avgExtT = 0.0, avgVerfT = 0.0, avgTotalT = 0.0;
			int errorCount = 0;
			for (int i = 0; i < outputs.length; i++) {
				// mc produced error?
				boolean hasError = outputs[i].isError;
				if (hasError)
					errorCount++;

				// always print whether or not it includes error
				performance += "," + (!hasError);

				Long extT = outputs[i].externalToolTime;
				performance += "," + extT;
				avgExtT += extT == null ? 0 : extT.doubleValue();

				Long verfT = outputs[i].verificationTime;
				performance += "," + verfT;
				avgVerfT += verfT == null ? 0 : verfT.doubleValue();

				Long totalTime = (extT == null ? 0 : extT) + (verfT == null ? 0 : verfT);
				performance += "," + totalTime;
				avgTotalT += totalTime;
			}
			// The final average includes only the runs which does not have
			// error
			Integer denom = REPETATIONS - errorCount;

			avgExtT = denom == 0 ? null : (avgExtT / denom);
			performance += "," + avgExtT;
			avgVerfT = denom == 0 ? null : (avgVerfT / denom);
			performance += "," + avgVerfT;
			avgTotalT = denom == 0 ? null : (avgTotalT / denom);
			performance += "," + avgTotalT;

			// The probability of app can run without error
			performance += "," + ((double) (REPETATIONS - errorCount) / REPETATIONS);
			// concatenate the output results
		}
		return performance;
	}

	/**
	 * 
	 * @param patternPropsList
	 * @return
	 */
	private static String getGraphProperties(List<PatternProps> patternPropsList) {
		String row = "";
		for (PatternProps patternProps : patternPropsList) {
			for (Prop prop : patternProps.getProps()) {
				if (prop.isEnabled()) {
					row += "," + prop.getValue();// prop.getElapsedTime(); //
				}
			}
		}
		return row;
	}

	/**
	 * Accumulate performance results, i.e. outputs
	 * 
	 * @param output
	 */
	public static void addOutput(Output output) {
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] == null) {
				outputs[i] = output;
				break;
			}
		}
	}

	/**
	 * 
	 */
	public static void cleanOutputs() {
		outputs = new Output[REPETATIONS];
	}

}
