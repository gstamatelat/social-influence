package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.util.collections.GraphState;

@Deprecated
public interface IterativeAlgorithmHandler {
    void newState(GraphState<Double> oldState, GraphState<Double> newState);
}
