package gr.james.influence.api;

@FunctionalInterface
public interface Serializer<T> {
    String serialize(T o);
}
