package gr.james.influence.demo;

import gr.james.influence.graph.DirectedGraph;

/**
 * Demonstration on how to manually create a directed graph.
 */
public final class CreateDirectedGraph {
    public static void main(String[] args) {
        System.out.println(createDirectedGraph());
    }

    /**
     * Creates and returns the graph (A-&gt;B, B-&gt;C, C-&gt;A, A-&gt;C).
     *
     * @return the graph (A-&gt;B, B-&gt;C, C-&gt;A, A-&gt;C)
     * @see <a href="http://pr.efactory.de/e-pagerank-algorithm.shtml">pr.efactory.de</a>
     */
    public static DirectedGraph<String, Object> createDirectedGraph() {
        // Instantiate an empty directed graph with vertices of type String
        final DirectedGraph<String, Object> g = DirectedGraph.create();

        // Add three unconnected vertices in the graph: A, B and C
        g.addVertices("A", "B", "C");

        // Add 4 directed edges
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("C", "A");
        g.addEdge("A", "C");

        // Return g
        return g;
    }
}
