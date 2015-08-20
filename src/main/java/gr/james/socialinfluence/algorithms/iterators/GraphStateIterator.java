package gr.james.socialinfluence.algorithms.iterators;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.GraphState;
import gr.james.socialinfluence.util.collections.Weighted;

import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class GraphStateIterator<T extends Comparable<T>> implements Iterator<Weighted<Vertex, T>> {
    protected PriorityQueue<Weighted<Vertex, T>> p;

    public GraphStateIterator(GraphState<T> state) {
        p = new PriorityQueue<>(state.size(), Collections.reverseOrder());
        p.addAll(state.keySet().stream().map(v -> new Weighted<>(v, state.get(v))).collect(Collectors.toList()));
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
