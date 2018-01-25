package gr.james.influence.api.graph;

import java.util.Objects;

final class DirectedEdgeImpl<V, E> implements DirectedEdge<V, E> {
    private final E edge;
    private final V source;
    private final V target;
    private final double weight;

    DirectedEdgeImpl(E edge, V source, V target, double weight) {
        this.edge = edge;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    @Override
    public E edge() {
        return edge;
    }

    @Override
    public V source() {
        return source;
    }

    @Override
    public V target() {
        return target;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", source, target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DirectedEdgeImpl<?, ?> that = (DirectedEdgeImpl<?, ?>) o;
        return Double.compare(weight, that.weight) == 0 && // maybe weight is easier to compare so it goes first
                Objects.equals(edge, that.edge) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edge, source, target, weight);
    }
}
