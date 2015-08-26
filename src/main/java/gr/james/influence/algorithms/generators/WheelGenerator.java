package gr.james.influence.algorithms.generators;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.graph.Vertex;

public class WheelGenerator implements GraphGenerator {
    private int totalVertices;

    public WheelGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory) {
        T g = new CycleGenerator(totalVertices - 1).generate(factory);

        Vertex n = g.addVertex();
        for (Vertex v : g) {
            if (!v.equals(n)) {
                g.addEdges(v, n);
            }
        }

        g.clearMeta();
        g.setGraphType("Wheel");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
