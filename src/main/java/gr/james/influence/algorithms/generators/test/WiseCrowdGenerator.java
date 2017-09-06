package gr.james.influence.algorithms.generators.test;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.util.Conditions;

import java.util.Random;

// From Jackson, figure 8.3.7
public class WiseCrowdGenerator implements GraphGenerator {
    private int totalVertices;
    private double delta;

    public WiseCrowdGenerator(int totalVertices, double delta) {
        Conditions.requireArgument(totalVertices > 1, "totalVertices must be > 1, got %d", totalVertices);
        Conditions.requireArgument(delta > 0.0 && delta < 1.0, "delta must be in (0,1), got %f", delta);
        this.totalVertices = totalVertices;
        this.delta = delta;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.createGraph();

        V boss = g.addVertex();

        while (g.getVerticesCount() < totalVertices) {
            V v = g.addVertex();
            g.addEdge(boss, v, delta / (totalVertices - 1));
            g.addEdge(v, boss, delta);
        }

        for (V v : g) {
            g.addEdge(v, v, 1 - delta);
        }

        g.setGraphType("WiseCrowd");
        g.setMeta("totalVertices", String.valueOf(totalVertices));
        g.setMeta("delta", String.valueOf(delta));

        return g;
    }
}
