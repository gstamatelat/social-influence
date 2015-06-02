package gr.james.socialinfluence.tests;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.Degree;
import gr.james.socialinfluence.graph.algorithms.PageRank;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.helper.Helper;

import java.util.Random;

/**
 * <p>This class demonstrates that in an undirected graph, eigenvector centrality vector and degree centrality vector
 * are proportional.</p>
 */
public class DegreeEigenvectorTest {
    public static void main(String[] args) {
        Graph g = BarabasiAlbert.generate(20, 2, 2, 1.0, new Random(3724));

        GraphState degree = Degree.execute(g, true);
        GraphState pagerank = PageRank.execute(g, 0.0);

        for (Vertex v : g.getVertices()) {
            double dg = degree.get(v);
            double ec = pagerank.get(v);
            Helper.log(String.format("ID: %s, Degree: %s, PageRank: %s, Ratio: %s", v.getId(), dg, ec, String.valueOf(dg / ec)));
        }
    }
}