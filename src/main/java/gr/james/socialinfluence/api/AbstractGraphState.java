package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public abstract class AbstractGraphState<T> extends HashMap<Vertex, T> implements GraphStateOld<T> {
    @Override
    public GraphStateOld<T> subtract(GraphStateOld<T> r) {
        GraphStateOld<T> ret = Helper.instantiateGeneric(this.getClass());
        for (Map.Entry<Vertex, T> e : this.entrySet()) {
            ret.put(e.getKey(),
                    this.subtract(e.getValue(), r.get(e.getKey()))
            );
        }
        return ret;
    }

    @Override
    public GraphStateOld<T> power(int p) {
        GraphStateOld<T> ret = Helper.instantiateGeneric(this.getClass());
        for (Map.Entry<Vertex, T> e : this.entrySet()) {
            ret.put(e.getKey(),
                    this.pow(e.getValue(), p)
            );
        }
        return ret;
    }

    @Override
    public GraphStateOld<T> abs() {
        GraphStateOld<T> ret = Helper.instantiateGeneric(this.getClass());
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

    protected abstract T pow(T t, int x);
}
