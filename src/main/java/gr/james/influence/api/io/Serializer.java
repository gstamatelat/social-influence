package gr.james.influence.api.io;

@FunctionalInterface
public interface Serializer<T> {
    String serialize(T o);
}
