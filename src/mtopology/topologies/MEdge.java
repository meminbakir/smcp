/**
 * 
 */
package mtopology.topologies;

/**
 * Custom directed multi edges. Does not have weight.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class MEdge {
	String source = "";
	String target = "";

	/**
	 * @param v1
	 * @param v2
	 */
	public MEdge(String source, String target) {
		this.source = source;
		this.target = target;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + source + " -> " + target + ")";
	}
}