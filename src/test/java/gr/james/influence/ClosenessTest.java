package gr.james.influence;

import gr.james.influence.algorithms.generators.PathGenerator;
import gr.james.influence.algorithms.scoring.Closeness;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ClosenessTest {
    @Test
    public void sumClosenessTest() {
        Graph g = new PathGenerator(4).generate();
        GraphState<Double> r = Closeness.closeness(g, Direction.INBOUND, g.getVertices());

        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(0)), 0.5, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(1)), 0.75, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(2)), 0.75, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(3)), 0.5, 0.0);
    }

    @Test
    public void sumClosenessIncludeTest() {
        Graph g = new PathGenerator(4).generate();

        Set<Vertex> include = new HashSet<>();
        include.add(g.getVertexFromIndex(0));
        include.add(g.getVertexFromIndex(1));
        include.add(g.getVertexFromIndex(2));

        GraphState<Double> r = Closeness.closeness(g, Direction.INBOUND, include);

        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(0)), 2.0 / 3.0, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(1)), 1.0, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(2)), 2.0 / 3.0, 0.0);
        Assert.assertEquals("sumClosenessTest", r.get(g.getVertexFromIndex(3)), 0.5, 0.0);
    }
}
