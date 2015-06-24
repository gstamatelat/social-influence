package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.game.players.*;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.generators.TwoWheels;

import java.util.HashMap;

public class Tests {
    public static void main(String[] args) {
        int maxMoves = 5; // CHANGE THIS
        long[] execution_t = {1000L, 15000L, 30000L}; // CHANGE THIS
        int rounds = 5; // CHANGE THIS (USUALLY NO NEED)

        for (long execution : execution_t) {
            HashMap<Player, Double> players = new HashMap<Player, Double>();
            players.put(new RandomPlayer(), 0.0);
            players.put(new MaxPageRankPlayer(), 0.0);
            players.put(new GreedyPlayer(), 0.0);
            players.put(new BruteForcePlayer(), 0.0);
            players.put(new MaxDegreePlayer(), 0.0);

            for (Player p1 : players.keySet()) {
                for (Player p2 : players.keySet()) {
                    if (p1 != p2) {
                        for (int i = 0; i < rounds; i++) {
                            Graph g = TwoWheels.generate(6); // CHANGE THIS
                            Game game = new Game(g);
                            GameDefinition d = new GameDefinition(maxMoves, maxMoves * 1.0, execution, true);
                            game.setPlayer(PlayerEnum.A, p1.findMove(g, d));
                            game.setPlayer(PlayerEnum.B, p2.findMove(g, d));
                            int result = game.runGame(d).score;
                            if (result == 0) {
                                players.put(p1, players.get(p1) + 0.5);
                                players.put(p2, players.get(p2) + 0.5);
                            } else if (result == -1) {
                                players.put(p1, players.get(p1) + 1.0);
                            } else {
                                players.put(p2, players.get(p2) + 1.0);
                            }
                        }
                    }
                }
            }

            System.out.println("-------------");
            System.out.println("Moves " + maxMoves + ", Execution " + execution / 1000);
            for (Player p : players.keySet()) {
                System.out.println(p.getClass().getSimpleName() + " " + players.get(p));
            }
        }
    }
}