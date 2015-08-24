package gr.james.socialinfluence.algorithms.scoring.util;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.collections.EvictingLinkedHashSet;
import gr.james.socialinfluence.util.collections.GraphState;

public final class IterativeAlgorithmHelper {
    public static final int REPS_INCREASE = 500;
    public static final int INITIAL_SIZE = 2;

    public static <T> GraphState<T> execute(Graph g, GraphState<T> initialState, StateAdvancer<T> advancer,
                                            ConvergePredicate<T> convergePredicate, double epsilon) {
        Conditions.requireAllNonNull(g, initialState, advancer, convergePredicate);
        Conditions.requireArgument(epsilon >= 0, "epsilon must be >= 0");

        EvictingLinkedHashSet<GraphState<T>> stateHistory = new EvictingLinkedHashSet<>(INITIAL_SIZE);

        GraphState<T> lastState = initialState;
        stateHistory.add(initialState);

        int reps = 0;

        boolean stabilized = false;
        while (!stabilized) {
            GraphState<T> nextState = advancer.next(lastState);

            if (epsilon != 0.0 && nextState.testConvergence(lastState, convergePredicate, epsilon)) {
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
                Finals.LOG.trace("Increased maxSize to {}", stateHistory.getMaxSize());
                reps = 0;
            }

            lastState = nextState;
        }

        return lastState;
    }
}
