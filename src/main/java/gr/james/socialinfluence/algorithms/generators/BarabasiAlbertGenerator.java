package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.algorithms.scoring.Degree;
import gr.james.socialinfluence.api.EvolvingGraphGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BarabasiAlbertGenerator<T extends Graph> implements EvolvingGraphGenerator<T> {
    private Class<T> type;
    private int totalVertices;
    private int initialClique;
    private int stepEdges;
    private double a;

    private T g;

    public BarabasiAlbertGenerator(Class<T> type, int totalVertices, int initialClique, int stepEdges, double a) {
        Conditions.requireArgument(stepEdges <= initialClique, Finals.E_BARABASI_STEP);

        this.type = type;
        this.totalVertices = totalVertices;
        this.initialClique = initialClique;
        this.stepEdges = stepEdges;
        this.a = a;

        reset();
    }

    @Override
    public T evolve() {
        GraphState<Integer> degree = Degree.execute(g, true);

        Map<Vertex, Double> weightMap = new HashMap<>();
        for (Vertex v : degree.keySet()) {
            weightMap.put(v, Math.pow((double) degree.get(v), a));
        }

        Set<Vertex> newVertices = Helper.weightedRandom(weightMap, stepEdges);

        Vertex v = g.addVertex();
        for (Vertex w : newVertices) {
            g.addEdges(v, w);
        }

        return g;
    }

    @Override
    public boolean canEvolve() {
        return (g.getVerticesCount() < totalVertices);
    }

    @Override
    public void reset() {
        g = new CompleteGenerator<>(type, initialClique).create();
        g.clearMeta();
        g.setGraphType("BarabasiAlbert");
        g.setMeta("totalVertices", String.valueOf(totalVertices));
        g.setMeta("initialClique", String.valueOf(initialClique));
        g.setMeta("stepEdges", String.valueOf(stepEdges));
        g.setMeta("a", String.valueOf(a));
    }
}
