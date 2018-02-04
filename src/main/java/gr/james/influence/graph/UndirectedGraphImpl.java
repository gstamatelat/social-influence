package gr.james.influence.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

final class UndirectedGraphImpl<V, E> extends AbstractUndirectedGraph<V, E> {
    private final Map<V, BiMap<V, UndirectedEdge<V, E>>> m;

    UndirectedGraphImpl() {
        this.m = new HashMap<>();
    }

    UndirectedGraphImpl(int expectedVertexCount) {
        if (expectedVertexCount < 0) {
            throw new IllegalArgumentException();
        }
        this.m = new HashMap<>(expectedVertexCount);
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
        Conditions.requireAllNonNull(v, w);
        final Map<V, UndirectedEdge<V, E>> edges = m.get(v);
        if (edges == null) {
            throw new IllegalVertexException();
        }
        if (!this.m.containsKey(w)) {
            throw new IllegalVertexException();
        }
        return edges.get(w);
    }

    @Override
    public Set<UndirectedEdge<V, E>> edges(V v) {
        Conditions.requireNonNull(v);
        final BiMap<V, UndirectedEdge<V, E>> map = m.get(v);
        if (map == null) {
            throw new IllegalVertexException();
        }
        return Collections.unmodifiableSet(map.values());
    }

    @Override
    public Set<V> adjacent(V v) {
        Conditions.requireNonNull(v);
        final BiMap<V, UndirectedEdge<V, E>> edges = m.get(v);
        if (edges == null) {
            throw new IllegalVertexException();
        }
        return Collections.unmodifiableSet(edges.keySet());
    }

    @Override
    public UndirectedEdge<V, E> addEdge(V v, V w, E edge, double weight) {
        Conditions.requireAllNonNull(v, w);
        Graphs.requireWeightLegal(weight);
        final BiMap<V, UndirectedEdge<V, E>> eOut = m.get(v);
        final BiMap<V, UndirectedEdge<V, E>> eIn = m.get(w);
        if (eOut == null || eIn == null) {
            throw new IllegalVertexException();
        }
        final UndirectedEdge<V, E> e = UndirectedEdge.from(edge, v, w, weight);
        final UndirectedEdge<V, E> e1 = eOut.putIfAbsent(w, e);
        final UndirectedEdge<V, E> e2 = eIn.putIfAbsent(v, e);
        assert (e1 == null && e2 == null) || (e1 != null && e2 != null && e1.equals(e2));
        if (e1 == null) {
            return e;
        } else {
            return null;
        }
    }

    @Override
    public UndirectedEdge<V, E> removeEdge(V v, V w) {
        Conditions.requireAllNonNull(v, w);
        final BiMap<V, UndirectedEdge<V, E>> eOut = m.get(v);
        final BiMap<V, UndirectedEdge<V, E>> eIn = m.get(w);
        if (eOut == null || eIn == null) {
            throw new IllegalVertexException();
        }
        final UndirectedEdge<V, E> e1 = eOut.remove(w);
        final UndirectedEdge<V, E> e2 = eIn.remove(v);
        assert (e1 == null && e2 == null) || (e1 != null && e2 != null && e1.equals(e2));
        return e1;
    }

    @Override
    public Set<V> vertexSet() {
        return Collections.unmodifiableSet(this.m.keySet());
    }

    @Override
    public boolean addVertex(V v) {
        Conditions.requireNonNull(v);
        final Object o = this.m.putIfAbsent(v, HashBiMap.create());
        return o == null;
    }

    @Override
    public boolean removeVertex(V v) {
        Conditions.requireNonNull(v);
        if (!this.m.containsKey(v)) {
            return false;
        }
        for (V w : this.m.get(v).keySet()) {
            final Object o = this.m.get(w).remove(v);
            assert o != null;
        }
        final Object o = this.m.remove(v);
        assert o != null;
        return true;
    }
}
