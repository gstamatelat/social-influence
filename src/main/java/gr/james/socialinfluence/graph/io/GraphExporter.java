package gr.james.socialinfluence.graph.io;

import gr.james.socialinfluence.graph.Graph;

import java.io.IOException;
import java.io.OutputStream;

public interface GraphExporter {
    void to(Graph g, OutputStream out) throws IOException;
}