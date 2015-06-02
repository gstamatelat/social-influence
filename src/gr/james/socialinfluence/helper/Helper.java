package gr.james.socialinfluence.helper;

import gr.james.socialinfluence.graph.Edge;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

public class Helper {
    /**
     * <p>Produces a random BigInteger between 0 (inclusive) and limit (exclusive)</p>
     * <p><b>Running Time:</b> Fast</p>
     *
     * @param limit the upper bound of the random BigInteger
     * @return the random BigInteger
     */
    public static BigInteger getRandomBigInteger(BigInteger limit) {
        BigInteger num;
        do {
            num = new BigInteger(limit.bitLength(), Finals.RANDOM);
        } while (num.compareTo(limit) >= 0 || num.compareTo(BigInteger.ZERO) < 0);
        return num;
    }

    public static void log(String text) {
        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + text);
    }

    public static void logError(String text) {
        System.err.println(text);
    }

    public static Random getRandom(Random r) {
        if (r == null) {
            return Finals.RANDOM;
        } else {
            return r;
        }
    }

    /**
     * <p>This field holds a serial number needed by {@link #getNextId getNextId()}.</p>
     */
    private static int nextId = 1;

    /**
     * <p>Returns an integer id that is guaranteed to be unique for every session (execution). This method is used by
     * {@link gr.james.socialinfluence.graph.Graph#addVertex() Graph.addVertex()} to produce a unique id for the new
     * vertex.</p>
     *
     * @return the unique id
     */
    public static int getNextId() {
        return nextId++;
    }

    public static double getWeightSum(Set<Edge> edges) {
        double sum = 0;
        for (Edge e : edges) {
            sum += e.getWeight();
        }
        return sum;
    }
}