package mchecking;

import org.sbml.jsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mchecking.enums.MCTypes;
import mchecking.toolprops.MCPropsLoader;
import mchecking.translator.Translator;
import mchecking.translator.qt.MCQuery;
import mchecking.translator.qt.PQuery;
import mchecking.verify.VerifyManager;
import output.Output;

public class MCheck {
	private static final Logger log = LoggerFactory.getLogger(MCheck.class);

	public Output modelCheck(Inputs input, SBMLDocument sbml, ModelChecker targetMC, PQuery pQuery) {
		// 1. Translate the query
		Translator translator = new Translator();
		MCQuery mcQuery = translator.translatePQuery(input, pQuery, targetMC);

		Output output = new Output(mcQuery);

		// if model checker supports the pattern, not a comment
		if (mcQuery.isVerifiable()) {
			String mcModelPath = null;

			// 2. Translate model
			if (targetMC.getType() == MCTypes.MRMC) {
				// MRMC requires properties to be translated to labels
				mcModelPath = translator.translateMC(input, sbml, targetMC, mcQuery);

			} else {
				mcModelPath = translator.translateMC(input, sbml, targetMC);
			}

			// 3. Verify model
			if (mcModelPath != null) {
				output = new VerifyManager().verify(input, mcModelPath, targetMC, mcQuery);
			} else {
				output.isError = true;
				output.isVerified = false;
				// output.error = "Model translation failed. See the log file.";
			}

		} else {
			output.result += "Query ID:" + mcQuery.getQueryID() + " " + mcQuery.getTranslatedQuery();
			log.info(output.result);
		}
		return output;
	}

}
