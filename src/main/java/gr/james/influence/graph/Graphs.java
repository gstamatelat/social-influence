package gr.james.influence.graph;

import com.google.common.collect.ImmutableBiMap;
import gr.james.influence.algorithms.layout.Tarjan;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.util.Finals;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.exceptions.GraphException;

import java.util.*;
import java.util.stream.Collectors;

// TODO: Maybe transfer these as static on Graph
public class Graphs {
    public static <V, E> void connect(Graph<V, E> g) {
        List<List<V>> scc = new Tarjan<V>().execute(g);
        // TODO: This is not the best way to connect the components, eg some edges might already exist
        for (int i = 0; i < scc.size(); i++) {
            List<V> thisComponent = scc.get(i);
            List<V> nextComponent = scc.get((i + 1) % scc.size());
            V v = thisComponent.get(RandomHelper.getRandom().nextInt(thisComponent.size()));
            V u = nextComponent.get(RandomHelper.getRandom().nextInt(nextComponent.size()));
            g.addEdge(v, u);
        }
    }

    public static <V, E> void randomizeEdgeWeights(Graph<V, E> g) {
        for (GraphEdge<V, E> e : g.getEdges()) {
            boolean change = g.setEdgeWeight(e.getSource(), e.getTarget(),
                    RandomHelper.getRandom().nextDouble());
            assert change;
        }
    }

    /**
     * <p>Fuses two or more vertices into a single one. This method may cause information loss
     * if there are conflicts on the edges.</p>
     *
     * @param g   the graph to apply the fusion to
     * @param f   an array of vertices to be fused
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return the vertex that is the result of the fusion
     */
    public static <V, E> V fuseVertices(Graph<V, E> g, Collection<V> f) {
        // TODO: Add link to http://mathworld.wolfram.com/VertexContraction.html
        V v = g.addVertex();

        for (V y : f) {
            for (Map.Entry<V, GraphEdge<V, E>> e : g.getOutEdges(y).entrySet()) {
                g.addEdge(v, e.getKey(), e.getValue().getWeight());
            }
            for (Map.Entry<V, GraphEdge<V, E>> e : g.getInEdges(y).entrySet()) {
                g.addEdge(e.getKey(), v, e.getValue().getWeight());
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
            for (GraphEdge<V, E> e : g.getEdges()) {
                r.addEdge(e.getSource(), e.getTarget(), e.getWeight());
            }
        }
        return r;
    }

    public static SimpleGraph combineGraphs(Collection<Graph<String, Object>> graphs) {
        return (SimpleGraph) combineGraphs(Finals.DEFAULT_GRAPH_FACTORY, graphs);
    }

    public static <V, E> Graph<V, E> subGraph(Graph<V, E> g, GraphFactory<V, E> factory, Collection<V> filter) {
        return deepCopy(g, factory, filter);
    }

    public static SimpleGraph subGraph(SimpleGraph g, Collection<String> filter) {
        return deepCopy(g, filter);
    }

    public static <V, E> Graph<V, E> deepCopy(Graph<V, E> g, GraphFactory<V, E> factory, Collection<V> filter) {
        Graph<V, E> r = factory.createGraph();
        for (V v : filter) {
            if (!g.containsVertex(v)) {
                throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "deepCopy");
            }
            r.addVertex(v);
        }
        for (V v : r) {
            g.getOutEdges(v).values().stream().filter(e -> r.containsVertex(e.getTarget()))
                    .forEach(e -> r.addEdge(e.getSource(), e.getTarget(), e.getEdge(), e.getWeight()));
        }
        for (String m : g.metaKeySet()) {
            r.setMeta(m, g.getMeta(m));
        }
        return r;
    }

    public static <V, E> Graph<V, E> deepCopy(Graph<V, E> g, GraphFactory<V, E> factory) {
        return deepCopy(g, factory, g.getVertices());
    }

    public static SimpleGraph deepCopy(SimpleGraph g, Collection<String> filter) {
        return (SimpleGraph) deepCopy(g, Finals.DEFAULT_GRAPH_FACTORY, filter);
    }

    public static SimpleGraph deepCopy(SimpleGraph g) {
        return (SimpleGraph) deepCopy(g, Finals.DEFAULT_GRAPH_FACTORY, g.getVertices());
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
     * outbound edge points to itself.</p>
     *
     * @param g   the graph that the operation is to be performed
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return an unmodifiable {@code Set} of all the stubborn vertices of {@code g}
     */
    public static <V, E> Set<V> getStubbornVertices(Graph<V, E> g) {
        return Collections.unmodifiableSet(
                g.getVertices().stream()
                        .filter(v -> g.getOutDegree(v) == 1 && g.getOutEdges(v).containsKey(v))
                        .collect(Collectors.toSet())
        );
    }
}
