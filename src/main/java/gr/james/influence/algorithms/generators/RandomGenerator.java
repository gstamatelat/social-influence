package gr.james.influence.algorithms.generators;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.RandomHelper;

/**
 * <p>Random graph evolving generator implementing the G(n,p) model of Edgar Gilbert.</p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model">
 * https://en.wikipedia.org/wiki/Erdős–Rényi_model</a>
 */
public class RandomGenerator implements GraphGenerator {
    private int totalVertices;
    private double p;

    public RandomGenerator(int totalVertices, double p) {
        this.totalVertices = totalVertices;
        this.p = p;
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory) {
        T g = factory.create();

        while (g.getVerticesCount() < totalVertices) {
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
        }

        g.setGraphType("Random");
        g.setMeta("totalVertices", String.valueOf(totalVertices));
        g.setMeta("p", String.valueOf(p));

        return g;
    }
}
