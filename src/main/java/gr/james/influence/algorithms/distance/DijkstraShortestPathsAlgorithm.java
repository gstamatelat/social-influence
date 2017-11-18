package gr.james.influence.algorithms.distance;

import com.google.common.collect.Lists;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.Conditions;

import java.util.*;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap. Instances of this class expect that the
 * graph will not be mutated after the constructor is invoked.
 *
 * @param <V> the vertex type
 */
@Deprecated
public class DijkstraShortestPathsAlgorithm<V> {
    private final Map<V, Double> distTo;
    private final Map<V, V> edgeTo;
    private final Graph<V, ?> g;

    /**
     * Construct an instance of {@link DijkstraShortestPathsAlgorithm} with a given {@link Graph}, a {@code source} and
     * optionally a {@code target}. The constructor will execute the algorithm until the {@code target} vertex has been
     * marked and will then stop. If {@code target} is {@code null} the algorithm will be executed until all vertices
     * have been marked.
     *
     * @param g      the {@link Graph} in which to perform the algorithm
     * @param source the source vertex
     * @param target the target vertex (can be  {@code null})
     * @throws NullPointerException     if {@code g} or {@code source} is {@code null}
     * @throws IllegalArgumentException if {@code source} is not in {@code g}
     * @throws IllegalArgumentException if {@code target} is not {@code null} and not in {@code g}
     */
    public DijkstraShortestPathsAlgorithm(Graph<V, ?> g, V source, V target) {
        Conditions.requireAllNonNull(g, source);
        if (target != null && !g.containsVertex(target)) {
            throw new IllegalArgumentException();
        }

        this.g = g;
        this.distTo = new HashMap<>();
        this.edgeTo = new HashMap<>();

        for (V v : g) {
            this.distTo.put(v, Double.POSITIVE_INFINITY);
            this.edgeTo.put(v, null);
        }
        this.distTo.put(source, 0.0);

        final PriorityQueue<V> pq = new PriorityQueue<>(Comparator.comparingDouble(distTo::get));

        pq.add(source);

        while (!pq.isEmpty()) {
            final V u = pq.poll();

            if (target != null && u.equals(target)) {
                return;
            }

            for (GraphEdge<V, ?> e : g.getOutEdges(u).values()) {
                final V to = e.getTarget();
                final double distanceThroughU = distTo.get(u) + e.getWeight();
                if (distanceThroughU < distTo.get(to)) {
                    pq.remove(to);
                    this.distTo.put(to, distanceThroughU);
                    this.edgeTo.put(to, u);
                    pq.add(to);
                }
            }
        }
    }

    /**
     * Get the distance of the shortest path to a target vertex. If there is no such path, this method will return
     * {@link Double#POSITIVE_INFINITY}. Additionally, this method may also return {@link Double#POSITIVE_INFINITY}
     * if the algorithm did not previously complete (if the constructor argument {@code target} was not {@code null})
     * even if there is a path from {@code start} to {@code v}. This method runs in constant time.
     *
     * @param v the vertex to get the distance to
     * @return the distance of the shortest path to {@code v}
     * @throws NullPointerException     if {@code v} is null
     * @throws IllegalArgumentException if {@code v} is not in the graph
     */
    public double distanceTo(V v) {
        Conditions.requireNonNull(v);
        final Double distance = distTo.get(v);
        if (distance == null) {
            throw new IllegalArgumentException();
        }
        return distance;
    }

    /**
     * Get the shortest path to a target vertex as a sequence of edges. If there is no path to the target or if the
     * target specified is the source vertex associated with this instance this method will return an empty sequence.
     * Additionally, this method may also return an empty sequence if the algorithm did not previously complete (if the
     * constructor argument {@code target} was not {@code null}) even if there is a path from {@code start} to
     * {@code v}. This method runs in time proportional to the shortest path length.
     *
     * @param v the vertex to get the shortest route to
     * @return an unmodifiable {@link List} of {@link GraphEdge} representing the shortest route to {@code v}
     * @throws NullPointerException     if {@code v} is null
     * @throws IllegalArgumentException if {@code v} is not in the graph
     */
    public List<GraphEdge<V, ?>> pathTo(V v) {
        Conditions.requireNonNull(v);
        if (!edgeTo.containsKey(v)) {
            throw new IllegalArgumentException();
        }
        final List<GraphEdge<V, ?>> route = new ArrayList<>();
        while (edgeTo.get(v) != null) {
            route.add(g.findEdge(edgeTo.get(v), v));
            v = edgeTo.get(v);
        }
        return Collections.unmodifiableList(Lists.reverse(route));
    }
}
