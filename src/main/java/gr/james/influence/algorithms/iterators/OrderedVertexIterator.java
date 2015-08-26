package gr.james.influence.algorithms.iterators;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Vertex;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>Iterates through vertices in a graph by their natural order, imposed by {@link Vertex#compareTo(Vertex)}.</p>
 */
public class OrderedVertexIterator implements Iterator<Vertex> {
    private TreeSet<Vertex> vertices;

    public OrderedVertexIterator(Graph g) {
        vertices = new TreeSet<>(g.getVertices());
    }

    @Override
    public boolean hasNext() {
        return this.vertices.size() > 0;
    }

    @Override
    public Vertex next() {
        Iterator<Vertex> it = this.vertices.iterator();
        Vertex next = it.next();
        it.remove();
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
