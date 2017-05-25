package gr.james.influence.api;

import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.RandomHelper;

import java.util.Random;

/**
 * <p>Represents an entity that can generate a {@code Graph} based on a set of rules.</p>
 */
public interface GraphGenerator {
    /**
     * <p>Decorates a {@code Graph} around a {@code GraphGenerator} so that any subsequent {@code generate} methods will
     * return a deep copy of {@code g}. In the case of this decorator it is obvious that
     * {@link #generate(GraphFactory, Random)} is identical to {@link #generate(GraphFactory)} and
     * {@link #generate(Random)} is identical to {@link #generate()}. The generator is backed by the input graph so that
     * any changes to {@code g} will reflect on the generator.</p>
     *
     * @param g   the graph to decorate around a {@code GraphGenerator}
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return a {@code GraphGenerator} that each of its {@link GraphGenerator#generate()} and
     * {@link GraphGenerator#generate(GraphFactory)} calls will return a deep copy of the original graph
     * @throws NullPointerException if {@code g} is {@code null}
     */
    static <V, E> GraphGenerator decorate(Graph<V, E> g) {
        Conditions.requireNonNull(g);
        return new GraphGenerator() {
            @Override
            public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
                // return Graphs.deepCopy(g, factory);
                // TODO
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity. This method is using the
     * global random instance.</p>
     *
     * @return the generated graph
     */
    default SimpleGraph generate() {
        return (SimpleGraph) generate(Finals.DEFAULT_GRAPH_FACTORY, RandomHelper.getRandom());
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity.</p>
     *
     * @param seed create a new {@link Random} with this seed
     * @return the generated graph
     */
    default SimpleGraph generate(long seed) {
        return (SimpleGraph) generate(Finals.DEFAULT_GRAPH_FACTORY, new Random(seed));
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity.</p>
     *
     * @param r the {@code Random} instance to use
     * @return the generated graph
     */
    default SimpleGraph generate(Random r) {
        return (SimpleGraph) generate(Finals.DEFAULT_GRAPH_FACTORY, r);
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity. This method is using the
     * global random instance.</p>
     *
     * @param factory the graphFactory of {@code T}
     * @param <V>     the vertex type
     * @param <E>     the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(GraphFactory<V, E> factory) {
        return generate(factory, RandomHelper.getRandom());
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity.</p>
     *
     * @param factory the graphFactory of {@code T}
     * @param seed    create a new {@link Random} with this seed
     * @param <V>     the vertex type
     * @param <E>     the edge type
     * @return the generated graph
     */
    default <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, long seed) {
        return generate(factory, new Random(seed));
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity.</p>
     *
     * @param factory the graphFactory of {@code T}
     * @param r       the {@code Random} instance to use
     * @param <V>     the vertex type
     * @param <E>     the edge type
     * @return the generated graph
     */
    <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r);
}
