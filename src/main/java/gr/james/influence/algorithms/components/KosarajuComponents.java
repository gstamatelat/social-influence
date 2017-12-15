package gr.james.influence.algorithms.components;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.Graph;
import gr.james.influence.api.algorithms.ConnectedComponents;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

import java.util.*;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm">Kosaraju's algorithm</a> to
 * find the strongly connected components of a directed graph. This implementation uses an iterative approach and is not
 * prone to {@link StackOverflowError}.
 *
 * @param <V> the type of vertex
 * @see TarjanComponents
 */
public class KosarajuComponents<V> implements ConnectedComponents<V> {
    private final Set<Set<V>> components;

    /**
     * Construct a new instance from a given graph. Calling the constructor will perform the actual algorithm.
     *
     * @param g the graph with which to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public KosarajuComponents(@UnmodifiableGraph Graph<V, ?> g) {
        Conditions.requireNonNull(g);
        this.components = executeAlgorithm(g);
    }

    /**
     * Convenience function to calculate the strongly connected components of a graph. This method will instantiate
     * {@link KosarajuComponents} with the given graph {@code g} and invoke the {@link #components()} method.
     *
     * @param g   the graph of which to get the components
     * @param <V> the type of vertex
     * @return the strongly connected components of {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> Set<Set<V>> execute(Graph<V, ?> g) {
        return new KosarajuComponents<>(g).components();
    }

    private static <V, E> Set<Set<V>> executeAlgorithm(Graph<V, E> g) {
        final Set<Set<V>> components = new HashSet<>();

        final Map<V, Iterator<V>> edgeIterator = new HashMap<>();
        for (V v : g) {
            edgeIterator.put(v, g.getOutEdges(v).keySet().iterator());
        }
        final GraphState<V, Boolean> visited = GraphState.create(g.getVertices(), false);
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
                        for (V w : g.getInEdges(next).keySet()) {
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

        assert components.stream().mapToInt(Set::size).sum() == g.getVerticesCount();

        return Collections.unmodifiableSet(components);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method runs in constant time.
     */
    @Override
    public Set<Set<V>> components() {
        return this.components;
    }
}
