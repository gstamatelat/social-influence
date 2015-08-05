package gr.james.socialinfluence.util.collections;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class VertexSequence implements Iterable<Vertex> {
    private List<Vertex> sequence;
    private double distance;

    public VertexSequence(List<Vertex> sequence, double distance) {
        this.sequence = Conditions.requireNonNull(sequence);
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public Vertex get(int index) {
        return sequence.get(index);
    }

    public int size() {
        return sequence.size();
    }

    @Override
    public Iterator<Vertex> iterator() {
        return Collections.unmodifiableList(sequence).iterator();
    }

    @Override
    public String toString() {
        return sequence.toString();
    }
}
