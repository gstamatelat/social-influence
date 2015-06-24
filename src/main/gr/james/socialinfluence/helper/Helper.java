package gr.james.socialinfluence.helper;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

public class Helper {
    public static void log(String messagePattern, Object... arguments) {
        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + String.format(messagePattern, arguments));
    }

    public static void logError(String messagePattern, Object... arguments) {
        System.err.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + String.format(messagePattern, arguments));
    }

    public static double getWeightSum(Set<Edge> edges) {
        double sum = 0;
        for (Edge e : edges) {
            sum += e.getWeight();
        }
        return sum;
    }

    public static <T> T instantiateGeneric(Class<T> type) {
        T t = null;
        try {
            t = type.newInstance();
        } catch (Exception e) {
            throw new GraphException(Finals.E_HELPER_INSTANTIATE, type.getSimpleName());
        }
        return t;
    }

    public static Graph combineGraphs(Graph[] graphs) {
        MemoryGraph r = new MemoryGraph();
        for (Graph g : graphs) {
            for (Vertex v : g.getVertices()) {
                //v.parentGraph = r;
                r.addVertex(v);
                //r.vertices.add(v);
            }
            for (Edge e : g.getEdges()) {
                r.addEdge(e.getSource(), e.getTarget()).setWeight(e.getWeight());
                //r.edges.add(e);
            }
        }
        for (Graph g : graphs) {
            g.clear();
            //g.vertices.clear();
            //g.edges.clear();
        }
        return r;
    }
}