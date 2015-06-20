package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.game.players.MaxPageRankPlayer;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.game.players.RandomPlayer;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;

public class DirectGame {
    public static void main(String[] args) {
        while (true) {
            GameDefinition d = new GameDefinition(5, 5.0, 5000L, true);
            Graph g = BarabasiAlbert.generate(25, 2, 2, 1);
            Player p1 = new MaxPageRankPlayer();
            Player p2 = new RandomPlayer();
            Game game = new Game(g);
            game.setPlayer(PlayerEnum.A, p1.findMove(g, d));
            game.setPlayer(PlayerEnum.B, p2.findMove(g, d));
            game.runGame(d);
            return;
        }
    }
}