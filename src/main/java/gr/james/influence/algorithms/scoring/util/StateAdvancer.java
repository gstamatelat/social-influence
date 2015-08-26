package gr.james.influence.algorithms.scoring.util;

import gr.james.influence.util.collections.GraphState;

public interface StateAdvancer<T> {
    GraphState<T> next(GraphState<T> old);
}
