package gr.james.influence.algorithms.iterators;

import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.RandomHelper;

import java.util.Iterator;

/**
 * <p>Represents a random surfer, implemented as iterator. It is generally recommended to avoid changes on the graph
 * and its structure while using this iterator as it is backed by the {@link DirectedGraph} object.</p>
 */
public class RandomSurferIterator<V, E> implements Iterator<V> {
    private DirectedGraph<V, E> g;
    private double dampingFactor;
    private V current;

    public RandomSurferIterator(DirectedGraph<V, E> g, double dampingFactor) {
        this(g, dampingFactor, Graphs.getRandomVertex(g));
    }

    public RandomSurferIterator(DirectedGraph<V, E> g, double dampingFactor, V initialVertex) {
        this.g = Conditions.requireNonNull(g);
        Conditions.requireArgument(dampingFactor >= 0 && dampingFactor <= 1,
                "dampingFactor must be in [0,1], got %f", dampingFactor);
        this.dampingFactor = dampingFactor;
        this.current = Conditions.requireVertexInGraph(g, initialVertex);
    }

    @Override
    public boolean hasNext() {
        return (g.vertexCount() > 0);
    }

    @Override
    public V next() {
        if (RandomHelper.getRandom().nextDouble() < dampingFactor) {
            this.current = Graphs.getWeightedRandomOutVertex(g, this.current);
            if (this.current == null) {
                this.current = Graphs.getRandomVertex(g);
            }
        } else {
            this.current = Graphs.getRandomVertex(g);
        }
        return this.current;
    }
}
