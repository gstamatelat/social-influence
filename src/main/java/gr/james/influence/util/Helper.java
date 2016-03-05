package gr.james.influence.util;

import gr.james.influence.util.collections.Weighted;

import java.util.*;

public final class Helper {
    /**
     * <p>Weighted random selection.</p>
     *
     * @param weightMap  a set of weighted objects as a {@code Map}
     * @param selections how many objects to select from the keys of {@code weightMap}
     * @param <E>        the type of objects
     * @param r          the {@code Random} to use
     * @return a {@code Set} of objects as the weighted random selection
     * @see "Efraimidis, Spirakis. Weighted random sampling with a reservoir."
     */
    public static <E> Set<E> weightedRandom(Map<E, Double> weightMap, int selections, Random r) {
        PriorityQueue<Weighted<E, Double>> keyQueue = new PriorityQueue<>(11, Collections.reverseOrder());
        for (E e : weightMap.keySet()) {
            keyQueue.add(new Weighted<>(e, Math.pow(r.nextDouble(), 1.0 / weightMap.get(e))));
        }
        Set<E> finalSelections = new HashSet<>();
        while (finalSelections.size() < selections) {
            finalSelections.add(keyQueue.poll().getObject());
        }
        return finalSelections;
    }

    /**
     * <p>Weighted random selection using the global random instance.</p>
     *
     * @param weightMap  a set of weighted objects as a {@code Map}
     * @param selections how many objects to select from the keys of {@code weightMap}
     * @param <E>        the type of objects
     * @return a {@code Set} of objects as the weighted random selection
     * @see "Efraimidis, Spirakis. Weighted random sampling with a reservoir."
     */
    public static <E> Set<E> weightedRandom(Map<E, Double> weightMap, int selections) {
        return weightedRandom(weightMap, selections, RandomHelper.getRandom());
    }

    public static String getExceptionString(Throwable e) {
        // TODO: If e.getMessage() is null it becomes ugly
        String exceptionAsString = String.format("\t%s: %s\n", e.getClass().getName(), e.getMessage());
        for (StackTraceElement s : e.getStackTrace()) {
            exceptionAsString += String.format("\t\t%s\n", s);
        }
        return exceptionAsString.substring(0, exceptionAsString.length() - 1);
    }

    @SuppressWarnings({"AssertWithSideEffects", "ConstantConditions", "UnusedAssignment"})
    public static boolean isAssertionEnabled() {
        boolean assertsEnabled = false;
        assert assertsEnabled = true;
        return assertsEnabled;
    }

    public static RuntimeException convertCheckedException(Exception e) {
        throw new RuntimeException(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
    }

    public static double benchmark(Runnable runnable, double seconds) {
        long elapsed;
        int reps = 0;
        long now = System.nanoTime();
        while ((elapsed = System.nanoTime() - now) < seconds * 1.0e9) {
            runnable.run();
            reps++;
        }
        System.out.printf("Reps: %d - Elapsed: %d%n", reps, elapsed);
        return (double) reps * 1.0e9 / (double) elapsed;
    }
}
