package gr.james.socialinfluence.api;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface GraphImporter {
    <T extends Graph> T from(InputStream in, GraphFactory<T> factory) throws IOException;
}
