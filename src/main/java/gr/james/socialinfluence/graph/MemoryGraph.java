package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.collections.Pair;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph extends Graph {
    private Map<Vertex, Pair<Map<Vertex, Edge>>> m;

    public MemoryGraph() {
        this.m = new LinkedHashMap<>();
    }

    public Vertex addVertex(Vertex v) {
        Pair<Map<Vertex, Edge>> pp = new Pair<Map<Vertex, Edge>>(new LinkedHashMap<Vertex, Edge>(), new LinkedHashMap<Vertex, Edge>());
        this.m.put(v, pp);
        return v;
    }


    /**
     * {@inheritDoc}
     * <dl><dt><b>Complexity:</b></dt><dd>O(n)</dd></dl>
     *
     * @throws GraphException {@inheritDoc}
     */
    public Graph removeVertex(Vertex v) {
        if (!this.containsVertex(v)) {
            throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "removeVertex");
        }
        for (Map.Entry<Vertex, Pair<Map<Vertex, Edge>>> e : this.m.entrySet()) {
            e.getValue().getFirst().remove(v);
            e.getValue().getSecond().remove(v);
        }
        this.m.remove(v);
        return this;
    }

    public Graph clear() {
        this.m.clear();
        return this;
    }

    public boolean containsVertex(Vertex v) {
        return this.m.containsKey(v);
    }

    public boolean containsEdge(Vertex source, Vertex target) {
        return this.m.get(source).getFirst().containsKey(target);
    }

    public Vertex getVertexFromIndex(int index) {
        if (index < 0 || index >= this.getVerticesCount()) {
            throw new GraphException(Finals.E_GRAPH_INDEX_OUT_OF_BOUNDS, index);
        }
        Iterator<Vertex> it = this.m.keySet().iterator();
        Vertex v = it.next();
        while (index-- > 0) {
            v = it.next();
        }
        return v;
    }

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

    public Graph removeEdge(Vertex source, Vertex target) {
        this.m.get(source).getFirst().remove(target);
        this.m.get(target).getSecond().remove(source);
        return this;
    }

    public Map<Vertex, Edge> getOutEdges(Vertex v) {
        return Collections.unmodifiableMap(this.m.get(v).getFirst());
    }

    public Map<Vertex, Edge> getInEdges(Vertex v) {
        return Collections.unmodifiableMap(this.m.get(v).getSecond());
    }

    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(this.m.keySet());
    }
}