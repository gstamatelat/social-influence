package gr.james.influence.graph;

/**
 * Represents of factory that can produce graphs.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface GraphFactory<V, E> {
    /**
     * Creates and returns a new empty {@link Graph}.
     *
     * @param type the type of graph
     * @return a new empty {@link Graph}
     * @throws NullPointerException if {@code type} is {@code null}
     */
    Graph<V, E> createGraph(GraphType type);

    /**
     * Creates and returns a new empty {@link Graph}.
     *
     * @param type                the type of graph
     * @param expectedVertexCount expected number of vertices
     * @return a new empty {@link Graph}
     * @throws NullPointerException     if {@code type} is {@code null}
     * @throws IllegalArgumentException if {@code expectedVertexCount} is negative
     */
    Graph<V, E> createGraph(GraphType type, int expectedVertexCount);

    /**
     * Creates and returns a new empty {@link Graph} of type {@link GraphType#WEIGHTED_DIRECTED}.
     *
     * @return a new empty {@link Graph} of type {@link GraphType#WEIGHTED_DIRECTED}
     */
    default Graph<V, E> createWeightedDirected() {
        return createGraph(GraphType.WEIGHTED_DIRECTED);
    }

    /**
     * Creates and returns a new empty {@link Graph} of type {@link GraphType#WEIGHTED_DIRECTED}.
     *
     * @param expectedVertexCount expected number of vertices
     * @return a new empty {@link Graph} of type {@link GraphType#WEIGHTED_DIRECTED}
     * @throws IllegalArgumentException if {@code expectedVertexCount} is negative
     */
    default Graph<V, E> createWeightedDirected(int expectedVertexCount) {
        return createGraph(GraphType.WEIGHTED_DIRECTED, expectedVertexCount);
    }
}
