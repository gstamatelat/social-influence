package gr.james.influence.graph;

import java.util.Set;

final class UnmodifiableUndirectedGraph<V, E> extends AbstractUndirectedGraph<V, E> {
    private final UndirectedGraph<V, E> g;

    UnmodifiableUndirectedGraph(UndirectedGraph<V, E> g) {
        this.g = g;
    }

    @Override
    public int modCount() {
        return this.g.modCount();
    }

    @Override
    public UndirectedEdge<V, E> findEdge(V v, V w) {
        return g.findEdge(v, w);
    }

    @Override
    public Set<UndirectedEdge<V, E>> edges(V v) {
        return g.edges(v);
    }

    @Override
    public Set<V> adjacent(V v) {
        return g.adjacent(v);
    }

    @Override
    public UndirectedEdge<V, E> addEdge(V v, V w, E edge, double weight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UndirectedEdge<V, E> removeEdge(V v, V w) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<V> vertexSet() {
        return g.vertexSet();
    }

    @Override
    public boolean addVertex(V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeVertex(V v) {
        throw new UnsupportedOperationException();
    }
}
