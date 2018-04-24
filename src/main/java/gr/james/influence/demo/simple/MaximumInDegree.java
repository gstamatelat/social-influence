package gr.james.influence.demo.simple;

import gr.james.influence.demo.GenerateRandomDirectedGraph;
import gr.james.influence.graph.DirectedGraph;

/**
 * Demonstration on how to get the maximum inbound degree in a {@link DirectedGraph}.
 */
public final class MaximumInDegree {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = GenerateRandomDirectedGraph.generateRandomDirectedGraph();
        assert maximumInDegree(g) == maximumInDegreeStream(g);
        System.out.println(maximumInDegree(g));
    }

    /**
     * Get the maximum inbound degree in a {@link DirectedGraph}.
     * <p>
     * Returns {@code -1} if the graph has no vertices. Returns {@code 0} if the graph has at least one vertex but no
     * edges. The degree of a vertex includes a self-loop, if present.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the maximum inbound degree in {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> int maximumInDegree(DirectedGraph<V, ?> g) {
        int maxDegree = -1;
        for (V v : g) {
            final int degree = g.inDegree(v);
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }
        return maxDegree;
    }

    /**
     * Get the maximum inbound degree in a {@link DirectedGraph} using the Java 8 Stream API.
     * <p>
     * Returns {@code -1} if the graph has no vertices. Returns {@code 0} if the graph has at least one vertex but no
     * edges. The degree of a vertex includes a self-loop, if present.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the maximum inbound degree in {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> int maximumInDegreeStream(DirectedGraph<V, ?> g) {
        return g.vertexSet().stream().mapToInt(g::inDegree).max().orElse(-1);
    }
}
