package gr.james.influence.algorithms.generators.random;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.graph.Graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        // Graph<V, E>[] c = new Graph<V, E>[clusters];
        List<Graph<V, E>> c = new ArrayList<>(clusters);

        GraphGenerator scaleFreeGenerator = new BarabasiAlbertGenerator(totalVertices, stepEdges, initialClique, a);
        for (int i = 0; i < clusters; i++) {
            c.add(scaleFreeGenerator.generate(factory, r));
            // c[i] = scaleFreeGenerator.generate(graphFactory, r);
        }

        assert c.size() == clusters;

        // V[] randomVertices = new V[clusters];
        List<V> randomVertices = new ArrayList<>(clusters);
        for (int i = 0; i < clusters; i++) {
            randomVertices.add(c.get(i).getRandomVertex(r));
            // randomVertices.set(i, c.get(i).getRandomVertex(r));
            // randomVertices[i] = c[i].getRandomVertex(r);
        }

        assert randomVertices.size() == clusters;

        Graph<V, E> g = Graphs.combineGraphs(factory, c);

        for (int i = 0; i < clusters; i++) {
            // Vertex s = randomVertices[i];
            V s = randomVertices.get(i);
            // V t = randomVertices[(i + 1) % clusters];
            V t = randomVertices.get((i + 1) % clusters);
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
