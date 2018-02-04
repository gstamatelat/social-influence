package gr.james.influence.graph;

@FunctionalInterface
public interface VertexProvider<V> {
    VertexProvider<Integer> intProvider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() {
            return nextId++;
        }
    };

    V getVertex();
}
