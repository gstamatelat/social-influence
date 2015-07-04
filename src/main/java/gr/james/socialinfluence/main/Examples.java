package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.WheelGenerator;

public class Examples {
    public static void main(String[] args) {
        GraphGenerator generator = new WheelGenerator<>(MemoryGraph.class, 100);
        Graph randomGraph = generator.create();
        System.out.println(randomGraph);
    }
}
