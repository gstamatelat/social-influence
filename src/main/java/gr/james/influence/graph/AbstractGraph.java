package gr.james.influence.graph;

import gr.james.influence.api.Graph;

public abstract class AbstractGraph extends TreeMapMetadata implements Graph {
    @Override
    public String toString() {
        return String.format("{container=%s, meta=%s}", this.getClass().getSimpleName(), this.meta);
    }
}
