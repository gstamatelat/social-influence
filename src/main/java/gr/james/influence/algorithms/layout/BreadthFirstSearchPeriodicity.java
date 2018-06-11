package gr.james.influence.algorithms.layout;

import com.google.common.math.IntMath;
import gr.james.influence.algorithms.components.ConnectedComponents;
import gr.james.influence.algorithms.components.KosarajuComponents;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Conditions;

import java.math.BigInteger;
import java.util.*;

/**
 * Implementation of the algorithm based on BFS in <i>Graph-theoretic analysis of finite Markov chains (Jarvis and
 * Shier)</i> to find the period of a directed graph.
 * <p>
 * Because the above algorithm only works for strongly connected graphs, this class also uses the theorem in <i>Social
 * and Economic Networks (Jackson)</i> which states that a graph is convergent if and only if every set of nodes that is
 * strongly connected and closed is aperiodic.
 * <p>
 * The period of the directed graph is calculated as the LCM of the periods of the strongly connected and closed
 * components.
 *
 * @param <V> the vertex type
 */
public class BreadthFirstSearchPeriodicity<V> {
    private final DirectedGraph<V, ?> g;
    private final ConnectedComponents<V> components;
    private final int modCount;
    private BigInteger period;

    /**
     * Constructs a new instance of {@link BreadthFirstSearchPeriodicity} and runs the algorithm.
     * <p>
     * The constructor runs in time O(V+E) in the worst case.
     *
     * @param g the graph
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public BreadthFirstSearchPeriodicity(DirectedGraph<V, ?> g) {
        this.g = g;
        this.components = new KosarajuComponents<>(g);
        this.modCount = g.modCount();
        this.period = BigInteger.ZERO;

        for (V v : g) {
            if (g.adjacentOut(v).size() == 0) {
                return;
            }
        }

        for (Set<V> c : components.components()) {
            if (Graphs.isClosedComponent(g, c)) {
                final int partPeriod = strongPeriod(g.subGraph(c));
                this.period = lcm(this.period, partPeriod);
            }
        }

        assert this.period.signum() == 1;
    }

    private static BigInteger lcm(BigInteger a, int b) {
        assert b >= 1;
        final BigInteger bBigInteger = BigInteger.valueOf(b);
        if (a.signum() == 0) {
            return bBigInteger;
        }
        return a.multiply(bBigInteger).divide(a.gcd(bBigInteger));
    }

    private static <V> int strongPeriod(DirectedGraph<V, ?> g) {
        int period = 0;
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
                    assert difference >= 0;
                    period = IntMath.gcd(period, difference);
                } else {
                    distance.put(v, distance.get(next) + 1);
                    queue.offer(v);
                }
            }
        }
        assert period >= 1;
        assert period <= 1 || distance.keySet().size() == g.vertexCount();
        return period;
    }

    /**
     * Returns the period of a directed graph.
     * <p>
     * This is a convenience method that is equivalent to
     * <pre><code>
     * return new BreadthFirstSearchPeriodicity&lt;&gt;(g).period();
     * </code></pre>
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the period of {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     * @see #BreadthFirstSearchPeriodicity(DirectedGraph)
     * @see #period()
     */
    public static <V> BigInteger period(DirectedGraph<V, ?> g) {
        return new BreadthFirstSearchPeriodicity<>(g).period();
    }

    /**
     * Returns a value indicating if a directed graph is aperiodic.
     * <p>
     * This is a convenience method that is equivalent to
     * <pre><code>
     * return new BreadthFirstSearchPeriodicity&lt;&gt;(g).isAperiodic();
     * </code></pre>
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return {@code true} if {@code g} is aperiodic, otherwise {@code false}
     * @throws NullPointerException if {@code g} is {@code null}
     * @see #BreadthFirstSearchPeriodicity(DirectedGraph)
     * @see #isAperiodic()
     */
    public static <V> boolean isAperiodic(DirectedGraph<V, ?> g) {
        return new BreadthFirstSearchPeriodicity<>(g).isAperiodic();
    }

    /**
     * Returns the graph associated with this instance.
     * <p>
     * This method runs in constant time.
     *
     * @return the graph associated with this instance
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public DirectedGraph<V, ?> getGraph() {
        Conditions.requireModCount(this.g, modCount);
        return this.g;
    }

    /**
     * Returns the strongly connected components of the graph as an instance of {@link ConnectedComponents}.
     * <p>
     * This method runs in constant time.
     *
     * @return the strongly connected components of the graph as an instance of {@link ConnectedComponents}
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public ConnectedComponents<V> components() {
        Conditions.requireModCount(this.g, modCount);
        return this.components;
    }

    /**
     * Returns the period of the graph.
     * <p>
     * The period is a positive integer greater or equal than 1 if the graph is a Markov chain, otherwise it is 0.
     * <p>
     * This method runs in constant time.
     *
     * @return the period of the graph
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public BigInteger period() {
        Conditions.requireModCount(this.g, modCount);
        return this.period;
    }

    /**
     * Returns a value indicating whether the graph is aperiodic.
     * <p>
     * A graph is aperiodic if and only if it represents a Markov chain and its period is 1. Thus, this method is
     * equivalent to
     * <pre><code>
     * return period().equals(BigInteger.ONE);
     * </code></pre>
     * <p>
     * Equivalently, if a graph is aperiodic, then the Markov chain it represents will converge.
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if the input graph is aperiodic, otherwise {@code false}
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public boolean isAperiodic() {
        Conditions.requireModCount(this.g, modCount);
        return this.period.equals(BigInteger.ONE);
    }

    /**
     * Returns a value indicating whether the graph represents a Markov chain.
     * <p>
     * A directed graph represents a Markov chain if every vertex has at least one outgoing edge.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return period().signum() &gt; 0;
     * </code></pre>
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if the graph represents a Markov chain, otherwise {@code false}
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public boolean isMarkovChain() {
        Conditions.requireModCount(this.g, modCount);
        return this.period.signum() > 0;
    }
}
