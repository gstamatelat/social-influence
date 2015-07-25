package gr.james.socialinfluence.util;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.util.collections.Weighted;

import java.util.*;

public class Helper {
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
     * <p>Efraimidis, Spirakis. "Weighted random sampling with a reservoir.</p>
     */
    public static <E> List<E> weightedRandom(Map<E, Double> weightMap, int selections) {
        ArrayList<E> finalSelections = new ArrayList<>();
        PriorityQueue<Weighted<E, Double>> keyQueue = new PriorityQueue<>();
        for (E e : weightMap.keySet()) {
            keyQueue.add(new Weighted<>(e, Math.pow(RandomHelper.getRandom().nextDouble(), 1.0 / weightMap.get(e))));
        }
        while (finalSelections.size() < selections) {
            finalSelections.add(keyQueue.poll().getObject());
        }
        return finalSelections;
    }
}
