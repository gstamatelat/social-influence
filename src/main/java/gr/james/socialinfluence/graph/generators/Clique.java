package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.GraphTransformations;
import gr.james.socialinfluence.helper.Helper;

public class Clique {
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices) {
        Graph g = Helper.instantiateGeneric(type);

        g.addVertices(totalVertices);
        GraphTransformations.connectAllVertices(g);

        return g.setMeta("name", "Clique")
                .setMeta("totalVertices", String.valueOf(totalVertices));
    }
}