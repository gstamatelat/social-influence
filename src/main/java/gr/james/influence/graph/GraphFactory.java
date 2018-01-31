package gr.james.influence.graph;

/**
 * Represents of factory that can produce graphs.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
@FunctionalInterface
public interface GraphFactory<V, E> {
    /**
     * Creates and returns a new empty {@link Graph}.
     *
     * @return a new empty {@link Graph}
     */
    Graph<V, E> createGraph();

    /**
     * Creates and returns a new empty {@link Graph}.
     *
     * @param expectedVertexCount expected number of vertices
     * @return a new empty {@link Graph}
     * @throws IllegalArgumentException if {@code expectedVertexCount} is negative
     */
    default Graph<V, E> createGraph(int expectedVertexCount) {
        if (expectedVertexCount < 0) {
            throw new IllegalArgumentException();
        }
        return createGraph();
    }
}
