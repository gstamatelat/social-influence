package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

public class TwoWheels {
    public static Graph generate(int wheelVertices) {
        Graph g1 = Wheel.generate(wheelVertices);
        Graph g2 = Wheel.generate(wheelVertices);
        Vertex a = g1.getVertices().iterator().next();
        Vertex b = g2.getVertices().iterator().next();
        Graph g = Graph.combineGraphs(new Graph[]{g1, g2});
        g.fuseVertices(new Vertex[]{a, b});

        return g.setName("TwoWheels").setMeta(String.format("%s,wheelVertices=%d", "TwoWheels", wheelVertices));
    }
}
