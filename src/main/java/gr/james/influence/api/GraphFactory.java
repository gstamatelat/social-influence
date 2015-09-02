package gr.james.influence.api;

/**
 * <p>Represents an entity that can create a specific type of graphs.</p>
 *
 * @param <T> the type of {@code Graph} that this entity can create
 */
public interface GraphFactory<T extends Graph> {
    /**
     * <p>Allocate and create an empty graph of type {@code T}.</p>
     *
     * @return a newly allocated and empty graph of type {@code T}
     */
    T create();
}
