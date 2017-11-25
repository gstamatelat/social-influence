package gr.james.influence.api;

import gr.james.influence.annotation.AutoGeneratedVertex;
import gr.james.influence.exceptions.InvalidVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.Helper;
import gr.james.influence.util.RandomHelper;

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
    /**
     * Checks if this graph contains an edge with the specified {@code source} and {@code target}.
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
     * Returns the {@link GraphEdge} from {@code source} to {@code target}, or {@code null} if there is no such edge.
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
     * Returns the weight of the edge from {@code source} to {@code target} if it exists, or {@code other} if it doesn't
     * exist.
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @param other  the default weight if no edge exists
     * @return the weight of the edge from {@code source} to {@code target}, or {@code other} if there is no such edge
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} doesn't belong in the graph
     */
    default double getEdgeWeightElse(V source, V target, double other) {
        final GraphEdge<V, E> edge = findEdge(source, target);
        if (edge == null) {
            return other;
        } else {
            assert edge.getWeight() > 0;
            return edge.getWeight();
        }
    }

    /**
     * Calculates the total amount of directed edges that this graph has.
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
     * Get all outbound edges of {@code v}. The result is a {@link Map} where the keys are the destination vertices
     * and the values are the respective {@link GraphEdge} objects.
     *
     * @param v the vertex to get the outbound edges of
     * @return the outbound edges of {@code v} as a {@code Map<V, GraphEdge<V,E>>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} is not in the graph
     * @see #getInEdges
     */
    Map<V, GraphEdge<V, E>> getOutEdges(V v);

    /**
     * Get all inbound edges of {@code v}. The result is a {@link Map} where the keys are the source vertices and the
     * values are the respective {@link GraphEdge} objects.
     *
     * @param v the vertex to get the inbound edges of
     * @return the inbound edges of {@code v} as a {@code Map<Vertex, GraphEdge<V, E>>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws InvalidVertexException if {@code v} is not in the graph
     * @see #getOutEdges
     */
    Map<V, GraphEdge<V, E>> getInEdges(V v);

    /**
     * Returns the sum of the outbound edge weights of a vertex.
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
     * Returns the sum of the inbound edge weights of a vertex.
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
     * Returns the outbound degree of a vertex, aka the number of outbound edges. Edge to self is included (if present).
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
     * Returns the inbound degree of a vertex, aka the number of inbound edges. Edge to self is included (if present).
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
     * Returns an list view of the vertices contained in this graph. The list is indexed at the order at which the
     * vertices were inserted in the graph and will always contain distinct objects.
     *
     * @return an unmodifiable list of vertices in this graph
     */
    List<V> getVertices();

    /**
     * Checks if the graph contains the specified vertex. {@code containsVertex(v)} will return the same value as
     * {@code getVertices().contains(v)} but could be faster depending on the {@code Graph} implementation.
     *
     * @param v the vertex to check whether it is contained in the graph
     * @return {@code true} if {@code v} exists in the graph, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    default boolean containsVertex(V v) {
        return this.getVertices().contains(Conditions.requireNonNull(v));
    }

    /**
     * Returns the number of vertices in this graph. {@code getVerticesCount()} will return the same value as
     * {@code getVertices().size()} but could be faster depending on the {@code Graph} implementation.
     *
     * @return the number of vertices in this graph
     */
    default int getVerticesCount() {
        return this.getVertices().size();
    }

    /**
     * Get a vertex of this graph based on its index. Index is a deterministic, per-graph attribute between {@code 0}
     * (inclusive) and {@link #getVerticesCount()} (exclusive), indicating the order at which the vertices were
     * inserted in the graph. {@code getVertexFromIndex(i)} will return the same vertex as {@code getVertices().get(i)}
     * but could be faster depending on the {@code Graph} implementation.
     *
     * @param index the index of the vertex
     * @return the vertex reference with the provided index
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= getVerticesCount()})
     */
    default V getVertexFromIndex(int index) {
        return this.getVertices().get(index);
    }

    /**
     * Return a uniformly distributed random vertex of this graph.
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
     * Return a uniformly distributed random vertex of this graph using the global random instance.
     *
     * @return a random vertex contained in this graph or {@code null} if the graph is empty
     */
    @Deprecated
    default V getRandomVertex() {
        return getRandomVertex(RandomHelper.getRandom());
    }

    /**
     * Get the read-only, index-based, vertex iterator for this graph. {@code iterator()} will return the same iterator
     * as {@code getVertices().iterator()} but could be faster depending on the {@code Graph} implementation.
     *
     * @return the index-based vertex iterator for this graph
     */
    default Iterator<V> iterator() {
        return this.getVertices().iterator();
    }

    /**
     * Insert the specified vertex {@code v} to the graph. If the vertex is already contained in the graph, this method
     * is a no-op.
     *
     * @param v the vertex to insert to the graph
     * @return {@code false} if the graph previously already contained the vertex, otherwise {@code true}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    boolean addVertex(V v);

    /**
     * Inserts a new unconnected vertex to the graph and returns it. Use {@link #addVertices(int)} for bulk inserts.
     *
     * @param vertexProvider the vertex provider to use when generating a new vertex instance
     * @return the new vertex object
     * @throws InvalidVertexException if the automatically generated vertex was already in the graph; this
     *                                behavior signals a flawed vertex provider
     * @throws NullPointerException   if {@code vertexProvider} is null or it generated {@code null}
     */
    default V addVertex(VertexProvider<V> vertexProvider) {
        V v = vertexProvider.getVertex();
        if (!this.addVertex(v)) {
            throw new InvalidVertexException(Finals.E_GRAPH_VERTEX_CONTAINED, v);
        }
        return v;
    }

    @AutoGeneratedVertex
    default V addVertex() {
        throw new UnsupportedOperationException();
    }

    /**
     * Insert a group of vertices in the graph. If any of the vertices are already on the graph, they are ignored.
     *
     * @param vertices the vertices to insert to the graph
     * @return an unmodifiable list view containing the vertices in {@code vertices} that were already in the graph
     * @throws NullPointerException if any vertex in {@code vertices} is {@code null}
     */
    default List<V> addVertices(V... vertices) {
        final List<V> contained = new ArrayList<>();
        for (V v : vertices) {
            if (!this.addVertex(v)) {
                contained.add(v);
            }
        }
        return Collections.unmodifiableList(contained);
    }

    /**
     * Insert a group of vertices in the graph. If any of the vertices are already on the graph, they are ignored.
     *
     * @param vertices an {@link Iterable} of vertices to insert to the graph
     * @return an unmodifiable list view containing the vertices in {@code vertices} that were already in the graph
     * @throws NullPointerException if any vertex in {@code vertices} is {@code null}
     */
    default List<V> addVertices(Iterable<V> vertices) {
        final List<V> contained = new ArrayList<>();
        for (V v : vertices) {
            if (!this.addVertex(v)) {
                contained.add(v);
            }
        }
        return Collections.unmodifiableList(contained);
    }

    /**
     * Insert {@code count} unconnected vertices in the graph.
     *
     * @param count          how many new vertices to add
     * @param vertexProvider the vertex provider to use when generating a new vertex instance
     * @return an unmodifiable list view of the vertices in the order that they were added
     * @throws InvalidVertexException if any automatically generated vertex was already in the graph; this
     *                                behavior signals a flawed vertex provider
     * @throws NullPointerException   if {@code vertexProvider} is null or any generated vertex was {@code null}
     */
    default List<V> addVertices(int count, VertexProvider<V> vertexProvider) {
        List<V> newVertices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newVertices.add(this.addVertex(vertexProvider));
        }
        return Collections.unmodifiableList(newVertices);
    }

    @AutoGeneratedVertex
    default List<V> addVertices(int count) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes a vertex from the graph if it is present. This method will also remove the inbound and outbound edges of
     * that vertex.
     *
     * @param v the vertex to be removed
     * @return {@code true} if the graph previously contained this vertex, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    boolean removeVertex(V v);

    /**
     * Removes a group of vertices from the graph. Vertices in the group that are not in the graph are silently ignored.
     *
     * @param vertices an {@link Iterable} of vertices to remove from the graph
     * @throws NullPointerException if {@code vertices} or any of the objects in {@code vertices} is {@code null}
     */
    default void removeVertices(Iterable<V> vertices) {
        vertices.forEach(this::removeVertex);
    }

    /**
     * Removes all vertices (and consequently all edges) from this graph. Metadata are not removed.
     */
    default void clear() {
        this.removeVertices(this.getVertices());
    }

    /**
     * Removes all edges from this graph. Metadata are not removed.
     */
    default void clearEdges() {
        removeEdges(getVertices());
        assert getEdgesCount() == 0;
    }

    /**
     * Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@link Finals#DEFAULT_EDGE_WEIGHT}. If an edge with the same {@code source} and {@code target} exists, nothing
     * happens.
     *
     * @param source       the source of the edge
     * @param target       the target of the edge
     * @param edgeProvider the edge provider to use when generating a new edge instance
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if any of the arguments is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} is not in the graph
     */
    @Deprecated
    default GraphEdge<V, E> addEdge(V source, V target, EdgeProvider<E> edgeProvider) {
        return addEdge(source, target, Finals.DEFAULT_EDGE_WEIGHT, edgeProvider);
    }

    /**
     * Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@link Finals#DEFAULT_EDGE_WEIGHT}. The edge object will be assigned to {@code null}. If an edge with the same
     * {@code source} and {@code target} exists, nothing happens.
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if any of the arguments is {@code null}
     * @throws InvalidVertexException if either {@code source} or {@code target} is not in the graph
     */
    default GraphEdge<V, E> addEdge(V source, V target) {
        return addEdge(source, target, null, Finals.DEFAULT_EDGE_WEIGHT);
    }

    /**
     * Creates an edge with the specified {@code source}, {@code target} and {@code weight}. If an edge with the same
     * {@code source} and {@code target} exists, nothing happens.
     *
     * @param source       the source of the edge
     * @param target       the target of the edge
     * @param weight       the weight to be associated with the edge
     * @param edgeProvider the edge provider to use when generating a new edge instance
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if either {@code source}, {@code target} or {@code edgeProvider} is {@code null}
     * @throws InvalidVertexException   if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    @Deprecated
    default GraphEdge<V, E> addEdge(V source, V target, double weight, EdgeProvider<E> edgeProvider) {
        return addEdge(source, target, edgeProvider.getEdge(), weight);
    }

    /**
     * Creates an edge with the specified {@code source}, {@code target} and {@code weight}. The edge object will be
     * assigned to {@code null}. If an edge with the same {@code source} and {@code target} exists, nothing happens.
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param weight the weight to be associated with the edge
     * @return the {@link GraphEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if any of the arguments is {@code null}
     * @throws InvalidVertexException   if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    default GraphEdge<V, E> addEdge(V source, V target, double weight) {
        return addEdge(source, target, null, weight);
    }

    /**
     * Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@link Finals#DEFAULT_EDGE_WEIGHT}. Also attaches the object {@code edge} to the edge. If an edge with the same
     * {@code source} and {@code target} exists, nothing happens.
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
     * Creates an edge with the specified {@code source}, {@code target} and {@code weight}. Also attaches the object
     * {@code edge} to the edge. If an edge with the same {@code source} and {@code target} exists, nothing happens.
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
     * Replaces the weight of the specified edge with {@code source} and {@code target} with {@code weight}. If an edge
     * with {@code source} and {@code target} doesn't exist, nothing will happen.
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
     * Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from the
     * operation. After the operation, a complete subgraph of {@code among} will be created. This method will only
     * create missing edges, existing ones will not be altered. If {@code among} only contains 2 (unique) vertices
     * {@code s} and {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If {@code among} only contains
     * 1 (unique) vertex or less, it's a no-op.
     *
     * @param among        an {@link Iterable} of vertices of which each pair will be connected
     * @param edgeProvider the edge provider to use when generating a new edge instance
     * @throws NullPointerException   if {@code among} or any vertex in {@code among} or {@code edgeProvider} is
     *                                {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} isn't part of the graph
     */
    @Deprecated
    default void addEdges(Iterable<V> among, EdgeProvider<E> edgeProvider) {
        for (V v : among) {
            for (V u : among) {
                if (!v.equals(u)) {
                    addEdge(v, u, edgeProvider);
                }
            }
        }
    }

    default void addEdges(Iterable<V> among) {
        for (V v : among) {
            for (V u : among) {
                if (!v.equals(u)) {
                    addEdge(v, u);
                }
            }
        }
    }

    /**
     * Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from the
     * operation. After the operation, a complete subgraph of {@code among} will be created. This method will only
     * create missing edges, existing ones will not be altered. If {@code among} only contains 2 (unique) vertices
     * {@code s} and {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If {@code among} only contains
     * 1 (unique) vertex or less, it's a no-op.
     *
     * @param among        the vertices as variable arguments to connect each of its pairs; you should prefer a
     *                     collection with a fast iterator implementation
     * @param edgeProvider the edge provider to use when generating a new edge instance
     * @throws NullPointerException   if any vertex in {@code among} or {@code edgeProvider} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} doesn't belong in the graph
     */
    @Deprecated
    default void addEdges(EdgeProvider<E> edgeProvider, V... among) {
        this.addEdges(Arrays.asList(among), edgeProvider);
    }

    default void addEdges(V... among) {
        this.addEdges(Arrays.asList(among));
    }

    /**
     * Remove the edge with the specified {@code source} and {@code target}, if it exists.
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
     * Removes all the (existing) edges of which both the source and the target are contained in {@code among}.
     * Self-loops are excluded from the operation. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be removed (assuming they exist). If {@code among} only
     * contains 1 (unique) vertex or less, it's a no-op.
     *
     * @param among an {@link Iterable} of vertices to strip the edges from
     * @throws NullPointerException   if {@code among} or any vertex in {@code among} is {@code null}
     * @throws InvalidVertexException if any vertex in {@code among} is not in the graph
     */
    default void removeEdges(Iterable<V> among) {
        for (V v : among) {
            for (V u : among) {
                if (!v.equals(u)) {
                    this.removeEdge(v, u);
                }
            }
        }
    }

    /**
     * Removes all the (existing) edges of which both the source and the target are contained in {@code among}.
     * Self-loops are excluded from the operation. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be removed (assuming they exist). If {@code among} only
     * contains 1 (unique) vertex or less, it's a no-op.
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

    /**
     * Get a collection of all edges in this graph. The items are of type {@link GraphEdge} and are in no particular
     * order inside the collection.
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
