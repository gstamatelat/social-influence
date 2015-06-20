package gr.james.socialinfluence.main;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.DeGroot;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.generators.Master;

import java.io.IOException;

public class CheckDraw {
    public static void main(String[] args) throws IOException {
        Graph g = Master.generate();
        GraphState state = new GraphState(g);

        Vertex s1 = g.addVertex();
        Vertex s2 = g.addVertex();

        g.addEdge(s1, s1);
        g.addEdge(s2, s2);
        g.addEdge(g.getVertexFromIndex(0), s1);
        g.addEdge(g.getVertexFromIndex(1), s2);

        state.put(g.getVertexFromIndex(3), 0.0);
        state.put(g.getVertexFromIndex(4), 1.0);

        DeGroot.execute(g, state);
    }
}