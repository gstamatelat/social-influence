package gr.james.influence.api;

import gr.james.influence.graph.GraphUtils;
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
     * any changes to {@code g} will reflect to the generator.</p>
     *
     * @param g the graph to decorate around a {@code GraphGenerator}
     * @return a {@code GraphGenerator} that each of its {@link GraphGenerator#generate()} and
     * {@link GraphGenerator#generate(GraphFactory)} calls will return a deep copy of the original graph
     * @throws NullPointerException if {@code g} is {@code null}
     */
    static GraphGenerator decorate(Graph g) {
        Conditions.requireNonNull(g);
        return new GraphGenerator() {
            @Override
            public <T extends Graph> T generate(GraphFactory<T> factory, Random r) {
                return GraphUtils.deepCopy(g, factory);
            }
        };
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity. This method is using the
     * global random instance.</p>
     *
     * @return the generated graph
     */
    default Graph generate() {
        return generate(Finals.DEFAULT_GRAPH_FACTORY, RandomHelper.getRandom());
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity.</p>
     *
     * @param seed create a new {@link Random} with this seed
     * @return the generated graph
     */
    default Graph generate(long seed) {
        return generate(Finals.DEFAULT_GRAPH_FACTORY, new Random(seed));
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity.</p>
     *
     * @param r the {@code Random} instance to use
     * @return the generated graph
     */
    default Graph generate(Random r) {
        return generate(Finals.DEFAULT_GRAPH_FACTORY, r);
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity. This method is using the
     * global random instance.</p>
     *
     * @param factory the factory of {@code T}
     * @param <T>     the type of graph to generate
     * @return the generated graph
     */
    default <T extends Graph> T generate(GraphFactory<T> factory) {
        return generate(factory, RandomHelper.getRandom());
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity.</p>
     *
     * @param factory the factory of {@code T}
     * @param seed    create a new {@link Random} with this seed
     * @param <T>     the type of graph to generate
     * @return the generated graph
     */
    default <T extends Graph> T generate(GraphFactory<T> factory, long seed) {
        return generate(factory, new Random(seed));
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity.</p>
     *
     * @param factory the factory of {@code T}
     * @param r       the {@code Random} instance to use
     * @param <T>     the type of graph to generate
     * @return the generated graph
     */
    <T extends Graph> T generate(GraphFactory<T> factory, Random r);
}
