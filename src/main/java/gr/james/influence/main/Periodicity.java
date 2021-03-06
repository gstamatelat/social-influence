package gr.james.influence.main;

import gr.james.influence.algorithms.scoring.DeGroot;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.collections.GraphState;

public class Periodicity {
    public static void main(String[] args) {
        DirectedGraph<String, Object> g = DirectedGraph.create();
        g.addVertices("1", "2", "3", "4", "5");
        g.addEdge("1", "2");
        g.addEdge("2", "1");
        //g.addEdge("1", "1");

        g.addEdge("3", "4");
        g.addEdge("4", "5");
        g.addEdge("5", "3");

        g.addEdge("3", "1");

        GraphState<String, Double> gs = GraphState.create(g.vertexSet(), 0.5);
        gs.put("1", 0.1);
        gs.put("2", 0.2);
        gs.put("3", 0.3);
        gs.put("4", 0.4);
        gs.put("5", 0.5);

        if (Graphs.converges(g)) {
            System.out.println("CONVERGES");
        } else {
            System.out.println("DOES NOT CONVERGE");
        }

        System.out.println(DeGroot.execute(g, gs));
    }
}
