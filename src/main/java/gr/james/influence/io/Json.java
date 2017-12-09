package gr.james.influence.io;

import gr.james.influence.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * https://github.com/jsongraph/json-graph-specification
 */
public class Json implements GraphImporter, GraphExporter {
    @Override
    public <V, E> Graph<V, E> from(InputStream source, GraphFactory<V, E> factory, Deserializer<V> deserializer) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, E> void to(Graph<V, E> g, OutputStream target, Serializer<V> serializer) {
        throw new UnsupportedOperationException();
    }

    private static class JsonGraph<V, E> {
        private boolean directed = true;
        private Map<String, String> metadata = new HashMap<>();
        private Set<JsonVertex<V>> nodes = new TreeSet<>();
        private Set<JsonEdge<V, E>> edges = new HashSet<>();

        public JsonGraph(Graph<V, E> g) {
            for (V v : g) {
                this.nodes.add(new JsonVertex<>(v));
                for (V y : g.getOutEdges(v).keySet()) {
                    this.edges.add(new JsonEdge<>(v, y, g.getOutEdges(v).get(y)));
                }
            }
        }
    }

    private static class JsonVertex<V> implements Comparable<JsonVertex> {
        private String id;
        private String label;

        public JsonVertex(V v) {
            this.id = v.toString();
            this.label = v.toString();
        }

        @Override
        public int compareTo(JsonVertex o) {
            return this.id.compareTo(o.id);
        }
    }

    private static class JsonEdge<V, E> {
        private String source;
        private String target;
        private boolean directed = true;
        private String label;

        public JsonEdge(V source, V target, GraphEdge<V, E> e) {
            this.source = source.toString();
            this.target = target.toString();
            this.label = String.valueOf(e.getWeight());
        }
    }
}
