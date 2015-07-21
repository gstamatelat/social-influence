package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Vertex;

import java.util.Map;
import java.util.Set;

public interface GraphState<T> extends Map<Vertex, T> {
    GraphState<T> subtract(GraphState<T> r);

    GraphState<T> abs();

    boolean lessThan(T e);

    double getMean();

    double getMean(Set<Vertex> includeOnly);

    double getSum();

    double getSum(Set<Vertex> includeOnly);
}
