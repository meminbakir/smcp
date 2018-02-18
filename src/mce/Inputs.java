package mce;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mehmet
 *
 */
public class Inputs {
	private static final Logger log = LoggerFactory.getLogger(Inputs.class);

	private String sbmlFilePath = "";
	private String fileName = "";
	private String pQueryFilePath = "";
	private String lowerBound = "0";
	private String upperBound = "100";
	private String simSamples = "500";// simulation samples
	private String simDepth = "5000";// simulation depth
	private String sbmlDirectoryPath;
	private boolean directoryProvided = false;
	private String outputDir = "./output";

	// @formatter:off
	static String usage =
			"\njava -jar smcp.jar -s <SBMLFile> -q <PatternQueryFile> [-lB <lowerBound>] [-uB <upperBound>] \n"
			+"\t<SBMLFile> \t\t SBML file path\n"
			+"\t<PatternQueryFile> \t Pattern query file path\n"
			+ "Optional:\n" 
			+ "\t[-lB <lowerBound>] the lower bound for species population, it must be between (-2147483648, 2147483647) [default: 0]\n"
			+ "\t[-uB <upperBound>] the upper bound for species population, it must be between (-2147483648, 2147483647) [default: 100]\n"
			+ "\t[-o] output directory [default: ./output]\n" 
			+ "\t[-h | -help] show the tool usage";
	// @formatter:on

	public Inputs(String[] arguments) {
		manageArgs(arguments);
	}

	public void manageArgs(String[] arguments) {
		if (getArgument(arguments, "-h") != null || getArgument(arguments, "-help") != null) {
			usage();
			exit();
		}

		if (getArgument(arguments, "-s") == null) {
			print("An SBML file should be provided");
			usage();
			exit();
		} else {
			setSbmlFilePath(getArgument(arguments, "-s"));
		}

		if (getArgument(arguments, "-q") == null) {
			print("The pattern query file path should be preceded-by '-q' parameter");
			usage();
			exit();
		} else {
			pQueryFilePath = getArgument(arguments, "-q");
		}

		String optionalParamName = "-lB";
		lowerBound = setOptionalArguments(arguments, lowerBound, optionalParamName);
		lowerBound = setBounds(lowerBound);
		optionalParamName = "-uB";
		upperBound = setOptionalArguments(arguments, upperBound, optionalParamName);
		upperBound = setBounds(upperBound);
		// upperBound should be greater than lower bound.
		checkBounds(lowerBound, upperBound);

		optionalParamName = "-o";
		outputDir = setOptionalArguments(arguments, outputDir, optionalParamName);
	}

	private void checkBounds(String lowerBound, String upperBound) {
		if (Integer.parseInt(lowerBound) >= Integer.parseInt(upperBound)) {
			print("Lower bound [-lB] " + lowerBound + " cannot be bigger than upper bound [-upB] " + upperBound);
			usage();
			exit();
		}
	}

	private String setBounds(String value) {
		Integer bound = getInteger(value);
		if (!(Math.abs(bound) <= (Integer.MAX_VALUE - 1))) {
			print("The bounds should be an integer value between (-2147483648, 2147483647)");
			usage();
			exit();
		}
		return String.valueOf(bound);
	}

	/**
	 * @param sbmlFilePath
	 *            the sbmlFilePath to set
	 */
	public void setSbmlFilePath(String sbmlFilePath) {
		this.sbmlFilePath = sbmlFilePath;
		setFileName(sbmlFilePath);
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	private void setFileName(String filePath) {
		if (!filePath.isEmpty()) {
			Path path = Paths.get(filePath);
			fileName = path.getFileName().toString();
			if (fileName.indexOf(".") > 0) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
		}
	}

	/**
	 * @return the sbmlFilePath
	 */
	public String getSbmlFilePath() {
		return sbmlFilePath;
	}

	/**
	 * @return the pQueryFilePath
	 */
	public String getpQueryFilePath() {
		return pQueryFilePath;
	}

	/**
	 * @return the sbmlDirectoryPath
	 */
	public String getSbmlDirectoryPath() {
		return sbmlDirectoryPath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the lowerBound
	 */
	public String getLowerBound() {
		return lowerBound;
	}

	/**
	 * @return the upperBound
	 */
	public String getUpperBound() {
		return upperBound;
	}

	/**
	 * @return the simSamples
	 */
	public String getSimSamples() {
		return simSamples;
	}

	/**
	 * @return the simDepth
	 */
	public String getSimDepth() {
		return simDepth;
	}

	/**
	 * @return the directoryProvided
	 */
	public boolean isDirectoryProvided() {
		return directoryProvided;
	}

	public String getOutputDir() {
		return outputDir + File.separator;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	private String setOptionalArguments(String[] arguments, String optionalParam, String optionalParamName) {
		// Optional params
		String optinalParamValue = getArgument(arguments, optionalParamName);
		if (optinalParamValue != null) {
			return optinalParamValue;
		} else {
			log.info("Optional parameter " + optionalParamName + " not specified, therefore the default value '"
					+ optionalParam + "' is used.");
			return optionalParam;
		}
	}

	private String getArgument(String[] arguments, String param) {
		String value = null;
		param = param.toLowerCase();
		for (int i = 0; i < arguments.length; i++) {
			String arg = arguments[i].toLowerCase();
			if (arg.equals(param)) {
				// if it is help there wont be next item
				if (param.equals("-h") || param.equals("-help")) {
					return "";// no value needed.
				}
				// get the value, next item
				if ((i + 1) < arguments.length) {
					value = arguments[i + 1];
					break;
				}
			}
		}
		return value;
	}

	void print(String msg) {
		System.out.println(msg);
	}

	private void usage() {
		print(usage);
	}

	private void exit() {
		print("Exited!");
		System.exit(1);
	}

	/**
	 * Return the integer value of the string, if it is not integer it will return
	 * null.
	 * 
	 * @param str
	 * @return
	 */
	private Integer getInteger(String str) {
		Integer number = null;
		try {
			number = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			// not number
		}
		return number;
	}

}
