package gr.james.influence.algorithms.generators;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.graph.Vertex;

import java.util.Random;

public class WeaklyConnectedGenerator implements GraphGenerator {
    public WeaklyConnectedGenerator() {
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory, Random r) {
        T g = factory.create();

        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        Vertex v3 = g.addVertex();
        Vertex v4 = g.addVertex();

        g.addEdges(v1, v2);
        g.addEdges(v3, v4);
        g.addEdge(v2, v3);

        g.setGraphType("WeaklyConnected");

        return g;
    }
}
