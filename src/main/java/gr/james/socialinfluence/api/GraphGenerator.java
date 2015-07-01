package gr.james.socialinfluence.api;

public interface GraphGenerator<T extends Graph> {
    T create();
}