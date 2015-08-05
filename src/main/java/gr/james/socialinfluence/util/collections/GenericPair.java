package gr.james.socialinfluence.util.collections;

import gr.james.socialinfluence.util.Conditions;

/**
 * <p>Stores exactly 2 non-null objects and is not mutable. They respect {@link #equals(Object)} and {@link #hashCode()}
 * and may be used as indices or map keys. Note that they do not protect from malevolent behavior: if one or another
 * object in the tuple is mutable, then it can be changed with the usual bad effects.</p>
 */
public class GenericPair<K, V> {
    private K first;
    private V second;

    /**
     * <p>Creates a {@code Pair} from the specified elements.</p>
     *
     * @param value1 the first value in the new {@code Pair}
     * @param value2 the second value in the new {@code Pair}
     * @throws NullPointerException if either argument is {@code null}
     */
    public GenericPair(K value1, V value2) {
        first = Conditions.requireNonNull(value1);
        second = Conditions.requireNonNull(value2);
    }

    /**
     * <p>Returns the first element.</p>
     *
     * @return the first element
     */
    public K getFirst() {
        return first;
    }

    /**
     * <p>Returns the second element.</p>
     *
     * @return the second element
     */
    public V getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericPair<?, ?> genericPair = (GenericPair<?, ?>) o;

        return first.equals(genericPair.first) && second.equals(genericPair.second);

    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("<%s, %s>", first, second);
    }
}
