/**
 * 
 */
package mchecking.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mce.util.Utils;
import mce.util.Utils.OS;
import mchecking.ModelChecker;
import mchecking.translator.qt.MCQuery;
import output.Output;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class PLASMAVerify extends Verify {
	static final Logger log = LoggerFactory.getLogger(PLASMAVerify.class);

	public PLASMAVerify(Inputs input, ModelChecker targetMC) {
		super(input, targetMC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.verify.IVerify#verify(java.lang.String,
	 * mchecking.translator.qt.MCQuery)
	 */
	@Override
	public Output verify(String mcModelFilePath, MCQuery mcQuery) throws IOException, InterruptedException {
		this.output = new Output(mcQuery);
		this.output.startTime = Utils.getDateandTime(true, true, null);
		// Plasma requires properties be read from a file, we need to save queries
		// first.
		String customQueryFilePath = this.saveQuery2File(mcQuery.getTranslatedQuery(), ".bltl");

		Path classPath = Paths.get(System.getProperty("user.dir"));
		// relative path of model and query file regarding class path
		Path mcModelRelPath = classPath.relativize(Paths.get(mcModelFilePath));
		Path customQueryRelPath = classPath.relativize(Paths.get(customQueryFilePath));
		// Command Example: java -jar fr.inria.plasmalab.plasmalab-1.3.2.jar -t -a
		// montecarlo -A"Total samples"=50 -m
		// NaCL.rml:rml --format csv -r liveness.bltl:bltl
		// String totalSample = "-A\"Total samples\"=1000";
		// String[] mcCommand = {"java", "-jar", MCPropsLoader.getAppPath(), "-t", "-a",
		// "montecarlo",totalSample, "-m",
		// mcModelRelPath + ":rml", "--format", "csv", "-r", customQueryRelPath +
		// ":bltl" };
		try {
			// PLASMA parameters act differently in windows and linux
			String plasmaCommand = "java -jar " + this.targetMC.getAppPath() + " -t " + "-a " + "montecarlo "
					+ "-A\"Total samples\"=" + this.input.getSimSamples() + " -m " + mcModelRelPath + ":rml "
					+ "--format " + "csv " + "-r " + customQueryRelPath + ":bltl";
			OS os = Utils.getOS();
			switch (os) {
			case WINDOWS:
				Utils.runCommand(plasmaCommand, this.output);
				break;
			default:
				// Linux forced to run command on terminal.
				String mcCommand[] = { "/bin/sh", "-c", plasmaCommand };
				Utils.runCommand(mcCommand, this.output);
				// if ((os != OS.LINUX)) {
				// String warning = "Your operating system(" + os + ") not tested, the app just
				// tested with "
				// + OS.WINDOWS + " and " + OS.LINUX;
				// log.warn(warning);
				// }
				break;
			}
		} catch (Exception e) {
			log.error("Exception thrown while '" + this.targetMC.getAppPath() + "' tryied to verify property'" + mcQuery
					+ "\n" + e.getMessage());
			e.printStackTrace();
		}
		return this.output;
	}

	/**
	 * Saves a query to file, if needed. This is part of verification process (not
	 * output)
	 * 
	 * @param input
	 * 
	 * @param customQuery
	 * @param extension
	 *            query extension e.g., .bltl
	 * @return
	 */

	private String saveQuery2File(String customQuery, String extension) {
		// e.g. models/translated/plasma/NaCL.bltl
		String outputFilePath = targetMC.getOutputDir(input) + File.separator + this.input.getFileName() + extension;
		Utils.write2File(outputFilePath, customQuery, false);
		return outputFilePath;
	}
}
