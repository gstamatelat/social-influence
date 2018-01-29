package gr.james.influence.graph;

import gr.james.influence.util.Conditions;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TreeMapMetadata implements Metadata {
    protected Map<String, String> meta;

    public TreeMapMetadata() {
        this.meta = new TreeMap<>();
    }

    @Override
    public final String getMeta(String key) {
        return this.meta.get(Conditions.requireNonNull(key));
    }

    @Override
    public final String setMeta(String key, String value) {
        return this.meta.put(Conditions.requireNonNull(key), Conditions.requireNonNull(value));
    }

    @Override
    public String removeMeta(String key) {
        return this.meta.remove(Conditions.requireNonNull(key));
    }

    @Override
    public Set<String> metaKeySet() {
        return Collections.unmodifiableSet(this.meta.keySet());
    }

    @Override
    public void clearMeta() {
        this.meta.clear();
    }
}
