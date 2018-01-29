package gr.james.influence.io;

@FunctionalInterface
public interface Serializer<T> {
    String serialize(T o);
}
