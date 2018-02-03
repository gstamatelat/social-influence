package gr.james.influence.graph;

/**
 * Represents of factory that can produce graphs.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public interface GraphFactory<G extends DirectedGraph<V, E>, V, E> {
    /**
     * Creates and returns a new empty {@link DirectedGraph}.
     *
     * @return a new empty {@link DirectedGraph}
     * @throws NullPointerException if {@code type} is {@code null}
     */
    G createGraph();

    /**
     * Creates and returns a new empty {@link DirectedGraph}.
     *
     * @param expectedVertexCount expected number of vertices
     * @return a new empty {@link DirectedGraph}
     * @throws NullPointerException     if {@code type} is {@code null}
     * @throws IllegalArgumentException if {@code expectedVertexCount} is negative
     */
    G createGraph(int expectedVertexCount);
}
