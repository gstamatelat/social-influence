package gr.james.influence.main;

import gr.james.influence.algorithms.generators.BarabasiAlbertGenerator;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.util.Helper;

public class Benchmark {
    public static void main(String[] args) {
        Graph g = new BarabasiAlbertGenerator(250, 2, 2, 1.0).generate();

        System.out.printf("%e%n", Helper.benchmark(() -> GraphUtils.deepCopy(g), 1.0));

        //System.out.printf("%e%n", Helper.benchmark(() -> GraphUtils.deepCopyObsolete(g, MemoryGraph::new, g.getVertices()), 1.0));

        /*Vertex v = new Vertex();
        System.out.printf("contains (false): %e%n", Helper.benchmark(() -> g.containsVertex(v), 1.0));

        Vertex y = g.getRandomVertex();
        System.out.printf("contains (true): %e%n", Helper.benchmark(() -> g.containsVertex(y), 1.0));*/
    }
}
