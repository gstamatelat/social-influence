package gr.james.influence.util;

import java.util.Random;

/**
 * Singleton class that allows access to a framework-wide {@link Random} instance (the global {@link Random} instance).
 * <p>
 * The global {@link Random} can be accessed using the {@link #getRandom()} method. This object will be lazily
 * initialized the first time it is accessed using the default {@link Random#Random()} constructor. You may also
 * initialize it manually using the {@link #initRandom(long)} method, which will allow you to set a seed.
 */
public final class RandomHelper {
    private static Random r = null;

    /**
     * Initialize the global {@link Random} instance with a seed.
     * <p>
     * You may not initialize the global {@link Random} instance once it has been accessed and you may only initialize
     * it once per framework instance.
     *
     * @param seed the seed to initialize the global {@link Random} with
     * @return {@code true} if the initialization was successful, otherwise {@code false}
     */
    public static boolean initRandom(long seed) {
        if (r == null) {
            r = new Random(seed);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieve the global {@link Random} instance of the framework.
     * <p>
     * If the instance hasn't been previously initialized, it will be initialized before returning with the default
     * {@link Random} constructor.
     *
     * @return the global {@link Random} instance
     */
    public static Random getRandom() {
        if (r == null) {
            r = new Random();
        }
        return r;
    }

    /**
     * <p>Get a new {@link Random} instance with an unspecified seed. This instance is distinct from the global
     * {@code Random} instance.</p>
     *
     * @return the new {@code Random} object
     * @deprecated will be removed after 2018-10-20, use {@code new Random()} instead
     */
    @Deprecated
    public static Random getNewRandom() {
        return new Random();
    }

    /**
     * <p>Get a new {@link Random} instance with the specified seed. This instance is distinct from the global
     * {@code Random} instance.</p>
     *
     * @param seed the seed to use
     * @return the new {@code Random} object
     * @deprecated will be removed after 2018-10-20, use {@code new Random(seed)} instead
     */
    @Deprecated
    public static Random getNewRandom(long seed) {
        return new Random(seed);
    }
}
