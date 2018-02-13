/**
 * Query Patterns
 */
package mchecking.enums;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public enum Pattern {
	WITH_PROBABILITY("WithProb"), // e.g. P<>k
	WHAT_PROBABILITY("WhatProb"), // QuestionMark e.g. P=?
	NOT,
	EVENTUALLY,
	ALWAYS,
	NEXT,
	NEVER,
	INFINITELY_OFTEN,
	STEADY_STATE,
	UNTIL,
	WEAK_UNTIL,
	RELEASE,
	FOLLOWS,
	PRECEDES;
	String description = "";

	Pattern() {
	}

	Pattern(String str) {
		this.description = str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// if pattern description has not been given then call the parent to string method.
		return this.description.isEmpty() ? super.toString() : this.description;
	}
}
// TODO ex pattern map mc, to be removed
// notPattern should stay at the end.
// public enum Pattern {
// WITH_PROBABILITY, // e.g. P<>k
// WHAT_PROBABILITY, // QuestionMark e.g. P=?
// // TODO the MCTypes should be removed
// EVENTUALLY(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// ALWAYS(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// NEXT(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.MRMC, MCTypes.MC2 }),
// NEVER(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// INFINITELY_OFTEN(new MCTypes[] { MCTypes.PLASMA, MCTypes.MC2 }),
// STEADY_STATE(new MCTypes[] { MCTypes.PLASMA, MCTypes.MRMC, MCTypes.MC2 }),
// UNTIL(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// WEAK_UNTIL(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// RELEASE(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// FOLLOWS(new MCTypes[] { MCTypes.PLASMA, MCTypes.MRMC, MCTypes.MC2 }),
// PRECEDES(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 }),
// NOT(new MCTypes[] { MCTypes.PRISM, MCTypes.PLASMA, MCTypes.YMER, MCTypes.MRMC, MCTypes.MC2 });
//
// Pattern() {
//
// }
//
// //
// Pattern(MCTypes[] mcTypes) {
// this.matchedMCs = new ArrayList<MCTypes>(Arrays.asList(mcTypes));
// }
//
// // The list of Model Checkers which can run this pattern
// List<MCTypes> matchedMCs = new ArrayList<>();
//
// /**
// * @return the matchedMCs that is the list of Model Checkers which can run this pattern
// */
// public List<MCTypes> getMatchedMCs() {
// return this.matchedMCs;
// }
// }
