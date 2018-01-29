package gr.james.influence.graph;

import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.List;

/**
 * <p>Unmodifiable decorator of a {@link Graph}.</p>
 */
public final class ImmutableGraph<V, E> extends GraphDecorator<V, E> {
    private ImmutableGraph(Graph<V, E> g) {
        super(g);
    }

    public static <V, E> ImmutableGraph<V, E> decorate(Graph<V, E> g) {
        if (g instanceof ImmutableGraph) {
            return (ImmutableGraph<V, E>) g;
        } else {
            return new ImmutableGraph<>(g);
        }
    }

    @Override
    public String setMeta(String key, String value) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public String removeMeta(String key) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void clearMeta() {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public V addVertex(VertexProvider<V> vertexProvider) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean addVertex(V v) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public List<V> addVertices(V... vertices) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public List<V> addVertices(Iterable<V> vertices) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public List<V> addVertices(int count, VertexProvider<V> vertexProvider) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean removeVertex(V v) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void removeVertices(Iterable<V> vertices) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void addEdges(Iterable<V> among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void addEdges(V... among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, double weight) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean setEdgeWeight(V source, V target, double weight) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void removeEdges(Iterable<V> among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void removeEdges(V... among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void clearEdges() {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }
}
