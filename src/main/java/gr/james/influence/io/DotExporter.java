package gr.james.influence.io;

import gr.james.influence.api.io.GraphExporter;
import gr.james.influence.api.io.Serializer;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.Graph;
import gr.james.influence.util.Finals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DotExporter implements GraphExporter {
    private String indent;

    public DotExporter() {
        this(Finals.DEFAULT_INDENT);
    }

    public DotExporter(String indent) {
        this.indent = indent;
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> vertexSerializer, Serializer<E> edgeSerializer) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));

        w.write(String.format("digraph G {%n"));
        for (DirectedEdge<V, E> e : g.edges()) {
            if (e.edge() == null) {
                w.write(String.format("%s%s -> %s [weight=%f]%n",
                        indent,
                        vertexSerializer.serialize(e.source()),
                        vertexSerializer.serialize(e.target()),
                        e.weight()));
            } else {
                w.write(String.format("%s%s -> %s [label=\"%s\",weight=%f]%n",
                        indent,
                        vertexSerializer.serialize(e.source()),
                        vertexSerializer.serialize(e.target()),
                        edgeSerializer.serialize(e.edge()),
                        e.weight()));
            }
        }
        w.write(String.format("}%n"));

        w.flush();
    }
}
