package gr.james.influence.algorithms.iterators;

import gr.james.influence.api.Graph;
import gr.james.influence.util.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class RandomVertexIterator<V> implements Iterator<V> {
    private ArrayList<V> nodes;

    public RandomVertexIterator(Graph<V, ?> g) {
        this.nodes = new ArrayList<>(g.getVertices());
    }

    @Override
    public boolean hasNext() {
        return this.nodes.size() > 0;
    }

    @Override
    public V next() {
        int randomIndex = RandomHelper.getRandom().nextInt(this.nodes.size());
        return this.nodes.remove(randomIndex);
    }
}
