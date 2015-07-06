package gr.james.socialinfluence.api;

public abstract class AbstractEvolvingGenerator<T extends Graph> implements EvolvingGraphGenerator<T> {
    @Override
    public T create() {
        T r = null;
        while (this.canEvolve()) {
            r = this.evolve();
        }
        this.reset();
        return r;
    }
}
