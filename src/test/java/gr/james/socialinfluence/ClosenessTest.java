package gr.james.socialinfluence;

import gr.james.socialinfluence.algorithms.generators.PathGenerator;
import gr.james.socialinfluence.algorithms.scoring.Closeness;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ClosenessTest {
    @Test
    public void sumClosenessTest() {
        Graph g = new PathGenerator<>(MemoryGraph.class, 4).create();
        GraphState<Double> r = Closeness.execute(g, true, g.getVertices(), new Closeness.ClosenessSumHandler());

        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(0)), 0.5, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(1)), 0.75, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(2)), 0.75, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(3)), 0.5, 0.0);
    }

    @Test
    public void sumClosenessIncludeTest() {
        Graph g = new PathGenerator<>(MemoryGraph.class, 4).create();

        Set<Vertex> include = new HashSet<>();
        include.add(g.getVertexFromIndex(0));
        include.add(g.getVertexFromIndex(1));
        include.add(g.getVertexFromIndex(2));

        GraphState<Double> r = Closeness.execute(g, true, include, new Closeness.ClosenessSumHandler());

        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(0)), 2.0 / 3.0, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(1)), 1.0, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(2)), 2.0 / 3.0, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(3)), 0.5, 0.0);
    }
}
