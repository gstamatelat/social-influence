package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.RandomHelper;

import java.util.Iterator;

/**
 * <p>Represents a random surfer, implemented as iterator. It is generally recommended to avoid changes on the graph
 * and its structure while using any iterator on this framework.</p>
 */
public class RandomSurferIterator implements Iterator<Vertex> {
    private MemoryGraph g;
    private double dampingFactor;
    private Vertex current;

    public RandomSurferIterator(MemoryGraph g, double dampingFactor) {
        this(g, dampingFactor, null);
    }

    public RandomSurferIterator(MemoryGraph g, double dampingFactor, Vertex initialVertex) {
        this.g = g;
        this.dampingFactor = dampingFactor;
        this.current = initialVertex;
    }

    @Override
    public boolean hasNext() {
        return (this.current.getParentGraph().getVerticesCount() > 0);
    }

    @Override
    public Vertex next() {
        if (this.current != null && RandomHelper.getRandom().nextDouble() > dampingFactor) {
            this.current = this.current.getRandomOutEdge(true).getTarget();
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