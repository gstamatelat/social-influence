package gr.james.influence.algorithms.iterators;

import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomVertexIterator<V> implements Iterator<V> {
    private final List<V> nodes;
    private Random random;

    public RandomVertexIterator(DirectedGraph<V, ?> g) {
        this.nodes = new ArrayList<>(g.vertexSet());
        this.random = RandomHelper.getRandom();
    }

    public RandomVertexIterator(DirectedGraph<V, ?> g, Random r) {
        this.nodes = new ArrayList<>(g.vertexSet());
        this.random = r;
    }

    public RandomVertexIterator(DirectedGraph<V, ?> g, long seed) {
        this.nodes = new ArrayList<>(g.vertexSet());
        this.random = new Random(seed);
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
