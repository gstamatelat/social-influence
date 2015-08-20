package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.GraphState;

@Deprecated
public interface IterativeAlgorithmHandler {
    void newState(GraphState<Double> oldState, GraphState<Double> newState);
}
