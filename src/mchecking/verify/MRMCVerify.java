/**
 * 
 */
package mchecking.verify;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mce.util.Utils;
import mchecking.ModelChecker;
import mchecking.translator.qt.MCQuery;
import output.Output;

/**
 * TODO THIS CLASS SHOULD BE TESTED IN WINDOWS
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class MRMCVerify extends Verify {

	static final Logger log = LoggerFactory.getLogger(MRMCVerify.class);

	public MRMCVerify(Inputs input, ModelChecker targetMC) {
		super(input, targetMC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.verify.IVerify#verify(java.lang.String, mchecking.translator.qt.MCQuery)
	 */
	@Override
	public Output verify(String mcModelFilePath, MCQuery mcQuery) throws IOException, InterruptedException {
		this.output = new Output(mcQuery);
		this.output.setVerificationResultFPath(this.input);
		this.output.startTime = Utils.getDateandTime(true, true, null);
		// Translate models from prism to MRMC format.
		// prism NaCl.prism -exportmrmc -exportlabels NaCl.lab -exporttrans NaCl.tra
		String mrmcInputFile = this.targetMC.getOutPutDir() + File.separator + this.input.getFileName();

		// try to generate .tar and label files with external tool, if succeeded
		// than call MRMC to run
		if (this.exportLabelandTar(mcModelFilePath, mrmcInputFile)) {
			// file are produced run MRMC mrmc ctmc NaCl.lab NaCl.tra
			// runMRMCInteractively(mcQuery, mrmcInputFile);
			this.runMRMCWithScript(mcQuery, mrmcInputFile);
		}
		return this.output;
	}

	/**
	 * Exports label and transition matrices from a prism model by using prism tool
	 * 
	 * @param mcModelFilePath
	 * @param mrmcInputFile
	 * @return true if external tool run is successful
	 * @throws IOException
	 * @throws InterruptedException
	 */

	private boolean exportLabelandTar(String mcModelFilePath, String mrmcInputFile)
			throws IOException, InterruptedException {
		boolean couldGenerate = true;
		this.output.hasExtTool = true;

		String customPrismPath = this.targetMC.getExternalTool().getExternalToolPath();
		// prism file.prism file.props -exportmrmc -exportlabels file.lab -exporttrans file.tra
		String[] prismCommand = { customPrismPath, mcModelFilePath, mcModelFilePath.replace(".prism", ".props"),
				"-exportmrmc", "-exportlabels", mrmcInputFile + ".lab", "-exporttrans", mrmcInputFile + ".tra" };
		log.info("PRISM tries to export .lab and .tra files");

		Utils.runExternalToolCommand(prismCommand, this.output);
		// MAKE SURE THE TOOL PROCESS ENDED
		Utils.makeSureExternalProcessHasEnded("prism");
		if (this.output.isError) {
			String error = "Error occured while PRISM tried to export .lab and .tra files for MRMC verification\n";
			this.output.error = error + this.output.error;
			log.error(this.output.error);
			couldGenerate = false;
		} else {
			// log the prism result
			log.info(this.output.extResult);
			log.info("PRISM finished exports .lab and .tra files");
			couldGenerate = true;
		}
		return couldGenerate;
	}

	/**
	 * @param mcQuery
	 * @param mrmcInputFile
	 * @param prismTime
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	private void runMRMCInteractively(MCQuery mcQuery, String mrmcInputFile) throws IOException, InterruptedException {
		ProcessBuilder pb;
		Process process;
		String[] mrmcCommand = { this.targetMC.getAppPath(), "ctmc", mrmcInputFile + ".lab", mrmcInputFile + ".tra" };

		OutputStream stdin = null;
		// launch EXE and grab stdin/stdout and stderr
		pb = new ProcessBuilder(mrmcCommand);
		long mrmcStart = System.nanoTime();
		process = pb.start();
		stdin = process.getOutputStream();
		// set simulation on
		this.executeInteractiveParameter(stdin, "set simulation on");
		this.executeInteractiveParameter(stdin, "set min_sample_size " + this.input.getSimSamples());
		this.executeInteractiveParameter(stdin, "set max_sample_size " + this.input.getSimSamples());
		this.executeInteractiveParameter(stdin, "set min_sim_depth " + this.input.getSimDepth());
		this.executeInteractiveParameter(stdin, "set max_sim_depth " + this.input.getSimDepth());
		this.executeInteractiveParameter(stdin, "set sim_method_steady " + "pure");
		this.executeInteractiveParameter(stdin, mcQuery.getTranslatedQuery());
		// executeInteractiveParameter(stdin, "print");
		this.executeInteractiveParameter(stdin, "quit");
		stdin.close();

		// Utils.getExecutionOutput(process, output);
		process.waitFor();

		long mrmcTime = System.nanoTime() - mrmcStart;
		this.output.verificationTime = mrmcTime;
	}

	/**
	 * @param stdin
	 * @throws IOException
	 */
	private void executeInteractiveParameter(OutputStream stdin, String parameter) throws IOException {
		parameter += "\n";
		stdin.write(parameter.getBytes());
		stdin.flush();
	}

	// TODO if eventually either this or interactive method is fine, then one of
	// them can be removed
	private void runMRMCWithScript(MCQuery mcQuery, String mrmcInputFile) throws IOException, InterruptedException {
		// Script can't create directories, so first create dirs if not exist
		File verDirPath = new File(this.output.getVerificationdirectoryPath());
		if (!verDirPath.exists())
			verDirPath.mkdir();

		String interactiveCommands = "(\n" + "echo set simulation on\n" + "echo set min_sample_size "
				+ this.input.getSimSamples() + "\n" + "echo set max_sample_size " + this.input.getSimSamples() + "\n"
				+ "echo set min_sim_depth " + this.input.getSimDepth() + "\n" + "echo set max_sim_depth "
				+ this.input.getSimDepth() + "\n" + "echo set sim_method_steady " + "pure" + "\n" + "echo \""
				+ mcQuery.getTranslatedQuery() + "\"\n" + "echo quit\n) | ";

		String mrmcCommand = interactiveCommands + this.targetMC.getAppPath() + " ctmc " + mrmcInputFile + ".lab "
				+ mrmcInputFile + ".tra" + " >" + this.output.getVerificationResultFPath() + " 2>&1\n";

		String mcCommand[] = { "/bin/sh", "-c", mrmcCommand };
		// String command = "/bin/sh -c "+ mrmcCommand;
		// Utils.runCommand(command, output);
		Utils.runCommand(mcCommand, this.output);
		// if exception has not been thrown the following code will be run
		// which means it does not has error
		this.output.verResult += "Output produced to: " + this.output.getVerificationResultFPath() + "\n";

		// MAKE SURE THE TOOL PROCESS ENDED
		Utils.makeSureExternalProcessHasEnded("mrmc");

	}

}
