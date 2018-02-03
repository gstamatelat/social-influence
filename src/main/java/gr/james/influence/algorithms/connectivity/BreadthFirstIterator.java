package gr.james.influence.algorithms.connectivity;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.algorithms.VertexIterator;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

import java.util.*;

/**
 * Implementation of breadth first search (BFS) as {@link Iterator}.
 * <p>
 * The size of the state of this class is {@code O(V)}.
 *
 * @param <V> the vertex type
 */
public class BreadthFirstIterator<V> implements VertexIterator<V> {
    private final DirectedGraph<V, ?> g;
    private final Queue<V> queue;
    private final Set<V> visited;

    /**
     * Construct a new {@link BreadthFirstIterator} from a given graph and a source vertex.
     *
     * @param g      the graph to perform BFS in
     * @param source the source vertex
     * @throws NullPointerException   if {@code g} or {@code source} is {@code null}
     * @throws IllegalVertexException if {@code source} is not in {@code g}
     */
    public BreadthFirstIterator(@UnmodifiableGraph DirectedGraph<V, ?> g, V source) {
        Conditions.requireVertexInGraph(g, source);

        this.g = g;
        this.queue = new LinkedList<>();
        this.visited = new HashSet<>();

        this.queue.offer(source);
        this.visited.add(source);
    }

    /**
     * Returns {@code true} if BFS has more vertices to discover, otherwise {@code false}.
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if BFS has more vertices to discover, otherwise {@code false}
     */
    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    /**
     * Returns the next vertex imposed by the BFS order.
     * <p>
     * This method runs in time proportional to the out degree of the next vertex.
     *
     * @return the next vertex
     * @throws NoSuchElementException if there are no more vertices to be discovered
     */
    @Override
    public V next() {
        final V next = queue.poll();

        if (next == null) {
            throw new NoSuchElementException();
        }

        for (V v : g.adjacentOut(next)) {
            if (visited.add(v)) {
                queue.offer(v);
            }
        }

        return next;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectedGraph<V, ?> getGraph() {
        return g;
    }
}
