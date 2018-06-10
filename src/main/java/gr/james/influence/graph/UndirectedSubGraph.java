package gr.james.influence.graph;

import com.google.common.collect.Sets;
import gr.james.influence.exceptions.IllegalVertexException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class UndirectedSubGraph<V, E> extends AbstractUndirectedGraph<V, E> {
    private final UndirectedGraph<V, E> g;
    private final Set<V> vertices;

    UndirectedSubGraph(UndirectedGraph<V, E> g, Set<V> vertices) {
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
    public UndirectedEdge<V, E> findEdge(V v, V w) {
        if (v == null || w == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v) || !vertices.contains(w)) {
            throw new IllegalVertexException();
        }
        return g.findEdge(v, w);
    }

    @Override
    public Set<UndirectedEdge<V, E>> edges(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v)) {
            throw new IllegalVertexException();
        }
        return Sets.filter(g.edges(v), e -> vertices.contains(e.v()) && vertices.contains(e.w()));
    }

    @Override
    public Set<V> adjacent(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v)) {
            throw new IllegalVertexException();
        }
        return Sets.filter(g.adjacent(v), vertices::contains);
    }

    @Override
    public UndirectedEdge<V, E> addEdge(V v, V w, E edge, double weight) {
        if (v == null || w == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v) || !vertices.contains(w)) {
            throw new IllegalVertexException();
        }
        return g.addEdge(v, w, edge, weight);
    }

    @Override
    public UndirectedEdge<V, E> removeEdge(V v, V w) {
        if (v == null || w == null) {
            throw new NullPointerException();
        }
        if (!vertices.contains(v) || !vertices.contains(w)) {
            throw new IllegalVertexException();
        }
        return g.removeEdge(v, w);
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
