package gr.james.influence.algorithms.iterators;

import gr.james.influence.graph.Graph;
import gr.james.influence.util.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomVertexIterator<V> implements Iterator<V> {
    private final List<V> nodes;
    private Random random;

    public RandomVertexIterator(Graph<V, ?> g) {
        this.nodes = new ArrayList<>(g.getVertices());
        this.random = RandomHelper.getRandom();
    }

    public RandomVertexIterator(Graph<V, ?> g, Random r) {
        this.nodes = new ArrayList<>(g.getVertices());
        this.random = r;
    }

    public RandomVertexIterator(Graph<V, ?> g, long seed) {
        this.nodes = new ArrayList<>(g.getVertices());
        this.random = RandomHelper.getNewRandom(seed);
    }

    @Override
    public boolean hasNext() {
        return this.nodes.size() > 0;
    }

    @Override
    public V next() {
        final int randomIndex = random.nextInt(nodes.size());
        final V v = nodes.get(randomIndex);
        final V last = nodes.get(nodes.size() - 1);
        nodes.set(randomIndex, last);
        nodes.remove(nodes.size() - 1);
        return v;
    }
}
