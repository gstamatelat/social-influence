package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.AbstractGraph;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.GraphException;
import gr.james.socialinfluence.util.collections.Pair;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph extends AbstractGraph {
    private Map<Vertex, Pair<Map<Vertex, Edge>>> m;

    public MemoryGraph() {
        this.m = new LinkedHashMap<>();
    }

    @Override
    public Vertex addVertex(Vertex v) {
        Pair<Map<Vertex, Edge>> pp = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>());
        this.m.put(v, pp);
        return v;
    }

    /**
     * {@inheritDoc}
     * <dl><dt><b>Complexity:</b></dt><dd>O(n)</dd></dl>
     *
     * @throws GraphException {@inheritDoc}
     */
    @Override
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

    @Override
    public Graph clear() {
        this.m.clear();
        return this;
    }

    @Override
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
}
