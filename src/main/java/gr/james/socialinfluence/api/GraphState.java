package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Vertex;

import java.util.Collection;
import java.util.Map;

public interface GraphState<T> extends Map<Vertex, T> {
    GraphState<T> subtract(GraphState<T> r);

    GraphState<T> abs();

    boolean lessThan(T e);

    double getMean();

    double getMean(Collection<Vertex> includeOnly);

    double getSum();

    double getSum(Collection<Vertex> includeOnly);
}
