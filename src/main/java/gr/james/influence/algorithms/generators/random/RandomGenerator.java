package gr.james.influence.algorithms.generators.random;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * <p>Random graph evolving generator implementing the G(n,p) model of Edgar Gilbert.</p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model">
 * https://en.wikipedia.org/wiki/Erdős–Rényi_model</a>
 */
public class RandomGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
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
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

        while (g.vertexCount() < totalVertices) {
            V v = g.addVertex(vertexProvider);
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

        return g;
    }
}
