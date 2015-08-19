package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.collections.EvictingLinkedHashSet;

public interface IterativeAlgorithm {
    GraphState<Double> nextState(GraphState<Double> oldState);

    default GraphState<Double> execute(Graph g, GraphState<Double> initialState, double epsilon, int history) {
        Conditions.requireArgument(history >= 1, "DeGroot history must be >= 1");

        EvictingLinkedHashSet<GraphState<Double>> stateHistory = new EvictingLinkedHashSet<>(history);

        GraphState<Double> lastState = initialState;
        stateHistory.add(initialState);

        boolean stabilized = false;
        while (!stabilized) {
            GraphState<Double> nextState = nextState(lastState);

            if (nextState.subtract(lastState).abs().lessThan(epsilon)) {
                stabilized = true;
            }

            if (!stateHistory.add(nextState)) {
                stabilized = true;
                if (Finals.LOG.isDebugEnabled() && !nextState.equals(lastState)) {
                    Finals.LOG.debug(Finals.L_PERIODIC, g);
                }
            }

            lastState = nextState;
        }

        return lastState;
    }
}
