package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimpleMatchingSimilarity}.
 */
public class SimpleMatchingSimilarityTests {
    /**
     * Test for the graph {A->B, A->C, C->B}.
     */
    @Test
    public void simple() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();
        final Integer v1 = g.addVertex(VertexProvider.INTEGER_PROVIDER);
        final Integer v2 = g.addVertex(VertexProvider.INTEGER_PROVIDER);
        final Integer v3 = g.addVertex(VertexProvider.INTEGER_PROVIDER);
        g.addEdge(v1, v2);
        g.addEdge(v1, v3);
        g.addEdge(v3, v2);
        final VertexSimilarity<Integer, Double> smc = new SimpleMatchingSimilarity<>(g);
        Assert.assertEquals("SimpleMatchingSimilarityTests.simple", 1.0 / 3.0, smc.similarity(v1, v2), 1e-4);
        Assert.assertEquals("SimpleMatchingSimilarityTests.simple", 2.0 / 3.0, smc.similarity(v1, v3), 1e-4);
        Assert.assertEquals("SimpleMatchingSimilarityTests.simple", 2.0 / 3.0, smc.similarity(v2, v3), 1e-4);
        Assert.assertEquals("SimpleMatchingSimilarityTests.simple", 1.0, smc.similarity(v1, v1), 1e-4);
        Assert.assertEquals("SimpleMatchingSimilarityTests.simple", 1.0, smc.similarity(v2, v2), 1e-4);
        Assert.assertEquals("SimpleMatchingSimilarityTests.simple", 1.0, smc.similarity(v3, v3), 1e-4);
    }

    /**
     * In a graph with isolated vertices all similarities must be 1.
     */
    @Test
    public void empty() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();
        g.addVertices(5, VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("SimpleMatchingSimilarityTests.empty", 1.0, smc.similarity(v, w), 1e-4);
            }
        }
    }

    /**
     * The SMC of neighboring vertices in a circle of length 4 or more is (V - 4) / V.
     */
    @Test
    public void circle() {
        for (int n = 4; n < 100; n++) {
            final DirectedGraph<Integer, Object> g = new CycleGenerator<Integer, Object>(n).generate(VertexProvider.INTEGER_PROVIDER);
            final VertexSimilarity<Integer, Double> smc = new SimpleMatchingSimilarity<>(g);
            for (Integer v : g) {
                for (Integer e : g.adjacentOut(v)) {
                    Assert.assertEquals("SimpleMatchingSimilarityTests.circle",
                            (g.vertexCount() - 4) / (double) g.vertexCount(),
                            smc.similarity(v, e), 1e-4);
                }
            }
        }
    }

    /**
     * smc(v1, v2) = smc(v2, v1).
     */
    @Test
    public void commutativity() {
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("SimpleMatchingSimilarityTests.commutativity",
                        smc.similarity(v, w), smc.similarity(w, v), 1e-4);
            }
        }
    }

    /**
     * smc(v, v) = 1.
     */
    @Test
    public void identity() {
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (Integer v : g) {
            Assert.assertEquals("SimpleMatchingSimilarityTests.identity",
                    1.0, smc.similarity(v, v), 1e-4);
        }
    }

    /**
     * In a complete graph with self loops all similarities must be 1.
     */
    @Test
    public void complete() {
        final DirectedGraph<Integer, Object> g = new CompleteGenerator<Integer, Object>(5).generate(VertexProvider.INTEGER_PROVIDER);
        for (Integer v : g) {
            g.addEdge(v, v, 1.0);
        }
        final VertexSimilarity<Integer, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("SimpleMatchingSimilarityTests.complete",
                        1.0, smc.similarity(v, w), 1e-4);
            }
        }
    }
}
