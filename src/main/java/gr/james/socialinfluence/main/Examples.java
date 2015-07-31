package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.CycleGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.io.Dot;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new CycleGenerator<>(MemoryGraph.class, 100).create();
        Dot dot = new Dot();
        dot.to(g, System.out);
    }
}
