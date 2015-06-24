package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

public class Path {
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices, boolean cycle) {
        Graph g = Helper.instantiateGeneric(type);

        Vertex startVertex = g.addVertex(), previousVertex = startVertex;
        while (g.getVerticesCount() < totalVertices) {
            Vertex newVertex = g.addVertex();
            g.addEdge(previousVertex, newVertex, true);
            previousVertex = newVertex;
        }
        if (cycle) {
            g.addEdge(previousVertex, startVertex, true);
        }

        return g.setName("Path").setMeta(String.format("%s,totalVertices=%d,cycle=%b", "Path", totalVertices, cycle));
    }
}