package gr.james.socialinfluence.graph.from;

import gr.james.socialinfluence.graph.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Csv {
    public static Graph get(InputStream in) throws IOException {
        Graph g = new Graph();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
        String line;
        int lineNo = 0;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();

        return g;
    }
}