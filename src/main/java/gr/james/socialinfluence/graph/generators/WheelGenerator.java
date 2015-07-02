package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

public class WheelGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int totalVertices;

    private T g;

    public WheelGenerator(Class<T> type, int totalVertices) {
        this.type = type;
        this.totalVertices = totalVertices;
        reset();
    }

    private void reset() {
        g = Helper.instantiateGeneric(type);
        g.setMeta("name", "WheelGenerator")
                .setMeta("totalVertices", String.valueOf(totalVertices));
    }

    @Override
    public T create() {
        reset();

        // TODO: At this point, the meta field is for the cycle graph
        g = new CycleGenerator<>(type, totalVertices - 1).create();
        Vertex n = g.addVertex();
        for (Vertex v : g.getVertices()) {
            if (!v.equals(n)) {
                g.addEdge(v, n, true);
            }
        }

        return g;
    }
}