package gr.james.influence.api;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@code GraphExporter} provides means to write a {@link Graph} to an {@link OutputStream} with the format imposed by
 * this class.
 */
public interface GraphExporter {
    /**
     * Export {@code g} to {@code target} using the format imposed by this class. The {@code V.toString} method is used
     * as a serializer. Implementations of this method should not close {@code target}, the caller is instead
     * responsible for doing so.
     *
     * @param g      the graph that will be exported to {@code target}
     * @param target the output stream to write the graph to
     * @param <V>    the vertex type
     * @param <E>    the edge type
     * @throws IOException if an I/O exception occurs
     */
    default <V, E> void to(Graph<V, E> g, OutputStream target) throws IOException {
        to(g, target, V::toString);
    }

    /**
     * Export {@code g} to {@code target} using the format imposed by this class. Implementations of this method should
     * not close {@code target}, instead the caller is responsible for doing so.
     *
     * @param g          the graph that will be exported to {@code target}
     * @param target     the output stream to write the graph to
     * @param serializer the serializer to use when writing to the output stream
     * @param <V>        the vertex type
     * @param <E>        the edge type
     * @throws IOException if an I/O exception occurs
     */
    <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> serializer) throws IOException;
}
