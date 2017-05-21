package gr.james.influence.graph.io;

import gr.james.influence.api.*;
import gr.james.influence.graph.Vertex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * https://github.com/jsongraph/json-graph-specification
 */
public class Json implements GraphImporter, GraphExporter {
    @Override
    public <T extends Graph> T from(InputStream source, GraphFactory<T> factory) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void to(Graph g, OutputStream target) {
        throw new UnsupportedOperationException();
    }

    private static class JsonGraph {
        private boolean directed = true;
        private Map<String, String> metadata = new HashMap<>();
        private Set<JsonVertex> nodes = new TreeSet<>();
        private Set<JsonEdge> edges = new HashSet<>();

        public JsonGraph(Graph g) {
            for (Vertex v : g) {
                this.nodes.add(new JsonVertex(v));
                for (Vertex y : g.getOutEdges(v).keySet()) {
                    this.edges.add(new JsonEdge(v, y, g.getOutEdges(v).get(y)));
                }
            }
        }
    }

    private static class JsonVertex implements Comparable<JsonVertex> {
        private String id;
        private String label;

        public JsonVertex(Vertex v) {
            this.id = String.valueOf(v.getId());
            this.label = v.getLabel();
        }

        @Override
        public int compareTo(JsonVertex o) {
            return this.id.compareTo(o.id);
        }
    }

    private static class JsonEdge {
        private String source;
        private String target;
        private boolean directed = true;
        private String label;

        public JsonEdge(Vertex source, Vertex target, GraphEdge e) {
            this.source = String.valueOf(source.getId());
            this.target = String.valueOf(target.getId());
            this.label = String.valueOf(e.getWeight());
        }
    }
}
