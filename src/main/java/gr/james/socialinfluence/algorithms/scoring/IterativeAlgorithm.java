package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.collections.EvictingLinkedHashSet;
import gr.james.socialinfluence.util.collections.GraphState;

public interface IterativeAlgorithm {
    int REPS_INCREASE = 1000;
    int INITIAL_SIZE = 2;

    GraphState<Double> nextState(GraphState<Double> oldState);

    default GraphState<Double> execute(Graph g, GraphState<Double> initialState, double epsilon) {
        EvictingLinkedHashSet<GraphState<Double>> stateHistory = new EvictingLinkedHashSet<>(INITIAL_SIZE);

        GraphState<Double> lastState = initialState;
        stateHistory.add(initialState);

        int reps = 0;

        boolean stabilized = false;
        while (!stabilized) {
            GraphState<Double> nextState = nextState(lastState);

            if (nextState.subtract(lastState).abs().lessThan(epsilon)) {
                stabilized = true;
            }

            if (!stateHistory.add(nextState)) {
                stabilized = true;
                if (Finals.LOG.isDebugEnabled() && !nextState.equals(lastState)) {
                    Finals.LOG.debug(Finals.L_PERIODIC, g, stateHistory.getMaxSize());
                }
            }

            if (++reps == REPS_INCREASE) {
                stateHistory.increaseMaxSize();
                Finals.LOG.debug("Increased maxSize to {}", stateHistory.getMaxSize());
                reps = 0;
            }

            lastState = nextState;
        }

        return lastState;
    }
}
