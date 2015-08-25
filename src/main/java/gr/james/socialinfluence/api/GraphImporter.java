package gr.james.socialinfluence.api;

import gr.james.socialinfluence.util.Finals;

import java.io.IOException;
import java.io.InputStream;

public interface GraphImporter {
    default Graph from(InputStream in) throws IOException {
        return from(in, Finals.DEFAULT_GRAPH_FACTORY);
    }

    <T extends Graph> T from(InputStream in, GraphFactory<T> factory) throws IOException;
}
