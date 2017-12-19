package gr.james.influence.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.io.GraphExporter;
import gr.james.influence.api.io.Serializer;
import gr.james.influence.util.Finals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class EdgesExporter implements GraphExporter {
    public static final String DEFAULT_SEPARATOR = ",";

    private final String delimiter;

    public EdgesExporter(String delimiter) {
        this.delimiter = delimiter;
    }

    public EdgesExporter() {
        this(DEFAULT_SEPARATOR);
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> serializer) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));

        for (V v : g) {
            for (V u : g.getOutEdges(v).keySet()) {
                w.write(String.format("%s%s%s%s%f%n",
                        serializer.serialize(v),
                        this.delimiter,
                        serializer.serialize(u),
                        this.delimiter,
                        g.getOutEdges(v).get(u).getWeight()));
            }
        }

        w.flush();
    }
}
