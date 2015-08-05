package gr.james.socialinfluence.util;

import java.util.Random;

/**
 * <p>Singleton class encapsulating a {@link Random} instance. This object is framework-global. You may use it in order
 * to avoid instantiating new {@code Random} objects within your classes.</p>
 */
public final class RandomHelper {
    /**
     * <p>The internal {@link Random} object of this singleton class.</p>
     */
    private static Random r = null;

    /**
     * <p>Initialize the global {@link Random} instance with a seed. You may not initialize the global {@code Random}
     * instance once it has been used at least once.</p>
     *
     * @param seed the seed to initialize the global {@code Random} with
     */
    public static void initRandom(long seed) {
        if (r == null) {
            Finals.LOG.info(Finals.L_RANDOM_SEED, seed);
            r = new Random(seed);
        } else {
            Finals.LOG.warn(Finals.L_RANDOM_ERROR);
        }
    }

    /**
     * <p>Retrieve the global {@link Random} instance for use. If the instance hasn't been previously initialized, it
     * will be initialized before returning with the default {@code Random} constructor.</p>
     *
     * @return the global {@code Random} instance
     */
    public static Random getRandom() {
        if (r == null) {
            r = new Random();
        }
        return r;
    }

    /**
     * <p>Get a new {@link Random} instance with the specified seed. This instance is distinct from the global
     * {@code Random} instance.</p>
     *
     * @param seed the seed to use
     * @return the new {@code Random} object
     */
    public static Random getRandom(long seed) {
        return new Random(seed);
    }
}
