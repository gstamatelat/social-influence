package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.game.players.MaxPageRankPlayer;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.game.players.RandomPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.helper.RandomHelper;

public class DirectGame {
    public static void main(String[] args) {
        while (true) {
            RandomHelper.initRandom(3724);
            GameDefinition d = new GameDefinition(5, 5.0, 5000L, true);
            MemoryGraph g = BarabasiAlbert.generate(25, 2, 2, 1);
            Player p1 = new MaxPageRankPlayer();
            Player p2 = new RandomPlayer();
            Game game = new Game(g);
            game.setPlayer(PlayerEnum.A, p1.findMove(g, d));
            game.setPlayer(PlayerEnum.B, p2.findMove(g, d));
            Helper.log("%s", game.runGame(d, 0.01));
            return;
        }
    }
}