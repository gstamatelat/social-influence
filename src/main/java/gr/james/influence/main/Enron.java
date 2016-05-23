package gr.james.influence.main;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.io.Edges;

import java.io.FileInputStream;
import java.io.IOException;

public class Enron {
    public static void main(String[] args) throws IOException {
        Graph g = new Edges().from(new FileInputStream("C:\\Users\\James\\Downloads\\graph.csv"));
        int k = 0;
    }
}
