package gr.james.influence.graph;

import gr.james.influence.api.graph.GraphFactory;
import gr.james.influence.api.graph.VertexProvider;

public class SimpleGraph extends MemoryGraph<Integer, Object> {
    public static VertexProvider<Integer> vertexProvider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() {
            return nextId++;
        }
    };

    public static GraphFactory<Integer, Object> graphFactory = SimpleGraph::new;

    public SimpleGraph() {
        super(vertexProvider);
    }
}
