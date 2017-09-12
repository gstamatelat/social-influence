package gr.james.influence.api;

@FunctionalInterface
public interface Deserializer<T> {
    T deserialize(String o);
}
