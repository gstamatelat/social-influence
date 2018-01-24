package gr.james.influence.api.algorithms;

import gr.james.influence.algorithms.distance.DijkstraClosestFirstIterator;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents an algorithm that is able to traverse the vertices of a graph in a specific order.
 * <p>
 * Implementations of this interface need not traverse all the vertices in the graph. As an example,
 * {@link DijkstraClosestFirstIterator} only traverses through vertices that are reachable from a source vertex.
 * <p>
 * This interface is an extension to the {@link Iterator} interface.
 *
 * @param <V> the vertex type
 */
public interface VertexIterator<V> extends Iterator<V> {
    /**
     * Returns {@code true} if the iteration has more vertices, otherwise {@code false}.
     *
     * @return {@code true} if the iteration has more vertices, otherwise {@code false}
     */
    @Override
    boolean hasNext();

    /**
     * Returns the next vertex in the iteration.
     *
     * @return the next vertex in the iteration
     * @throws NoSuchElementException if the iteration has no more vertices
     */
    @Override
    V next();

    /**
     * Returns the {@link Graph} linked to this instance.
     * <p>
     * This method cannot return {@code null}.
     * <p>
     * This method runs in constant time.
     *
     * @return the {@link Graph} linked to this instance
     */
    Graph<V, ?> getGraph();

    /**
     * Runs this iterator until there are no more vertices.
     */
    default void exhaust() {
        while (hasNext()) {
            next();
        }
    }

    /**
     * Runs this iterator until there are no more vertices.
     * <p>
     * This method is identical to {@link #exhaust()} except that it also returns the vertices and, thus, is slower.
     *
     * @return a {@link List} of vertices in the order at which they have been returned by {@link #next()}
     */
    default List<V> exhaustVertices() {
        final List<V> vertices = new ArrayList<>();
        while (hasNext()) {
            vertices.add(next());
        }
        return vertices;
    }

    /**
     * Runs this iterator until there are no more vertices or until a vertex has been returned.
     *
     * @param until the vertex on which to stop the iteration
     * @throws NullPointerException   if {@code until} is {@code null}
     * @throws IllegalVertexException if {@code until} is not in the graph referenced by this instance
     */
    default void exhaust(V until) {
        Conditions.requireNonNullAndExists(until, getGraph());
        while (hasNext()) {
            if (next().equals(until)) {
                break;
            }
        }
    }

    /**
     * Runs this iterator until there are no more vertices or until a vertex has been returned.
     * <p>
     * This method is identical to {@link #exhaust(Object)} except that it also returns the vertices and, thus, is
     * slower.
     *
     * @param until the vertex on which to stop the iteration
     * @return a {@link List} of vertices in the order at which they have been returned by {@link #next()}
     * @throws NullPointerException   if {@code until} is {@code null}
     * @throws IllegalVertexException if {@code until} is not in the graph referenced by this instance
     */
    default List<V> exhaustVertices(V until) {
        Conditions.requireNonNullAndExists(until, getGraph());
        final List<V> vertices = new ArrayList<>();
        while (hasNext()) {
            final V next = next();
            vertices.add(next);
            if (next.equals(until)) {
                break;
            }
        }
        return vertices;
    }
}
