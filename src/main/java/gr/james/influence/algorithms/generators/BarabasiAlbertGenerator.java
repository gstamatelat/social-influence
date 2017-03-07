package gr.james.influence.algorithms.generators;

import gr.james.influence.algorithms.scoring.Degree;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphGenerator;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Vertex;
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
    public <T extends Graph> T generate(GraphFactory<T> factory, Random r) {
        T g = new CompleteGenerator(initialClique).generate(factory);

        while (g.getVerticesCount() < totalVertices) {
            GraphState<Integer> degree = Degree.execute(g, Direction.INBOUND);

            Map<Vertex, Double> weightMap = new HashMap<>();
            for (Vertex v : degree.keySet()) {
                weightMap.put(v, Math.pow((double) degree.get(v), a));
            }

            Set<Vertex> newVertices = Helper.weightedRandom(weightMap, stepEdges, r);

            Vertex v = g.addVertex();
            for (Vertex w : newVertices) {
                g.addEdges(v, w);
            }
        }

        g.clearMeta();
        g.setGraphType("BarabasiAlbert");
        g.setMeta("totalVertices", String.valueOf(totalVertices));
        g.setMeta("initialClique", String.valueOf(initialClique));
        g.setMeta("stepEdges", String.valueOf(stepEdges));
        g.setMeta("a", String.valueOf(a));

        return g;
    }
}
