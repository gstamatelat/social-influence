package gr.james.influence.algorithms.iterators;

import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;
import gr.james.influence.util.collections.Weighted;

import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

public class GraphStateIterator<T extends Comparable<T>> implements Iterator<Weighted<Vertex, T>> {
    protected PriorityQueue<Weighted<Vertex, T>> p;

    public GraphStateIterator(GraphState<T> state) {
        p = new PriorityQueue<>(state.size(), Collections.reverseOrder());
        state.keySet().stream().map(v -> new Weighted<>(v, state.get(v))).forEach(p::add);
    }

    @Override
    public boolean hasNext() {
        return !p.isEmpty();
    }

    @Override
    public Weighted<Vertex, T> next() {
        return p.poll();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
