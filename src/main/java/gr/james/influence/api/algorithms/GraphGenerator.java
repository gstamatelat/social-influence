package gr.james.influence.api.algorithms;

import gr.james.influence.graph.Graph;
import gr.james.influence.graph.GraphFactory;
import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.RandomHelper;

import java.util.Random;

/**
 * Represents an entity that can generate a {@code Graph} based on a set of rules.
 */
public interface GraphGenerator {
    /**
     * Generate a new {@link SimpleGraph} based on the rules imposed by this entity. This method is using the global
     * random instance.
     *
     * @return the generated graph
     */
    default SimpleGraph generate() {
        return (SimpleGraph) generate(SimpleGraph::new, RandomHelper.getRandom(), SimpleGraph.vertexProvider);
    }

    /**
     * Generate a new {@link SimpleGraph} based on the rules imposed by this entity.
     *
     * @param seed the seed for the {@link Random}
     * @return the generated graph
     */
    default SimpleGraph generate(long seed) {
        return (SimpleGraph) generate(SimpleGraph::new, new Random(seed), SimpleGraph.vertexProvider);
    }

    /**
     * Generate a new {@link SimpleGraph} based on the rules imposed by this entity.
     *
     * @param r the {@link Random} instance to use
     * @return the generated graph
     */
    default SimpleGraph generate(Random r) {
        return (SimpleGraph) generate(SimpleGraph::new, r, SimpleGraph.vertexProvider);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity. This method is using the global random
     * instance.
     *
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(VertexProvider<V> vertexProvider) {
        return generate(Graph::create, RandomHelper.getRandom(), vertexProvider);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity.
     *
     * @param seed           the seed for the {@link Random}
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(long seed, VertexProvider<V> vertexProvider) {
        return generate(Graph::create, new Random(seed), vertexProvider);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity.
     *
     * @param r              the {@link Random} instance to use
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(Random r, VertexProvider<V> vertexProvider) {
        return generate(Graph::create, r, vertexProvider);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity. This method is using the global random
     * instance.
     *
     * @param factory        the {@link GraphFactory} to produce the new graph
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, VertexProvider<V> vertexProvider) {
        return generate(factory, RandomHelper.getRandom(), vertexProvider);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity.
     *
     * @param factory        the {@link GraphFactory} to produce the new graph
     * @param seed           the seed for the {@link Random}
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, long seed, VertexProvider<V> vertexProvider) {
        return generate(factory, new Random(seed), vertexProvider);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity.
     *
     * @param factory        the {@link GraphFactory} to produce the new graph
     * @param r              the {@link Random} instance to use
     * @param vertexProvider the vertex provider
     * @param <V>            the vertex type
     * @param <E>            the edge type
     * @return the generated graph
     */
    <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r, VertexProvider<V> vertexProvider);
}
