package gr.james.influence.algorithms.generators.test;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Conditions;

import java.util.Map;
import java.util.Random;

// From Jackson, figure 8.3.7
public class WiseCrowdGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    private int totalVertices;
    private double delta;

    public WiseCrowdGenerator(int totalVertices, double delta) {
        Conditions.requireArgument(totalVertices > 1, "totalVertices must be > 1, got %d", totalVertices);
        Conditions.requireArgument(delta > 0.0 && delta < 1.0, "delta must be in (0,1), got %f", delta);
        this.totalVertices = totalVertices;
        this.delta = delta;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

        V boss = g.addVertex(vertexProvider);

        while (g.vertexCount() < totalVertices) {
            V v = g.addVertex(vertexProvider);
            g.addEdge(boss, v, delta / (totalVertices - 1));
            g.addEdge(v, boss, delta);
        }

        for (V v : g) {
            g.addEdge(v, v, 1 - delta);
        }

        return g;
    }
}
