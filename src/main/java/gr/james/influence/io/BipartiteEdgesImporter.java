package gr.james.influence.io;


import gr.james.influence.exceptions.InvalidFormatException;
import gr.james.influence.graph.BipartiteGraph;
import gr.james.influence.util.Finals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BipartiteEdgesImporter<V, E> implements GraphImporter<BipartiteGraph<V, E>, V, E> {
    public static final String DEFAULT_SEPARATOR = ",";
    public static final String PARALLEL_EDGES = "The .edges file contains parallel edge [%s -- %s]";
    public static final String WRONG_SET = "Vertex %s exists on both sets";
    public static final String WRONG_RECORDS = "A line in an .edges format file must contain exactly two records";

    private final String delimiter;

    public BipartiteEdgesImporter(String delimiter) {
        this.delimiter = delimiter;
    }

    public BipartiteEdgesImporter() {
        this(DEFAULT_SEPARATOR);
    }

    @Override
    public BipartiteGraph<V, E> from(InputStream source, Deserializer<V> deserializer) throws IOException {
        final BipartiteGraph<V, E> g = BipartiteGraph.create();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));

        String line;
        while ((line = reader.readLine()) != null) {
            final String[] sp = line.split(this.delimiter);
            if (sp.length != 2) {
                throw new InvalidFormatException(WRONG_RECORDS);
            }
            final V sp0 = deserializer.deserialize(sp[0]);
            final V sp1 = deserializer.deserialize(sp[1]);
            if (g.vertexSetB().contains(sp0)) {
                throw new InvalidFormatException(WRONG_SET, sp0);
            }
            if (g.vertexSetA().contains(sp1)) {
                throw new InvalidFormatException(WRONG_SET, sp1);
            }
            g.addVertexInA(sp0);
            g.addVertexInB(sp1);
            if (g.addEdge(sp0, sp1) == null) {
                throw new InvalidFormatException(PARALLEL_EDGES, sp0, sp1);
            }
        }

        return g;
    }
}
