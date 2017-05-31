package gr.james.influence.graph;

import gr.james.influence.api.*;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.Pair;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph<V, E> extends TreeMapMetadata implements Graph<V, E> {
    private Map<V, Pair<Map<V, GraphEdge<V, E>>>> m;
    private List<V> vList;
    private VertexFactory<V> vertexFactory;
    private EdgeFactory<E> edgeFactory;

    /**
     * <p>Constructs an empty {@code MemoryGraph}.</p>
     *
     * @param vertexFactory the vertex factory to use when invoking {@link #addVertex()}
     * @param edgeFactory   the edge factory to use when invoking {@link #addEdge(Object, Object)} and related methods
     */
    public MemoryGraph(VertexFactory<V> vertexFactory, EdgeFactory<E> edgeFactory) {
        this.m = new HashMap<>();
        this.vList = new ArrayList<>();
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
    }

    public MemoryGraph(VertexFactory<V> vertexFactory) {
        this.m = new HashMap<>();
        this.vList = new ArrayList<>();
        this.vertexFactory = vertexFactory;
        this.edgeFactory = null;
    }

    public MemoryGraph(EdgeFactory<E> edgeFactory) {
        this.m = new HashMap<>();
        this.vList = new ArrayList<>();
        this.vertexFactory = null;
        this.edgeFactory = edgeFactory;
    }

    public MemoryGraph() {
        this.m = new HashMap<>();
        this.vList = new ArrayList<>();
        this.vertexFactory = null;
        this.edgeFactory = null;
    }

    @Override
    public VertexFactory<V> getVertexFactory() {
        return vertexFactory;
    }

    @Override
    public EdgeFactory<E> getEdgeFactory() {
        return edgeFactory;
    }

    @Override
    public GraphFactory<V, E> getGraphFactory() {
        return new GraphFactory<V, E>() {
            @Override
            public Graph<V, E> create() {
                return new MemoryGraph<>(vertexFactory, edgeFactory);
            }

            @Override
            public VertexFactory<V> getVertexFactory() {
                return vertexFactory;
            }

            @Override
            public EdgeFactory<E> getEdgeFactory() {
                return edgeFactory;
            }
        };
    }

    @Override
    public boolean containsVertex(V v) {
        return this.m.containsKey(Conditions.requireNonNull(v));
    }

    @Override
    public boolean addVertex(V v) {
        if (this.containsVertex(v)) {
            return false;
        } else {
            Pair<Map<V, GraphEdge<V, E>>> pp = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>());
            Object o = this.m.put(v, pp);
            assert o == null;
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
        for (V d : this.m.get(v).getFirst().keySet()) {
            Object o = this.m.get(d).getSecond().remove(v);
            assert o != null;
        }
        for (V d : this.m.get(v).getSecond().keySet()) {
            Object o = this.m.get(d).getFirst().remove(v);
            assert o != null;
        }
        Object o1 = this.m.remove(v);
        boolean o2 = this.vList.remove(v);
        assert o1 != null;
        assert o2;
        return true;
    }

    @Override
    public void clear() {
        this.m.clear();
        this.vList.clear();
    }

    @Override
    public GraphEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        if (!containsEdge(source, target)) {
            GraphEdge<V, E> e = new MemoryGraphEdge<>(edge, source, target, weight);
            GraphEdge<V, E> e1 = this.m.get(source).getFirst().put(target, e);
            GraphEdge<V, E> e2 = this.m.get(target).getSecond().put(source, e);
            assert e1 == null;
            assert e2 == null;
            return e;
        } else {
            return null;
        }
    }

    @Override
    public GraphEdge<V, E> removeEdge(V source, V target) {
        if (this.m.get(source).getFirst().remove(target) != null) {
            GraphEdge<V, E> h = this.m.get(target).getSecond().remove(source);
            assert h != null;
            return h;
        } else {
            return null;
        }
    }

    @Override
    public Map<V, GraphEdge<V, E>> getOutEdges(V v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.m.get(v).getFirst());
    }

    @Override
    public Map<V, GraphEdge<V, E>> getInEdges(V v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.m.get(v).getSecond());
    }

    @Override
    public List<V> getVertices() {
        return Collections.unmodifiableList(vList);
    }

    @Override
    public String toString() {
        return String.format("{container=%s, meta=%s}", this.getClass().getSimpleName(), this.meta);
    }
}
