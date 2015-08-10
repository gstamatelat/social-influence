package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;

public class WheelGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int totalVertices;

    public WheelGenerator(Class<T> type, int totalVertices) {
        this.type = type;
        this.totalVertices = totalVertices;
    }

    @Override
    public T create() {
        T g = new CycleGenerator<>(type, totalVertices - 1).create();

        Vertex n = g.addVertex();
        for (Vertex v : g) {
            if (!v.equals(n)) {
                g.addEdge(v, n, true);
            }
        }

        g.clearMeta()
                .setMeta(Finals.TYPE_META, "Wheel")
                .setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
