package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.BarabasiAlbertClusterGenerator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.players.SlowPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.Finals;

public class SlowPlayerTest {
    public static void main(String[] args) {
        Graph g = new BarabasiAlbertClusterGenerator<>(MemoryGraph.class, 50, 2, 2, 1.0, 5).create();
        GameDefinition d = new GameDefinition(5, 5.0, 5000L);

        Player slowPlayer = new SlowPlayer();
        Move m = slowPlayer.getMove(g, d);
        Finals.LOG.info("Final move: {}", m);
    }
}
