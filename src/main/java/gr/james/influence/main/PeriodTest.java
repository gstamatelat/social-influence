package gr.james.influence.main;

import gr.james.influence.algorithms.scoring.DeGroot;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.collections.GraphState;

public class PeriodTest {
    public static void main(String[] args) {
        DirectedGraph<Integer, Object> g = DirectedGraph.create();

        g.addVertices(1, 2, 3, 4, 5, 6, 7, 8);

        g.addEdge(1, 2);
        g.addEdge(2, 1);

        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 3);

        g.addEdge(6, 7);
        g.addEdge(7, 8);
        g.addEdge(8, 6);

        g.addEdge(6, 2);
        g.addEdge(6, 5);

        GraphState<Integer, Double> initial = GraphState.create(g.vertexSet(), 0.0);
        initial.put(1, 0.1);
        initial.put(2, 0.2);
        initial.put(3, 0.3);
        initial.put(4, 0.4);
        initial.put(5, 0.5);
        initial.put(6, 0.6);

        System.out.println(Graphs.converges(g));
        DeGroot.execute(g, initial);
    }
}
