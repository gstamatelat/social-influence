package gr.james.influence.graph;

import java.util.Set;

final class UndirectedGraphImpl<V, E> implements UndirectedGraph<V, E> {
    UndirectedGraphImpl() {
        // TODO
        throw new UnsupportedOperationException();
    }

    UndirectedGraphImpl(int expectedVertexCount) {
        if (expectedVertexCount < 0) {
            throw new IllegalArgumentException();
        }
        // TODO
        throw new UnsupportedOperationException();
    }

    UndirectedGraphImpl(UndirectedGraph<V, E> g) {
        this(g.vertexCount());
        for (V v : g) {
            final boolean inserted = addVertex(v);
            assert inserted;
        }
        for (UndirectedEdge<V, E> e : g.edges()) {
            final UndirectedEdge<V, E> newEdge = addEdge(e.v(), e.w(), e.value(), e.weight());
            assert newEdge != null;
        }
    }

    @Override
    public UndirectedEdge<V, E> findEdge(V v, V w) {
        return null;
    }

    @Override
    public Set<UndirectedEdge<V, E>> edges(V v) {
        return null;
    }

    @Override
    public Set<V> adjacent(V v) {
        return null;
    }

    @Override
    public UndirectedEdge<V, E> addEdge(V v, V w, E edge, double weight) {
        return null;
    }

    @Override
    public UndirectedEdge<V, E> removeEdge(V v, V w) {
        return null;
    }

    @Override
    public Set<V> vertexSet() {
        return null;
    }

    @Override
    public boolean addVertex(V v) {
        return false;
    }

    @Override
    public boolean removeVertex(V v) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
