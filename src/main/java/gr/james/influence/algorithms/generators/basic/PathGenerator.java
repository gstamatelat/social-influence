package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.util.Finals;

import java.util.Random;

public class PathGenerator implements GraphGenerator {
    private int totalVertices;

    public PathGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.createGraph();

        V startVertex = g.addVertex(), previousVertex = startVertex;
        while (g.getVerticesCount() < totalVertices) {
            V newVertex = g.addVertex();
            g.addEdges(previousVertex, newVertex);
            previousVertex = newVertex;
        }

        g.setMeta(Finals.TYPE_META, "Path");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
