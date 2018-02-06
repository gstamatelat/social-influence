package gr.james.influence.graph;

import java.util.Set;

final class UnmodifiableBipartiteGraph<V, E> extends AbstractBipartiteGraph<V, E> {
    private final BipartiteGraph<V, E> g;

    UnmodifiableBipartiteGraph(BipartiteGraph<V, E> g) {
        this.g = g;
    }

    @Override
    public Set<V> vertexASet() {
        return this.g.vertexASet();
    }

    @Override
    public Set<V> vertexBSet() {
        return this.g.vertexBSet();
    }

    @Override
    public boolean addVertexInA(V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addVertexInB(V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UndirectedEdge<V, E> findEdge(V v, V w) {
        return this.g.findEdge(v, w);
    }

    @Override
    public Set<UndirectedEdge<V, E>> edges(V v) {
        return this.g.edges(v);
    }

    @Override
    public Set<V> adjacent(V v) {
        return this.g.adjacent(v);
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
        return this.g.vertexSet();
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
