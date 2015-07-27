package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphOperations;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;

import java.util.Iterator;

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

        Vertex a, b;
        Iterator<Vertex> it1 = g1.getVertices().iterator();
        Iterator<Vertex> it2 = g2.getVertices().iterator();

        //noinspection StatementWithEmptyBody
        while (g1.getOutDegree((a = it1.next())) != 3) ;
        //noinspection StatementWithEmptyBody
        while (g2.getOutDegree((b = it2.next())) != 3) ;

        T g = GraphOperations.combineGraphs(type, new Graph[]{g1, g2});
        GraphOperations.fuseVertices(g, new Vertex[]{a, b});

        g.setMeta(Finals.DEFAULT_TYPE_META, "TwoWheels")
                .setMeta("wheelVertices", String.valueOf(wheelVertices));

        return g;
    }
}
