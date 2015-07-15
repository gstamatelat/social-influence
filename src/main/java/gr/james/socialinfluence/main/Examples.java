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
import java.util.HashSet;
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
        int count = 0;



        Graph g = new MemoryGraph();
        Set<Vertex> set = new HashSet<>();



        g.addVertices((rows + 1) * (columns + 1));
        set.addAll(g.getVertices());
        Vertex[] a = set.toArray(new Vertex[(rows+1)*(columns+1)]);

        for (int i=0; i<=rows; i++){
            for (int j=0; j<=columns; j++){
                if(j!=columns) {
                    g.addEdge(a[count], a[count + 1], true);
                }
                if(i!=rows) {
                    g.addEdge(a[count], a[count + columns + 1], true);
                }

                count=count+1;
            }
        }

        Dot dot = new Dot();
        dot.to(g, System.out);
    }
}
