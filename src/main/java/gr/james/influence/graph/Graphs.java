package gr.james.influence.graph;

import com.google.common.collect.ImmutableBiMap;
import gr.james.influence.algorithms.components.TarjanComponents;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.RandomHelper;
import gr.james.sampling.EfraimidisSampling;
import gr.james.sampling.RandomSampling;
import gr.james.sampling.WeightedRandomSampling;

import java.util.*;
import java.util.stream.Collectors;

public final class Graphs {
    private Graphs() {
    }

    public static <V, E> void connect(Graph<V, E> g) {
        List<List<V>> scc = new TarjanComponents<V>().execute(g);
        for (int i = 0; i < scc.size(); i++) {
            List<V> thisComponent = scc.get(i);
            List<V> nextComponent = scc.get((i + 1) % scc.size());
            V v = thisComponent.get(RandomHelper.getRandom().nextInt(thisComponent.size()));
            V u = nextComponent.get(RandomHelper.getRandom().nextInt(nextComponent.size()));
            g.addEdge(v, u);
        }
    }

    public static <V, E> Graph<V, E> randomizeEdgeWeights(Graph<V, E> g, boolean unused) {
        final Graph<V, E> newGraph = new MemoryGraph<>();
        for (V v : g) {
            newGraph.addVertex(v);
        }
        for (DirectedEdge<V, E> e : g.edges()) {
            newGraph.addEdge(e.source(), e.target(), e.edge(), RandomHelper.getRandom().nextDouble());
        }
        return newGraph;
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
    public static <V, E> V fuseVertices(Graph<V, E> g, Iterable<V> f, VertexProvider<V> vertexProvider) {
        V v = g.addVertex(vertexProvider);

        for (V y : f) {
            for (Map.Entry<V, DirectedEdge<V, E>> e : g.getOutEdges(y).entrySet()) {
                g.addEdge(v, e.getKey(), e.getValue().weight());
            }
            for (Map.Entry<V, DirectedEdge<V, E>> e : g.getInEdges(y).entrySet()) {
                g.addEdge(e.getKey(), v, e.getValue().weight());
            }
            g.removeVertex(y);
        }

        return v;
    }

    /**
     * <p>Combine several graphs into a single one. When combining, the vertices of the input graphs will be inserted to
     * the output along with their edges. The original graphs will not be modified.</p>
     *
     * @param type   the graphFactory that will used to create the output graph
     * @param graphs the graph objects to combine
     * @param <V>    the vertex type
     * @param <E>    the edge type
     * @return the combined graph
     */
    public static <V, E> Graph<V, E> combineGraphs(GraphFactory<V, E> type, Collection<Graph<V, E>> graphs) {
        Graph<V, E> r = type.createGraph();
        for (Graph<V, E> g : graphs) {
            for (V v : g) {
                r.addVertex(v);
            }
            for (DirectedEdge<V, E> e : g.edges()) {
                r.addEdge(e.source(), e.target(), e.weight());
            }
        }
        return r;
    }

    public static SimpleGraph combineGraphs(Collection<Graph<Integer, Object>> graphs) {
        return (SimpleGraph) combineGraphs(SimpleGraph::new, graphs);
    }

    public static <V, E> Graph<V, E> deepCopy(Graph<V, E> g, GraphFactory<V, E> factory, Collection<V> filter) {
        final Graph<V, E> r = factory.createGraph();
        for (V v : filter) {
            if (!g.containsVertex(v)) {
                throw new IllegalVertexException();
            }
            r.addVertex(v);
        }
        for (DirectedEdge<V, E> e : g.edges()) {
            if (r.containsVertex(e.source()) && r.containsVertex(e.target())) {
                r.addEdge(e.source(), e.target(), e.edge(), e.weight());
            }
        }
        for (String m : g.metaKeySet()) {
            r.setMeta(m, g.getMeta(m));
        }
        return r;
    }

    public static <V, E> Graph<V, E> deepCopy(Graph<V, E> g, GraphFactory<V, E> factory) {
        return deepCopy(g, factory, g.vertexSet());
    }

    public static <V, E> Graph<V, E> deepCopy(Graph<V, E> g) {
        return deepCopy(g, Graph::create, g.vertexSet());
    }

    public static <V, E> Graph<V, E> deepCopy(Graph<V, E> g, Collection<V> filter) {
        return deepCopy(g, Graph::create, filter);
    }

    public static SimpleGraph deepCopy(SimpleGraph g, Collection<Integer> filter) {
        return (SimpleGraph) deepCopy(g, SimpleGraph::new, filter);
    }

    public static SimpleGraph deepCopy(SimpleGraph g) {
        return (SimpleGraph) deepCopy(g, SimpleGraph::new, g.getVertices());
    }

    public static <V, E> ImmutableBiMap<V, Integer> getGraphIndexMap(Graph<V, E> g) {
        ImmutableBiMap.Builder<V, Integer> builder = ImmutableBiMap.builder();
        List<V> vertices = g.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            builder.put(vertices.get(i), i);
        }
        return builder.build();
    }

    /**
     * <p>Filters out and returns the stubborn vertices contained in {@code g}. A stubborn vertex is one that its only
     * outbound edge points to itself. By this definition, this method will not return vertices that only point to other
     * stubborn vertices.</p>
     *
     * @param g   the graph that the operation is to be performed
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return an unmodifiable {@code Set} of all the stubborn vertices of {@code g}
     * @throws NullPointerException if {@code g} is null
     */
    public static <V, E> Set<V> getStubbornVertices(Graph<V, E> g) {
        return Collections.unmodifiableSet(
                g.getVertices().stream()
                        .filter(v -> g.outDegree(v) == 1 && g.getOutEdges(v).containsKey(v))
                        .collect(Collectors.toSet())
        );
    }

    /**
     * Returns a uniformly distributed random vertex of a graph using the provided {@code Random} instance.
     * <p>
     * This method runs in constant time.
     *
     * @param g      the {@code Graph}
     * @param random the {@code Random} instance to use
     * @param <V>    the vertex type
     * @return a uniformly distributed random vertex of {@code g}
     * @throws NullPointerException     if {@code g} or {@code random} is {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code g} is empty
     */
    public static <V> V getRandomVertex(Graph<V, ?> g, Random random) {
        Conditions.requireAllNonNull(g, random);
        Conditions.requireArgument(g.vertexCount() > 0);
        return g.getVertexFromIndex(random.nextInt(g.vertexCount()));
    }

    /**
     * Returns a uniformly distributed random vertex of a graph using the global random instance.
     * <p>
     * This method runs in constant time.
     *
     * @param g   the {@code Graph}
     * @param <V> the vertex type
     * @return a uniformly distributed random vertex of {@code g}
     * @throws NullPointerException     if {@code g} or {@code random} is {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code g} is empty
     */
    public static <V> V getRandomVertex(Graph<V, ?> g) {
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
    public static <V> int getEdgesCount(Graph<V, ?> g) {
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
    public static <V> V getRandomOutVertex(Graph<V, ?> g, V v) {
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
    public static <V> V getWeightedRandomOutVertex(Graph<V, ?> g, V v) {
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

    public static double getDensity(Graph<?, ?> g) {
        double n = g.vertexCount();
        double e = Graphs.getEdgesCount(g);
        return e / (n * (n - 1));
    }
}
