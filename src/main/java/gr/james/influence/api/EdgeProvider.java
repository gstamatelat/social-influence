package gr.james.influence.api;

@FunctionalInterface
public interface EdgeProvider<E> {
    E get();
}
