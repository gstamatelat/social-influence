package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.algorithms.Closeness;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertGenerator;

public class DirectGame {
    public static void main(String[] args) {
        Graph g = new BarabasiAlbertGenerator<>(MemoryGraph.class, 50, 2, 2, 1).create();

        GraphState<Double> r = Closeness.execute(g, true, g.getVertices(), new Closeness.ClosenessSumHandler());

        int t = 0;
    }
}
