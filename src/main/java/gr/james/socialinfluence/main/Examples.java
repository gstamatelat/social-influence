package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.PathGenerator;
import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new PathGenerator<>(MemoryGraph.class, 25).create();
        System.out.println(PageRank.execute(g, 0.15));
    }
}
