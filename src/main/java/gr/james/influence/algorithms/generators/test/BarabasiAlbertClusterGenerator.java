package gr.james.influence.algorithms.generators.test;

import gr.james.influence.algorithms.generators.GraphGenerator;
import gr.james.influence.algorithms.generators.random.BarabasiAlbertGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BarabasiAlbertClusterGenerator<V, E> implements GraphGenerator<DirectedGraph<V, E>, V, E> {
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
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        // DirectedGraph<V, E>[] c = new DirectedGraph<V, E>[clusters];
        List<DirectedGraph<V, E>> c = new ArrayList<>(clusters);

        BarabasiAlbertGenerator<V, E> scaleFreeGenerator = new BarabasiAlbertGenerator<>(totalVertices, stepEdges, initialClique, a);
        for (int i = 0; i < clusters; i++) {
            c.add(scaleFreeGenerator.generate(r, vertexProvider));
            // c[i] = scaleFreeGenerator.generate(graphFactory, r);
        }

        assert c.size() == clusters;

        // V[] randomVertices = new V[clusters];
        List<V> randomVertices = new ArrayList<>(clusters);
        for (int i = 0; i < clusters; i++) {
            randomVertices.add(Graphs.getRandomVertex(c.get(i), r));
            // randomVertices.set(i, c.get(i).getRandomVertex(r));
            // randomVertices[i] = c[i].getRandomVertex(r);
        }

        assert randomVertices.size() == clusters;

        DirectedGraph<V, E> g = Graphs.combineGraphs(c);

        for (int i = 0; i < clusters; i++) {
            // Vertex s = randomVertices[i];
            V s = randomVertices.get(i);
            // V t = randomVertices[(i + 1) % clusters];
            V t = randomVertices.get((i + 1) % clusters);
            g.addEdges(s, t);
        }

        return g;
    }
}
