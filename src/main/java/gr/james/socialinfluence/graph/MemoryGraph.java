package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.GraphException;
import gr.james.socialinfluence.util.collections.Pair;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs. This
 * implementation is using a cache mechanism; refer to individual methods for more information.</p>
 * <dl><dt><b>Complexity:</b></dt><dd>
 * Add Vertex: O(1)
 * <br>
 * Remove Vertex: O(n)
 * <br>
 * Contains Vertex: O(1)
 * <br>
 * Add Edge: O(1)
 * <br>
 * Remove Edge: O(1)
 * <br>
 * Get Out Edges of Vertex: O(1)
 * <br>
 * Get In Edges of Vertex: O(1)
 * <br>
 * Get Vertices: O(1)
 * </dd></dl>
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
        Objects.requireNonNull(v);
        if (this.containsVertex(v)) {
            return false;
        } else {
            graphStructureChanged();
            Pair<Map<Vertex, Edge>> pp = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>());
            this.m.put(v, pp);
            return true;
        }
    }

    @Override
    public boolean removeVertex(Vertex v) {
        Objects.requireNonNull(v);
        if (!this.containsVertex(v)) {
            return false;
        }
        for (Map.Entry<Vertex, Pair<Map<Vertex, Edge>>> e : this.m.entrySet()) {
            e.getValue().getFirst().remove(v);
            e.getValue().getSecond().remove(v);
        }
        graphStructureChanged();
        this.m.remove(v);
        return true;
    }

    @Override
    public void clear() {
        this.m.clear();
        graphStructureChanged();
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
    public Graph removeEdge(Vertex source, Vertex target) {
        this.m.get(source).getFirst().remove(target);
        this.m.get(target).getSecond().remove(source);
        return this;
    }

    @Override
    public Map<Vertex, Edge> getOutEdges(Vertex v) {
        return Collections.unmodifiableMap(this.m.get(v).getFirst());
    }

    @Override
    public Map<Vertex, Edge> getInEdges(Vertex v) {
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

    private void graphStructureChanged() {
        this.vertexCache = null;
    }
}
