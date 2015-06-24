package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.helper.Helper;

public class Clique {
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices) {
        Graph g = Helper.instantiateGeneric(type);
        g.addVertices(totalVertices);
        g.connectAllVertices();
        return g.setName("Clique").setMeta(String.format("%s,totalVertices=%d", "Clique", totalVertices));
    }
}