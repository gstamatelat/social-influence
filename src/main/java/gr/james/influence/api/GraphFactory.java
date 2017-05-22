package gr.james.influence.api;

/**
 * <p>Represents an entity that can create a specific type of graphs.</p>
 */
public interface GraphFactory<V, E> {
    /**
     * <p>Allocate and create an empty graph of type {@code T}.</p>
     *
     * @return a newly allocated and empty graph of type {@code T}
     */
    Graph<V, E> create();

    VertexFactory<V> getVertexFactory();

    EdgeFactory<E> getEdgeFactory();
}
