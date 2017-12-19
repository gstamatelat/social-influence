package gr.james.influence.algorithms.iterators;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.RandomHelper;

import java.util.Iterator;

/**
 * <p>Represents a random surfer, implemented as iterator. It is generally recommended to avoid changes on the graph
 * and its structure while using this iterator as it is backed by the {@link Graph} object.</p>
 */
public class RandomSurferIterator<V, E> implements Iterator<V> {
    private Graph<V, E> g;
    private double dampingFactor;
    private V current;

    public RandomSurferIterator(Graph<V, E> g, double dampingFactor) {
        this(g, dampingFactor, Graphs.getRandomVertex(g));
    }

    public RandomSurferIterator(Graph<V, E> g, double dampingFactor, V initialVertex) {
        this.g = Conditions.requireNonNull(g);
        Conditions.requireArgument(dampingFactor >= 0 && dampingFactor <= 1,
                "dampingFactor must be inside [0,1], got %f", dampingFactor);
        this.dampingFactor = dampingFactor;
        this.current = Conditions.requireNonNullAndExists(initialVertex, g);
    }

    @Override
    public boolean hasNext() {
        return (g.getVerticesCount() > 0);
    }

    @Override
    public V next() {
        if (RandomHelper.getRandom().nextDouble() > dampingFactor) {
            this.current = g.getRandomOutEdge(this.current, true);
            if (this.current == null) {
                this.current = Graphs.getRandomVertex(g);
            }
        } else {
            this.current = Graphs.getRandomVertex(g);
        }
        return this.current;
    }
}
