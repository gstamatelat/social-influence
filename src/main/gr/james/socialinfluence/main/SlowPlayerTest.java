package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.game.players.SlowPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertCluster;
import gr.james.socialinfluence.helper.Helper;

public class SlowPlayerTest {
    public static void main(String[] args) {
        MemoryGraph g = BarabasiAlbertCluster.generate(50, 2, 2, 1.0, 5);
        GameDefinition d = new GameDefinition(5, 5.0, 5000L, true);

        Player slowPlayer = new SlowPlayer();
        Move m = slowPlayer.findMove(g, d);
        Helper.log("Final move: %s", m);
    }
}