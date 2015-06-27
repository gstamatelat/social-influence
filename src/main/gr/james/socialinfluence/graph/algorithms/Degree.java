package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.collections.GraphState;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

public class Degree {
    public static GraphState execute(Graph g, boolean in) {
        // TODO: I don't like this very much, returning double etc
        GraphState degreesState = new GraphState();
        for (Vertex v : g.getVertices()) {
            degreesState.put(v, (in) ? (double) g.getInDegree(v) : (double) g.getOutDegree(v));
        }
        return degreesState;
    }
}