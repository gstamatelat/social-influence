package gr.james.influence.graph;

import java.util.Set;

final class UnmodifiableDirectedGraph<V, E> extends AbstractDirectedGraph<V, E> {
    private final DirectedGraph<V, E> g;

    UnmodifiableDirectedGraph(DirectedGraph<V, E> g) {
        this.g = g;
    }

    @Override
    public int modCount() {
        return this.g.modCount();
    }

    @Override
    public DirectedEdge<V, E> findEdge(V source, V target) {
        return g.findEdge(source, target);
    }

    @Override
    public Set<DirectedEdge<V, E>> outEdges(V v) {
        return g.outEdges(v);
    }

    @Override
    public Set<V> adjacentOut(V v) {
        return g.adjacentOut(v);
    }

    @Override
    public Set<DirectedEdge<V, E>> inEdges(V v) {
        return g.inEdges(v);
    }

    @Override
    public Set<V> adjacentIn(V v) {
        return g.adjacentIn(v);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
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
