package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Vertex;

import java.util.Collection;
import java.util.Map;

@Deprecated
public interface GraphStateOld<T> extends Map<Vertex, T> {
    GraphStateOld<T> subtract(GraphStateOld<T> r);

    GraphStateOld<T> power(int p);

    GraphStateOld<T> abs();

    boolean lessThan(T e);

    default double getMean() {
        return getMean(this.keySet());
    }

    default double getMean(Collection<Vertex> includeOnly) {
        return getSum(includeOnly) / includeOnly.size();
    }

    default double getSum() {
        return getSum(this.keySet());
    }

    double getSum(Collection<Vertex> includeOnly);
}
