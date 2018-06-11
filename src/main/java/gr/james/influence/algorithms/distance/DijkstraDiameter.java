package gr.james.influence.algorithms.distance;

import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Diameter calculation of a directed and weighted graph using the Dijkstra's algorithm.
 * <p>
 * The implemented algorithm executes Dijkstra's algorithm for every vertex in {@code g} and calculates the maximum of
 * the respective shortest paths.
 * <p>
 * This class uses constant state size.
 *
 * @param <V> the vertex type
 */
public class DijkstraDiameter<V> {
    private final DirectedGraph<V, ?> g;
    private final int modCount;
    private double diameter;

    /**
     * Construct a new instance of {@link DijkstraDiameter} using the specified graph and execute the algorithm.
     *
     * @param g the input graph
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if {@code g} contains less than 2 vertices
     */
    public DijkstraDiameter(DirectedGraph<V, ?> g) {
        Conditions.requireNonNull(g);
        Conditions.requireArgument(g.vertexCount() > 1);

        this.g = g;
        this.modCount = g.modCount();
        this.diameter = Double.NEGATIVE_INFINITY;

        for (V v : g) {
            final DijkstraClosestFirstIterator<V> dijkstra = new DijkstraClosestFirstIterator<>(g, v);
            final List<V> reachable = dijkstra.exhaustVertices();
            final double dist = reachable.size() < g.vertexCount() ?
                    Double.POSITIVE_INFINITY :
                    dijkstra.distanceTo(reachable.get(reachable.size() - 1));
            if (dist > diameter) {
                diameter = dist;
            }
            if (Double.isInfinite(diameter)) {
                break;
            }
        }

        assert diameter > 0;
    }

    /**
     * Returns the diameter of a directed graph.
     * <p>
     * This is a convenience method equivalent to
     * <pre><code>
     * return new DijkstraDiameter&lt;&gt;(g).diameter();
     * </code></pre>
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the diameter of {@code g}
     * @see #DijkstraDiameter(DirectedGraph)
     * @see #diameter()
     */
    public static <V> double diameter(DirectedGraph<V, ?> g) {
        return new DijkstraDiameter<>(g).diameter();
    }

    /**
     * Returns the diameter of the graph.
     * <p>
     * Returns {@link Double#POSITIVE_INFINITY} if the graph is not strongly connected.
     * <p>
     * This method runs in constant time.
     *
     * @return the diameter of the graph
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public double diameter() {
        Conditions.requireModCount(this.g, this.modCount);
        return this.diameter;
    }

    /**
     * Returns a value indicating whether the graph is strongly connected.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * return Double.isFinite(diameter());
     * </code></pre>
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if the graph is strongly connected, otherwise {@code false}
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public boolean isStronglyConnected() {
        Conditions.requireModCount(this.g, this.modCount);
        return Double.isFinite(this.diameter);
    }
}
