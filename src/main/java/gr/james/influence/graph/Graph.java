package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalEdgeException;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.EdgesIterator;

import java.util.*;

/**
 * Represents a weighted and directed graph which can contain self loops but not parallel edges.
 * <p>
 * More formally, any ordered pair of endpoints may correspond to at most one edge.
 * <p>
 * An ordered pair {@code (a, b)} is a pair of objects where the order in which the objects appear in the pair is
 * significant: the ordered pair {@code (a, b)} is different from the ordered pair {@code (b, a)} unless {@code a = b}.
 * <h2>Vertices and edges</h2>
 * Objects of type {@code <V>} are used as vertices in the graph and each vertex is identified by the reference to this
 * object. Such objects must be unique in the scope of a graph. Methods that accept vertex objects as arguments will
 * automatically (but not silently) handle duplicate values.
 * <p>
 * On the other hand, edge objects {@code <E>} are objects that attach to higher level edges in the graph and, thus,
 * need not be unique. In fact, edge objects can also be {@code null}. It is the client's responsibility to ensure
 * uniqueness for edge objects if such behavior is desired.
 * <p>
 * Both vertex and edge types {@code <V>} and {@code <E>} need to be immutable or effectively immutable.
 * <h2>Collections returned</h2>
 * Methods that return collections ({@link Map Maps}, {@link Set Sets} and {@link List Lists}) return read-only views of
 * the actual collections they represent, meaning that you can't insert, remove or reorder elements. These collections
 * may or may not be backed by the graph so changes to the graph may not reflect on collections after they have been
 * returned; you need to call the method again.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface Graph<V, E> extends Iterable<V>, Metadata {
    /*static <V, E> Graph<V, E> create() {
        return new MemoryGraph<>();
    }

    static <V, E> Graph<V, E> create(int expectedVertexCount) {
        return new MemoryGraph<>(expectedVertexCount);
    }*/

    /*static <V, E> GraphFactory<Graph<V, E>, V, E> factory() {
        return new GraphFactory<Graph<V, E>, V, E>() {
            @Override
            public Graph<V, E> createGraph() {
                return new MemoryGraph<>();
            }

            @Override
            public Graph<V, E> createGraph(int expectedVertexCount) {
                return new MemoryGraph<>(expectedVertexCount);
            }
        };
    }*/

    /*GraphFactory<? extends Graph<V, E>, V, E> factory();*/

    /**
     * Returns the type of this graph.
     *
     * @return the type of this graph
     */
    GraphType type();

    /*Graph<V, E> create();*/

    /**
     * Checks if this graph contains an edge with the specified {@code source} and {@code target}.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * findEdge(source, target) != null
     * </code></pre>
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return {@code true} if an edge with the specified {@code source} and {@code target} exists, otherwise
     * {@code false}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    default boolean containsEdge(V source, V target) {
        return findEdge(source, target) != null;
    }

    /**
     * Returns the {@link DirectedEdge} from {@code source} to {@code target}, or {@code null} if there is no such edge.
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @return the {@link DirectedEdge} from {@code source} to {@code target}, or {@code null} if there is no such edge
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    DirectedEdge<V, E> findEdge(V source, V target);

    /**
     * Returns the weight of the edge from {@code source} to {@code target} if it exists, or throws exception if it
     * doesn't exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final DirectedEdge&lt;V, E&gt; edge = findEdge(source, target);
     * if (edge == null) {
     *     throw new IllegalEdgeException();
     * }
     * return edge.weight();
     * </code></pre>
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @return the weight of the edge from {@code source} to {@code target}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     * @throws IllegalEdgeException   if there is no edge from {@code source} to {@code target}
     */
    default double getWeight(V source, V target) {
        final DirectedEdge<V, E> edge = findEdge(source, target);
        if (edge == null) {
            throw new IllegalEdgeException();
        } else {
            assert edge.weight() > 0;
            return edge.weight();
        }
    }

    /**
     * Returns the weight of the edge from {@code source} to {@code target} if it exists, or {@code other} if it doesn't
     * exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final DirectedEdge&lt;V, E&gt; edge = findEdge(source, target);
     * if (edge == null) {
     *     return other;
     * }
     * return edge.weight();
     * </code></pre>
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @param other  the default weight if no edge exists
     * @return the weight of the edge from {@code source} to {@code target}, or {@code other} if there is no such edge
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    default double getWeightElse(V source, V target, double other) {
        final DirectedEdge<V, E> edge = findEdge(source, target);
        if (edge == null) {
            return other;
        } else {
            assert edge.weight() > 0;
            return edge.weight();
        }
    }

    /**
     * Returns the object of the edge from {@code source} to {@code target} if it exists, or throws exception if it
     * doesn't exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final DirectedEdge&lt;V, E&gt; edge = findEdge(source, target);
     * if (edge == null) {
     *     throw new IllegalEdgeException();
     * }
     * return edge.edge();
     * </code></pre>
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @return the object of the edge from {@code source} to {@code target}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     * @throws IllegalEdgeException   if there is no edge from {@code source} to {@code target}
     */
    default E getEdge(V source, V target) {
        final DirectedEdge<V, E> edge = findEdge(source, target);
        if (edge == null) {
            throw new IllegalEdgeException();
        } else {
            return edge.value();
        }
    }

    /**
     * Returns the object of the edge from {@code source} to {@code target} if it exists, or {@code other} if it doesn't
     * exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final DirectedEdge&lt;V, E&gt; edge = findEdge(source, target);
     * if (edge == null) {
     *     return other;
     * }
     * return edge.edge();
     * </code></pre>
     *
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @param other  the default object if no edge exists
     * @return the object of the edge from {@code source} to {@code target}, or {@code other} if there is no such edge
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    default E getEdgeElse(V source, V target, E other) {
        final DirectedEdge<V, E> edge = findEdge(source, target);
        if (edge == null) {
            return other;
        } else {
            return edge.value();
        }
    }

    /**
     * Get all outbound edges of {@code v}. The result is a {@link Map} where the keys are the destination vertices
     * and the values are the respective {@link DirectedEdge} objects.
     *
     * @param v the vertex to get the outbound edges of
     * @return the outbound edges of {@code v} as a {@code Map<V, DirectedEdge<V,E>>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #getInEdges
     */
    @Deprecated
    Map<V, DirectedEdge<V, E>> getOutEdges(V v);

    /**
     * Gets an {@link Set} of all outbound edges of {@code v}.
     *
     * @param v the vertex to get the outbound edges of
     * @return the outbound edges of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    Set<DirectedEdge<V, E>> outEdges(V v);

    /**
     * Get all outbound incident vertices of {@code v}.
     * <p>
     * Complexity: O(1)
     *
     * @param v the vertex to get the outbound incident vertices of
     * @return the outbound incident vertices of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #adjacentIn(Object)
     */
    Set<V> adjacentOut(V v);

    /**
     * Get all inbound edges of {@code v}. The result is a {@link Map} where the keys are the source vertices and the
     * values are the respective {@link DirectedEdge} objects.
     *
     * @param v the vertex to get the inbound edges of
     * @return the inbound edges of {@code v} as a {@code Map<Vertex, DirectedEdge<V, E>>}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #getOutEdges
     */
    @Deprecated
    Map<V, DirectedEdge<V, E>> getInEdges(V v);

    /**
     * Gets an {@link Set} of all inbound edges of {@code v}.
     *
     * @param v the vertex to get the inbound edges of
     * @return the inbound edges of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    Set<DirectedEdge<V, E>> inEdges(V v);

    /**
     * Get all inbound incident vertices of {@code v}.
     * <p>
     * Complexity: O(1)
     *
     * @param v the vertex to get the inbound incident vertices of
     * @return the inbound incident vertices of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #adjacentOut(Object)
     */
    Set<V> adjacentIn(V v);

    /**
     * Get an {@link Iterable} of all edges in this graph.
     * <p>
     * This method uses a lazy approach on populating the returned {@link Iterable} and is suitable to use in a
     * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/language/foreach.html">For-Each Loop</a>.
     * <p>
     * The edges inside the returned {@link Iterable} are in no particular order.
     * <p>
     * The {@link Iterable} that is returned is exhausted in time {@code O(V+E)} and uses constant extra space.
     *
     * @return an {@link Iterable} of all the edges in this graph
     */
    default Iterable<DirectedEdge<V, E>> edges() {
        return () -> new EdgesIterator<>(Graph.this);
    }

    /**
     * Returns the sum of the outbound edge weights of a vertex.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * double sum = 0;
     * for (DirectedEdge&lt;V, E&gt; e : outEdges(v)) {
     *     sum += e.weight();
     * }
     * return sum;
     * </code></pre>
     *
     * @param v the vertex
     * @return the sum of weights of all outbound edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #inStrength
     */
    default double outStrength(V v) {
        double sum = 0;
        for (DirectedEdge<V, E> e : outEdges(v)) {
            sum += e.weight();
        }
        return sum;
    }

    /**
     * Returns the sum of the inbound edge weights of a vertex.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * double sum = 0;
     * for (DirectedEdge&lt;V, E&gt; e : inEdges(v)) {
     *     sum += e.weight();
     * }
     * return sum;
     * </code></pre>
     *
     * @param v the vertex
     * @return the sum of weights of all inbound edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #outStrength(Object)
     */
    default double inStrength(V v) {
        double sum = 0;
        for (DirectedEdge<V, E> e : inEdges(v)) {
            sum += e.weight();
        }
        return sum;
    }

    /**
     * Returns the outbound degree of a vertex, aka the number of outbound edges. Edge to self is included (if present).
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return adjacentOut(v).size();
     * </code></pre>
     *
     * @param v the vertex
     * @return the outbound degree of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #inDegree
     */
    default int outDegree(V v) {
        return adjacentOut(v).size();
    }

    /**
     * Returns the inbound degree of a vertex, aka the number of inbound edges. Edge to self is included (if present).
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return adjacentIn(v).size();
     * </code></pre>
     *
     * @param v the vertex
     * @return the inbound degree of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     * @see #outDegree
     */
    default int inDegree(V v) {
        return adjacentIn(v).size();
    }

    /**
     * Returns an list view of the vertices contained in this graph. The list is indexed at the order at which the
     * vertices were inserted in the graph and will always contain distinct objects.
     *
     * @return an unmodifiable list of vertices in this graph
     */
    @Deprecated
    List<V> getVertices();

    /**
     * Returns an unmodifiable {@link Set} of the vertices contained in this graph.
     * <p>
     * The vertices are in no particular order inside the {@link Set}.
     * <p>
     * Complexity: O(1)
     *
     * @return an unmodifiable {@link Set} of the vertices contained in this graph
     */
    Set<V> vertexSet();

    /**
     * Checks if the graph contains the specified vertex.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * if (v == null) {
     *     throw new NullPointerException();
     * }
     * return vertexSet().contains(v);
     * </code></pre>
     * <p>
     * Complexity: O(1)
     *
     * @param v the vertex to check whether it is contained in the graph
     * @return {@code true} if {@code v} exists in the graph, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    default boolean containsVertex(V v) {
        return this.vertexSet().contains(Conditions.requireNonNull(v));
    }

    /**
     * Returns the number of vertices in this graph.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return vertexSet().size();
     * </code></pre>
     * <p>
     * Complexity: O(1)
     *
     * @return the number of vertices in this graph
     */
    default int vertexCount() {
        return this.vertexSet().size();
    }

    /**
     * Get the read-only {@link Iterator} over the vertices of this graph.
     * <p>
     * The vertex iteration is performed in no particular order. This method is equivalent to
     * <pre><code>
     * return vertexSet().iterator();
     * </code></pre>
     * <p>
     * Complexity: O(1)
     *
     * @return the read-only {@link Iterator} over the vertices of this graph
     */
    @Override
    default Iterator<V> iterator() {
        return this.vertexSet().iterator();
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

    default V addVertex(VertexProvider<V> vertexProvider) {
        if (vertexProvider == null) {
            throw new NullPointerException();
        }
        final V v = vertexProvider.getVertex();
        if (!addVertex(v)) {
            throw new IllegalVertexException();
        }
        return v;
    }

    default List<V> addVertices(int n, VertexProvider<V> vertexProvider) {
        if (vertexProvider == null) {
            throw new NullPointerException();
        }
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        final List<V> contained = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            contained.add(addVertex(vertexProvider));
        }
        return Collections.unmodifiableList(contained);
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
        this.removeVertices(vertexSet());
    }

    /**
     * Removes all edges from this graph. Metadata are not removed.
     */
    default void clearEdges() {
        removeEdges(vertexSet());
        assert Graphs.getEdgesCount(this) == 0;
    }

    /**
     * Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@value Finals#DEFAULT_EDGE_WEIGHT}. The edge object will be assigned to {@code null}. If an edge with the same
     * {@code source} and {@code target} exists, nothing happens.
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return the {@link DirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if any of the arguments is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    default DirectedEdge<V, E> addEdge(V source, V target) {
        return addEdge(source, target, null, Finals.DEFAULT_EDGE_WEIGHT);
    }

    /**
     * Creates an edge with the specified {@code source}, {@code target} and {@code weight}. The edge object will be
     * assigned to {@code null}. If an edge with the same {@code source} and {@code target} exists, nothing happens.
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param weight the weight to be associated with the edge
     * @return the {@link DirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if any of the arguments is {@code null}
     * @throws IllegalVertexException   if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    default DirectedEdge<V, E> addEdge(V source, V target, double weight) {
        return addEdge(source, target, null, weight);
    }

    /**
     * Creates an edge with the specified {@code source} and {@code target} and default weight
     * {@value Finals#DEFAULT_EDGE_WEIGHT}. Also attaches the object {@code edge} to the edge. If an edge with the same
     * {@code source} and {@code target} exists, nothing happens.
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @param edge   the object to attach to the edge
     * @return the {@link DirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    default DirectedEdge<V, E> addEdge(V source, V target, E edge) {
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
     * @return the {@link DirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException   if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight);

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
     * @throws IllegalVertexException   if either {@code source} or {@code target} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    default boolean setEdgeWeight(V source, V target, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        DirectedEdge<V, E> previousEdge = removeEdge(source, target);
        if (previousEdge != null) {
            DirectedEdge<V, E> e = addEdge(source, target, previousEdge.value(), weight);
            assert e != null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from the
     * operation.
     * <p>
     * After the operation, a complete subgraph of {@code among} will be created. This method will only create missing
     * edges, existing ones will not be altered. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If {@code among} only contains 1 (unique)
     * vertex or less, it's a no-op.
     * <p>
     * The edge objects will be assigned to {@code null} and the edge weights to {@value Finals#DEFAULT_EDGE_WEIGHT}.
     *
     * @param among an {@link Iterable} of vertices of which each pair will be connected
     * @throws NullPointerException   if {@code among} or any vertex in {@code among} is {@code null}
     * @throws IllegalVertexException if any vertex in {@code among} is not in the graph
     */
    default void addEdges(Iterable<V> among) {
        for (V v : among) {
            for (V u : among) {
                if (!v.equals(u)) {
                    final DirectedEdge<V, E> edge = addEdge(v, u);
                    assert edge.source() == v;
                    assert edge.target() == u;
                    assert edge.value() == null;
                    assert edge.weight() == Finals.DEFAULT_EDGE_WEIGHT;
                }
            }
        }
    }

    /**
     * Connects every vertex in {@code among} with every other vertex in {@code among}; self-loops are excluded from the
     * operation.
     * <p>
     * After the operation, a complete subgraph of {@code among} will be created. This method will only create missing
     * edges, existing ones will not be altered. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be created. If {@code among} only contains 1 (unique)
     * vertex or less, it's a no-op.
     * <p>
     * The edge objects will be assigned to {@code null} and the edge weights to {@value Finals#DEFAULT_EDGE_WEIGHT}.
     *
     * @param among the vertices of which each pair will be connected
     * @throws NullPointerException   if {@code among} or any vertex in {@code among} is {@code null}
     * @throws IllegalVertexException if any vertex in {@code among} is not in the graph
     */
    default void addEdges(V... among) {
        this.addEdges(Arrays.asList(among));
    }

    /**
     * Remove the edge with the specified {@code source} and {@code target}, if it exists.
     *
     * @param source the source of the edge
     * @param target the target of the edge
     * @return the {@link DirectedEdge} that was removed if there was previously a directed edge
     * {@code (source,target)}, otherwise {@code null}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    DirectedEdge<V, E> removeEdge(V source, V target);

    /**
     * Removes all the (existing) edges of which both the source and the target are contained in {@code among}.
     * Self-loops are excluded from the operation. If {@code among} only contains 2 (unique) vertices {@code s} and
     * {@code t}, edges {@code (s,t)} and {@code (t,s)} will be removed (assuming they exist). If {@code among} only
     * contains 1 (unique) vertex or less, it's a no-op.
     *
     * @param among an {@link Iterable} of vertices to strip the edges from
     * @throws NullPointerException   if {@code among} or any vertex in {@code among} is {@code null}
     * @throws IllegalVertexException if any vertex in {@code among} is not in the graph
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
     * @throws IllegalVertexException if any vertex in {@code among} is not in the graph
     */
    default void removeEdges(V... among) {
        this.removeEdges(Arrays.asList(among));
    }

    /**
     * Returns a string representation of this graph.
     *
     * @return a string representation of this graph
     */
    @Override
    String toString();
}
