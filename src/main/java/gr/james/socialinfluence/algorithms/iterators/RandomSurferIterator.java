package gr.james.socialinfluence.algorithms.iterators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.RandomHelper;

import java.util.Iterator;

/**
 * <p>Represents a random surfer, implemented as iterator. It is generally recommended to avoid changes on the graph
 * and its structure while using this iterator as it is backed by the {@link Graph} object.</p>
 */
public class RandomSurferIterator implements Iterator<Vertex> {
    private Graph g;
    private double dampingFactor;
    private Vertex current;

    public RandomSurferIterator(Graph g, double dampingFactor) {
        this(g, dampingFactor, g.getRandomVertex());
    }

    public RandomSurferIterator(Graph g, double dampingFactor, Vertex initialVertex) {
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
    public Vertex next() {
        if (RandomHelper.getRandom().nextDouble() > dampingFactor) {
            this.current = g.getRandomOutEdge(this.current, true);
            if (this.current == null) {
                this.current = g.getRandomVertex();
            }
        } else {
            this.current = g.getRandomVertex();
        }
        return this.current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
