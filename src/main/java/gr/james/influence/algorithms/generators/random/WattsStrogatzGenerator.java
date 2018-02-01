package gr.james.influence.algorithms.generators.random;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.GraphFactory;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WattsStrogatzGenerator implements GraphGenerator {
    private int n;
    private int k;
    private double b;

    public WattsStrogatzGenerator(int n, int k, double b) {
        Conditions.requireArgument(b >= 0 && b <= 1, "b must be inside [0,1], got %f", b);
        Conditions.requireArgument(k < n, "k must be smaller than n");
        Conditions.requireArgument(k % 2 == 0, "k must be an even number, got %d", k);

        this.n = n;
        this.k = k;
        this.b = b;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        Graph<V, E> g = factory.createWeightedDirected();

        List<V> l = g.addVertices(n, vertexProvider);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                g.addEdges(l.get(i), l.get((i + j) % n));
            }
        }

        V sub;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (r.nextDouble() <= b) {
                    V a = l.get(i);
                    V b = l.get((i + j) % n);
                    g.removeEdges(a, b);
                    do {
                        sub = Graphs.getRandomVertex(g, r);
                    } while (sub == a || sub == b || g.containsEdge(a, sub));
                    g.addEdges(a, sub);
                }
            }
        }

        g.setMeta(Finals.TYPE_META, "WattsStrogatz");
        g.setMeta("n", String.valueOf(n));
        g.setMeta("k", String.valueOf(k));
        g.setMeta("b", String.valueOf(b));

        return g;
    }
}
