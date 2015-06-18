package gr.james.socialinfluence.main;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.BruteForcePlayer;
import gr.james.socialinfluence.game.players.Player;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.io.Csv;

import java.io.IOException;
import java.net.URL;

public class CheckDraw {
    public static void main(String[] args) throws IOException {
        Graph g = Csv.from(new URL("YOUNOTAKECANDLE").openStream());

        Player p = new BruteForcePlayer().setOption("epsilon", "0.0001").setOption("weight_levels", "1");
        p.findMove(g, new GameDefinition(3, 3.0, 0, false));
    }
}