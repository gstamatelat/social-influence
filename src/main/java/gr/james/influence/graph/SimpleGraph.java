package gr.james.influence.graph;

import gr.james.influence.api.EdgeFactory;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphFactory;
import gr.james.influence.api.VertexFactory;

public class SimpleGraph extends MemoryGraph<String, Object> {
    public static final VertexFactory<String> vertexFactory = new VertexFactory<String>() {
        private int nextId = 0;

        @Override
        public String createVertex() {
            return String.valueOf(nextId++);
        }

        @Override
        public String createVertex(String s) {
            return s;
        }
    };
    
    public static final EdgeFactory<Object> edgeFactory = Object::new;

    public static final GraphFactory<String, Object> graphFactory = new GraphFactory<String, Object>() {
        @Override
        public Graph<String, Object> create() {
            return new SimpleGraph();
        }

        @Override
        public VertexFactory<String> getVertexFactory() {
            return vertexFactory;
        }

        @Override
        public EdgeFactory<Object> getEdgeFactory() {
            return edgeFactory;
        }
    };

    public SimpleGraph() {
        super(vertexFactory, edgeFactory);
    }
}
