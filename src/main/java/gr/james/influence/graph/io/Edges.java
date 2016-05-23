package gr.james.influence.graph.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphExporter;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphImporter;
import gr.james.influence.graph.Edge;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;
import gr.james.influence.util.exceptions.GraphException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Edges implements GraphImporter, GraphExporter {
    private String delimiter = ",";

    public Edges(String delimiter) {
        this.delimiter = delimiter;
    }

    public Edges() {
        this.delimiter = ",";
    }

    @Override
    public <T extends Graph> T from(InputStream source, GraphFactory<T> factory) throws IOException {
        T g = factory.create();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        Map<String, Vertex> nodeMap = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            String[] sp = line.split(this.delimiter);
            if (sp.length != 3) {
                throw new GraphException(Finals.E_EDGES_IMPORT);
            }
            if (!nodeMap.containsKey(sp[0])) {
                nodeMap.put(sp[0], g.addVertex(sp[0]));
            }
            if (!nodeMap.containsKey(sp[1])) {
                nodeMap.put(sp[1], g.addVertex(sp[1]));
            }
            Edge e = g.addEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1]), Double.parseDouble(sp[2]));
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
    public void to(Graph g, OutputStream target) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));

        for (Vertex v : g) {
            for (Vertex u : g.getOutEdges(v).keySet()) {
                w.write(String.format("%d%s%d%s%f%n", v.getId(), this.delimiter, u.getId(), this.delimiter, g.getOutEdges(v).get(u).getWeight()));
            }
        }

        w.flush();
    }
}
