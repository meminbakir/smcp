
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
public class PRISMVerify extends Verify {

	static final Logger log = LoggerFactory.getLogger(PRISMVerify.class);

	public PRISMVerify(Inputs input, ModelChecker targetMC) {
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
		// C:\prism\customPrism\bin\customPrism.bat C:\Users\Mehmet\OneDrive\git\mce\mce\models\translated\prism\NaCl5.prism
		// -pf P >0.5 [!(F !response>0)] -sim -simmethod aci -simsamples 3 -simpathlen 100
		String[] mcCommand = { this.targetMC.getAppPath(), mcModelFilePath, "-pf", mcQuery.getTranslatedQuery(), "-sim",
				"-simmethod", "ci", "-simsamples", this.input.getSimSamples(), "-simpathlen", this.input.getSimDepth() };
		Utils.runCommand(mcCommand, this.output);
		return this.output;
	}

}
