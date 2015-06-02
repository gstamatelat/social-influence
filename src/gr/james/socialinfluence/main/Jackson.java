package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.BruteForcePlayer;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.generators.TwoWheels;

public class Jackson {
    public static void main(String[] args) {
        /*Graph g = new Graph();
        Vertex a = g.addVertex();
        Vertex b = g.addVertex();
        Vertex c = g.addVertex();
        Vertex d = g.addVertex();
        a.addEdge(b);
        b.addEdge(a);
        b.addEdge(c);
        c.addEdge(b);
        c.addEdge(d);
        d.addEdge(c);
        d.addEdge(a);
        a.addEdge(d);
        b.addEdge(d).setWeight(2.0);
        d.addEdge(b).setWeight(2.0);
        BruteForcePlayer player = new BruteForcePlayer();
        player.findMove(g, 4, 4.0, 60000L, true);*/
        Graph g = TwoWheels.generate(7);
        BruteForcePlayer player = new BruteForcePlayer();
        player.findMove(g, new GameDefinition(7, 7.0, 60000L, false));
    }
}