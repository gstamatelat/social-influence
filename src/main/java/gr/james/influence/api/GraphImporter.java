package gr.james.influence.api;

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
    default Graph from(URL source) throws IOException {
        return from(source, Finals.DEFAULT_GRAPH_FACTORY);
    }

    /**
     * <p>Read a {@code Graph} of type {@code T} from {@code source} using the format imposed by this class.</p>
     *
     * @param source  the input stream to read the graph from
     * @param factory the factory to use when creating the new {@code Graph}
     * @param <T>     the type of the {@code Graph} to import into
     * @return a new {@code Graph} of type {@code T} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default <T extends Graph> T from(URL source, GraphFactory<T> factory) throws IOException {
        try (InputStream tmp = source.openStream()) {
            return from(tmp, factory);
        }
    }

    /**
     * <p>Read a {@code Graph} of default type from {@code source} using the format imposed by this class.
     * Implementations of this method should not close {@code source}, instead the caller is responsible for doing so.
     * </p>
     *
     * @param source the input stream to read the graph from
     * @return a new {@code Graph} of default type that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    default Graph from(InputStream source) throws IOException {
        return from(source, Finals.DEFAULT_GRAPH_FACTORY);
    }

    /**
     * <p>Read a {@code Graph} of type {@code T} from {@code source} using the format imposed by this class.
     * Implementations of this method should not close {@code source}, instead the caller is responsible for doing so.
     * </p>
     *
     * @param source  the input stream to read the graph from
     * @param factory the factory to use when creating the new {@code Graph}
     * @param <T>     the type of the {@code Graph} to import into
     * @return a new {@code Graph} of type {@code T} that was read from {@code source}
     * @throws IOException if an I/O exception occurs
     */
    <T extends Graph> T from(InputStream source, GraphFactory<T> factory) throws IOException;
    // TODO: This method should store the filename in the metadata somehow
}
