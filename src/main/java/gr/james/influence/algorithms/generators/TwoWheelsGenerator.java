package gr.james.influence.algorithms.generators;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;

import java.util.Random;

public class TwoWheelsGenerator implements GraphGenerator {
    private int wheelVertices;

    public TwoWheelsGenerator(int wheelVertices) {
        this.wheelVertices = wheelVertices;
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory, Random r) {
        WheelGenerator wheelGenerator = new WheelGenerator(wheelVertices);
        T g1 = wheelGenerator.generate(factory);
        T g2 = wheelGenerator.generate(factory);

        Vertex a = g1.getVertexFromIndex(0);
        Vertex b = g2.getVertexFromIndex(0);

        T g = GraphUtils.combineGraphs(factory, new Graph[]{g1, g2});
        GraphUtils.fuseVertices(g, new Vertex[]{a, b});

        g.setGraphType("TwoWheels");
        g.setMeta("wheelVertices", String.valueOf(wheelVertices));

        return g;
    }
}
