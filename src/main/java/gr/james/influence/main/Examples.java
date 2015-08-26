package gr.james.influence.main;

import gr.james.influence.algorithms.generators.BarabasiAlbertGenerator;
import gr.james.influence.algorithms.scoring.HITS;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphImporter;
import gr.james.influence.game.GameDefinition;
import gr.james.influence.game.Player;
import gr.james.influence.game.players.ExceptionPlayer;
import gr.james.influence.graph.MemoryGraph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.graph.io.Csv;

import java.io.IOException;
import java.net.URL;

public class Examples {
    public static void main(String[] args) throws IOException {
        Graph g = new MemoryGraph();
        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        Vertex v3 = g.addVertex();
        g.addEdge(v1, v3);
        g.addEdge(v2, v3);
        HITS.execute(g, 0.0);

        g = new BarabasiAlbertGenerator(40, 2, 2, 1.0).generate();
        System.out.println(HITS.execute(g, 0.0));
        System.out.println(PageRank.execute(g, 0.0));

        Player p = new ExceptionPlayer();
        p.getMove(g, new GameDefinition(3, 3.0, 0));
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");

        GraphImporter importer = new Csv();
        Graph fff = importer.from(new URL("https://raw.githubusercontent.com/gstamatelat/social-influence/gh-pages/graphs/school.csv"));
        int h = 0;
    }
}
