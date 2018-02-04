package gr.james.influence.main;

import gr.james.influence.graph.UndirectedGraph;

public class UndirectedTest {
    public static void main(String[] args) {
        UndirectedGraph<String, Object> g = UndirectedGraph.create();
        g.addVertices("1", "2", "3", "4");
        g.addEdge("1", "2");
        g.addEdge("2", "3");
        g.addEdge("3", "4");
        System.out.println(g);
        System.out.println(g.asDirected());
    }
}
