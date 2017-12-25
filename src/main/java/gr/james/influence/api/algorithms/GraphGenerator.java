package gr.james.influence.api.algorithms;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.util.Finals;
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
        return (SimpleGraph) generate(Finals.DEFAULT_GRAPH_FACTORY, RandomHelper.getRandom());
    }

    /**
     * Generate a new {@link SimpleGraph} based on the rules imposed by this entity.
     *
     * @param seed the seed for the {@link Random}
     * @return the generated graph
     */
    default SimpleGraph generate(long seed) {
        return (SimpleGraph) generate(Finals.DEFAULT_GRAPH_FACTORY, new Random(seed));
    }

    /**
     * Generate a new {@link SimpleGraph} based on the rules imposed by this entity.
     *
     * @param r the {@link Random} instance to use
     * @return the generated graph
     */
    default SimpleGraph generate(Random r) {
        return (SimpleGraph) generate(Finals.DEFAULT_GRAPH_FACTORY, r);
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity. This method is using the global random
     * instance.
     *
     * @param factory the {@link GraphFactory} to produce the new graph
     * @param <V>     the vertex type
     * @param <E>     the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(GraphFactory<V, E> factory) {
        return generate(factory, RandomHelper.getRandom());
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity.
     *
     * @param factory the {@link GraphFactory} to produce the new graph
     * @param seed    the seed for the {@link Random}
     * @param <V>     the vertex type
     * @param <E>     the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, long seed) {
        return generate(factory, new Random(seed));
    }

    /**
     * Generate a new {@link Graph} based on the rules imposed by this entity.
     *
     * @param factory the {@link GraphFactory} to produce the new graph
     * @param r       the {@link Random} instance to use
     * @param <V>     the vertex type
     * @param <E>     the edge type
     * @return the generated graph
     */
    <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r);
}
