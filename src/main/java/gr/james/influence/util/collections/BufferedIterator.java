package gr.james.influence.util.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class provides a skeletal implementation of {@link Iterator} and is intended to be used in situations where the
 * return value of {@link #hasNext()} cannot be determined in constant time before invoking {@link #next()}.
 * <p>
 * An example use case of this class is an {@code Iterator} that iterates through all non-zero elements of an
 * {@code int} array. In this case, using a naive {@link Iterator} implementation, both {@link #hasNext()} and
 * {@link #next()} run in {@code O(n)} time because they have to traverse the array to find the next non-zero value.
 * <p>
 * The way {@code BufferedIterator} works is by buffering iterator elements eagerly before they are returned so that the
 * {@link #hasNext()} method runs in constant time.
 * <p>
 * Implementations need only implement the method {@link #proceed()}.
 *
 * @param <E> the type of elements returned by this iterator
 */
public abstract class BufferedIterator<E> implements Iterator<E> {
    private boolean hasNext = true;
    private Iterator<E> buffer;

    /**
     * Construct a new {@link BufferedIterator}.
     * <p>
     * The constructor will invoke the method {@link #proceed()} once.
     */
    public BufferedIterator() {
        init();
        buffer = proceed();
        if (!buffer.hasNext()) {
            hasNext = false;
        }
    }

    /**
     * Returns the next group of elements that should be returned by this iterator.
     * <p>
     * If the iteration is finished this method should return an empty iterator.
     * <p>
     * This method is similar to the {@link #next()} method but has the following differences:
     * <ul>
     * <li>Returns an {@link Iterator} of elements instead of a single element</li>
     * <li>Returns an empty {@link Iterator} instead of throwing {@link NoSuchElementException}</li>
     * </ul>
     * <p>
     * This method is invoked by the constructor. This is important because the programmer that is overriding this class
     * has to avoid using uninitialized fields. The {@link #init()} method is appropriate to initialize fields that are
     * used inside the {@code #proceed()} method.
     *
     * @return the next group of elements that should be returned by this iterator
     */
    protected abstract Iterator<E> proceed();

    /**
     * Perform initialization logic.
     * <p>
     * This method is invoked in the constructor and is the recommended way to initialize fields that are used inside
     * the {@link #proceed()} method.
     */
    protected void init() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean hasNext() {
        return hasNext;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    @Override
    public final E next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        assert buffer.hasNext();
        final E n = buffer.next();
        if (!buffer.hasNext()) {
            buffer = proceed();
            if (!buffer.hasNext()) {
                hasNext = false;
            }
        }
        return n;
    }
}
