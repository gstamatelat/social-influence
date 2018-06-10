package gr.james.influence.graph;

import com.google.common.collect.Sets;
import gr.james.influence.exceptions.IllegalVertexException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class DirectedSubGraph<V, E> extends AbstractDirectedGraph<V, E> {
    private final DirectedGraph<V, E> g;
    private final Set<V> vertices;

    DirectedSubGraph(DirectedGraph<V, E> g, Set<V> vertices) {
        this.g = g;
        this.vertices = new HashSet<>(vertices);

        for (V v : this.vertices) {
            if (!g.containsVertex(v)) {
                throw new IllegalVertexException();
            }
        }
    }

    @Override
    public int modCount() {
        return this.g.modCount();
    }

    @Override
    public DirectedEdge<V, E> findEdge(V source, V target) {
        if (source == null || target == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalVertexException();
        }
        return g.findEdge(source, target);
    }

    @Override
    public Set<DirectedEdge<V, E>> outEdges(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v)) {
            throw new IllegalVertexException();
        }
        return Sets.filter(g.outEdges(v), e -> vertices.contains(e.source()) && vertices.contains(e.target()));
    }

    @Override
    public Set<V> adjacentOut(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v)) {
            throw new IllegalVertexException();
        }
        return Sets.filter(g.adjacentOut(v), vertices::contains);
    }

    @Override
    public Set<DirectedEdge<V, E>> inEdges(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v)) {
            throw new IllegalVertexException();
        }
        return Sets.filter(g.inEdges(v), e -> vertices.contains(e.source()) && vertices.contains(e.target()));
    }

    @Override
    public Set<V> adjacentIn(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v)) {
            throw new IllegalVertexException();
        }
        return Sets.filter(g.adjacentIn(v), vertices::contains);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        if (source == null || target == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalVertexException();
        }
        return g.addEdge(source, target, edge, weight);
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        if (source == null || target == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalVertexException();
        }
        return g.removeEdge(source, target);
    }

    @Override
    public Set<V> vertexSet() {
        return Collections.unmodifiableSet(vertices);
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
