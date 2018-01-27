package gr.james.influence.graph;

import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.util.Conditions;

import java.util.*;
import java.util.function.Consumer;

public abstract class GraphDecorator<V, E> implements Graph<V, E> {
    private Graph<V, E> g;

    protected GraphDecorator(Graph<V, E> g) {
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
    public DirectedEdge<V, E> findEdge(V source, V target) {
        return this.g.findEdge(source, target);
    }

    @Override
    public double getWeight(V source, V target) {
        return this.g.getWeight(source, target);
    }

    @Override
    public double getWeightElse(V source, V target, double other) {
        return this.g.getWeightElse(source, target, other);
    }

    @Override
    public E getEdge(V source, V target) {
        return this.g.getEdge(source, target);
    }

    @Override
    public E getEdgeElse(V source, V target, E other) {
        return this.g.getEdgeElse(source, target, other);
    }

    @Override
    public V getVertexFromIndex(int index) {
        return this.g.getVertexFromIndex(index);
    }

    @Override
    public Collection<DirectedEdge<V, E>> getEdges() {
        return this.g.getEdges();
    }

    @Override
    public Map<V, DirectedEdge<V, E>> getOutEdges(V v) {
        return this.g.getOutEdges(v);
    }

    @Override
    public Set<DirectedEdge<V, E>> outEdges(V v) {
        return this.g.outEdges(v);
    }

    @Override
    public Set<V> adjacentOut(V v) {
        return this.g.adjacentOut(v);
    }

    @Override
    public Map<V, DirectedEdge<V, E>> getInEdges(V v) {
        return this.g.getInEdges(v);
    }

    @Override
    public Set<DirectedEdge<V, E>> inEdges(V v) {
        return this.g.inEdges(v);
    }

    @Override
    public Set<V> adjacentIn(V v) {
        return this.g.adjacentIn(v);
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
    public int outDegree(V v) {
        return this.g.outDegree(v);
    }

    @Override
    public int inDegree(V v) {
        return this.g.inDegree(v);
    }

    @Override
    public List<V> getVertices() {
        return this.g.getVertices();
    }

    @Override
    public Set<V> vertexSet() {
        return this.g.vertexSet();
    }

    @Override
    public int vertexCount() {
        return this.g.vertexCount();
    }

    @Override
    public V getRandomOutEdge(V from, boolean weighted) {
        return this.g.getRandomOutEdge(from, weighted);
    }

    @Override
    public double getDensity() {
        return this.g.getDensity();
    }

    @Override
    public V addVertex() {
        return this.g.addVertex();
    }

    @Override
    public boolean addVertex(V v) {
        return this.g.addVertex(v);
    }

    @Override
    public List<V> addVertices(V... vertices) {
        return this.g.addVertices(vertices);
    }

    @Override
    public List<V> addVertices(Iterable<V> vertices) {
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
    public void removeVertices(Iterable<V> vertices) {
        this.g.removeVertices(vertices);
    }

    @Override
    public void addEdges(Iterable<V> among) {
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
    public DirectedEdge<V, E> addEdge(V source, V target) {
        return this.g.addEdge(source, target);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, double weight) {
        return this.g.addEdge(source, target, weight);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge) {
        return this.g.addEdge(source, target, edge);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        return this.g.addEdge(source, target, edge, weight);
    }

    @Override
    public boolean setEdgeWeight(V source, V target, double weight) {
        return this.g.setEdgeWeight(source, target, weight);
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        return this.g.removeEdge(source, target);
    }

    @Override
    public void removeEdges(Iterable<V> among) {
        this.g.removeEdges(among);
    }

    @Override
    public void clearEdges() {
        this.g.clearEdges();
    }

    @Override
    public void removeEdges(V... among) {
        this.g.removeEdges(among);
    }

    @Override
    public Iterable<DirectedEdge<V, E>> edges() {
        return this.g.edges();
    }

    @Override
    public String toString() {
        return this.g.toString();
    }
}
