package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class AbstractGraph implements Graph {
    protected Map<String, String> meta;

    public AbstractGraph() {
        this.meta = new TreeMap<>();
    }

    @Override
    public final String getMeta(String key) {
        return this.meta.get(key);
    }

    @Override
    public final Graph setMeta(String key, String value) {
        this.meta.put(key, value);
        return this;
    }

    @Override
    public Graph clearMeta() {
        this.meta.clear();
        return this;
    }

    @Override
    public String getGraphType() {
        return this.meta.get("type");
    }

    @Override
    public abstract Vertex addVertex(Vertex v);

    @Override
    public abstract boolean containsVertex(Vertex v);

    @Override
    public abstract boolean containsEdge(Vertex source, Vertex target);

    @Override
    public abstract Graph removeVertex(Vertex v);

    @Override
    public abstract Graph clear();

    @Override
    public abstract Vertex getVertexFromIndex(int index);

    @Override
    public abstract Edge addEdge(Vertex source, Vertex target);

    @Override
    public abstract Graph removeEdge(Vertex source, Vertex target);

    @Override
    public abstract Map<Vertex, Edge> getOutEdges(Vertex v);

    @Override
    public abstract Map<Vertex, Edge> getInEdges(Vertex v);

    @Override
    public abstract Set<Vertex> getVertices();

    @Override
    public String toString() {
        return String.format("{type=%s, meta=%s}", this.getClass().getSimpleName(), meta);
    }
}
