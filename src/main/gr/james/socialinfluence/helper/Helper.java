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

    public static void log(String messagePattern, Object... arguments) {
        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + String.format(messagePattern, arguments));
    }

    public static void logError(String messagePattern, Object... arguments) {
        System.err.println(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) + " | " + String.format(messagePattern, arguments));
    }

    public static Random getRandom(Random r) {
        if (r == null) {
            return Finals.RANDOM;
        } else {
            return r;
        }
    }

    public static double getWeightSum(Set<Edge> edges) {
        double sum = 0;
        for (Edge e : edges) {
            sum += e.getWeight();
        }
        return sum;
    }
}