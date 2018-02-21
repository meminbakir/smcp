/**
 * 
 */
package output;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mce.util.Utils;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.MCQuery;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class Output {
	private static final Logger log = LoggerFactory.getLogger(Output.class);
	private static final String verificationDirectory = "verification";
	private String verificationResultFPath = "";

	private String fileName = null;
	private int lineNumber = 0;
	public MCTypes mcType = null;
	private int queryID = 0;
	private String pQuery = "";
	private String translatedQuery = "";
	private boolean isVerifiable = true;

	public boolean isError = false;// Whether error risen
	public String error = "";

	// The execution result of external tools before verification, e.g. prism
	// simulator, prismMrmcExport
	public boolean hasExtTool = false;
	public String extResult = "";

	// verificationResult
	public String verResult = "";

	// Metrics
	public Long externalToolTime = null;
	public Long verificationTime = null;
	public String memory = null;

	// this includes remained outputs, includes comment and not verifiable
	// output
	public String result = "";
	private String queryTranslationNote = "";
	public String startTime;
	private String predictResultMessage;

	public Output() {
	}

	public Output(MCQuery mcQuery) {
		lineNumber = mcQuery.getLineNumber();
		mcType = mcQuery.getTargetMC().getType();
		queryID = mcQuery.getQueryID();
		pQuery = mcQuery.getPQuery().getPQuery();
		translatedQuery = mcQuery.getTranslatedQuery();
		isVerifiable = mcQuery.isVerifiable();
		queryTranslationNote = mcQuery.getNote();
	}

	/**
	 * @return the verification directory
	 */
	public String getVerificationdirectoryPath(Inputs input) {
		//
		// String verificationDirectoryPath = System.getProperty("user.dir") +
		// File.separator + "models" + File.separator
		// + verificationDirectory + File.separator;

		String verificationDirectoryPath = input.getOutputDir() + File.separator + verificationDirectory
				+ File.separator;
		return verificationDirectoryPath;
	}

	/**
	 * @return the verificationResultFPath
	 */
	public String getVerificationResultFPath() {

		return verificationResultFPath;
	}

	/**
	 * @param verificationResultFPath
	 *            the verificationResultFilePath to set
	 */
	public void setVerificationResultFPath(Inputs input) {
		fileName = input.getFileName();
		verificationResultFPath = getVerificationdirectoryPath(input) + mcType + "_" + fileName + "." + "result.txt";
	}

	public void print() {
		String result = getInputDetails();
		if (isVerifiable) {
			result += getVerificationResult();
		} else {
			result += this.result;
		}

		result += Utils.lineSeparator();
		log.debug(result);
		Utils.out(result);

		// mrmc it produces its output with command line, in order to not
		// overwrite the result file, I append the output details to file
		if (mcType.equals(MCTypes.MRMC))
			Utils.write2File(verificationResultFPath, result, true);
		else
			Utils.write2File(verificationResultFPath, result, true);
	}

	/**
	 * If command set to predict which means get the SMC prediction only and does
	 * not perform verification. Then we will show only The prediction result.
	 */
	public void printPredictionResult() {
		log.debug(predictResultMessage);
		Utils.out(predictResultMessage);
	}

	private String getInputDetails() {
		// String result = "\n\t----- Execution Summary -----\n" + "Starting Time: " +
		// startTime + "\n" + "File Name: "
		// + fileName + "\n" + "Query ID: " + queryID + " (line, " + lineNumber + ")\n"
		// + "Selected Model Checker: "
		// + mcType + "\nPattern Query: " + pQuery + "\n";

		String result = "\n\t----- Execution Summary   -----\n" + "Starting Time: " + startTime + "\n" + "File Name: "
				+ fileName + "\n" + "Predicted Model Checker: " + mcType + "\nPattern Query: " + pQuery + "\n";
		return result;
	}

	private String getVerificationResult() {

		String result = "Translated Query: " + translatedQuery + "\n";
		// + (queryTranslationNote.isEmpty() ? "" : ", Note: " + queryTranslationNote) +
		// "\n";
		if (isError) {
			result += "Elapsed Time: N/A \n";
			result += "\t-----   Error Details!   -----\n" + error;
			// if error risen from external tools, get the message
			if (hasExtTool) {
				result += extResult;
			}
		} else {
			if (hasExtTool) {
				result += "Elapsed time = " + "External tool time: " + TimeUnit.NANOSECONDS.toSeconds(externalToolTime)
						+ " (sec) + " + "Model Checker time: " + TimeUnit.NANOSECONDS.toSeconds(verificationTime)
						+ " (sec) Total time: " + TimeUnit.NANOSECONDS.toSeconds((externalToolTime + verificationTime))
						+ " (sec))\n";
			} else
				result += "Elapsed Time: " + TimeUnit.NANOSECONDS.toSeconds(verificationTime) + " (sec)\n";
		}
		result += "\t----- " + mcType + " Verification Output   -----\n";
		result += verResult;
		// result+="----- Verification Ended -----";
		return result;
	}

	// Print pure only errors
	public void printError() {
		log.error(error);
//		Utils.out(error);
	}

	public void setPredictResult(String message) {
		predictResultMessage = message;
	}

}
