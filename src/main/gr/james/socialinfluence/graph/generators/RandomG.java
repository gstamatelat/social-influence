package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.RandomHelper;

public class RandomG {
    public static MemoryGraph generate(int totalVertices, double p) {
        MemoryGraph g = new MemoryGraph();

        while (g.getVerticesCount() < totalVertices) {
            Vertex v = g.addVertex();
            for (Vertex y : g.getVertices()) {
                if (!v.equals(y)) {
                    if (RandomHelper.getRandom().nextDouble() < p) {
                        v.addEdge(y);
                    }
                    if (RandomHelper.getRandom().nextDouble() < p) {
                        y.addEdge(v);
                    }
                }
            }
        }

        /* Make sure it's strongly connected */
        g.createCircle(false);

        return g.setName("RandomG").setMeta(String.format("%s,totalVertices=%d,p=%f", "RandomG", totalVertices, p));
    }
}