package gr.james.influence.graph;

/**
 * Represents a directed edge.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface DirectedEdge<V, E> {
    /**
     * Construct a new {@link DirectedEdge} from the given arguments.
     *
     * @param edge   the object assigned to the edge
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @param weight the weight of the edge
     * @param <V>    the vertex type
     * @param <E>    the edge type
     * @return a new {@link DirectedEdge} from the given arguments
     */
    static <V, E> DirectedEdge<V, E> from(E edge, V source, V target, double weight) {
        return new DirectedEdgeImpl<>(edge, source, target, weight);
    }

    /**
     * Returns the reverse of this {@link DirectedEdge}.
     * <p>
     * The reverse edge has the same {@link #edge()} and {@link #weight()} as the original one but the {@link #source()}
     * and {@link #target()} are interchanged.
     *
     * @return the reverse of this {@link DirectedEdge}
     */
    default DirectedEdge<V, E> reverse() {
        return new DirectedEdgeImpl<>(edge(), target(), source(), weight());
    }

    /**
     * Returns the edge object assigned.
     *
     * @return the edge object assigned
     */
    E edge();

    /**
     * Returns the source vertex of the edge.
     *
     * @return the source vertex of the edge
     */
    V source();

    /**
     * Returns the target vertex of the edge.
     *
     * @return the target vertex of the edge
     */
    V target();

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
     * Indicates whether some object is equal to this {@link DirectedEdge}.
     * <p>
     * This method will return {@code true} if {@code obj} is of type {@link DirectedEdge} and is equal to this edge.
     * Two {@link DirectedEdge} are equal if the respective fields {@link #edge()}, {@link #source()}, {@link #target()}
     * and {@link #weight()} are equal.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if {@code obj} is equal to this {@link DirectedEdge}, otherwise {@code false}
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns a hash code value for this {@link DirectedEdge}.
     *
     * @return a hash code value for this {@link DirectedEdge}
     */
    @Override
    int hashCode();
}
