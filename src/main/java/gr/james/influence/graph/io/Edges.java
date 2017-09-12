package gr.james.influence.graph.io;

import gr.james.influence.api.*;
import gr.james.influence.util.Finals;
import gr.james.influence.util.exceptions.GraphException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Edges implements GraphImporter, GraphExporter {
    public static final String DEFAULT_SEPARATOR = ",";

    private String delimiter;

    public Edges(String delimiter) {
        this.delimiter = delimiter;
    }

    public Edges() {
        this.delimiter = DEFAULT_SEPARATOR;
    }

    @Override
    public <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException {
        Graph<V, E> g = factory.createGraph();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        Map<String, V> nodeMap = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            String[] sp = line.split(this.delimiter);
            if (sp.length != 3) {
                throw new GraphException(Finals.E_EDGES_IMPORT);
            }
            if (!nodeMap.containsKey(sp[0])) {
                V sp0 = deserializer.deserialize(sp[0]);
                g.addVertex(sp0);
                nodeMap.put(sp[0], sp0);
            }
            if (!nodeMap.containsKey(sp[1])) {
                V sp1 = deserializer.deserialize(sp[1]);
                g.addVertex(sp1);
                nodeMap.put(sp[1], sp1);
            }
            GraphEdge e = g.addEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1]), Double.parseDouble(sp[2]));
            if (e == null) {
                g.setEdgeWeight(nodeMap.get(sp[0]), nodeMap.get(sp[1]),
                        g.findEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1])).getWeight() + Double.parseDouble(sp[2]));
                Finals.LOG.info(Finals.L_EDGES_IMPORT_MERGE, sp[0], sp[1]);
            }
        }

        g.setMeta(Finals.TYPE_META, "EdgesImport");
        g.setMeta("source", source.toString());

        return g;
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> serializer) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));

        for (V v : g) {
            for (V u : g.getOutEdges(v).keySet()) {
                w.write(String.format("%s%s%s%s%f%n",
                        serializer.serialize(v),
                        this.delimiter,
                        serializer.serialize(u),
                        this.delimiter,
                        g.getOutEdges(v).get(u).getWeight()));
            }
        }

        w.flush();
    }
}
