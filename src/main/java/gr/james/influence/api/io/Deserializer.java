package gr.james.influence.api.io;

@FunctionalInterface
public interface Deserializer<T> {
    T deserialize(String o);
}
