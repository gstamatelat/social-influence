package gr.james.socialinfluence.graph.io;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphExporter;
import gr.james.socialinfluence.api.GraphImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Json implements GraphImporter, GraphExporter {
    @Override
    public Graph from(InputStream in) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void to(Graph g, OutputStream out) throws IOException {
        throw new UnsupportedOperationException();
    }
}