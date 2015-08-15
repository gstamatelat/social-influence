package gr.james.socialinfluence.util;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.util.collections.Weighted;
import gr.james.socialinfluence.util.exceptions.GraphException;

import java.util.*;

public final class Helper {
    public static double getWeightSum(Collection<Edge> edges) {
        double sum = 0;
        for (Edge e : edges) {
            sum += e.getWeight();
        }
        return sum;
    }

    public static <T> T instantiateGeneric(Class<T> type) {
        T t;
        try {
            t = type.newInstance();
        } catch (Exception e) {
            throw new GraphException(Finals.E_HELPER_INSTANTIATE, type.getSimpleName());
        }
        return t;
    }

    /**
     * <p>Weighted random selection.</p>
     *
     * @param weightMap  a set of weighted objects as a {@code Map}
     * @param selections how many objects to select from the keys of {@code weightMap}
     * @param <E>        the type of objects
     * @return a {@code Set} of objects as the weighted random selection
     * @see "Efraimidis, Spirakis. Weighted random sampling with a reservoir."
     */
    public static <E> Set<E> weightedRandom(Map<E, Double> weightMap, int selections) {
        PriorityQueue<Weighted<E, Double>> keyQueue = new PriorityQueue<>(11, Collections.reverseOrder());
        for (E e : weightMap.keySet()) {
            keyQueue.add(new Weighted<>(e, Math.pow(RandomHelper.getRandom().nextDouble(), 1.0 / weightMap.get(e))));
        }
        Set<E> finalSelections = new HashSet<>();
        while (finalSelections.size() < selections) {
            finalSelections.add(keyQueue.poll().getObject());
        }
        return finalSelections;
    }

    public static String getExceptionString(Exception e) {
        // TODO: If e.getMessage() is null it becomes ugly
        String exceptionAsString = String.format("\t%s: %s\n", e.getClass().getName(), e.getMessage());
        for (StackTraceElement s : e.getStackTrace()) {
            exceptionAsString += String.format("\t\t%s\n", s);
        }
        return exceptionAsString.substring(0, exceptionAsString.length() - 1);
    }

    public static RuntimeException convertCheckedException(Exception e) {
        throw new RuntimeException(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
    }
}
