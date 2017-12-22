package gr.james.influence.main;

import gr.james.influence.algorithms.generators.test.MasterGenerator;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.io.DotExporter;

import java.io.IOException;

public class Reverse {
    public static void main(String[] args) throws IOException {
        SimpleGraph g = new MasterGenerator().generate();
        new DotExporter().to(g, System.out);
        Graphs.reverse(g);
        new DotExporter().to(g, System.out);
    }
}
