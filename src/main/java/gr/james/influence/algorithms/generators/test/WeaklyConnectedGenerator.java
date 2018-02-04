package gr.james.influence.algorithms.generators.test;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

public class WeaklyConnectedGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    public WeaklyConnectedGenerator() {
    }

    @Override
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

        V v1 = g.addVertex(vertexProvider);
        V v2 = g.addVertex(vertexProvider);
        V v3 = g.addVertex(vertexProvider);
        V v4 = g.addVertex(vertexProvider);

        g.addEdges(v1, v2);
        g.addEdges(v3, v4);
        g.addEdge(v2, v3);

        return g;
    }
}
