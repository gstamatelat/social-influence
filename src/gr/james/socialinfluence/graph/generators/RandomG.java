package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Random;

public class RandomG {
    public static Graph generate(int totalVertices, double p, Random R) {
        R = Helper.getRandom(R);

        Graph g = new Graph();

        while (g.getVerticesCount() < totalVertices) {
            Vertex v = g.addVertex();
            for (Vertex y : g.getVertices()) {
                if (!v.equals(y)) {
                    if (R.nextDouble() < p) {
                        v.addEdge(y);
                    }
                    if (R.nextDouble() < p) {
                        y.addEdge(v);
                    }
                }
            }
        }

        /* Make sure it's strongly connected */
        g.createCircle(false);

        return g.setName("RandomG").setMeta(String.format("%s,totalVertices=%d,p=%f", "RandomG", totalVertices, p));
    }

    public static Graph generate(int totalVertices, double p) {
        return generate(totalVertices, p, null);
    }
}