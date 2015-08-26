package gr.james.influence.graph.io;

import gr.james.influence.algorithms.iterators.OrderedVertexIterator;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphExporter;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphImporter;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;

import java.io.*;
import java.util.Arrays;

public class Csv implements GraphImporter, GraphExporter {
    @Override
    public <T extends Graph> T from(InputStream source, GraphFactory<T> factory) throws IOException {
        T g = factory.create();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        String line;
        boolean firstLine = true;
        OrderedVertexIterator it = null;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                g.addVertices(line.split(";").length - 1);
                it = new OrderedVertexIterator(g);
            } else {
                Vertex v = it.next();
                String[] splitted = line.split(";");
                splitted = Arrays.copyOfRange(splitted, 1, splitted.length);
                OrderedVertexIterator it2 = new OrderedVertexIterator(g);
                for (String t : splitted) {
                    Vertex u = it2.next();
                    double value = Double.parseDouble(t);
                    if (value > 0) {
                        g.addEdge(v, u, value);
                    }
                }
            }
            firstLine = false;
        }

        g.setGraphType("CSVImport");
        g.setMeta("source", source.toString());

        return g;
    }

    @Override
    public void to(Graph g, OutputStream target) {
        throw new UnsupportedOperationException();
    }
}