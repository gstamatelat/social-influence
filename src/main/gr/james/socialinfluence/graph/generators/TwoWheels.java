package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

import java.util.Iterator;

public class TwoWheels {
    public static <T extends Graph> Graph generate(Class<T> type, int wheelVertices) {
        Graph g1 = Wheel.generate(type, wheelVertices);
        Graph g2 = Wheel.generate(type, wheelVertices);

        Vertex a, b;
        Iterator<Vertex> it1 = g1.getVertices().iterator();
        Iterator<Vertex> it2 = g2.getVertices().iterator();
        while ((a = it1.next()).getOutDegree() != 3) ;
        while ((b = it2.next()).getOutDegree() != 3) ;

        Graph g = Helper.combineGraphs(new Graph[]{g1, g2});
        g.fuseVertices(new Vertex[]{a, b});

        return g.setName("TwoWheels").setMeta(String.format("%s,wheelVertices=%d", "TwoWheels", wheelVertices));
    }
}