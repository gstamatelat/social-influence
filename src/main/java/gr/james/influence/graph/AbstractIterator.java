package gr.james.influence.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class AbstractIterator<T> implements Iterator<T> {
    private T next;

    AbstractIterator() {
        init();
        this.next = computeNext();
    }

    abstract T computeNext();

    abstract void init();

    @Override
    public final boolean hasNext() {
        return next != null;
    }

    @Override
    public final T next() {
        final T r = this.next;
        if (r == null) {
            throw new NoSuchElementException();
        }
        this.next = computeNext();
        return r;
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
