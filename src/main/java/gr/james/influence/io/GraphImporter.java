package gr.james.influence.io;

import gr.james.influence.graph.Graph;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A {@code GraphImporter} provides means to read a {@link Graph} from an {@link InputStream} with the format imposed by
 * this class.
 */
public interface GraphImporter<G extends Graph<V, E>, V, E> {
    /**
     * Read a {@link Graph} from the {@code source} URL using the format imposed by this class.
     *
     * @param source       the input stream to read the graph from
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @return a new {@link Graph} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default G from(URL source, Deserializer<V> deserializer) throws IOException {
        try (InputStream tmp = source.openStream()) {
            return from(tmp, deserializer);
        }
    }

    /**
     * Read a {@link Graph} from {@code source} using the format imposed by this class. The implementation of this
     * method will not close {@code source}.
     *
     * @param source       the input stream to read the graph from
     * @param deserializer the vertex deserializer to use when parsing the input stream
     * @return a new {@link Graph} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    G from(InputStream source, Deserializer<V> deserializer) throws IOException;
}
