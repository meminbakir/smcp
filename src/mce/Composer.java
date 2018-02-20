package mce;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.util.Utils;
import mchecking.MCheck;
import mchecking.ModelChecker;
import mchecking.translator.qt.PQuery;
import mlearning.MLearning;
import mtopology.PatternProps;
import mtopology.TopologyManager;
import output.Output;
import performance.ManageTests;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class Composer {
	private static final Logger log = LoggerFactory.getLogger(Composer.class);

	public Output compose(Inputs input, SBMLDocument sbml, PQuery pQuery) {

		List<PatternProps> patternPropsList = null;

		log.info("Extracting topological features...");

		patternPropsList = new TopologyManager().getTopologicalProperties(sbml, pQuery);
		log.info("Topological features calculation finished.");
		Output output = new Output();

		// Run all model checkers, for test purpose.
		// output = testAllMCheckers(input, sbml, pQuery, patternPropsList);
		output = testOneModelChecker(input, sbml, pQuery, patternPropsList);

		return output;
	}

	/**
	 * 
	 * To Test and Record the performance results It should be uncommented after
	 * performance experiments.
	 * 
	 * @param input
	 * @param sbml
	 * @param pQuery
	 * @param output
	 * @param patternPropsList
	 * @return
	 */
	@SuppressWarnings("unused")
	private Output testAllMCheckers(Inputs input, SBMLDocument sbml, PQuery pQuery,
			List<PatternProps> patternPropsList) {
		Output output = ManageTests.runPerformanceTests(input, sbml, pQuery, patternPropsList);
		return output;
	}

	/**
	 * @param pQuery
	 * @param patternProps
	 * @param output
	 */
	private Output testOneModelChecker(Inputs input, SBMLDocument sbml, PQuery pQuery,
			List<PatternProps> patternPropsList) {
		ModelChecker targetMC = new MLearning().estimateMChecker(patternPropsList, pQuery);
		Output output = null;
		if (input.isVerify()) {
			output = new MCheck().modelCheck(input, sbml, targetMC, pQuery);
		} else {
			output = new Output();
			String message = String.format("The fastest SMC prediction for model: %s and pattern: %s is: %s",
					input.getFileName(), pQuery.getPatterns().get(1), targetMC.getType());
			output.setPredictResult(message);
		}
		return output;
	}

	/**
	 * Write calculated graph properties to a file
	 * 
	 * @param patternProps
	 * @return
	 */
	@SuppressWarnings("unused")
	private String writeAttrs2File(PatternProps patternProps) {
		System.out.println(patternProps);
		// TODO write 2 a file
		return null;
	}
}
