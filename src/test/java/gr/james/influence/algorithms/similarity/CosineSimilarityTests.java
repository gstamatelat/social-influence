package gr.james.influence.algorithms.similarity;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.CycleGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link CosineSimilarity}.
 */
public class CosineSimilarityTests {
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
        final VertexSimilarity<String, Double> cosine = new CosineSimilarity<>(g);
        Assert.assertTrue("CosineSimilarityTests.simple", Double.isNaN(cosine.similarity("A", "B")));
        Assert.assertEquals("CosineSimilarityTests.simple", 1.0 / Math.sqrt(2), cosine.similarity("A", "C"), 1e-4);
        Assert.assertTrue("CosineSimilarityTests.simple", Double.isNaN(cosine.similarity("B", "C")));
        Assert.assertEquals("CosineSimilarityTests.simple", 1.0, cosine.similarity("A", "A"), 1e-4);
        Assert.assertEquals("CosineSimilarityTests.simple", 1.0, cosine.similarity("C", "C"), 1e-4);
        Assert.assertTrue("CosineSimilarityTests.simple", Double.isNaN(cosine.similarity("B", "B")));
    }

    /**
     * In a graph with isolated vertices all similarities must be NaN.
     */
    @Test
    public void empty() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();
        g.addVertices(5, VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> cosine = new CosineSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertTrue("CosineSimilarityTests.empty", Double.isNaN(cosine.similarity(v, w)));
            }
        }
    }

    /**
     * The Cosine similarity of neighboring vertices in a circle of length 4 or more is 0.
     */
    @Test
    public void circle() {
        for (int n = 4; n < 100; n++) {
            final DirectedGraph<Integer, Object> g = new CycleGenerator<Integer, Object>(n).generate(VertexProvider.INTEGER_PROVIDER);
            final VertexSimilarity<Integer, Double> cosine = new CosineSimilarity<>(g);
            for (Integer v : g) {
                for (Integer e : g.adjacentOut(v)) {
                    Assert.assertEquals("CosineSimilarityTests.circle",
                            0, cosine.similarity(v, e), 1e-4);
                }
            }
        }
    }

    /**
     * cosine(v1, v2) = cosine(v2, v1).
     */
    @Test
    public void commutativity() {
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> cosine = new CosineSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("CosineSimilarityTests.commutativity",
                        cosine.similarity(v, w), cosine.similarity(w, v), 1e-4);
            }
        }
    }

    /**
     * cosine(v, v) = 1.
     */
    @Test
    public void identity() {
        final DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(100, 0.1).generate(VertexProvider.INTEGER_PROVIDER);
        final VertexSimilarity<Integer, Double> cosine = new CosineSimilarity<>(g);
        for (Integer v : g) {
            if (g.outDegree(v) != 0) {
                Assert.assertEquals("CosineSimilarityTests.identity",
                        1.0, cosine.similarity(v, v), 1e-4);
            } else {
                Assert.assertTrue("CosineSimilarityTests.identity",
                        Double.isNaN(cosine.similarity(v, v)));
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
        final VertexSimilarity<Integer, Double> cosine = new CosineSimilarity<>(g);
        for (Integer v : g) {
            for (Integer w : g) {
                Assert.assertEquals("CosineSimilarityTests.complete", 1.0, cosine.similarity(v, w), 1e-4);
            }
        }
    }
}
