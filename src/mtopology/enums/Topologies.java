/**
 * 
 */
package mtopology.enums;

/**
 * Topologies which includes graph and non-graph properties
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public enum Topologies {
	/**
	 * Simple Graph, has no loops and no parallel edges
	 */
	SIMPLE_UNDIRECTED("SUD"),
	SIMPLE_DIRECTED("SD"),
	/**
	 * A pseudograph is a non-simple graph in which both graph loops and multiple edges are permitted
	 */
	UNDIRECTED_PSEUDO("PUD"),
	DIRECTED_PSEUDO("PD"),
	/**
	 * The Non-Graph topological properties.
	 */
	NON_GRAPH("NG");

	private String value = "";

	private Topologies(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.value;
	}
}
