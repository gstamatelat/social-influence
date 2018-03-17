package gr.james.influence.demo;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;

/**
 * Demonstration on how to use a graph generator to generate a random directed graph.
 */
public final class GenerateRandomDirectedGraph {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = generateRandomDirectedGraph();
        System.out.println(g);
    }

    /**
     * Generate a random {@link DirectedGraph} sitting on 5 vertices with connection probability {@code 0.5}.
     * <p>
     * This method uses the hardcoded seed {@code 283764}.
     *
     * @return a random {@link DirectedGraph} with 5 vertices and connection probability {@code 0.5}
     */
    public static DirectedGraph<String, Object> generateRandomDirectedGraph() {
        // Instantiate a RandomGraphGenerator
        final RandomGraphGenerator<DirectedGraph<String, Object>, String, Object> randomGenerator =
                new RandomGenerator<>(5, 0.5);

        // Invoke the method generate to generate the graph
        return randomGenerator.generate(283764, VertexProvider.STRING_PROVIDER);
    }
}
