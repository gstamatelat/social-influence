package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.RandomHelper;

public class WattsStrogatzGenerator<T extends Graph> implements GraphGenerator<T> {
    double p;
    private Class<T> type;
    private int n, k;

    public WattsStrogatzGenerator(Class<T> type, int n, int k, double b) {
        Conditions.checkArgument(k % 2 == 0, "k must be an even number, got %d", k);
        Conditions.checkArgument(b >= 0 && b <= 1, "b must be inside [0,1], got %f", b);
        Conditions.checkArgument(k < n, "n must be smaller than k");

        this.type = type;
        this.n = n;
        this.k = k;
        this.p = b;
    }

    @Override
    public T create() {
        T g = Helper.instantiateGeneric(type);
        g.addVertices(n);
        Vertex sub;

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                g.addEdge(g.getVertexFromIndex(i), g.getVertexFromIndex((i + j) % n), true);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (RandomHelper.getRandom().nextDouble() <= p) {
                    g.removeEdge(g.getVertexFromIndex(i), g.getVertexFromIndex((i + j) % n));
                    g.removeEdge(g.getVertexFromIndex((i + j) % n), g.getVertexFromIndex(i));
                    sub = g.getRandomVertex();
                    while (sub == g.getVertexFromIndex(i) || sub == g.getVertexFromIndex((i + j) % n) || g.containsEdge(g.getVertexFromIndex(i), sub) || g.containsEdge(sub, g.getVertexFromIndex(i))) {
                        sub = g.getRandomVertex();
                    }
                    g.addEdge(g.getVertexFromIndex(i), sub, true);
                }
            }
        }

        return g;
    }
}
