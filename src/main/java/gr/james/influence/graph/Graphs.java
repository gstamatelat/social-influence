package gr.james.influence.graph;

import gr.james.influence.algorithms.components.KosarajuComponents;
import gr.james.influence.algorithms.layout.BreadthFirstSearchPeriodicity;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.RandomHelper;
import gr.james.sampling.EfraimidisSampling;
import gr.james.sampling.RandomSampling;
import gr.james.sampling.WatermanSampling;
import gr.james.sampling.WeightedRandomSampling;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Helper static utilities for graphs.
 */
public final class Graphs {
    private Graphs() {
    }

    /*public static boolean isWeightLegal(double weight) {
        return !Double.isInfinite(weight) && !Double.isNaN(weight);
    }

    public static double requireWeightLegal(double weight) {
        if (!isWeightLegal(weight)) {
            throw new IllegalWeightException();
        }
        return weight;
    }*/

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
        assert KosarajuComponents.components(g).size() == 1;
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
     * @return any vertex in {@code g} that satisfies {@code condition}, or {@code null} if no vertex in {@code g}
     * satisfies {@code condition}
     * @throws NullPointerException if {@code g} or {@code condition} is {@code null}
     * @see #findRandomVertex(Graph, Predicate)
     */
    public static <V> V findVertex(Graph<V, ?> g, Predicate<V> condition) {
        for (V v : g) {
            if (condition.test(v)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Finds and returns a random vertex in a graph that satisfies a condition using the provided {@link Random}
     * instance.
     * <p>
     * If many vertices satisfy the condition, a uniformly random vertex is selected and returned. Returns {@code null}
     * if no vertex in the graph satisfies the condition.
     * <p>
     * Complexity: O(V)
     *
     * @param g         the graph
     * @param condition the condition to be satisfied
     * @param random    the {@link Random} instance to use
     * @param <V>       the vertex type
     * @return a uniformly random vertex in {@code g} that satisfies {@code condition}, or {@code null} if no vertex in
     * {@code g} satisfies {@code condition}
     * @throws NullPointerException if {@code g} or {@code condition} is {@code null}
     * @see #findVertex(Graph, Predicate)
     */
    public static <V> V findRandomVertex(Graph<V, ?> g, Predicate<V> condition, Random random) {
        final WatermanSampling<V> sampling = new WatermanSampling<>(1, random);
        for (V v : g) {
            if (condition.test(v)) {
                sampling.feed(v);
            }
        }
        final Collection<V> sample = sampling.sample();
        if (sample.size() == 0) {
            return null;
        } else {
            return sample.iterator().next();
        }
    }

    /**
     * Finds and returns a random vertex in a graph that satisfies a condition using the global random instance.
     * <p>
     * If many vertices satisfy the condition, a uniformly random vertex is selected and returned. Returns {@code null}
     * if no vertex in the graph satisfies the condition.
     * <p>
     * Complexity: O(V)
     *
     * @param g         the graph
     * @param condition the condition to be satisfied
     * @param <V>       the vertex type
     * @return a uniformly random vertex in {@code g} that satisfies {@code condition}, or {@code null} if no vertex in
     * {@code g} satisfies {@code condition}
     * @throws NullPointerException if {@code g} or {@code condition} is {@code null}
     * @see #findVertex(Graph, Predicate)
     */
    public static <V> V findRandomVertex(Graph<V, ?> g, Predicate<V> condition) {
        return findRandomVertex(g, condition, RandomHelper.getRandom());
    }

    /**
     * Returns a uniformly distributed random vertex of a graph using the provided {@code Random} instance.
     * <p>
     * Complexity: O(V)
     *
     * @param g      the graph
     * @param random the {@code Random} instance to use
     * @param <V>    the vertex type
     * @return a uniformly distributed random vertex of {@code g}
     * @throws NullPointerException     if {@code g} or {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code g} is empty
     */
    public static <V> V getRandomVertex(Graph<V, ?> g, Random random) {
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
     * @param g   the graph
     * @param <V> the vertex type
     * @return a uniformly distributed random vertex of {@code g}
     * @throws NullPointerException     if {@code g} or {@code random} is {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code g} is empty
     */
    public static <V> V getRandomVertex(Graph<V, ?> g) {
        return getRandomVertex(g, RandomHelper.getRandom());
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

    /**
     * Checks two {@link DirectedGraph} for equality.
     * <p>
     * Two directed graphs are considered equal if their vertex sets and their edge sets are equal.
     *
     * @param g1  one graph
     * @param g2  the other graph
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return {@code true} if {@code g1} and {@code g2} are equal, otherwise {@code false}
     */
    public static <V, E> boolean equals(DirectedGraph<V, E> g1, DirectedGraph<V, E> g2) {
        if (g1 == g2) {
            return true;
        }
        if (g1 == null || g2 == null) {
            return false;
        }
        if (!g1.vertexSet().equals(g2.vertexSet())) {
            return false;
        }
        for (V v : g1) {
            if (!g1.outEdges(v).equals(g2.outEdges(v))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks two {@link UndirectedGraph} for equality.
     * <p>
     * Two undirected graphs are considered equal if their vertex sets and their edge sets are equal.
     *
     * @param g1  one graph
     * @param g2  the other graph
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return {@code true} if {@code g1} and {@code g2} are equal, otherwise {@code false}
     */
    public static <V, E> boolean equals(UndirectedGraph<V, E> g1, UndirectedGraph<V, E> g2) {
        if (g1 == g2) {
            return true;
        }
        if (g1 == null || g2 == null) {
            return false;
        }
        if (!g1.vertexSet().equals(g2.vertexSet())) {
            return false;
        }
        for (V v : g1) {
            if (!g1.edges(v).equals(g2.edges(v))) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
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

    @Deprecated
    public static <G extends DirectedGraph<V, E>, V, E> DirectedGraph<V, E> deepCopy(G g) {
        return deepCopy(g, g.vertexSet());
    }

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
     * @see #getStubbornComponents(DirectedGraph)
     */
    public static <V> Set<V> getStubbornVertices(DirectedGraph<V, ?> g) {
        return Collections.unmodifiableSet(
                g.vertexSet().stream()
                        .filter(v -> g.outDegree(v) == 1 && g.adjacentOut(v).contains(v))
                        .collect(Collectors.toSet())
        );
    }

    /**
     * Returns the stubborn components contained in {@code g}.
     * <p>
     * A stubborn component is a (maximal) strongly connected component that is also closed.
     * <p>
     * A directed graph always contains at least one stubborn component, unless the graph is empty.
     *
     * @param g   the graph that the operation is to be performed
     * @param <V> the vertex type
     * @return an unmodifiable {@code Set} of all the stubborn components in {@code g}
     * @throws NullPointerException if {@code g} is null
     * @see #getStubbornVertices(DirectedGraph)
     */
    public static <V> Set<Set<V>> getStubbornComponents(DirectedGraph<V, ?> g) {
        final Set<Set<V>> scc = KosarajuComponents.components(g);
        final Set<Set<V>> stubbornComponents = new HashSet<>();
        for (Set<V> c : scc) {
            if (isClosedComponent(g, c)) {
                stubbornComponents.add(c);
            }
        }
        return stubbornComponents;
    }

    /**
     * Checks whether a component of a graph is a closed component.
     * <p>
     * A closed component consists of vertices that only point to other vertices inside the component.
     * <p>
     * The empty component returns {@code true}.
     *
     * @param g         the graph
     * @param component the component to check if it is closed
     * @param <V>       the vertex type
     * @return {@code true} if {@code component} is closed, otherwise {@code false}
     * @throws NullPointerException   if {@code g} or {@code component} is {@code null}
     * @throws IllegalVertexException if not all vertices in {@code component} are elements of {@code g}
     */
    public static <V> boolean isClosedComponent(DirectedGraph<V, ?> g, Set<V> component) {
        for (V v : component) {
            if (!component.containsAll(g.adjacentOut(v))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks weather a strongly connected {@link DirectedGraph} is aperiodic.
     * <p>
     * A strongly connected directed graph is aperiodic if there is no integer {@code k > 1} that divides the length of
     * every cycle in the graph.
     * <p>
     * This method assumes that the argument is strongly connected and will perform no checks to ensure that. The
     * behavior of this method when the input is not strongly connected is undefined.
     * <p>
     * This method uses the algorithm based on BFS in <i>Graph-theoretic analysis of finite Markov chains (Jarvis and
     * Shier)</i>.
     *
     * @param g   the strongly connected graph
     * @param <V> the vertex type
     * @return {@code true} if {@code g} is aperiodic, otherwise {@code false}
     * @throws NullPointerException if {@code g} is {@code null}
     * @deprecated will be removed after 2018-12-10, use {@link BreadthFirstSearchPeriodicity} instead
     */
    @Deprecated
    public static <V> boolean isStrongAperiodic(DirectedGraph<V, ?> g) {
        return BreadthFirstSearchPeriodicity.isAperiodic(g);
    }

    /**
     * Checks if a {@link DirectedGraph} is a markov chain.
     * <p>
     * A directed graph represents a Markov chain if every vertex has at least one outgoing edge.
     * <p>
     * This method runs in time O(V).
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return {@code true} if {@code g} is a markov chain, otherwise {@code false}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> boolean isMarkovChain(DirectedGraph<V, ?> g) {
        for (V v : g) {
            if (g.adjacentOut(v).size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks weather the updating process in a graph converges.
     * <p>
     * According to <i>Social and Economic Networks (Jackson)</i>, a graph is convergent if and only if every set of
     * nodes that is strongly connected and closed is aperiodic.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return {@code true} if {@code g} converges, otherwise {@code false}
     * @throws NullPointerException if {@code g} is {@code null}
     * @deprecated will re removed after 2018-12-11, use {@link BreadthFirstSearchPeriodicity#isAperiodic()}
     */
    @Deprecated
    public static <V> boolean converges(DirectedGraph<V, ?> g) {
        return BreadthFirstSearchPeriodicity.isAperiodic(g);
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
     * Returns the number of undirected edges in {@code g}.
     * <p>
     * Complexity: O(V)
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the number of undirected edges in {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> int getEdgesCount(UndirectedGraph<V, ?> g) {
        int count = 0;
        for (V v : g.vertexSet()) {
            count += g.adjacent(v).size();
        }
        assert count % 2 == 0;
        return count / 2;
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
