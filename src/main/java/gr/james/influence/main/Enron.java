package gr.james.influence.main;

import gr.james.influence.algorithms.scoring.Degree;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.io.Edges;
import gr.james.influence.util.collections.GraphState;

import java.io.FileInputStream;
import java.io.IOException;

public class Enron {
    public static void main(String[] args) throws IOException {
        Graph g = new Edges().from(new FileInputStream("C:\\Users\\James\\Downloads\\graph.csv"));

        GraphState<Integer> inDeg = Degree.execute(g, true);
        GraphState<Integer> outDeg = Degree.execute(g, false);

        // In Degree distribution
        System.out.println("IN DEGREE");
        int maxIn = inDeg.values().stream().mapToInt(i -> i).max().getAsInt();
        int[] distIn = new int[maxIn + 1];
        for (int d : inDeg.values()) {
            distIn[d]++;
        }
        for (int i = 0; i < distIn.length; i++) {
            System.out.printf("%d %d%n", i, distIn[i]);
        }

        // Out Degree distribution
        System.out.println("OUT DEGREE");
        int maxOut = outDeg.values().stream().mapToInt(i -> i).max().getAsInt();
        int[] distOut = new int[maxOut + 1];
        for (int d : outDeg.values()) {
            distOut[d]++;
        }
        for (int i = 0; i < distOut.length; i++) {
            System.out.printf("%d %d%n", i, distOut[i]);
        }
    }
}
