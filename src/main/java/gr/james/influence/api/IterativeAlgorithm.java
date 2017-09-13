package gr.james.influence.api;

import gr.james.influence.util.collections.GraphState;

import java.util.Iterator;

public interface IterativeAlgorithm<V, T> extends Iterator<GraphState<V, T>> {
    @Override
    GraphState<V, T> next();

    @Override
    boolean hasNext();

    default GraphState<V, T> run() {
        GraphState<V, T> state = null;
        while (hasNext()) {
            state = next();
        }
        return state;
    }
}
