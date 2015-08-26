package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.collections.Pair;

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
    public Edge addEdge(Vertex source, Vertex target) {
        if (!containsEdge(source, target)) {
            Edge e = new Edge();
            this.m.get(source).getFirst().put(target, e);
            this.m.get(target).getSecond().put(source, e);
            return e;
        } else {
            return null;
        }
    }

    @Override
    public boolean removeEdge(Vertex source, Vertex target) {
        if (!this.containsEdge(source, target)) {
            return false;
        } else {
            this.m.get(source).getFirst().remove(target);
            this.m.get(target).getSecond().remove(source);
            return true;
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
