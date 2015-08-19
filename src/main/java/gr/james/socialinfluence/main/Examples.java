package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.RandomGenerator;
import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.RandomHelper;
import gr.james.socialinfluence.util.collections.VertexPair;

import java.io.IOException;
import java.util.Map;

public class Examples {
    public static void main(String[] args) throws IOException {
        while (true) {
            double dampingFactor = RandomHelper.getRandom().nextDouble();
            double p = RandomHelper.getRandom().nextDouble();
            int vertexCount = 40;

            /* Create graph and randomize edge weights */
            Graph g = new RandomGenerator<>(MemoryGraph.class, vertexCount, p).create();
            //GraphUtils.createCircle(g, true);
            for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
                //e.getValue().setWeight(RandomHelper.getRandom().nextDouble());
            }

            /* PageRank */
            //new Dot().to(g, System.out);
            //Finals.LOG.debug("{}");
            PageRank.execute(g, dampingFactor);
        }
    }
}
