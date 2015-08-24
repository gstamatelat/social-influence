package gr.james.socialinfluence.api;

import gr.james.socialinfluence.util.Finals;

public interface GraphGenerator {
    default Graph create() {
        return create(Finals.DEFAULT_GRAPH_FACTORY);
    }

    <T extends Graph> T create(GraphFactory<T> factory);
}
