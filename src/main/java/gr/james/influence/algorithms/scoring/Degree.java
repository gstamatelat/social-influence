package gr.james.influence.algorithms.scoring;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Direction;
import gr.james.influence.util.collections.GraphState;

import java.util.Collection;

public class Degree {
    public static <V> GraphState<V, Integer> execute(Graph<V, ?> g, Collection<V> filter, Direction direction) {
        GraphState<V, Integer> degreesState = GraphState.create();

        g.getVertices().stream().filter(filter::contains)
                .forEach(i -> degreesState.put(i, (direction.isInbound()) ? g.getInDegree(i) : g.getOutDegree(i)));

        return degreesState;
    }

    public static <V> GraphState<V, Integer> execute(Graph<V, ?> g, Direction direction) {
        return execute(g, g.getVertices(), direction);
    }
}
