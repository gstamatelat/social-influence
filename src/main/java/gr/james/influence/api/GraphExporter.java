package gr.james.influence.api;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>A {@code GraphExporter} provides means to write a Graph to an {@link OutputStream} with the format imposed by this
 * class.</p>
 */
public interface GraphExporter {
    /**
     * <p>Export {@code g} to {@code target} using the format imposed by this class. Implementations of this method
     * should not close {@code target}, instead the caller is responsible for doing so.</p>
     *
     * @param g      the graph that will be exported to {@code target}
     * @param target the output stream to write the graph to
     * @throws IOException if an I/O exception occurs
     */
    <V, E> void to(Graph<V, E> g, OutputStream target) throws IOException;
}
