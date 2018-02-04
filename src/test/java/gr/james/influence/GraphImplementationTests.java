package gr.james.influence;

import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class GraphImplementationTests {
    /**
     * <p>{@code DirectedGraph.iterator()} must return the same iterator as {@code DirectedGraph.getVertices().iterator()}</p>
     */
    @Test
    public void vertexIteratorTest() {
        DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(250, 0.2).generate(VertexProvider.intProvider);

        Iterator<Integer> it1 = g.iterator();
        Iterator<Integer> it2 = g.vertexSet().iterator();

        while (it1.hasNext() || it2.hasNext()) {
            Assert.assertEquals("vertexIteratorTest", true, it1.hasNext());
            Assert.assertEquals("vertexIteratorTest", true, it2.hasNext());
            Assert.assertEquals("vertexIteratorTest", it1.next(), it2.next());
        }
    }

    /**
     * <p>{@code DirectedGraph.getVertexFromIndex(i)} must return the same vertex as {@code DirectedGraph.getVertices().get(i)}</p>
     */
    /*@Test
    public void vertexIndexText() {
        SimpleGraph g = new RandomGenerator(250, 0.2).generate();
        for (int i = 0; i < g.vertexCount(); i++) {
            Assert.assertEquals("vertexIndexText", g.getVertexFromIndex(i), g.getVertices().get(i));
        }
    }*/

    /**
     * <p>{@code DirectedGraph.verticesCountTest()} must return the same value as {@code DirectedGraph.getVertices().size()}</p>
     */
    @Test
    public void verticesCountTest() {
        DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(250, 0.2).generate(VertexProvider.intProvider);
        Assert.assertEquals("verticesCountTest", g.vertexCount(), g.vertexSet().size());
    }
}
