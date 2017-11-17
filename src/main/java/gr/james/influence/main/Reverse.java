package gr.james.influence.main;

import gr.james.influence.algorithms.generators.test.MasterGenerator;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.graph.io.Dot;

import java.io.IOException;

public class Reverse {
    public static void main(String[] args) throws IOException {
        SimpleGraph g = new MasterGenerator().generate();
        new Dot().to(g, System.out);
        Graphs.reverse(g);
        new Dot().to(g, System.out);
    }
}
