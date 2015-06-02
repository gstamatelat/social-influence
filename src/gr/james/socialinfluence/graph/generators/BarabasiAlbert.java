package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.Degree;
import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.helper.WeightedRandom;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BarabasiAlbert {
    public static Graph generate(int totalVertices, int initialClique, int stepEdges, double a, Random R) {
        R = Helper.getRandom(R);

        if (stepEdges > initialClique) {
            Helper.logError("stepEdges <= initialClique is a constraint");
            return null;
        }

        Graph g = Clique.generate(initialClique);
        while (g.getVerticesCount() < totalVertices) {
            HashMap<Vertex, Double> weightMap = Degree.execute(g, true);
            for (Vertex v : weightMap.keySet()) {
                weightMap.put(v, Math.pow(weightMap.get(v), a));
            }
            List<Vertex> newVertices = WeightedRandom.makeRandomSelection(weightMap, stepEdges, R);

            Vertex v = g.addVertex();
            for (Vertex w : newVertices) {
                g.addEdge(v, w, true);
            }
        }

        return g.setName("BarabasiAlbert").setMeta(String.format("%s,totalVertices=%d,initialClique=%d,stepEdges=%d,a=%f", "BarabasiAlbert", totalVertices, initialClique, stepEdges, a));
    }

    public static Graph generate(int totalVertices, int initialClique, int stepEdges, double a) {
        return generate(totalVertices, initialClique, stepEdges, a, null);
    }
}