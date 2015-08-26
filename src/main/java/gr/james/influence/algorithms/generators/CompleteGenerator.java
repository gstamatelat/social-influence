package gr.james.influence.algorithms.generators;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;

public class CompleteGenerator implements GraphGenerator {
    private int totalVertices;

    public CompleteGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory) {
        T g = factory.create();

        g.addEdges(g.addVertices(totalVertices));

        g.setGraphType("Complete");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
