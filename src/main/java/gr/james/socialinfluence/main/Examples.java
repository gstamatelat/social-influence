package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.algorithms.scoring.HITS;
import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.game.players.ExceptionPlayer;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new MemoryGraph();
        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        Vertex v3 = g.addVertex();
        g.addEdge(v1, v3);
        g.addEdge(v2, v3);
        HITS.execute(g, 0.0);

        g = new BarabasiAlbertGenerator(40, 2, 2, 1.0).create();
        System.out.println(HITS.execute(g, 0.0));
        System.out.println(PageRank.execute(g, 0.0));

        Player p = new ExceptionPlayer();
        p.getMove(g, new GameDefinition(3, 3.0, 0));
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
        /*while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
