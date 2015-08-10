package gr.james.socialinfluence;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class GraphImplementationTests {
    /**
     * <p>{@code Graph.iterator()} must return the same iterator as {@code Graph.getVertices().iterator()}</p>
     */
    @Test
    public void vertexIteratorTest() {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 250, 0.2).create();

        Iterator<Vertex> it1 = g.iterator();
        Iterator<Vertex> it2 = g.getVertices().iterator();

        while (it1.hasNext() || it2.hasNext()) {
            Assert.assertEquals("vertexIteratorTest", true, it1.hasNext());
            Assert.assertEquals("vertexIteratorTest", true, it2.hasNext());
            Assert.assertEquals("vertexIteratorTest", it1.next(), it2.next());
        }
    }

    /**
     * <p>{@code Graph.getVertexFromIndex(i)} must return the same vertex as {@code Graph.getVertices().get(i)}</p>
     */
    @Test
    public void vertexIndexText() {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 250, 0.2).create();
        for (int i = 0; i < g.getVerticesCount(); i++) {
            Assert.assertEquals("vertexIndexText", g.getVertexFromIndex(i), g.getVertices().get(i));
        }
    }

    /**
     * <p>{@code Graph.verticesCountTest()} must return the same value as {@code Graph.getVertices().size()}</p>
     */
    @Test
    public void verticesCountTest() {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 250, 0.2).create();
        Assert.assertEquals("verticesCountTest", g.getVerticesCount(), g.getVertices().size());
    }
}
