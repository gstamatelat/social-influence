package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.graph.Vertex;

public class TwoWheelsGenerator implements GraphGenerator {
    private int wheelVertices;

    public TwoWheelsGenerator(int wheelVertices) {
        this.wheelVertices = wheelVertices;
    }

    @Override
    public <T extends Graph> T create(GraphFactory<T> factory) {
        WheelGenerator wheelGenerator = new WheelGenerator(wheelVertices);
        T g1 = wheelGenerator.create(factory);
        T g2 = wheelGenerator.create(factory);

        Vertex a = g1.getVertexFromIndex(0);
        Vertex b = g2.getVertexFromIndex(0);

        T g = GraphUtils.combineGraphs(factory, new Graph[]{g1, g2});
        GraphUtils.fuseVertices(g, new Vertex[]{a, b});

        g.setGraphType("TwoWheels");
        g.setMeta("wheelVertices", String.valueOf(wheelVertices));

        return g;
    }
}
