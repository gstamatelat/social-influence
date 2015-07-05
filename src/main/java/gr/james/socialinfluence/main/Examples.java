package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.ExceptionPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertGenerator;

public class Examples {
    public static void main(String[] args) {
        GraphGenerator generator = new BarabasiAlbertGenerator<>(MemoryGraph.class, 25, 2, 2, 1.0);
        Graph scaleFreeGraph = generator.create();
        Player p = new ExceptionPlayer();
        p.getMove(scaleFreeGraph, new GameDefinition(2, 2.0, 1000));
    }
}
