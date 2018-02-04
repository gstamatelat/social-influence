package gr.james.influence.algorithms.generators;

import gr.james.influence.graph.Graph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.RandomHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents an algorithm that can generate a {@link Graph}.
 */
public interface RandomGraphGenerator<G extends Graph<V, E>, V, E> {
    /**
     * Generates a graph.
     * <p>
     * This method is using the global random instance.
     *
     * @param vertexProvider the vertex provider
     * @return the generated graph
     */
    default G generate(VertexProvider<V> vertexProvider) {
        return generate(RandomHelper.getRandom(), vertexProvider, new HashMap<>());
    }

    default G generate(VertexProvider<V> vertexProvider, Map<String, V> identification) {
        return generate(RandomHelper.getRandom(), vertexProvider, identification);
    }

    /**
     * Generates a graph.
     *
     * @param seed           the seed for the {@link Random}
     * @param vertexProvider the vertex provider
     * @return the generated graph
     */
    default G generate(long seed, VertexProvider<V> vertexProvider) {
        return generate(new Random(seed), vertexProvider, new HashMap<>());
    }

    default G generate(long seed, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        return generate(new Random(seed), vertexProvider);
    }

    /**
     * Generates a graph.
     *
     * @param r              the {@link Random} instance to use
     * @param vertexProvider the vertex provider
     * @return the generated graph
     */
    default G generate(Random r, VertexProvider<V> vertexProvider) {
        return generate(r, vertexProvider, new HashMap<>());
    }

    G generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification);
}
