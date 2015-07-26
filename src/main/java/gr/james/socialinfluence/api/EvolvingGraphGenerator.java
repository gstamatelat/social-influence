package gr.james.socialinfluence.api;

public interface EvolvingGraphGenerator<T extends Graph> extends GraphGenerator<T> {
    T evolve();

    boolean canEvolve();

    void reset();

    default T create() {
        T r = null;
        while (this.canEvolve()) {
            r = this.evolve();
        }
        this.reset();
        return r;
    }
}
