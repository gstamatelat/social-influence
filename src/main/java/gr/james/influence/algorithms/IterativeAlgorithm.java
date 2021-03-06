package gr.james.influence.algorithms;

import gr.james.influence.util.collections.GraphState;

public interface IterativeAlgorithm<V, T> {
    GraphState<V, T> next();

    boolean hasNext();

    default GraphState<V, T> run() {
        GraphState<V, T> state = null;
        while (hasNext()) {
            state = next();
        }
        return state;
    }
}
