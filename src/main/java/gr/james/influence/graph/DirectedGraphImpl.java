package gr.james.influence.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;

import java.util.*;

final class DirectedGraphImpl<V, E> implements DirectedGraph<V, E> {
    private final Map<V, BiMap<V, DirectedEdge<V, E>>> mOut;
    private final Map<V, BiMap<V, DirectedEdge<V, E>>> mIn;

    DirectedGraphImpl() {
        this.mOut = new HashMap<>();
        this.mIn = new HashMap<>();
    }

    DirectedGraphImpl(int expectedVertexCount) {
        if (expectedVertexCount < 0) {
            throw new IllegalArgumentException();
        }
        this.mOut = new HashMap<>(expectedVertexCount);
        this.mIn = new HashMap<>(expectedVertexCount);
    }

    DirectedGraphImpl(DirectedGraph<V, E> g) {
        this(g.vertexCount());
        for (V v : g) {
            final boolean inserted = addVertex(v);
            assert inserted;
        }
        for (DirectedEdge<V, E> e : g.edges()) {
            final DirectedEdge<V, E> newEdge = addEdge(e.source(), e.target(), e.value(), e.weight());
            assert newEdge != null;
        }
    }

    @Override
    public DirectedEdge<V, E> findEdge(V source, V target) {
        Conditions.requireAllNonNull(source, target);
        final Map<V, DirectedEdge<V, E>> edges = mOut.get(source);
        if (edges == null) {
            throw new IllegalVertexException();
        }
        if (!mOut.containsKey(target)) {
            throw new IllegalVertexException();
        }
        return edges.get(target);
    }

    @Override
    public Set<V> adjacentOut(V v) {
        Conditions.requireNonNull(v);
        final BiMap<V, DirectedEdge<V, E>> edges = mOut.get(v);
        if (edges == null) {
            throw new IllegalVertexException();
        }
        return Collections.unmodifiableSet(edges.keySet());
    }

    @Override
    public Set<V> adjacentIn(V v) {
        Conditions.requireNonNull(v);
        final BiMap<V, DirectedEdge<V, E>> edges = mIn.get(v);
        if (edges == null) {
            throw new IllegalVertexException();
        }
        return Collections.unmodifiableSet(edges.keySet());
    }

    @Override
    public Set<V> vertexSet() {
        assert Objects.equals(this.mOut.keySet(), this.mIn.keySet());
        return Collections.unmodifiableSet(this.mOut.keySet());
    }

    @Override
    public Set<DirectedEdge<V, E>> outEdges(V v) {
        Conditions.requireNonNull(v);
        final BiMap<V, DirectedEdge<V, E>> map = mOut.get(v);
        if (map == null) {
            throw new IllegalVertexException();
        }
        return Collections.unmodifiableSet(map.values());
    }

    @Override
    public Set<DirectedEdge<V, E>> inEdges(V v) {
        Conditions.requireNonNull(v);
        final BiMap<V, DirectedEdge<V, E>> map = mIn.get(v);
        if (map == null) {
            throw new IllegalVertexException();
        }
        return Collections.unmodifiableSet(map.values());
    }

    @Override
    public boolean addVertex(V v) {
        if (this.containsVertex(v)) {
            return false;
        } else {
            final Object o1 = this.mOut.put(v, HashBiMap.create());
            final Object o2 = this.mIn.put(v, HashBiMap.create());
            assert o1 == null && o2 == null;
            return true;
        }
    }

    @Override
    public boolean removeVertex(V v) {
        if (!this.containsVertex(v)) {
            return false;
        }
        for (V d : mOut.get(v).keySet()) {
            final Object o = mIn.get(d).remove(v);
            assert o != null;
        }
        for (V d : mIn.get(v).keySet()) {
            final Object o = mOut.get(d).remove(v);
            assert o != null;
        }
        final Object o1 = this.mOut.remove(v);
        final Object o2 = this.mIn.remove(v);
        assert o1 != null && o2 != null;
        return true;
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        if (!containsEdge(source, target)) {
            final DirectedEdge<V, E> e = DirectedEdge.from(edge, source, target, weight);
            final DirectedEdge<V, E> e1 = this.mOut.get(source).put(target, e);
            final DirectedEdge<V, E> e2 = this.mIn.get(target).put(source, e);
            assert e1 == null;
            assert e2 == null;
            return e;
        } else {
            return null;
        }
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        if (this.mOut.get(source).remove(target) != null) {
            final DirectedEdge<V, E> h = this.mIn.get(target).remove(source);
            assert h != null;
            assert h.source().equals(source);
            assert h.target().equals(target);
            return h;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("DirectedGraph(%d) {%n", this.vertexCount()));
        for (DirectedEdge<V, E> e : this.edges()) {
            if (e.value() == null) {
                sb.append(String.format("  %s -> %s [%.2f]%n", e.source(), e.target(), e.weight()));
            } else {
                sb.append(String.format("  %s -> %s (%s) [%.2f]%n", e.source(), e.target(), e.value(), e.weight()));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
