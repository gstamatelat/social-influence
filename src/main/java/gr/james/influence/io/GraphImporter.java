package gr.james.influence.io;

import gr.james.influence.exceptions.InvalidFormatException;
import gr.james.influence.graph.Graph;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A {@code GraphImporter} provides means to read a graph from an {@link InputStream} with a specific format.
 */
public interface GraphImporter<G extends Graph<V, E>, V, E> {
    /**
     * Read a graph from the {@code source} URL using the format specified by this class.
     *
     * @param source       the input stream to read the graph from
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @return a new graph that was read from {@code source}
     * @throws IOException            if an I/O exception occurs
     * @throws InvalidFormatException if the format is invalid
     */
    default G from(URL source, Deserializer<V> deserializer) throws IOException {
        try (InputStream tmp = source.openStream()) {
            return from(tmp, deserializer);
        }
    }

    /**
     * Read a graph from {@code source} using the format specified by this class.
     * <p>
     * This method does not close {@code source}.
     *
     * @param source       the input stream to read the graph from
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @return a new graph that was read from {@code source}
     * @throws IOException            if an I/O exception occurs
     * @throws InvalidFormatException if the format is invalid
     */
    G from(InputStream source, Deserializer<V> deserializer) throws IOException;
}
