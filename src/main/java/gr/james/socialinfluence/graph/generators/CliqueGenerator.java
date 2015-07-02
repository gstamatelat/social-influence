package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.GraphTransformations;
import gr.james.socialinfluence.helper.Helper;

public class CliqueGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int totalVertices;

    private T g;

    public CliqueGenerator(Class<T> type, int totalVertices) {
        this.type = type;
        this.totalVertices = totalVertices;
        reset();
    }

    private void reset() {
        g = Helper.instantiateGeneric(type);
        g.setMeta("name", "Clique")
                .setMeta("totalVertices", String.valueOf(totalVertices));
    }

    @Override
    public T create() {
        reset();

        g.addVertices(totalVertices);
        GraphTransformations.connectAllVertices(g);

        return g;
    }
}