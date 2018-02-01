package gr.james.influence.graph;

/**
 * Represents a graph type.
 */
public interface GraphType {
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
}
