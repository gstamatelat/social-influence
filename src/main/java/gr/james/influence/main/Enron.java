package gr.james.influence.main;

import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.io.Edges;
import gr.james.influence.util.collections.GraphState;

import java.io.FileInputStream;
import java.io.IOException;

public class Enron {
    public static void main(String[] args) throws IOException {
        Graph g = new Edges().from(new FileInputStream("C:\\Users\\James\\Downloads\\graph.csv"));
        GraphState<Double> pr = PageRank.execute(g, 0.15);
        System.out.println(pr);
    }
}
