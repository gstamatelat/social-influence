package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.Finals;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        int n = 1000;
        double p = 0.05;

        MemoryGraph g = new RandomGenerator<>(MemoryGraph.class, n, p).create();

        long now = System.currentTimeMillis();
        while (g.getVerticesCount() > 0) {
            g.removeVertex(g.getRandomVertex());
        }
        Finals.LOG.info("removeVertex: {}", System.currentTimeMillis() - now);

        g = new RandomGenerator<>(MemoryGraph.class, n, p).create();

        now = System.currentTimeMillis();
        while (g.getVerticesCount() > 0) {
            /*g.removeVertexNew(g.getRandomVertex());*/
        }
        Finals.LOG.info("removeVertexNew: {}", System.currentTimeMillis() - now);
    }
}
