package gr.james.influence.algorithms.scoring;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;

import java.util.Collection;

public class Degree {
    public static GraphState<Integer> execute(Graph g, Collection<Vertex> filter, Direction direction) {
        GraphState<Integer> degreesState = new GraphState<>();

        g.getVertices().stream().filter(filter::contains)
                .forEach(i -> degreesState.put(i, (direction.isInbound()) ? g.getInDegree(i) : g.getOutDegree(i)));

        return degreesState;
    }

    public static GraphState<Integer> execute(Graph g, Direction direction) {
        return execute(g, g.getVertices(), direction);
    }
}
