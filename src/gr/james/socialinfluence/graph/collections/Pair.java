package gr.james.socialinfluence.graph.collections;

import gr.james.socialinfluence.helper.Finals;

/**
 * <p>Stores exactly 2 non-null objects and is not mutable. They respect {@link #equals(Object)} and {@link #hashCode()}
 * and may be used as indices or map keys. Note that they do not protect from malevolent behavior: if one or another
 * object in the tuple is mutable, then it can be changed with the usual bad effects.</p>
 */
public class Pair<T> {
    private T first;
    private T second;

    /**
     * <p>Creates a {@code Pair} from the specified elements.</p>
     *
     * @param value1 the first value in the new {@code Pair}
     * @param value2 the second value in the new {@code Pair}
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public Pair(T value1, T value2) {
        if (value1 == null || value2 == null) {
            throw new IllegalArgumentException(Finals.E_PAIR_NULL);
        }
        first = value1;
        second = value2;
    }

    /**
     * <p>Returns the first element.</p>
     */
    public T getFirst() {
        return first;
    }

    /**
     * <p>Returns the second element.</p>
     */
    public T getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?> pair = (Pair<?>) o;

        return first.equals(pair.first) && second.equals(pair.second);

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