package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.RandomHelper;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {

            /*Graph g = new MemoryGraph();

            List<Vertex> added = g.addVertices(4);

            Vertex v1 = added.get(0);
            Vertex v2 = added.get(1);
            Vertex v3 = added.get(2);
            Vertex v4 = added.get(3);

            g.addEdge(v1, v3);
            g.addEdge(v3, v2);
            g.addEdge(v3, v4);
            g.addEdge(v4, v1);
            g.addEdge(v2, v1);*/


            double dampingFactor = RandomHelper.getRandom().nextDouble();
            double p = RandomHelper.getRandom().nextDouble();
            int vertexCount = 40;

            Graph g = new RandomGenerator<>(MemoryGraph.class, vertexCount, p).create();

            /* PageRank */
            //new Dot().to(g, System.out);
            /*PageRank.execute(g, 0.0, 0.0, (oldState, newState) -> System.out.printf("%.17f,%.17f,%.17f,%.17f%n",
                            newState.get(v1), newState.get(v2), newState.get(v3), newState.get(v4)
                    )
            );*/
            PageRank.execute(g, dampingFactor, 0.0);
        }
        System.out.println(System.currentTimeMillis() - now);
    }
}
