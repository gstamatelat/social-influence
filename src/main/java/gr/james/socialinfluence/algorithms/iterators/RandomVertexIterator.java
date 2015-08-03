package gr.james.socialinfluence.algorithms.iterators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class RandomVertexIterator implements Iterator<Vertex> {
    private ArrayList<Vertex> nodes;

    public RandomVertexIterator(Graph g) {
        this.nodes = new ArrayList<>(g.getVerticesAsList());
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
