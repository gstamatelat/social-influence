//NOT FINISHED

package gr.james.socialinfluence.main;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.players.ExceptionPlayer;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.graph.generators.RandomGenerator;
import gr.james.socialinfluence.graph.io.Dot;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.Scanner;

public class Examples {
    public static void main(String[] args) throws IOException {

        Scanner scan= new Scanner(System.in);
        System.out.println("print rows");
        int rows= scan.nextInt();
        System.out.println("print columns");
        int columns= scan.nextInt();

        LinkedList<Vertex> list = new LinkedList<>();


        Graph g = new MemoryGraph();

        Vertex v = g.addVertex();


        for (int i=1; i<=rows; i++){
            for (int j=1; j<=columns; j++){
                if(j>1){
                    g.addEdge(v1, v2, true);
                }
            }
        }


        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        g.addEdge(v1, v2, true);

        Dot dot = new Dot();
        dot.to(g, System.out);
    }
}
