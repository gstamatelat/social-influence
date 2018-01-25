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

    public E getEdge() {
        return edge;
    }

    public V getSource() {
        return source;
    }

    public V getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", source, target);
    }
}
