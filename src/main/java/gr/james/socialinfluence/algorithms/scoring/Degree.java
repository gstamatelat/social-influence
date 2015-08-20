package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.GraphState;

public class Degree {
    public static GraphState<Integer> execute(Graph g, boolean in) {
        GraphState<Integer> degreesState = new GraphState<>();
        for (Vertex v : g) {
            degreesState.put(v, (in) ? g.getInDegree(v) : g.getOutDegree(v));
        }
        return degreesState;
    }
}
