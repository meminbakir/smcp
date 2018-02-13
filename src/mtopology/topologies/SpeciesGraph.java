package mtopology.topologies;

import java.util.Set;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.Reaction;
//import org.sbml.libsbml.SBMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;
import mtopology.enums.Topologies;

/**
 * Extends multi graph, each vertex corresponds to a unique species, and so the number of vertices in a species
 * dependency graph is equal to the number of species in the model. A directed edge is exists vertex Vi to vertex V_{j}
 * if for any reaction species S_{i} is a reactant and species S_{j} is a product. THERE MIGHT BE MORE THAN ONE EDGES
 * BETWEEN TWO VERTICES, THAT IS LOOP AND PARALLEL EDGES ALLOWED.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class SpeciesGraph extends MGraph {
	private static final Logger log = LoggerFactory.getLogger(SpeciesGraph.class);

	/**
	 * @param input
	 * @param sbmlModel
	 */
	public SpeciesGraph(Model sbmlModel, PQuery pQuery, Topologies topologies) {
		super("SpeciesG", pQuery, topologies);
		initiliazeGraph(sbmlModel);
	}

	/**
	 * Creates vertices and edges from sbml, it only regards species and reactions, it does not take constants
	 * (parameters in SBML) into consideration
	 * 
	 * @param input
	 * @param sbmlModel
	 */
	@Override
	public void initiliazeGraph(Model sbmlModel) {

		long numReactions = sbmlModel.getNumReactions();
		Reaction reaction = null;
		if (numReactions == 0) {
			// String path = "";
			// SBMLDocument sbml = (SBMLDocument) sbmlModel.getParentSBMLObject();
			// path = sbml.getLocationURI() + sbml.getName();
			log.warn("No Reactions are defined in ");
		} else {
			// first get the species which involves in reaction
			for (int n = 0; n < numReactions; n++) {
				reaction = sbmlModel.getReaction(n);
				// if reaction is bidirectional then separate each direction
				String direction = "forward";
				composeVandE(reaction, direction);
				if (reaction.getReversible()) {
					direction = "backward";
					composeVandE(reaction, direction);
				}
			}

			// then, get species may not involves reactions at all, but
			// exists..., i.e., isolated nodes, they can have only in pseudo graph I think, TODO confirm it
			// if (graphType.equals(GraphType.PSEUDO_DIRECTED) || graphType.equals(GraphType.PSEUDO_UNDIRECTED)) {
			for (int n = 0; n < sbmlModel.getNumSpecies(); n++) {
				addVertex(sbmlModel.getSpecies(n).getId());
			}
			// }
		}
	}

	/**
	 * Compose Vertices and Edges.
	 * 
	 * @param reaction
	 * @param direction
	 */
	private void composeVandE(Reaction reaction, String direction) {
		// If any species is in list of the queried species, then we will add them to the queriedVertices set to
		// calculate
		// properties regarding queried species in PQuery.
		Set<String> queriedSpecies = pQuery.getQueriedSpecies();

		if (direction.equals("forward")) {
			for (int i = 0; i < reaction.getNumReactants(); i++) {
				String source = reaction.getReactant(i).getSpecies();
				// add regular vertex
				addVertex(source);
				// if it is a species queried in PQuery, then add it to queriedVertices set
				if (queriedSpecies.contains(source)) {
					addQueriedVertices(source);
				}
				for (int j = 0; j < reaction.getNumProducts(); j++) {
					String target = reaction.getProduct(j).getSpecies();
					// add regular vertex and edge
					addVertex(target);
					addEdge(source, target);
					// if it is a species queried in PQuery, then add it to queriedVertices set
					if (queriedSpecies.contains(target)) {
						addQueriedVertices(target);
					}
				}
			}
		} else if (direction.equals("backward")) {
			for (int i = 0; i < reaction.getNumProducts(); i++) {
				String source = reaction.getProduct(i).getSpecies();
				// add regular vertex
				addVertex(source);
				// if it is a species queried in PQuery, then add it to queriedVertices set
				if (queriedSpecies.contains(source)) {
					addQueriedVertices(source);
				}
				for (int j = 0; j < reaction.getNumReactants(); j++) {
					String target = reaction.getReactant(j).getSpecies();
					// add regular vertex and edge
					addVertex(target);
					addEdge(source, target);
					// if it is a species queried in PQuery, then add it to queriedVertices set
					if (queriedSpecies.contains(target)) {
						addQueriedVertices(target);
					}
				}
			}
		}
	}

	@Override
	public PatternProps calculateProps() {
		// Calculate properties
		patternProps = new CalculateGraphProps(this).calculateProps();
		// The prefix to be added before the name of property, e.g. species_pesudo_directed_graph...
		String propertyPrefix = getName() + "_" + getGraphType() + "_";
		patternProps.setPrefix(propertyPrefix);
		return patternProps;
	}
}