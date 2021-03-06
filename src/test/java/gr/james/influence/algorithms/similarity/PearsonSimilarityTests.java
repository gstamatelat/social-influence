package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PearsonSimilarity}.
 */
public class PearsonSimilarityTests {
    /**
     * Test for the graph {A->B (1), A->C (1), C->B (2)}.
     */
    @Test
    public void simple() {
        final DirectedGraph<String, Object> g = DirectedGraph.create();
        g.addVertices("A", "B", "C");
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("C", "B");
        final VertexSimilarity<String, Double> pearson = new PearsonSimilarity<>(g);
        final double v1v2 = pearson.similarity("A", "B");
        final double v1v3 = pearson.similarity("A", "C");
        final double v2v3 = pearson.similarity("B", "C");
        Assert.assertEquals("PearsonSimilarityTests.simple", 0.5, v1v3, 1e-4);
        Assert.assertTrue("PearsonSimilarityTests.simple", Double.isNaN(v1v2));
        Assert.assertTrue("PearsonSimilarityTests.simple", Double.isNaN(v2v3));
    }

    /**
     * In a graph with isolated vertices all similarities must be NaN.
     */
    @Test
    public void empty() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();
        g.addVertices(VertexProvider.INTEGER_PROVIDER, 5);
        final VertexSimilarity<Integer, Double> pearson = new PearsonSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertTrue("PearsonSimilarityTests.empty", Double.isNaN(pearson.similarity(v, w)));
            }
        }
    }

    /**
     * The Pearson similarity of neighboring vertices in a circle of length 4 or more is 2 / (2 - n).
     */
    @Test
    public void circle() {
        for (int n = 4; n < 100; n++) {
            final double similarity = 2.0 / (2.0 - n);
            final DirectedGraph<Integer, Object> g = new CycleGenerator<Integer, Object>(n).generate(VertexProvider.INTEGER_PROVIDER);
            final VertexSimilarity<Integer, Double> pearson = new PearsonSimilarity<>(g);
            for (Integer v : g) {
                for (Integer e : g.adjacentOut(v)) {
                    Assert.assertEquals("PearsonSimilarityTests.circle",
                            similarity, pearson.similarity(v, e), 1e-4);
                }
            }
        }
    }

    /**
     * pearson(v1, v2) = pearson(v2, v1).
     */
    @Test
    public void commutativity() {
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> pearson = new PearsonSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("PearsonSimilarityTests.commutativity",
                        pearson.similarity(v, w), pearson.similarity(w, v), 1e-4);
            }
        }
    }

    /**
     * pearson(v, v) = 1.
     */
    @Test
    public void identity() {
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final PearsonSimilarity<Integer> pearson = new PearsonSimilarity<>(g);
        for (Integer v : g) {
            if (pearson.variance(v) != 0) {
                Assert.assertEquals("PearsonSimilarityTests.identity",
                        1.0, pearson.similarity(v, v), 1e-4);
            } else {
                Assert.assertTrue("PearsonSimilarityTests.identity",
                        Double.isNaN(pearson.similarity(v, v)));
            }
        }
    }

    /**
     * In a complete graph with self loops all similarities must be NaN.
     */
    @Test
    public void complete() {
        final DirectedGraph<Integer, Object> g = new CompleteGenerator<Integer, Object>(5).generate(VertexProvider.INTEGER_PROVIDER);
        for (Integer v : g) {
            g.addEdge(v, v, 1.0);
        }
        final PearsonSimilarity<Integer> pearson = new PearsonSimilarity<>(g);
        for (Integer v : g) {
            Assert.assertTrue("PearsonSimilarityTests.complete", pearson.average(v) == 1.0);
            Assert.assertTrue("PearsonSimilarityTests.complete", pearson.variance(v) == 0.0);
        }
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertTrue("PearsonSimilarityTests.complete", Double.isNaN(pearson.similarity(v, w)));
            }
        }
    }
}
