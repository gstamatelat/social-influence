package gr.james.socialinfluence.tests;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.players.BruteForcePlayer;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.generators.TwoWheels;
import gr.james.socialinfluence.helper.Helper;

/**
 * <p>Demonstrates how to use the Player class and how to find best moves.</p>
 */
public class PlayerTest {
    public static void main(String[] args) {
        //Graph g = BarabasiAlbert.generate(1000, 3, 3, 1.0);
        Graph g = TwoWheels.generate(6);

        Player p = new BruteForcePlayer();
        Move m = p.setOption("clever", "false").setOption("weight_levels", "10").findMove(g, new GameDefinition(3, 3.0, 10000L, false));
        Helper.log("Final move: " + m.toString());
    }
}