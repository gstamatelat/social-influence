package gr.james.socialinfluence.algorithms.iterators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
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
        this(g, dampingFactor, null);
    }

    public RandomSurferIterator(Graph g, double dampingFactor, Vertex initialVertex) {
        this.g = g;
        this.dampingFactor = dampingFactor;
        this.current = initialVertex;
    }

    @Override
    public boolean hasNext() {
        return (g.getVerticesCount() > 0);
    }

    @Override
    public Vertex next() {
        if (this.current != null && RandomHelper.getRandom().nextDouble() > dampingFactor) {
            this.current = g.getRandomOutEdge(this.current, true);
            if (this.current == null) {
                this.current = new RandomVertexIterator(g).next();
            }
        } else {
            this.current = new RandomVertexIterator(g).next();
        }
        return this.current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
