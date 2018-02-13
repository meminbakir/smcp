/**
 * 
 */
package mtopology.topologies;

import org.jgrapht.graph.DefaultEdge;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class JGTEdge extends DefaultEdge {
	private static final long serialVersionUID = 1L;

	@Override
	public String getSource() {
		return super.getSource().toString();
	}
	@Override
	public String getTarget() {
		return super.getTarget().toString();
	}


	@Override
	public String toString() {
		return "(" + getSource() + " -> " + getTarget() + ")";
	}
}