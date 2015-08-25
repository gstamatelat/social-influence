package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;

public class PathGenerator implements GraphGenerator {
    private int totalVertices;

    public PathGenerator(int totalVertices) {
        this.totalVertices = totalVertices;
    }

    @Override
    public <T extends Graph> T generate(GraphFactory<T> factory) {
        T g = factory.create();

        Vertex startVertex = g.addVertex(), previousVertex = startVertex;
        while (g.getVerticesCount() < totalVertices) {
            Vertex newVertex = g.addVertex();
            g.addEdges(previousVertex, newVertex);
            previousVertex = newVertex;
        }

        g.setGraphType("Path");
        g.setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
