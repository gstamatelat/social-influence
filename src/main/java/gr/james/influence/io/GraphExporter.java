package gr.james.influence.io;

import gr.james.influence.graph.Graph;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@code GraphExporter} provides means to write a {@link Graph} to an {@link OutputStream} with the format imposed by
 * this class.
 */
public interface GraphExporter {
    /**
     * Export {@code g} to {@code target} using the format imposed by this class. The {@code V.toString} method is used
     * as a serializer for the vertices and the {@code E.toString} method is used as a serializer for the edges.
     * Implementations of this method should not close {@code target}, the caller is instead responsible for doing so.
     *
     * @param g      the graph that will be exported to {@code target}
     * @param target the output stream to write the graph to
     * @param <V>    the vertex type
     * @param <E>    the edge type
     * @throws IOException if an I/O exception occurs
     */
    default <V, E> void to(Graph<V, E> g, OutputStream target) throws IOException {
        to(g, target, V::toString, E::toString);
    }

    /**
     * Export {@code g} to {@code target} using the format imposed by this class. Implementations of this method should
     * not close {@code target}, instead the caller is responsible for doing so.
     *
     * @param g                the graph that will be exported to {@code target}
     * @param target           the output stream to write the graph to
     * @param vertexSerializer the serializer for the vertices
     * @param edgeSerializer   the serializer for the edges
     * @param <V>              the vertex type
     * @param <E>              the edge type
     * @throws IOException if an I/O exception occurs
     */
    <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> vertexSerializer, Serializer<E> edgeSerializer)
            throws IOException;
}
