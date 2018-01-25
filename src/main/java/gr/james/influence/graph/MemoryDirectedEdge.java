package gr.james.influence.graph;

import gr.james.influence.api.graph.DirectedEdge;

public class MemoryDirectedEdge<V, E> implements DirectedEdge<V, E> {
    private E edge;
    private V source;
    private V target;
    private double weight;

    public MemoryDirectedEdge(E edge, V source, V target, double weight) {
        this.edge = edge;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public E edge() {
        return edge;
    }

    public V source() {
        return source;
    }

    public V target() {
        return target;
    }

    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", source, target);
    }
}
