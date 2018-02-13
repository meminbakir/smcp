/**
 * 
 */
package mtopology;

import java.util.ArrayList;
import java.util.List;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mchecking.translator.qt.PQuery;
import mtopology.enums.Topologies;
import mtopology.topologies.ITopology;
import mtopology.topologies.NonGraph;
import mtopology.topologies.ReactionGraph;
import mtopology.topologies.SpeciesGraph;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class TopologyManager {
	private static final Logger log = LoggerFactory.getLogger(TopologyManager.class);

	public List<PatternProps> getTopologicalProperties(SBMLDocument sbmlDocument, PQuery pQuery) {
		Model sbmlModel = sbmlDocument.getModel();
		return generateTopologicalProperties(sbmlModel, pQuery);
	}

	/**
	 * Generate Directed Pseudo and Non Graph properties only.
	 * 
	 * @param sbmlModel
	 * @return
	 */
	private List<PatternProps> generateTopologicalProperties(Model sbmlModel, PQuery pQuery) {
		List<PatternProps> patternPropsList = new ArrayList<PatternProps>();
		PatternProps patternProps = null;
		ITopology topology = null;

		/**
		 * The Following DP, Undirected DP and NG properties only will be enabled
		 */
		String[] DP = { "Vertices", "Edges", "InDegreeMIN", "InDegreeMEAN", "InDegreeMAX", "InDegreeSUM",
				"OutDegreeMIN", "OutDegreeMEAN", "OutDegreeMAX", "OutDegreeSUM", "TotalDegreeMIN", "TotalDegreeMEAN",
				"TotalDegreeMAX", "TotalDegreeSUM", "Density" }; // , "Reciprocity", "WeakComponents",
		// String[] UDP = { "UNDIRECTEDBiconnectedComponents", "UNDIRECTEDArticulationPoints" };

		// Non Graph Properties
		String[] NG = { "NumOfNONConstantSpecies", "Species_X_Reactions", "UpdatesMIN", "UpdatesMEAN", "UpdatesMAX",
				"UpdatesSUM" }; // "NumOfConstantSpecies",

		// Species Pseudo Directed Graph
		topology = new SpeciesGraph(sbmlModel, pQuery, Topologies.DIRECTED_PSEUDO);
		patternProps = new PatternProps(DP);
		topology.setPatternProps(patternProps);
		// calculate the properties
		patternProps = topology.calculateProps();
		patternPropsList.add(patternProps);

		// Reaction Pseudo Directed Graph
		topology = new ReactionGraph(sbmlModel, pQuery, Topologies.DIRECTED_PSEUDO);
		patternProps = new PatternProps(DP);
		topology.setPatternProps(patternProps);
		// calculate its properties
		patternProps = topology.calculateProps();
		patternPropsList.add(patternProps);

		topology = new NonGraph(sbmlModel);
		patternProps = new PatternProps();
		patternProps.loadAllNonGraphProperties();
		patternProps.disableAllProps();
		patternProps.enableByName(NG);
		// calculate its properties
		topology.setPatternProps(patternProps);
		patternProps = topology.calculateProps();
		patternPropsList.add(patternProps);

		return patternPropsList;
	}
}
