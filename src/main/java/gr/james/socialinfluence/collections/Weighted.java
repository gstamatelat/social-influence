package gr.james.socialinfluence.collections;

public class Weighted<T, W extends Comparable<W>> implements Comparable<Weighted<T, W>> {
    private T o;
    private W w;

    public Weighted(T o, W w) {
        this.o = o;
        this.w = w;
    }

    public T getObject() {
        return o;
    }

    @Override
    public int compareTo(Weighted<T, W> o) {
        return this.w.compareTo(o.w);
    }
}
