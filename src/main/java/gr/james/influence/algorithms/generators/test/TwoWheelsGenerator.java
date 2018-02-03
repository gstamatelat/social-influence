package gr.james.influence.algorithms.generators.test;

import gr.james.influence.algorithms.generators.basic.WheelGenerator;
import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A generator that produces undirected two-wheel graphs.
 * <p>
 * The identification of this generator is:
 * <ul>
 * <li>center: the center of the graph</li>
 * <li>hub1: the hub of one of the wheels</li>
 * <li>hub2: the hub of the other wheel</li>
 * </ul>
 */
public class TwoWheelsGenerator<V, E> implements GraphGenerator<Graph<V, E>, V, E> {
    private final int wheelVertices;

    /**
     * Construct a new {@link TwoWheelsGenerator}.
     *
     * @param wheelVertices the vertex count on each wheel
     * @throws IllegalArgumentException if {@code vertexCount < 4}
     */
    public TwoWheelsGenerator(int wheelVertices) {
        if (wheelVertices < 4) {
            throw new IllegalArgumentException();
        }
        this.wheelVertices = wheelVertices;
    }

    @Override
    public Graph<V, E> generate(Random r,
                                VertexProvider<V> vertexProvider,
                                Map<String, V> identification) {
        final WheelGenerator<V, E> wheelGenerator = new WheelGenerator<>(wheelVertices);
        final Map<String, V> identification1 = new HashMap<>();
        final Map<String, V> identification2 = new HashMap<>();
        final Graph<V, E> g1 = wheelGenerator.generate(r, vertexProvider, identification1);
        final Graph<V, E> g2 = wheelGenerator.generate(r, vertexProvider, identification2);
        final V hub1 = identification1.get("hub");
        final V hub2 = identification2.get("hub");

        final V a = Graphs.findVertex(g1, v -> !v.equals(hub1));
        final V b = Graphs.findVertex(g2, v -> !v.equals(hub2));
        assert a != null;
        assert b != null;

        final Graph<V, E> g = Graphs.combineGraphs(Arrays.asList(g1, g2));
        final V center = Graphs.fuseVertices(g, Arrays.asList(a, b), vertexProvider);

        identification.put("center", center);
        identification.put("hub1", hub1);
        identification.put("hub2", hub2);

        assert g.vertexCount() == 2 * wheelVertices - 1;

        return g;
    }
}
