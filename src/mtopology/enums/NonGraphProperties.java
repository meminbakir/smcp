/**
 * 
 */
package mtopology.enums;

/**
 * The properties which can not be represented, calculated, by one graph.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public enum NonGraphProperties implements IProperties {
	NumOfSpecies, NumOfReactions, NumOfConstantSpecies, NumOfNONConstantSpecies, Species_X_Reactions,

	/**
	 * Updates are the number of species changed in each reaction. e.g., C + 2O
	 * -> CO_{2}, updates value is 3 because the value of three species are
	 * changing, namely one C, one O and one CO_{2}
	 */
	UpdatesMIN, UpdatesMEAN, UpdatesMAX, UpdatesSUM,
	/**
	 * Updates are the number of species changed in each reaction. We respect
	 * the Stoichiometry of the reaction. e.g., C + 2O -> CO_{2}, updates value
	 * is 4 namely one C, two O and one CO_{2}.
	 * 
	 * P.S. Stoichiometry is a section of chemistry that involves using
	 * relationships between reactants and/or products in a chemical reaction to
	 * determine desired quantitative data. By considering stoichiometry we
	 * expect to get more quantitative data.
	 * 
	 * @see http://chemwiki.ucdavis.edu/Analytical_Chemistry/Chemical_Reactions/
	 *      Stoichiometry_and_Balancing_Reactions
	 */
	StoichiometricUpdatesMIN, StoichiometricUpdatesMEAN, StoichiometricUpdatesMAX, StoichiometricUpdatesSUM,
	/**
	 * How deep is the queried species in the reaction list. Includes its own
	 * reaction.
	 */
	QueriedSpeciesReactionDepth,
	/**
	 * How many updates required to come across to the queried species. It
	 * includes the updates required its own reaction.
	 */
	QueriedSpeciesUpdatesDepth

}
