package gr.james.influence.graph;

import gr.james.influence.api.Graph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.VertexPair;

import java.util.*;
import java.util.function.Consumer;

public abstract class GraphDecorator implements Graph {
    private Graph g;

    protected GraphDecorator(Graph g) {
        this.g = Conditions.requireNonNull(g);
    }

    @Override
    public String getMeta(String key) {
        return this.g.getMeta(key);
    }

    @Override
    public String setMeta(String key, String value) {
        return this.g.setMeta(key, value);
    }

    @Override
    public String removeMeta(String key) {
        return this.g.removeMeta(key);
    }

    @Override
    public Set<String> metaKeySet() {
        return this.g.metaKeySet();
    }

    @Override
    public void clearMeta() {
        this.g.clearMeta();
    }

    @Override
    public String getGraphType() {
        return this.g.getGraphType();
    }

    @Override
    public void setGraphType(String type) {
        this.g.setGraphType(type);
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this.g.iterator();
    }

    @Override
    public void forEach(Consumer<? super Vertex> action) {
        this.g.forEach(action);
    }

    @Override
    public Spliterator<Vertex> spliterator() {
        return this.g.spliterator();
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
    public Edge findEdge(Vertex source, Vertex target) {
        return this.g.findEdge(source, target);
    }

    @Override
    public Vertex getVertexFromIndex(int index) {
        return this.g.getVertexFromIndex(index);
    }

    @Override
    public List<Vertex> getVerticesFromLabel(String label) {
        return this.g.getVerticesFromLabel(label);
    }

    @Override
    public Vertex getVertexFromLabel(String label) {
        return this.g.getVertexFromLabel(label);
    }

    @Override
    public Vertex getRandomVertex(Random r) {
        return this.g.getRandomVertex(r);
    }

    @Override
    public Vertex getRandomVertex() {
        return this.g.getRandomVertex();
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
    public double getOutStrength(Vertex v) {
        return this.g.getOutStrength(v);
    }

    @Override
    public double getInStrength(Vertex v) {
        return this.g.getInStrength(v);
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
    public List<Vertex> getVertices() {
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

    @Override
    public double getDensity() {
        return this.g.getDensity();
    }

    @Override
    public double getAveragePathLength() {
        return g.getAveragePathLength();
    }

    @Override
    public Vertex addVertex() {
        return this.g.addVertex();
    }

    @Override
    public Vertex addVertex(String label) {
        return this.g.addVertex(label);
    }

    @Override
    public boolean addVertex(Vertex v) {
        return this.g.addVertex(v);
    }

    @Override
    public List<Vertex> addVertices(Vertex... vertices) {
        return this.g.addVertices(vertices);
    }

    @Override
    public List<Vertex> addVertices(int count) {
        return this.g.addVertices(count);
    }

    @Override
    public boolean removeVertex(Vertex v) {
        return this.g.removeVertex(v);
    }

    @Override
    public void removeVertices(Collection<Vertex> vertices) {
        this.g.removeVertices(vertices);
    }

    @Override
    public void addEdges(Collection<Vertex> among) {
        this.g.addEdges(among);
    }

    @Override
    public void addEdges(Vertex... among) {
        this.g.addEdges(among);
    }

    @Override
    public void clear() {
        this.g.clear();
    }

    @Override
    public Edge addEdge(Vertex source, Vertex target) {
        return this.g.addEdge(source, target);
    }

    @Override
    public boolean setEdgeWeight(Vertex source, Vertex target, double weight) {
        return this.g.setEdgeWeight(source, target, weight);
    }

    @Override
    public Edge addEdge(Vertex source, Vertex target, double weight) {
        return this.g.addEdge(source, target, weight);
    }

    @Override
    public boolean removeEdge(Vertex source, Vertex target) {
        return this.g.removeEdge(source, target);
    }

    @Override
    public void removeEdges(Collection<Vertex> among) {
        this.g.removeEdges(among);
    }

    @Override
    public void removeEdges(Vertex... among) {
        this.g.removeEdges(among);
    }

    @Override
    public String toString() {
        return this.g.toString();
    }
}
