package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;

public class TwoWheelsGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int wheelVertices;

    public TwoWheelsGenerator(Class<T> type, int wheelVertices) {
        this.type = type;
        this.wheelVertices = wheelVertices;
    }

    @Override
    public T create() {
        WheelGenerator wheelGenerator = new WheelGenerator<>(type, wheelVertices);
        Graph g1 = wheelGenerator.create();
        Graph g2 = wheelGenerator.create();

        Vertex a = g1.getVertexFromIndex(0);
        Vertex b = g2.getVertexFromIndex(0);

        T g = GraphUtils.combineGraphs(type, new Graph[]{g1, g2});
        GraphUtils.fuseVertices(g, new Vertex[]{a, b});

        g.setMeta(Finals.TYPE_META, "TwoWheels")
                .setMeta("wheelVertices", String.valueOf(wheelVertices));

        return g;
    }
}
