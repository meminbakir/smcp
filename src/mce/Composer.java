package mce;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

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

	public Output compose(Inputs input, SBMLDocument sbml, PQuery pQuery) {

		List<PatternProps> patternPropsList = null;

		Utils.out("Extracting topological features...");

		patternPropsList = new TopologyManager().getTopologicalProperties(sbml, pQuery);
		Utils.out("Topological features calculation finished.");
		Output output = new Output();

		// Run all model checkers, for test purpose.
		 output = testAllMCheckers(input, sbml, pQuery, patternPropsList);
//		output = testOneModelChecker(input, sbml, pQuery, patternPropsList);

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
		// TODO 04.02.2018 Uncommented 2 lines
		System.out.println("Model" + input.getFileName() + " predicted MC " + targetMC.getType());
		Output output = new MCheck().modelCheck(input, sbml, targetMC, pQuery);
		return output;
		// TODO 04.02.2018 commented
		// return null;
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
