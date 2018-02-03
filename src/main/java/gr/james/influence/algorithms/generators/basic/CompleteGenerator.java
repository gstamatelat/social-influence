package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.algorithms.generators.GraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.Map;
import java.util.Random;

public class CompleteGenerator<V, E> implements GraphGenerator<DirectedGraph<V, E>, V, E> {
    private int totalVertices;

    public CompleteGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

        g.addEdges(g.addVertices(totalVertices, vertexProvider));

        g.setMeta(Finals.TYPE_META, "Complete");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
