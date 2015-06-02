package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

public class RandomVertexIterator implements Iterator<Vertex> {
    private ArrayList<Vertex> nodes;

    public RandomVertexIterator(Graph g) {
        this.nodes = new ArrayList<Vertex>();
        this.nodes.addAll(g.getVertices());
    }

    @Override
    public boolean hasNext() {
        return this.nodes.size() > 0;
    }

    @Override
    public Vertex next() {
        int randomIndex = Finals.RANDOM.nextInt(this.nodes.size());
        return this.nodes.remove(randomIndex);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}