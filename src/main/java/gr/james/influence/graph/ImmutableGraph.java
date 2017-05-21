package gr.james.influence.graph;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.Finals;

import java.util.Collection;
import java.util.List;

/**
 * <p>Unmodifiable decorator of a {@link Graph}.</p>
 */
public final class ImmutableGraph extends GraphDecorator {
    private ImmutableGraph(Graph g) {
        super(g);
    }

    public static ImmutableGraph decorate(Graph g) {
        if (g instanceof ImmutableGraph) {
            Finals.LOG.debug("Graph {} is already an instance of ImmutableGraph", g);
            return (ImmutableGraph) g;
        } else {
            return new ImmutableGraph(g);
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
    public void setGraphType(String type) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public Vertex addVertex() {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public Vertex addVertex(String label) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean addVertex(Vertex v) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public List<Vertex> addVertices(Vertex... vertices) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public List<Vertex> addVertices(int count) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean removeVertex(Vertex v) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void removeVertices(Collection<Vertex> vertices) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void addEdges(Collection<Vertex> among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void addEdges(Vertex... among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public GraphEdge addEdge(Vertex source, Vertex target) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean setEdgeWeight(Vertex source, Vertex target, double weight) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public GraphEdge addEdge(Vertex source, Vertex target, double weight) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public boolean removeEdge(Vertex source, Vertex target) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void removeEdges(Collection<Vertex> among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }

    @Override
    public void removeEdges(Vertex... among) {
        throw new UnsupportedOperationException(Finals.E_IMMUTABLE_GRAPH);
    }
}
