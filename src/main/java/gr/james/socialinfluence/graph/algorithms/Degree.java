package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.collections.states.IntegerGraphState;
import gr.james.socialinfluence.graph.Vertex;

public class Degree {
    public static IntegerGraphState execute(Graph g, boolean in) {
        // TODO: I don't like this very much, returning double etc
        IntegerGraphState degreesState = new IntegerGraphState();
        for (Vertex v : g.getVertices()) {
            degreesState.put(v, (in) ? g.getInDegree(v) : g.getOutDegree(v));
        }
        return degreesState;
    }
}
