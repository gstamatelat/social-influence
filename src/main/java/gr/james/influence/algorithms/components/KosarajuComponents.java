package gr.james.influence.algorithms.components;

import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

import java.util.*;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm">Kosaraju's algorithm</a> to
 * find the strongly connected components of a directed graph.
 *
 * @param <V> the type of vertex
 */
public class KosarajuComponents<V> implements ConnectedComponents<V> {
    private final DirectedGraph<V, ?> g;
    private final int modCount;
    private final Set<Set<V>> components;

    /**
     * Construct a new instance from a given graph. Calling the constructor will perform the actual algorithm.
     *
     * @param g the graph with which to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public KosarajuComponents(DirectedGraph<V, ?> g) {
        this.g = g;
        this.modCount = g.modCount();
        this.components = executeAlgorithm(g);
    }

    /**
     * Returns the strongly connected components of the graph.
     * <p>
     * This is a convenience method equivalent to
     * <pre><code>
     * return new KosarajuComponents&lt;&gt;(g).components();
     * </code></pre>
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return an unmodifiable set of strongly connected components of {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     * @see #KosarajuComponents(DirectedGraph)
     * @see #components()
     */
    public static <V> Set<Set<V>> components(DirectedGraph<V, ?> g) {
        return new KosarajuComponents<>(g).components();
    }

    private static <V, E> Set<Set<V>> executeAlgorithm(DirectedGraph<V, E> g) {
        final Set<Set<V>> components = new HashSet<>();

        final Map<V, Iterator<V>> edgeIterator = new HashMap<>();
        for (V v : g) {
            edgeIterator.put(v, g.adjacentOut(v).iterator());
        }
        final GraphState<V, Boolean> visited = GraphState.create(g.vertexSet(), false);
        final Stack<V> dfsStack = new Stack<>();

        for (V start : g) {
            if (!visited.get(start)) {
                // Some variables
                final Stack<V> depthFirstOrder = new Stack<>();
                final LinkedHashSet<V> reverseDepthFirstOrder = new LinkedHashSet<>();

                // Get the depth first order
                assert dfsStack.empty();
                dfsStack.push(start);
                visited.put(start, true);
                while (!dfsStack.empty()) {
                    V v = dfsStack.peek();
                    if (edgeIterator.get(v).hasNext()) {
                        V w = edgeIterator.get(v).next();
                        if (!visited.get(w)) {
                            dfsStack.push(w);
                            visited.put(w, true);
                        }
                    } else {
                        depthFirstOrder.push(dfsStack.pop());
                    }
                }

                // reverse depth order
                while (!depthFirstOrder.empty()) {
                    reverseDepthFirstOrder.add(depthFirstOrder.pop());
                }

                // Perform reverse DFS for each vertex in depthFirstOrder
                while (!reverseDepthFirstOrder.isEmpty()) {
                    Set<V> component = new HashSet<>();
                    assert dfsStack.empty();
                    dfsStack.push(reverseDepthFirstOrder.iterator().next());
                    while (!dfsStack.empty()) {
                        V next = dfsStack.pop();
                        component.add(next);
                        reverseDepthFirstOrder.remove(next);
                        for (V w : g.adjacentIn(next)) {
                            if (reverseDepthFirstOrder.contains(w)) {
                                dfsStack.push(w);
                            }
                        }
                    }
                    reverseDepthFirstOrder.removeAll(component);
                    components.add(Collections.unmodifiableSet(component));
                }
            }
        }

        assert components.stream().mapToInt(Set::size).sum() == g.vertexCount();

        return Collections.unmodifiableSet(components);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ConcurrentModificationException {@inheritDoc}
     */
    @Override
    public Set<Set<V>> components() {
        Conditions.requireModCount(this.g, this.modCount);
        return this.components;
    }
}
