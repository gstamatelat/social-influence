package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.players.MaxPageRankPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.RandomHelper;

public class DirectGame {
    public static void main(String[] args) {
        RandomHelper.initRandom(3724);
        GameDefinition d = new GameDefinition(5, 5.0, 5000L);
        Graph g = new BarabasiAlbertGenerator<>(MemoryGraph.class, 50, 2, 2, 1).create();

        Game game = new Game(g);
        Player p1 = new MaxPageRankPlayer();
        Player p2 = new MaxPageRankPlayer();

        GameResult r = Game.runPlayers(p1, p2, g, d, 0.0);

        Finals.LOG.info("{}", r);
    }
}
