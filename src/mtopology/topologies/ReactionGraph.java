package mtopology.topologies;

import java.util.ArrayList;
import java.util.Collections;

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
 * Extends multi graph, each vertex corresponds to a reaction so the number of vertices are equal to the number of
 * reactions in the model. If firing of reaction changes the density of reactant of any reactions(namely its propensity,
 * P.S reaction always changes itself propensity), and edge will be placed for each reactant, namely if reactants and
 * products of Ri are at least one reactant of Rj (where i can be equal to j) then there is an edge between Ri and Rj.
 * Note, if the reaction Ri changes more than one reactant of Rj, then for each reactant a direct edge will be added.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class ReactionGraph extends MGraph {
	private static final Logger log = LoggerFactory.getLogger(ReactionGraph.class);
	// Holds each reactions and its reactants and products
	private ArrayList<ReactionBean> reactionBeans = new ArrayList<ReactionBean>();

	/**
	 * @param input
	 * @param sbmlModel
	 */
	public ReactionGraph(Model sbmlModel, PQuery pQuery, Topologies topologies) {
		super("ReactionG", pQuery, topologies);
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
		// Load reactions and their molecules(reactants and products) to list
		loadMoleculeOfReaction(sbmlModel);
		for (ReactionBean sourceReaction : reactionBeans) {
			String source = sourceReaction.reactionID;
			addVertex(source);

			// all molecules effected by current reaction, i.e., reactants and products
			ArrayList<String> molecules = new ArrayList<String>();
			molecules.addAll(sourceReaction.reactants);
			molecules.addAll(sourceReaction.products);
			for (String molecule : molecules) {
				for (ReactionBean targetReaction : reactionBeans) {
					// if target reactants density changes by this reaction
					if (targetReaction.reactants.contains(molecule)) {
						addEdge(source, targetReaction.reactionID);
					}
				}
			}
			// If reaction includes any species queried in PQuery, the we add the reactionID to the queriedVertices set.
			if (!Collections.disjoint(molecules, pQuery.getQueriedSpecies())) {
				addQueriedVertices(source);
			}

		}
	}

	private void loadMoleculeOfReaction(Model sbmlModel) {
		long numReactions = sbmlModel.getNumReactions();
		Reaction reaction = null;
		if (numReactions == 0) {
			// String path = "";
			// SBMLDocument sbml = (SBMLDocument) sbmlModel.getParentSBMLObject();
			// path = sbml.getLocationURI() + sbml.getName();
			// log.warn("No Reactions are defined in " + path);
			log.warn("No Reactions are defined in");
		} else {
			// First get the species which involves in reaction
			for (int n = 0; n < numReactions; n++) {
				reaction = sbmlModel.getReaction(n);

				ReactionBean reactionBean = new ReactionBean();
				reactionBean.reactionID = reaction.getId();

				for (int i = 0; i < reaction.getNumReactants(); i++) {
					String reactant = reaction.getReactant(i).getSpecies();
					reactionBean.reactants.add(reactant);
				}
				for (int i = 0; i < reaction.getNumProducts(); i++) {
					String product = reaction.getProduct(i).getSpecies();
					reactionBean.products.add(product);
				}
				reactionBeans.add(reactionBean);
				// For reverse reactions
				if (reaction.getReversible()) {
					// If reversible then the reverse is second reaction, and
					// products becomes its reactants, and vice versa
					ReactionBean reverseReactionBean = new ReactionBean();
					reverseReactionBean.reactionID = reaction.getId() + "_rev";

					for (int i = 0; i < reaction.getNumProducts(); i++) {
						String product = reaction.getProduct(i).getSpecies();
						reverseReactionBean.reactants.add(product);
					}
					for (int i = 0; i < reaction.getNumReactants(); i++) {
						String reactant = reaction.getReactant(i).getSpecies();
						reverseReactionBean.products.add(reactant);
					}
					reactionBeans.add(reverseReactionBean);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtopology.dmgraph.MGraph#calculateProps()
	 */
	@Override
	public PatternProps calculateProps() {
		// Calculate properties
		patternProps = new CalculateGraphProps(this).calculateProps();
		// The prefix to be added before the name of property, e.g. species_pesudo_directed_graph...
		String propertyPrefix = getName() + "_" + getGraphType() + "_";
		patternProps.setPrefix(propertyPrefix);
		return patternProps;
	}

	class ReactionBean {
		String reactionID = "";
		ArrayList<String> reactants = new ArrayList<String>();
		ArrayList<String> products = new ArrayList<String>();

	}
}
