package gr.james.influence.algorithms.generators;

import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.RandomHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents an entity that can generate a {@code DirectedGraph} based on a set of rules.
 */
public interface GraphGenerator<G extends DirectedGraph<V, E>, V, E> {
    /**
     * Generate a new {@link DirectedGraph} based on the rules imposed by this entity. This method is using the global random
     * instance.
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
     * Generate a new {@link DirectedGraph} based on the rules imposed by this entity.
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
     * Generate a new {@link DirectedGraph} based on the rules imposed by this entity.
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
