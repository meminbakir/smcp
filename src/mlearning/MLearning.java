package mlearning;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mchecking.ModelChecker;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class MLearning {
	private static final Logger log = LoggerFactory.getLogger(MLearning.class);

	public ModelChecker estimateMChecker(List<PatternProps> patternPropsList, PQuery pQuery) {
		log.info("\tStart Prediction.");
		Predictor predictor = new Predictor();
		MCTypes targetMC = predictor.pythonPredict(patternPropsList, pQuery);
		log.info("\tPrediction Ended.");
		// Cross-Checks if the estimated MC can verify the query. But ML has to return the one can verify.
		ModelChecker targetModelChecker = new ModelChecker(targetMC);
		List<MCTypes> supportedMCs = ModelChecker.getSupportedModelCheckers(pQuery);
		if (supportedMCs.contains(targetModelChecker.getType())) {
			log.info("\tThe fastest SMC prediction for " + pQuery.getPatterns().get(1) + " pattern is:"
					+ targetModelChecker.getType());
		} else {
			// If predicted SMC does not support the pattern
			log.error("Problem... The estimated SMC " + targetModelChecker.getType() + " does not support the pattern :"
					+ pQuery.getPQuery()); // Actually, this problem should never happen.
		}
		return targetModelChecker;
	}

}
