package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.GraphException;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.RandomHelper;

public class SmallWorldNetworkGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int n, k;
    double p;

    public SmallWorldNetworkGenerator(Class<T> type, int n, int k, double p){

        if (k % 2 != 0) {
            throw new GraphException("k must be an even number, found " + k);
        }
        if (p < 0 || p > 1){
            throw new GraphException("p must be between 0 and 1, found " + p);
        }

        this.type = type;
        this.n = n;
        this.k = k;
        this.p = p;
    }

    @Override
    public T create() {

        T g = Helper.instantiateGeneric(type);
        g.addVertices(n);
        Vertex sub;

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (i + j < n) {
                    g.addEdge(g.getVertexFromIndex(i), g.getVertexFromIndex(i + j), true);
                } else {
                    g.addEdge(g.getVertexFromIndex(i), g.getVertexFromIndex(i + j - n), true);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (i + j < n) {
                    if (p >= RandomHelper.getRandom().nextDouble()) {
                        g.removeEdge(g.getVertexFromIndex(i), g.getVertexFromIndex(i + j));
                        g.removeEdge(g.getVertexFromIndex(i + j), g.getVertexFromIndex(i));
                        sub = g.getRandomVertex();
                        while (sub == g.getVertexFromIndex(i) || sub == g.getVertexFromIndex(i + j) || g.containsEdge(g.getVertexFromIndex(i), sub) || g.containsEdge(sub, g.getVertexFromIndex(i))) {
                            sub = g.getRandomVertex();
                        }
                        g.addEdge(g.getVertexFromIndex(i), sub, true);
                    }
                } else {
                    if (p >= RandomHelper.getRandom().nextDouble()) {
                        g.removeEdge(g.getVertexFromIndex(i), g.getVertexFromIndex(i + j - n));
                        g.removeEdge(g.getVertexFromIndex(i + j - n), g.getVertexFromIndex(i));
                        sub = g.getRandomVertex();
                        while (sub == g.getVertexFromIndex(i) || sub == g.getVertexFromIndex(i + j - n) || g.containsEdge(g.getVertexFromIndex(i), sub) || g.containsEdge(sub, g.getVertexFromIndex(i))) {
                            sub = g.getRandomVertex();
                        }
                        g.addEdge(g.getVertexFromIndex(i), sub, true);
                    }
                }
            }
        }

        return g;
    }
}
