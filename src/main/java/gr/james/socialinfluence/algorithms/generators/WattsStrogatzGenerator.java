package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.RandomHelper;

public class WattsStrogatzGenerator<T extends Graph> implements GraphGenerator<T> {
    double b;
    private Class<T> type;
    private int n, k;

    public WattsStrogatzGenerator(Class<T> type, int n, int k, double b) {
        Conditions.requireArgument(k % 2 == 0, "k must be an even number, got %d", k);
        Conditions.requireArgument(b >= 0 && b <= 1, "b must be inside [0,1], got %f", b);
        Conditions.requireArgument(k < n, "n must be smaller than k");

        this.type = type;
        this.n = n;
        this.k = k;
        this.b = b;
    }

    @Override
    public T create() {
        T g = Helper.instantiateGeneric(type);

        g.addVertices(n);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                g.addEdge(g.getVertexFromIndex(i), g.getVertexFromIndex((i + j) % n), true);
            }
        }

        Vertex sub;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (RandomHelper.getRandom().nextDouble() <= b) {
                    Vertex a = g.getVertexFromIndex(i);
                    Vertex b = g.getVertexFromIndex((i + j) % n);
                    g.removeEdges(a, b);
                    do {
                        sub = g.getRandomVertex();
                    } while (sub == a || sub == b || g.containsEdge(a, sub));
                    g.addEdge(a, sub, true);
                }
            }
        }

        g.setMeta(Finals.TYPE_META, "WattsStrogatz")
                .setMeta("n", String.valueOf(n))
                .setMeta("k", String.valueOf(k))
                .setMeta("b", String.valueOf(b));

        return g;
    }
}
