package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.RandomHelper;

import java.util.List;

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
    public <T extends Graph> T generate(GraphFactory<T> factory) {
        T g = factory.create();

        List<Vertex> l = g.addVertices(n);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                g.addEdges(l.get(i), l.get((i + j) % n));
            }
        }

        Vertex sub;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (RandomHelper.getRandom().nextDouble() <= b) {
                    Vertex a = l.get(i);
                    Vertex b = l.get((i + j) % n);
                    g.removeEdges(a, b);
                    do {
                        sub = g.getRandomVertex();
                    } while (sub == a || sub == b || g.containsEdge(a, sub));
                    g.addEdges(a, sub);
                }
            }
        }

        g.setGraphType("WattsStrogatz");
        g.setMeta("n", String.valueOf(n));
        g.setMeta("k", String.valueOf(k));
        g.setMeta("b", String.valueOf(b));

        return g;
    }
}
