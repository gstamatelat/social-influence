package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalEdgeException;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public interface UndirectedGraph<V, E> extends Graph<V, E> {
    /**
     * Creates and returns a new empty {@link UndirectedGraph}.
     *
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return a new empty {@link UndirectedGraph}
     */
    static <V, E> UndirectedGraph<V, E> create() {
        return new UndirectedGraphImpl<>();
    }

    /**
     * Creates and returns a new empty {@link UndirectedGraph} with some expectation on the vertex count.
     *
     * @param expectedVertexCount the expected vertex count
     * @param <V>                 the vertex type
     * @param <E>                 the edge type
     * @return a new empty {@link UndirectedGraph}
     * @throws IllegalArgumentException if {@code expectedVertexCount} is negative
     */
    static <V, E> UndirectedGraph<V, E> create(int expectedVertexCount) {
        return new UndirectedGraphImpl<>(expectedVertexCount);
    }

    /**
     * Creates and returns a new {@link UndirectedGraph} from a copy of the given graph.
     *
     * @param g   the graph to copy
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return a new {@link UndirectedGraph} from a copy of {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    static <V, E> UndirectedGraph<V, E> create(UndirectedGraph<V, E> g) {
        return new UndirectedGraphImpl<>(g);
    }

    /**
     * Returns an unmodifiable decorator around this graph.
     * <p>
     * Invoking mutation methods on the resulting graph will result in {@link UnsupportedOperationException}.
     *
     * @return an unmodifiable decorator around this graph
     */
    default UndirectedGraph<V, E> asUnmodifiable() {
        return new UnmodifiableUndirectedGraph<>(this);
    }

    /**
     * Creates and returns an immutable graph from a copy of this graph.
     *
     * @return an immutable graph from a copy of this graph
     */
    default UndirectedGraph<V, E> toImmutable() {
        return create(this).asUnmodifiable();
    }

    default DirectedGraph<V, E> asDirected() {
        return new AbstractDirectedGraph<V, E>() {
            @Override
            public DirectedEdge<V, E> findEdge(V source, V target) {
                final UndirectedEdge<V, E> e = UndirectedGraph.this.findEdge(source, target);
                return DirectedEdge.from(e.value(), source, target, e.weight());
            }

            @Override
            public Set<DirectedEdge<V, E>> outEdges(V v) {
                final Set<UndirectedEdge<V, E>> edges = UndirectedGraph.this.edges(v);
                return new AbstractSet<DirectedEdge<V, E>>() {
                    @Override
                    public Iterator<DirectedEdge<V, E>> iterator() {
                        return new Iterator<DirectedEdge<V, E>>() {
                            private final Iterator<UndirectedEdge<V, E>> it = edges.iterator();

                            @Override
                            public boolean hasNext() {
                                return it.hasNext();
                            }

                            @Override
                            public DirectedEdge<V, E> next() {
                                final UndirectedEdge<V, E> e = it.next();
                                return DirectedEdge.from(e.value(), v, e.other(v), e.weight());
                            }
                        };
                    }

                    @Override
                    public int size() {
                        return edges.size();
                    }

                    @Override
                    public boolean contains(Object o) {
                        if (!(o instanceof DirectedEdge)) {
                            return false;
                        }
                        final DirectedEdge e = (DirectedEdge) o;
                        return edges.contains(UndirectedEdge.from(e.value(), e.source(), e.target(), e.weight()));
                    }
                };
            }

            @Override
            public Set<V> adjacentOut(V v) {
                return UndirectedGraph.this.adjacent(v);
            }

            @Override
            public Set<DirectedEdge<V, E>> inEdges(V v) {
                final Set<UndirectedEdge<V, E>> edges = UndirectedGraph.this.edges(v);
                return new AbstractSet<DirectedEdge<V, E>>() {
                    @Override
                    public Iterator<DirectedEdge<V, E>> iterator() {
                        return new Iterator<DirectedEdge<V, E>>() {
                            private final Iterator<UndirectedEdge<V, E>> it = edges.iterator();

                            @Override
                            public boolean hasNext() {
                                return it.hasNext();
                            }

                            @Override
                            public DirectedEdge<V, E> next() {
                                final UndirectedEdge<V, E> e = it.next();
                                return DirectedEdge.from(e.value(), e.other(v), v, e.weight());
                            }
                        };
                    }

                    @Override
                    public int size() {
                        return edges.size();
                    }

                    @Override
                    public boolean contains(Object o) {
                        if (!(o instanceof DirectedEdge)) {
                            return false;
                        }
                        final DirectedEdge e = (DirectedEdge) o;
                        return edges.contains(UndirectedEdge.from(e.value(), e.source(), e.target(), e.weight()));
                    }
                };
            }

            @Override
            public Set<V> adjacentIn(V v) {
                return UndirectedGraph.this.adjacent(v);
            }

            @Override
            public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
                throw new UnsupportedOperationException();
            }

            @Override
            public DirectedEdge<V, E> removeEdge(V source, V target) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Set<V> vertexSet() {
                return UndirectedGraph.this.vertexSet();
            }

            @Override
            public boolean addVertex(V v) {
                return UndirectedGraph.this.addVertex(v);
            }

            @Override
            public boolean removeVertex(V v) {
                return UndirectedGraph.this.removeVertex(v);
            }
        };
    }

    /**
     * Checks if this graph contains an edge connecting {@code v} and {@code w}.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * findEdge(v, w) != null
     * </code></pre>
     *
     * @param v one end of the egde
     * @param w the other end of the edge
     * @return {@code true} if an edge connecting {@code v} and {@code w} exists, otherwise {@code false}
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     */
    default boolean containsEdge(V v, V w) {
        return findEdge(v, w) != null;
    }

    /**
     * Returns the {@link UndirectedEdge} connecting {@code v} and {@code w}, or {@code null} if there is no such edge.
     *
     * @param v one end of the egde
     * @param w the other end of the edge
     * @return the {@link UndirectedEdge} connecting {@code v} and {@code w}, or {@code null} if there is no such edge
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     */
    UndirectedEdge<V, E> findEdge(V v, V w);

    /**
     * Returns the weight of the edge connecting {@code v} and {@code w} if it exists, or throws exception if it doesn't
     * exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final UndirectedEdge&lt;V, E&gt; edge = findEdge(v, w);
     * if (edge == null) {
     *     throw new IllegalEdgeException();
     * }
     * return edge.weight();
     * </code></pre>
     *
     * @param v one end of the egde
     * @param w the other end of the edge
     * @return the weight of the edge connecting {@code v} and {@code w}
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     * @throws IllegalEdgeException   if there is no edge connecting {@code v} and {@code w}
     */
    default double getWeight(V v, V w) {
        final UndirectedEdge<V, E> edge = findEdge(v, w);
        if (edge == null) {
            throw new IllegalEdgeException();
        } else {
            assert edge.weight() > 0;
            return edge.weight();
        }
    }

    /**
     * Returns the weight of the edge connecting {@code v} and {@code w} if it exists, or {@code other} if it doesn't
     * exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final UndirectedEdge&lt;V, E&gt; edge = findEdge(v, w);
     * if (edge == null) {
     *     return other;
     * }
     * return edge.weight();
     * </code></pre>
     *
     * @param v     one end of the egde
     * @param w     the other end of the edge
     * @param other the default weight if no edge exists
     * @return the weight of the edge connecting {@code v} and {@code w}, or {@code other} if there is no such edge
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     */
    default double getWeightElse(V v, V w, double other) {
        final UndirectedEdge<V, E> edge = findEdge(v, w);
        if (edge == null) {
            return other;
        } else {
            assert edge.weight() > 0;
            return edge.weight();
        }
    }

    /**
     * Returns the object of the edge connecting {@code v} and {@code w} if it exists, or throws exception if it
     * doesn't exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final UndirectedEdge&lt;V, E&gt; edge = findEdge(v, w);
     * if (edge == null) {
     *     throw new IllegalEdgeException();
     * }
     * return edge.edge();
     * </code></pre>
     *
     * @param v one end of the egde
     * @param w the other end of the edge
     * @return the object of the edge connecting {@code v} and {@code w}
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     * @throws IllegalEdgeException   if there is no edge connecting {@code v} and {@code w}
     */
    default E getEdge(V v, V w) {
        final UndirectedEdge<V, E> edge = findEdge(v, w);
        if (edge == null) {
            throw new IllegalEdgeException();
        } else {
            return edge.value();
        }
    }

    /**
     * Returns the object of the edge connecting {@code v} and {@code w} if it exists, or {@code other} if it doesn't
     * exist.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * final UndirectedEdge&lt;V, E&gt; edge = findEdge(v, w);
     * if (edge == null) {
     *     return other;
     * }
     * return edge.edge();
     * </code></pre>
     *
     * @param v     one end of the egde
     * @param w     the other end of the edge
     * @param other the default object if no edge exists
     * @return the object of the edge connecting {@code v} and {@code w}, or {@code other} if there is no such edge
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     */
    default E getEdgeElse(V v, V w, E other) {
        final UndirectedEdge<V, E> edge = findEdge(v, w);
        if (edge == null) {
            return other;
        } else {
            return edge.value();
        }
    }

    /**
     * Gets an {@link Set} of all edges of {@code v}.
     *
     * @param v the vertex to get the edges of
     * @return the edges of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    Set<UndirectedEdge<V, E>> edges(V v);

    /**
     * Get all incident vertices of {@code v}.
     * <p>
     * Complexity: O(1)
     *
     * @param v the vertex to get the incident vertices of
     * @return the incident vertices of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    Set<V> adjacent(V v);

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
    default Iterable<UndirectedEdge<V, E>> edges() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the sum of the edge weights of a vertex.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * double sum = 0;
     * for (UndirectedEdge&lt;V, E&gt; e : edges(v)) {
     *     sum += e.weight();
     * }
     * return sum;
     * </code></pre>
     *
     * @param v the vertex
     * @return the sum of weights of all edges of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    default double strength(V v) {
        double sum = 0;
        for (UndirectedEdge<V, E> e : edges(v)) {
            sum += e.weight();
        }
        return sum;
    }

    /**
     * Returns the degree of a vertex, aka the number of edges. Edge to self is included (if present).
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return adjacent(v).size();
     * </code></pre>
     *
     * @param v the vertex
     * @return the degree of vertex {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    default int degree(V v) {
        return adjacent(v).size();
    }

    /**
     * Creates an edge connecting {@code v} and {@code w} with default weight
     * {@value Finals#DEFAULT_EDGE_WEIGHT}. The edge object will be assigned to {@code null}. If an edge with the same
     * {@code v} and {@code w} exists, nothing happens.
     *
     * @param v one end of the egde
     * @param w the other end of the edge
     * @return the {@link UndirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if any of the arguments is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     */
    default UndirectedEdge<V, E> addEdge(V v, V w) {
        return addEdge(v, w, null, Finals.DEFAULT_EDGE_WEIGHT);
    }

    /**
     * Creates an edge connecting {@code v} and {@code w} with {@code weight}. The edge object will be
     * assigned to {@code null}. If an edge with the same {@code v} and {@code w} exists, nothing happens.
     *
     * @param v      one end of the egde
     * @param w      the other end of the edge
     * @param weight the weight to be associated with the edge
     * @return the {@link UndirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if any of the arguments is {@code null}
     * @throws IllegalVertexException   if either {@code v} or {@code w} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    default UndirectedEdge<V, E> addEdge(V v, V w, double weight) {
        return addEdge(v, w, null, weight);
    }

    /**
     * Creates an edge connecting {@code v} and {@code w} and default weight
     * {@value Finals#DEFAULT_EDGE_WEIGHT}. Also attaches the object {@code edge} to the edge. If an edge with the same
     * {@code v} and {@code w} exists, nothing happens.
     *
     * @param v    one end of the egde
     * @param w    the other end of the edge
     * @param edge the object to attach to the edge
     * @return the {@link UndirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException   if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException if either {@code v} or {@code w} is not in the graph
     */
    default UndirectedEdge<V, E> addEdge(V v, V w, E edge) {
        return addEdge(v, w, edge, Finals.DEFAULT_EDGE_WEIGHT);
    }

    /**
     * Creates an edge connecting {@code v} and {@code w} with {@code weight}. Also attaches the object
     * {@code edge} to the edge. If an edge with the same {@code v} and {@code w} exists, nothing happens.
     *
     * @param v      one end of the egde
     * @param w      the other end of the edge
     * @param edge   the object to attach to the edge
     * @param weight the weight to be associated with the edge
     * @return the {@link UndirectedEdge} object of the newly added edge, or {@code null} if an edge already exists
     * @throws NullPointerException     if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException   if either {@code v} or {@code w} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    UndirectedEdge<V, E> addEdge(V v, V w, E edge, double weight);

    /**
     * Replaces the weight of the specified edge with {@code v} and {@code w} with {@code weight}. If an edge
     * with {@code v} and {@code w} doesn't exist, nothing will happen.
     *
     * @param v      one end of the egde
     * @param w      the other end of the edge
     * @param weight the new weight to be associated with the edge
     * @return {@code true} if there was previously an edge with the specified {@code v} and {@code w} and
     * thus the weight could be changed, {@code false} otherwise
     * @throws NullPointerException     if either {@code v} or {@code w} is {@code null}
     * @throws IllegalVertexException   if either {@code v} or {@code w} is not in the graph
     * @throws IllegalArgumentException if {@code weight} is non-positive
     */
    default boolean setEdgeWeight(V v, V w, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        UndirectedEdge<V, E> previousEdge = removeEdge(v, w);
        if (previousEdge != null) {
            UndirectedEdge<V, E> e = addEdge(v, w, previousEdge.value(), weight);
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
                    final UndirectedEdge<V, E> edge = addEdge(v, u);
                    assert edge.v() == v;
                    assert edge.w() == u;
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
    @SuppressWarnings({"unchecked"})
    default void addEdges(V... among) {
        this.addEdges(Arrays.asList(among));
    }

    /**
     * Remove the edge with the specified {@code source} and {@code target}, if it exists.
     *
     * @param v one end of the egde
     * @param w the other end of the edge
     * @return the {@link DirectedEdge} that was removed if there was previously a directed edge
     * {@code (source,target)}, otherwise {@code null}
     * @throws NullPointerException   if either {@code source} or {@code target} is {@code null}
     * @throws IllegalVertexException if either {@code source} or {@code target} is not in the graph
     */
    UndirectedEdge<V, E> removeEdge(V v, V w);

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
    @SuppressWarnings({"unchecked"})
    default void removeEdges(V... among) {
        this.removeEdges(Arrays.asList(among));
    }
}
