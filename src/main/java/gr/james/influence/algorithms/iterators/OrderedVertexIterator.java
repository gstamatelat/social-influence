package gr.james.influence.algorithms.iterators;

import gr.james.influence.api.Graph;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>Iterates through vertices in a graph by their natural order.</p>
 */
public class OrderedVertexIterator<V> implements Iterator<V> {
    private TreeSet<V> vertices;

    public OrderedVertexIterator(Graph<V, ?> g) {
        vertices = new TreeSet<>(g.getVertices());
    }

    @Override
    public boolean hasNext() {
        return this.vertices.size() > 0;
    }

    @Override
    public V next() {
        Iterator<V> it = this.vertices.iterator();
        V next = it.next();
        it.remove();
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
