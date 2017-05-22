package gr.james.influence.util.collections;

import gr.james.influence.util.Conditions;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class VertexSequence<V> implements Iterable<V> {
    private List<V> sequence;
    private double distance;

    public VertexSequence(List<V> sequence, double distance) {
        this.sequence = Conditions.requireNonNull(sequence);
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public V get(int index) {
        return sequence.get(index);
    }

    public int size() {
        return sequence.size();
    }

    @Override
    public Iterator<V> iterator() {
        return Collections.unmodifiableList(sequence).iterator();
    }

    @Override
    public String toString() {
        return sequence.toString();
    }
}
