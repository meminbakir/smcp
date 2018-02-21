/**
 * 
 */
package mtopology.topologies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.BiconnectivityInspector;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import mtopology.PatternProps;
import mtopology.enums.GraphP;
import mtopology.enums.IProperties;
import mtopology.enums.Topologies;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class CalculateGraphProps {

	private static final Logger log = LoggerFactory.getLogger(CalculateGraphProps.class);

	// The custom graph(MGraph) variables
	private PatternProps patternProps;

	// Graph can be species or reaction graph
	MGraph graph = null;
	Topologies topologies = null; // which can be Simple or Pseudo graph with
									// undirected and directed combinations

	Graph<String, JGTEdge> jgtGraph = null;
	edu.uci.ics.jung.graph.Graph<String, JUNGEdge> jungGraph = null;

	/**
	 * Widely used graph props
	 */
	int verticesSize = 0;
	int edgeSize = 0;
	Double maxPossibleEdgeSize = 0.0;// Maximum possible edge size ( V*(V-1))
										// for directed graphs
	// For performance issue BiconnectivityInspector declared as class variable
	BiconnectivityInspector<String, JGTEdge> biConIns = null;

	/**
	 * Vertices which are queried inside PQuery
	 */
	String startingVertex = null;
	Set<String> queriedVertices = null;
	int queriedVerticesSize = 0;

	/**
	 * Initialize DirectedPseudograph, and fill vertex and edges with Custom
	 * Graph object, i.e., the custom graph
	 * 
	 * @param customDWGraph
	 */
	public CalculateGraphProps(ITopology graph) {
		this.graph = (MGraph) graph;
		topologies = graph.getGraphType();// Simple or Pseudo graph with
											// undirected and directed
											// combinations
		// Get the properties to be calculated
		patternProps = graph.getPatternProps();
		initializeGraph();
		initializeVariables();
		// Vertices related to PQuery
		startingVertex = this.graph.getStartingVertex();
		queriedVertices = this.graph.getQueriedVertices();
		queriedVerticesSize = this.graph.getQueriedVertices().size();
	}

	/**
	 * Initialize the corresponding JGrapht and JUNG graphs
	 */
	private void initializeGraph() {
		// Initialize the graph types which will do the calculations
		if (topologies.equals(Topologies.SIMPLE_UNDIRECTED)) {
			jgtGraph = new SimpleGraph<String, JGTEdge>(JGTEdge.class);// Jgrapht
			jungGraph = new UndirectedSparseGraph<String, JUNGEdge>();// Jung
		} else if (topologies.equals(Topologies.SIMPLE_DIRECTED)) {
			jgtGraph = new SimpleDirectedGraph<String, JGTEdge>(JGTEdge.class);// Jgrapht
			jungGraph = new DirectedSparseGraph<String, JUNGEdge>();// Jung

		} else if (topologies.equals(Topologies.UNDIRECTED_PSEUDO)) {
			jgtGraph = new Pseudograph<String, JGTEdge>(JGTEdge.class);// Jgrapht
			jungGraph = new UndirectedSparseMultigraph<String, JUNGEdge>();
		} else if (topologies.equals(Topologies.DIRECTED_PSEUDO)) {
			jgtGraph = new DirectedPseudograph<String, JGTEdge>(JGTEdge.class);
			jungGraph = new DirectedSparseMultigraph<String, JUNGEdge>();
		}
		// Add vertices to initialised graph
		for (String vertex : graph.getVertices()) {
			jgtGraph.addVertex(vertex);
			jungGraph.addVertex(vertex);
		}
		// Add edges to the initialised graph
		for (MEdge cEdge : graph.getEdges()) {
			String source = cEdge.getSource();
			String target = cEdge.getTarget();
			JUNGEdge jungCustomDiEdge = new JUNGEdge(source, target);
			jgtGraph.addEdge(source, target);
			jungGraph.addEdge(jungCustomDiEdge, source, target);
		}
	}

	/**
	 * Initialize variables which are used frequently.
	 */
	private void initializeVariables() {
		verticesSize = jgtGraph.vertexSet().size();
		edgeSize = jgtGraph.edgeSet().size();

		// directed graph with no loops, or parallel edges
		if (jgtGraph instanceof SimpleGraph) {
			if (verticesSize == 1) {
				maxPossibleEdgeSize = Double.NaN;
			} else
				maxPossibleEdgeSize = (double) ((verticesSize * (verticesSize - 1)) / 2);
		} else if (jgtGraph instanceof SimpleDirectedGraph) {
			// maxPossibleEdgeSize calculated, based on IGraph implementation.
			if (verticesSize == 1) {
				maxPossibleEdgeSize = Double.NaN;
			} else
				maxPossibleEdgeSize = (double) (verticesSize * (verticesSize - 1));

		} else if (jgtGraph instanceof Pseudograph) {
			// maxPossibleEdgeSize calculated, based on IGraph implementation.
			maxPossibleEdgeSize = (double) ((verticesSize * (verticesSize + 1)) / 2);
		} else if (jgtGraph instanceof DirectedPseudograph) {
			// maxPossibleEdgeSize calculated, based on IGraph implementation.
			maxPossibleEdgeSize = (double) (verticesSize * verticesSize);
		}
	}

	/**
	 * Calculates properties assigned to graph
	 * 
	 * @return
	 */
	public PatternProps calculateProps() {
		log.debug("Calculating topological properties of " + graph.getName() + " " + graph.getGraphType());
		// Generate properties enabled for current pattern
		GraphP thisProp = GraphP.Vertices;
		if (patternProps.isEnabled(thisProp)) {
			verticesNumber(thisProp);
		}
		thisProp = GraphP.Edges;
		if (patternProps.isEnabled(thisProp)) {
			edgeNumber(thisProp);
		}

		calculateDegrees();

		thisProp = GraphP.Density;
		if (patternProps.isEnabled(thisProp)) {
			density(thisProp);
		}

		calculateShortestPathsProps();

		thisProp = GraphP.Reciprocity;
		if (patternProps.isEnabled(thisProp)) {
			reciprocity(thisProp);
		}
		thisProp = GraphP.WeakComponents;
		if (patternProps.isEnabled(thisProp)) {
			weakComponents(thisProp);
		}
		// Uses JUNG Library
		calculateBetweenness();

		thisProp = GraphP.UNDIRECTEDBiconnectedComponents;
		if (patternProps.isEnabled(thisProp)) {
			undirectedBiconnectedComponents(thisProp);
		}
		thisProp = GraphP.UNDIRECTEDArticulationPoints;
		if (patternProps.isEnabled(thisProp)) {
			undirectedArticulationPoints(thisProp);
		}
		thisProp = GraphP.UNDIRECTEDMinimumCut;
		if (patternProps.isEnabled(thisProp)) {
			undirectedMinimumCut(thisProp);
		}

		thisProp = GraphP.DIRECTEDStrongComponents;
		if (patternProps.isEnabled(thisProp)) {
			directedStrongComponents(thisProp);
		}

		return patternProps;

	}

	/**
	 * Calculate vertices number, add the results (value and time) back
	 * to @param patternProps object
	 * 
	 * @param thisProp
	 *            the property currently will be calculated
	 * @return
	 */
	private void verticesNumber(GraphP thisProp) {
		long start = System.nanoTime();
		String value = String.valueOf(verticesSize);
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);

	}

	/**
	 * Calculate edge number, add the results (value and time) back to @param
	 * patternProps object
	 * 
	 * @param thisProp
	 *            the property currently will be calculated
	 * @return
	 */
	private void edgeNumber(GraphP thisProp) {
		long start = System.nanoTime();
		String value = String.valueOf(edgeSize);
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);
	}

	/**
	 * Calculate InDegrees, OutDegrees and Total Degrees of each vertex, and
	 * also sum each group which will for graph-wise For performance, I have
	 * combined all three degree properties, so if any of these properties be
	 * disabled then the separate the deprecated version of these methods should
	 * be used.
	 * 
	 */
	private void calculateDegrees() {
		// For performance, we will calculate all following props in one loop
		boolean inDegreesEnabled = patternProps.areAllEnabled(
				new IProperties[] { GraphP.InDegreeMIN, GraphP.InDegreeMEAN, GraphP.InDegreeMAX, GraphP.InDegreeSUM });
		boolean outDegreesEnabled = patternProps.areAllEnabled(new IProperties[] { GraphP.OutDegreeMIN,
				GraphP.OutDegreeMEAN, GraphP.OutDegreeMAX, GraphP.OutDegreeSUM });
		boolean totalDegreesEnabled = patternProps.areAllEnabled(new IProperties[] { GraphP.TotalDegreeMIN,
				GraphP.TotalDegreeMEAN, GraphP.TotalDegreeMAX, GraphP.TotalDegreeSUM });
		// Similar properties regarding only the species queried in PQuery
		boolean queriedVerticesInDegreeEnabled = patternProps.areAllEnabled(
				new IProperties[] { GraphP.QueriedVerticesInDegreeMIN, GraphP.QueriedVerticesInDegreeMEAN,
						GraphP.QueriedVerticesInDegreeMAX, GraphP.QueriedVerticesInDegreeSUM });

		boolean queriedVerticesOutDegreeEnabled = patternProps.areAllEnabled(
				new IProperties[] { GraphP.QueriedVerticesOutDegreeMIN, GraphP.QueriedVerticesOutDegreeMEAN,
						GraphP.QueriedVerticesOutDegreeMAX, GraphP.QueriedVerticesOutDegreeSUM });

		boolean queriedVerticesTotalDegreeEnabled = patternProps.areAllEnabled(
				new IProperties[] { GraphP.QueriedVerticesTotalDegreeMIN, GraphP.QueriedVerticesTotalDegreeMEAN,
						GraphP.QueriedVerticesTotalDegreeMAX, GraphP.QueriedVerticesTotalDegreeSUM });

		// If any of degree properties requested then do calculation.
		if (inDegreesEnabled || outDegreesEnabled || totalDegreesEnabled || queriedVerticesInDegreeEnabled
				|| queriedVerticesOutDegreeEnabled || queriedVerticesTotalDegreeEnabled) {
			Double inDegreeMIN = Double.POSITIVE_INFINITY, inDegreeMEAN = 0.0, inDegreeMAX = 0.0, inDegreeSum = 0.0;
			Double outDegreeMIN = Double.POSITIVE_INFINITY, outDegreeMEAN = 0.0, outDegreeMAX = 0.0, outDegreeSum = 0.0;
			Double totalDegreeMIN = Double.POSITIVE_INFINITY, totalDegreeMEAN = 0.0, totalDegreeMAX = 0.0,
					totalDegreeSum = 0.0;

			Double queriedVerticesInDegreeMIN = Double.POSITIVE_INFINITY, queriedVerticesInDegreeMEAN = 0.0,
					queriedVerticesInDegreeMAX = 0.0, queriedVerticesInDegreeSum = 0.0;
			Double queriedVerticesOutDegreeMIN = Double.POSITIVE_INFINITY, queriedVerticesOutDegreeMEAN = 0.0,
					queriedVerticesOutDegreeMAX = 0.0, queriedVerticesOutDegreeSum = 0.0;
			Double queriedVerticesTotalDegreeMIN = Double.POSITIVE_INFINITY, queriedVerticesTotalDegreeMEAN = 0.0,
					queriedVerticesTotalDegreeMAX = 0.0, queriedVerticesTotalDegreeSum = 0.0;

			long start = System.nanoTime();

			for (String vertex : jgtGraph.vertexSet()) {
				// Only directed graphs has in and out degrees.
				if (jgtGraph instanceof UndirectedGraph) {
					inDegreesEnabled = false;
					outDegreesEnabled = false;
					queriedVerticesInDegreeEnabled = false;
					queriedVerticesOutDegreeEnabled = false;
				} else if (jgtGraph instanceof DirectedGraph) {
					// Calculate for all vertices
					int inDegree = ((DirectedGraph<String, JGTEdge>) jgtGraph).inDegreeOf(vertex);
					inDegreeMIN = Math.min(inDegreeMIN, inDegree);
					inDegreeSum += inDegree;
					inDegreeMAX = Math.max(inDegreeMAX, inDegree);

					int outDegree = ((DirectedGraph<String, JGTEdge>) jgtGraph).outDegreeOf(vertex);
					outDegreeMIN = Math.min(outDegreeMIN, outDegree);
					outDegreeSum += outDegree;
					outDegreeMAX = Math.max(outDegreeMAX, outDegree);
					// Calculate for queried vertices
					if (queriedVerticesInDegreeEnabled || queriedVerticesOutDegreeEnabled) {
						if (queriedVertices.contains(vertex)) {
							queriedVerticesInDegreeMIN = Math.min(queriedVerticesInDegreeMIN, inDegree);
							queriedVerticesInDegreeSum += inDegree;
							queriedVerticesInDegreeMAX = Math.max(queriedVerticesInDegreeMAX, inDegree);

							queriedVerticesOutDegreeMIN = Math.min(queriedVerticesOutDegreeMIN, outDegree);
							queriedVerticesOutDegreeSum += outDegree;
							queriedVerticesOutDegreeMAX = Math.max(queriedVerticesOutDegreeMAX, outDegree);
						}
					}
				}
				// undirected, and directed graphs has total degree
				int totalDegree = jgtGraph.edgesOf(vertex).size();
				totalDegreeMIN = Math.min(totalDegreeMIN, totalDegree);
				totalDegreeSum += totalDegree;
				totalDegreeMAX = Math.max(totalDegreeMAX, totalDegree);

				// Calculate for queried vertices
				if (queriedVerticesTotalDegreeEnabled) {
					if (queriedVertices.contains(vertex)) {
						queriedVerticesTotalDegreeMIN = Math.min(queriedVerticesTotalDegreeMIN, totalDegree);
						queriedVerticesTotalDegreeSum += totalDegree;
						queriedVerticesTotalDegreeMAX = Math.max(queriedVerticesTotalDegreeMAX, totalDegree);
					}
				}
			}
			// Calculate for all vertices
			inDegreeMEAN = inDegreeSum / verticesSize;
			outDegreeMEAN = outDegreeSum / verticesSize;
			totalDegreeMEAN = totalDegreeSum / verticesSize;
			// Calculate for queried vertices
			if (queriedVerticesInDegreeEnabled)
				queriedVerticesInDegreeMEAN = (queriedVerticesSize == 0) ? 0
						: (queriedVerticesInDegreeSum / queriedVerticesSize);
			if (queriedVerticesOutDegreeEnabled)
				queriedVerticesOutDegreeMEAN = (queriedVerticesSize == 0) ? 0
						: (queriedVerticesOutDegreeSum / queriedVerticesSize);
			if (queriedVerticesTotalDegreeEnabled)
				queriedVerticesTotalDegreeMEAN = (queriedVerticesSize == 0) ? 0
						: (queriedVerticesTotalDegreeSum / queriedVerticesSize);

			long elapsedTime = System.nanoTime() - start;

			if (inDegreesEnabled) {
				// Add InDegrees
				patternProps.setTimeAndValue(GraphP.InDegreeMIN, elapsedTime, inDegreeMIN.toString());
				patternProps.setTimeAndValue(GraphP.InDegreeMEAN, elapsedTime, inDegreeMEAN.toString());
				patternProps.setTimeAndValue(GraphP.InDegreeMAX, elapsedTime, inDegreeMAX.toString());
				patternProps.setTimeAndValue(GraphP.InDegreeSUM, elapsedTime, inDegreeSum.toString());
			}
			if (outDegreesEnabled) {
				// Add OutDegrees
				patternProps.setTimeAndValue(GraphP.OutDegreeMIN, elapsedTime, outDegreeMIN.toString());
				patternProps.setTimeAndValue(GraphP.OutDegreeMEAN, elapsedTime, outDegreeMEAN.toString());
				patternProps.setTimeAndValue(GraphP.OutDegreeMAX, elapsedTime, outDegreeMAX.toString());
				patternProps.setTimeAndValue(GraphP.OutDegreeSUM, elapsedTime, outDegreeSum.toString());
			}
			if (totalDegreesEnabled) {
				// In+Out Degrees
				patternProps.setTimeAndValue(GraphP.TotalDegreeMIN, elapsedTime, totalDegreeMIN.toString());
				patternProps.setTimeAndValue(GraphP.TotalDegreeMEAN, elapsedTime, totalDegreeMEAN.toString());
				patternProps.setTimeAndValue(GraphP.TotalDegreeMAX, elapsedTime, totalDegreeMAX.toString());
				patternProps.setTimeAndValue(GraphP.TotalDegreeSUM, elapsedTime, totalDegreeSum.toString());
			}

			// Degrees related to queried vertices
			if (queriedVerticesInDegreeEnabled) {
				// Add InDegrees
				patternProps.setTimeAndValue(GraphP.QueriedVerticesInDegreeMIN, elapsedTime,
						queriedVerticesInDegreeMIN.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesInDegreeMEAN, elapsedTime,
						queriedVerticesInDegreeMEAN.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesInDegreeMAX, elapsedTime,
						queriedVerticesInDegreeMAX.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesInDegreeSUM, elapsedTime,
						queriedVerticesInDegreeSum.toString());
			}
			if (queriedVerticesOutDegreeEnabled) {
				// Add OutDegrees
				patternProps.setTimeAndValue(GraphP.QueriedVerticesOutDegreeMIN, elapsedTime,
						queriedVerticesOutDegreeMIN.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesOutDegreeMEAN, elapsedTime,
						queriedVerticesOutDegreeMEAN.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesOutDegreeMAX, elapsedTime,
						queriedVerticesOutDegreeMAX.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesOutDegreeSUM, elapsedTime,
						queriedVerticesOutDegreeSum.toString());
			}
			if (queriedVerticesTotalDegreeEnabled) {
				// In+Out Degrees
				patternProps.setTimeAndValue(GraphP.QueriedVerticesTotalDegreeMIN, elapsedTime,
						queriedVerticesTotalDegreeMIN.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesTotalDegreeMEAN, elapsedTime,
						queriedVerticesTotalDegreeMEAN.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesTotalDegreeMAX, elapsedTime,
						queriedVerticesTotalDegreeMAX.toString());
				patternProps.setTimeAndValue(GraphP.QueriedVerticesTotalDegreeSUM, elapsedTime,
						queriedVerticesTotalDegreeSum.toString());
			}
		}
	}

	/**
	 * The density of a graph G = (V,E) measures how many edges are in set E
	 * compared to the maximum possible number of edges between vertices in set
	 * V. the maximum possible number of edges, namely maxPossibleEdgeSize, for
	 * undirected graph has no loops (simple directed graph) can have at most
	 * |V| * (|V| - 1) / 2 edges, simple directed graph can have at most (|V| *
	 * (|V|- 1)) edges so the density of a directed graph is |E| / (|V| * (|V|-
	 * 1)) Based on IGraph density implementation, undirected pseudo graph(has
	 * loops and parallel edges) has at most V*(V+1)/2 possible edges, the
	 * directed pseudo graph has V*V edges @see
	 * http://webwhompers.com/graph-theory.html
	 * 
	 * @param thisProp
	 */
	private void density(GraphP thisProp) {
		long start = System.nanoTime();
		double result = 0.0;
		if (Double.isNaN(maxPossibleEdgeSize) || maxPossibleEdgeSize == 0)
			result = 0;
		else
			result = edgeSize / maxPossibleEdgeSize;
		String value = String.valueOf(result);
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);
	}

	/**
	 * Properties regarding shortest paths we use The Floyd-Warshall algorithm,
	 * which finds all shortest paths (all n^2 of them) in O(n^3) time. It can
	 * also calculate the graph diameter. It ignores weights while calculating
	 * shortest paths.
	 * 
	 * @see https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
	 */
	private void calculateShortestPathsProps() {
		// Eventually before each calculation state of property should be
		// checked, whether it is enabled, then it should be calculated.
		boolean isAnyEnabled = patternProps.isAnyEnabled(new IProperties[] { GraphP.TotalSPs, GraphP.TotalSPsLength,
				GraphP.TransitiveClosure, GraphP.AvgSPsLength, GraphP.VertexAvgSPsLengthMIN,
				GraphP.VertexAvgSPsLengthMEAN, GraphP.VertexAvgSPsLengthMAX, GraphP.VertexAvgSPsLengthSUM,
				GraphP.FarnessMIN, GraphP.FarnessMEAN, GraphP.FarnessMAX, GraphP.FarnessSUM, GraphP.ClosenessMIN,
				GraphP.ClosenessMEAN, GraphP.ClosenessMAX, GraphP.ClosenessSUM, GraphP.QueriedVerticesFarnessMIN,
				GraphP.QueriedVerticesFarnessMEAN, GraphP.QueriedVerticesFarnessMAX, GraphP.QueriedVerticesFarnessSUM,
				GraphP.QueriedVerticesClosenessMIN, GraphP.QueriedVerticesClosenessMEAN,
				GraphP.QueriedVerticesClosenessMAX, GraphP.QueriedVerticesClosenessSUM,
				GraphP.QueriedVerticesDistance2StartVertexMIN, GraphP.QueriedVerticesDistance2StartVertexMEAN,
				GraphP.QueriedVerticesDistance2StartVertexMAX, GraphP.QueriedVerticesDistance2StartVertexSUM,
				GraphP.Diameter });

		if (isAnyEnabled) {
			long startShortestPathsTime = System.nanoTime();
			FloydWarshallShortestPaths<String, JGTEdge> fwShortesPaths = new FloydWarshallShortestPaths<String, JGTEdge>(
					jgtGraph);

			Collection<GraphPath<String, JGTEdge>> shortestPaths = fwShortesPaths.getShortestPaths();
			long endShortestPathsTime = System.nanoTime() - startShortestPathsTime;

			/**
			 * Properties regarding all shortest paths
			 */

			/**
			 * Total number of shortest paths in the graph
			 */
			GraphP thisProp = GraphP.TotalSPs;
			if (patternProps.isEnabled(thisProp)) {
				int totalSPs = 0;
				long start = System.nanoTime();
				totalSPs = shortestPaths.size();
				long elapsedTime = endShortestPathsTime + (System.nanoTime() - start);
				patternProps.setTimeAndValue(thisProp, elapsedTime, String.valueOf(totalSPs));
			}

			/**
			 * The summation of all shortest paths LENGTH of all vertices
			 */
			thisProp = GraphP.TotalSPsLength;
			if (patternProps.isEnabled(thisProp)) {
				long start = System.nanoTime();
				int totalShortestPathsLength = 0;

				for (GraphPath<String, JGTEdge> path : shortestPaths)
					totalShortestPathsLength += path.getEdgeList().size();

				long elapsedTime = endShortestPathsTime + (System.nanoTime() - start);
				String value = String.valueOf(totalShortestPathsLength);
				patternProps.setTimeAndValue(thisProp, elapsedTime, value);
			}

			thisProp = GraphP.TransitiveClosure;
			if (patternProps.isEnabled(thisProp)) {
				long start = System.nanoTime();
				double averageShortestPathsNumber = 0.0;
				if (Double.isNaN(maxPossibleEdgeSize) || maxPossibleEdgeSize == 0)
					averageShortestPathsNumber = 0;
				else
					averageShortestPathsNumber = shortestPaths.size() / maxPossibleEdgeSize;
				long elapsedTime = endShortestPathsTime + (System.nanoTime() - start);
				String value = String.valueOf(averageShortestPathsNumber);
				patternProps.setTimeAndValue(thisProp, elapsedTime, value);
			}

			/**
			 * We divide the total length of shortest paths to max number of
			 * edges based on the definition section of average path length
			 * definition in wikipedia
			 * 
			 * https://en.wikipedia.org/wiki/Average_path_length
			 */
			thisProp = GraphP.AvgSPsLength;
			if (patternProps.isEnabled(thisProp)) {
				long start = System.nanoTime();
				int totalShortestPathsLength = 0;
				for (GraphPath<String, JGTEdge> path : shortestPaths)
					totalShortestPathsLength += path.getEdgeList().size();

				double averagePathlength = 0.0;
				if (Double.isNaN(maxPossibleEdgeSize) || maxPossibleEdgeSize == 0)
					averagePathlength = 0;
				else
					averagePathlength = (totalShortestPathsLength) / maxPossibleEdgeSize;

				String value = String.valueOf(averagePathlength);
				long elapsedTime = endShortestPathsTime + (System.nanoTime() - start);
				patternProps.setTimeAndValue(thisProp, elapsedTime, value);
			}

			/**
			 * Properties regarding the shortest paths OF EACH VERTEX Namely,
			 * here the average is the summation of shortest paths of each
			 * vertex, divided by its shortest path number
			 */
			long startAvgPathsTime = System.nanoTime();
			double vertexAvgSPsLengthMIN = Double.POSITIVE_INFINITY, vertexAvgSPsLengthMEAN = 0.0,
					vertexAvgSPsLengthMAX = 0.0, vertexAvgSPsLengthSUM = 0.0;
			double farnessMIN = Double.POSITIVE_INFINITY, farnessMEAN = 0.0, farnessMAX = 0.0, farnessSUM = 0.0;
			double closenessMIN = Double.POSITIVE_INFINITY, closenessMEAN = 0.0, closenessMAX = 0.0, closenessSUM = 0.0;

			// Properties related to Queried Species
			double QueriedVerticesFarnessMIN = Double.POSITIVE_INFINITY, QueriedVerticesFarnessMEAN = 0.0,
					QueriedVerticesFarnessMAX = 0.0, QueriedVerticesFarnessSUM = 0.0;

			double QueriedVerticesClosenessMIN = Double.POSITIVE_INFINITY, QueriedVerticesClosenessMEAN = 0.0,
					QueriedVerticesClosenessMAX = 0.0, QueriedVerticesClosenessSUM = 0.0;
			for (String vertex : jgtGraph.vertexSet()) {
				List<GraphPath<String, JGTEdge>> vertexShortestPaths = fwShortesPaths.getShortestPaths(vertex);
				/**
				 * The farness of a node x is defined as the sum of its
				 * distances from all other nodes, and its closeness was defined
				 * as kind of the inverse of the farness, see
				 * https://en.wikipedia.org/wiki/Centrality
				 */
				int vertexSPathsCount = vertexShortestPaths.size();
				double vertexFarness = 0;

				// sum the shortest path belongs to current vertex
				for (GraphPath<String, JGTEdge> vertexShortestPath : vertexShortestPaths) {
					int pathLegth = vertexShortestPath.getEdgeList().size();
					vertexFarness += pathLegth;
				}
				// average path stats
				double averagePathLenght = (vertexSPathsCount == 0) ? 0 : (vertexFarness / vertexSPathsCount);
				vertexAvgSPsLengthMIN = Math.min(vertexAvgSPsLengthMIN, averagePathLenght);
				vertexAvgSPsLengthSUM += averagePathLenght;
				vertexAvgSPsLengthMAX = Math.max(vertexAvgSPsLengthMAX, averagePathLenght);

				farnessMIN = Math.min(farnessMIN, vertexFarness);
				farnessSUM += vertexFarness;
				farnessMAX = Math.max(farnessMAX, vertexFarness);

				// Closeness scores are calculated using the formula 1/(average
				// distance from vertex v to all other vertices)
				double vertexClosenessCentrality = (averagePathLenght == 0) ? 0 : (1 / averagePathLenght);
				closenessMIN = Math.min(closenessMIN, vertexClosenessCentrality);
				closenessSUM += vertexClosenessCentrality;
				closenessMAX = Math.max(closenessMAX, vertexClosenessCentrality);

				// Properties related to Queried Species
				if (queriedVertices.contains(vertex)) {
					QueriedVerticesFarnessMIN = Math.min(QueriedVerticesFarnessMIN, vertexFarness);
					QueriedVerticesFarnessSUM += vertexFarness;
					QueriedVerticesFarnessMAX = Math.max(QueriedVerticesFarnessMAX, vertexFarness);

					double queriedVerticesClosenessCentrality = (averagePathLenght == 0) ? 0 : (1 / averagePathLenght);
					QueriedVerticesClosenessMIN = Math.min(QueriedVerticesClosenessMIN,
							queriedVerticesClosenessCentrality);
					QueriedVerticesClosenessSUM += queriedVerticesClosenessCentrality;
					QueriedVerticesClosenessMAX = Math.max(QueriedVerticesClosenessMAX,
							queriedVerticesClosenessCentrality);
				}
			}
			vertexAvgSPsLengthMEAN = vertexAvgSPsLengthSUM / verticesSize;
			farnessMEAN = farnessSUM / verticesSize;
			closenessMEAN = closenessSUM / verticesSize;
			// Properties related to Queried Species
			QueriedVerticesFarnessMEAN = (queriedVerticesSize == 0) ? 0
					: (QueriedVerticesFarnessSUM / queriedVerticesSize);
			QueriedVerticesClosenessMEAN = (queriedVerticesSize == 0) ? 0
					: (QueriedVerticesClosenessSUM / queriedVerticesSize);

			// Properties related to the distance between start vertex to
			// queried
			// vertices.
			double QueriedVerticesDistance2StartVertexMIN = Double.POSITIVE_INFINITY,
					QueriedVerticesDistance2StartVertexMEAN = 0.0, QueriedVerticesDistance2StartVertexMAX = 0.0,
					QueriedVerticesDistance2StartVertexSUM = 0.0;
			for (String queriedVertex : queriedVertices) {
				int queried2StartDistance = 0;
				// if start and queried vertices are not the same vertices
				if (!startingVertex.equals(queriedVertex)) {
					GraphPath<String, JGTEdge> queried2StartPath = fwShortesPaths.getShortestPath(startingVertex,
							queriedVertex);
					if (queried2StartPath != null) {
						queried2StartDistance = queried2StartPath.getEdgeList().size();
					}
				} else {
					queried2StartDistance = 0;
				}
				QueriedVerticesDistance2StartVertexMIN = Math.min(QueriedVerticesDistance2StartVertexMIN,
						queried2StartDistance);
				QueriedVerticesDistance2StartVertexSUM += queried2StartDistance;
				QueriedVerticesDistance2StartVertexMAX = Math.max(QueriedVerticesDistance2StartVertexMAX,
						queried2StartDistance);
			}
			QueriedVerticesDistance2StartVertexMEAN = (queriedVerticesSize == 0) ? 0
					: (QueriedVerticesDistance2StartVertexSUM / queriedVerticesSize);

			long elapsedAvgPathsTime = System.nanoTime() - startAvgPathsTime;
			thisProp = GraphP.VertexAvgSPsLengthMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexAvgSPsLengthMIN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.VertexAvgSPsLengthMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexAvgSPsLengthMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.VertexAvgSPsLengthMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexAvgSPsLengthMAX);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.VertexAvgSPsLengthSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexAvgSPsLengthSUM);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}

			// Farness properties
			thisProp = GraphP.FarnessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(farnessMIN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.FarnessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(farnessMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.FarnessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(farnessMAX);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.FarnessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(farnessSUM);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}

			// Closeness Centrality features
			thisProp = GraphP.ClosenessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(closenessMIN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.ClosenessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(closenessMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.ClosenessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(closenessMAX);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.ClosenessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(closenessSUM);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}

			/**
			 * Properties related to Queried Species QueriedVerticesFarness
			 */
			// QueriedVerticesFarness properties
			thisProp = GraphP.QueriedVerticesFarnessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesFarnessMIN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesFarnessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesFarnessMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesFarnessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesFarnessMAX);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesFarnessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesFarnessSUM);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			// QueriedVerticesCloseness Centrality features
			thisProp = GraphP.QueriedVerticesClosenessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesClosenessMIN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesClosenessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesClosenessMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesClosenessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesClosenessMAX);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesClosenessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesClosenessSUM);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			// QueriedVerticesDistance2StartVertex properties
			thisProp = GraphP.QueriedVerticesDistance2StartVertexMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesDistance2StartVertexMIN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesDistance2StartVertexMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesDistance2StartVertexMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesDistance2StartVertexMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesDistance2StartVertexMAX);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}
			thisProp = GraphP.QueriedVerticesDistance2StartVertexSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesDistance2StartVertexSUM);
				patternProps.setTimeAndValue(thisProp, elapsedAvgPathsTime, value);
			}

			/**
			 * Diameter is "the longest of shortest paths", i.e., max(d(u,v))
			 * where d(u,v) is a graph distance. For more
			 * <a href="http://mathworld.wolfram.com/GraphDiameter.html">Graph
			 * Diameter</a> "the longest of shortest paths"
			 */
			thisProp = GraphP.Diameter;
			if (patternProps.isEnabled(thisProp)) {
				long start = System.nanoTime();
				int diameter = 0;
				for (GraphPath<String, JGTEdge> path : shortestPaths)
					diameter = Math.max(diameter, path.getEdgeList().size());
				String value = String.valueOf(diameter);
				long elapsedTime = System.nanoTime() - start;
				patternProps.setTimeAndValue(thisProp, elapsedTime, value);
			}
		}

	}

	/**
	 * CALCULATION DONE BY JUNG LIBRARY Betweenness centrality of default graph.
	 */
	private void calculateBetweenness() {

		boolean isAnyEnabled = patternProps
				.isAnyEnabled(new IProperties[] { GraphP.EdgeBetweennessMIN, GraphP.EdgeBetweennessMEAN,
						GraphP.EdgeBetweennessMAX, GraphP.EdgeBetweennessSUM, GraphP.VertexBetweennessMIN,
						GraphP.VertexBetweennessMEAN, GraphP.VertexBetweennessMAX, GraphP.VertexBetweennessSUM,
						GraphP.QueriedVerticesBetweennessMIN, GraphP.QueriedVerticesBetweennessMEAN,
						GraphP.QueriedVerticesBetweennessMAX, GraphP.QueriedVerticesBetweennessSUM });
		if (isAnyEnabled) {
			long startBetweennessTime = System.nanoTime();
			BetweennessCentrality<String, JUNGEdge> betweenessCs = new BetweennessCentrality<String, JUNGEdge>(
					jungGraph);

			long elapsedBetwennessTime = System.nanoTime() - startBetweennessTime;

			long startEdgeBetTime = System.nanoTime();
			// Calculating Betweenness centrality score of EDGES:
			Collection<JUNGEdge> edges = jungGraph.getEdges();
			double edgeBetMIN = 0, edgeBetMEAN = 0, edgeBetMAX = 0, edgeBetSUM = 0.0;
			for (JUNGEdge edge : edges) {
				double betweeness = betweenessCs.getEdgeScore(edge);
				edgeBetMIN = Math.min(edgeBetMIN, betweeness);
				edgeBetSUM += betweeness;
				edgeBetMAX = Math.max(edgeBetMAX, betweeness);
			}
			edgeBetMEAN = (edges.size() == 0) ? 0 : (edgeBetSUM / edges.size());

			long elapsedEdgeBetTime = (System.nanoTime() - startEdgeBetTime) + elapsedBetwennessTime;
			GraphP thisProp = GraphP.EdgeBetweennessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(edgeBetMIN);
				patternProps.setTimeAndValue(thisProp, elapsedEdgeBetTime, value);
			}
			thisProp = GraphP.EdgeBetweennessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(edgeBetMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedEdgeBetTime, value);
			}
			thisProp = GraphP.EdgeBetweennessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(edgeBetMAX);
				patternProps.setTimeAndValue(thisProp, elapsedEdgeBetTime, value);
			}
			thisProp = GraphP.EdgeBetweennessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(edgeBetSUM);
				patternProps.setTimeAndValue(thisProp, elapsedEdgeBetTime, value);
			}

			long startVertexBetTime = System.nanoTime();
			Collection<String> vertices = jungGraph.getVertices();
			double vertexBetMIN = 0, vertexBetMEAN = 0, vertexBetMAX = 0, vertexBetSUM = 0.0;
			double QueriedVerticesBetweennessMIN = 0, QueriedVerticesBetweennessMEAN = 0,
					QueriedVerticesBetweennessMAX = 0, QueriedVerticesBetweennessSUM = 0;
			for (String vertex : vertices) {
				double betweeness = betweenessCs.getVertexScore(vertex);
				vertexBetMIN = Math.min(vertexBetMIN, betweeness);
				vertexBetSUM += betweeness;
				vertexBetMAX = Math.max(vertexBetMAX, betweeness);
				// Properties related to Queried Species
				if (queriedVertices.contains(vertex)) {
					QueriedVerticesBetweennessMIN = Math.min(QueriedVerticesBetweennessMIN, betweeness);
					QueriedVerticesBetweennessSUM += betweeness;
					QueriedVerticesBetweennessMAX = Math.max(QueriedVerticesBetweennessMAX, betweeness);
				}
			}
			vertexBetMEAN = (vertices.size() == 0) ? 0 : (vertexBetSUM / vertices.size());
			// Properties related to Queried Species
			QueriedVerticesBetweennessMEAN = (queriedVerticesSize == 0) ? 0
					: (QueriedVerticesBetweennessSUM / queriedVerticesSize);

			long elapsedVertexBetTime = (System.nanoTime() - startVertexBetTime) + elapsedBetwennessTime;

			thisProp = GraphP.VertexBetweennessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexBetMIN);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			thisProp = GraphP.VertexBetweennessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexBetMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			thisProp = GraphP.VertexBetweennessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexBetMAX);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			thisProp = GraphP.VertexBetweennessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(vertexBetSUM);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			// Properties related to Queried Species
			thisProp = GraphP.QueriedVerticesBetweennessMIN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesBetweennessMIN);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			thisProp = GraphP.QueriedVerticesBetweennessMEAN;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesBetweennessMEAN);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			thisProp = GraphP.QueriedVerticesBetweennessMAX;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesBetweennessMAX);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
			thisProp = GraphP.QueriedVerticesBetweennessSUM;
			if (patternProps.isEnabled(thisProp)) {
				String value = String.valueOf(QueriedVerticesBetweennessSUM);
				patternProps.setTimeAndValue(thisProp, elapsedVertexBetTime, value);
			}
		}
	}

	/**
	 * The reciprocity of a graph g is the fraction of reciprocal edges over all
	 * edges of g. For a directed graph, the edges (u,v) and (v,u) are
	 * reciprocal and form a cycle of <b>length 2</b>, not 1. For an undirected
	 * graph, all edges are reciprocal. Reciprocity can be calculated with
	 * undirected graphs, directed graphs, and weighted graphs.
	 * <link>https://reference
	 * .wolfram.com/language/ref/GraphReciprocity.html</link>
	 * 
	 * @param thisProp
	 */
	private void reciprocity(GraphP thisProp) {
		long start = System.nanoTime();
		double recip = 0.0;
		if (jgtGraph instanceof UndirectedGraph) {
			// For an undirected graph, all edges are reciprocal.
			recip = 1;// edgeSize/edgeSize =1;
		} else if (jgtGraph instanceof DirectedGraph) {
			double count = 0;// reciprocity for each vertex.
			for (String source : jgtGraph.vertexSet()) {
				ArrayList<String> visittedTargets = new ArrayList<String>();
				for (JGTEdge source2TargetEdge : ((DirectedGraph<String, JGTEdge>) jgtGraph).outgoingEdgesOf(source)) {
					String target = ((DirectedGraph<String, JGTEdge>) jgtGraph).getEdgeTarget(source2TargetEdge);
					if (!visittedTargets.contains(target)) {
						// if there are many parallel directed edges, then we
						// need to calculate all outs and ins from source,
						// target vertices, the reciprocity of current source
						// vertex will be the min of these
						int allOuts2Target = ((DirectedGraph<String, JGTEdge>) jgtGraph).getAllEdges(source, target)
								.size();
						int allInsFromTarget = ((DirectedGraph<String, JGTEdge>) jgtGraph).getAllEdges(target, source)
								.size();
						// the reciprocity will be the
						count += Math.min(allOuts2Target, allInsFromTarget);
						visittedTargets.add(target);
					}
				}
			}
			recip = (edgeSize == 0) ? 0 : (count / edgeSize);
		}
		String value = String.valueOf(recip);
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);

	}

	/**
	 * A weak component is a maximal subgraph which would be connected if we
	 * ignored the direction of the arcs. A strong component is a maximal
	 * subgraph in which there is a path from every point to every point
	 * following all the arcs in the direction they are pointing.
	 * 
	 * @param thisProp
	 */
	private void weakComponents(GraphP thisProp) {
		long start = System.nanoTime();
		ConnectivityInspector<String, JGTEdge> conIns = null;
		if (jgtGraph instanceof UndirectedGraph) {
			conIns = new ConnectivityInspector<String, JGTEdge>((UndirectedGraph<String, JGTEdge>) jgtGraph);
		} else if (jgtGraph instanceof DirectedGraph) {
			conIns = new ConnectivityInspector<String, JGTEdge>((DirectedGraph<String, JGTEdge>) jgtGraph);
		}
		String value = String.valueOf(conIns.connectedSets().size());
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);
	}

	/**
	 * A strong component is a maximal subgraph in which there is a path from
	 * every point to every point following all the arcs in the direction they
	 * are pointing.
	 * 
	 * @param thisProp
	 */
	private void directedStrongComponents(GraphP thisProp) {
		// if the graph is directed, then it can be calculated
		if (jgtGraph instanceof DirectedGraph) {
			long start = System.nanoTime();
			StrongConnectivityInspector<String, JGTEdge> conIns = new StrongConnectivityInspector<String, JGTEdge>(
					(DirectedGraph<String, JGTEdge>) jgtGraph);
			String value = String.valueOf(conIns.stronglyConnectedSubgraphs().size());
			long elapsedTime = System.nanoTime() - start;
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}
	}

	/**
	 * In graph theory, a biconnected component (also known as a block or
	 * 2-connected component) is a maximal biconnected subgraph. Any connected
	 * graph decomposes into a tree of biconnected components called the
	 * block-cut tree of the graph. The graph translated to UNDIRECTED graph
	 * first, then value calculated.The blocks are attached to each other at
	 * shared vertices called cut vertices or articulation points. Specifically,
	 * a cut vertex is any vertex whose removal increases the number of
	 * connected components.
	 * 
	 * @see https://en.wikipedia.org/wiki/Biconnected_component
	 * 
	 * @param thisProp
	 */
	private void undirectedBiconnectedComponents(GraphP thisProp) {
		// if the graph is undirected, then it can be calculated
		if (jgtGraph instanceof UndirectedGraph) {
			long start = System.nanoTime();
			if (biConIns == null)
				biConIns = new BiconnectivityInspector<String, JGTEdge>(((UndirectedGraph<String, JGTEdge>) jgtGraph));
			String value = String.valueOf(biConIns.getBiconnectedVertexComponents().size());
			long elapsedTime = System.nanoTime() - start;
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}

	}

	/**
	 * The blocks are attached to each other at shared vertices called cut
	 * vertices or articulation points. Specifically, a cut vertex is any vertex
	 * whose removal increases the number of connected components (whose removal
	 * disconnects the remaining subgraph).The graph translated to UNDIRECTED
	 * graph first, then value calculated.
	 * 
	 * @see https://en.wikipedia.org/wiki/Biconnected_component
	 * 
	 * @param thisProp
	 */
	private void undirectedArticulationPoints(GraphP thisProp) {
		// if the graph is undirected, then it can be calculated
		if (jgtGraph instanceof UndirectedGraph) {
			long start = System.nanoTime();

			if (biConIns == null)
				biConIns = new BiconnectivityInspector<String, JGTEdge>(((UndirectedGraph<String, JGTEdge>) jgtGraph));
			String value = String.valueOf(biConIns.getCutpoints().size());
			long elapsedTime = System.nanoTime() - start;
			patternProps.setTimeAndValue(thisProp, elapsedTime, value);
		}

	}

	/**
	 * Let G=(V,E) be a (not necessarily simple) UNDIRECTED 'edge-weighted'
	 * graph with nonnegative weights. A cut C of G is any nontrivial subset of
	 * V, and the weight of the cut is the sum of weights of edges crossing the
	 * cut. A mincut is then defined as a cut of G of minimum weight. The
	 * problem is polynomial time solvable as a series of network flow problems
	 * or using the algorithm of Stoer and Wagner (1994).
	 * 
	 * It requires graph be undirected and weighted.
	 * 
	 * @see https://en.wikipedia.org/wiki/Stoer%E2%80%93Wagner_algorithm
	 * @see http://mathworld.wolfram.com/Mincut.html
	 * 
	 * @param thisProp
	 */
	private void undirectedMinimumCut(GraphP thisProp) {
		long start = System.nanoTime();
		double result = 0.0;
		// Graph should have at least two vertex otherwise it throws exception,
		// if less then two vertices then minimumCut will be zero
		if (verticesSize >= 2) {
			if (jgtGraph instanceof UndirectedGraph) {
				// Loop is not allowed with this minCut algorithm, so we need to
				// remove loops first (but I leave the parallel
				// edges).
				UndirectedGraph<String, JGTEdge> pGraph = new Pseudograph<String, JGTEdge>(JGTEdge.class);

				for (String vertex : jgtGraph.vertexSet()) {
					pGraph.addVertex(vertex);
				}

				for (JGTEdge cEdge : jgtGraph.edgeSet()) {
					// if it is not loop, namely vertex pointing itself
					if (!cEdge.getSource().equals(cEdge.getTarget())) {
						pGraph.addEdge(cEdge.getSource(), cEdge.getTarget());
					}
				}
				StoerWagnerMinimumCut<String, JGTEdge> minCut = new StoerWagnerMinimumCut<String, JGTEdge>(pGraph);
				/**
				 * It gets only one min cut, but this cut may require more than
				 * one edge to be removed, the minCutWeight returns total weight
				 * of these edges
				 */
				result = minCut.minCutWeight();// the out (cut_ted) edge num
												// (since weight is 1)
			}
		}
		String value = String.valueOf(result);
		long elapsedTime = System.nanoTime() - start;
		patternProps.setTimeAndValue(thisProp, elapsedTime, value);
	}
}
