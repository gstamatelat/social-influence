package gr.james.influence.algorithms.generators.test;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;

import java.util.Random;

public class WeaklyConnectedGenerator implements GraphGenerator {
    public WeaklyConnectedGenerator() {
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.createGraph();

        V v1 = g.addVertex();
        V v2 = g.addVertex();
        V v3 = g.addVertex();
        V v4 = g.addVertex();

        g.addEdges(v1, v2);
        g.addEdges(v3, v4);
        g.addEdge(v2, v3);

        g.setGraphType("WeaklyConnected");

        return g;
    }
}
