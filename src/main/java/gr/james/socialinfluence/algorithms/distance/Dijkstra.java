package gr.james.socialinfluence.algorithms.distance;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.VertexPair;
import gr.james.socialinfluence.util.collections.VertexSequence;

import java.util.*;

public class Dijkstra {
    public static Map<Vertex, Double> execute(Graph g, Vertex source) {
        Map<Vertex, Collection<VertexSequence>> paths = executeWithPathInternal(g, source, true);
        Map<Vertex, Double> r = new HashMap<>();
        for (Vertex v : paths.keySet()) {
            r.put(v, paths.get(v).iterator().next().getDistance());
        }
        return Collections.unmodifiableMap(r);
    }

    public static Map<Vertex, Collection<VertexSequence>> executeWithPath(Graph g, Vertex source) {
        return executeWithPathInternal(g, source, false);
    }

    public static Map<VertexPair, Double> executeDistanceMap(Graph g) {
        HashMap<VertexPair, Double> dist = new HashMap<>();
        for (Vertex v : g) {
            Map<Vertex, Double> temp = Dijkstra.execute(g, v);
            for (Map.Entry<Vertex, Double> e : temp.entrySet()) {
                dist.put(new VertexPair(v, e.getKey()), e.getValue());
            }
        }
        return Collections.unmodifiableMap(dist);
    }

    private static Map<Vertex, Collection<VertexSequence>> executeWithPathInternal(Graph g, Vertex source, boolean dummyPaths) {
        Map<Vertex, DijkstraNode> nodeMap = new HashMap<>();
        for (Vertex v : g) {
            nodeMap.put(v, new DijkstraNode(v, Double.POSITIVE_INFINITY));
        }

        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();

        nodeMap.get(source).distance = 0.0;
        pq.offer(nodeMap.get(source));

        while (!pq.isEmpty()) {
            DijkstraNode u = pq.poll();

            for (Map.Entry<Vertex, Edge> e : g.getOutEdges(u.vertex).entrySet()) {
                DijkstraNode v = nodeMap.get(e.getKey());
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

        Map<Vertex, Collection<VertexSequence>> r = new HashMap<>();
        for (DijkstraNode e : nodeMap.values()) {
            Collection<VertexSequence> shortestRoutes;
            if (dummyPaths) {
                VertexSequence h = new VertexSequence(new ArrayList<>(), e.distance);
                shortestRoutes = new ArrayList<>();
                shortestRoutes.add(h);
            } else {
                shortestRoutes = e.getShortestPaths();
            }
            r.put(e.vertex, shortestRoutes);
        }
        return Collections.unmodifiableMap(r);
    }

    private static class DijkstraNode implements Comparable {
        public Vertex vertex;
        public double distance;
        public Set<DijkstraNode> parents;

        public DijkstraNode(Vertex vertex, double distance) {
            this.vertex = vertex;
            this.parents = new HashSet<>();
            this.distance = distance;
        }

        public Collection<VertexSequence> getShortestPaths() {
            Collection<List<DijkstraNode>> r = this.getPathsToTop();

            Collection<VertexSequence> u = new ArrayList<>();
            for (List<DijkstraNode> l : r) {
                List<Vertex> ll = new ArrayList<>();
                ListIterator<DijkstraNode> li = l.listIterator(l.size() - 1);
                while (li.hasPrevious()) {
                    ll.add(li.previous().vertex);
                }
                u.add(new VertexSequence(ll, this.distance));
            }

            return Collections.unmodifiableCollection(u);
        }

        private Collection<List<DijkstraNode>> getPathsToTop() {
            Collection<List<DijkstraNode>> r = new ArrayList<>();
            List<DijkstraNode> first = new ArrayList<>();
            first.add(this);
            r.add(first);

            boolean done = false;
            while (!done) {
                done = true;
                for (List<DijkstraNode> e : r) {
                    DijkstraNode v = e.get(e.size() - 1);
                    if (!v.parents.isEmpty()) {
                        done = false;
                        r.remove(e);
                        for (DijkstraNode n : v.parents) {
                            List<DijkstraNode> copy = new ArrayList<>(e);
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
