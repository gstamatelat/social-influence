package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Iterator;

public class TwoWheels {
    public static Graph generate(int wheelVertices) {
        MemoryGraph g1 = (MemoryGraph) Wheel.generate(wheelVertices);
        MemoryGraph g2 = (MemoryGraph) Wheel.generate(wheelVertices);

        Vertex a, b;
        Iterator<Vertex> it1 = g1.getVertices().iterator();
        Iterator<Vertex> it2 = g2.getVertices().iterator();
        while ((a = it1.next()).getOutDegree() != 3) ;
        while ((b = it2.next()).getOutDegree() != 3) ;

        MemoryGraph g = MemoryGraph.combineGraphs(new MemoryGraph[]{g1, g2});
        g.fuseVertices(new Vertex[]{a, b});

        return g.setName("TwoWheels").setMeta(String.format("%s,wheelVertices=%d", "TwoWheels", wheelVertices));
    }
}