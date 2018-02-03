package gr.james.influence.algorithms.iterators;

import gr.james.influence.graph.Graph;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>Iterates through vertices in a graph by their natural order.</p>
 */
public class OrderedVertexIterator<V> implements Iterator<V> {
    private TreeSet<V> vertices;

    public OrderedVertexIterator(Graph<V, ?> g) {
        vertices = new TreeSet<>(g.vertexSet());
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
}
