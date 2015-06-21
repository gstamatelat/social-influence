package gr.james.socialinfluence.main;

import gr.james.socialinfluence.collections.GraphState;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.DeGroot;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.helper.RandomHelper;

import java.io.IOException;

public class CheckDraw {
    public static void main(String[] args) throws IOException {
        Graph g = BarabasiAlbert.generate(50, 2, 1, 1.0);

        GraphState initialState = new GraphState(g, 0.0);
        for (Vertex v : g.getVertices()) {
            initialState.put(v, RandomHelper.getRandom().nextDouble());
        }

        GraphState finalState = DeGroot.execute(g, initialState, 0.0, true);
    }
}