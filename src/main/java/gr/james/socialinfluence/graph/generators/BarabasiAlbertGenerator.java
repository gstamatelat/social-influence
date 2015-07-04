package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.Degree;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.WeightedRandom;

import java.util.HashMap;
import java.util.List;

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
        HashMap<Vertex, Double> weightMap = Degree.execute(g, true);
        for (Vertex v : weightMap.keySet()) {
            weightMap.put(v, Math.pow(weightMap.get(v), a));
        }
        List<Vertex> newVertices = WeightedRandom.makeRandomSelection(weightMap, stepEdges);

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
        g = new CliqueGenerator<>(type, initialClique).create();
        g.clearMeta()
                .setMeta("type", "BarabasiAlbert")
                .setMeta("totalVertices", String.valueOf(totalVertices))
                .setMeta("initialClique", String.valueOf(initialClique))
                .setMeta("stepEdges", String.valueOf(stepEdges))
                .setMeta("a", String.valueOf(a));
    }
}
