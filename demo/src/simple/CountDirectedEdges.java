package simple;

import gr.james.influence.graph.DirectedGraph;

/**
 * Demonstration on how to count the edges of a {@link DirectedGraph}.
 */
public final class CountDirectedEdges {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();
        assert countEdges(g) == countEdgesStream(g);
        System.out.println(g);
        System.out.printf("Graph has %d directed edges%n", countEdges(g));
    }

    /**
     * Get the number of directed edges in a {@link DirectedGraph}.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the number of directed edges in {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> int countEdges(DirectedGraph<V, ?> g) {
        int edgesCount = 0;
        for (V v : g) {
            edgesCount += g.adjacentOut(v).size();
        }
        return edgesCount;
    }

    /**
     * Get the number of directed edges in a {@link DirectedGraph} using the Java 8 Stream API.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the number of directed edges in {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> int countEdgesStream(DirectedGraph<V, ?> g) {
        return g.vertexSet().stream().mapToInt(v -> g.adjacentOut(v).size()).sum();
    }
}
