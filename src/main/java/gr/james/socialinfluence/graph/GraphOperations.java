package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.helper.Helper;

import java.util.Iterator;
import java.util.Map;

public class GraphOperations {
    public static void createCircle(Graph g, boolean undirected) {
        Iterator<Vertex> vertexIterator = g.getVertices().iterator();
        Vertex previous = vertexIterator.next();
        Vertex first = previous;
        while (vertexIterator.hasNext()) {
            Vertex next = vertexIterator.next();
            // TODO: Should only add if not exists in order to leave the weight unmodified
            g.addEdge(previous, next, undirected);
            previous = next;
        }
        g.addEdge(previous, first, undirected);
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
     * <p>Connects all the vertices in the graph. Does not create self-connections (loops).</p>
     * <dl><dt><b>Complexity:</b></dt><dd>O(n<sup>2</sup>)</dd></dl>
     *
     * @param g the graph to apply the transformation to
     */
    public static void connectAllVertices(Graph g) {
        for (Vertex v : g.getVertices()) {
            for (Vertex w : g.getVertices()) {
                if (!v.equals(w)) {
                    g.addEdge(v, w);
                }
            }
        }
    }

    public static <T extends Graph> Graph combineGraphs(Class<T> type, Graph[] graphs) {
        Graph r = Helper.instantiateGeneric(type);
        for (Graph g : graphs) {
            for (Vertex v : g.getVertices()) {
                r.addVertex(v);
            }
            for (FullEdge e : g.getEdges()) {
                r.addEdge(e.getSource(), e.getTarget()).setWeight(e.getEdge().getWeight());
            }
        }
        for (Graph g : graphs) {
            g.clear();
        }
        return r;
    }
}
