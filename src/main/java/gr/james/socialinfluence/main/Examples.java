package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.CycleGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.MemoryGraph;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new CycleGenerator<>(MemoryGraph.class, 3).create();
        /*for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            e.getValue().setWeight(RandomHelper.getRandom().nextDouble());
        }*/

        Game game = new Game(g);

        Move m1 = new Move();
        m1.putVertex(g.getVertexFromIndex(0), 1.5);
        m1.putVertex(g.getVertexFromIndex(1), 1.5);
        /*m1.putVertex(g.getVertexFromIndex(2), 0.02);*/

        Move m2 = new Move();
        /*m2.putVertex(g.getVertexFromIndex(0), 0.5);
        m2.putVertex(g.getVertexFromIndex(1), 0.5);
        m2.putVertex(g.getVertexFromIndex(2), 2.0);*/
        m2.putVertex(g.getVertexFromIndex(0), 0.5);
        m2.putVertex(g.getVertexFromIndex(1), 0.5);
        m2.putVertex(g.getVertexFromIndex(2), 2.0);

        game.setPlayerAMove(m1);
        game.setPlayerBMove(m2);

        GameResult r = game.runGame(new GameDefinition(3, 3.0, 50000L), 0.0);
        System.out.println(r);
        System.out.println(r.fullState);
    }
}
