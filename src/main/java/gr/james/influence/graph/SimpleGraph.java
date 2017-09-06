package gr.james.influence.graph;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;

public class SimpleGraph extends MemoryGraph<String, Object> {
    public static GraphFactory<String, Object> factory = new GraphFactory<String, Object>() {
        private int nextId = 0;

        @Override
        public Graph<String, Object> createGraph() {
            return new SimpleGraph();
        }

        @Override
        public String createVertex() {
            return String.valueOf(nextId++);
        }

        @Override
        public String createVertex(Object o) {
            return o.toString();
        }

        @Override
        public Object createEdge() {
            return null;
        }
    };

    public SimpleGraph() {
        super(factory);
    }
}
