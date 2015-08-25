package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.util.Finals;

public interface GraphGenerator {
    static GraphGenerator decorate(Graph g) {
        return new GraphGenerator() {
            @Override
            public <T extends Graph> T create(GraphFactory<T> factory) {
                return GraphUtils.deepCopy(g, factory);
            }
        };
    }

    default Graph create() {
        return create(Finals.DEFAULT_GRAPH_FACTORY);
    }

    <T extends Graph> T create(GraphFactory<T> factory);
}
