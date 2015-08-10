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
    public void vertexIterator() {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 250, 0.2).create();

        Iterator<Vertex> it1 = g.iterator();
        Iterator<Vertex> it2 = g.getVerticesAsList().iterator();

        while (it1.hasNext() || it2.hasNext()) {
            Assert.assertEquals("", true, it1.hasNext());
            Assert.assertEquals("", true, it2.hasNext());
            Assert.assertEquals("", it1.next(), it2.next());
        }
    }
}
