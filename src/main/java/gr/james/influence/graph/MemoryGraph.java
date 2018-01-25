package gr.james.influence.graph;

import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.VertexProvider;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph<V, E> extends TreeMapMetadata implements Graph<V, E> {
    private final Map<V, Map<V, DirectedEdge<V, E>>> mOut;
    private final Map<V, Map<V, DirectedEdge<V, E>>> mIn;
    private final List<V> vList;
    private final VertexProvider<V> vertexProvider;

    /**
     * <p>Constructs an empty {@code MemoryGraph}.</p>
     */
    public MemoryGraph() {
        this(null);
    }

    public MemoryGraph(VertexProvider<V> vertexProvider) {
        this.mOut = new HashMap<>();
        this.mIn = new HashMap<>();
        this.vList = new ArrayList<>();
        this.vertexProvider = vertexProvider;
    }

    @Override
    public boolean addVertex(V v) {
        if (this.containsVertex(v)) {
            return false;
        } else {
            final Object o1 = this.mOut.put(v, new HashMap<>());
            final Object o2 = this.mIn.put(v, new HashMap<>());
            assert o1 == null && o2 == null;
            assert !this.vList.contains(v);
            this.vList.add(v);
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
        final boolean o3 = this.vList.remove(v);
        assert o1 != null && o2 != null;
        assert o3;
        return true;
    }

    @Override
    public void clear() {
        this.mOut.clear();
        this.mIn.clear();
        this.vList.clear();
    }

    @Override
    public void clearEdges() {
        for (V v : this) {
            this.mOut.get(v).clear();
            this.mIn.get(v).clear();
        }
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        if (!containsEdge(source, target)) {
            final DirectedEdge<V, E> e = new MemoryDirectedEdge<>(edge, source, target, weight);
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
            assert h.getSource().equals(source);
            assert h.getTarget().equals(target);
            return h;
        } else {
            return null;
        }
    }

    @Override
    public Map<V, DirectedEdge<V, E>> getOutEdges(V v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.mOut.get(v));
    }

    @Override
    public Set<V> adjacentOut(V v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableSet(this.mOut.get(v).keySet());
    }

    @Override
    public Map<V, DirectedEdge<V, E>> getInEdges(V v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.mIn.get(v));
    }

    @Override
    public Set<V> adjacentIn(V v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableSet(this.mIn.get(v).keySet());
    }

    @Override
    public List<V> getVertices() {
        return Collections.unmodifiableList(vList);
    }

    @Override
    public Set<V> vertexSet() {
        assert Objects.equals(this.mOut.keySet(), this.mIn.keySet());
        return Collections.unmodifiableSet(this.mOut.keySet());
    }

    @Override
    public V addVertex() {
        if (vertexProvider == null) {
            throw new UnsupportedOperationException();
        }
        final V v = vertexProvider.getVertex();
        if (!addVertex(v)) {
            throw new IllegalVertexException();
        }
        return v;
    }

    @Override
    public String toString() {
        return String.format("{container=%s, meta=%s}", this.getClass().getSimpleName(), this.meta);
    }
}
