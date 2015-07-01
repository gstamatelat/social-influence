package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.BruteForcePlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.Wheel;

import java.io.IOException;

public class CheckDraw {
    public static void main(String[] args) throws IOException {
        Graph g = Wheel.generate(MemoryGraph.class, 15);
        Player p = new BruteForcePlayer();
        p.findMove(g, new GameDefinition(3, 3.0, 0, false));
    }
}