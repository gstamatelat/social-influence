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
     * instance once it has been used at least once and you may only initialize it once per framework instance.</p>
     *
     * @param seed the seed to initialize the global {@code Random} with
     * @return {@code true} if the initialization was successful, otherwise {@code false}
     */
    public static boolean initRandom(long seed) {
        if (r == null) {
            Finals.LOG.info("Initialized global random with seed: {}", seed);
            r = new Random(seed);
            return true;
        } else {
            Finals.LOG.info("Initialization of global random failed with seed: {}", seed);
            return false;
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
