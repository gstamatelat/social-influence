package gr.james.influence.main;

import gr.james.influence.algorithms.scoring.HITS;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;
import gr.james.influence.graph.io.Edges;
import gr.james.influence.util.collections.GraphState;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bipartite {
    public static void main(String[] args) throws IOException {
        Graph g = new Edges().from(new FileInputStream("/home/james/Documents/bipartite.csv"));

        List<Vertex> g1v = g.getVertices().stream()
                .filter(vertex -> Arrays.asList(new String[]{"a1", "a2", "a3", "b1", "b2", "b3"}).contains(vertex.getLabel()))
                .collect(Collectors.toList());

        List<Vertex> g2v = g.getVertices().stream()
                .filter(vertex -> Arrays.asList(new String[]{"b1", "b2", "b3", "c1", "c2", "c3"}).contains(vertex.getLabel()))
                .collect(Collectors.toList());

        Graph g1 = GraphUtils.subGraph(g, g1v);
        Graph g2 = GraphUtils.subGraph(g, g2v);

        GraphState<HITS.HITSScore> hits = HITS.execute(g, 0.0);
        GraphState<HITS.HITSScore> hits1 = HITS.execute(g1, 0.0);
        GraphState<HITS.HITSScore> hits2 = HITS.execute(g2, 0.0);

        System.out.println(hits.toString());
        System.out.println(hits1.toString());
        System.out.println(hits2.toString());
    }
}
