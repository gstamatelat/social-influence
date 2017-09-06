package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;

import java.util.Random;

public class CompleteGenerator implements GraphGenerator {
    private int totalVertices;

    public CompleteGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.createGraph();

        g.addEdges(g.addVertices(totalVertices));

        g.setGraphType("Complete");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
