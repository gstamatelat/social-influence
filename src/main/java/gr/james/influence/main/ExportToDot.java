package gr.james.influence.main;

import gr.james.influence.algorithms.generators.random.BarabasiAlbertGenerator;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.io.Dot;

import java.io.IOException;

public class ExportToDot {
    public static void main(String[] args) throws IOException {
        Graph g = new BarabasiAlbertGenerator(20, 2, 2, 1.0).generate();
        new Dot().to(g, System.out);
    }
}
