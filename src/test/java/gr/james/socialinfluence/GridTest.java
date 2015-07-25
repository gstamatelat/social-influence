package gr.james.socialinfluence;

import gr.james.socialinfluence.algorithms.generators.GridGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import org.junit.Assert;
import org.junit.Test;


public class GridTest {
    /**
     * <p>A NxM {@link GridGenerator grid graph} must always have N * M nodes and 2 * N * M - M - N edges</p>
     */
    @Test
    public void gridTest() {
        int[] N = {1, 6, 7, 8, 23, 34, 55};
        int[] M = {1, 2, 3, 7, 21, 40, 49};
        for (int i : N) {
            for (int j : M) {
                Graph g = new GridGenerator<>(MemoryGraph.class, i, j).create();
                Assert.assertEquals("gridGenerator.invalidVertexCount", g.getVerticesCount(), i * j);
                Assert.assertEquals("gridGenerator.invalidEdgeCount", g.getEdgesCount(), 2 * (2 * i * j - i - j));
            }
        }
    }
}
