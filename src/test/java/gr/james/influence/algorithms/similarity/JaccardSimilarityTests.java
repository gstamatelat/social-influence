package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
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
        final DirectedGraph<String, Object> g = DirectedGraph.create();
        g.addVertices("A", "B", "C");
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("C", "B");
        final VertexSimilarity<String, Double> jaccard = new JaccardSimilarity<>(g);
        Assert.assertEquals("JaccardSimilarityTests.simple", 0.0, jaccard.similarity("A", "B"), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 0.5, jaccard.similarity("A", "C"), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 0.0, jaccard.similarity("B", "C"), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 1.0, jaccard.similarity("A", "A"), 1e-4);
        Assert.assertEquals("JaccardSimilarityTests.simple", 1.0, jaccard.similarity("C", "C"), 1e-4);
        Assert.assertTrue("JaccardSimilarityTests.simple", Double.isNaN(jaccard.similarity("B", "B")));
    }

    /**
     * In a graph with isolated vertices all similarities must be NaN.
     */
    @Test
    public void empty() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();
        g.addVertices(VertexProvider.INTEGER_PROVIDER, 5);
        final VertexSimilarity<Integer, Double> jaccard = new JaccardSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
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
            final DirectedGraph<Integer, Object> g = new CycleGenerator<Integer, Object>(n).generate(VertexProvider.INTEGER_PROVIDER);
            final VertexSimilarity<Integer, Double> jaccard = new JaccardSimilarity<>(g);
            for (Integer v : g) {
                for (Integer e : g.adjacentOut(v)) {
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
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> jaccard = new JaccardSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
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
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> jaccard = new JaccardSimilarity<>(g);
        for (Integer v : g) {
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
        final DirectedGraph<Integer, Object> g = new CompleteGenerator<Integer, Object>(5).generate(VertexProvider.INTEGER_PROVIDER);
        for (Integer v : g) {
            g.addEdge(v, v, 1.0);
        }
        final VertexSimilarity<Integer, Double> jaccard = new JaccardSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("JaccardSimilarityTests.complete", 1.0, jaccard.similarity(v, w), 1e-4);
            }
        }
    }
}
