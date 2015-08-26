package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;

/**
 * <p>Represents an entity that can generate a {@code Graph} based on a set of rules.</p>
 */
public interface GraphGenerator {
    /**
     * <p>Decorates a {@code Graph} around a {@code GraphGenerator} so that any subsequent
     * {@link GraphGenerator#generate()} or {@link GraphGenerator#generate(GraphFactory)} methods will return a deep
     * copy of {@code g}. The generator is backed by the input graph so that any changes to {@code g} will reflect to
     * the generator.</p>
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
            public <T extends Graph> T generate(GraphFactory<T> factory) {
                return GraphUtils.deepCopy(g, factory);
            }
        };
    }

    /**
     * <p>Generate a new graph of default type based on the rules imposed by this entity.</p>
     *
     * @return the generated graph
     */
    default Graph generate() {
        return generate(Finals.DEFAULT_GRAPH_FACTORY);
    }

    /**
     * <p>Generate a new graph of type {@code T} based on the rules imposed by this entity.</p>
     *
     * @param factory the factory of {@code T}
     * @param <T>     the type of graph to generate
     * @return the generated graph
     */
    <T extends Graph> T generate(GraphFactory<T> factory);
}
