package gr.james.influence.main;

import gr.james.influence.algorithms.generators.test.WiseCrowdGenerator;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.api.Graph;
import gr.james.influence.io.DotExporter;
import gr.james.influence.util.collections.GraphState;

import java.io.IOException;

public class WiseCrowdTest {
    public static void main(String[] args) throws IOException {
        Graph wiseCrowd = new WiseCrowdGenerator(11, 0.5).generate();
        new DotExporter().to(wiseCrowd, System.out);
        GraphState<String, Double> pr = PageRank.execute(wiseCrowd, 0.0);
        System.out.println(pr);
    }
}
