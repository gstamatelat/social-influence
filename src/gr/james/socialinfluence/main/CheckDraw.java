package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.game.players.GreedyPlayer;
import gr.james.socialinfluence.graph.generators.Path;
import gr.james.socialinfluence.graph.Graph;

public class CheckDraw {
    public static void main(String[] args) {
        Graph g = Path.generate(11, false);
        Player p = new GreedyPlayer();

        Game game = new Game(g);
        GameDefinition d = new GameDefinition(5, 5.0, 10000L, false);
        game.setPlayer(PlayerEnum.A, p.findMove(g, d));
        game.setPlayer(PlayerEnum.B, p.findMove(g, d));
        System.out.println(game.runGame(d));
    }
}
