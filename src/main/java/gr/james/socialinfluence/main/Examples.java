package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.io.Json;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 10, 0.05).create();
        Json h = new Json();
        h.to(g, System.out);
    }
}
