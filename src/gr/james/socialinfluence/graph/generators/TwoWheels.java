package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

public class TwoWheels {
    public static Graph generate(int wheelVertices) {
        Graph g1 = Wheel.generate(wheelVertices);
        Graph g2 = Wheel.generate(wheelVertices);
        Vertex a = null;
        Vertex b = null;
        for (Vertex v : g1.getVertices()) {
            if (v.getOutDegree() == 3) {
                a = v;
                break;
            }
        }
        for (Vertex v : g2.getVertices()) {
            if (v.getOutDegree() == 3) {
                b = v;
                break;
            }
        }
        Graph g = Graph.combineGraphs(new Graph[]{g1, g2});
        g.fuseVertices(new Vertex[]{a, b});

        return g.setName("TwoWheels").setMeta(String.format("%s,wheelVertices=%d", "TwoWheels", wheelVertices));
    }
}