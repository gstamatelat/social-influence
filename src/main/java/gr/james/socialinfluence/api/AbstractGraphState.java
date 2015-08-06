package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGraphState<T> extends HashMap<Vertex, T> implements GraphState<T> {
    @Override
    public double getMean() {
        return getMean(this.keySet());
    }

    @Override
    public double getMean(Collection<Vertex> includeOnly) {
        return getSum(includeOnly) / includeOnly.size();
    }

    @Override
    public double getSum() {
        return getSum(this.keySet());
    }

    @Override
    public GraphState<T> subtract(GraphState<T> r) {
        GraphState<T> ret = Helper.instantiateGeneric(this.getClass());
        for (Map.Entry<Vertex, T> e : this.entrySet()) {
            ret.put(e.getKey(),
                    this.subtract(e.getValue(), r.get(e.getKey()))
            );
        }
        return ret;
    }

    @Override
    public GraphState<T> abs() {
        GraphState<T> ret = Helper.instantiateGeneric(this.getClass());
        for (Map.Entry<Vertex, T> e : this.entrySet()) {
            ret.put(e.getKey(),
                    this.abs(e.getValue())
            );
        }
        return ret;
    }

    @Override
    public boolean lessThan(T e) {
        boolean ret = true;
        for (T k : this.values()) {
            if (this.greaterThan(k, e)) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    @Override
    public double getSum(Collection<Vertex> includeOnly) {
        double sum = 0.0;

        for (Map.Entry<Vertex, T> e : this.entrySet()) {
            if (includeOnly.contains(e.getKey())) {
                sum = this.add(e.getValue(), sum);
            }
        }

        return sum;
    }

    protected abstract T add(T x1, T x2);

    protected abstract double add(T x1, double x2);

    protected abstract T subtract(T x1, T x2);

    protected abstract boolean greaterThan(T x1, T x2);

    protected abstract T abs(T x);
}
