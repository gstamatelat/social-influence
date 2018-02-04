package gr.james.influence.algorithms.generators;

import gr.james.influence.graph.Graph;
import gr.james.influence.graph.VertexProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an algorithm that can generate a {@link Graph}.
 */
public interface GraphGenerator<G extends Graph<V, E>, V, E> {
    /**
     * Generates and returns a graph.
     *
     * @param vertexProvider the vertex provider
     * @return the generated graph
     */
    default G generate(VertexProvider<V> vertexProvider) {
        return generate(vertexProvider, new HashMap<>());
    }

    /**
     * Generates and returns a graph.
     * <p>
     * This method populates the {@code identification} {@link Map} with information that identifies the vertices in the
     * generated graph.
     *
     * @param vertexProvider the vertex provider
     * @param identification the identification map
     * @return the generated graph
     */
    G generate(VertexProvider<V> vertexProvider, Map<String, V> identification);
}
