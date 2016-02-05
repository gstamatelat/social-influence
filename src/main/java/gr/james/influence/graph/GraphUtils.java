package gr.james.influence.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.util.Finals;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.collections.VertexPair;
import gr.james.influence.util.exceptions.GraphException;

import java.util.*;
import java.util.stream.Collectors;

// TODO: Maybe transfer these as static on Graph
public class GraphUtils {
    public static void createCircle(Graph g, boolean undirected) {
        Iterator<Vertex> vertexIterator = g.iterator();
        Vertex previous = vertexIterator.next();
        Vertex first = previous;
        while (vertexIterator.hasNext()) {
            Vertex next = vertexIterator.next();
            if (undirected) {
                g.addEdges(previous, next);
            } else {
                g.addEdge(previous, next);
            }
            previous = next;
        }
        if (undirected) {
            g.addEdges(previous, first);
        } else {
            g.addEdge(previous, first);
        }
    }

    public static void randomizeEdgeWeights(Graph g) {
        for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            boolean change = g.setEdgeWeight(e.getKey().getSource(), e.getKey().getTarget(),
                    RandomHelper.getRandom().nextDouble());
            assert change;
        }
    }

    /**
     * <p>Fuses two or more vertices into a single one. This method may cause information loss
     * if there are conflicts on the edges.</p>
     *
     * @param g the graph to apply the fusion to
     * @param f an array of vertices to be fused
     * @return the vertex that is the result of the fusion
     */
    public static Vertex fuseVertices(Graph g, Vertex[] f) {
        // TODO: Add link to http://mathworld.wolfram.com/VertexContraction.html
        Vertex v = g.addVertex();

        for (Vertex y : f) {
            for (Map.Entry<Vertex, Edge> e : g.getOutEdges(y).entrySet()) {
                g.addEdge(v, e.getKey(), e.getValue().getWeight());
            }
            for (Map.Entry<Vertex, Edge> e : g.getInEdges(y).entrySet()) {
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
     * @param type   the factory that will used to create the output graph
     * @param graphs the graph objects to combine
     * @param <T>    the type of the output graph
     * @return the combined graph
     */
    public static <T extends Graph> T combineGraphs(GraphFactory<T> type, Graph[] graphs) {
        T r = type.create();
        for (Graph g : graphs) {
            for (Vertex v : g) {
                r.addVertex(v);
            }
            for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
                r.addEdge(e.getKey().getFirst(), e.getKey().getSecond(), e.getValue().getWeight());
            }
        }
        return r;
    }

    public static Graph combineGraphs(Graph[] graphs) {
        return combineGraphs(Finals.DEFAULT_GRAPH_FACTORY, graphs);
    }

    public static <T extends Graph> T deepCopy(Graph g, GraphFactory<T> factory, Collection<Vertex> filter) {
        T r = factory.create();
        for (Vertex v : filter) {
            if (!g.containsVertex(v)) {
                throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "deepCopy");
            }
            r.addVertex(v);
        }
        for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            if ((r.containsVertex(e.getKey().getFirst())) && r.containsVertex(e.getKey().getSecond())) {
                r.addEdge(e.getKey().getFirst(), e.getKey().getSecond(), e.getValue().getWeight());
            }
        }
        return r;
    }

    public static <T extends Graph> T deepCopy(Graph g, GraphFactory<T> factory) {
        return deepCopy(g, factory, g.getVertices());
    }

    public static Graph deepCopy(Graph g, Collection<Vertex> filter) {
        return deepCopy(g, Finals.DEFAULT_GRAPH_FACTORY, filter);
    }

    public static Graph deepCopy(Graph g) {
        // TODO: We need to have a way to get a GraphFactory out of a Graph in order to produce same-typed graphs
        // TODO: On a second thought, maybe not, why would someone need to have the same type?
        return deepCopy(g, Finals.DEFAULT_GRAPH_FACTORY, g.getVertices());
    }

    public static BiMap<Vertex, Integer> getGraphIndexMap(Graph g) {
        // TODO: It's probably better to return an ImmutableBiMap
        BiMap<Vertex, Integer> vertexMap = HashBiMap.create();

        List<Vertex> vertices = g.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            vertexMap.put(vertices.get(i), i++);
        }

        return vertexMap;
    }

    /**
     * <p>Filters out and returns the stubborn vertices contained in {@code g}. A stubborn vertex is one that its only
     * outbound edge points to itself.</p>
     *
     * @param g the graph that the operation is to be performed
     * @return an unmodifiable {@code Set} of all the stubborn vertices of {@code g}
     */
    public static Set<Vertex> getStubbornVertices(Graph g) {
        return Collections.unmodifiableSet(
                g.getVertices().stream()
                        .filter(v -> g.getOutDegree(v) == 1 && g.getOutEdges(v).containsKey(v))
                        .collect(Collectors.toSet())
        );
    }
}
