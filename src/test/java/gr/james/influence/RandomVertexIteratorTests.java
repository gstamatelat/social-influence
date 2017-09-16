package gr.james.influence;

import com.google.common.collect.Lists;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.algorithms.iterators.RandomVertexIterator;
import gr.james.influence.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class RandomVertexIteratorTests {
    private SimpleGraph g = new RandomGenerator(100, 0.0).generate();

    @Test
    public void allItems() {
        List<String> it = Lists.newArrayList(new RandomVertexIterator<>(g));
        Assert.assertTrue("RandomVertexIteratorTests.allItems", it.containsAll(g.getVertices()));
        Assert.assertTrue("RandomVertexIteratorTests.allItems", g.getVertices().containsAll(it));
    }

    @Test
    public void differentOrder() {
        List<String> it1 = Lists.newArrayList(new RandomVertexIterator<>(g));
        List<String> it2 = Lists.newArrayList(new RandomVertexIterator<>(g));
        Assert.assertEquals("RandomVertexIteratorTests.differentOrder", it1.size(), it2.size());
        Assert.assertNotEquals("RandomVertexIteratorTests.differentOrder", it1, it2);
    }

    @Test
    public void random() {
        List<String> it1 = Lists.newArrayList(new RandomVertexIterator<>(g, new Random(12345)));
        List<String> it2 = Lists.newArrayList(new RandomVertexIterator<>(g, 12345));
        Assert.assertEquals("RandomVertexIteratorTests.random", it1, it2);
    }
}