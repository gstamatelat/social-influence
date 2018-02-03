package gr.james.influence.main;

import gr.james.influence.algorithms.generators.random.BarabasiAlbertGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Helper;

public class Benchmark {
    public static void main(String[] args) {
        Graph<Integer, Object> g = new BarabasiAlbertGenerator<>(250, 2, 2, 1.0).generate();

        System.out.printf("%e%n", Helper.benchmark(() -> Graphs.deepCopy(g), 1.0));

        //System.out.printf("%e%n", Helper.benchmark(() -> Graphs.deepCopyObsolete(g, MemoryGraph::new, g.getVertices()), 1.0));

        /*Vertex v = new Vertex();
        System.out.printf("contains (false): %e%n", Helper.benchmark(() -> g.containsVertex(v), 1.0));

        Vertex y = g.getRandomVertex();
        System.out.printf("contains (true): %e%n", Helper.benchmark(() -> g.containsVertex(y), 1.0));*/
    }
}
