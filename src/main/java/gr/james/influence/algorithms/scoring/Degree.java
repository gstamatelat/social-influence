package gr.james.influence.algorithms.scoring;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;

import java.util.Collection;

public class Degree {
    public static GraphState<Integer> execute(Graph g, Collection<Vertex> filter, boolean in) {
        GraphState<Integer> degreesState = new GraphState<>();

        g.getVertices().stream().filter(filter::contains)
                .forEach(i -> degreesState.put(i, (in) ? g.getInDegree(i) : g.getOutDegree(i)));

        return degreesState;
    }

    public static GraphState<Integer> execute(Graph g, boolean in) {
        return execute(g, g.getVertices(), in);
    }
}
