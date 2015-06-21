package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.*;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.io.Csv;
import gr.james.socialinfluence.helper.Helper;

import java.io.IOException;
import java.net.URL;

public class CheckDraw {
    public static void main(String[] args) throws IOException {
        Graph g = Csv.from(new URL("http://loki.ee.duth.gr/school.csv").openStream());

        Game game = new Game(g);

        Move m1 = new Move();
        m1.putVertex(g.getVertexFromIndex(56), 1.017341);
        m1.putVertex(g.getVertexFromIndex(191), 0.982659);

        Move m2 = new Move();
        m2.putVertex(g.getVertexFromIndex(56), 0.830000);
        m2.putVertex(g.getVertexFromIndex(237), 1.170000);

        GameDefinition d = new GameDefinition(2, 2.0, 0, false);

        game.setPlayer(PlayerEnum.A, m1);
        game.setPlayer(PlayerEnum.B, m2);

        GameResult gr = game.runGame(d);

        Helper.log("%s", gr);
    }
}