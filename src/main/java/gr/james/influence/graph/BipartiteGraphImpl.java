package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class BipartiteGraphImpl<V, E> extends AbstractUndirectedGraph<V, E> implements BipartiteGraph<V, E> {
    private final UndirectedGraph<V, E> g;
    private final Set<V> setA;
    private final Set<V> setB;

    BipartiteGraphImpl() {
        this.g = UndirectedGraph.create();
        this.setA = new HashSet<>();
        this.setB = new HashSet<>();
    }

    BipartiteGraphImpl(int expectedVertexCount) {
        this.g = UndirectedGraph.create(expectedVertexCount);
        this.setA = new HashSet<>(expectedVertexCount);
        this.setB = new HashSet<>(expectedVertexCount);
    }

    BipartiteGraphImpl(BipartiteGraph<V, E> g) {
        this.g = UndirectedGraph.create(g);
        this.setA = new HashSet<>(g.vertexASet());
        this.setB = new HashSet<>(g.vertexBSet());
    }

    @Override
    public Set<V> vertexASet() {
        return Collections.unmodifiableSet(setA);
    }

    @Override
    public Set<V> vertexBSet() {
        return Collections.unmodifiableSet(setB);
    }

    @Override
    public boolean addVertexInA(V v) {
        if (this.g.addVertex(v)) {
            boolean inserted = setA.add(v);
            assert inserted;
            return true;
        }
        return false;
    }

    @Override
    public boolean addVertexInB(V v) {
        if (this.g.addVertex(v)) {
            boolean inserted = setB.add(v);
            assert inserted;
            return true;
        }
        return false;
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
        Conditions.requireVertexInGraph(this.g, v);
        Conditions.requireVertexInGraph(this.g, w);
        if (setA.contains(v) && setA.contains(w)) {
            throw new IllegalVertexException();
        }
        if (setB.contains(v) && setB.contains(w)) {
            throw new IllegalVertexException();
        }
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeVertex(V v) {
        if (this.g.removeVertex(v)) {
            boolean removedA = this.setA.remove(v);
            boolean removedB = this.setB.remove(v);
            assert removedA ^ removedB;
            return true;
        }
        return false;
    }
}
