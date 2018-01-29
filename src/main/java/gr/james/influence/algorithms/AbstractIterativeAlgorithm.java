package gr.james.influence.algorithms;

import gr.james.influence.api.algorithms.IterativeAlgorithm;
import gr.james.influence.graph.Graph;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.EvictingLinkedHashSet;
import gr.james.influence.util.collections.GraphState;

import java.util.NoSuchElementException;

public abstract class AbstractIterativeAlgorithm<V, T> implements IterativeAlgorithm<V, T> {
    public static final int INITIAL_SIZE = 2;
    public static final int REPS_INCREASE = 500;

    private GraphState<V, T> lastState;
    private Graph<V, ?> g;
    private boolean hasNext;
    private EvictingLinkedHashSet<GraphState<V, T>> stateHistory;
    private int reps;

    protected AbstractIterativeAlgorithm(Graph<V, ?> g, GraphState<V, T> initial) {
        this.g = g;
        this.lastState = initial;
        this.hasNext = true;
        this.stateHistory = new EvictingLinkedHashSet<>(INITIAL_SIZE);
        this.reps = 0;
    }

    protected abstract boolean converges(GraphState<V, T> previous, GraphState<V, T> next);

    protected abstract GraphState<V, T> step(Graph<V, ?> g, GraphState<V, T> previous);

    @Override
    public GraphState<V, T> next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }

        GraphState<V, T> nextState = step(g, lastState);

        if (converges(lastState, nextState)) {
            hasNext = false;
            return nextState;
        }

        if (!stateHistory.add(nextState)) {
            hasNext = false;
            if (Finals.LOG.isDebugEnabled() && !nextState.equals(lastState)) {
                Finals.LOG.debug(Finals.L_PERIODIC, g, stateHistory.getMaxSize());
            }
            return nextState;
        }

        if (++reps == REPS_INCREASE) {
            stateHistory.increaseMaxSize();
            Finals.LOG.trace("Increased maxSize to {}", stateHistory.getMaxSize());
            reps = 0;
        }

        lastState = nextState;
        return lastState;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }
}
