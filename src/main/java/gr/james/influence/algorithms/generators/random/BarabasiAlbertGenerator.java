package gr.james.influence.algorithms.generators.random;

import gr.james.influence.algorithms.generators.basic.CompleteGenerator;
import gr.james.influence.algorithms.scoring.DegreeCentrality;
import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.GraphFactory;
import gr.james.influence.graph.Direction;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;
import gr.james.influence.util.Helper;
import gr.james.influence.util.collections.GraphState;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BarabasiAlbertGenerator implements GraphGenerator {
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
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = new CompleteGenerator(initialClique).generate(factory);

        while (g.vertexCount() < totalVertices) {
            GraphState<V, Integer> degree = DegreeCentrality.execute(g, Direction.INBOUND);

            Map<V, Double> weightMap = new HashMap<>();
            for (V v : degree.keySet()) {
                weightMap.put(v, Math.pow((double) degree.get(v), a));
            }

            Set<V> newVertices = Helper.weightedRandom(weightMap, stepEdges, r);

            V v = g.addVertex();
            for (V w : newVertices) {
                g.addEdges(v, w);
            }
        }

        g.clearMeta();
        g.setMeta(Finals.TYPE_META, "BarabasiAlbert");
        g.setMeta("totalVertices", String.valueOf(totalVertices));
        g.setMeta("initialClique", String.valueOf(initialClique));
        g.setMeta("stepEdges", String.valueOf(stepEdges));
        g.setMeta("a", String.valueOf(a));

        return g;
    }
}
