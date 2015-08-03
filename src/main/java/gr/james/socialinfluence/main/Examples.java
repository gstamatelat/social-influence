package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.distance.Dijkstra;
import gr.james.socialinfluence.algorithms.distance.FloydWarshall;
import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.Finals;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 500, 0.5).create();
        Finals.LOG.info("START");
        Dijkstra.executeDistanceMap(g);
        Finals.LOG.info("DONE DIJKSTRA");
        FloydWarshall.execute(g);
        Finals.LOG.info("DONE FLOYD-WARSHALL");
    }
}
