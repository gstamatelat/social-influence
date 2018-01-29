package gr.james.influence.io;

@FunctionalInterface
public interface Deserializer<T> {
    T deserialize(String o);
}
