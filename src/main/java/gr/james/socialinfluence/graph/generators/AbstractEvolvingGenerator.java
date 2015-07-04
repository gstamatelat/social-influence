package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.EvolvingGraphGenerator;
import gr.james.socialinfluence.api.Graph;

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
