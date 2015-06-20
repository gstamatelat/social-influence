package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.helper.Helper;

import java.io.IOException;

public class CheckDraw {
    public static void main(String[] args) throws IOException {
        Graph g = BarabasiAlbert.generate(5, 2, 2, 1.0);

        Move m = new Move();
        m.putVertex(g.getRandomVertex(), 1.0);

        Game game = new Game(g);
        game.setPlayer(PlayerEnum.A, new Move());
        game.setPlayer(PlayerEnum.B, m);

        GameResult gr = game.runGame(null);
        Helper.log("%d", gr.score);
        Helper.log("%s", gr.fullState);
    }
}