package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class RandomVertexIterator implements Iterator<Vertex> {
    private ArrayList<Vertex> nodes;

    public RandomVertexIterator(MemoryGraph g) {
        this.nodes = new ArrayList<Vertex>();
        this.nodes.addAll(g.getVertices());
    }

    @Override
    public boolean hasNext() {
        return this.nodes.size() > 0;
    }

    @Override
    public Vertex next() {
        int randomIndex = RandomHelper.getRandom().nextInt(this.nodes.size());
        return this.nodes.remove(randomIndex);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}