package mlearning;

import java.util.List;

import mce.util.Utils;
import mchecking.ModelChecker;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MLearning {
	public ModelChecker estimateMChecker(List<PatternProps> patternPropsList, PQuery pQuery) {
		Utils.out("\tStart Prediction.");
		Predictor predictor = new Predictor();
		MCTypes targetMC = predictor.pythonPredict(patternPropsList, pQuery);
		Utils.out("\tPrediction Ended.");
		// Cross-Checks if the estimated MC can verify the query. But ML has to return the one can verify.
		ModelChecker targetModelChecker = new ModelChecker(targetMC);
		List<MCTypes> supportedMCs = ModelChecker.getSupportedModelCheckers(pQuery);
		if (supportedMCs.contains(targetModelChecker.getType())) {
			Utils.out("\tThe fastest SMC prediction for " + pQuery.getPatterns().get(1) + " pattern is:"
					+ targetModelChecker.getType());
		} else {
			// If predicted SMC does not support the pattern
			Utils.out("Problem... The estimated SMC " + targetModelChecker.getType() + " does not support the pattern :"
					+ pQuery.getPQuery()); // Actually, this problem should never happen.
		}
		return targetModelChecker;
	}

}
