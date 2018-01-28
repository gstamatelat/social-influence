package gr.james.influence.graph;

import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.api.graph.Graph;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Provides a way to create a {@link Graph} decorator with reversed edges.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public final class EdgeReversedGraph<V, E> extends GraphDecorator<V, E> {
    private EdgeReversedGraph(Graph<V, E> g) {
        super(g);
    }

    /**
     * Decorate {@code g} as a graph with reversed edges.
     *
     * @param g   the graph to decorate
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return a decorator of {@code g} with reversed edges
     */
    public static <V, E> Graph<V, E> decorate(Graph<V, E> g) {
        return new EdgeReversedGraph<>(g);
    }

    @Override
    public boolean containsEdge(V source, V target) {
        return super.containsEdge(target, source);
    }

    @Override
    public DirectedEdge<V, E> findEdge(V source, V target) {
        return super.findEdge(target, source);
    }

    @Override
    public double getWeight(V source, V target) {
        return super.getWeight(target, source);
    }

    @Override
    public double getWeightElse(V source, V target, double other) {
        return super.getWeightElse(target, source, other);
    }

    @Override
    public E getEdge(V source, V target) {
        return super.getEdge(target, source);
    }

    @Override
    public E getEdgeElse(V source, V target, E other) {
        return super.getEdgeElse(target, source, other);
    }

    @Override
    public Set<V> adjacentIn(V v) {
        return super.adjacentOut(v);
    }

    @Override
    public Set<V> adjacentOut(V v) {
        return super.adjacentIn(v);
    }

    @Override
    public Set<DirectedEdge<V, E>> inEdges(V v) {
        final Set<DirectedEdge<V, E>> edges = super.outEdges(v);
        return new AbstractSet<DirectedEdge<V, E>>() {
            @Override
            public Iterator<DirectedEdge<V, E>> iterator() {
                return new Iterator<DirectedEdge<V, E>>() {
                    final Iterator<DirectedEdge<V, E>> it = edges.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public DirectedEdge<V, E> next() {
                        return it.next().reverse();
                    }
                };
            }

            @Override
            public int size() {
                return edges.size();
            }
        };
    }

    @Override
    public Set<DirectedEdge<V, E>> outEdges(V v) {
        final Set<DirectedEdge<V, E>> edges = super.inEdges(v);
        return new AbstractSet<DirectedEdge<V, E>>() {
            @Override
            public Iterator<DirectedEdge<V, E>> iterator() {
                return new Iterator<DirectedEdge<V, E>>() {
                    final Iterator<DirectedEdge<V, E>> it = edges.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public DirectedEdge<V, E> next() {
                        return it.next().reverse();
                    }
                };
            }

            @Override
            public int size() {
                return edges.size();
            }
        };
    }

    @Override
    public Iterable<DirectedEdge<V, E>> edges() {
        Iterable<DirectedEdge<V, E>> edges = super.edges();
        return () -> {
            final Iterator<DirectedEdge<V, E>> it = edges.iterator();
            return new Iterator<DirectedEdge<V, E>>() {
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public DirectedEdge<V, E> next() {
                    return it.next().reverse();
                }
            };
        };
    }

    @Override
    public double outStrength(V v) {
        return super.inStrength(v);
    }

    @Override
    public double inStrength(V v) {
        return super.outStrength(v);
    }

    @Override
    public int outDegree(V v) {
        return super.inDegree(v);
    }

    @Override
    public int inDegree(V v) {
        return super.outDegree(v);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target) {
        return super.addEdge(target, source);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, double weight) {
        return super.addEdge(target, source, weight);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge) {
        return super.addEdge(target, source, edge);
    }

    @Override
    public DirectedEdge<V, E> addEdge(V source, V target, E edge, double weight) {
        return super.addEdge(target, source, edge, weight);
    }

    @Override
    public boolean setEdgeWeight(V source, V target, double weight) {
        return super.setEdgeWeight(target, source, weight);
    }

    @Override
    public DirectedEdge<V, E> removeEdge(V source, V target) {
        return super.removeEdge(target, source);
    }
}
