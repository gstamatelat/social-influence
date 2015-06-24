package gr.james.socialinfluence.helper;

import gr.james.socialinfluence.graph.Edge;

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
}