package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.AbstractIterativeAlgorithm;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

public class HITS<V> extends AbstractIterativeAlgorithm<V, HITS.HITSScore> {
    public static final double DEFAULT_PRECISION = -1.0;

    private double epsilon;

    public HITS(DirectedGraph<V, ?> g, double epsilon) {
        super(g, GraphState.create(g.vertexSet(), new HITSScore(0.0, 1.0)));
        this.epsilon = epsilon;
    }

    public static <V> GraphState<V, HITSScore> execute(DirectedGraph<V, ?> g, double epsilon) {
        return new HITS<>(g, epsilon).run();
    }

    public static <V> GraphState<V, HITSScore> execute(DirectedGraph<V, ?> g) {
        return new HITS<>(g, DEFAULT_PRECISION).run();
    }

    @Override
    protected boolean converges(GraphState<V, HITSScore> previous, GraphState<V, HITSScore> next) {
        for (V v : next.keySet()) {
            if (Math.abs(next.get(v).authority - previous.get(v).authority) > epsilon ||
                    Math.abs(next.get(v).hub - previous.get(v).hub) > epsilon) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected GraphState<V, HITSScore> step(DirectedGraph<V, ?> g, GraphState<V, HITSScore> previous) {
        GraphState<V, HITSScore> next = GraphState.create(g.vertexSet(), new HITSScore(0.0, 0.0));

        for (V v : g) {
            for (DirectedEdge<V, ?> e : g.inEdges(v)) {
                next.put(v, next.get(v).addToAuthority(e.weight() * previous.get(e.source()).getHub()));
            }
        }

        for (V v : g) {
            for (DirectedEdge<V, ?> e : g.outEdges(v)) {
                next.put(v, next.get(v).addToHub(e.weight() * next.get(e.target()).getAuthority()));
            }
        }

        final double authoritySum = Math.sqrt(next.values().stream()
                .mapToDouble(x -> Math.pow(x.getAuthority(), 2.0)).sum());
        final double hubSum = Math.sqrt(next.values().stream()
                .mapToDouble(x -> Math.pow(x.getHub(), 2.0)).sum());

        next.replaceAll((vertex, hitsScore) -> new HITSScore(
                hitsScore.getAuthority() * g.vertexCount() / authoritySum,
                hitsScore.getHub() * g.vertexCount() / hubSum)
        );

        return next;
    }

    public static class HITSScore {
        private double authority;
        private double hub;

        public HITSScore(double authority, double hub) {
            this.authority = authority;
            this.hub = hub;
        }

        public HITSScore addToHub(double by) {
            HITSScore s = new HITSScore(this.authority, this.hub);
            s.hub += by;
            return s;
        }

        public HITSScore addToAuthority(double by) {
            HITSScore s = new HITSScore(this.authority, this.hub);
            s.authority += by;
            return s;
        }

        public double getAuthority() {
            return authority;
        }

        public double getHub() {
            return hub;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HITSScore hitsScore = (HITSScore) o;

            return Double.compare(hitsScore.authority, authority) == 0 && Double.compare(hitsScore.hub, hub) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(authority);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(hub);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return String.format("{authority=%.2f,hub=%.2f}", this.authority, this.hub);
        }
    }
}
