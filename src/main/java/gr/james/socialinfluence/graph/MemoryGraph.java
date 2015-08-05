package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.collections.Pair;
import gr.james.socialinfluence.util.exceptions.GraphException;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists.</p>
 */
public class MemoryGraph extends AbstractGraph {
    private Map<Vertex, Pair<Map<Vertex, Edge>>> m;
    private List<Vertex> vertexCache;

    /**
     * <p>Constructs an empty {@code MemoryGraph}.</p>
     */
    public MemoryGraph() {
        this.m = new LinkedHashMap<>();
        this.vertexCache = null;
    }

    @Override
    public boolean containsVertex(Vertex v) {
        return this.m.containsKey(v);
    }

    @Override
    public boolean addVertex(Vertex v) {
        Conditions.requireNonNull(v);
        if (this.containsVertex(v)) {
            return false;
        } else {
            Pair<Map<Vertex, Edge>> pp = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>());
            this.m.put(v, pp);
            verticesChanged();
            return true;
        }
    }

    @Override
    public boolean removeVertex(Vertex v) {
        Conditions.requireNonNull(v);
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
        verticesChanged();
        return true;
    }

    @Override
    public void clear() {
        this.m.clear();
        verticesChanged();
    }

    @Override
    public Edge addEdge(Vertex source, Vertex target) {
        if (!this.containsVertex(source) || !this.containsVertex(target)) {
            throw new GraphException(Finals.E_GRAPH_EDGE_DIFFERENT);
        }
        Edge e = new Edge();
        if (!this.m.get(source).getFirst().containsKey(target)) {
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
    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(this.m.keySet());
    }

    @Override
    public List<Vertex> getVerticesAsList() {
        if (this.vertexCache == null) {
            this.vertexCache = new ArrayList<>(this.m.keySet());
        }
        return Collections.unmodifiableList(vertexCache);
    }

    private void verticesChanged() {
        this.vertexCache = null;
    }
}
