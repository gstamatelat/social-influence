package gr.james.socialinfluence.api;

import gr.james.socialinfluence.algorithms.distance.Dijkstra;
import gr.james.socialinfluence.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.GraphException;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.collections.VertexPair;

import java.util.*;

/**
 * <p>Represents a collection of vertices and edges. The graph is weighted directed and there can't be more than one
 * edge from node {@code i} to node {@code j} (it's not a multigraph).</p><p><b>Remark on performance &ndash; </b>Some
 * methods have both single-vertex and multi-vertex variants, for example {@code getDegree(Vertex v)} and {@code
 * getDegree()}. You should prefer the latter when your algorithm needs all the degrees and avoid iterating through all
 * vertices and calling the single-vertex method each time. {@code for (Vertex v: g.getVertices()) { v.getInEdges(); }}
 * construct is to be avoided.</p><p><b>Collections returned &ndash; </b>Methods that return collections
 * ({@link Map Maps} or {@link Set Sets}) return read-only versions of them, meaning that you can't
 * insert or remove elements. These collections are also not backed by the graph, changes to the graph won't affect
 * these collections after they have been returned, you need to call the method again. The elements themselves,
 * however, are shallow copies and can be used to change the state of the graph.</p>
 */
public interface Graph {
    String getMeta(String key);

    Graph setMeta(String key, String value);

    Graph clearMeta();

    default String getGraphType() {
        return this.getMeta("type");
    }

    boolean containsVertex(Vertex v);

    boolean containsEdge(Vertex source, Vertex target);

    default <T extends Graph> Graph deepCopy(Class<T> type) {
        return deepCopy(type, this.getVertices());
    }

    default <T extends Graph> Graph deepCopy(Class<T> type, Set<Vertex> includeOnly) {
        Graph g = Helper.instantiateGeneric(type);
        for (Vertex v : includeOnly) {
            if (!this.containsVertex(v)) {
                throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "deepCopy");
            }
            g.addVertex(v);
        }
        for (Map.Entry<VertexPair, Edge> e : this.getEdges().entrySet()) {
            if ((g.containsVertex(e.getKey().getFirst())) && g.containsVertex(e.getKey().getSecond())) {
                g.addEdge(e.getKey().getFirst(), e.getKey().getSecond()).setWeight(e.getValue().getWeight());
            }
        }
        return g;
    }

    /**
     * <p>Get a {@link Vertex} of this graph based on its index. Index is a deterministic, per-graph attribute between
     * {@code 0} (inclusive) and {@link #getVerticesCount()} (exclusive), indicating the rank of the ID of the specific
     * vertex in the ordered ID list.</p>
     *
     * @param index the index of the vertex
     * @return the vertex reference with the provided index
     * @throws GraphException if {@code index} is outside of {@code 0} (inclusive) and {@link #getVerticesCount()}
     *                        (exclusive)
     */
    Vertex getVertexFromIndex(int index);

    default Vertex getRandomVertex() {
        // TODO: There must be some better way ...
        RandomVertexIterator rvi = new RandomVertexIterator(this);
        return rvi.next();
    }

    default Set<Vertex> getStubbornVertices() {
        Set<Vertex> stubborn = new TreeSet<>();
        for (Vertex v : this.getVertices()) {
            if (this.getOutDegree(v) == 1 && this.getOutEdges(v).containsKey(v)) {
                stubborn.add(v);
            }
        }
        return Collections.unmodifiableSet(stubborn);
    }

    default Map<VertexPair, Edge> getEdges() {
        Map<VertexPair, Edge> edges = new HashMap<>();
        for (Vertex v : this.getVertices()) {
            for (Map.Entry<Vertex, Edge> e : this.getOutEdges(v).entrySet()) {
                edges.put(new VertexPair(v, e.getKey()), e.getValue());
            }
        }
        return Collections.unmodifiableMap(edges);
    }

    /**
     * <p>Calculates the total amount of directed edges that this graph has. This method is a little faster than using
     * {@code getEdges().size()}.</p>
     * <dl><dt><b>Complexity:</b></dt><dd>O(1)</dd></dl>
     *
     * @return the number of directed edges in this graph
     */
    default int getEdgesCount() {
        int count = 0;
        for (Vertex v : this.getVertices()) {
            count += this.getOutEdges(v).size();
        }
        return count;
    }

    Map<Vertex, Edge> getOutEdges(Vertex v);

    Map<Vertex, Edge> getInEdges(Vertex v);

    /**
     * <p>Returns the sum of the outbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all outbound edges of vertex {@code v}
     * @see #getInWeightSum(Vertex)
     */
    default double getOutWeightSum(Vertex v) {
        return Helper.getWeightSum(this.getOutEdges(v).values());
    }

    /**
     * <p>Returns the sum of the inbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all inbound edges of vertex {@code v}
     * @see #getOutWeightSum(Vertex)
     */
    default double getInWeightSum(Vertex v) {
        return Helper.getWeightSum(this.getInEdges(v).values());
    }

    /**
     * <p>Returns the outbound degree of a vertex, aka the number of outbound edges. Edge to self is included (if
     * present).</p>
     *
     * @param v the vertex
     * @return the outbound degree of vertex {@code v}
     * @see #getInDegree(Vertex)
     */
    default int getOutDegree(Vertex v) {
        return this.getOutEdges(v).size();
    }

    /**
     * <p>Returns the inbound degree of a vertex, aka the number of inbound edges. Edge to self is included (if
     * present).</p>
     *
     * @param v the vertex
     * @return the inbound degree of vertex {@code v}
     * @see #getOutDegree(Vertex)
     */
    default int getInDegree(Vertex v) {
        return this.getInEdges(v).size();
    }

    /**
     * <p>Returns true if for every edge with source S and target T where S and T are different,
     * there is always an edge with source T and target S.</p>
     *
     * @return true if the graph is undirected, otherwise false
     */
    default boolean isUndirected() {
        // TODO: Implement
        /*ArrayList<Vertex[]> edgeList = new ArrayList<>();
        for (Edge e : this.edges) {
            Vertex v = e.getSource();
            Vertex w = e.getTarget();
            if (!v.equals(w)) {
                int indexOfOpposite = -1;
                for (int i = 0; i < edgeList.size(); i++) {
                    if (edgeList.get(i)[0].equals(w) && edgeList.get(i)[1].equals(v)) {
                        indexOfOpposite = i;
                        break;
                    }
                }
                if (indexOfOpposite > -1) {
                    edgeList.remove(indexOfOpposite);
                } else {
                    edgeList.add(new Vertex[]{v, w});
                }
            }
        }
        return edgeList.size() == 0;*/
        return true;
    }

    /**
     * <p>Returns an unmodifiable Set of vertices that this graph consists of.</p>
     * <dl><dt><b>Complexity:</b></dt><dd>O(1)</dd></dl>
     *
     * @return the list of vertices of this graph
     */
    Set<Vertex> getVertices();

    /**
     * <p>Returns the number of vertices in this graph. This method is faster than using getVertices().size()</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of vertices in this graph
     */
    default int getVerticesCount() {
        return this.getVertices().size();
    }

    default Vertex getRandomOutEdge(Vertex from, boolean weighted) {
        HashMap<Vertex, Double> weightMap = new HashMap<>();
        Map<Vertex, Edge> outEdges = this.getOutEdges(from);
        for (Map.Entry<Vertex, Edge> e : outEdges.entrySet()) {
            weightMap.put(e.getKey(), (weighted ? e.getValue().getWeight() : 1.0));
        }
        Set<Vertex> edges = Helper.weightedRandom(weightMap, 1);
        return edges.iterator().next();
    }

    default double getDiameter() {
        // TODO: Should return a list/path/walk of vertices to show both the weight sum and the steps
        HashMap<Vertex[], Double> distanceMap = new HashMap<>();

        for (Vertex v : this.getVertices()) {
            HashMap<Vertex, Double> temp = Dijkstra.execute(this, v);
            for (Map.Entry<Vertex, Double> e : temp.entrySet()) {
                distanceMap.put(new Vertex[]{v, e.getKey()}, e.getValue());
            }
        }

        double diameter = 0;
        for (Double d : distanceMap.values()) {
            if (d > diameter) {
                diameter = d;
            }
        }

        return diameter;
    }

    /**
     * <p>Inserts a new vertex to the graph and returns it. Use {@link #addVertices(int)} for bulk inserts.</p>
     * <dl><dt><b>Complexity:</b></dt><dd>O(1)</dd></dl>
     *
     * @return the new vertex object
     */
    default Vertex addVertex() {
        Vertex v = new Vertex();
        return this.addVertex(v);
    }

    Vertex addVertex(Vertex v);

    default Set<Vertex> addVertices(int count) {
        Set<Vertex> newVertices = new HashSet<>();
        for (int i = 0; i < count; i++) {
            newVertices.add(this.addVertex());
        }
        return Collections.unmodifiableSet(newVertices);
    }

    /**
     * <p>Removes a vertex from the graph. This method will also remove the inbound and outbound edges of that vertex.
     * </p>
     *
     * @param v the vertex to be removed
     * @return the current instance
     * @throws GraphException if {@code v} does not belong in the graph
     */
    Graph removeVertex(Vertex v);

    default Graph removeVertices(Collection<Vertex> vertices) {
        vertices.forEach(this::removeVertex);
        return this;
    }

    Graph clear();

    Edge addEdge(Vertex source, Vertex target);

    default Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected) {
        Set<Edge> addedEdges = new HashSet<>();
        addedEdges.add(this.addEdge(source, target));
        if (undirected) {
            addedEdges.add(this.addEdge(target, source));
        }
        return Collections.unmodifiableSet(addedEdges);
    }

    Graph removeEdge(Vertex source, Vertex target);
}
