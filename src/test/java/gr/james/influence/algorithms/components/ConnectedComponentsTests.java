package gr.james.influence.algorithms.components;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.basic.DirectedPathGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class ConnectedComponentsTests {
    private final Function<Graph<Integer, Object>, ConnectedComponents<Integer>> factory;

    public ConnectedComponentsTests(Function<Graph<Integer, Object>, ConnectedComponents<Integer>> factory) {
        this.factory = factory;
    }

    @Parameterized.Parameters
    public static Collection<Function<Graph<Integer, Object>, ConnectedComponents<Integer>>> implementations() {
        final Collection<Function<Graph<Integer, Object>, ConnectedComponents<Integer>>> implementations = new ArrayList<>();
        implementations.add(KosarajuComponents::new);
        return implementations;
    }

    /**
     * An empty graph should not have any connected components
     */
    @Test
    public void empty() {
        final SimpleGraph g = new SimpleGraph();
        final ConnectedComponents<Integer> cc = factory.apply(g);

        Assert.assertTrue("ConnectedComponentsTests.empty", cc.size() == 0);

        api(cc, g);
    }

    /**
     * An isolated graph of n vertices should have exactly n connected components
     */
    @Test
    public void isolated() {
        final int n = 25;

        final Graph<Integer, Object> g = new DirectedPathGenerator<>(n).generate();

        final ConnectedComponents<Integer> cc = factory.apply(g);

        // there are n components
        Assert.assertTrue("ConnectedComponentsTests.isolated", cc.size() == n);

        // each component has 1 element
        for (Set<Integer> c : cc.components()) {
            Assert.assertTrue("ConnectedComponentsTests.isolated", c.size() == 1);
        }

        api(cc, g);
    }

    /**
     * In a complete graph only one component should exist
     */
    @Test
    public void complete() {
        final int n = 25;
        final Graph<Integer, Object> g = new CompleteGenerator<>(n).generate();
        final ConnectedComponents<Integer> cc = factory.apply(g);

        Assert.assertTrue("ConnectedComponentsTests.complete", cc.size() == 1);

        api(cc, g);
    }

    /**
     * Unconnected clusters
     */
    @Test
    public void clusters() {
        final Graph<Integer, Object> g1 = new CompleteGenerator<>(10).generate();
        final Graph<Integer, Object> g2 = new CompleteGenerator<>(15).generate();
        final Graph<Integer, Object> g3 = new CompleteGenerator<>(20).generate();
        final Graph<Integer, Object> g = Graphs.combineGraphs(Arrays.asList(g1, g2, g3));
        final ConnectedComponents<Integer> cc = factory.apply(g);
        final Set<Set<Integer>> s = new HashSet<>();
        s.add(g1.vertexSet());
        s.add(g2.vertexSet());
        s.add(g3.vertexSet());
        Assert.assertEquals("ConnectedComponentsTests.clusters", s, cc.components());
    }

    /**
     * Generic tests on random graph
     */
    @Test
    public void random() {
        final int n = 100;
        final Graph<Integer, Object> g = new RandomGenerator<>(n, 0.1, true).generate();
        final ConnectedComponents<Integer> cc = factory.apply(g);

        api(cc, g);
    }

    private void api(ConnectedComponents<Integer> cc, Graph<Integer, Object> g) {
        // size should be components().size()
        Assert.assertEquals("ConnectedComponentsTests.api", cc.size(), cc.components().size());

        // union of the components equals the graph vertices
        Assert.assertEquals("ConnectedComponentsTests.api",
                cc.components().stream().flatMap(Collection::stream).collect(Collectors.toSet()),
                g.vertexSet()
        );

        // components cannot be empty
        for (Set<Integer> s : cc.components()) {
            Assert.assertTrue("ConnectedComponentsTests.api", s.size() > 0);
        }

        // connected() and component() invariant
        for (Integer v : g.vertexSet()) {
            for (Integer w : g.vertexSet()) {
                Assert.assertEquals("ConnectedComponentsTests.api",
                        cc.connected(v, w), cc.component(v).equals(cc.component(w)));
            }
        }

        // component() and components() invariant
        final Set<Set<Integer>> m = new HashSet<>();
        final Set<Integer> pending = new HashSet<>(g.vertexSet());
        while (pending.size() > 0) {
            final Integer next = pending.iterator().next();
            final Set<Integer> component = cc.component(next);
            pending.removeAll(component);
            m.add(component);
        }
        Assert.assertEquals("ConnectedComponentsTests.api", m, cc.components());

        // connected() is commutative
        for (Set<Integer> s : cc.components()) {
            for (Integer v : s) {
                for (Integer w : s) {
                    Assert.assertEquals("ConnectedComponentsTests.api", cc.connected(v, w), cc.connected(w, v));
                }
            }
        }
    }
}
