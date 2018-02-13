/**
 * 
 */
package mtopology.topologies;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class JUNGEdge {
	String source, target;

	public JUNGEdge(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String toString() { // Always good for debugging
		return "(" + source + " -> " + target + ")";
	}

}
