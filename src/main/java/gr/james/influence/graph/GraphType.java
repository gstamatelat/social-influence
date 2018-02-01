package gr.james.influence.graph;

/**
 * Represents a graph type.
 */
public interface GraphType {
    /**
     * A {@link GraphType} representing an uweighted and undirected graph.
     */
    GraphType GRAPH = from(false, false);

    /**
     * A {@link GraphType} representing an uweighted and directed graph.
     */
    GraphType DIRECTED = from(true, false);

    /**
     * A {@link GraphType} representing a weighted and undirected graph.
     */
    GraphType WEIGHTED = from(false, true);

    /**
     * A {@link GraphType} representing a weighted and directed graph.
     */
    GraphType WEIGHTED_DIRECTED = from(true, true);

    /**
     * Constructs a new {@link GraphType} from the given arguments.
     *
     * @param directed if the graph is directed
     * @param weighted if the graph is weighted
     * @return a new {@link GraphType} from the given arguments
     */
    static GraphType from(boolean directed, boolean weighted) {
        return new GraphTypeImpl(directed, weighted);
    }

    /**
     * Returns {@code true} if the graph is directed, otherwise {@code false}.
     *
     * @return {@code true} if the graph is directed, otherwise {@code false}
     */
    boolean isDirected();

    /**
     * Returns {@code true} if the graph is weighted, otherwise {@code false}.
     *
     * @return {@code true} if the graph is weighted, otherwise {@code false}
     */
    boolean isWeighted();

    /**
     * Returns a string representation of this {@link GraphType}.
     *
     * @return a string representation of this {@link GraphType}
     */
    @Override
    String toString();

    /**
     * Indicates whether some object is equal to this {@link GraphType}.
     * <p>
     * This method will return {@code true} if {@code obj} is of type {@link GraphType} and is equal to this
     * {@link GraphType}. Two {@link GraphType} objects are equal if the respective fields {@link #isDirected()} and
     * {@link #isWeighted()} are equal.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if {@code obj} is equal to this {@link GraphType}, otherwise {@code false}
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns a hash code value for this {@link GraphType}.
     *
     * @return a hash code value for this {@link GraphType}
     */
    @Override
    int hashCode();
}
