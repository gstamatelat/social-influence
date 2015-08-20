package gr.james.socialinfluence.util.collections;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.exceptions.GraphException;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.ToDoubleFunction;

public class GraphState<T> extends HashMap<Vertex, T> {
    private ToDoubleFunction<T> converter;

    public GraphState() {
        this.converter = null;
    }

    public GraphState(Graph g, T i) {
        this.converter = null;
        for (Vertex v : g) {
            this.put(v, i);
        }
    }

    public GraphState(ToDoubleFunction<T> converter) {
        this.converter = converter;
    }

    public double getAsDouble(Vertex v) {
        if (converter == null) {
            T t = this.get(v);
            if (t instanceof Number) {
                return ((Number) t).doubleValue();
            } else {
                throw new GraphException("You must provide a converter to GraphState");
            }
        } else {
            return converter.applyAsDouble(this.get(v));
        }
    }

    public GraphState<Double> subtract(GraphState<T> r) {
        GraphState<Double> newState = new GraphState<>();
        for (Vertex v : this.keySet()) {
            newState.put(v,
                    this.getAsDouble(v) - r.getAsDouble(v)
            );
        }
        return newState;
    }

    public GraphState<Double> power(int p) {
        GraphState<Double> newState = new GraphState<>();
        for (Vertex v : this.keySet()) {
            newState.put(v,
                    Math.pow(this.getAsDouble(v), p)
            );
        }
        return newState;
    }

    public GraphState<Double> abs() {
        GraphState<Double> newState = new GraphState<>();
        for (Vertex v : this.keySet()) {
            newState.put(v,
                    Math.abs(this.getAsDouble(v))
            );
        }
        return newState;
    }

    public boolean lessThan(double e) {
        for (Vertex v : this.keySet()) {
            if (this.getAsDouble(v) >= e) {
                return false;
            }
        }
        return true;
    }

    public Weighted<Vertex, Double> getMax() {
        return getMax(this.keySet());
    }

    public Weighted<Vertex, Double> getMax(Collection<Vertex> includeOnly) {
        return this.entrySet().stream().filter(vertexTEntry -> includeOnly.contains(vertexTEntry.getKey()))
                .max((o1, o2) -> Double.compare(this.getAsDouble(o1.getKey()), this.getAsDouble(o2.getKey())))
                .map(i -> new Weighted<>(i.getKey(), this.getAsDouble(i.getKey()))).get();
    }

    public Weighted<Vertex, Double> getMin() {
        return getMin(this.keySet());
    }

    public Weighted<Vertex, Double> getMin(Collection<Vertex> includeOnly) {
        return this.entrySet().stream().filter(vertexTEntry -> includeOnly.contains(vertexTEntry.getKey()))
                .min((o1, o2) -> Double.compare(this.getAsDouble(o1.getKey()), this.getAsDouble(o2.getKey())))
                .map(i -> new Weighted<>(i.getKey(), this.getAsDouble(i.getKey()))).get();
    }

    @Deprecated
    public double getMean() {
        return getMean(this.keySet());
    }

    @Deprecated
    public double getMean(Collection<Vertex> includeOnly) {
        return getSum(includeOnly) / includeOnly.size();
    }

    public double getAverage() {
        return getAverage(this.keySet());
    }

    public double getAverage(Collection<Vertex> includeOnly) {
        return getSum(includeOnly) / includeOnly.size();
    }

    public double getSum() {
        return getSum(this.keySet());
    }

    public double getSum(Collection<Vertex> includeOnly) {
        return this.entrySet().stream().filter(vertexTEntry -> includeOnly.contains(vertexTEntry.getKey()))
                .mapToDouble(i -> this.getAsDouble(i.getKey())).sum();
    }
}
