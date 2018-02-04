package gr.james.influence.algorithms;

/**
 * Represents an algorithm that computes a property of a graph.
 * <p>
 * Typically, these algorithms produce a single value of a generic type from a given graph.
 *
 * @param <T> the property type
 */
@FunctionalInterface
public interface GraphProperty<T> {
    /**
     * Get the value of the property for the given graph.
     *
     * @return the value of the property for the given graph
     */
    T get();
}
