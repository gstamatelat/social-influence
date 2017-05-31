package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;

import java.util.Random;

public class CycleGenerator implements GraphGenerator {
    private int totalVertices;

    public CycleGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.create();

        V startVertex = g.addVertex(), previousVertex = startVertex;
        while (g.getVerticesCount() < totalVertices) {
            V newVertex = g.addVertex();
            g.addEdges(previousVertex, newVertex);
            previousVertex = newVertex;
        }
        g.addEdges(startVertex, previousVertex);

        g.setGraphType("Cycle");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}