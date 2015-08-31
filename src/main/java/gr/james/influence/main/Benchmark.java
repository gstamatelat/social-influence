package gr.james.influence.main;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.MemoryGraph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Helper;

public class Benchmark {
    public static void main(String[] args) {
        Graph g = new MemoryGraph();

        System.out.printf("%e%n", Helper.benchmark(g::addVertex, 1.0));

        Vertex v = new Vertex();
        System.out.printf("contains (false): %e%n", Helper.benchmark(() -> g.containsVertex(v), 1.0));

        Vertex y = g.getRandomVertex();
        System.out.printf("contains (true): %e%n", Helper.benchmark(() -> g.containsVertex(y), 1.0));
    }
}
