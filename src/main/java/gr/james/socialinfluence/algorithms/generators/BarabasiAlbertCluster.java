package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphOperations;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

public class BarabasiAlbertCluster {
    // TODO: Convert to GraphGenerator
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices, int initialClique, int stepEdges, double a, int clusters) {
        Graph[] c = new Graph[clusters];

        GraphGenerator scaleFreeGenerator = new BarabasiAlbertGenerator<>(type, totalVertices, stepEdges, initialClique, a);
        for (int i = 0; i < clusters; i++) {
            c[i] = scaleFreeGenerator.create();
        }

        Vertex[] randomVertices = new Vertex[clusters];
        for (int i = 0; i < clusters; i++) {
            randomVertices[i] = c[i].getRandomVertex();
        }

        Graph g = GraphOperations.combineGraphs(MemoryGraph.class, c);

        for (int i = 0; i < clusters; i++) {
            Vertex s = randomVertices[i];
            Vertex t = randomVertices[(i + 1) % clusters];
            g.addEdge(s, t, true);
        }

        return g.setMeta("type", "BarabasiAlbertCluster")
                .setMeta("totalVertices", String.valueOf(totalVertices))
                .setMeta("initialClique", String.valueOf(initialClique))
                .setMeta("stepEdges", String.valueOf(stepEdges))
                .setMeta("a", String.valueOf(a))
                .setMeta("clusters", String.valueOf(clusters));
    }
}