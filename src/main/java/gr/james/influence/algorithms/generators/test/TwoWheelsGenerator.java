package gr.james.influence.algorithms.generators.test;

import gr.james.influence.algorithms.generators.basic.WheelGenerator;
import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.GraphFactory;
import gr.james.influence.api.graph.VertexProvider;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Finals;

import java.util.Arrays;
import java.util.Random;

public class TwoWheelsGenerator implements GraphGenerator {
    private int wheelVertices;

    public TwoWheelsGenerator(int wheelVertices) {
        this.wheelVertices = wheelVertices;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r, VertexProvider<V> vertexProvider) {
        WheelGenerator wheelGenerator = new WheelGenerator(wheelVertices);
        Graph<V, E> g1 = wheelGenerator.generate(factory, vertexProvider);
        Graph<V, E> g2 = wheelGenerator.generate(factory, vertexProvider);

        V a = g1.getVertexFromIndex(0);
        V b = g2.getVertexFromIndex(0);

        Graph<V, E> g = Graphs.combineGraphs(factory, Arrays.asList(g1, g2));
        Graphs.fuseVertices(g, Arrays.asList(a, b), vertexProvider);

        g.setMeta(Finals.TYPE_META, "TwoWheels");
        g.setMeta("wheelVertices", String.valueOf(wheelVertices));

        return g;
    }
}
