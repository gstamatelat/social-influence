package gr.james.influence.demo;

import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;

/**
 * Demonstration of a simple brute force search method.
 * <p>
 * Determines the best edge to remove from a {@link DirectedGraph} so as to maximize the eigenvector centrality of a
 * specified vertex.
 */
public final class MaximizeCentrality {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = new RandomGenerator<String, Object>(50, 0.2)
                .generate(VertexProvider.STRING_PROVIDER);
        final String v = Graphs.getRandomVertex(g);
        System.out.printf("Trying to maximize the centrality of vertex %s%n", v);
        System.out.printf("Initial eigenvector centrality of the vertex %s is %.2f%n",
                v, PageRank.execute(g, 0.0).get(v));
        maximizeCentrality(g, v);
    }

    /**
     * Determine the best edge to remove from a graph so as to maximize the eigenvector centrality of a specified
     * vertex.
     * <p>
     * This method uses a brute force search for all edges in {@code g}.
     *
     * @param g   the graph to perform the search
     * @param v   the vertex to maximize its centrality
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return the edge of {@code g} that, when removed, maximizes the eigenvector centrality of {@code v}
     * @throws NullPointerException     if {@code g} or {@code v} is {@code null}
     * @throws IllegalVertexException   if {@code v} is not part of {@code g}
     * @throws IllegalArgumentException if {@code g} doesn't have any edges
     */
    public static <V, E> DirectedEdge<V, E> maximizeCentrality(DirectedGraph<V, E> g, V v) {
        // Terminate early if g doesn't have any edges because the search would be meaningless
        if (Graphs.getEdgesCount(g) == 0) {
            throw new IllegalArgumentException();
        }

        // Terminate early if v is not in g
        if (!g.containsVertex(v)) {
            throw new IllegalVertexException();
        }

        // Copy g because it may be unmodifiable
        final DirectedGraph<V, E> h = DirectedGraph.create(g);

        // This variable holds the best edge during the search
        DirectedEdge<V, E> bestEdge = null;

        // This variable holds the best value of eigenvector centrality during the search
        double bestEigenvector = -1;

        // Perform a search for every edge in g
        for (DirectedEdge<V, E> edge : g.edges()) {
            // Remove the edge
            final DirectedEdge<V, E> removedEdge = h.removeEdge(edge.source(), edge.target());

            // Surely, the removedEdge should never be null since it is guaranteed to exist in h
            assert removedEdge != null;

            // Calculate the eigenvector centrality of v using the PageRank class with damping factor of 0
            final double eigenvector = PageRank.execute(h, 0.0).get(v);

            // Assign a new edge if better
            if (eigenvector > bestEigenvector) {
                bestEigenvector = eigenvector;
                bestEdge = removedEdge;
                System.out.printf("Found new edge %s yielding eigenvector centrality of %.2f%n", removedEdge, eigenvector);
            }

            // Add the edge back into the graph
            h.addEdge(edge.source(), edge.target(), edge.value(), edge.weight());
        }

        // Return the best edge
        return bestEdge;
    }
}
