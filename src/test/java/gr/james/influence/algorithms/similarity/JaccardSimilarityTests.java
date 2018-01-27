package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.api.algorithms.VertexSimilarity;
import gr.james.influence.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link JaccardSimilarity}.
 */
public class JaccardSimilarityTests {
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
        final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
        Assert.assertEquals("JaccardSimilarityTests.simple", 0.0, jaccard.similarity(v1, v2), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 0.5, jaccard.similarity(v1, v3), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 0.0, jaccard.similarity(v2, v3), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 1.0, jaccard.similarity(v1, v1), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 1.0, jaccard.similarity(v3, v3), 1e-4);
        Assert.assertTrue("JaccardSimilarityTests.simple", Double.isNaN(jaccard.similarity(v2, v2)));
    }

    /**
     * In a graph with isolated vertices all similarities must be NaN.
     */
    @Test
    public void empty() {
        final SimpleGraph g = new SimpleGraph();
        g.addVertices(5);
        final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
                Assert.assertTrue("JaccardSimilarityTests.empty", Double.isNaN(jaccard.similarity(v, w)));
            }
        }
    }

    /**
     * The Jaccard similarity of neighboring vertices in a circle of length 4 or more is 0.
     */
    @Test
    public void circle() {
        for (int n = 4; n < 100; n++) {
            final SimpleGraph g = new CycleGenerator(n).generate();
            final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
            for (String v : g) {
                for (String e : g.getOutEdges(v).keySet()) {
                    Assert.assertEquals("JaccardSimilarityTests.circle",
                            0, jaccard.similarity(v, e), 1e-4);
                }
            }
        }
    }

    /**
     * jaccard(v1, v2) = jaccard(v2, v1).
     */
    @Test
    public void commutativity() {
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
                Assert.assertEquals("JaccardSimilarityTests.commutativity",
                        jaccard.similarity(v, w), jaccard.similarity(w, v), 1e-4);
            }
        }
    }

    /**
     * jaccard(v, v) = 1.
     */
    @Test
    public void identity() {
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
        for (String v : g) {
            if (g.outDegree(v) != 0) {
                Assert.assertEquals("JaccardSimilarityTests.identity",
                        1.0, jaccard.similarity(v, v), 1e-4);
            } else {
                Assert.assertTrue("JaccardSimilarityTests.identity",
                        Double.isNaN(jaccard.similarity(v, v)));
            }
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
        final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
                Assert.assertEquals("JaccardSimilarityTests.complete", 1.0, jaccard.similarity(v, w), 1e-4);
            }
        }
    }
}
