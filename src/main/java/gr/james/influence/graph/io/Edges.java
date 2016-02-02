package gr.james.influence.graph.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphExporter;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphImporter;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;
import gr.james.influence.util.exceptions.GraphException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Edges implements GraphImporter, GraphExporter {
    @Override
    public <T extends Graph> T from(InputStream source, GraphFactory<T> factory) throws IOException {
        T g = factory.create();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        Map<String, Vertex> nodeMap = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            String[] sp = line.split(",");
            if (sp.length != 2) {
                throw new GraphException(Finals.E_EDGES_IMPORT);
            }
            if (!nodeMap.containsKey(sp[0])) {
                nodeMap.put(sp[0], g.addVertex());
            }
            if (!nodeMap.containsKey(sp[1])) {
                nodeMap.put(sp[1], g.addVertex());
            }
            g.addEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1]));
        }

        g.setGraphType("EdgesImport");
        g.setMeta("source", source.toString());

        return g;
    }

    @Override
    public void to(Graph g, OutputStream target) {
        throw new UnsupportedOperationException();
    }
}
