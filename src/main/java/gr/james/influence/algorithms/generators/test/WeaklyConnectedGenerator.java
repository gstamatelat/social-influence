package gr.james.influence.algorithms.generators.test;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.GraphFactory;
import gr.james.influence.api.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.Random;

public class WeaklyConnectedGenerator implements GraphGenerator {
    public WeaklyConnectedGenerator() {
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r, VertexProvider<V> vertexProvider) {
        Graph<V, E> g = factory.createGraph();

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
