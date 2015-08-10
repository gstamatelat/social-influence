package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.EvolvingGraphGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.RandomHelper;

/**
 * <p>Random graph evolving generator implementing the G(n,p) model of Edgar Gilbert.</p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model">
 * https://en.wikipedia.org/wiki/Erdős–Rényi_model</a>
 */
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
        for (Vertex y : g) {
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
        g.setMeta(Finals.TYPE_META, "Random")
                .setMeta("totalVertices", String.valueOf(totalVertices))
                .setMeta("p", String.valueOf(p));
    }
}
