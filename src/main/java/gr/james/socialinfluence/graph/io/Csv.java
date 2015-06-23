package gr.james.socialinfluence.graph.io;

import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.iterators.IndexIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Csv {
    public static Graph from(InputStream source) throws IOException {
        Graph g = new Graph();

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, "UTF8"));
        String line;
        boolean firstLine = true;
        IndexIterator it = null;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                g.addVertices(line.split(";").length - 1);
                it = new IndexIterator(g);
            } else {
                Vertex v = it.next();
                String[] splitted = line.split(";");
                splitted = Arrays.copyOfRange(splitted, 1, splitted.length);
                IndexIterator it2 = new IndexIterator(g);
                for (String t : splitted) {
                    Vertex u = it2.next();
                    double value = Double.parseDouble(t);
                    if (value > 0) {
                        g.addEdge(v, u).setWeight(value);
                    }
                }
            }
            firstLine = false;
        }
        reader.close();

        return g.setName("CSVImport").setMeta(String.format("CSV,source=%s", source));
    }
}