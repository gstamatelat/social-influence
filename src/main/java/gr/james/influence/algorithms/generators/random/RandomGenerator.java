package gr.james.influence.algorithms.generators.random;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;

import java.util.Random;

/**
 * <p>Random graph evolving generator implementing the G(n,p) model of Edgar Gilbert.</p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model">
 * https://en.wikipedia.org/wiki/Erdős–Rényi_model</a>
 */
public class RandomGenerator implements GraphGenerator {
    private int totalVertices;
    private double p;
    private boolean directed;

    public RandomGenerator(int totalVertices, double p) {
        this.totalVertices = totalVertices;
        this.p = p;
        this.directed = true;
    }

    public RandomGenerator(int totalVertices, double p, boolean directed) {
        this.totalVertices = totalVertices;
        this.p = p;
        this.directed = directed;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.create();

        while (g.getVerticesCount() < totalVertices) {
            V v = g.addVertex();
            for (V y : g) {
                if (!v.equals(y)) {
                    if (directed) {
                        if (r.nextDouble() < p) {
                            g.addEdge(v, y);
                        }
                        if (r.nextDouble() < p) {
                            g.addEdge(y, v);
                        }
                    } else {
                        if (r.nextDouble() < p) {
                            g.addEdges(v, y);
                        }
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