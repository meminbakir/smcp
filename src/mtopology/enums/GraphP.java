/**
 * 
 */
package mtopology.enums;

/**
 * Represents the graph properties to be calculated. The properties are common for both directed and undirected graphs,
 * except those started with UNDIRECTED are specific to only directed graphs, and similarly those starts wit DIRECTED are
 * specific to the directed graph types.
 * 
 * @author Mehmet Emin BAKIR
 *
 * 
 */
public enum GraphP implements IProperties {
	Vertices,
	Edges,

	/**
	 * Calculation regards the InDegrees of each vertex
	 */
	InDegreeMIN,
	InDegreeMEAN,
	InDegreeMAX,
	/**
	 * Sum of InDegrees of all vertices
	 */
	InDegreeSUM,
	/**
	 * QueriedVertices are the species queried with PQuery
	 */
	QueriedVerticesInDegreeMIN,
	QueriedVerticesInDegreeMEAN,
	QueriedVerticesInDegreeMAX,
	QueriedVerticesInDegreeSUM,

	/**
	 * Calculation regards the OutDegrees of each vertex
	 */
	OutDegreeMIN,
	OutDegreeMEAN,
	OutDegreeMAX,
	/**
	 * Sum of OutDegrees of all vertices
	 */
	OutDegreeSUM,
	/**
	 * QueriedVertices are the species queried with PQuery
	 */
	QueriedVerticesOutDegreeMIN,
	QueriedVerticesOutDegreeMEAN,
	QueriedVerticesOutDegreeMAX,
	QueriedVerticesOutDegreeSUM,

	/**
	 * Calculation regards the Total degree of each vertex
	 */
	TotalDegreeMIN,
	TotalDegreeMEAN,
	TotalDegreeMAX,
	/**
	 * Sum of TotalDegrees of all vertices
	 */
	TotalDegreeSUM,
	/**
	 * QueriedVertices are the species queried with PQuery
	 */
	QueriedVerticesTotalDegreeMIN,
	QueriedVerticesTotalDegreeMEAN,
	QueriedVerticesTotalDegreeMAX,
	QueriedVerticesTotalDegreeSUM,

	/**
	 * The density of a graph G = (V,E) measures how many edges are in set E compared to the maximum possible number of edges
	 * between vertices in set V. the maximum possible number of edges, namely maxPossibleEdgeSize, for undirected graph has
	 * no loops (simple directed graph) can have at most |V| * (|V| - 1) / 2 edges, simple directed graph can have at most
	 * (|V| * (|V|- 1)) edges so the density of a directed graph is |E| / (|V| * (|V|- 1)) Based on IGraph density
	 * implementation, undirected pseudo graph(has loops and parallel edges) has at most V*(V+1)/2 possible edges, the
	 * directed pseudo graph has V*V edges @see http://webwhompers.com/graph-theory.html
	 */
	Density,

	/**
	 * Total number of shortest paths in the graph, namely shortestPaths size(number)
	 */
	TotalSPs,
	/**
	 * The summation of all shortest paths LENGTH of all vertices
	 */
	TotalSPsLength,

	/**
	 * The average number of shortest paths in the graph. I added this prop, I expect it will give us information on how easy
	 * it to access different species. The amount of species also important, because high number of species will cause many
	 * short paths while maybe harder to access species. It might be better to find something like path density. The number
	 * of all possible paths can be huge, instead of paths, to Normalise shortest paths over species number, To get density
	 * of shortest paths over max num of edges, we can use shortestPathsNumber/allVertices(|V|*|V-1|). TODO This property
	 * gives same results as transitiveClosure, we have to rename this and remove transitiveClosure which is probably
	 * computationally expensive with internal implementation /** Consider a directed graph G=(V,E), where V is the set of
	 * vertices and E is the set of edges. The transitive closure of G is a graph G+ = (V,E+) such that for all v,w in V
	 * there is an edge (v,w) in E+ if and only if there is a non-null path from v to w in G. That is, can one get from node
	 * a to node d in one or more hops?
	 * 
	 * @see http://www.cs.hut.fi/~enu/tc.html
	 * @see https://en.wikipedia.org/wiki/Transitive_closure#In_graph_theory
	 */
	TransitiveClosure,
	/**
	 * Divide the total length of shortest paths to max number of edges based (|V|*(|V|-1))
	 * <a href="https://en.wikipedia.org/wiki/Average_path_length">https://en. wikipedia.org/wiki/Average_path_length</a>
	 * 
	 * @see https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
	 */
	AvgSPsLength,

	/**
	 * AverageShortestPaths is; For each vertex v in graph, calculates the average shortest path length from v to all other
	 * vertices in graph using the metric specified by d. Returns the min of AverageShortestPaths of all vertices
	 */
	VertexAvgSPsLengthMIN,

	/**
	 * Returns the Mean of AverageShortestPaths which calculated for each vertex {@link GProps#AverageShortestPathsMin} of
	 * all vertices
	 */
	VertexAvgSPsLengthMEAN,

	/**
	 * Returns the Max of AverageShortestPaths which calculated for each vertex {@link GProps#AverageShortestPathsMin} of all
	 * vertices
	 */
	VertexAvgSPsLengthMAX,
	VertexAvgSPsLengthSUM,
	/**
	 * Calculate the distance between the start vertex to the queries vertex.
	 */
	QueriedVerticesDistance2StartVertexMIN,
	QueriedVerticesDistance2StartVertexMEAN,
	QueriedVerticesDistance2StartVertexMAX,
	QueriedVerticesDistance2StartVertexSUM,
	/**
	 * The farness of a node x is defined as the sum of its distances from all other nodes, and its closeness was defined as
	 * the inverse of the farness,
	 * 
	 * @see https://en.wikipedia.org/wiki/Centrality
	 */
	FarnessMIN,
	FarnessMEAN,
	FarnessMAX,
	FarnessSUM,
	/**
	 * The queried vertices is the queried species.
	 */
	QueriedVerticesFarnessMIN,
	QueriedVerticesFarnessMEAN,
	QueriedVerticesFarnessMAX,
	QueriedVerticesFarnessSUM,
	/**
	 * Closeness centrality of each vertex is 1/(the mean of its shortest path lengths) MIN, MEAN and MAX are graph-wise
	 * values of the closeness.
	 */
	ClosenessMIN,
	ClosenessMEAN,
	ClosenessMAX,
	ClosenessSUM,

	QueriedVerticesClosenessMIN,
	QueriedVerticesClosenessMEAN,
	QueriedVerticesClosenessMAX,
	QueriedVerticesClosenessSUM,
	/**
	 * The longest of shortest paths
	 */
	Diameter,

	/**
	 * An important node will lie on a high proportion of paths between other nodes in the network. That is It is equal to
	 * the number of shortest paths from all vertices to all others that pass through that node.
	 */
	EdgeBetweennessMIN,
	EdgeBetweennessMEAN,
	EdgeBetweennessMAX,
	EdgeBetweennessSUM,

	VertexBetweennessMIN,
	VertexBetweennessMEAN,
	VertexBetweennessMAX,
	VertexBetweennessSUM,

	QueriedVerticesBetweennessMIN,
	QueriedVerticesBetweennessMEAN,
	QueriedVerticesBetweennessMAX,
	QueriedVerticesBetweennessSUM,

	/**
	 * The reciprocity of a graph g is the fraction of reciprocal edges over all edges of g. For a directed graph, the edges
	 * (u,v) and (v,u) are reciprocal and form a cycle of <b>length 2</b>, not 1. For an undirected graph, all edges are
	 * reciprocal. Reciprocity can be calculated with undirected graphs, directed graphs, and weighted graphs.
	 * <link>https://reference .wolfram.com/language/ref/GraphReciprocity.html</link>
	 */
	Reciprocity,

	/**
	 * A weak component is a maximal subgraph which would be connected if we ignored the direction of the arcs. Namely, there
	 * is a path from every point to every point following all the arcs regardless of their direction.
	 */
	WeakComponents,

	/**
	 * A strong component is a maximal subgraph in which there is a path from every point to every point following all the
	 * arcs in the direction they are pointing. Can be computed only for DIRECTED graphs.
	 */
	DIRECTEDStrongComponents,

	/**
	 * A biconnected component (also known as a block or 2-connected component) is a maximal biconnected subgraph. Can be
	 * computed only for UNDIRECTED graphs.
	 */
	UNDIRECTEDBiconnectedComponents,

	/**
	 * The blocks are attached to each other at shared vertices called cut vertices or articulation points. Specifically, a
	 * cut vertex is any vertex whose removal increases the number of connected components (whose removal disconnects the
	 * remaining subgraph).The graph translated to undirected graph first, then value calculated. Can be computed only for
	 * UNDIRECTED graphs.
	 * 
	 * @see https://en.wikipedia.org/wiki/Biconnected_component
	 */
	UNDIRECTEDArticulationPoints,

	/**
	 * A cut of a graph G is a partition of the vertices into two, non-empty sets. The weight of such a partition is the
	 * number of edges between the two sets if G is unweighted, or the sum of the weights of all edges between the two sets
	 * if G is weighted. A min-cut is a cut having the least weight. Can be computed only for UNDIRECTED graphs.
	 * 
	 * @see http://www.boost.org/doc/libs/1_46_1/libs/graph/doc/ stoer_wagner_min_cut.html
	 * @see https://en.wikipedia.org/wiki/Minimum_cut
	 */
	UNDIRECTEDMinimumCut
}