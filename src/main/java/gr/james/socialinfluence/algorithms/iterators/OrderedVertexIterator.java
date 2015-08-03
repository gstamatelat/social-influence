package gr.james.socialinfluence.algorithms.iterators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Iterator;
import java.util.TreeSet;

@Deprecated
public class OrderedVertexIterator implements Iterator<Vertex> {
    private TreeSet<Vertex> vertices;

    public OrderedVertexIterator(Graph g) {
        vertices = new TreeSet<>(g.getVerticesAsList());
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
