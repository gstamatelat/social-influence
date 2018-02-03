package gr.james.influence.io;

import gr.james.influence.algorithms.iterators.OrderedVertexIterator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.MutableGraph;
import gr.james.influence.util.Finals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CSVImporter<V, E> implements GraphImporter<Graph<V, E>, V, E> {
    @Override
    public Graph<V, E> from(InputStream source, Deserializer<V> deserializer) throws IOException {
        Graph<V, E> g = new MutableGraph<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, Finals.IO_ENCODING));
        String line;
        boolean firstLine = true;
        OrderedVertexIterator<V> it = null;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                // TODO
                //g.addVertices(line.split(";").length - 1);
                it = new OrderedVertexIterator<>(g);
            } else {
                V v = it.next();
                String[] splitted = line.split(";");
                splitted = Arrays.copyOfRange(splitted, 1, splitted.length);
                OrderedVertexIterator<V> it2 = new OrderedVertexIterator<>(g);
                for (String t : splitted) {
                    V u = it2.next();
                    double value = Double.parseDouble(t);
                    if (value > 0) {
                        g.addEdge(v, u, value);
                    }
                }
            }
            firstLine = false;
        }

        g.setMeta(Finals.TYPE_META, "CSVImport");
        g.setMeta("source", source.toString());

        return g;
    }
}
