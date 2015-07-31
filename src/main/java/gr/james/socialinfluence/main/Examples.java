package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.io.IOException;
import java.util.List;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
        List<Vertex> vertexList = g.getVerticesAsList();
        for (int i = 0; i < g.getVerticesCount(); i++) {
            Vertex v = vertexList.get(i);
            // Do something with v
        }
    }
}
