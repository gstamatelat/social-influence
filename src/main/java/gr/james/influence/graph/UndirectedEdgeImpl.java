package gr.james.influence.graph;

import java.util.Objects;

final class UndirectedEdgeImpl<V, E> implements UndirectedEdge<V, E> {
    private final E value;
    private final V v;
    private final V w;
    private final double weight;

    UndirectedEdgeImpl(E value, V v, V w, double weight) {
        this.value = value;
        this.v = v;
        this.w = w;
        this.weight = weight;
        assert Graphs.isWeightLegal(weight);
    }

    @Override
    public E value() {
        return value;
    }

    @Override
    public V v() {
        return v;
    }

    @Override
    public V w() {
        return w;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%s -- %s", v, w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof UndirectedEdge)) {
            return false;
        }
        final UndirectedEdge<?, ?> that = (UndirectedEdge<?, ?>) o;
        return Double.compare(weight(), that.weight()) == 0 && // maybe weight is easier to compare so it goes first
                Objects.equals(value(), that.value()) &&
                ((Objects.equals(v(), that.v()) && Objects.equals(w(), that.w())) ||
                        (Objects.equals(v(), that.w()) && Objects.equals(w(), that.v())));
    }

    @Override
    public int hashCode() {
        final int vHash = v().hashCode();
        final int wHash = w().hashCode();
        return Objects.hash(value,
                Math.min(vHash, wHash),
                Math.max(vHash, wHash),
                weight);
    }
}
