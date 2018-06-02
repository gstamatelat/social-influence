package gr.james.influence.graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GraphsTests {
    /**
     * Test the {@link Graphs#getStubbornComponents(DirectedGraph)} method on a manually created graph.
     */
    @Test
    public void stubbornComponentsManual() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();

        g.addVertices(1, 2, 3, 4, 5);

        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        g.addEdge(4, 5);
        g.addEdge(5, 4);

        g.addEdge(4, 1);
        g.addEdge(5, 3);

        Set<Set<Integer>> stubbornComponents = Graphs.getStubbornComponents(g);

        Assert.assertEquals("GraphsTests.stubbornComponentsManual", 1, stubbornComponents.size());
        Assert.assertEquals("GraphsTests.stubbornComponentsManual",
                new HashSet<>(Arrays.asList(1, 2, 3)), stubbornComponents.iterator().next());
    }

    /**
     * Test the {@link Graphs#getStubbornComponents(DirectedGraph)} method on a graph without any stubborn vertices.
     */
    @Test
    public void stubbornComponentsStrong() {
        final DirectedGraph<Integer, Object> g = DirectedGraph.create();

        g.addVertices(1, 2, 3);

        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        Set<Set<Integer>> stubbornComponents = Graphs.getStubbornComponents(g);

        Assert.assertEquals("GraphsTests.stubbornComponentsStrong", 1, stubbornComponents.size());
        Assert.assertEquals("GraphsTests.stubbornComponentsStrong",
                new HashSet<>(Arrays.asList(1, 2, 3)), stubbornComponents.iterator().next());
    }
}
