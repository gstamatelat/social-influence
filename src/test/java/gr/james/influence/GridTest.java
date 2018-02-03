package gr.james.influence;

import gr.james.influence.algorithms.generators.basic.GridGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.RandomHelper;
import org.junit.Assert;
import org.junit.Test;


public class GridTest {
    /**
     * <p>A NxM {@link GridGenerator grid graph} must always have N * M nodes and 2 * N * M - M - N edges</p>
     */
    @Test
    public void gridTest() {
        int i = RandomHelper.getRandom().nextInt(100) + 1;
        int j = RandomHelper.getRandom().nextInt(100) + 1;

        DirectedGraph<Integer, Object> g = new GridGenerator<Integer, Object>(i, j).generate(IntegerVertexProvider.provider);
        Assert.assertEquals("gridGenerator.invalidVertexCount", g.vertexCount(), i * j);
        Assert.assertEquals("gridGenerator.invalidEdgeCount", Graphs.getEdgesCount(g), 2 * (2 * i * j - i - j));
    }
}
