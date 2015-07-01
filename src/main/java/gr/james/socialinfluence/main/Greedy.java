package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.players.GreedyPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertCluster;
import gr.james.socialinfluence.helper.Finals;

public class Greedy {
    public static void main(String[] args) {
        /* You only need to change these lines.
        * Pick the graph type and number of moves.
        * If you change the graph type remember to update any imports */
        //Graph g = TwoWheels.generate();
        Graph g = BarabasiAlbertCluster.generate(MemoryGraph.class, 50, 2, 2, 1.0, 5);
        //Graph g = Path.generate(12, true);
        //Graph g = Path.generate(10, false);
        //Graph g = BarabasiAlbert.generate(50,2);
        int movesCount = 5;

        Player greedyPlayer = new GreedyPlayer();
        Move m = greedyPlayer.findMove(g, new GameDefinition(movesCount, (double) movesCount, 60000L, false));
        Finals.LOG.info("Final move: {}", m);
    }
}