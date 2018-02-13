package gr.james.influence.algorithms.clustering;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple greedy algorithm for the k-center problem.
 * <p>
 * This greedy algorithm will start from an arbitrary center in the data set and pick a new center that is farthest
 * from the current set of centers.
 *
 * @param <V> the item type
 */
public abstract class AbstractGreedyKCenter<V> {
    /**
     * Computes the distance between two elements of the data set.
     *
     * @param v1 one item
     * @param v2 the other item
     * @return the distance between two elements of the data set
     */
    protected abstract double distance(V v1, V v2);

    /**
     * Returns the distance between one element and a set of elements.
     * <p>
     * By default, this method computes the distances from {@code v} to all items in {@code others} and returns the sum
     * of their natural logarithms.
     *
     * @param v      the element
     * @param others the set of elements
     * @return the distance between {@code v} and {@code others}
     */
    protected double distance(V v, Set<V> others) {
        assert !others.contains(v);
        assert others.size() > 0;
        double distance = 0;
        for (V w : others) {
            distance += Math.log(distance(v, w));
        }
        return distance;
    }

    /**
     * Perform the algorithm and return the resulting centers.
     *
     * @param data  the data set
     * @param start the item to start from
     * @param size  the number of centers to find
     * @return the resulting centers
     * @throws NullPointerException     if {@code data} is {@code null}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code size > data.size()}
     * @throws IllegalArgumentException if {@code start} is not in {@code data}
     */
    public final Set<V> centers(Set<V> data, V start, int size) {
        if (size < 1) {
            throw new IllegalArgumentException();
        }
        if (size > data.size()) {
            throw new IllegalArgumentException();
        }
        if (!data.contains(start)) {
            throw new IllegalArgumentException();
        }

        final Set<V> items = new HashSet<>();
        items.add(start);

        while (items.size() < size) {
            double minDistance = Double.POSITIVE_INFINITY;
            V minItem = null;
            for (V v : data) {
                if (!items.contains(v)) {
                    final double currentDistance = distance(v, items);
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        minItem = v;
                    }
                }
            }
            assert minItem != null;
            items.add(minItem);
        }

        return items;
    }
}
