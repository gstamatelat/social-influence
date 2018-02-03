package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.*;

public interface Graph<V> extends Iterable<V> {
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
        for (V v : vertices) {
            removeVertex(v);
        }
    }
}
