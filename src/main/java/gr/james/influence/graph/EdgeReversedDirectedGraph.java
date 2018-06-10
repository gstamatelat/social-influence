package gr.james.influence.graph;

import java.util.Set;

final class EdgeReversedDirectedGraph<V, E> extends AbstractDirectedGraph<V, E> {
    private final DirectedGraph<V, E> g;

    public EdgeReversedDirectedGraph(DirectedGraph<V, E> g) {
        this.g = g;
    }

    @Override
    public int modCount() {
        return this.g.modCount();
    }

    @Override
    public DirectedEdge<V, E> findEdge(V source, V target) {
        return g.findEdge(target, source).reverse();
    }

    @Override
    public Set<V> adjacentOut(V v) {
        return g.adjacentIn(v);
    }

    @Override
    public Set<V> adjacentIn(V v) {
        return g.adjacentOut(v);
    }

    @Override
    public Set<DirectedEdge<V, E>> outEdges(V v) {
        return new ReverseEdgeSet<>(g.inEdges(v));
    }

    @Override
    public Set<DirectedEdge<V, E>> inEdges(V v) {
        return new ReverseEdgeSet<>(g.outEdges(v));
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        return g.addEdge(target, source, edge, weight).reverse();
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        return g.removeEdge(target, source).reverse();
    }

    @Override
    public Set<V> vertexSet() {
        return g.vertexSet();
    }

    @Override
    public boolean addVertex(V v) {
        return g.addVertex(v);
    }

    @Override
    public boolean removeVertex(V v) {
        return g.removeVertex(v);
    }
}
