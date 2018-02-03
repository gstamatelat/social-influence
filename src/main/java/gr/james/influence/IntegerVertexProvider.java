package gr.james.influence;

import gr.james.influence.graph.VertexProvider;

// TODO: Move this to test scope
@Deprecated
public class IntegerVertexProvider {
    public static VertexProvider<Integer> provider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() {
            return nextId++;
        }
    };
}
