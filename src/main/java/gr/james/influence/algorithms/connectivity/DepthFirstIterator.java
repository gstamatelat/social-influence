package gr.james.influence.algorithms.connectivity;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.algorithms.VertexIterator;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.Graph;
import gr.james.influence.util.Conditions;

import java.util.*;

/**
 * Implementation of depth first search (DFS) as {@link Iterator}.
 * <p>
 * The size of the state of this class is {@code O(V)}.
 *
 * @param <V> the vertex type
 */
public class DepthFirstIterator<V> implements VertexIterator<V> {
    private final Graph<V, ?> g;
    private final Deque<V> stack;
    private final Set<V> visited;

    /**
     * Construct a new {@link DepthFirstIterator} from a given graph and a source vertex.
     *
     * @param g      the graph to perform DFS in
     * @param source the source vertex
     * @throws NullPointerException   if {@code g} or {@code source} is {@code null}
     * @throws IllegalVertexException if {@code source} is not in {@code g}
     */
    public DepthFirstIterator(@UnmodifiableGraph Graph<V, ?> g, V source) {
        Conditions.requireVertexInGraph(g, source);

        this.g = g;
        this.stack = new LinkedList<>();
        this.visited = new HashSet<>();

        this.stack.offer(source);
        this.visited.add(source);
    }

    /**
     * Returns {@code true} if DFS has more vertices to discover, otherwise {@code false}.
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if DFS has more vertices to discover, otherwise {@code false}
     */
    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * Returns the next vertex imposed by the DFS order.
     * <p>
     * This method runs in time proportional to the out degree of the next vertex.
     *
     * @return the next vertex
     * @throws NoSuchElementException if there are no more vertices to be discovered
     */
    @Override
    public V next() {
        final V next = stack.pop();

        if (next == null) {
            throw new NoSuchElementException();
        }

        for (V v : g.getOutEdges(next).keySet()) {
            if (visited.add(v)) {
                stack.push(v);
            }
        }

        return next;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph<V, ?> getGraph() {
        return g;
    }
}
