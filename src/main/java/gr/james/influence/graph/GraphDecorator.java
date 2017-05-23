package gr.james.influence.graph;

import gr.james.influence.api.*;
import gr.james.influence.util.Conditions;

import java.util.*;
import java.util.function.Consumer;

public abstract class GraphDecorator<V, E> implements Graph<V, E> {
    private Graph<V, E> g;

    protected GraphDecorator(Graph<V, E> g) {
        this.g = Conditions.requireNonNull(g);
    }

    @Override
    public VertexFactory<V> getVertexFactory() {
        return this.g.getVertexFactory();
    }

    @Override
    public EdgeFactory<E> getEdgeFactory() {
        return this.g.getEdgeFactory();
    }

    @Override
    public GraphFactory<V, E> getGraphFactory() {
        return this.g.getGraphFactory();
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
    public Iterator<V> iterator() {
        return this.g.iterator();
    }

    @Override
    public void forEach(Consumer<? super V> action) {
        this.g.forEach(action);
    }

    @Override
    public Spliterator<V> spliterator() {
        return this.g.spliterator();
    }

    @Override
    public boolean containsVertex(V v) {
        return this.g.containsVertex(v);
    }

    @Override
    public boolean containsEdge(V source, V target) {
        return this.g.containsEdge(source, target);
    }

    @Override
    public GraphEdge<V, E> findEdge(V source, V target) {
        return this.g.findEdge(source, target);
    }

    @Override
    public V getVertexFromIndex(int index) {
        return this.g.getVertexFromIndex(index);
    }

    /*@Override
    public List<V> getVerticesFromLabel(String label) {
        return this.g.getVerticesFromLabel(label);
    }

    @Override
    public V getVertexFromLabel(String label) {
        return this.g.getVertexFromLabel(label);
    }*/

    @Override
    public V getRandomVertex(Random r) {
        return this.g.getRandomVertex(r);
    }

    @Override
    public V getRandomVertex() {
        return this.g.getRandomVertex();
    }

    /*@Override
    public Map<Pair<V>, GraphEdge<V, E>> getEdges() {
        return this.g.getEdges();
    }*/

    @Override
    public Collection<GraphEdge<V, E>> getEdges() {
        return this.g.getEdges();
    }

    @Override
    public int getEdgesCount() {
        return this.g.getEdgesCount();
    }

    @Override
    public Map<V, GraphEdge<V, E>> getOutEdges(V v) {
        return this.g.getOutEdges(v);
    }

    @Override
    public Map<V, GraphEdge<V, E>> getInEdges(V v) {
        return this.g.getInEdges(v);
    }

    @Override
    public double getOutStrength(V v) {
        return this.g.getOutStrength(v);
    }

    @Override
    public double getInStrength(V v) {
        return this.g.getInStrength(v);
    }

    @Override
    public int getOutDegree(V v) {
        return this.g.getOutDegree(v);
    }

    @Override
    public int getInDegree(V v) {
        return this.g.getInDegree(v);
    }

    @Override
    public List<V> getVertices() {
        return this.g.getVertices();
    }

    @Override
    public int getVerticesCount() {
        return this.g.getVerticesCount();
    }

    @Override
    public V getRandomOutEdge(V from, boolean weighted) {
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
    public V addVertex() {
        return this.g.addVertex();
    }

    /*@Override
    public V addVertex(String label) {
        return this.g.addVertex(label);
    }*/

    @Override
    public boolean addVertex(V v) {
        return this.g.addVertex(v);
    }

    @Override
    public List<V> addVertices(V... vertices) {
        return this.g.addVertices(vertices);
    }

    @Override
    public List<V> addVertices(int count) {
        return this.g.addVertices(count);
    }

    @Override
    public boolean removeVertex(V v) {
        return this.g.removeVertex(v);
    }

    @Override
    public void removeVertices(Collection<V> vertices) {
        this.g.removeVertices(vertices);
    }

    @Override
    public void addEdges(Collection<V> among) {
        this.g.addEdges(among);
    }

    @Override
    public void addEdges(V... among) {
        this.g.addEdges(among);
    }

    @Override
    public void clear() {
        this.g.clear();
    }

    @Override
    public GraphEdge<V, E> addEdge(V source, V target) {
        return this.g.addEdge(source, target);
    }

    @Override
    public boolean setEdgeWeight(V source, V target, double weight) {
        return this.g.setEdgeWeight(source, target, weight);
    }

    @Override
    public GraphEdge<V, E> addEdge(V source, V target, double weight) {
        return this.g.addEdge(source, target, weight);
    }

    @Override
    public GraphEdge<V, E> removeEdge(V source, V target) {
        return this.g.removeEdge(source, target);
    }

    @Override
    public void removeEdges(Collection<V> among) {
        this.g.removeEdges(among);
    }

    @Override
    public void removeEdges(V... among) {
        this.g.removeEdges(among);
    }

    @Override
    public String toString() {
        return this.g.toString();
    }
}
