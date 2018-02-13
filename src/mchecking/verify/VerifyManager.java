/**
 * 
 */
package mchecking.verify;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.MCQuery;
import output.Output;

public class VerifyManager {
	private static final Logger log = LoggerFactory.getLogger(VerifyManager.class);

	public Output verify(Inputs input, String mcModelFilePath, ModelChecker targetMC, MCQuery mcQuery) {
		Verify verifier = null;
		Output output = null;
		MCTypes targetMCType = targetMC.getType();
		try {
			if (targetMCType == MCTypes.PRISM) {
				verifier = new PRISMVerify(input, targetMC);
			} else if (targetMCType == MCTypes.PLASMA) {
				verifier = new PLASMAVerify(input, targetMC);
			} else if (targetMCType == MCTypes.YMER) {
				verifier = new YMERVerify(input, targetMC);
			} else if (targetMCType == MCTypes.MRMC) {
				verifier = new MRMCVerify(input, targetMC);
			} else if (targetMCType == MCTypes.MC2) {
				verifier = new MC2Verify(input, targetMC);
			}
			output = verifier.verify(mcModelFilePath, mcQuery);
		} catch (IOException | InterruptedException e) {
			log.error("Exception rised while '" + targetMC.getAppPath() + "' tryied to verify property'" + mcQuery + "\n"
					+ "Make sure your application path is correct (check mcProp.xml), and remove any enter or tab charachters."
					+ e.getMessage());
			e.printStackTrace();
		}

		return output;
	}
}
