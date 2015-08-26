package gr.james.influence.graph.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphExporter;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphImporter;
import gr.james.influence.graph.Edge;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.VertexPair;

import java.io.*;
import java.util.Map;

public class Dot implements GraphImporter, GraphExporter {
    @Override
    public <T extends Graph> T from(InputStream source, GraphFactory<T> factory) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void to(Graph g, OutputStream target) throws IOException {
        String dot = "digraph G {" + System.lineSeparator();
        dot += "  overlap = false;" + System.lineSeparator();
        dot += "  bgcolor = transparent;" + System.lineSeparator();
        dot += "  splines = true;" + System.lineSeparator();
        for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            Vertex v = e.getKey().getFirst();
            Vertex w = e.getKey().getSecond();
            dot += "  " + v.toString() + " -> " + w.toString() + System.lineSeparator();
        }
        dot += "}" + System.lineSeparator();

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));
        w.write(dot);
        w.flush();
    }
}