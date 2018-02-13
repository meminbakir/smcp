package mce;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	private String lowwerBound = "0";
	private String upperBound = String.valueOf(Integer.MAX_VALUE - 1);
	private String simSamples = "500";// simulation samples
	private String simDepth = "5000";// simulation depth
	private String sbmlDirectoryPath;
	private boolean directoryProvided = false;
	private String outputDir = "./output";

	public Inputs(String[] arguments) {
		manageArgs(arguments);
	}

	public void manageArgs(String[] arguments) {
		if (getArgument(arguments, "-h") != null || getArgument(arguments, "-help") != null) {
			usage();
			exit();
		}

		if (getArgument(arguments, "-s") == null) {
			print("An sbml file should be provided");
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
		lowwerBound = setOptionalArguments(arguments, lowwerBound, optionalParamName);
		optionalParamName = "-upB";
		upperBound = setOptionalArguments(arguments, upperBound, optionalParamName);
		optionalParamName = "-o";
		outputDir = setOptionalArguments(arguments, outputDir, optionalParamName);
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
	 * @return the lowwerBound
	 */
	public String getLowwerBound() {
		return lowwerBound;
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
			log.info("Optinal parameter " + optionalParamName + " not specified, therefore the default value '"
					+ optionalParam + "' will be used.");
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
		String usage = "Usage:\n" + "\tmce [-s] \"SBML file path\" [-q] \"Pattern based query file path\" \n"
				+ "Optional:\n" + "\t[-lB] lower bound of molecules\n" + "\t[-upB] upper bound of molecules\n"
				+ "\t[-o] output directory (./output is default)\n" + "for help [-h] | [-help]\n";
		print(usage);
	}

	private void exit() {
		log.error("The application exits.");
		System.exit(1);
	}

}
