package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Graph;

import java.io.IOException;
import java.io.InputStream;

public interface GraphImporter {
    Graph from(InputStream in) throws IOException;
}