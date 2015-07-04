package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.GraphOperations;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Iterator;

public class TwoWheels {
    public static <T extends Graph> Graph generate(Class<T> type, int wheelVertices) {
        WheelGenerator wheelGenerator = new WheelGenerator<>(type, wheelVertices);
        Graph g1 = wheelGenerator.create();
        Graph g2 = wheelGenerator.create();

        Vertex a, b;
        Iterator<Vertex> it1 = g1.getVertices().iterator();
        Iterator<Vertex> it2 = g2.getVertices().iterator();
        while (g1.getOutDegree((a = it1.next())) != 3) ;
        while (g2.getOutDegree((b = it2.next())) != 3) ;

        Graph g = GraphOperations.combineGraphs(MemoryGraph.class, new Graph[]{g1, g2});
        GraphOperations.fuseVertices(g, new Vertex[]{a, b});

        return g.setMeta("name", "TwoWheels")
                .setMeta("wheelVertices", String.valueOf(wheelVertices));
    }
}
