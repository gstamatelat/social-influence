package centralities;

import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;
import simple.CreateDirectedGraph;

/**
 * Demonstration on how to calculate PageRank on a {@link DirectedGraph}.
 */
public final class CalculatePageRank {
    public static void main(String[] args) {
        // Create a graph
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();

        // Populate the pr object with the PageRank values
        final GraphState<String, Double> pr = pageRank(g);

        // Print the graph
        System.out.println(g);

        // Print PageRank as map
        System.out.println(pr);

        // Print the PageRank values individually for each vertex
        for (String v : g) {
            System.out.printf("%s = %f%n", v, pr.get(v));
        }
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
