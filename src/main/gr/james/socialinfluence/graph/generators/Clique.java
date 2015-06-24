package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.MemoryGraph;

public class Clique {
    public static MemoryGraph generate(int totalVertices) {
        MemoryGraph g = new MemoryGraph();
        g.addVertices(totalVertices);
        g.connectAllVertices();
        g.setName("Clique");
        g.setMeta(String.format("%s,totalVertices=%d", "Clique", totalVertices));
        return g;
    }
}