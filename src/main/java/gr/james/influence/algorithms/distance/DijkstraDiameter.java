package gr.james.influence.algorithms.distance;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.Graph;
import gr.james.influence.api.algorithms.GraphProperty;
import gr.james.influence.util.Conditions;

import java.util.List;

/**
 * Diameter calculation of a directed and weighted graph using the Dijkstra's algorithm.
 * <p>
 * This class uses constant state size.
 *
 * @param <V> the vertex type
 */
public class DijkstraDiameter<V> implements GraphProperty<Double> {
    private Double diameter;

    /**
     * Construct a new instance of {@link DijkstraDiameter} using the specified graph.
     * <p>
     * The constructor executes Dijkstra's algorithm for every vertex in {@code g} and calculates the maximum of the
     * respective shortest paths.
     *
     * @param g the input graph
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if {@code g} contains less than 2 vertices
     */
    public DijkstraDiameter(@UnmodifiableGraph Graph<V, ?> g) {
        Conditions.requireNonNull(g);
        Conditions.requireArgument(g.getVerticesCount() > 1);

        diameter = Double.NEGATIVE_INFINITY;

        for (V v : g) {
            final DijkstraClosestFirstIterator<V> dijkstra = new DijkstraClosestFirstIterator<>(g, v);
            final List<V> reachable = dijkstra.exhaust();
            final double dist = reachable.size() < g.getVerticesCount() ?
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
     * Get the diameter of the graph.
     * <p>
     * If the graph is not strongly connected, returns {@link Double#POSITIVE_INFINITY}.
     * <p>
     * This method runs in constant time.
     *
     * @return the diameter of the graph
     */
    @Override
    public Double get() {
        return diameter;
    }
}
