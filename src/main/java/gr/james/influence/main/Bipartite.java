package gr.james.influence.main;

import gr.james.influence.algorithms.scoring.HITS;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.graph.io.EdgesImporterExporter;
import gr.james.influence.util.collections.GraphState;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bipartite {
    public static void main(String[] args) throws IOException {
        SimpleGraph g = new EdgesImporterExporter().from(new FileInputStream("/home/james/Documents/bipartite.csv"));

        List<String> g1v = g.getVertices().stream()
                .filter(vertex -> Arrays.asList(new String[]{"a1", "a2", "a3", "b1", "b2", "b3"}).contains(vertex))
                .collect(Collectors.toList());

        List<String> g2v = g.getVertices().stream()
                .filter(vertex -> Arrays.asList(new String[]{"b1", "b2", "b3", "c1", "c2", "c3"}).contains(vertex))
                .collect(Collectors.toList());

        SimpleGraph g1 = Graphs.subGraph(g, g1v);
        SimpleGraph g2 = Graphs.subGraph(g, g2v);

        GraphState<String, HITS.HITSScore> hits = HITS.execute(g, 0.0);
        GraphState<String, HITS.HITSScore> hits1 = HITS.execute(g1, 0.0);
        GraphState<String, HITS.HITSScore> hits2 = HITS.execute(g2, 0.0);

        System.out.println(hits.toString());
        System.out.println(hits1.toString());
        System.out.println(hits2.toString());
    }
}
