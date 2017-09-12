package gr.james.influence.api;

import gr.james.influence.util.Finals;

public interface GraphFactory<V, E> {
    default Graph<V, E> createGraph() {
        throw new UnsupportedOperationException(Finals.E_GRAPH_NOT_SUPPORTED);
    }

    default V createVertex() {
        throw new UnsupportedOperationException(Finals.E_GRAPH_NOT_SUPPORTED);
    }

    default E createEdge() {
        throw new UnsupportedOperationException(Finals.E_GRAPH_NOT_SUPPORTED);
    }
}
