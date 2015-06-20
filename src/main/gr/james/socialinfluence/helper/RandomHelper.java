package gr.james.socialinfluence.helper;

import java.util.Random;

public class RandomHelper {
    private static Random R = null;

    public static boolean initRandom(long seed) {
        if (R == null) {
            R = new Random(seed);
            return true;
        } else {
            return false;
        }
    }

    public static Random getRandom() {
        if (R == null) {
            R = new Random();
        }
        return R;
    }
}