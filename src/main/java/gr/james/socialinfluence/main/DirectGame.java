package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.algorithms.scoring.Closeness;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.MemoryGraph;

public class DirectGame {
    public static void main(String[] args) {
        Graph g = new BarabasiAlbertGenerator<>(MemoryGraph.class, 50, 2, 2, 1).create();

        GraphState<Double> r = Closeness.execute(g, true, g.getVertices(), new Closeness.ClosenessSumHandler());

        int t = 0;
    }
}
