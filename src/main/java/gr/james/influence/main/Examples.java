package gr.james.influence.main;

import gr.james.influence.algorithms.generators.random.BarabasiAlbertGenerator;
import gr.james.influence.algorithms.generators.test.MasterGenerator;
import gr.james.influence.algorithms.generators.test.WeaklyConnectedGenerator;
import gr.james.influence.algorithms.scoring.DeGroot;
import gr.james.influence.algorithms.scoring.HITS;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.api.Graph;
import gr.james.influence.game.GameDefinition;
import gr.james.influence.game.Player;
import gr.james.influence.game.players.ExceptionPlayer;
import gr.james.influence.graph.MemoryGraph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;

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

        g = new BarabasiAlbertGenerator(40, 2, 2, 1.0).generate();
        System.out.println(HITS.execute(g, 0.0));
        System.out.println(PageRank.execute(g, 0.0));

        Player p = new ExceptionPlayer();
        p.getMove(g, new GameDefinition(3, 3.0, 0, 0.0));
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");

        /*GraphImporter importer = new Csv();
        Graph fff = importer.from(new URL("https://raw.githubusercontent.com/gstamatelat/social-influence/gh-pages/graphs/school.csv"));
        int h = 0;*/

        Graph eee = new MasterGenerator().generate();
        System.out.println(eee.getAveragePathLength());

        //Graph g0 = new Csv().from(new URL("https://euclid.ee.duth.gr:25312/index.php/s/wXmIirefZKmv95w/download?path=%2F&files=school-2.csv"));
        //Graph g0 = new Edges(" ").from(new URL("https://euclid.ee.duth.gr:25312/index.php/s/wXmIirefZKmv95w/download?path=%2F&files=twitter.edges"));
        Graph g0 = new WeaklyConnectedGenerator().generate();

        GraphState<Double> initial = new GraphState<>(g0, 0.5);
        initial.put(g0.getVertexFromIndex(0), 0.2);
        initial.put(g0.getVertexFromIndex(1), 0.3);
        initial.put(g0.getVertexFromIndex(2), 0.4);
        initial.put(g0.getVertexFromIndex(3), 0.5);
        GraphState<Double> limiting = DeGroot.execute(g0, initial);

        GraphState<Double> aaa = PageRank.execute(g0, 0.0);
        System.out.println(g0.getDiameter());
    }
}
