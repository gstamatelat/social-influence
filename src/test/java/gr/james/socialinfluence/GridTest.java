package gr.james.socialinfluence;

import gr.james.socialinfluence.algorithms.generators.GridGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.RandomHelper;
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

        Graph g = new GridGenerator<>(MemoryGraph.class, i, j).create();
        Assert.assertEquals("gridGenerator.invalidVertexCount", g.getVerticesCount(), i * j);
        Assert.assertEquals("gridGenerator.invalidEdgeCount", g.getEdgesCount(), 2 * (2 * i * j - i - j));
    }
}
