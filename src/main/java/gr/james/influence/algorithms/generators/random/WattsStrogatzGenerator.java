package gr.james.influence.algorithms.generators.random;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Conditions;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WattsStrogatzGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
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
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

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

        return g;
    }
}
