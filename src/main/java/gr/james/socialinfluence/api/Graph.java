package gr.james.socialinfluence.api;

import gr.james.socialinfluence.algorithms.distance.Dijkstra;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.RandomHelper;
import gr.james.socialinfluence.util.collections.VertexPair;
import gr.james.socialinfluence.util.exceptions.InvalidVertexException;

import java.util.*;

/**
 * <p>Represents a collection of vertices and edges. The graph is weighted, directed and there can't be more than one
 * edge from node {@code i} to node {@code j} (it's not a multigraph).</p>
 * <dl><dt><b>Collections returned:</b></dt><dd>Methods that return collections ({@link Map Maps}, {@link Set Sets} and
 * {@link List Lists}) return read-only views of the actual collections they represent, meaning that you can't insert,
 * remove or reorder elements. These collections may also not be backed by the graph, changes to the graph may not
 * affect these collections after they have been returned; you need to call the method again. This behavior depends on
 * the underlying {@code Graph} implementation.</dd></dl>
 */
public interface Graph extends Iterable<Vertex>, Metadata {
    default String getGraphType() {
        return getMeta(Finals.TYPE_META);
    }

    default void setGraphType(String type) {
        setMeta(Finals.TYPE_META, type);
    }

    /**
     * <p>Checks if this graph contains an edge with the specified {@code source} and {@code target}.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return {@code true} if an edge with the specified {@code source} and {@code target} exists, otherwise
     * {@code false}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} doesn't belong in the graph
     */
    default boolean containsEdge(Vertex source, Vertex target) {
        return findEdge(source, target) != null;
    }

    /**
     * <p>Returns the {@code Edge} from {@code source} to {@code target}, or {@code null} if there is no such edge.</p>
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @return the {@code Edge} from {@code source} to {@code target}, or {@code null} if there is no such edge
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} doesn't belong in the graph
     */
    default Edge findEdge(Vertex source, Vertex target) {
        return this.getOutEdges(source).get(Conditions.requireNonNullAndExists(target, this));
    }

    /**
     * <p>Return a uniformly distributed random vertex of this graph.</p>
     *
     * @return a random vertex contained in this graph
     */
    default Vertex getRandomVertex() {
        // TODO: Return null or exception if the graph is empty
        return getVertexFromIndex(RandomHelper.getRandom().nextInt(getVerticesCount()));
    }

    /**
     * <p>Calculates the total amount of directed edges that this graph has.</p>
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

    /**
     * <p>Get all outbound edges of {@code v}. The result is a {@link Map} where the keys are the destination vertices
     * and the values are the respective {@link Edge} objects.</p>
     *
     * @param v the vertex to get the outbound edges of
     * @return the outbound edges of {@code v} as a {@code Map<Vertex, Edge>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
     * @see #getInEdges(Vertex)
     */
    Map<Vertex, Edge> getOutEdges(Vertex v);

    /**
     * <p>Get all inbound edges of {@code v}. The result is a {@link Map} where the keys are the source vertices and the
     * values are the respective {@link Edge} objects.</p>
     *
     * @param v the vertex to get the inbound edges of
     * @return the inbound edges of {@code v} as a {@code Map<Vertex, Edge>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
     * @see #getOutEdges(Vertex)
     */
    Map<Vertex, Edge> getInEdges(Vertex v);

    /**
     * <p>Returns the sum of the outbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all outbound edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
     * @see #getInStrength(Vertex)
     */
    default double getOutStrength(Vertex v) {
        return Helper.getWeightSum(this.getOutEdges(v).values());
    }

    /**
     * <p>Returns the sum of the inbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all inbound edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
     * @see #getOutStrength(Vertex)
     */
    default double getInStrength(Vertex v) {
        return Helper.getWeightSum(this.getInEdges(v).values());
    }

    /**
     * <p>Returns the outbound degree of a vertex, aka the number of outbound edges. Edge to self is included (if
     * present).</p>
     *
     * @param v the vertex
     * @return the outbound degree of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
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
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
     * @see #getOutDegree(Vertex)
     */
    default int getInDegree(Vertex v) {
        return this.getInEdges(v).size();
    }

    /**
     * <p>Returns an list view of the vertices contained in this graph. The list is indexed at the order at which the
     * vertices were inserted in the graph.</p>
     *
     * @return an unmodifiable list of vertices in this graph
     */
    List<Vertex> getVertices();

    /**
     * <p>Checks if the graph contains the specified vertex. {@code containsVertex(v)} will return the same value as
     * {@code getVertices().contains(v)} but could be faster depending on the {@code Graph} implementation.</p>
     *
     * @param v the {@link Vertex} to check whether it is contained in the graph
     * @return {@code true} if {@code v} exists in the graph, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    default boolean containsVertex(Vertex v) {
        return this.getVertices().contains(Conditions.requireNonNull(v));
    }

    /**
     * <p>Returns the number of vertices in this graph. {@code getVerticesCount()} will return the same value as
     * {@code getVertices().size()} but could be faster depending on the {@code Graph} implementation.</p>
     *
     * @return the number of vertices in this graph
     */
    default int getVerticesCount() {
        return this.getVertices().size();
    }

    /**
     * <p>Get a {@link Vertex} of this graph based on its index. Index is a deterministic, per-graph attribute between
     * {@code 0} (inclusive) and {@link #getVerticesCount()} (exclusive), indicating the order at which the vertices
     * were inserted in the graph. {@code getVertexFromIndex(i)} will return the same vertex as
     * {@code getVertices().get(i)} but could be faster depending on the {@code Graph} implementation.</p>
     *
     * @param index the index of the vertex
     * @return the vertex reference with the provided index
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= getVerticesCount()</tt>)
     */
    default Vertex getVertexFromIndex(int index) {
        return this.getVertices().get(index);
    }

    /**
     * <p>Get the read-only, index-based, vertex iterator for this graph. {@code iterator()} will return the same
     * iterator as {@code getVertices().iterator()} but could be faster depending on the {@code Graph} implementation.
     * </p>
     *
     * @return the index-based vertex iterator for this graph
     */
    default Iterator<Vertex> iterator() {
        return this.getVertices().iterator();
    }

    /**
     * <p>Insert the specified vertex {@code v} to the graph. If the vertex is already contained in the graph, this
     * method is a no-op.</p>
     *
     * @param v the vertex to insert to the graph
     * @return {@code false} if the graph previously already contained the vertex, otherwise {@code true}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    boolean addVertex(Vertex v);

    /**
     * <p>Inserts a new unconnected vertex to the graph and returns it. Use {@link #addVertices(int)} for bulk inserts.
     * </p>
     *
     * @return the new vertex object
     */
    default Vertex addVertex() {
        Vertex v = new Vertex();
        this.addVertex(v);
        return v;
    }

    /**
     * <p>Insert {@code count} unconnected vertices in the graph.</p>
     *
     * @param count how many new vertices to add
     * @return an unmodifiable list view of the vertices in the order that they were added
     */
    default List<Vertex> addVertices(int count) {
        List<Vertex> newVertices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newVertices.add(this.addVertex());
        }
        return Collections.unmodifiableList(newVertices);
    }

    /**
     * <p>Removes a vertex from the graph if it is present. This method will also remove the inbound and outbound edges
     * of that vertex.</p>
     *
     * @param v the vertex to be removed
     * @return {@code true} if the graph previously contained this vertex, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    boolean removeVertex(Vertex v);

    /**
     * <p>Removes a collection of vertices from the graph.</p>
     *
     * @param vertices a collection of vertices to remove from the graph; you should prefer a collection with a fast
     *                 {@code next()} implementation
     * @throws NullPointerException if any of the vertices in {@code vertices} is {@code null}
     */
    default void removeVertices(Collection<Vertex> vertices) {
        vertices.forEach(this::removeVertex);
    }

    /**
     * <p>Removes all vertices and all edges from this graph.</p>
     */
    default void clear() {
        this.removeVertices(this.getVertices());
    }

    /**
     * <p>Creates an edge with the specified {@code source} and {@code target}. If an edge with the same {@code source}
     * and {@code target} exists, nothing happens.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return the {@code Edge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} doesn't belong in the graph
     */
    Edge addEdge(Vertex source, Vertex target);

    /**
     * <p>Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from
     * the operation. After the operation, a complete subgraph of {@code among} will be created. If {@code among} only
     * contains 2 (unique) vertices {@code s} and {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If
     * {@code among} only contains 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among a collection of vertices of which each pair will be connected; you should prefer a collection with a
     *              fast {@code next()} implementation
     * @throws NullPointerException   if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} doesn't belong in the graph
     */
    default void addEdges(Collection<Vertex> among) {
        for (Vertex v : among) {
            for (Vertex u : among) {
                if (!v.equals(u)) {
                    this.addEdge(v, u);
                }
            }
        }
    }

    /**
     * <p>Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from
     * the operation. After the operation, a complete subgraph of {@code among} will be created. If {@code among} only
     * contains 2 (unique) vertices {@code s} and {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If
     * {@code among} only contains 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among the vertices as variable arguments to connect each of its pairs; you should prefer a collection with
     *              a fast {@code next()} implementation
     * @throws NullPointerException   if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} doesn't belong in the graph
     */
    default void addEdges(Vertex... among) {
        this.addEdges(Arrays.asList(among));
    }

    /**
     * <p>Remove the edge with the specified {@code source} and {@code target}, if it exists.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return {@code true} if there was previously a directed edge {@code (source,target)}, otherwise {@code false}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} doesn't belong in the graph
     */
    boolean removeEdge(Vertex source, Vertex target);

    /**
     * <p>Removes all the (existing) edges of which both the source and the target are contained in {@code among}.
     * Self-loops are excluded from the operation. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be removed (assuming they exist). If {@code among} only
     * contains 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among a collection of vertices to strip the edges from; you should prefer a collection with a fast
     *              {@code next()} implementation
     * @throws NullPointerException   if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} doesn't belong in the graph
     */
    default void removeEdges(Collection<Vertex> among) {
        for (Vertex v : among) {
            for (Vertex u : among) {
                if (!v.equals(u)) {
                    this.removeEdge(v, u);
                }
            }
        }
    }

    /**
     * <p>Removes all the (existing) edges of which both the source and the target are contained in {@code among}.
     * Self-loops are excluded from the operation. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be removed (assuming they exist). If {@code among} only
     * contains 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among the vertices as variable arguments to strip the edges from; you should prefer a collection with a
     *              fast {@code next()} implementation
     * @throws NullPointerException   if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} doesn't belong in the graph
     */
    default void removeEdges(Vertex... among) {
        this.removeEdges(Arrays.asList(among));
    }

    default Vertex getRandomOutEdge(Vertex from, boolean weighted) {
        // TODO: Documentation and probably return a pair or vertex and edge
        // TODO: What if no out edge?
        Map<Vertex, Double> weightMap = new HashMap<>();
        Map<Vertex, Edge> outEdges = this.getOutEdges(from);
        for (Map.Entry<Vertex, Edge> e : outEdges.entrySet()) {
            weightMap.put(e.getKey(), (weighted ? e.getValue().getWeight() : 1.0));
        }
        Set<Vertex> edges = Helper.weightedRandom(weightMap, 1);
        return edges.iterator().next();
    }

    default double getDiameter() {
        // TODO: Should return a list/path/walk of vertices to show both the weight sum and the steps
        Map<VertexPair, Double> distanceMap = Dijkstra.executeDistanceMap(this);

        double diameter = 0;
        for (double d : distanceMap.values()) {
            if (d > diameter) {
                diameter = d;
            }
        }

        return diameter;
    }

    @Deprecated
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
     * <p>Returns true if for every edge with source S and target T where S and T are different,
     * there is always an edge with source T and target S.</p>
     *
     * @return {@code true} if the graph is undirected, otherwise {@code false}
     */
    @Deprecated
    default boolean isUndirected() {
        // TODO: Implement
        /*ArrayList<VertexPair> edgeList = new ArrayList<>();
        for (VertexPair e : this.getEdges().keySet()) {
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
        return false;
    }
}
