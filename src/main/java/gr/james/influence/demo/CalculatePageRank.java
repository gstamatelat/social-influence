package gr.james.influence.demo;

import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

/**
 * Demonstration on how to calculate PageRank on a {@link DirectedGraph}.
 */
public final class CalculatePageRank {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();
        System.out.println(g);
        System.out.println(pageRank(g));
    }

    /**
     * Run PageRank and return the {@link GraphState} result.
     * <p>
     * This method uses a damping factor of {@code 0.5} and a small epsilon.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the {@link GraphState} result of PageRank
     * @throws NullPointerException if {@code g} is {@code null}
     * @see <a href="http://pr.efactory.de/e-pagerank-algorithm.shtml">pr.efactory.de</a>
     */
    public static <V> GraphState<V, Double> pageRank(DirectedGraph<V, ?> g) {
        // Instantiate the PageRank with a damping factor of 0.5 and a small epsilon
        final PageRank<V> pageRank = new PageRank<>(g, 0.5, 1.0e-7);

        // Run and return the GraphState result
        return pageRank.run();
    }
}
