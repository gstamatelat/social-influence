package gr.james.influence.io;

import gr.james.influence.graph.Graph;
import gr.james.influence.graph.GraphFactory;
import gr.james.influence.graph.SimpleGraph;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A {@code GraphImporter} provides means to read a {@link Graph} from an {@link InputStream} with the format imposed by
 * this class.
 */
public interface GraphImporter {
    /**
     * Read a {@link Graph} of default type from the {@code source} URL using the format imposed by this class.
     *
     * @param source the input stream to read the graph from
     * @return a new {@link Graph} of default type that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default SimpleGraph from(URL source) throws IOException {
        return (SimpleGraph) from(source, SimpleGraph.factory, Integer::parseInt);
    }

    /**
     * Read a {@link Graph} from the {@code source} URL using the format imposed by this class.
     *
     * @param source       the input stream to read the graph from
     * @param factory      the graph factory to use when creating the new {@link Graph}
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @param <V>          the vertex type
     * @param <E>          the edge type
     * @return a new {@link Graph} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default <V, E> Graph<V, E> from(URL source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException {
        try (InputStream tmp = source.openStream()) {
            return from(tmp, factory, deserializer);
        }
    }

    /**
     * Read a {@link Graph} of default type from {@code source} using the format imposed by this class. The
     * implementation of this method will not close {@code source}.
     *
     * @param source the input stream to read the graph from
     * @return a new {@link Graph} of default type that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default SimpleGraph from(InputStream source) throws IOException {
        return (SimpleGraph) from(source, SimpleGraph.factory, Integer::parseInt);
    }

    /**
     * Read a {@link Graph} from {@code source} using the format imposed by this class. The implementation of this
     * method will not close {@code source}.
     *
     * @param source       the input stream to read the graph from
     * @param factory      the graph factory to use when creating the new {@link Graph}
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @param <V>          the vertex type
     * @param <E>          the edge type
     * @return a new {@link Graph} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException;
}
