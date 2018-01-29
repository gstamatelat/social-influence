package gr.james.influence.graph;

import gr.james.influence.api.graph.VertexProvider;

import java.util.List;

public class SimpleGraph extends MemoryGraph<Integer, Object> {
    public static VertexProvider<Integer> vertexProvider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() {
            return nextId++;
        }
    };

    //public static GraphFactory<Integer, Object> graphFactory = SimpleGraph::new;

    //private final VertexProvider<Integer> provider = new IntegerVertexProvider();

    public SimpleGraph() {
        super();
    }

    public Integer addVertex() {
        return super.addVertex(vertexProvider);
    }

    public List<Integer> addVertices(int n) {
        return super.addVertices(n, vertexProvider);
    }
}
