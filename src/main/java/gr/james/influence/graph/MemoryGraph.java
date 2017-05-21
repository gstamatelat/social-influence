package gr.james.influence.graph;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.Pair;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph extends AbstractGraph {
    private Map<Vertex, Pair<Map<Vertex, GraphEdge>>> m;
    private List<Vertex> vList;

    /**
     * <p>Constructs an empty {@code MemoryGraph}.</p>
     */
    public MemoryGraph() {
        this.m = new HashMap<>();
        this.vList = new ArrayList<>();
    }

    @Override
    public boolean containsVertex(Vertex v) {
        return this.m.containsKey(Conditions.requireNonNull(v));
    }

    @Override
    public boolean addVertex(Vertex v) {
        if (this.containsVertex(v)) {
            return false;
        } else {
            Pair<Map<Vertex, GraphEdge>> pp = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>());
            Object o = this.m.put(v, pp);
            assert o == null;
            this.vList.add(v);
            return true;
        }
    }

    @Override
    public boolean removeVertex(Vertex v) {
        if (!this.containsVertex(v)) {
            return false;
        }
        for (Vertex d : this.m.get(v).getFirst().keySet()) {
            Object o = this.m.get(d).getSecond().remove(v);
            assert o != null;
        }
        for (Vertex d : this.m.get(v).getSecond().keySet()) {
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
    public GraphEdge addEdge(Vertex source, Vertex target, double weight) {
        Conditions.requireArgument(weight > 0, Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        if (!containsEdge(source, target)) {
            GraphEdge e = new MemoryGraphEdge(new Edge(), source, target, weight);
            GraphEdge e1 = this.m.get(source).getFirst().put(target, e);
            GraphEdge e2 = this.m.get(target).getSecond().put(source, e);
            assert e1 == null;
            assert e2 == null;
            return e;
        } else {
            return null;
        }
    }

    @Override
    public boolean removeEdge(Vertex source, Vertex target) {
        if (this.m.get(source).getFirst().remove(target) != null) {
            GraphEdge h = this.m.get(target).getSecond().remove(source);
            assert h != null;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<Vertex, GraphEdge> getOutEdges(Vertex v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.m.get(v).getFirst());
    }

    @Override
    public Map<Vertex, GraphEdge> getInEdges(Vertex v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.m.get(v).getSecond());
    }

    @Override
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vList);
    }
}
