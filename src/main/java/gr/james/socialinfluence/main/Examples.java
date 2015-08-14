package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.ExceptionPlayer;
import gr.james.socialinfluence.game.players.GreedyPlayer;
import gr.james.socialinfluence.game.players.MaxPageRankPlayer;
import gr.james.socialinfluence.game.players.RandomPlayer;
import gr.james.socialinfluence.game.tournament.Tournament;
import gr.james.socialinfluence.graph.MemoryGraph;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Tournament tourney = new Tournament((done, total) -> {
            /*System.out.printf("%d/%d\n", done, total);*/
        });
        tourney.addPlayers(new GreedyPlayer(), new RandomPlayer(), new MaxPageRankPlayer(), new ExceptionPlayer());
        GraphGenerator generator = new BarabasiAlbertGenerator<>(MemoryGraph.class, 100, 2, 2, 1.0);
        GameDefinition d = new GameDefinition(3, 3.0, 1000L);
        tourney.run(generator, d);

        System.out.println();
        System.out.printf("GRAPH:      %s\n", generator.create());
        System.out.printf("DEFINITION: %s\n", d);
        System.out.printf("SCORES:     %s\n", tourney.getScores());
    }
}
