package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractGraph implements Graph {
    protected Map<String, String> meta;

    public AbstractGraph() {
        this.meta = new TreeMap<>();
    }

    @Override
    public final String getMeta(String key) {
        return this.meta.get(key);
    }

    @Override
    public final Graph setMeta(String key, String value) {
        this.meta.put(key, value);
        return this;
    }

    @Override
    public Graph clearMeta() {
        this.meta.clear();
        return this;
    }

    @Override
    public String toString() {
        return String.format("{type=%s, meta=%s}", this.getClass().getSimpleName(), this.meta);
    }
}
