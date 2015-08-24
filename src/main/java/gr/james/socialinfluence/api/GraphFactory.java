package gr.james.socialinfluence.api;

@FunctionalInterface
public interface GraphFactory<T extends Graph> {
    T create();
}
