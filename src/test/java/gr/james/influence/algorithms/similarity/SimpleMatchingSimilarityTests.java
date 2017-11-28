package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.api.algorithms.VertexSimilarity;
import gr.james.influence.graph.SimpleGraph;
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
        final SimpleGraph g = new SimpleGraph();
        final String v1 = g.addVertex();
        final String v2 = g.addVertex();
        final String v3 = g.addVertex();
        g.addEdge(v1, v2);
        g.addEdge(v1, v3);
        g.addEdge(v3, v2);
        final VertexSimilarity<String, Double> smc = new SimpleMatchingSimilarity<>(g);
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
        final SimpleGraph g = new SimpleGraph();
        g.addVertices(5);
        final VertexSimilarity<String, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
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
            final SimpleGraph g = new CycleGenerator(n).generate();
            final VertexSimilarity<String, Double> smc = new SimpleMatchingSimilarity<>(g);
            for (String v : g) {
                for (String e : g.getOutEdges(v).keySet()) {
                    Assert.assertEquals("SimpleMatchingSimilarityTests.circle",
                            (g.getVerticesCount() - 4) / (double) g.getVerticesCount(),
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
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final VertexSimilarity<String, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
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
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final VertexSimilarity<String, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (String v : g) {
            Assert.assertEquals("SimpleMatchingSimilarityTests.identity",
                    1.0, smc.similarity(v, v), 1e-4);
        }
    }

    /**
     * In a complete graph with self loops all similarities must be 1.
     */
    @Test
    public void complete() {
        final SimpleGraph g = new CompleteGenerator(5).generate();
        for (String v : g) {
            g.addEdge(v, v, 1.0);
        }
        final VertexSimilarity<String, Double> smc = new SimpleMatchingSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
                Assert.assertEquals("SimpleMatchingSimilarityTests.complete",
                        1.0, smc.similarity(v, w), 1e-4);
            }
        }
    }
}
