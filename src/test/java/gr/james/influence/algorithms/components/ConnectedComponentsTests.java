package gr.james.influence.algorithms.components;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.api.algorithms.ConnectedComponents;
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
    private final Function<SimpleGraph, ConnectedComponents<String>> factory;

    public ConnectedComponentsTests(Function<SimpleGraph, ConnectedComponents<String>> factory) {
        this.factory = factory;
    }

    @Parameterized.Parameters
    public static Collection<Function<SimpleGraph, ConnectedComponents<String>>> implementations() {
        final Collection<Function<SimpleGraph, ConnectedComponents<String>>> implementations = new ArrayList<>();
        implementations.add(KosarajuComponents::new);
        return implementations;
    }

    /**
     * An empty graph should not have any connected components
     */
    @Test
    public void empty() {
        final SimpleGraph g = new SimpleGraph();
        final ConnectedComponents<String> cc = factory.apply(g);

        Assert.assertTrue("ConnectedComponentsTests.empty", cc.size() == 0);

        api(cc, g);
    }

    /**
     * An isolated graph of n vertices should have exactly n connected components
     */
    @Test
    public void isolated() {
        final int n = 25;

        final SimpleGraph g = new SimpleGraph();
        g.addVertices(n);
        for (int i = 0; i < g.getVerticesCount() - 1; i++) {
            g.addEdge(g.getVertexFromIndex(i), g.getVertexFromIndex(i + 1));
        }

        final ConnectedComponents<String> cc = factory.apply(g);

        // there are n components
        Assert.assertTrue("ConnectedComponentsTests.isolated", cc.size() == n);

        // each component has 1 element
        for (Set<String> c : cc.components()) {
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
        final SimpleGraph g = new CompleteGenerator(n).generate();
        final ConnectedComponents<String> cc = factory.apply(g);

        Assert.assertTrue("ConnectedComponentsTests.complete", cc.size() == 1);

        api(cc, g);
    }

    /**
     * Unconnected clusters
     */
    @Test
    public void clusters() {
        final SimpleGraph g1 = new CompleteGenerator(10).generate();
        final SimpleGraph g2 = new CompleteGenerator(15).generate();
        final SimpleGraph g3 = new CompleteGenerator(20).generate();
        final SimpleGraph g = Graphs.combineGraphs(Arrays.asList(new SimpleGraph[]{g1, g2, g3}));
        final ConnectedComponents<String> cc = factory.apply(g);
        final Set<Set<String>> s = new HashSet<>();
        s.add(new HashSet<>(g1.getVertices()));
        s.add(new HashSet<>(g2.getVertices()));
        s.add(new HashSet<>(g3.getVertices()));
        Assert.assertEquals("ConnectedComponentsTests.clusters", s, cc.components());
    }

    /**
     * Generic tests on random graph
     */
    @Test
    public void random() {
        final int n = 100;
        final SimpleGraph g = new RandomGenerator(n, 0.1, true).generate();
        final ConnectedComponents<String> cc = factory.apply(g);

        api(cc, g);
    }

    private void api(ConnectedComponents<String> cc, SimpleGraph g) {
        // size should be components().size()
        Assert.assertEquals("ConnectedComponentsTests.api", cc.size(), cc.components().size());

        // union of the components equals the graph vertices
        Assert.assertEquals("ConnectedComponentsTests.api",
                cc.components().stream().flatMap(Collection::stream).collect(Collectors.toSet()),
                new HashSet<>(g.getVertices())
        );

        // components cannot be empty
        for (Set<String> s : cc.components()) {
            Assert.assertTrue("ConnectedComponentsTests.api", s.size() > 0);
        }

        // connected() and component() invariant
        for (String v : g.getVertices()) {
            for (String w : g.getVertices()) {
                Assert.assertEquals("ConnectedComponentsTests.api",
                        cc.connected(v, w), cc.component(v).equals(cc.component(w)));
            }
        }

        // component() and components() invariant
        final Set<Set<String>> m = new HashSet<>();
        final Set<String> pending = new HashSet<>(g.getVertices());
        while (pending.size() > 0) {
            final String next = pending.iterator().next();
            final Set<String> component = cc.component(next);
            pending.removeAll(component);
            m.add(component);
        }
        Assert.assertEquals("ConnectedComponentsTests.api", m, cc.components());

        // connected() is commutative
        for (Set<String> s : cc.components()) {
            for (String v : s) {
                for (String w : s) {
                    Assert.assertEquals("ConnectedComponentsTests.api", cc.connected(v, w), cc.connected(w, v));
                }
            }
        }
    }
}