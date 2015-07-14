package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.ExceptionPlayer;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.graph.generators.RandomGenerator;
import gr.james.socialinfluence.graph.io.Dot;

import java.io.IOException;
import java.util.Set;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new MemoryGraph();
        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        Vertex v3 = g.addVertex();
        Vertex v4 = g.addVertex();
        Vertex v5 = g.addVertex();
        Vertex v6 = g.addVertex();
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v1, v4);
        g.addEdge(v4, v5);
        g.addEdge(v5, v6);
        g.addEdge(v2, v5);
        g.addEdge(v3, v6);

        Dot dot = new Dot();
        dot.to(g, System.out);
    }
}
