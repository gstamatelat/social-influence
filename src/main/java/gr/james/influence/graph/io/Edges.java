package gr.james.influence.graph.io;

import gr.james.influence.api.*;
import gr.james.influence.util.Finals;
import gr.james.influence.util.exceptions.GraphException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Edges implements GraphImporter, GraphExporter {
    private String delimiter = ",";

    public Edges(String delimiter, Function stringMapping) {
        this.delimiter = delimiter;
    }

    public Edges() {
        this.delimiter = ",";
    }

    @Override
    public <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory) throws IOException {
        Graph<V, E> g = factory.create();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        Map<String, V> nodeMap = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            String[] sp = line.split(this.delimiter);
            if (sp.length != 3) {
                throw new GraphException(Finals.E_EDGES_IMPORT);
            }
            if (!nodeMap.containsKey(sp[0])) {
                V v = factory.getVertexFactory().createVertex(sp[0]);
                g.addVertex(v);
                nodeMap.put(sp[0], v);
            }
            if (!nodeMap.containsKey(sp[1])) {
                V v = factory.getVertexFactory().createVertex(sp[1]);
                g.addVertex(v);
                nodeMap.put(sp[1], v);
            }
            GraphEdge e = g.addEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1]), Double.parseDouble(sp[2]));
            if (e == null) {
                g.setEdgeWeight(nodeMap.get(sp[0]), nodeMap.get(sp[1]),
                        g.findEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1])).getWeight() + Double.parseDouble(sp[2]));
                Finals.LOG.info(Finals.L_EDGES_IMPORT_MERGE, sp[0], sp[1]);
            }
        }

        g.setGraphType("EdgesImport");
        g.setMeta("source", source.toString());

        return g;
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));

        for (V v : g) {
            for (V u : g.getOutEdges(v).keySet()) {
                w.write(String.format("%s%s%s%s%f%n", v, this.delimiter, u, this.delimiter, g.getOutEdges(v).get(u).getWeight()));
            }
        }

        w.flush();
    }
}
