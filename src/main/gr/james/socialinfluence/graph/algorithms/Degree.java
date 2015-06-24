package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.collections.GraphState;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Map;

public class Degree {
    public static GraphState execute(Graph g, boolean in) {
        // TODO: I don't like this very much, returning double etc
        Map<Vertex, Integer> degrees = (in) ? g.getInDegree() : g.getOutDegree();
        GraphState degreesState = new GraphState();
        for (Map.Entry<Vertex, Integer> e : degrees.entrySet()) {
            degreesState.put(e.getKey(), e.getValue().doubleValue());
        }
        return degreesState;
    }
}