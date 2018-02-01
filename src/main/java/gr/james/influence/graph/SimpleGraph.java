package gr.james.influence.graph;

import java.util.List;

public class SimpleGraph extends MemoryGraph<Integer, Object> {
    public static VertexProvider<Integer> vertexProvider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() {
            return nextId++;
        }
    };

    public static GraphFactory<Integer, Object> factory = new GraphFactory<Integer, Object>() {
        @Override
        public SimpleGraph createGraph(GraphType type) {
            return new SimpleGraph();
        }

        @Override
        public SimpleGraph createGraph(GraphType type, int expectedVertexCount) {
            return new SimpleGraph(expectedVertexCount);
        }
    };

    public SimpleGraph() {
        super();
    }

    public SimpleGraph(int expectedVertexCount) {
        super(expectedVertexCount);
    }

    public Integer addVertex() {
        return super.addVertex(vertexProvider);
    }

    public List<Integer> addVertices(int n) {
        return super.addVertices(n, vertexProvider);
    }
}
