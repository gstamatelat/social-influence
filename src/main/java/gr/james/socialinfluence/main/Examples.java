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
import gr.james.socialinfluence.graph.generators.GridGenerator;
import gr.james.socialinfluence.graph.generators.RandomGenerator;
import gr.james.socialinfluence.graph.io.Dot;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Scanner;

public class Examples {
    public static void main(String[] args) throws IOException {

        GridGenerator generator = new GridGenerator<>(MemoryGraph.class, 25, 35);
        Graph gridGraph = generator.create();
        System.out.println(gridGraph);
    }
}
