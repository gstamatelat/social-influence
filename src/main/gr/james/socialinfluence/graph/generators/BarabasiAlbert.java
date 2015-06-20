package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.Degree;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.WeightedRandom;

import java.util.HashMap;
import java.util.List;

public class BarabasiAlbert {
    public static Graph generate(int totalVertices, int initialClique, int stepEdges, double a) {
        if (stepEdges > initialClique) {
            throw new GraphException(Finals.E_BARABASI_STEP);
        }

        Graph g = Clique.generate(initialClique);
        while (g.getVerticesCount() < totalVertices) {
            HashMap<Vertex, Double> weightMap = Degree.execute(g, true);
            for (Vertex v : weightMap.keySet()) {
                weightMap.put(v, Math.pow(weightMap.get(v), a));
            }
            List<Vertex> newVertices = WeightedRandom.makeRandomSelection(weightMap, stepEdges);

            Vertex v = g.addVertex();
            for (Vertex w : newVertices) {
                g.addEdge(v, w, true);
            }
        }

        return g.setName("BarabasiAlbert").setMeta(String.format("%s,totalVertices=%d,initialClique=%d,stepEdges=%d,a=%f", "BarabasiAlbert", totalVertices, initialClique, stepEdges, a));
    }
}