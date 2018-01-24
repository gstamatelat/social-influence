package gr.james.influence.util.collections;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.GraphEdge;
import gr.james.influence.util.Conditions;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Read only {@link Iterator} over the edges of a graph.
 * <p>
 * This iterator returns the edges of a graph in no particular order.
 * <p>
 * The method {@link #remove()} is not supported, so it will always throw {@link UnsupportedOperationException}.
 * <p>
 * This iterator is exhausted in time {@code O(V+E)} and uses constant extra space.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public class EdgesIterator<V, E> implements Iterator<GraphEdge<V, E>> {
    private final Graph<V, E> g;
    private final Iterator<V> vertexIterator;
    private boolean hasNext;
    private Iterator<GraphEdge<V, E>> buffer;

    /**
     * Construct a new {@link EdgesIterator} using the specified graph {@code g}.
     *
     * @param g the graph over which to iterate the edges
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public EdgesIterator(@UnmodifiableGraph Graph<V, E> g) {
        Conditions.requireNonNull(g);

        this.g = g;
        this.vertexIterator = g.iterator();
        this.hasNext = true;
        this.buffer = Collections.emptyIterator();

        advance();
    }

    private void advance() {
        while (!buffer.hasNext()) {
            if (vertexIterator.hasNext()) {
                buffer = g.getOutEdges(vertexIterator.next()).values().iterator();
            } else {
                hasNext = false;
                return;
            }
        }
    }

    /**
     * Returns {@code true} if the iteration has more edges, otherwise returns {@code false}.
     *
     * @return {@code true} if the iteration has more edges, otherwise {@code false}
     */
    @Override
    public boolean hasNext() {
        return hasNext;
    }

    /**
     * Returns the next edge in the iteration.
     *
     * @return the next edge in the iteration
     * @throws NoSuchElementException if the iteration has no more edges
     */
    @Override
    public GraphEdge<V, E> next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        assert buffer.hasNext();
        final GraphEdge<V, E> n = buffer.next();
        advance();
        return n;
    }

    /**
     * The {@code remove()} method is not supported and will always throw an {@link UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public void remove() {
        Iterator.super.remove();
    }

    /**
     * Performs the given action for each remaining edge until all edges have been processed or the action throws an
     * exception.
     * <p>
     * Actions are performed in the order of iteration. Exceptions thrown by the action are relayed to the caller.
     *
     * @param action the action to be performed for each edge
     * @throws NullPointerException if the specified {@code action} is {@code null}
     */
    @Override
    public void forEachRemaining(Consumer<? super GraphEdge<V, E>> action) {
        Iterator.super.forEachRemaining(action);
    }
}
