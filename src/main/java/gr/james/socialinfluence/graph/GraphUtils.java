package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.collections.VertexPair;
import gr.james.socialinfluence.util.exceptions.GraphException;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * <p>Fuses two or more vertices into a single one. This method may cause information loss
     * if there are conflicts on the edges.</p>
     *
     * @param g the graph to apply the fusion to
     * @param f an array of vertices to be fused
     * @return the vertex that is the result of the fusion
     */
    public static Vertex fuseVertices(Graph g, Vertex[] f) {
        Vertex v = g.addVertex();

        for (Vertex y : f) {
            for (Map.Entry<Vertex, Edge> e : g.getOutEdges(y).entrySet()) {
                g.addEdge(v, e.getKey()).setWeight(e.getValue().getWeight());
            }
            for (Map.Entry<Vertex, Edge> e : g.getInEdges(y).entrySet()) {
                g.addEdge(e.getKey(), v).setWeight(e.getValue().getWeight());
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
                r.addEdge(e.getKey().getFirst(), e.getKey().getSecond()).setWeight(e.getValue().getWeight());
            }
        }
        return r;
    }

    public static Graph combineGraphs(Graph[] graphs) {
        return combineGraphs(Finals.DEFAULT_GRAPH_FACTORY, graphs);
    }

    public static <T extends Graph> Graph deepCopy(Class<T> type, Graph g) {
        return deepCopy(type, g, g.getVertices());
    }

    public static <T extends Graph> Graph deepCopy(Class<T> type, Graph g, Collection<Vertex> includeOnly) {
        Graph r = Helper.instantiateGeneric(type);
        for (Vertex v : includeOnly) {
            if (!g.containsVertex(v)) {
                throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "deepCopy");
            }
            r.addVertex(v);
        }
        for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            if ((r.containsVertex(e.getKey().getFirst())) && r.containsVertex(e.getKey().getSecond())) {
                r.addEdge(e.getKey().getFirst(), e.getKey().getSecond()).setWeight(e.getValue().getWeight());
            }
        }
        return r;
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
