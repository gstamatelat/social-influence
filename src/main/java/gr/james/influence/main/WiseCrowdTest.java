package gr.james.influence.main;

import gr.james.influence.algorithms.generators.test.WiseCrowdGenerator;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.io.DotExporter;
import gr.james.influence.util.collections.GraphState;

import java.io.IOException;

public class WiseCrowdTest {
    public static void main(String[] args) throws IOException {
        DirectedGraph wiseCrowd = new WiseCrowdGenerator<Integer, Object>(11, 0.5).generate(VertexProvider.intProvider);
        new DotExporter().to(wiseCrowd, System.out);
        GraphState<String, Double> pr = PageRank.execute(wiseCrowd, 0.0);
        System.out.println(pr);
    }
}
