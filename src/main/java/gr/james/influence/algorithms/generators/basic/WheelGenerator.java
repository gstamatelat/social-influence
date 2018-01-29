package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.GraphFactory;
import gr.james.influence.api.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.Random;

public class WheelGenerator implements GraphGenerator {
    private int totalVertices;

    public WheelGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r, VertexProvider<V> vertexProvider) {
        Graph<V, E> g = new CycleGenerator(totalVertices - 1).generate(factory, vertexProvider);

        V n = g.addVertex();
        for (V v : g) {
            if (!v.equals(n)) {
                g.addEdges(v, n);
            }
        }

        g.clearMeta();
        g.setMeta(Finals.TYPE_META, "Wheel");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
