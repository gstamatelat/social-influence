package gr.james.socialinfluence.helper;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.FullEdge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

public class Helper {
    public static void log(String messagePattern, Object... arguments) {
        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + String.format(messagePattern, arguments));
    }

    public static void logError(String messagePattern, Object... arguments) {
        System.err.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + String.format(messagePattern, arguments));
    }

    public static double getWeightSum(Collection<Edge> edges) {
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