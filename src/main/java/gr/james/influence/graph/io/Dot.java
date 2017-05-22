package gr.james.influence.graph.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphExporter;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.GraphImporter;
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
    public <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target) throws IOException {
        String dot = String.format("digraph G {%n");
        for (V v : g) {
            for (V u : g.getOutEdges(v).keySet()) {
                double weight = g.getOutEdges(v).get(u).getWeight();
                dot += String.format("%s%s -> %s [label=\"%.2f\",weight=%f]%n", indent, v, u, weight, weight);
            }
        }
        dot += String.format("}%n");

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(target, Finals.IO_ENCODING));
        w.write(dot);
        w.flush();
    }
}
