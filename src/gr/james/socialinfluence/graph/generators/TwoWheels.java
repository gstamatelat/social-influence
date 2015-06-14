package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Iterator;

public class TwoWheels {
    public static Graph generate(int wheelVertices) {
        Graph g1 = Wheel.generate(wheelVertices);
        Graph g2 = Wheel.generate(wheelVertices);

        Vertex a, b;
        Iterator<Vertex> it1 = g1.getVertices().iterator();
        Iterator<Vertex> it2 = g2.getVertices().iterator();
        while ((a = it1.next()).getOutDegree() != 3) ;
        while ((b = it2.next()).getOutDegree() != 3) ;

        Graph g = Graph.combineGraphs(new Graph[]{g1, g2});
        g.fuseVertices(new Vertex[]{a, b});

        return g.setName("TwoWheels").setMeta(String.format("%s,wheelVertices=%d", "TwoWheels", wheelVertices));
    }
}