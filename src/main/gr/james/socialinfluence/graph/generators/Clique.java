package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;

public class Clique {
    public static Graph generate(int totalVertices) {
        Graph g = new Graph();
        g.addVertices(totalVertices);
        g.connectAllVertices();
        g.setName("Clique");
        g.setMeta(String.format("%s,totalVertices=%d", "Clique", totalVertices));
        return g;
    }
}