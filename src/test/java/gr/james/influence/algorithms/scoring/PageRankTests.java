package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.collections.GraphState;
import org.junit.Assert;
import org.junit.Test;

import java.util.OptionalDouble;

/**
 * Tests for {@link PageRank}.
 */
public class PageRankTests {
    /**
     * The PageRank values in a strongly connected directed graph must have an average of 1.
     */
    @Test
    public void average() {
        final DirectedGraph<Integer, Object> g =
                new RandomGenerator<Integer, Object>(50, 0.3).generate(VertexProvider.INTEGER_PROVIDER);
        Graphs.connect(g);
        final PageRank<Integer> pageRank = new PageRank<>(g, 0.5, -1);
        final GraphState<Integer, Double> pr = pageRank.run();
        final OptionalDouble average = pr.values().stream().mapToDouble(x -> x).average();
        assert average.isPresent();
        Assert.assertEquals("PageRankTests.average", 1, average.getAsDouble(), 1.0e-4);
    }
}
