package gr.james.influence.graph;

import java.util.Set;

final class SwappedBipartiteGraph<V, E> extends AbstractBipartiteGraph<V, E> {
    private final BipartiteGraph<V, E> g;

    SwappedBipartiteGraph(BipartiteGraph<V, E> g) {
        this.g = g;
    }

    @Override
    public int modCount() {
        return this.g.modCount();
    }

    @Override
    public Set<V> vertexSetA() {
        return this.g.vertexSetB();
    }

    @Override
    public Set<V> vertexSetB() {
        return this.g.vertexSetA();
    }

    @Override
    public boolean addVertexInA(V v) {
        return this.g.addVertexInB(v);
    }

    @Override
    public boolean addVertexInB(V v) {
        return this.g.addVertexInA(v);
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
        return this.g.addEdge(v, w, edge, weight);
    }

    @Override
    public UndirectedEdge<V, E> removeEdge(V v, V w) {
        return this.g.removeEdge(v, w);
    }

    @Override
    public Set<V> vertexSet() {
        return this.g.vertexSet();
    }

    @Override
    public boolean addVertex(V v) {
        return this.g.addVertex(v);
    }

    @Override
    public boolean removeVertex(V v) {
        return this.g.removeVertex(v);
    }
}
