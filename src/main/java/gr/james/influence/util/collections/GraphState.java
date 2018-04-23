package gr.james.influence.util.collections;

import com.google.common.collect.ForwardingMap;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class GraphState<V, T> extends ForwardingMap<V, T> {
    private Map<V, T> delegate = new HashMap<>();
    private ToDoubleFunction<T> converter = null;

    private GraphState() {
    }

    /**
     * Create and return an empty {@link GraphState}.
     *
     * @param <V> the vertex type
     * @param <T> the state type
     * @return a new empty {@link GraphState}
     */
    public static <V, T> GraphState<V, T> create() {
        return new GraphState<>();
    }

    /**
     * Create and return a new {@link GraphState} with the specified vertex collection and all values set to a single
     * value.
     *
     * @param c     the vertex set
     * @param value the value to associate with each vertex
     * @param <V>   the vertex type
     * @param <T>   the state type
     * @return a new {@link GraphState} with the collection {@code c} as keys and all values set to {@code value}
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public static <V, T> GraphState<V, T> create(Collection<V> c, T value) {
        GraphState<V, T> s = new GraphState<>();
        for (V v : c) {
            s.put(v, value);
        }
        return s;
    }

    /**
     * Create and return a new {@link GraphState} with the specified vertex collection and values generated using a
     * {@link Supplier}.
     * <p>
     * The supplier is consumed at the order of iteration of {@code c}.
     *
     * @param c        the vertex set
     * @param supplier the {@link Supplier} to use when generating values
     * @param <V>      the vertex type
     * @param <T>      the state type
     * @return a new {@link GraphState} with the collection {@code c} as keys and all values generated using
     * {@code supplier}
     * @throws NullPointerException if {@code c} or {@code supplier} is {@code null}
     */
    public static <V, T> GraphState<V, T> create(Collection<V> c, Supplier<T> supplier) {
        GraphState<V, T> s = new GraphState<>();
        for (V v : c) {
            s.put(v, supplier.get());
        }
        return s;
    }

    @Override
    protected Map<V, T> delegate() {
        return delegate;
    }

    @Deprecated
    public double getAsDouble(V v) {
        if (converter == null) {
            T t = this.get(v);
            if (t instanceof Number) {
                return ((Number) t).doubleValue();
            } else {
                throw new UnsupportedOperationException("You must provide a converter to GraphState if the type is not a number");
            }
        } else {
            return converter.applyAsDouble(this.get(v));
        }
    }

    public GraphState<V, Double> subtract(GraphState<V, T> r) {
        GraphState<V, Double> newState = new GraphState<>();
        for (V v : this.keySet()) {
            newState.put(v,
                    this.getAsDouble(v) - r.getAsDouble(v)
            );
        }
        return newState;
    }

    public GraphState<V, Double> power(double p) {
        GraphState<V, Double> newState = new GraphState<>();
        for (V v : this.keySet()) {
            newState.put(v,
                    Math.pow(this.getAsDouble(v), p)
            );
        }
        return newState;
    }

    public GraphState<V, Double> abs() {
        GraphState<V, Double> newState = new GraphState<>();
        for (V v : this.keySet()) {
            newState.put(v,
                    Math.abs(this.getAsDouble(v))
            );
        }
        return newState;
    }

    public boolean lessThan(double e) {
        for (V v : this.keySet()) {
            if (this.getAsDouble(v) >= e) {
                return false;
            }
        }
        return true;
    }

    public Weighted<V, Double> getMax() {
        return getMax(this.keySet());
    }

    public Weighted<V, Double> getMax(Collection<V> filter) {
        return this.entrySet().stream().filter(vertexTEntry -> filter.contains(vertexTEntry.getKey()))
                .max(Comparator.comparingDouble(o -> this.getAsDouble(o.getKey())))
                .map(i -> new Weighted<>(i.getKey(), this.getAsDouble(i.getKey()))).get();
    }

    public Weighted<V, Double> getMin() {
        return getMin(this.keySet());
    }

    public Weighted<V, Double> getMin(Collection<V> filter) {
        return this.entrySet().stream().filter(vertexTEntry -> filter.contains(vertexTEntry.getKey()))
                .min(Comparator.comparingDouble(o -> this.getAsDouble(o.getKey())))
                .map(i -> new Weighted<>(i.getKey(), this.getAsDouble(i.getKey()))).get();
    }

    public double getAverage() {
        return getAverage(this.keySet());
    }

    public double getAverage(Collection<V> filter) {
        return getSum(filter) / filter.size();
    }

    public double getSum() {
        return getSum(this.keySet());
    }

    public double getSum(Collection<V> filter) {
        return this.entrySet().stream().filter(vertexTEntry -> filter.contains(vertexTEntry.getKey()))
                .mapToDouble(i -> this.getAsDouble(i.getKey())).sum();
    }

    /*@Override
    public String toString() {
        if (this.values().stream().findFirst().get() instanceof Double) {
            return "{" + this.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                    .map(i -> String.format("%s=%.2f", i.getKey(), this.getAsDouble(i.getKey())))
                    .collect(Collectors.joining(", ")) + "}";
        } else {
            return "{" + this.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                    .map(i -> String.format("%s=%s", i.getKey(), i.getValue())).collect(Collectors.joining(", ")) + "}";
        }
    }*/
}
