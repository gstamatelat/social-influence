package gr.james.influence.algorithms.layout;

import gr.james.influence.api.Graph;
import gr.james.influence.util.collections.GraphState;

import java.util.*;

/**
 * <p>Implementation of the <a href="https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm">Kosaraju's algorithm</a> to
 * find strongly connected components. This implementation uses an iterative approach and is not prone to
 * StackOverflowException.</p>
 *
 * @see TarjanComponents
 */
public class KosarajuComponents { // TODO: Implement ConnectedComponents
    public static <V, E> Set<Set<V>> execute(Graph<V, E> g) {

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
                    components.add(component);
                }
            }
        }

        assert components.stream().mapToInt(Set::size).sum() == g.getVerticesCount();

        return components;
    }
}
