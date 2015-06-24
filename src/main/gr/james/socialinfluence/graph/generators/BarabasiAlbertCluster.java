package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

public class BarabasiAlbertCluster {
    public static Graph generate(int totalVertices, int initialClique, int stepEdges, double a, int clusters) {
        MemoryGraph[] c = new MemoryGraph[clusters];

        for (int i = 0; i < clusters; i++) {
            c[i] = (MemoryGraph) BarabasiAlbert.generate(totalVertices, stepEdges, initialClique, a);
        }

        Vertex[] randomVertices = new Vertex[clusters];
        for (int i = 0; i < clusters; i++) {
            randomVertices[i] = c[i].getRandomVertex();
        }

        MemoryGraph g = Helper.combineGraphs(c);

        for (int i = 0; i < clusters; i++) {
            Vertex s = randomVertices[i];
            Vertex t = randomVertices[(i + 1) % clusters];
            g.addEdge(s, t, true);
        }

        return g.setName("BarabasiAlbertCluster").setMeta(String.format("%s,totalVertices=%d,initialClique=%d,stepEdges=%d,a=%f,clusters=%d", "BarabasiAlbertCluster", totalVertices, initialClique, stepEdges, a, clusters));
    }
}