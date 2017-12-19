package gr.james.influence.io;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.io.Deserializer;
import gr.james.influence.api.io.GraphImporter;
import gr.james.influence.util.Finals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EdgesImporter implements GraphImporter {
    public static final String DEFAULT_SEPARATOR = ",";
    public static final double DEFAULT_WEIGHT = 1;
    public static final String PARALLEL_EDGES = "The .edges file contains parallel edge [%s -> %s]";
    public static final String WRONG_RECORDS = "A line in an .edges format file must contain two or three records";

    private final String delimiter;

    public EdgesImporter(String delimiter) {
        this.delimiter = delimiter;
    }

    public EdgesImporter() {
        this(DEFAULT_SEPARATOR);
    }

    @Override
    public <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException {
        final Graph<V, E> g = factory.createGraph();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        final Map<String, V> nodeMap = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            final String[] sp = line.split(this.delimiter);
            if (sp.length != 2 && sp.length != 3) {
                throw new IllegalArgumentException(WRONG_RECORDS);
            }
            final double weight = sp.length == 2 ? DEFAULT_WEIGHT : Double.parseDouble(sp[2]);
            if (!nodeMap.containsKey(sp[0])) {
                final V sp0 = deserializer.deserialize(sp[0]);
                g.addVertex(sp0);
                nodeMap.put(sp[0], sp0);
            }
            if (!nodeMap.containsKey(sp[1])) {
                final V sp1 = deserializer.deserialize(sp[1]);
                g.addVertex(sp1);
                nodeMap.put(sp[1], sp1);
            }
            if (g.addEdge(nodeMap.get(sp[0]), nodeMap.get(sp[1]), weight) == null) {
                throw new IllegalArgumentException(String.format(PARALLEL_EDGES, sp[0], sp[1]));
            }
        }

        g.setMeta(Finals.TYPE_META, "EdgesImport");
        g.setMeta("source", source.toString());

        return g;
    }
}
