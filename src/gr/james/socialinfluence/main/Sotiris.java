package gr.james.socialinfluence.main;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;

public class Sotiris {
    public static void main(String[] args) {
        Graph g = BarabasiAlbert.generate(10000, 2, 1, 1.0);
        Vertex v = g.getRandomVertex();

        long now = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            g.getOutEdges(v);
        }
        System.out.println(System.currentTimeMillis() - now);

        now = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            g.getOutEdges();
        }
        System.out.println(System.currentTimeMillis() - now);
    }
}