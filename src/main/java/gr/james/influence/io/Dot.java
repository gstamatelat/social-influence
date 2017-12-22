package gr.james.influence.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.io.Deserializer;
import gr.james.influence.api.io.GraphExporter;
import gr.james.influence.api.io.GraphImporter;
import gr.james.influence.api.io.Serializer;
import gr.james.influence.util.Finals;

import java.io.*;

public class Dot implements GraphImporter, GraphExporter {
    private String indent;

    public Dot() {
        this(Finals.DEFAULT_INDENT);
    }

    public Dot(String indent) {
        this.indent = indent;
    }

    @Override
    public <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> vertexSerializer, Serializer<E> edgeSerializer) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));

        w.write(String.format("digraph G {%n"));
        for (GraphEdge<V, E> e : g.edges()) {
            w.write(String.format("%s%s -> %s [label=\"%s\",weight=%f]%n",
                    indent,
                    vertexSerializer.serialize(e.getSource()),
                    vertexSerializer.serialize(e.getTarget()),
                    edgeSerializer.serialize(e.getEdge()),
                    e.getWeight()));
        }
        w.write(String.format("}%n"));

        w.flush();
    }
}
