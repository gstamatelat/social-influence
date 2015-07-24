package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.collections.Weighted;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Iterator;
import java.util.PriorityQueue;

public class GraphStateIterator<T extends Comparable<T>> implements Iterator<Vertex> {
    private PriorityQueue<Weighted<Vertex, T>> p = new PriorityQueue<>();

    public GraphStateIterator(GraphState<T> state) {
        for (Vertex v : state.keySet()) {
            p.add(new Weighted<>(v, state.get(v)));
        }
    }

    @Override
    public boolean hasNext() {
        return !p.isEmpty();
    }

    @Override
    public Vertex next() {
        return p.poll().getObject();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
