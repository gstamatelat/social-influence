package gr.james.influence.algorithms.distance;

import com.google.common.collect.Lists;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.exceptions.InvalidVertexException;

import java.util.*;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap. This class is implemented as an
 * {@link Iterator} where the order of iteration is imposed by the (ascending) distance from the specified source
 * vertex. Instances of this class expect that the graph will not be mutated during iteration.
 *
 * @param <V> the vertex type
 */
public class DijkstraClosestFirstIterator<V> implements Iterator<V> {
    private final Map<V, Double> distTo;
    private final Map<V, V> edgeTo;
    private final Graph<V, ?> g;
    private final PriorityQueue<V> pq;

    /**
     * Construct an instance of {@link DijkstraClosestFirstIterator} with a given {@link Graph} and a {@code source}.
     * The constructor will initialize the instance in time proportional to the number of vertices in the graph.
     *
     * @param g      the {@link Graph} in which to perform the algorithm
     * @param source the source vertex
     * @throws NullPointerException   if {@code g} or {@code source} is {@code null}
     * @throws InvalidVertexException if {@code source} is not in {@code g}
     */
    public DijkstraClosestFirstIterator(Graph<V, ?> g, V source) {
        Conditions.requireAllNonNull(g, source);
        if (!g.containsVertex(source)) {
            throw new InvalidVertexException();
        }

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
     * unmarked. Otherwise returns {@code false}. This method runs in constant time.
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

        return u;
    }

    /**
     * Runs this iterator until there are no more elements.
     */
    public void exhaust() {
        while (hasNext()) {
            next();
        }
    }

    /**
     * Runs this iterator until there are no more elements or until a vertex has been marked.
     *
     * @param until the vertex on which to stop the iteration
     * @throws NullPointerException   if {@code until} is {@code null}
     * @throws InvalidVertexException if {@code until} is not in {@code g}
     */
    public void exhaust(V until) {
        Conditions.requireNonNullAndExists(until, g);
        while (hasNext()) {
            if (next().equals(until)) {
                break;
            }
        }
    }

    /**
     * Get the distance of the shortest path to a target vertex. If there is no such path, this method will return
     * {@link Double#POSITIVE_INFINITY}. Additionally, this method may also return {@link Double#POSITIVE_INFINITY}
     * if the algorithm did not previously complete (if the iterator is not exhausted) even if there is a path from
     * {@code start} to {@code v}. This method runs in constant time.
     *
     * @param v the vertex to get the distance to
     * @return the distance of the shortest path to {@code v}
     * @throws NullPointerException   if {@code v} is null
     * @throws InvalidVertexException if {@code v} is not in the graph
     */
    public double distanceTo(V v) {
        Conditions.requireNonNull(v);
        final Double distance = distTo.get(v);
        if (distance == null) {
            throw new InvalidVertexException();
        }
        return distance;
    }

    /**
     * Get the shortest path to a target vertex as a sequence of edges. If there is no path to the target or if the
     * target specified is the source vertex associated with this instance this method will return an empty sequence.
     * Additionally, this method may also return an empty sequence if the algorithm did not previously complete (if the
     * iterator is not exhausted) even if there is a path from {@code start} to {@code v}. This method runs in time
     * proportional to the shortest path length.
     *
     * @param v the vertex to get the shortest route to
     * @return an unmodifiable {@link List} of {@link GraphEdge} representing the shortest route to {@code v}
     * @throws NullPointerException   if {@code v} is null
     * @throws InvalidVertexException if {@code v} is not in the graph
     */
    public List<GraphEdge<V, ?>> pathTo(V v) {
        Conditions.requireNonNull(v);
        if (!edgeTo.containsKey(v)) {
            throw new InvalidVertexException();
        }
        final List<GraphEdge<V, ?>> route = new ArrayList<>();
        while (edgeTo.get(v) != null) {
            route.add(g.findEdge(edgeTo.get(v), v));
            v = edgeTo.get(v);
        }
        return Collections.unmodifiableList(Lists.reverse(route));
    }
}