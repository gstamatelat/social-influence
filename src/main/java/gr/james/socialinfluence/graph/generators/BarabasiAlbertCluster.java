package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

public class BarabasiAlbertCluster {
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices, int initialClique, int stepEdges, double a, int clusters) {
        Graph[] c = new Graph[clusters];

        for (int i = 0; i < clusters; i++) {
            c[i] = BarabasiAlbert.generate(type, totalVertices, stepEdges, initialClique, a);
        }

        Vertex[] randomVertices = new Vertex[clusters];
        for (int i = 0; i < clusters; i++) {
            randomVertices[i] = c[i].getRandomVertex();
        }

        Graph g = Helper.combineGraphs(MemoryGraph.class, c);

        for (int i = 0; i < clusters; i++) {
            Vertex s = randomVertices[i];
            Vertex t = randomVertices[(i + 1) % clusters];
            g.addEdge(s, t, true);
        }

        return g.setMeta("name", "BarabasiAlbertCluster")
                .setMeta("totalVertices", String.valueOf(totalVertices))
                .setMeta("initialClique", String.valueOf(initialClique))
                .setMeta("stepEdges", String.valueOf(stepEdges))
                .setMeta("a", String.valueOf(a))
                .setMeta("clusters", String.valueOf(clusters));
    }
}