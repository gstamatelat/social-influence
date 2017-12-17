package gr.james.influence.api;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an object that contains metadata information. Metadata consists of a key-value map, in which keys and
 * values are strings. Metadata cannot contain {@code null} keys or values and keys must be unique.
 */
public interface Metadata {
    /**
     * Gets the specified meta field.
     *
     * @param key the key to get the value of
     * @return the meta field that corresponds to {@code key} or {@code null} if it doesn't exist
     * @throws NullPointerException is {@code key} is {@code null}
     */
    String getMeta(String key);

    /**
     * Maps {@code key} to {@code value}.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key} or {@code null} if there was no mapping for {@code key}
     * @throws NullPointerException if either {@code key} or {@code value} is {@code null}
     */
    String setMeta(String key, String value);

    /**
     * Removes the mapping for the {@code key}.
     *
     * @param key key whose mapping is to be removed
     * @return the previous value associated with {@code key} or {@code null} if there was no mapping for {@code key}
     * @throws NullPointerException if {@code key} is {@code null}
     */
    String removeMeta(String key);

    /**
     * Returns a {@link Set} view of the metadata keys.
     *
     * @return a unmodifiable {@code Set} of the keys contained in this instance
     */
    Set<String> metaKeySet();

    /**
     * Removes all meta fields.
     */
    default void clearMeta() {
        final Set<String> keySet = new HashSet<>(metaKeySet());
        keySet.forEach(this::removeMeta);
        assert metaKeySet().size() == 0;
    }
}
