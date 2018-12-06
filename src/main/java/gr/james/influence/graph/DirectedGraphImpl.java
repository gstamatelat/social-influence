package gr.james.influence.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.*;

final class DirectedGraphImpl<V, E> extends AbstractDirectedGraph<V, E> {
    private final Map<V, BiMap<V, DirectedEdge<V, E>>> mOut;
    private final Map<V, BiMap<V, DirectedEdge<V, E>>> mIn;
    private int modCount;

    DirectedGraphImpl() {
        this.mOut = new HashMap<>();
        this.mIn = new HashMap<>();
        this.modCount = 0;
    }

    DirectedGraphImpl(int expectedVertexCount) {
        if (expectedVertexCount < 0) {
            throw new IllegalArgumentException();
        }
        this.mOut = new HashMap<>(expectedVertexCount);
        this.mIn = new HashMap<>(expectedVertexCount);
        this.modCount = 0;
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
        assert Graphs.equals(this, g);
        this.modCount = 0;
    }

    DirectedGraphImpl(UndirectedGraph<V, E> g) {
        this(g.vertexCount());
        for (V v : g) {
            final boolean inserted = addVertex(v);
            assert inserted;
        }
        for (UndirectedEdge<V, E> e : g.edges()) {
            final DirectedEdge<V, E> newEdge1 = addEdge(e.v(), e.w(), e.value(), e.weight());
            final DirectedEdge<V, E> newEdge2 = addEdge(e.w(), e.v(), e.value(), e.weight());
            assert newEdge1 != null;
            assert newEdge2 != null;
        }
        assert Graphs.equals(this, g.asDirected());
        this.modCount = 0;
    }

    @Override
    public int modCount() {
        return this.modCount;
    }

    @Override
    public DirectedEdge<V, E> findEdge(V source, V target) {
        Conditions.requireAllNonNull(source, target);
        final Map<V, DirectedEdge<V, E>> edges = mOut.get(source);
        if (edges == null) {
            throw new IllegalVertexException();
        }
        if (!this.mIn.containsKey(target)) {
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
        Conditions.requireNonNull(v);
        final Object o1 = this.mOut.putIfAbsent(v, HashBiMap.create());
        final Object o2 = this.mIn.putIfAbsent(v, HashBiMap.create());
        assert (o1 == null) == (o2 == null);
        if (o1 == null) {
            this.modCount++;
        }
        return o1 == null;
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
        this.modCount++;
        return true;
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        Conditions.requireAllNonNull(source, target);
        /*Graphs.requireWeightLegal(weight);*/
        final BiMap<V, DirectedEdge<V, E>> eOut = mOut.get(source);
        final BiMap<V, DirectedEdge<V, E>> eIn = mIn.get(target);
        if (eOut == null || eIn == null) {
            throw new IllegalVertexException();
        }
        final DirectedEdge<V, E> e = DirectedEdge.from(edge, source, target, weight);
        final DirectedEdge<V, E> e1 = eOut.putIfAbsent(target, e);
        final DirectedEdge<V, E> e2 = eIn.putIfAbsent(source, e);
        assert (e1 == null && e2 == null) || (e1 != null && e2 != null && e1.equals(e2));
        if (e1 == null) {
            this.modCount++;
            return e;
        } else {
            return null;
        }
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        Conditions.requireAllNonNull(source, target);
        final BiMap<V, DirectedEdge<V, E>> eOut = mOut.get(source);
        final BiMap<V, DirectedEdge<V, E>> eIn = mIn.get(target);
        if (eOut == null || eIn == null) {
            throw new IllegalVertexException();
        }
        final DirectedEdge<V, E> e1 = eOut.remove(target);
        final DirectedEdge<V, E> e2 = eIn.remove(source);
        assert (e1 == null && e2 == null) || (e1 != null && e2 != null && e1.equals(e2));
        if (e1 != null) {
            this.modCount++;
        }
        return e1;
    }
}
