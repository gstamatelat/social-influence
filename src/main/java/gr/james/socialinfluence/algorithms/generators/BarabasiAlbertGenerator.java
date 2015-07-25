package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.algorithms.scoring.Degree;
import gr.james.socialinfluence.api.AbstractEvolvingGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.GraphException;
import gr.james.socialinfluence.util.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarabasiAlbertGenerator<T extends Graph> extends AbstractEvolvingGenerator<T> {
    private Class<T> type;
    private int totalVertices;
    private int initialClique;
    private int stepEdges;
    private double a;

    private T g;

    public BarabasiAlbertGenerator(Class<T> type, int totalVertices, int initialClique, int stepEdges, double a) {
        if (stepEdges > initialClique) {
            throw new GraphException(Finals.E_BARABASI_STEP);
        }

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

        List<Vertex> newVertices = Helper.weightedRandom(weightMap, stepEdges);

        Vertex v = g.addVertex();
        for (Vertex w : newVertices) {
            g.addEdge(v, w, true);
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
        g.clearMeta()
                .setMeta("type", "BarabasiAlbert")
                .setMeta("totalVertices", String.valueOf(totalVertices))
                .setMeta("initialClique", String.valueOf(initialClique))
                .setMeta("stepEdges", String.valueOf(stepEdges))
                .setMeta("a", String.valueOf(a));
    }
}
