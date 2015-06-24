package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Iterator;
import java.util.TreeSet;

public class IndexIterator implements Iterator<Vertex> {
    TreeSet<Vertex> vertices = new TreeSet<Vertex>();

    public IndexIterator(MemoryGraph g) {
        this.vertices.addAll(g.getVertices());
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