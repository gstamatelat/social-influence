package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.api.VertexSimilarity;
import gr.james.influence.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Test;

public class PearsonSimilarityTest {
    /**
     * Test for the graph {A->B (1), A->C (1), C->B (2)}
     */
    @Test
    public void simple() {
        final SimpleGraph g = new SimpleGraph();
        final String v1 = g.addVertex();
        final String v2 = g.addVertex();
        final String v3 = g.addVertex();
        g.addEdge(v1, v2, 1);
        g.addEdge(v1, v3, 1);
        g.addEdge(v3, v2, 2);
        final VertexSimilarity<String, Double> pearson = new PearsonSimilarity<>(g);
        final double v1v2 = pearson.similarity(v1, v2);
        final double v1v3 = pearson.similarity(v1, v3);
        final double v2v3 = pearson.similarity(v2, v3);
        Assert.assertEquals("PearsonSimilarityTest.simple", 0.5, v1v3, 1e-4);
        Assert.assertTrue("PearsonSimilarityTest.simple", Double.isNaN(v1v2));
        Assert.assertTrue("PearsonSimilarityTest.simple", Double.isNaN(v2v3));
    }

    /**
     * The Pearson similarity of neighboring vertices in a circle of length 4 or more is 2 / (2 - n)
     */
    @Test
    public void circle() {
        for (int n = 4; n < 100; n++) {
            final double similarity = 2.0 / (2.0 - n);
            final SimpleGraph g = new CycleGenerator(n).generate();
            final VertexSimilarity<String, Double> pearson = new PearsonSimilarity<>(g);
            for (String v : g) {
                for (String e : g.getOutEdges(v).keySet()) {
                    Assert.assertEquals("PearsonSimilarityTest.circle",
                            similarity, pearson.similarity(v, e), 1e-4);
                }
            }
        }
    }

    /**
     * pearson(v1, v2) = pearson(v2, v1)
     */
    @Test
    public void commutativity() {
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final VertexSimilarity<String, Double> pearson = new PearsonSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
                Assert.assertEquals("PearsonSimilarityTest.commutativity",
                        pearson.similarity(v, w), pearson.similarity(w, v), 1e-4);
            }
        }
    }

    /**
     * Similarity is undefined if and only if variance of either vertex is zero
     */
    @Test
    public void undefined() {
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final PearsonSimilarity<String> pearson = new PearsonSimilarity<>(g);
        for (String v : g) {
            for (String w : g) {
                Assert.assertEquals("PearsonSimilarityTest.undefined",
                        pearson.variance(v) == 0 || pearson.variance(w) == 0,
                        Double.isNaN(pearson.similarity(v, w)));
            }
        }
    }

    /**
     * pearson(v, v) = 1
     */
    @Test
    public void identity() {
        final SimpleGraph g = new RandomGenerator(100, 0.1).generate();
        final PearsonSimilarity<String> pearson = new PearsonSimilarity<>(g);
        for (String v : g) {
            if (pearson.variance(v) != 0) {
                Assert.assertEquals("PearsonSimilarityTest.identity",
                        1.0, pearson.similarity(v, v), 1e-4);
            } else {
                Assert.assertTrue("PearsonSimilarityTest.identity",
                        Double.isNaN(pearson.similarity(v, v)));
            }
        }
    }

    /**
     * In a complete graph with self loops all similarities must be NaN
     */
    @Test
    public void complete() {
        final SimpleGraph g = new CompleteGenerator(5).generate();
        for (String v : g) {
            g.addEdge(v, v, 1.0);
        }
        final PearsonSimilarity<String> pearson = new PearsonSimilarity<>(g);
        for (String v : g) {
            Assert.assertTrue("PearsonSimilarityTest.complete", pearson.average(v) == 1.0);
            Assert.assertTrue("PearsonSimilarityTest.complete", pearson.variance(v) == 0.0);
        }
        for (String v : g) {
            for (String w : g) {
                Assert.assertTrue("PearsonSimilarityTest.complete", Double.isNaN(pearson.similarity(v, w)));
            }
        }
    }
}
