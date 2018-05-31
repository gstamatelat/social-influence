package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class BipartiteGraphImpl<V, E> extends AbstractBipartiteGraph<V, E> {
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
        this.setA = new HashSet<>(g.vertexSetA());
        this.setB = new HashSet<>(g.vertexSetB());
    }

    @Override
    public Set<V> vertexSetA() {
        return Collections.unmodifiableSet(setA);
    }

    @Override
    public Set<V> vertexSetB() {
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
            throw new IllegalVertexException("Vertices %s and %s are in the same disjoint set", v, w);
        }
        if (setB.contains(v) && setB.contains(w)) {
            throw new IllegalVertexException("Vertices %s and %s are in the same disjoint set", v, w);
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
        throw new UnsupportedOperationException("Cannot directly add a vertex, must use addVertexInA or addVertexInB methods");
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
