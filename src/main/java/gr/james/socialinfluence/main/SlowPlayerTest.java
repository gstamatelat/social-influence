package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.game.players.SlowPlayer;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertCluster;
import gr.james.socialinfluence.helper.Finals;

public class SlowPlayerTest {
    public static void main(String[] args) {
        Graph g = BarabasiAlbertCluster.generate(MemoryGraph.class, 50, 2, 2, 1.0, 5);
        GameDefinition d = new GameDefinition(5, 5.0, 5000L, true);

        Player slowPlayer = new SlowPlayer();
        Move m = slowPlayer.findMove(g, d);
        Finals.LOG.info("Final move: {}", m);
    }
}