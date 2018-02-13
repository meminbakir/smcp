package mtopology.topologies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.sbml.jsbml.Model;

//import org.sbml.libsbml.Model;

import mce.util.StringUtils;
import mchecking.translator.qt.PQuery;
import mtopology.PatternProps;
import mtopology.enums.GraphP;
import mtopology.enums.Topologies;

/**
 * Generates a custom directed multi-graph (no weight) from SBML model. The graph may have loop and parallel edges.The
 * graph is consumed by other graph libraries, i.e., jgrapht, jung
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public abstract class MGraph implements ITopology {
	private String name;// graph name
	Topologies topologies = null;
	Set<String> vertices = new LinkedHashSet<String>();
	List<MEdge> mEdges = new ArrayList<MEdge>();
	PatternProps patternProps = null;
	/**
	 * We will use the PQuery species to calculate the graph properties related to the queried species
	 */
	// First vertex of graph
	String startingVertex = null;
	PQuery pQuery = null;
	// Queried Vertices are the species queried in PQuery
	Set<String> queriedVertices = new HashSet<>();

	/**
	 * @param string
	 * @param graphType2
	 */
	public MGraph(String name, PQuery pQuery, Topologies topologies) {
		this.name = name;
		this.topologies = topologies;
		this.pQuery = pQuery;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	abstract public void initiliazeGraph(Model sbmlModel);

	@Override
	abstract public PatternProps calculateProps();

	/**
	 * @return the vertices
	 */
	public Set<String> getVertices() {
		return vertices;
	}

	/**
	 * @return the edges
	 */
	public List<MEdge> getEdges() {
		return mEdges;
	}

	/**
	 * Adds new vertex, it does not allow duplication.
	 * 
	 * @param name
	 */
	public void addVertex(String name) {
		// if it is first vertex, then it is the starting vertex
		if (StringUtils.isNullOrEmpty(startingVertex))
			startingVertex = name;
		vertices.add(name);
	}

	/**
	 * Adds a new edge, I does not allow duplication or loops for simple graphs, and allows for pseudo graphs. It also
	 * considers the reverse directions. <br>
	 * If graph is simple undirected, it does not allow loops, parallels. Reverse directions should not be added as new
	 * edge. <br>
	 * If graph is simple and directed it does not allow loops and parallels. Indeed, the reverse directions can be
	 * added. <br>
	 * If graph is pseudo undirected, loops, parallels allowed and reverse directions.<br>
	 * if graph is pseudo directed loops, parallels allowed and reverse directions.<br>
	 * 
	 * @param v1
	 *            source Vertex
	 * @param v2
	 *            target Vertex
	 * @return if added successfully
	 */
	public boolean addEdge(String v1, String v2) {
		boolean addEdge = false;
		// If graph is simple undirected, it does not allow loops, parallels.
		// Reverse directions should not be added as new
		// edge.
		if (topologies.equals(Topologies.SIMPLE_UNDIRECTED)) {
			// no loops
			if (v1.equals(v2))
				addEdge = false;
			// edge should has not been added in either directions
			else if (hasEdge(v1, v2) || hasEdge(v2, v1))
				addEdge = false;
			// it is unique, then add the edge
			else
				addEdge = true;
		}
		// If graph is simple and directed it does not allow loops and
		// parallels. Indeed, the reverse directions can be
		// added.
		else if (topologies.equals(Topologies.SIMPLE_DIRECTED)) {
			// no loops
			if (v1.equals(v2))
				addEdge = false;
			// the same edge should has not been added, the reverse direction
			// can be added.
			else if (hasEdge(v1, v2))
				addEdge = false;
			// it is unique, then add the edge
			else
				addEdge = true;
		}
		// If graph is pseudo undirected, loops, parallels allowed and reverse
		// directions.
		else if (topologies.equals(Topologies.UNDIRECTED_PSEUDO)) {
			addEdge = true;
		}
		// if graph is pseudo directed loops, parallels allowed and reverse
		// directions
		else if (topologies.equals(Topologies.DIRECTED_PSEUDO)) {
			addEdge = true;
		}
		if (addEdge) {
			MEdge mEdge = new MEdge(v1, v2);
			mEdges.add(mEdge);
		}
		return addEdge;
	}

	/**
	 * Returns true if edge already included.
	 * 
	 * @param v1
	 * @param v2
	 * @return true if edge already exist, false otherwise.
	 */
	public boolean hasEdge(String v1, String v2) {
		boolean hasEdge = false;
		for (MEdge mEdge : mEdges) {
			if (mEdge.getSource().equals(v1) && mEdge.getTarget().equals(v2)) {
				hasEdge = true;
				break;
			}
		}
		return hasEdge;
	}

	/**
	 * @return the patternProps
	 */
	@Override
	public PatternProps getPatternProps() {
		// if pattern props externally are not defined and set, then return a
		// pattern with default values
		if (patternProps == null) {
			patternProps = new PatternProps();
			patternProps.loadAllGraphProperties();// Initialize all default
													// graph properties
			if (topologies != null) {
				// if it is undirected graph, then disable directed graph
				// properties
				if (topologies.equals(Topologies.SIMPLE_UNDIRECTED)
						|| topologies.equals(Topologies.UNDIRECTED_PSEUDO)) {
					GraphP[] beDisabled = { GraphP.InDegreeMIN, GraphP.InDegreeMEAN, GraphP.InDegreeMAX,
							GraphP.InDegreeSUM, GraphP.OutDegreeMIN, GraphP.OutDegreeMEAN, GraphP.OutDegreeMAX,
							GraphP.OutDegreeSUM, GraphP.QueriedVerticesInDegreeMIN, GraphP.QueriedVerticesInDegreeMEAN,
							GraphP.QueriedVerticesInDegreeMAX, GraphP.QueriedVerticesInDegreeSUM,
							GraphP.QueriedVerticesOutDegreeMIN, GraphP.QueriedVerticesOutDegreeMEAN,
							GraphP.QueriedVerticesOutDegreeMAX, GraphP.QueriedVerticesOutDegreeSUM,
							GraphP.DIRECTEDStrongComponents };
					patternProps.disableProps(Arrays.asList(beDisabled));
				}
				// if it is directed graph, then disable undirected graph
				// properties
				else if (topologies.equals(Topologies.SIMPLE_DIRECTED)
						|| topologies.equals(Topologies.DIRECTED_PSEUDO)) {
					GraphP[] beDisabled = { GraphP.UNDIRECTEDArticulationPoints, GraphP.UNDIRECTEDBiconnectedComponents,
							GraphP.UNDIRECTEDMinimumCut };
					patternProps.disableProps(Arrays.asList(beDisabled));
				}
			}
		}
		return patternProps;
	}

	/**
	 * @param patternProps
	 *            the patternProps to set
	 */
	@Override
	public void setPatternProps(PatternProps patternProps) {
		this.patternProps = patternProps;
	}

	@Override
	public Topologies getGraphType() {
		return topologies;
	}

	/**
	 * @return the startingVertex
	 */
	public String getStartingVertex() {
		return startingVertex;
	}

	/**
	 * @param startingVertex
	 *            the startingVertex to set
	 */
	public void setStartingVertex(String startingVertex) {
		this.startingVertex = startingVertex;
	}

	public void setPQuery(PQuery pQuery) {
		this.pQuery = pQuery;
	}

	public PQuery getPQuery() {
		return pQuery;
	}

	/**
	 * Set of species which are queried in PQuery
	 * 
	 * @return Set of species name
	 */
	public Set<String> getQueriedVertices() {
		return queriedVertices;
	}

	/**
	 * Adds a vertex to the set of queried vertices, i.e., queriedVertices.
	 */
	public void addQueriedVertices(String queriedSpecies) {
		queriedVertices.add(queriedSpecies);
	}

	@Override
	public String toString() {
		String result = name + " [ " + vertices + "-" + mEdges + "]";
		return result;
	}
}