package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.collections.VertexPair;
import gr.james.influence.util.collections.VertexSequence;

import java.util.*;

@Deprecated
public class Dijkstra {
    public static <V, E> Map<V, Double> execute(Graph<V, E> g, V source) {
        Map<V, Collection<VertexSequence<V>>> paths = executeWithPathInternal(g, source, true);
        Map<V, Double> r = new HashMap<>();
        for (V v : paths.keySet()) {
            r.put(v, paths.get(v).iterator().next().getDistance());
        }
        return Collections.unmodifiableMap(r);
    }

    public static <V, E> Map<V, Collection<VertexSequence<V>>> executeWithPath(Graph<V, E> g, V source) {
        return executeWithPathInternal(g, source, false);
    }

    public static <V, E> Map<VertexPair<V>, Double> executeDistanceMap(Graph<V, E> g) {
        HashMap<VertexPair<V>, Double> dist = new HashMap<>();
        for (V v : g) {
            Map<V, Double> temp = Dijkstra.execute(g, v);
            for (Map.Entry<V, Double> e : temp.entrySet()) {
                dist.put(new VertexPair<>(v, e.getKey()), e.getValue());
            }
        }
        return Collections.unmodifiableMap(dist);
    }

    private static <V, E> Map<V, Collection<VertexSequence<V>>> executeWithPathInternal(Graph<V, E> g, V source, boolean dummyPaths) {
        Map<V, DijkstraNode<V>> nodeMap = new HashMap<>();
        for (V v : g) {
            nodeMap.put(v, new DijkstraNode<>(v, Double.POSITIVE_INFINITY));
        }

        PriorityQueue<DijkstraNode<V>> pq = new PriorityQueue<>();

        nodeMap.get(source).distance = 0.0;
        pq.offer(nodeMap.get(source));

        while (!pq.isEmpty()) {
            DijkstraNode<V> u = pq.poll();

            for (Map.Entry<V, GraphEdge<V, E>> e : g.getOutEdges(u.vertex).entrySet()) {
                DijkstraNode<V> v = nodeMap.get(e.getKey());
                double weight = e.getValue().getWeight();
                double distanceThroughU = u.distance + weight;
                if (distanceThroughU <= v.distance) {
                    pq.remove(v);
                    v.distance = distanceThroughU;
                    v.parents.add(nodeMap.get(u.vertex));
                    pq.add(v);
                }
            }
        }

        Map<V, Collection<VertexSequence<V>>> r = new HashMap<>();
        for (DijkstraNode<V> e : nodeMap.values()) {
            Collection<VertexSequence<V>> shortestRoutes;
            if (dummyPaths) {
                VertexSequence<V> h = new VertexSequence<>(new ArrayList<>(), e.distance);
                shortestRoutes = new ArrayList<>();
                shortestRoutes.add(h);
            } else {
                shortestRoutes = e.getShortestPaths();
            }
            r.put(e.vertex, shortestRoutes);
        }
        return Collections.unmodifiableMap(r);
    }

    private static class DijkstraNode<V> implements Comparable {
        public V vertex;
        public double distance;
        public Set<DijkstraNode<V>> parents;

        public DijkstraNode(V vertex, double distance) {
            this.vertex = vertex;
            this.parents = new HashSet<>();
            this.distance = distance;
        }

        public Collection<VertexSequence<V>> getShortestPaths() {
            Collection<List<DijkstraNode<V>>> r = this.getPathsToTop();

            Collection<VertexSequence<V>> u = new ArrayList<>();
            for (List<DijkstraNode<V>> l : r) {
                List<V> ll = new ArrayList<>();
                ListIterator<DijkstraNode<V>> li = l.listIterator(l.size() - 1);
                while (li.hasPrevious()) {
                    ll.add(li.previous().vertex);
                }
                u.add(new VertexSequence<>(ll, this.distance));
            }

            return Collections.unmodifiableCollection(u);
        }

        private Collection<List<DijkstraNode<V>>> getPathsToTop() {
            Collection<List<DijkstraNode<V>>> r = new ArrayList<>();
            List<DijkstraNode<V>> first = new ArrayList<>();
            first.add(this);
            r.add(first);

            boolean done = false;
            while (!done) {
                done = true;
                for (List<DijkstraNode<V>> e : r) {
                    DijkstraNode<V> v = e.get(e.size() - 1);
                    if (!v.parents.isEmpty()) {
                        done = false;
                        r.remove(e);
                        for (DijkstraNode<V> n : v.parents) {
                            List<DijkstraNode<V>> copy = new ArrayList<>(e);
                            copy.add(n);
                            r.add(copy);
                        }
                        break;
                    }
                }
            }

            return r;
        }

        @Override
        public int compareTo(Object o) {
            DijkstraNode other = (DijkstraNode) o;
            return Double.compare(this.distance, other.distance);
        }
    }
}
