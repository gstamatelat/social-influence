package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.scoring.util.IterativeAlgorithmHelper;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Edge;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;
import gr.james.influence.util.collections.Weighted;

import java.util.Map;

public class HITS {
    public static GraphState<HITSScore> execute(Graph g, double epsilon) {
        return IterativeAlgorithmHelper.execute(
                g,
                new GraphState<>(g, new HITSScore(0.0, 1.0)),
                old -> {
                    GraphState<HITSScore> next = new GraphState<>(g, new HITSScore(0.0, 0.0));

                    for (Vertex v : g) {
                        Map<Vertex, Weighted<Edge, Double>> inEdges = g.getInEdges(v);
                        for (Map.Entry<Vertex, Weighted<Edge, Double>> e : inEdges.entrySet()) {
                            next.put(v, next.get(v).addToAuthority(e.getValue().getWeight() * old.get(e.getKey()).getHub()));
                        }
                    }

                    for (Vertex v : g) {
                        Map<Vertex, Weighted<Edge, Double>> outEdges = g.getOutEdges(v);
                        for (Map.Entry<Vertex, Weighted<Edge, Double>> e : outEdges.entrySet()) {
                            next.put(v, next.get(v).addToHub(e.getValue().getWeight() * next.get(e.getKey()).getAuthority()));
                        }
                    }

                    //final double authoritySum = next.values().stream().mapToDouble(HITSScore::getAuthority).sum();
                    final double authoritySum = Math.sqrt(next.values().stream()
                            .mapToDouble(x -> Math.pow(x.getAuthority(), 2.0)).sum());
                    //final double hubSum = next.values().stream().mapToDouble(HITSScore::getHub).sum();
                    final double hubSum = Math.sqrt(next.values().stream()
                            .mapToDouble(x -> Math.pow(x.getHub(), 2.0)).sum());

                    next.replaceAll((vertex, hitsScore) -> new HITSScore(
                            hitsScore.getAuthority() * g.getVerticesCount() / authoritySum,
                            hitsScore.getHub() * g.getVerticesCount() / hubSum)
                    );

                    return next;
                },
                (t1, t2, e) -> Math.abs(t1.authority - t2.authority) <= e && Math.abs(t1.hub - t2.hub) <= e,
                epsilon
        );
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
