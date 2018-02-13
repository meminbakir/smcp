/**
 * 
 */
package mchecking.verify;

import java.io.IOException;

import mce.Inputs;
import mce.util.Utils;
import mchecking.ModelChecker;
import mchecking.translator.qt.MCQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import output.Output;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class YMERVerify extends Verify {

	static final Logger log = LoggerFactory.getLogger(YMERVerify.class);

	public YMERVerify(Inputs input, ModelChecker targetMC) {
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
		this.output.startTime = Utils.getDateandTime(true, true, null);
		// ymer NaCl.sm --fixed-sample-size 5 --max-path-length 10000 <(echo 'P>=1 [ false U<=1 (na_plus>999) ]')
		String query = "<(echo '" + mcQuery.getTranslatedQuery() + "'\n)";// query needs passes with echo command
		String command = Utils.startInLowerPriority + this.targetMC.getAppPath() + " " + mcModelFilePath + " "
				+ "--fixed-sample-size" + " "
				+ this.input.getSimSamples() + " " + "--max-path-length" + " " + this.input.getSimDepth() + " " + query;
		String mcCommand[] = { "/bin/bash", "-c", command };
		Utils.runCommand(mcCommand, this.output);
		return this.output;
	}

}
