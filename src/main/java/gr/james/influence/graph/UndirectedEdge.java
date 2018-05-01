package gr.james.influence.graph;

/**
 * Represents an edge of an {@link UndirectedGraph}.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface UndirectedEdge<V, E> {
    /**
     * Construct a new {@link UndirectedEdge} from the given arguments.
     *
     * @param value  the value of the edge
     * @param v      one end of the edge
     * @param w      the other end of the edge
     * @param weight the weight of the edge
     * @param <V>    the vertex type
     * @param <E>    the edge type
     * @return a new {@link DirectedEdge} from the given arguments
     */
    static <V, E> UndirectedEdge<V, E> from(E value, V v, V w, double weight) {
        return new UndirectedEdgeImpl<>(value, v, w, weight);
    }

    /**
     * Returns the value of the edge.
     *
     * @return the value of the edge
     */
    E value();

    /**
     * Returns an arbitrary end of the edge.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * other(w())
     * </code></pre>
     *
     * @return an arbitrary end of the edge
     */
    V v();

    /**
     * Returns the end of the edge that is not {@link #v()}.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * other(v())
     * </code></pre>
     *
     * @return the end of the edge that is not {@link #v()}
     */
    V w();

    /**
     * Returns the vertex that is on the other end of another vertex.
     * <p>
     * In other words, this method returns the vertex that sits opposite of {@code ref} in this edge.
     *
     * @param ref the other vertex
     * @return the vertex that is on the other end of another vertex
     * @throws NullPointerException     if {@code ref} is {@code null}
     * @throws IllegalArgumentException if {@code ref} is not incident to this edge
     */
    default V other(V ref) {
        if (ref.equals(v())) {
            return w();
        } else if (ref.equals(w())) {
            return v();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the weight of the edge.
     *
     * @return the weight of the edge
     */
    double weight();

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    @Override
    String toString();

    /**
     * Indicates whether some object is equal to this {@link UndirectedEdge}.
     * <p>
     * This method will return {@code true} if {@code obj} is of type {@link UndirectedEdge} and is equal to this edge.
     * Two {@link UndirectedEdge} objects are equal if the respective fields {@link #value()}, {@link #v()},
     * {@link #w()} and {@link #weight()} are equal.
     * <p>
     * Unlike a {@link DirectedEdge}, the order of the fields {@link #v()} and {@link #w()} is not taken into account
     * for the {@code equals} method.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if {@code obj} is equal to this {@link DirectedEdge}, otherwise {@code false}
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns a hash code value for this {@link UndirectedEdge}.
     *
     * @return a hash code value for this {@link UndirectedEdge}
     */
    @Override
    int hashCode();
}
