package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.*;

/**
 * Base interface for all graph types.
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
 * Methods that return collections ({@link Map Maps} and {@link Set Sets} return read-only views of the actual
 * collections they represent, meaning that you can't insert, remove or reorder elements. These collections are backed
 * by the graph so changes to the graph will reflect on the collections after they have been returned.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface Graph<V, E> extends Iterable<V> {
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
     * Returns an arbitrary vertex from the graph.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return vertexSet().iterator().next();
     * </code></pre>
     * <p>
     * Complexity: O(1)
     *
     * @return an arbitrary vertex from the graph
     * @throws NoSuchElementException if the graph is empty
     */
    default V anyVertex() {
        return this.vertexSet().iterator().next();
    }

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
     * Insert the specified vertex {@code v} to the graph.
     * <p>
     * If the vertex is already contained in the graph, this method is a no-op and returns {@code false}.
     * <p>
     * Complexity: O(1)
     *
     * @param v the vertex to insert to the graph
     * @return {@code false} if the graph previously already contained the vertex, otherwise {@code true}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    boolean addVertex(V v);

    /**
     * Insert a vertex produced by a {@link VertexProvider} to the graph.
     * <p>
     * If the vertex is already contained in the graph, this method will throw {@link IllegalVertexException} to
     * indicate this unusual behavior.
     * <p>
     * Complexity: O(1)
     *
     * @param vertexProvider the vertex provider
     * @return the newly added vertex
     * @throws NullPointerException   if {@code vertexProvider} is {@code null}
     * @throws IllegalVertexException if {@code vertexProvider} produced a vertex that is already in the graph
     * @throws NoSuchElementException if the {@link VertexProvider#create()} method of {@code vertexProvider} threw
     *                                exception
     */
    default V addVertex(VertexProvider<V> vertexProvider) {
        if (vertexProvider == null) {
            throw new NullPointerException();
        }
        final V v = vertexProvider.create();
        if (!addVertex(v)) {
            throw new IllegalVertexException();
        }
        return v;
    }

    /**
     * Insert a number of vertices produced by a {@link VertexProvider} to the graph.
     * <p>
     * This method returns a {@link List} of the vertices that were generated and inserted. This collection always
     * contains {@code n} distinct elements in the order at which they were generated.
     *
     * @param vertexProvider the vertex provider
     * @param n              the number of vertices to generate and insert
     * @return a {@link List} of the vertices that were generated and inserted
     * @throws NullPointerException     if {@code vertexProvider} is {@code null}
     * @throws IllegalArgumentException if {@code n < 0}
     * @throws IllegalVertexException   if {@code vertexProvider} produced a vertex that is already in the graph
     * @throws NoSuchElementException   if the {@link VertexProvider#create()} method of {@code vertexProvider} threw
     *                                  exception
     */
    default List<V> addVertices(VertexProvider<V> vertexProvider, int n) {
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
        assert contained.size() == n;
        assert contained.stream().distinct().count() == n;
        return Collections.unmodifiableList(contained);
    }

    /**
     * Insert a group of vertices in the graph.
     * <p>
     * If any of the vertices are already on the graph, they are returned as a {@link List}.
     * <p>
     * This is mostly a convenient method to manually add vertices in a graph:
     * <pre><code>
     * UndirectedGraph&lt;String, Object&gt; g = UndirectedGraph.create();
     * g.addVertices("1", "2", "3");
     * </code></pre>
     *
     * @param vertices the vertices to insert to the graph
     * @return an unmodifiable list view containing the vertices in {@code vertices} that were already in the graph
     * @throws NullPointerException if any vertex in {@code vertices} is {@code null}
     */
    @SuppressWarnings({"unchecked"})
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
     * Insert a group of vertices in the graph.
     * <p>
     * If any of the vertices are already on the graph, they are returned as a {@link List}.
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
     * Removes a vertex from the graph if it is present.
     * <p>
     * This method will also remove the incident edges of that vertex.
     *
     * @param v the vertex to be removed
     * @return {@code true} if the graph previously contained this vertex, otherwise {@code false}
     * @throws NullPointerException if {@code v} is {@code null}
     */
    boolean removeVertex(V v);

    /**
     * Removes a group of vertices from the graph.
     * <p>
     * Vertices in the group that are not in the graph are silently ignored.
     *
     * @param vertices an {@link Iterable} of vertices to remove from the graph
     * @throws NullPointerException if {@code vertices} or any of the objects in {@code vertices} is {@code null}
     */
    default void removeVertices(Iterable<V> vertices) {
        for (V v : vertices) {
            removeVertex(v);
        }
    }

    /**
     * Returns an unmodifiable decorator around this graph.
     * <p>
     * Invoking mutation methods on the resulting graph will result in {@link UnsupportedOperationException}.
     *
     * @return an unmodifiable decorator around this graph
     */
    Graph<V, E> asUnmodifiable();

    /**
     * Creates and returns an immutable graph from a copy of this graph.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return copy().asUnmodifiable();
     * </code></pre>
     *
     * @return an immutable graph from a copy of this graph
     */
    Graph<V, E> toImmutable();

    /**
     * Returns a subgraph view of this graph that only contains the supplied vertices along with their interconnections.
     * <p>
     * The resulting graph is backed by this graph and will reflect changes to it. The subgraph, however, is not
     * tolerant to behavior involving removing any vertex in {@code vertices} from the original graph. Performing this
     * operation after the subgraph is produced may lead to unexpected behavior without exception. The subgraph is
     * tolerant, however, to any other mutation, like edge insertion/removal and vertex insertions. It is also safe to
     * remove any vertex not contained in the subgraph.
     * <p>
     * The operations of inserting or removing vertices from the subgraph will fail with
     * {@link UnsupportedOperationException}.
     * <p>
     * This method runs in time proportional to the size of {@code vertices}.
     *
     * @param vertices the vertices of the subgraph
     * @return a subgraph view of this graph bounded by {@code vertices}
     * @throws NullPointerException   if {@code vertices} is {@code null}
     * @throws IllegalVertexException if not all {@code vertices} are elements of this graph
     */
    Graph<V, E> subGraph(Set<V> vertices);

    /**
     * Returns the number of changes made to this graph during its lifetime.
     * <p>
     * The {@code modCount} is a convenient way to check for modifications made in a graph instance between two points
     * in time.
     *
     * @return the number of changes made to this graph during its lifetime
     */
    int modCount();

    /**
     * Returns a copy of this graph.
     *
     * @return a copy of this graph
     */
    Graph<V, E> copy();

    /**
     * Returns a string representation of this graph.
     *
     * @return a string representation of this graph
     */
    @Override
    String toString();
}
