/**
 * 
 */
package mtopology.topologies;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.Reaction;
//import org.sbml.libsbml.Species;
//import org.sbml.libsbml.SpeciesReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mtopology.PatternProps;
import mtopology.enums.NonGraphProperties;
import mtopology.enums.Topologies;

/**
 * This class includes custom topological properties of graph which can not be presented by only one graph, so this
 * custom class generated. It calculates number of species, number of reactions, and values related to the number of
 * species updates in each reaction. We calculated updates with and without the stoichiometry of species. Updates are
 * the number of species changed in each reaction. We respect the Stoichiometry of the reaction. e.g., C + 2O -> CO_{2},
 * updates value is 4 namely one C, two O and one CO_{2}.<br>
 * P.S. Stoichiometry is a section of chemistry that involves using relationships between reactants and/or products in a
 * chemical reaction to determine desired quantitative data. By considering stoichiometry we expect to get more
 * quantitative data.
 * 
 * @see http://chemwiki.ucdavis.edu/Analytical_Chemistry/Chemical_Reactions/ Stoichiometry_and_Balancing_Reactions
 * @author Mehmet Emin BAKIR
 *
 */
public class NonGraph implements ITopology {
	private static final Logger log = LoggerFactory.getLogger(NonGraph.class);
	Model sbmlModel = null;
	private PatternProps patternProps = null;

	long numOfReactions = 0;
	double updatesMIN = Double.MAX_VALUE, updatesMEAN = 0.0, updatesMAX = Double.MIN_VALUE, updatesSUM = 0.0;
	double stoichiometricUpdatesMIN = Double.MAX_VALUE, stoichiometricUpdatesMEAN = 0.0,
			stoichiometricUpdatesMAX = Double.MIN_VALUE, stoichiometricUpdatesSUM = 0.0;

	public NonGraph(Model sbmlModel) {
		this.sbmlModel = sbmlModel;
	}

	@Override
	public PatternProps calculateProps() {
		log.debug("Calculating topological properties of NONGRAPH");

		NonGraphProperties thisProp = NonGraphProperties.NumOfSpecies;
		if (getPatternProps().isEnabled(thisProp)) {
			numOfSpecies(thisProp);
		}
		thisProp = NonGraphProperties.NumOfReactions;
		// if (getPatternProps().isEnabled(thisProp)) {
		// Regardless of whether NumOfReactions property is enabled, we need to
		// calculate this property to be used by others.
		numOfReactions(thisProp);
		// }
		// Calculate Constant and NONConstant species
		if (getPatternProps().isEnabled(NonGraphProperties.NumOfConstantSpecies)
				|| getPatternProps().isEnabled(NonGraphProperties.NumOfNONConstantSpecies)) {
			numOfConstantandNONConstantSpecies();
		}

		thisProp = NonGraphProperties.Species_X_Reactions;
		if (getPatternProps().isEnabled(thisProp)) {
			numOfSpecies_X_Reactions(thisProp);
		}

		calculateUpdates();

		// The prefix to be added before the name of property, e.g.
		// species_pesudo_directed_graph...
		String propertyPrefix = this.getGraphType() + "_";
		this.patternProps.setPrefix(propertyPrefix);

		return patternProps;

	}

	/**
	 * Calculates number of Constant species, which either BoundaryCondition or Constant attributes set true
	 * 
	 * @param thisProp
	 */
	private void numOfConstantandNONConstantSpecies() {
		long start = System.nanoTime();
		int numOfConstantSpecies = 0;
		int numOfNONConstantSpecies = 0;
		for (int i = 0; i < sbmlModel.getNumSpecies(); i++) {
			Species species = sbmlModel.getSpecies(i);
			if (species.getConstant() || species.getBoundaryCondition()) {
				numOfConstantSpecies++;
			} else
				numOfNONConstantSpecies++;
		}
		long elapsedTime = System.nanoTime() - start;
		NonGraphProperties thisProp = NonGraphProperties.NumOfConstantSpecies;
		if (getPatternProps().isEnabled(thisProp))
			patternProps.setTimeAndValue(thisProp, elapsedTime, String.valueOf(numOfConstantSpecies));

		thisProp = NonGraphProperties.NumOfNONConstantSpecies;
		if (getPatternProps().isEnabled(thisProp))
			patternProps.setTimeAndValue(thisProp, elapsedTime, String.valueOf(numOfNONConstantSpecies));

	}

	/***
	 * Number of species multiplied by number of reactions
	 * 
	 * @param thisProp
	 */
	private void numOfSpecies_X_Reactions(NonGraphProperties thisProp) {
		long start = System.nanoTime();
		Long speciesXReactions = numOfReactions * (sbmlModel.getNumSpecies());
		String value = String.valueOf(speciesXReactions);
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);
	}

	/**
	 * @param thisProp
	 */
	private void numOfSpecies(NonGraphProperties thisProp) {
		long start = System.nanoTime();
		String value = String.valueOf(sbmlModel.getNumSpecies());
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);
	}

	/**
	 * Calculates number of reactions, including reversible ones.
	 * 
	 * @param thisProp
	 */
	private void numOfReactions(NonGraphProperties thisProp) {
		long start = System.nanoTime();
		// we cannot use sbmlModel.getNumReactions() directly if there are
		// bidirectional reactions exists.
		for (int i = 0; i < sbmlModel.getNumReactions(); i++) {
			numOfReactions++;
			if (sbmlModel.getReaction(i).getReversible()) {
				numOfReactions++;
			}
		}
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(numOfReactions);
			long elapsedTime = System.nanoTime() - start;
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
	}

	/**
	 * 
	 */
	private void calculateUpdates() {
		long start = System.nanoTime();
		Reaction reaction = null;
		// First get the species which involves in reaction
		for (int i = 0; i < sbmlModel.getNumReactions(); i++) {
			reaction = sbmlModel.getReaction(i);
			// if reaction is bidirectional then separate each direction
			String direction = "forward";
			getUpdates(reaction, direction);
			if (reaction.getReversible()) {
				direction = "backward";
				getUpdates(reaction, direction);
			}
		}
		updatesMEAN = updatesSUM / numOfReactions;
		stoichiometricUpdatesMEAN = stoichiometricUpdatesSUM / numOfReactions;

		long elapsedTime = System.nanoTime() - start;
		NonGraphProperties thisProp = NonGraphProperties.UpdatesMIN;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(updatesMIN);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
		thisProp = NonGraphProperties.UpdatesMEAN;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(updatesMEAN);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
		thisProp = NonGraphProperties.UpdatesMAX;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(updatesMAX);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
		thisProp = NonGraphProperties.UpdatesSUM;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(updatesSUM);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}

		thisProp = NonGraphProperties.StoichiometricUpdatesMIN;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(stoichiometricUpdatesMIN);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
		thisProp = NonGraphProperties.StoichiometricUpdatesMEAN;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(stoichiometricUpdatesMEAN);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
		thisProp = NonGraphProperties.StoichiometricUpdatesMAX;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(stoichiometricUpdatesMAX);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
		thisProp = NonGraphProperties.StoichiometricUpdatesSUM;
		if (getPatternProps().isEnabled(thisProp)) {
			String value = String.valueOf(stoichiometricUpdatesSUM);
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}

	}

	/**
	 * Compose Vertices and Edges.
	 * 
	 * @param reaction
	 * @param direction
	 */
	private void getUpdates(Reaction reaction, String direction) {
		// keeps the number of species need to be updated, if this reaction
		// fires
		long reactionUpdates = 0;
		long reactionStoichiometricUpdates = 0;
		// For updates the directions are not important, but we used them here
		// just in case
		if (direction.equals("forward")) {
			reactionUpdates = reaction.getNumReactants() + reaction.getNumProducts();
			for (int i = 0; i < reaction.getNumReactants(); i++) {
				SpeciesReference sr = reaction.getReactant(i);
				reactionStoichiometricUpdates += sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;
			}
			for (int j = 0; j < reaction.getNumProducts(); j++) {
				SpeciesReference sr = reaction.getProduct(j);
				reactionStoichiometricUpdates += sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;
			}
		} else if (direction.equals("backward")) {
			reactionUpdates = reaction.getNumProducts() + reaction.getNumReactants();
			for (int i = 0; i < reaction.getNumProducts(); i++) {
				SpeciesReference sr = reaction.getProduct(i);
				reactionStoichiometricUpdates += sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;

			}
			for (int j = 0; j < reaction.getNumReactants(); j++) {
				SpeciesReference sr = reaction.getReactant(j);
				reactionStoichiometricUpdates += sr.isSetStoichiometry() ? sr.getStoichiometry() : 1;
			}
		}
		updatesMIN = Math.min(updatesMIN, reactionUpdates);
		updatesMAX = Math.max(updatesMAX, reactionUpdates);
		updatesSUM += reactionUpdates;
		stoichiometricUpdatesMIN = Math.min(stoichiometricUpdatesMIN, reactionStoichiometricUpdates);
		stoichiometricUpdatesMAX = Math.max(stoichiometricUpdatesMAX, reactionStoichiometricUpdates);
		stoichiometricUpdatesSUM += reactionStoichiometricUpdates;
	}

	/**
	 * Return patternProps with the enabled default Non Graph Properties
	 * 
	 * @return the patternProps
	 */
	@Override
	public PatternProps getPatternProps() {
		// if pattern props externally are not defined and set, then return a
		// pattern with default values
		if (this.patternProps == null) {
			this.patternProps = new PatternProps();
			this.patternProps.loadAllNonGraphProperties();// Initialize all
															// default graph
															// properties
		}
		return this.patternProps;
	}

	/**
	 * @param patternProps
	 *            the patternProps to set
	 */
	@Override
	public void setPatternProps(PatternProps patternProps) {
		this.patternProps = patternProps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtopology.dmgraph.ITopology#getGraphType()
	 */
	@Override
	public Topologies getGraphType() {
		return Topologies.NON_GRAPH;
	}
}
