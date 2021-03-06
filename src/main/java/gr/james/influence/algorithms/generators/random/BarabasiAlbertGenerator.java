package gr.james.influence.algorithms.generators.random;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.scoring.DegreeCentrality;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;
import gr.james.sampling.EfraimidisSampling;

import java.util.*;

public class BarabasiAlbertGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    private int totalVertices;
    private int initialClique;
    private int stepEdges;
    private double a;

    public BarabasiAlbertGenerator(int totalVertices, int initialClique, int stepEdges, double a) {
        Conditions.requireArgument(stepEdges <= initialClique, Finals.E_BARABASI_STEP);

        this.totalVertices = totalVertices;
        this.initialClique = initialClique;
        this.stepEdges = stepEdges;
        this.a = a;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = new CompleteGenerator<V, E>(initialClique).generate(r, vertexProvider);

        while (g.vertexCount() < totalVertices) {
            GraphState<V, Integer> degree = DegreeCentrality.execute(g, Direction.INBOUND);

            Map<V, Double> weightMap = new HashMap<>();
            for (V v : degree.keySet()) {
                weightMap.put(v, Math.pow((double) degree.get(v), a));
            }

            EfraimidisSampling<V> wrs = new EfraimidisSampling<>(stepEdges, r);
            wrs.feed(weightMap);
            Set<V> newVertices = new HashSet<>(wrs.sample());
            //Set<V> newVertices = Helper.weightedRandom(weightMap, stepEdges, r);

            V v = g.addVertex(vertexProvider);
            for (V w : newVertices) {
                g.addEdges(v, w);
            }
        }

        return g;
    }
}
