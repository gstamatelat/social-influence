package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
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

        Graph g = Helper.combineGraphs(c);

        for (int i = 0; i < clusters; i++) {
            Vertex s = randomVertices[i];
            Vertex t = randomVertices[(i + 1) % clusters];
            g.addEdge(s, t, true);
        }

        return g.setName("BarabasiAlbertCluster").setMeta(String.format("%s,totalVertices=%d,initialClique=%d,stepEdges=%d,a=%f,clusters=%d", "BarabasiAlbertCluster", totalVertices, initialClique, stepEdges, a, clusters));
    }
}