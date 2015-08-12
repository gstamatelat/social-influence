package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
        for (Vertex v : g) {
            // Do something with v
            System.out.println(v);
        }
    }
}
