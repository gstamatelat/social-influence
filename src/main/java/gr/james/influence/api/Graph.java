package gr.james.influence.api;

import gr.james.influence.algorithms.distance.Dijkstra;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.Helper;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.collections.VertexPair;
import gr.james.influence.util.exceptions.InvalidVertexException;

import java.util.*;

/**
 * <p>Represents a collection of vertices and edges. The graph is weighted, directed and there can't be more than one
 * edge from node {@code i} to node {@code j} (it's not a multigraph).</p>
 * <dl><dt><b>Vertices and edges:</b></dt><dd>Objects of type {@code <V>} are used as vertices in the graph as-is. This
 * behavior implies that such objects must be unique. Methods that accept vertex objects as arguments will automatically
 * (but not silently) handle duplicate values.<br><br>On the other hand, edge objects {@code <E>} are objects that
 * attach to higher level edges in the graph ({@link GraphEdge}) and, thus, need not be unique. In fact, edge objects
 * can also be {@code null}. It is the caller's responsibility to ensure uniqueness for edge objects if such behavior is
 * desired.<br><br>Both vertex and edge types need to be immutable or effectively immutable.</dd></dl>
 * <dl><dt><b>Collections returned:</b></dt><dd>Methods that return collections ({@link Map Maps}, {@link Set Sets} and
 * {@link List Lists}) return read-only views of the actual collections they represent, meaning that you can't insert,
 * remove or reorder elements. These collections may also not be backed by the graph, changes to the graph may not
 * affect these collections after they have been returned; you need to call the method again. This behavior depends on
 * the underlying {@code Graph} implementation.</dd></dl>
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface Graph<V, E> extends Iterable<V>, Metadata {
    GraphFactory<V, E> getGraphFactory();

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
    default boolean containsEdge(V source, V target) {
        return findEdge(source, target) != null;
    }

    /**
     * <p>Returns the {@link GraphEdge} from {@code source} to {@code target}, or {@code null} if there is no such
     * edge.</p>
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @return the {@link GraphEdge} from {@code source} to {@code target}, or {@code null} if there is no such edge
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} doesn't belong in the graph
     */
    default GraphEdge<V, E> findEdge(V source, V target) {
        return this.getOutEdges(source).get(Conditions.requireNonNullAndExists(target, this));
    }

    /**
     * <p>Calculates the total amount of directed edges that this graph has.</p>
     *
     * @return the number of directed edges in this graph
     */
    default int getEdgesCount() {
        int count = 0;
        for (V v : this.getVertices()) {
            count += this.getOutEdges(v).size();
        }
        return count;
    }

    /**
     * <p>Get all outbound edges of {@code v}. The result is a {@link Map} where the keys are the destination vertices
     * and the values are the respective {@link GraphEdge} objects.</p>
     *
     * @param v the vertex to get the outbound edges of
     * @return the outbound edges of {@code v} as a {@code Map<V, GraphEdge<V,E>>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} is not in the graph
     * @see #getInEdges
     */
    Map<V, GraphEdge<V, E>> getOutEdges(V v);

    /**
     * <p>Get all inbound edges of {@code v}. The result is a {@link Map} where the keys are the source vertices and the
     * values are the respective {@link GraphEdge} objects.</p>
     *
     * @param v the vertex to get the inbound edges of
     * @return the inbound edges of {@code v} as a {@code Map<Vertex, GraphEdge<V, E>>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} is not in the graph
     * @see #getOutEdges
     */
    Map<V, GraphEdge<V, E>> getInEdges(V v);

    /**
     * <p>Returns the sum of the outbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all outbound edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} is not in the graph
     * @see #getInStrength
     */
    default double getOutStrength(V v) {
        return this.getOutEdges(v).values().stream().mapToDouble(GraphEdge::getWeight).sum();
    }

    /**
     * <p>Returns the sum of the inbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all inbound edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} is not in the graph
     * @see #getOutStrength
     */
    default double getInStrength(V v) {
        return this.getInEdges(v).values().stream().mapToDouble(GraphEdge::getWeight).sum();
    }

    /**
     * <p>Returns the outbound degree of a vertex, aka the number of outbound edges. Edge to self is included (if
     * present).</p>
     *
     * @param v the vertex
     * @return the outbound degree of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} doesn't belong in the graph
     * @see #getInDegree
     */
    default int getOutDegree(V v) {
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
     * @see #getOutDegree
     */
    default int getInDegree(V v) {
        return this.getInEdges(v).size();
    }

    /**
     * <p>Returns an list view of the vertices contained in this graph. The list is indexed at the order at which the
     * vertices were inserted in the graph and will always contain distinct objects.</p>
     *
     * @return an unmodifiable list of vertices in this graph
     */
    List<V> getVertices();

    /**
     * <p>Checks if the graph contains the specified vertex. {@code containsVertex(v)} will return the same value as
     * {@code getVertices().contains(v)} but could be faster depending on the {@code Graph} implementation.</p>
     *
     * @param v the vertex to check whether it is contained in the graph
     * @return {@code true} if {@code v} exists in the graph, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    default boolean containsVertex(V v) {
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
     * <p>Get a {@link V} of this graph based on its index. Index is a deterministic, per-graph attribute between
     * {@code 0} (inclusive) and {@link #getVerticesCount()} (exclusive), indicating the order at which the vertices
     * were inserted in the graph. {@code getVertexFromIndex(i)} will return the same vertex as
     * {@code getVertices().get(i)} but could be faster depending on the {@code Graph} implementation.</p>
     *
     * @param index the index of the vertex
     * @return the vertex reference with the provided index
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= getVerticesCount()</tt>)
     */
    default V getVertexFromIndex(int index) {
        return this.getVertices().get(index);
    }

    /**
     * <p>Return a uniformly distributed random vertex of this graph.</p>
     *
     * @param r the {@link Random} instance to use for the operation
     * @return a random vertex contained in this graph or {@code null} if the graph is empty
     */
    @Deprecated
    default V getRandomVertex(Random r) {
        if (getVerticesCount() == 0) {
            return null;
        } else {
            return getVertexFromIndex(r.nextInt(getVerticesCount()));
        }
    }

    /**
     * <p>Return a uniformly distributed random vertex of this graph using the global random instance.</p>
     *
     * @return a random vertex contained in this graph or {@code null} if the graph is empty
     */
    @Deprecated
    default V getRandomVertex() {
        return getRandomVertex(RandomHelper.getRandom());
    }

    /**
     * <p>Get the read-only, index-based, vertex iterator for this graph. {@code iterator()} will return the same
     * iterator as {@code getVertices().iterator()} but could be faster depending on the {@code Graph} implementation.
     * </p>
     *
     * @return the index-based vertex iterator for this graph
     */
    default Iterator<V> iterator() {
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
    boolean addVertex(V v);

    /**
     * <p>Inserts a new unconnected vertex to the graph and returns it. Use {@link #addVertices(int)} for bulk inserts.
     * </p>
     *
     * @return the new vertex object
     * @throws UnsupportedOperationException if this method is not supported by this graph instance
     * @throws InvalidVertexException        if the automatically generated vertex was already in the graph; this
     *                                       behavior signals a flawed vertex factory
     * @throws NullPointerException          if the vertex factory generated {@code null}
     */
    default V addVertex() {
        if (getGraphFactory() == null) {
            throw new UnsupportedOperationException(Finals.E_GRAPH_NOT_SUPPORTED);
        }
        V v = getGraphFactory().createVertex();
        if (!this.addVertex(v)) {
            throw new InvalidVertexException(Finals.E_GRAPH_VERTEX_CONTAINED, v);
        }
        return v;
    }

    /**
     * <p>Insert a collection of vertices in the graph. If any of the vertices are already on the graph, they are
     * ignored.</p>
     *
     * @param vertices the vertices to insert to the graph
     * @return a {@link List} containing the vertices in {@code vertices} that were already in the graph
     * @throws NullPointerException if any vertex in {@code vertices} is {@code null}
     */
    default List<V> addVertices(V... vertices) {
        List<V> contained = new ArrayList<>();
        for (V v : vertices) {
            if (!this.addVertex(v)) {
                contained.add(v);
            }
        }
        return contained;
    }

    /**
     * <p>Insert {@code count} unconnected vertices in the graph.</p>
     *
     * @param count how many new vertices to add
     * @return an unmodifiable list view of the vertices in the order that they were added
     * @throws UnsupportedOperationException if this method is not supported by this graph instance
     * @throws InvalidVertexException        if any automatically generated vertex was already in the graph; this
     *                                       behavior signals a flawed vertex factory
     * @throws NullPointerException          if the vertex factory generated one {@code null} value
     */
    default List<V> addVertices(int count) {
        List<V> newVertices = new ArrayList<>();
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
    boolean removeVertex(V v);

    /**
     * <p>Removes a collection of vertices from the graph.</p>
     *
     * @param vertices a collection of vertices to remove from the graph; you should prefer a collection with a fast
     *                 iterator
     * @throws NullPointerException if any of the vertices in {@code vertices} is {@code null}
     */
    default void removeVertices(Collection<V> vertices) {
        vertices.forEach(this::removeVertex);
    }

    /**
     * <p>Removes all vertices (and consequently all edges) from this graph. Metadata are not removed.</p>
     */
    default void clear() {
        this.removeVertices(this.getVertices());
    }

    /**
     * <p>Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@link Finals#DEFAULT_EDGE_WEIGHT}. If an edge with the same {@code source} and {@code target} exists, nothing
     * happens.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException          if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException        if either {@code source} or {@code target} is not in the graph
     * @throws UnsupportedOperationException if this method is not supported by this graph instance
     */
    default GraphEdge<V, E> addEdge(V source, V target) {
        return addEdge(source, target, Finals.DEFAULT_EDGE_WEIGHT);
    }

    /**
     * <p>Creates an edge with the specified {@code source}, {@code target} and {@code weight}. If an edge with the
     * same {@code source} and {@code target} exists, nothing happens.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param weight the weight to be associated with the edge
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException          if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException        if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException      if {@code weight} is non-positive
     * @throws UnsupportedOperationException if this method is not supported by this graph instance
     */
    default GraphEdge<V, E> addEdge(V source, V target, double weight) {
        if (getGraphFactory() == null) {
            throw new UnsupportedOperationException(Finals.E_GRAPH_NOT_SUPPORTED);
        }
        return addEdge(source, target, getGraphFactory().createEdge(), weight);
    }

    /**
     * <p>Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@link Finals#DEFAULT_EDGE_WEIGHT}. Also attaches the object {@code edge} to the edge. If an edge with the same
     * {@code source} and {@code target} exists, nothing happens.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param edge   the object to attach to the edge
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} is not in the graph
     */
    default GraphEdge<V, E> addEdge(V source, V target, E edge) {
        return addEdge(source, target, edge, Finals.DEFAULT_EDGE_WEIGHT);
    }

    /**
     * <p>Creates an edge with the specified {@code source}, {@code target} and {@code weight}. Also attaches the
     * object {@code edge} to the edge. If an edge with the same {@code source} and {@code target} exists, nothing
     * happens.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param edge   the object to attach to the edge
     * @param weight the weight to be associated with the edge
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException   if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    GraphEdge<V, E> addEdge(V source, V target, E edge, double weight);

    /**
     * <p>Replaces the weight of the specified edge with {@code source} and {@code target} with {@code weight}. If an
     * edge with {@code source} and {@code target} doesn't exist, nothing will happen.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param weight the new weight to be associated with the edge
     * @return {@code true} if there was previously an edge with the specified {@code source} and {@code target} and
     * thus the weight could be changed, {@code false} otherwise
     * @throws NullPointerException     if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException   if either {@code source} or {@code target} doesn't belong in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    default boolean setEdgeWeight(V source, V target, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        GraphEdge<V, E> previousEdge = removeEdge(source, target);
        if (previousEdge != null) {
            GraphEdge<V, E> e = addEdge(source, target, previousEdge.getEdge(), weight);
            assert e != null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from
     * the operation. After the operation, a complete subgraph of {@code among} will be created. This method will only
     * create missing edges, existing ones will not be altered. If {@code among} only contains 2 (unique) vertices
     * {@code s} and {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If {@code among} only contains
     * 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among a collection of vertices of which each pair will be connected; you should prefer a collection with a
     *              fast iterator implementation
     * @throws NullPointerException          if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException        if any vertex in {@code among} doesn't belong in the graph
     * @throws UnsupportedOperationException if this method is not supported by this graph instance
     */
    default void addEdges(Collection<V> among) {
        for (V v : among) {
            for (V u : among) {
                if (!v.equals(u)) {
                    addEdge(v, u);
                }
            }
        }
        assert Graphs.subGraph(this, this.getGraphFactory(), among)
                .getEdgesCount() == among.size() * (among.size() - 1);
    }

    /**
     * <p>Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from
     * the operation. After the operation, a complete subgraph of {@code among} will be created. This method will only
     * create missing edges, existing ones will not be altered. If {@code among} only contains 2 (unique) vertices
     * {@code s} and {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If {@code among} only contains
     * 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among the vertices as variable arguments to connect each of its pairs; you should prefer a collection with
     *              a fast iterator implementation
     * @throws NullPointerException          if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException        if any vertex in {@code among} doesn't belong in the graph
     * @throws UnsupportedOperationException if this method is not supported by this graph instance
     */
    default void addEdges(V... among) {
        this.addEdges(Arrays.asList(among));
    }

    /**
     * <p>Remove the edge with the specified {@code source} and {@code target}, if it exists.</p>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return the {@link GraphEdge} that was removed if there was previously a directed edge
     * {@code (source,target)}, otherwise {@code null}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} is not in the graph
     */
    GraphEdge<V, E> removeEdge(V source, V target);

    /**
     * <p>Removes all the (existing) edges of which both the source and the target are contained in {@code among}.
     * Self-loops are excluded from the operation. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be removed (assuming they exist). If {@code among} only
     * contains 1 (unique) vertex or less, it's a no-op.</p>
     *
     * @param among a collection of vertices to strip the edges from; you should prefer a collection with a fast
     *              iterator
     * @throws NullPointerException   if any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} is not in the graph
     */
    default void removeEdges(Collection<V> among) {
        for (V v : among) {
            for (V u : among) {
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
    default void removeEdges(V... among) {
        this.removeEdges(Arrays.asList(among));
    }

    @Deprecated
    default V getRandomOutEdge(V from, boolean weighted) {
        // TODO: Return GraphEdge
        // TODO: What if no out edge?
        Map<V, Double> weightMap = new HashMap<>();
        Map<V, GraphEdge<V, E>> outEdges = this.getOutEdges(from);
        for (Map.Entry<V, GraphEdge<V, E>> e : outEdges.entrySet()) {
            weightMap.put(e.getKey(), (weighted ? e.getValue().getWeight() : 1.0));
        }
        Set<V> edges = Helper.weightedRandom(weightMap, 1);
        return edges.iterator().next();
    }

    @Deprecated
    default double getDensity() {
        double n = this.getVerticesCount();
        double e = this.getEdgesCount();
        return e / (n * (n - 1));
    }

    @Deprecated
    default double getAveragePathLength() {
        Map<VertexPair<V>, Double> distanceMap = Dijkstra.executeDistanceMap(this);

        double sum = 0;
        for (double d : distanceMap.values()) {
            sum += d;
        }

        return sum / (this.getVerticesCount() * (this.getVerticesCount() - 1));
    }

    /**
     * <p>Get a collection of all edges in this graph. The items are of type {@link GraphEdge} and are in no particular
     * order inside the collection.</p>
     *
     * @return a collection of all edges in the graph
     */
    default Collection<GraphEdge<V, E>> getEdges() {
        Collection<GraphEdge<V, E>> edges = new LinkedHashSet<>();
        for (V v : this.getVertices()) {
            edges.addAll(this.getOutEdges(v).values());
        }
        return Collections.unmodifiableCollection(edges);
    }
}
