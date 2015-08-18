package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.PathGenerator;
import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.players.MaxPageRankPlayer;
import gr.james.socialinfluence.game.players.RandomPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.Finals;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        GameResult r = Game.runPlayers(new MaxPageRankPlayer(), new RandomPlayer(),
                new PathGenerator<>(MemoryGraph.class, 5).create(), new GameDefinition(2, 2.0, 5000L), 0.0);
        Finals.LOG.info("{}", r);
    }
}
