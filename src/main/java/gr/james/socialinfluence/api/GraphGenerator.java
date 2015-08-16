package gr.james.socialinfluence.api;

@FunctionalInterface
public interface GraphGenerator<T extends Graph> {
    T create();
}
