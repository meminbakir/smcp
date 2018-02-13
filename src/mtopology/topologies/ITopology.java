/**
 * 
 */
package mtopology.topologies;

import mtopology.PatternProps;
import mtopology.enums.Topologies;

/**
 * The interface for graph and non-graph topological properties.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public interface ITopology {
	public PatternProps calculateProps();
	public Topologies getGraphType();

	public PatternProps getPatternProps();
	public void setPatternProps(PatternProps patternProps);
}
