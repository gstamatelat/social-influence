package gr.james.influence.graph;

import gr.james.influence.api.Graph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.Pair;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph extends AbstractGraph {
    private Map<Vertex, Pair<Map<Vertex, Edge>>> m;
    private List<Vertex> vList;

    /**
     * <p>Constructs an empty {@code MemoryGraph}.</p>
     */
    public MemoryGraph() {
        this.m = new HashMap<>(); // TODO: This was a LinkedHashMap; if it isn't causing any problems, remove this comment
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
            Pair<Map<Vertex, Edge>> pp = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>());
            this.m.put(v, pp);
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
            this.m.get(d).getSecond().remove(v);
        }
        for (Vertex d : this.m.get(v).getSecond().keySet()) {
            this.m.get(d).getFirst().remove(v);
        }
        this.m.remove(v);
        this.vList.remove(v);
        return true;
    }

    @Override
    public void clear() {
        this.m.clear();
        this.vList.clear();
    }

    @Override
    public Edge addEdge(Vertex source, Vertex target, double weight) {
        if (!containsEdge(source, target)) {
            Edge e = new Edge(weight);
            Edge e1 = this.m.get(source).getFirst().put(target, e);
            Edge e2 = this.m.get(target).getSecond().put(source, e);
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
            Edge h = this.m.get(target).getSecond().remove(source);
            assert h != null;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<Vertex, Edge> getOutEdges(Vertex v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.m.get(v).getFirst());
    }

    @Override
    public Map<Vertex, Edge> getInEdges(Vertex v) {
        Conditions.requireNonNullAndExists(v, this);
        return Collections.unmodifiableMap(this.m.get(v).getSecond());
    }

    @Override
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vList);
    }
}
