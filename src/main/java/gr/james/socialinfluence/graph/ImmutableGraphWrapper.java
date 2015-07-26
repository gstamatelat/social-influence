package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.ImmutableGraph;
import gr.james.socialinfluence.util.collections.VertexPair;

import java.util.Map;
import java.util.Set;

public class ImmutableGraphWrapper implements ImmutableGraph {
    private Graph g;

    public ImmutableGraphWrapper(Graph g) {
        this.g = g;
    }

    @Override
    public String getMeta(String key) {
        return this.g.getMeta(key);
    }

    @Override
    public String getGraphType() {
        return this.g.getGraphType();
    }

    @Override
    public boolean containsVertex(Vertex v) {
        return this.g.containsVertex(v);
    }

    @Override
    public boolean containsEdge(Vertex source, Vertex target) {
        return this.g.containsEdge(source, target);
    }

    @Override
    public <T extends Graph> Graph deepCopy(Class<T> type) {
        return this.g.deepCopy(type);
    }

    @Override
    public <T extends Graph> Graph deepCopy(Class<T> type, Set<Vertex> includeOnly) {
        return this.g.deepCopy(type, includeOnly);
    }

    @Override
    public Vertex getVertexFromIndex(int index) {
        return this.g.getVertexFromIndex(index);
    }

    @Override
    public Vertex getRandomVertex() {
        return this.g.getRandomVertex();
    }

    @Override
    public Set<Vertex> getStubbornVertices() {
        return this.g.getStubbornVertices();
    }

    @Override
    public Map<VertexPair, Edge> getEdges() {
        return this.g.getEdges();
    }

    @Override
    public int getEdgesCount() {
        return this.g.getEdgesCount();
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
    public double getOutWeightSum(Vertex v) {
        return this.g.getOutWeightSum(v);
    }

    @Override
    public double getInWeightSum(Vertex v) {
        return this.g.getInWeightSum(v);
    }

    @Override
    public int getOutDegree(Vertex v) {
        return this.g.getOutDegree(v);
    }

    @Override
    public int getInDegree(Vertex v) {
        return this.g.getInDegree(v);
    }

    @Override
    public boolean isUndirected() {
        return this.g.isUndirected();
    }

    @Override
    public Set<Vertex> getVertices() {
        return this.g.getVertices();
    }

    @Override
    public int getVerticesCount() {
        return this.g.getVerticesCount();
    }

    @Override
    public Vertex getRandomOutEdge(Vertex from, boolean weighted) {
        return this.g.getRandomOutEdge(from, weighted);
    }

    @Override
    public double getDiameter() {
        return this.g.getDiameter();
    }
}
