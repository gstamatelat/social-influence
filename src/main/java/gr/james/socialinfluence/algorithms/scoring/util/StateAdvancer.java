package gr.james.socialinfluence.algorithms.scoring.util;

import gr.james.socialinfluence.util.collections.GraphState;

public interface StateAdvancer<T> {
    GraphState<T> next(GraphState<T> old);
}
