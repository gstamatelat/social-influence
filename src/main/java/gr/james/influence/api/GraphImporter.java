package gr.james.influence.api;

import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.util.Finals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * <p>A {@code GraphImporter} provides means to read a Graph from an {@link InputStream} with the format imposed by this
 * class.</p>
 */
public interface GraphImporter {
    /**
     * <p>Read a {@code Graph} of default type from {@code source} using the format imposed by this class.</p>
     *
     * @param source the input stream to read the graph from
     * @return a new {@code Graph} of default type that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default SimpleGraph from(URL source) throws IOException {
        return (SimpleGraph) from(source, Finals.DEFAULT_GRAPH_FACTORY, s -> s);
    }

    /**
     * <p>Read a {@code Graph} from {@code source} using the format imposed by this class.</p>
     *
     * @param source       the input stream to read the graph from
     * @param factory      the graph factory to use when creating the new {@code Graph}
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @param <V>          the vertex type
     * @param <E>          the edge type
     * @return a new {@code Graph} of type {@code T} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default <V, E> Graph<V, E> from(URL source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException {
        try (InputStream tmp = source.openStream()) {
            return from(tmp, factory, deserializer);
        }
    }

    /**
     * <p>Read a {@code Graph} of default type from {@code source} using the format imposed by this class.
     * Implementations of this method should not close {@code source}, the caller is instead responsible for doing so.
     * </p>
     *
     * @param source the input stream to read the graph from
     * @return a new {@code Graph} of default type that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default SimpleGraph from(InputStream source) throws IOException {
        return (SimpleGraph) from(source, Finals.DEFAULT_GRAPH_FACTORY, s -> s);
    }

    /**
     * <p>Read a {@code Graph} from {@code source} using the format imposed by this class. Implementations of this
     * method should not close {@code source}, the caller is instead responsible for doing so.</p>
     *
     * @param source       the input stream to read the graph from
     * @param factory      the graph factory to use when creating the new {@code Graph}
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @param <V>          the vertex type
     * @param <E>          the edge type
     * @return a new {@code Graph} of type {@code T} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException;
}
