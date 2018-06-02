package gr.james.influence.graph;

import gr.james.influence.algorithms.components.KosarajuComponents;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.exceptions.IllegalWeightException;
import gr.james.influence.util.RandomHelper;
import gr.james.sampling.EfraimidisSampling;
import gr.james.sampling.RandomSampling;
import gr.james.sampling.WeightedRandomSampling;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Graphs {
    private Graphs() {
    }

    public static boolean isWeightLegal(double weight) {
        return !Double.isInfinite(weight) && !Double.isNaN(weight);
    }

    public static double requireWeightLegal(double weight) {
        if (!isWeightLegal(weight)) {
            throw new IllegalWeightException();
        }
        return weight;
    }

    public static <V, E> void connect(DirectedGraph<V, E> g) {
        final Set<Set<V>> scc = new KosarajuComponents<>(g).components();
        final Iterator<Set<V>> it = scc.iterator();
        if (!it.hasNext()) {
            return;
        }
        Set<V> pre = it.next();
        Set<V> start = pre;
        if (!it.hasNext()) {
            return;
        }
        while (it.hasNext()) {
            Set<V> next = it.next();
            g.addEdge(pre.iterator().next(), next.iterator().next());
            pre = next;
        }
        g.addEdge(pre.iterator().next(), start.iterator().next());
        assert KosarajuComponents.execute(g).size() == 1;
    }

    public static <V, E> DirectedGraph<V, E> randomizeEdgeWeights(DirectedGraph<V, E> g, boolean unused) {
        final DirectedGraph<V, E> newGraph = new DirectedGraphImpl<>();
        for (V v : g) {
            newGraph.addVertex(v);
        }
        for (DirectedEdge<V, E> e : g.edges()) {
            newGraph.addEdge(e.source(), e.target(), e.value(), RandomHelper.getRandom().nextDouble());
        }
        return newGraph;
    }

    /**
     * Finds and returns any vertex in a graph that satisfies a condition.
     * <p>
     * If many vertices satisfy the condition, an arbitrary vertex is selected and returned. Returns {@code null} if no
     * vertex in the graph satisfies the condition.
     * <p>
     * Complexity: O(V)
     *
     * @param g         the graph
     * @param condition the condition to be satisfied
     * @param <V>       the vertex type
     * @return any vertex in a graph that satisfies {@code condition}, or {@code null} if no vertex in {@code g}
     * satisfies {@code condition}
     * @throws NullPointerException if {@code g} or {@code condition} is {@code null}
     */
    public static <V> V findVertex(DirectedGraph<V, ?> g, Predicate<V> condition) {
        for (V v : g) {
            if (condition.test(v)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Fuses two or more vertices into a single one. This operation is also known as contraction. This method may cause
     * information loss if there are conflicts on the edges.
     *
     * @param g              the graph to apply the fusion to
     * @param f              an array of vertices to be fused
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the vertex that is the result of the fusion
     * @see <a href="http://mathworld.wolfram.com/VertexContraction.html">VertexContraction @ mathworld.wolfram.com</a>
     */
    public static <V, E> V fuseVertices(DirectedGraph<V, E> g, Iterable<V> f, VertexProvider<V> vertexProvider) {
        V v = g.addVertex(vertexProvider);

        for (V y : f) {
            for (DirectedEdge<V, E> e : g.outEdges(y)) {
                g.addEdge(v, e.target(), e.value(), e.weight());
            }
            for (DirectedEdge<V, E> e : g.inEdges(y)) {
                g.addEdge(e.source(), v, e.value(), e.weight());
            }
            g.removeVertex(y);
        }

        return v;
    }

    /**
     * <p>Combine several graphs into a single one. When combining, the vertices of the input graphs will be inserted to
     * the output along with their edges. The original graphs will not be modified.</p>
     *
     * @param graphs the graph objects to combine
     * @param <V>    the vertex type
     * @param <E>    the edge type
     * @return the combined graph
     */
    public static <V, E> DirectedGraph<V, E> combineGraphs(Collection<? extends DirectedGraph<V, E>> graphs) {
        final DirectedGraph<V, E> r = new DirectedGraphImpl<>();
        for (DirectedGraph<V, E> g : graphs) {
            for (V v : g) {
                r.addVertex(v);
            }
            for (DirectedEdge<V, E> e : g.edges()) {
                r.addEdge(e.source(), e.target(), e.weight());
            }
        }
        return r;
    }

    public static <G extends DirectedGraph<V, E>, V, E> DirectedGraph<V, E> deepCopy(G g, Collection<V> filter) {
        final DirectedGraph<V, E> r = new DirectedGraphImpl<>();
        //final DirectedGraph<V, E> r = factory.createWeightedDirected();
        for (V v : filter) {
            if (!g.containsVertex(v)) {
                throw new IllegalVertexException();
            }
            r.addVertex(v);
        }
        for (DirectedEdge<V, E> e : g.edges()) {
            if (r.containsVertex(e.source()) && r.containsVertex(e.target())) {
                r.addEdge(e.source(), e.target(), e.value(), e.weight());
            }
        }
        return r;
    }

    public static <G extends DirectedGraph<V, E>, V, E> DirectedGraph<V, E> deepCopy(G g) {
        return deepCopy(g, g.vertexSet());
    }

    /*public static <V, E> DirectedGraph<V, E> deepCopy(DirectedGraph<V, E> g, GraphFactory<V, E> factory) {
        return deepCopy(g, factory, g.vertexSet());
    }

    public static <V, E> DirectedGraph<V, E> deepCopy(DirectedGraph<V, E> g) {
        return deepCopy(g, DirectedGraph.factory(), g.vertexSet());
    }

    public static <V, E> DirectedGraph<V, E> deepCopy(DirectedGraph<V, E> g, Collection<V> filter) {
        return deepCopy(g, DirectedGraph.factory(), filter);
    }

    public static SimpleGraph deepCopy(SimpleGraph g, Collection<Integer> filter) {
        return (SimpleGraph) deepCopy(g, SimpleGraph.factory, filter);
    }

    public static SimpleGraph deepCopy(SimpleGraph g) {
        return (SimpleGraph) deepCopy(g, SimpleGraph.factory, g.getVertices());
    }*/

    /**
     * Filters out and returns the stubborn vertices contained in {@code g}.
     * <p>
     * A stubborn vertex is one that its only outbound edge points to itself. By this definition, this method will not
     * return vertices that only point to other stubborn vertices. This method will also not detect stubborn components.
     * <p>
     * Complexity: O(V)
     *
     * @param g   the graph that the operation is to be performed
     * @param <V> the vertex type
     * @return an unmodifiable {@code Set} of all the stubborn vertices in {@code g}
     * @throws NullPointerException if {@code g} is null
     */
    public static <V> Set<V> getStubbornVertices(DirectedGraph<V, ?> g) {
        return Collections.unmodifiableSet(
                g.vertexSet().stream()
                        .filter(v -> g.outDegree(v) == 1 && g.adjacentOut(v).contains(v))
                        .collect(Collectors.toSet())
        );
    }

    /**
     * Returns a uniformly distributed random vertex of a graph using the provided {@code Random} instance.
     * <p>
     * Complexity: O(V)
     *
     * @param g      the {@code DirectedGraph}
     * @param random the {@code Random} instance to use
     * @param <V>    the vertex type
     * @return a uniformly distributed random vertex of {@code g}
     * @throws NullPointerException     if {@code g} or {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code g} is empty
     */
    public static <V> V getRandomVertex(DirectedGraph<V, ?> g, Random random) {
        if (g.vertexCount() < 1) {
            throw new IllegalArgumentException();
        }
        int r = random.nextInt(g.vertexCount());
        for (V v : g) {
            if (r-- == 0) {
                return v;
            }
        }
        assert false;
        return null;
    }

    /**
     * Returns a uniformly distributed random vertex of a graph using the global random instance.
     * <p>
     * Complexity: O(V)
     *
     * @param g   the {@code DirectedGraph}
     * @param <V> the vertex type
     * @return a uniformly distributed random vertex of {@code g}
     * @throws NullPointerException     if {@code g} or {@code random} is {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code g} is empty
     */
    public static <V> V getRandomVertex(DirectedGraph<V, ?> g) {
        return getRandomVertex(g, RandomHelper.getRandom());
    }

    /**
     * Returns the number of directed edges in {@code g}.
     * <p>
     * Complexity: O(V)
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the number of directed edges in {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> int getEdgesCount(DirectedGraph<V, ?> g) {
        int count = 0;
        for (V v : g.vertexSet()) {
            count += g.adjacentOut(v).size();
        }
        return count;
    }

    /**
     * Returns a random vertex that is adjacent to {@code v}.
     * <p>
     * This method returns {@code null} if {@code v} doesn't have any outbound edges.
     *
     * @param g   the graph
     * @param v   the vertex
     * @param <V> the vertex type
     * @return a random vertex that is adjacent to {@code v}
     * @throws NullPointerException   if {@code g} or {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in {@code g}
     * @see <a href="https://doi.org/10.1016/j.ipl.2005.11.003">doi:10.1016/j.ipl.2005.11.003</a>
     */
    public static <V> V getRandomOutVertex(DirectedGraph<V, ?> g, V v) {
        final RandomSampling<V> rs = new EfraimidisSampling<>(1, RandomHelper.getRandom());
        rs.feed(g.adjacentOut(v));
        final Collection<V> sample = rs.sample();
        if (sample.isEmpty()) {
            return null;
        }
        assert sample.size() == 1;
        return sample.iterator().next();
    }

    /**
     * Returns a random vertex that is adjacent to {@code v} with distribution corresponding to the edge weights.
     * <p>
     * This method returns {@code null} if {@code v} doesn't have any outbound edges.
     *
     * @param g   the graph
     * @param v   the vertex
     * @param <V> the vertex type
     * @return a random vertex that is adjacent to {@code v}
     * @throws NullPointerException   if {@code g} or {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in {@code g}
     * @throws RuntimeException       if the weight of any outgoind edge of {@code v} is not in {@code (0, +Inf)}
     *                                because such values are incompatible with the algorithm
     * @see <a href="https://doi.org/10.1016/j.ipl.2005.11.003">doi:10.1016/j.ipl.2005.11.003</a>
     */
    public static <V> V getWeightedRandomOutVertex(DirectedGraph<V, ?> g, V v) {
        final WeightedRandomSampling<V> wrs = new EfraimidisSampling<>(1, RandomHelper.getRandom());
        for (DirectedEdge<V, ?> e : g.outEdges(v)) {
            wrs.feed(e.target(), e.weight());
        }
        final Collection<V> sample = wrs.sample();
        if (sample.isEmpty()) {
            return null;
        }
        assert sample.size() == 1;
        return sample.iterator().next();
    }

    public static double getDensity(DirectedGraph<?, ?> g) {
        double n = g.vertexCount();
        double e = Graphs.getEdgesCount(g);
        return e / (n * (n - 1));
    }
}
