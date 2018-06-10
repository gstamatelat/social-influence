package gr.james.influence.algorithms.layout;

import com.google.common.math.IntMath;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Implementation of the algorithm based on BFS in <i>Graph-theoretic analysis of finite Markov chains (Jarvis and
 * Shier)</i> to find the period of a strongly connected graph.
 *
 * @param <V> the vertex type
 */
public class BreadthFirstSearchPeriodicity<V> {
    private final DirectedGraph<V, ?> g;
    private int period;

    /**
     * Constructs a new instance of {@link BreadthFirstSearchPeriodicity} and runs the algorithm.
     * <p>
     * Because of how the algorithm works, the constructor assumes that the graph is strongly connected and will perform
     * no checks to ensure that. The behavior of the algorithm when the input is not strongly connected is undefined.
     * <p>
     * This constructor will throw {@link IllegalArgumentException} if there is a hint suggesting that the graph is not
     * strongly connected. This behavior, however, is not guaranteed to take place. Exhaustive strong connectivity check
     * is not possible without damaging the running time.
     * <p>
     * The constructor runs in time O(V+E) in the worst case.
     *
     * @param g the strongly connected graph
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if there is a hint suggesting that {@code g} is not strongly connected
     */
    public BreadthFirstSearchPeriodicity(@UnmodifiableGraph DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
        this.period = 0;

        final Queue<V> queue = new LinkedList<>();
        final Map<V, Integer> distance = new HashMap<>();
        final V start = g.anyVertex();
        queue.offer(start);
        distance.put(start, 0);
        while (!queue.isEmpty() && period != 1) {
            final V next = queue.poll();
            for (V v : g.adjacentOut(next)) {
                if (distance.containsKey(v)) {
                    final int difference = distance.get(next) - distance.get(v) + 1;
                    assert difference >= 1;
                    period = IntMath.gcd(period, difference);
                } else {
                    distance.put(v, distance.get(next) + 1);
                    queue.offer(v);
                }
            }
        }

        if (this.period == 0) {
            throw new IllegalArgumentException("The graph did not contain any cycles; cannot be strongly connected");
        }
        if (period != 1 && distance.keySet().size() != g.vertexCount()) {
            throw new IllegalArgumentException("The BFS did not traverse the entire graph; cannot be strongly connected");
        }
    }

    /**
     * Returns the period of a strongly connected graph.
     * <p>
     * This is a convenience method that is equivalent to
     * <pre><code>
     * return new BreadthFirstSearchPeriodicity&lt;&gt;(g).period();
     * </code></pre>
     *
     * @param g   the strongly connected graph
     * @param <V> the vertex type
     * @return the period of {@code g}
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if there is a hint suggesting that {@code g} is not strongly connected
     * @see #BreadthFirstSearchPeriodicity(DirectedGraph)
     * @see #period()
     */
    public static <V> int period(DirectedGraph<V, ?> g) {
        return new BreadthFirstSearchPeriodicity<>(g).period();
    }

    /**
     * Returns a value indicating if a strongly connected graph is aperiodic.
     * <p>
     * This is a convenience method that is equivalent to
     * <pre><code>
     * return new BreadthFirstSearchPeriodicity&lt;&gt;(g).isAperiodic();
     * </code></pre>
     *
     * @param g   the strongly connected graph
     * @param <V> the vertex type
     * @return {@code true} if {@code g} is aperiodic, otherwise {@code false}
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if there is a hint suggesting that {@code g} is not strongly connected
     * @see #BreadthFirstSearchPeriodicity(DirectedGraph)
     * @see #isAperiodic()
     */
    public static <V> boolean isAperiodic(DirectedGraph<V, ?> g) {
        return new BreadthFirstSearchPeriodicity<>(g).isAperiodic();
    }

    /**
     * Returns the graph associated with this instance.
     *
     * @return the graph associated with this instance
     */
    public DirectedGraph<V, ?> getGraph() {
        return this.g;
    }

    /**
     * Returns the period of the graph.
     * <p>
     * The period is a positive integer greater or equal than 1.
     * <p>
     * This method runs in constant time.
     *
     * @return the period of the graph
     */
    public int period() {
        return this.period;
    }

    /**
     * Returns a value indicating whether the graph is aperiodic.
     * <p>
     * A strongly connected graph is aperiodic if and only if its period is 1. This, this method is equivalent to
     * <pre><code>
     * return period() == 1;
     * </code></pre>
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if the input graph is aperiodic, otherwise {@code false}
     */
    public boolean isAperiodic() {
        return this.period == 1;
    }
}
