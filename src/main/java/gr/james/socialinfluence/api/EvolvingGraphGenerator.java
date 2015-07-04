package gr.james.socialinfluence.api;

public interface EvolvingGraphGenerator<T extends Graph> extends GraphGenerator<T> {
    T evolve();

    boolean canEvolve();

    void reset();
}
