package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.graph.generators.Path;
import gr.james.socialinfluence.graph.Graph;

public class DirectGame {
    public static void main(String[] args) {
        Graph graph = Path.generate(10, true);
        Game game = new Game(graph);

        Move a = new Move();
        a.putVertex(graph.getVertexFromId(2), 1.0);
        a.putVertex(graph.getVertexFromId(3), 1.0);
        a.putVertex(graph.getVertexFromId(8), 1.0);

        Move b = new Move();
        b.putVertex(graph.getVertexFromId(1), 1.0);
        b.putVertex(graph.getVertexFromId(3), 1.0);
        b.putVertex(graph.getVertexFromId(5), 1.0);

        Move c = new Move();
        c.putVertex(graph.getVertexFromId(1), 1.0);
        c.putVertex(graph.getVertexFromId(6), 1.0);
        c.putVertex(graph.getVertexFromId(10), 1.0);

        game.setPlayer(PlayerEnum.A, a).setPlayer(PlayerEnum.B, b);
        System.out.println(game.runGame(null));
        //System.out.println(game.swapPlayers().runGame().getMean());

        game.setPlayer(PlayerEnum.A, b).setPlayer(PlayerEnum.B, c);
        System.out.println(game.runGame(null));
        //System.out.println(game.swapPlayers().runGame().getMean());

        game.setPlayer(PlayerEnum.A, a).setPlayer(PlayerEnum.B, c);
        System.out.println(game.runGame(null));
        //System.out.println(game.swapPlayers().runGame().getMean());
    }
}