/**
 * 
 */
package mchecking.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import mce.Inputs;
import mce.util.Utils;
import mchecking.ModelChecker;
import mchecking.enums.ExtTTypes;
import mchecking.enums.Pattern;
import mchecking.toolprops.ExternalTool;
import mchecking.translator.qt.MCQuery;
import mchecking.translator.qt.PQuery2MC2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import output.Output;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MC2Verify extends Verify {
	static final Logger log = LoggerFactory.getLogger(MC2Verify.class);

	public MC2Verify(Inputs input, ModelChecker targetMC) {
		super(input, targetMC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.verify.IVerify#verify(mce.Inputs, java.lang.String, mchecking.translator.qt.MCQuery)
	 */
	@Override
	public Output verify(String mcModelFilePath, MCQuery mcQuery) throws IOException, InterruptedException {
		// generate output object which will hold results
		output = new Output(mcQuery);
		output.startTime = Utils.getDateandTime(true, true, null);

		// Name simulator file by replacing the model file extension with '.sims' extension
		String modelExtesion = ".";
		if (targetMC.isHasExternalTool()) {
			modelExtesion += targetMC.getExternalTool().getModelExtension();
		} else {
			modelExtesion += targetMC.getModelExtension();
		}
		String mc2SimsFile = mcModelFilePath.replace(modelExtesion, ".sims");
		// try to generate simulation traces with external tool, if succeeded then call MC2 to run
		if (generateSimTraces4MC2(mcModelFilePath, mc2SimsFile, mcQuery)) {
			// MC2 requires properties be read from a file
			String customQueryFilePath = saveQuery2File(mcQuery.getTranslatedQuery(), ".queries");
			// java -jar /opt/mc2/mc2.jar stoch NaCl.sims NaCl.queries
			String mcCommand = "java -jar " + targetMC.getAppPath() + " stoch " + mc2SimsFile + " " + customQueryFilePath;
			log.info("MC2 starts verification");
			Utils.runCommand(mcCommand, output);
			// MAKE SURE THE TOOL PROCESS ENDED
			Utils.makeSureExternalProcessHasEnded("mc2");
		}
		return output;
	}

	/**
	 * This method will calls custom prism which does not require to remove action column and allows to produces multiple
	 * paths from one call
	 * 
	 * @param mcModelFilePath
	 * @param mc2SimsFile
	 * @param mcQuery
	 * @return true if simulation generation is successful
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean generateSimTraces4MC2(String mcModelFilePath, String mc2SimsFile, MCQuery mcQuery)
			throws IOException, InterruptedException {
		boolean couldGenerate = true;
		output.hasExtTool = true;
		// if previous simulation file exist, then remove it
		if (Files.exists(Paths.get(mc2SimsFile))) {
			Files.delete(Paths.get(mc2SimsFile));
		}
		ExternalTool externalTool = targetMC.getExternalTool();
		// Run the external simulator specified in mcProp.xml file
		log.info(externalTool.getExtTType() + " tries to generate simulation traces for MC2 ...");
		if (externalTool.getExtTType().equals(ExtTTypes.GILLESPIE2)) {
			useGillespie2asSimulator(externalTool, mcModelFilePath, mc2SimsFile, mcQuery);
		} else if (externalTool.getExtTType().equals(ExtTTypes.PRISM)) {
			usePRISMasSimulator(externalTool, mcModelFilePath, mc2SimsFile, mcQuery);
		}

		// if no error, then run mc2
		if (output.isError) {
			String error = externalTool.getExtTType() + " could not generate simulation traces for MC2\n";
			output.error = error + output.error;
			log.error(output.error);
			couldGenerate = false;
		} else {
			log.info(output.extResult);
			log.info(externalTool.getExtTType() + " generated simulation traces for MC2\n");
			couldGenerate = true;
		}
		return couldGenerate;
	}

	/**
	 * @param mcModelFilePath
	 *            sbml file
	 * @param mc2SimsFile
	 *            the path file which simulation will be printed out.
	 * @param mcQuery
	 *            only the species specified in list will be printed to simulation output.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void useGillespie2asSimulator(ExternalTool externalTool, String mcModelFilePath, String mc2SimsFile,
			MCQuery mcQuery) throws IOException, InterruptedException {

		// gillespie2.exe -m EnzymaticReaction.sbml -t(time) 10000 -l(printAllSteps) -c -r(repeat) 1000 (-a OR -p)
		String speciesCommand = "";// '-a' print all species, '-p' print only specified species.

		String printAllSteps = "";
		// if query is NeXt pattern then all steps should be printed out with (-l) flag, it is like PRIMS' changes=false flag
		if (mcQuery.getPQuery().getPatterns().contains(Pattern.NEXT)) {
			printAllSteps = "-l";
		}
		String speciesStr = "";
		List<String> speciesList = ((PQuery2MC2) mcQuery).getSpecies();
		if (speciesList.isEmpty())
			speciesCommand = "-a";// any specific species has not been listed, so print all.
		else
			speciesCommand = "-p";// print only species specified in the query.

		int lastElement = 1;
		for (String species : speciesList) {
			speciesStr += species;
			if (lastElement != speciesList.size())
				speciesStr += ",";
			lastElement++;
		}

		String[] command = { externalTool.getExternalToolPath(), "-m", mcModelFilePath, "-t", input.getSimDepth(),
				printAllSteps, "-c", "-r", input.getSimSamples(), speciesCommand, speciesStr, "-o", mc2SimsFile };
		Utils.runExternalToolCommand(command, output);
		// make sure the tool process ended
		Utils.makeSureExternalProcessHasEnded("gillespie2");
	}

	/**
	 * 
	 * @param externalTool
	 * @param mcModelFilePath
	 * @param mc2SimsFile
	 * @param mcQuery
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void usePRISMasSimulator(ExternalTool externalTool, String mcModelFilePath, String mc2SimsFile, MCQuery mcQuery)
			throws IOException, InterruptedException {
		// String prismCommand = externalToolPath + " " + mcModelFilePath + " -simpath " + input.getSimDepth() +
		// ",changes=false " + mc2SimsFile + " -customSim -customSimSamples " + Integer.parseInt(input.getSimSamples());
		String changes = ",changes=";
		// if query is NeXt pattern then all steps should be printed out with (-l) flag, it is like PRIMS' changes=false flag
		if (mcQuery.getPQuery().getPatterns().contains(Pattern.NEXT)) {
			changes += "false";
		} else {
			changes += "true";
		}
		String[] prismCommand = { externalTool.getExternalToolPath(), mcModelFilePath, "-simpath",
				input.getSimDepth() + changes, mc2SimsFile, "-customSim", "-customSimSamples",
				input.getSimSamples() };
		Utils.runExternalToolCommand(prismCommand, output);
		// make sure the tool process ended
		Utils.makeSureExternalProcessHasEnded("prism");
	}

	/**
	 * Saves a query to file, if needed. This is part of verification process (not output)
	 * 
	 * @param customQuery
	 * @param extension
	 *            query extension e.g., .bltl
	 * @return
	 */
	private String saveQuery2File(String customQuery, String extension) {
		// e.g. models/translated/plasma/NaCL.bltl
//		String outputFilePath = targetMC.getOutPutDir() + File.separator + input.getFileName() + extension;
		String outputFilePath = targetMC.getOutputDir(input) + File.separator + input.getFileName() + extension;
		Utils.write2File(outputFilePath, customQuery, false);
		return outputFilePath;
	}

}
