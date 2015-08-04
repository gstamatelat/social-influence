package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.states.IntegerGraphState;

public class Degree {
    public static GraphState<Integer> execute(Graph g, boolean in) {
        GraphState<Integer> degreesState = new IntegerGraphState();
        for (Vertex v : g.getVerticesAsList()) {
            degreesState.put(v, (in) ? g.getInDegree(v) : g.getOutDegree(v));
        }
        return degreesState;
    }
}
