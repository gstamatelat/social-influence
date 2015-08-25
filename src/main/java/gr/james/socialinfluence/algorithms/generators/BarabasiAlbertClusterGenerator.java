package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.graph.Vertex;

public class BarabasiAlbertClusterGenerator implements GraphGenerator {
    private int totalVertices;
    private int initialClique;
    private int stepEdges;
    private double a;
    private int clusters;

    public BarabasiAlbertClusterGenerator(int totalVertices, int initialClique, int stepEdges, double a, int clusters) {
        this.totalVertices = totalVertices;
        this.initialClique = initialClique;
        this.stepEdges = stepEdges;
        this.a = a;
        this.clusters = clusters;
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory) {
        Graph[] c = new Graph[clusters];

        GraphGenerator scaleFreeGenerator = new BarabasiAlbertGenerator(totalVertices, stepEdges, initialClique, a);
        for (int i = 0; i < clusters; i++) {
            c[i] = scaleFreeGenerator.generate(factory);
        }

        Vertex[] randomVertices = new Vertex[clusters];
        for (int i = 0; i < clusters; i++) {
            randomVertices[i] = c[i].getRandomVertex();
        }

        T g = GraphUtils.combineGraphs(factory, c);

        for (int i = 0; i < clusters; i++) {
            Vertex s = randomVertices[i];
            Vertex t = randomVertices[(i + 1) % clusters];
            g.addEdges(s, t);
        }

        g.setGraphType("BarabasiAlbertCluster");
        g.setMeta("totalVertices", String.valueOf(totalVertices));
        g.setMeta("initialClique", String.valueOf(initialClique));
        g.setMeta("stepEdges", String.valueOf(stepEdges));
        g.setMeta("a", String.valueOf(a));
        g.setMeta("clusters", String.valueOf(clusters));

        return g;
    }
}
