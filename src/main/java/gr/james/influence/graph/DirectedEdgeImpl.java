package gr.james.influence.graph;

import java.util.Objects;

final class DirectedEdgeImpl<V, E> implements DirectedEdge<V, E> {
    private final E value;
    private final V source;
    private final V target;
    private final double weight;

    DirectedEdgeImpl(E value, V source, V target, double weight) {
        this.value = value;
        this.source = source;
        this.target = target;
        this.weight = weight;
        /*assert Graphs.isWeightLegal(weight);*/
    }

    @Override
    public E value() {
        return value;
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
        if (!(o instanceof DirectedEdge)) {
            return false;
        }
        final DirectedEdge<?, ?> that = (DirectedEdge<?, ?>) o;
        return Double.compare(weight(), that.weight()) == 0 && // maybe weight is easier to compare so it goes first
                Objects.equals(value(), that.value()) &&
                Objects.equals(source(), that.source()) &&
                Objects.equals(target(), that.target());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, source, target, weight);
    }
}
