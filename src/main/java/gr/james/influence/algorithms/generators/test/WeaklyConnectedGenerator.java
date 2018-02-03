package gr.james.influence.algorithms.generators.test;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.MemoryGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.Map;
import java.util.Random;

public class WeaklyConnectedGenerator<V, E> implements GraphGenerator<Graph<V, E>, V, E> {
    public WeaklyConnectedGenerator() {
    }

    @Override
    public Graph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        Graph<V, E> g = new MemoryGraph<>();

        V v1 = g.addVertex(vertexProvider);
        V v2 = g.addVertex(vertexProvider);
        V v3 = g.addVertex(vertexProvider);
        V v4 = g.addVertex(vertexProvider);

        g.addEdges(v1, v2);
        g.addEdges(v3, v4);
        g.addEdge(v2, v3);

        g.setMeta(Finals.TYPE_META, "WeaklyConnected");

        return g;
    }
}
