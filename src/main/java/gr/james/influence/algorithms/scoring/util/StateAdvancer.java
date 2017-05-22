package gr.james.influence.algorithms.scoring.util;

import gr.james.influence.util.collections.GraphState;

public interface StateAdvancer<V, T> {
    GraphState<V, T> next(GraphState<V, T> old);
}
