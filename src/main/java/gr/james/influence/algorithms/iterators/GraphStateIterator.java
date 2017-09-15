package gr.james.influence.algorithms.iterators;

import gr.james.influence.util.collections.GraphState;
import gr.james.influence.util.collections.Weighted;

import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

public class GraphStateIterator<V, T extends Comparable<T>> implements Iterator<Weighted<V, T>> {
    protected PriorityQueue<Weighted<V, T>> p;

    public GraphStateIterator(GraphState<V, T> state) {
        p = new PriorityQueue<>(state.size(), Collections.reverseOrder());
        state.keySet().stream().map(v -> new Weighted<>(v, state.get(v))).forEach(p::add);
    }

    @Override
    public boolean hasNext() {
        return !p.isEmpty();
    }

    @Override
    public Weighted<V, T> next() {
        return p.poll();
    }
}
