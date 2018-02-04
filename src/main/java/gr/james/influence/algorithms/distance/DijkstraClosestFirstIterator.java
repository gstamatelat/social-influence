package gr.james.influence.algorithms.distance;

import com.google.common.collect.Lists;
import gr.james.influence.algorithms.VertexIterator;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

import java.util.*;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap.
 * <p>
 * This class is implemented as an {@link Iterator} where the order of iteration is imposed by the (ascending) distance
 * from the specified source vertex. The iterator will only traverse through the elements that are reachable from the
 * source.
 * <p>
 * The size of the state of this class is {@code O(V)}.
 *
 * @param <V> the vertex type
 */
public class DijkstraClosestFirstIterator<V> implements VertexIterator<V> {
    private final Map<V, Double> distTo;
    private final Map<V, V> edgeTo;
    private final DirectedGraph<V, ?> g;
    private final PriorityQueue<V> pq;

    /**
     * Construct an instance of {@link DijkstraClosestFirstIterator} with a given {@link DirectedGraph} and a {@code source}.
     * <p>
     * The constructor will initialize the instance in time {@code O(V)}.
     *
     * @param g      the {@link DirectedGraph} in which to perform the algorithm
     * @param source the source vertex
     * @throws NullPointerException   if {@code g} or {@code source} is {@code null}
     * @throws IllegalVertexException if {@code source} is not in {@code g}
     */
    public DijkstraClosestFirstIterator(@UnmodifiableGraph DirectedGraph<V, ?> g, V source) {
        Conditions.requireVertexInGraph(g, source);

        this.g = g;
        this.distTo = new HashMap<>();
        this.edgeTo = new HashMap<>();

        for (V v : g) {
            this.distTo.put(v, Double.POSITIVE_INFINITY);
            this.edgeTo.put(v, null);
        }
        this.distTo.put(source, 0.0);

        pq = new PriorityQueue<>(Comparator.comparingDouble(distTo::get));

        pq.add(source);
    }

    /**
     * Returns {@code true} if the algorithm has more vertices to discover, that is if there are still vertices that are
     * unmarked. Otherwise returns {@code false}.
     * <p>
     * This method runs in constant time.
     *
     * @return {@code true} if the algorithm has more vertices to discover, otherwise {@code false}
     */
    @Override
    public boolean hasNext() {
        return !pq.isEmpty();
    }

    /**
     * Returns the next closest vertex to the source.
     *
     * @return the next closest vertex to the source
     * @throws NoSuchElementException if there are no more vertices to be marked
     */
    @Override
    public V next() {
        final V u = pq.poll();

        if (u == null) {
            throw new NoSuchElementException();
        }

        for (DirectedEdge<V, ?> e : g.outEdges(u)) {
            final V to = e.target();
            final double distanceThroughU = distTo.get(u) + e.weight();
            if (distanceThroughU < distTo.get(to)) {
                pq.remove(to);
                this.distTo.put(to, distanceThroughU);
                this.edgeTo.put(to, u);
                pq.add(to);
            }
        }

        return u;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectedGraph<V, ?> getGraph() {
        return g;
    }

    /**
     * Get the distance of the shortest path to a target vertex.
     * <p>
     * If there is no such path, this method will return {@link Double#POSITIVE_INFINITY}. Additionally, this method may
     * also return {@link Double#POSITIVE_INFINITY} if the algorithm did not previously complete (if the iterator is not
     * exhausted) even if there is a path from {@code start} to {@code v}.
     * <p>
     * This method runs in constant time.
     *
     * @param v the vertex to get the distance to
     * @return the distance of the shortest path to {@code v}
     * @throws NullPointerException   if {@code v} is null
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    public double distanceTo(V v) {
        Conditions.requireNonNull(v);
        final Double distance = distTo.get(v);
        if (distance == null) {
            throw new IllegalVertexException();
        }
        return distance;
    }

    /**
     * Get the shortest path to a target vertex as a sequence of edges.
     * <p>
     * If there is no path to the target or if the target specified is the source vertex associated with this instance
     * this method will return an empty sequence. Additionally, this method may also return an empty sequence if the
     * algorithm did not previously complete (if the iterator is not exhausted) even if there is a path from
     * {@code start} to {@code v}.
     * <p>
     * This method runs in time proportional to the shortest path length.
     *
     * @param v the vertex to get the shortest route to
     * @return an unmodifiable {@link List} of {@link DirectedEdge} representing the shortest route to {@code v}
     * @throws NullPointerException   if {@code v} is null
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    public List<DirectedEdge<V, ?>> pathTo(V v) {
        Conditions.requireNonNull(v);
        if (!edgeTo.containsKey(v)) {
            throw new IllegalVertexException();
        }
        final List<DirectedEdge<V, ?>> route = new ArrayList<>();
        while (edgeTo.get(v) != null) {
            route.add(g.findEdge(edgeTo.get(v), v));
            v = edgeTo.get(v);
        }
        return Collections.unmodifiableList(Lists.reverse(route));
    }
}
