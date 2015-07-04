package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.EvolvingGraphGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.helper.RandomHelper;

public class RandomGenerator<T extends Graph> implements EvolvingGraphGenerator<T> {
    private Class<T> type;
    private int totalVertices;
    private double p;

    private T g;

    public RandomGenerator(Class<T> type, int totalVertices, double p) {
        this.type = type;
        this.totalVertices = totalVertices;
        this.p = p;
        reset();
    }

    @Override
    public T evolve() {
        Vertex v = g.addVertex();
        for (Vertex y : g.getVertices()) {
            if (!v.equals(y)) {
                if (RandomHelper.getRandom().nextDouble() < p) {
                    g.addEdge(v, y);
                }
                if (RandomHelper.getRandom().nextDouble() < p) {
                    g.addEdge(y, v);
                }
            }
        }
        return g;
    }

    @Override
    public boolean canEvolve() {
        return (g.getVerticesCount() < totalVertices);
    }

    @Override
    public void reset() {
        g = Helper.instantiateGeneric(type);
        g.setMeta("type", "Random")
                .setMeta("totalVertices", String.valueOf(totalVertices))
                .setMeta("p", String.valueOf(p));
    }

    @Override
    public T create() {
        while (this.canEvolve()) {
            this.evolve();
        }
        T r = g;
        this.reset();
        return r;
    }
}
