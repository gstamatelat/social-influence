package gr.james.socialinfluence.util.collections;

import gr.james.socialinfluence.util.Conditions;

import java.util.LinkedHashSet;

/**
 * <p>Represents a {@link LinkedHashSet} with a maximum capacity. When this maximum capacity is exceeded, the oldest
 * element is removed. This structure is often called buffer; this one has unique elements.</p>
 *
 * @param <E> the type of the elements
 */
public class EvictingLinkedHashSet<E> {
    LinkedHashSet<E> s = new LinkedHashSet<>();
    private int maxSize;

    public EvictingLinkedHashSet(int maxSize) {
        setMaxSize(maxSize);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        Conditions.requireArgument(maxSize >= 1, "maxSize >= 1");
        this.maxSize = maxSize;
    }

    public void increaseMaxSize() {
        setMaxSize(maxSize + 1);
    }

    public boolean add(E e) {
        boolean r = s.add(e);
        if (s.size() > maxSize) {
            s.remove(s.iterator().next());
        }
        return r;
    }
}
