package gr.james.influence.graph;

import gr.james.influence.api.EdgeProvider;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.VertexProvider;

public class SimpleGraph extends MemoryGraph<String, Object> {
    public static VertexProvider<String> vertexProvider = new VertexProvider<String>() {
        private int nextId = 0;

        @Override
        public String getVertex() {
            return String.valueOf(nextId++);
        }
    };

    public static EdgeProvider<Object> edgeProvider = () -> null;

    public static GraphFactory<String, Object> graphFactory = SimpleGraph::new;

    public SimpleGraph() {
        super(vertexProvider, edgeProvider);
    }
}
