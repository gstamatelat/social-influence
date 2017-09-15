package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.AbstractIterativeAlgorithm;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.collections.GraphState;

import java.util.Map;

public class HITS<V> extends AbstractIterativeAlgorithm<V, HITS.HITSScore> {
    public static final double DEFAULT_PRECISION = -1.0;

    private double epsilon;

    public HITS(Graph<V, ?> g, double epsilon) {
        super(g, GraphState.create(g.getVertices(), new HITSScore(0.0, 1.0)));
        this.epsilon = epsilon;
    }

    public static <V> GraphState<V, HITSScore> execute(Graph<V, ?> g, double epsilon) {
        return new HITS<>(g, epsilon).run();
    }

    public static <V> GraphState<V, HITSScore> execute(Graph<V, ?> g) {
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
    protected GraphState<V, HITSScore> step(Graph<V, ?> g, GraphState<V, HITSScore> previous) {
        GraphState<V, HITSScore> next = GraphState.create(g.getVertices(), new HITSScore(0.0, 0.0));

        for (V v : g) {
            Map<V, ? extends GraphEdge<V, ?>> inEdges = g.getInEdges(v);
            for (Map.Entry<V, ? extends GraphEdge<V, ?>> e : inEdges.entrySet()) {
                next.put(v, next.get(v).addToAuthority(e.getValue().getWeight() * previous.get(e.getKey()).getHub()));
            }
        }

        for (V v : g) {
            Map<V, ? extends GraphEdge<V, ?>> outEdges = g.getOutEdges(v);
            for (Map.Entry<V, ? extends GraphEdge<V, ?>> e : outEdges.entrySet()) {
                next.put(v, next.get(v).addToHub(e.getValue().getWeight() * next.get(e.getKey()).getAuthority()));
            }
        }

        final double authoritySum = Math.sqrt(next.values().stream()
                .mapToDouble(x -> Math.pow(x.getAuthority(), 2.0)).sum());
        final double hubSum = Math.sqrt(next.values().stream()
                .mapToDouble(x -> Math.pow(x.getHub(), 2.0)).sum());

        next.replaceAll((vertex, hitsScore) -> new HITSScore(
                hitsScore.getAuthority() * g.getVerticesCount() / authoritySum,
                hitsScore.getHub() * g.getVerticesCount() / hubSum)
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
