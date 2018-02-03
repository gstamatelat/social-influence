package gr.james.influence.graph;

import java.util.List;

@Deprecated
public class SimpleGraph extends MutableGraph<Integer, Object> {
    public static VertexProvider<Integer> vertexProvider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() {
            return nextId++;
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
