package gr.james.influence.demo;

import gr.james.influence.graph.DirectedGraph;

/**
 * Demonstration on how to manually create a directed graph.
 */
public final class CreateDirectedGraph {
    public static void main(String[] args) {
        createDirectedGraph();
    }

    /**
     * Creates the graph (A-&gt;B, B-&gt;C, C-&gt;A, B-&gt;A).
     */
    public static void createDirectedGraph() {
        // Instantiate an empty directed graph with vertices of type String
        final DirectedGraph<String, Object> g = DirectedGraph.create();

        // Add three unconnected vertices in the graph: A, B and C
        g.addVertices("A", "B", "C");

        // Add 4 directed edges
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("C", "A");
        g.addEdge("B", "A");

        // Print the string representation of g in stdout
        System.out.println(g);
    }
}
