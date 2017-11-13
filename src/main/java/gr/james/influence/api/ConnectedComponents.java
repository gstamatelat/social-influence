package gr.james.influence.api;

import java.util.Set;

/**
 * Represents an algorithm that calculates the strongly connected components of a graph.
 *
 * @param <V> the type of vertex
 */
public interface ConnectedComponents<V> {
    /**
     * Returns how many strongly connected components there are in the graph.
     *
     * @return how many connected components there are
     */
    default int size() {
        return components().size();
    }

    /**
     * Checks if two vertices belong in the same component. This method is commutative.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return {@code true} if {@code v1} and {@code v2} are in the same component, otherwise {@code false}
     * @throws NullPointerException     if either vertex is {@code null}
     * @throws IllegalArgumentException if either vertex is not in the graph
     */
    boolean connected(V v1, V v2);

    /**
     * Returns the set of vertices of the connected component enclosing {@code v}. This set includes the vertex itself.
     *
     * @param v the vertex of which to get the enclosing component
     * @return the unmodifiable connected component of {@code v}
     * @throws NullPointerException     if {@code v} is {@code null}
     * @throws IllegalArgumentException if {@code v} is not in the graph
     */
    Set<V> component(V v);

    /**
     * Get all connected components of the graph associated with this instance.
     *
     * @return an unmodifiable set of connected components of the graph
     */
    Set<Set<V>> components();
}
