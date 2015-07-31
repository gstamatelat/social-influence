package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImmutableGraph implements Graph {
    // TODO: Edge has setWeight method, which can be used to change the graph
    private Graph g;

    public ImmutableGraph(Graph g) {
        this.g = g;
    }

    @Override
    public String getMeta(String key) {
        return this.g.getMeta(key);
    }

    @Override
    public Graph setMeta(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph clearMeta() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vertex getVertexFromIndex(int index) {
        return this.g.getVertexFromIndex(index);
    }

    @Override
    public Map<Vertex, Edge> getOutEdges(Vertex v) {
        return this.g.getOutEdges(v);
    }

    @Override
    public Map<Vertex, Edge> getInEdges(Vertex v) {
        return this.g.getInEdges(v);
    }

    @Override
    public Set<Vertex> getVertices() {
        return this.g.getVertices();
    }

    @Override
    public List<Vertex> getVerticesAsList() {
        return this.g.getVerticesAsList();
    }

    @Override
    public Vertex addVertex(Vertex v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph removeVertex(Vertex v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Edge addEdge(Vertex source, Vertex target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph removeEdge(Vertex source, Vertex target) {
        throw new UnsupportedOperationException();
    }
}
