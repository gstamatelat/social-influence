package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphOperations;
import gr.james.socialinfluence.util.Helper;

public class CompleteGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int totalVertices;

    public CompleteGenerator(Class<T> type, int totalVertices) {
        this.type = type;
        this.totalVertices = totalVertices;
    }

    @Override
    public T create() {
        T g = Helper.instantiateGeneric(type);

        g.addVertices(totalVertices);
        GraphOperations.connectAllVertices(g);

        g.setMeta("type", "Complete")
                .setMeta("totalVertices", String.valueOf(totalVertices));

        return g;
    }
}
