package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ImmutableGraph implements Graph {
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
    public String getName() {
        return this.g.getName();
    }

    @Override
    public Vertex addVertex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vertex addVertex(Vertex v) {
        throw new UnsupportedOperationException();
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
    public Set<Vertex> addVertices(int count) {
        throw new UnsupportedOperationException();
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
    public Graph removeVertex(Vertex v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph removeVertices(Collection<Vertex> vertices) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph clear() {
        throw new UnsupportedOperationException();
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
    public Set<FullEdge> getEdges() {
        return this.g.getEdges();
    }

    @Override
    public int getEdgesCount() {
        return this.g.getEdgesCount();
    }

    @Override
    public Edge addEdge(Vertex source, Vertex target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Graph removeEdge(Vertex source, Vertex target) {
        throw new UnsupportedOperationException();
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