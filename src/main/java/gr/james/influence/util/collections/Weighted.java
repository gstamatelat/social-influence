package gr.james.influence.util.collections;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Represents an object that has a weight assigned to it.
 * <p>
 * This class provides implementations of the methods {@link #compareTo(Weighted)}, {@link #equals(Object)} and
 * {@link #hashCode()} with the general contract of comparison and equality based solely on the weight.
 * <p>
 * This class may not be suitable for use in certain collections, especially collections that implement the {@link Set}
 * interface, where {@link Weighted} instances with the same weight will be considered equal. A typical use case of this
 * class is in a {@link PriorityQueue}.
 *
 * @param <T> the object type
 * @param <W> the weight type
 */
public class Weighted<T, W extends Comparable<W>> implements Comparable<Weighted<T, W>> {
    /**
     * The object assigned to this {@link Weighted}.
     */
    public final T object;

    /**
     * The weight assigned to this {@link Weighted}.
     */
    public final W weight;

    /**
     * Construct a new {@link Weighted} from the given {@code object} and {@code weight}.
     *
     * @param object the object
     * @param weight the weight
     */
    public Weighted(T object, W weight) {
        this.object = object;
        this.weight = weight;
    }

    /**
     * Compares this object with the specified object for order.  Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     * <p>
     * The comparison is based on the weights of the respective {@link Weighted}. This method will delegate the
     * comparison to the {@code compareTo} method of {@code W}.
     *
     * @param o the object to be compared
     * @return a negative integer, zero, or a positive integer as the weight of this object is less than, equal to, or
     * greater than the weight of the specified object
     * @throws NullPointerException if {@code o} is {@code null} or the weight of either {@code this} or {@code o} is
     *                              {@code null}
     */
    @Override
    public int compareTo(Weighted<T, W> o) {
        return this.weight.compareTo(o.weight);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * <p>
     * The equality is based on the weights of the respective {@link Weighted}. This method will delegate to the
     * {@code weight.equals()} method. Basically, two {@link Weighted} are equal if and only if their weights are equal.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if {@code this.weight} and {@code obj.weight} are equal; otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(weight, ((Weighted<?, ?>) obj).weight);
    }

    /**
     * Returns a hash code value for the object. This method delegates to {@code weight.hashCode()}.
     *
     * @return the hash code of {@link #weight}
     */
    @Override
    public int hashCode() {
        return weight.hashCode();
    }
}
