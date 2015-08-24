package gr.james.socialinfluence.graph.io;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphExporter;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.api.GraphImporter;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.collections.VertexPair;

import java.io.*;
import java.util.Map;

public class Dot implements GraphImporter, GraphExporter {
    @Override
    public <T extends Graph> T from(InputStream in, GraphFactory<T> factory) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void to(Graph g, OutputStream out) throws IOException {
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

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out, Finals.IO_ENCODING));
        w.write(dot);
        w.flush();
        //w.close(); // Don't close someone else's stream
    }
}
