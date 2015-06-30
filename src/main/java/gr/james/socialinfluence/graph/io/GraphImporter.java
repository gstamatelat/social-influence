package gr.james.socialinfluence.graph.io;

import gr.james.socialinfluence.graph.Graph;

import java.io.IOException;
import java.io.InputStream;

public interface GraphImporter {
    Graph from(InputStream in) throws IOException;
}