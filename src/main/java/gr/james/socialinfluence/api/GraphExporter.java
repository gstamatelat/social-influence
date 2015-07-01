package gr.james.socialinfluence.api;

import java.io.IOException;
import java.io.OutputStream;

public interface GraphExporter {
    void to(Graph g, OutputStream out) throws IOException;
}