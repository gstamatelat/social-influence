package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.CycleGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.game.players.BruteForcePlayer;
import gr.james.socialinfluence.graph.MemoryGraph;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new CycleGenerator<>(MemoryGraph.class, 3).create();
        Player p = new BruteForcePlayer();
        p.getMove(g, new GameDefinition(3, 3.0, 0));
    }
}
