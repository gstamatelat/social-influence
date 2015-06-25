package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.helper.RandomHelper;

public class RandomG {
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices, double p) {
        Graph g = Helper.instantiateGeneric(type);

        while (g.getVerticesCount() < totalVertices) {
            Vertex v = g.addVertex();
            for (Vertex y : g.getVertices()) {
                if (!v.equals(y)) {
                    if (RandomHelper.getRandom().nextDouble() < p) {
                        g.addEdge(v, y);
                    }
                    if (RandomHelper.getRandom().nextDouble() < p) {
                        g.addEdge(y, v);
                    }
                }
            }
        }

        /* Make sure it's strongly connected */
        g.createCircle(false);

        return g.setName("RandomG").setMeta(String.format("%s,totalVertices=%d,p=%f", "RandomG", totalVertices, p));
    }
}