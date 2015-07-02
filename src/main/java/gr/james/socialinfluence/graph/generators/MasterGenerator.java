package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

public class MasterGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;

    private T g;

    public MasterGenerator(Class<T> type) {
        this.type = type;
        reset();
    }

    private void reset() {
        g = Helper.instantiateGeneric(type);
        g.setMeta("name", "Master");
    }

    @Override
    public T create() {
        reset();

        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        Vertex v3 = g.addVertex();
        g.addEdge(v1, v3);
        g.addEdge(v1, v2);
        g.addEdge(v3, v2);
        g.addEdge(v2, v1);

        return g;
    }
}