package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.MemoryGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.Map;
import java.util.Random;

public class CompleteGenerator<V, E> implements GraphGenerator<Graph<V, E>, V, E> {
    private int totalVertices;

    public CompleteGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public Graph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        Graph<V, E> g = new MemoryGraph<>();

        g.addEdges(g.addVertices(totalVertices, vertexProvider));

        g.setMeta(Finals.TYPE_META, "Complete");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
