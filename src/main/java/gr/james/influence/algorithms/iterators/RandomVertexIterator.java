package gr.james.influence.algorithms.iterators;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class RandomVertexIterator implements Iterator<Vertex> {
    private ArrayList<Vertex> nodes;

    public RandomVertexIterator(Graph g) {
        this.nodes = new ArrayList<>(g.getVertices());
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
