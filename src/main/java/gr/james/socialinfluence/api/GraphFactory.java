package gr.james.socialinfluence.api;

public interface GraphFactory<T extends Graph> {
    T create();
}
