package gr.james.socialinfluence.main;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Examples {
    public static void main(String[] args) throws IOException {
        BiMap<Integer, Vertex> fff = HashBiMap.create();
        List<Vertex> ggg = new ArrayList<>();

        int SIZE = 1000000;

        for (int i = 0; i < SIZE; i++) {
            Vertex v = new Vertex();
            fff.put(i, v);
            ggg.add(v);
        }

        Vertex search = new Vertex();
        fff.put(SIZE, search);
        ggg.add(search);

        Finals.LOG.info("START");
        Finals.LOG.info("{}", fff.containsValue(search));
        Finals.LOG.info("{}", ggg.contains(search));

        Finals.LOG.info("START");
        Finals.LOG.info("{}", fff.get(SIZE - 9));
        Finals.LOG.info("{}", ggg.get(SIZE - 9));

        Graph g = new MemoryGraph();
        g.addVertex(null);
        g.addVertex(null);
        System.out.println(g.getVerticesCount());
        int hhh = 0;
    }
}
