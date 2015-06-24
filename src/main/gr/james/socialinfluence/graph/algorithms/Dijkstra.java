package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Dijkstra {
    public static HashMap<Vertex, Double> execute(Graph g, Vertex source) {
        HashMap<Vertex, DijkstraNode> nodeMap = new HashMap<Vertex, DijkstraNode>();
        for (Vertex v : g.getVertices()) {
            nodeMap.put(v, new DijkstraNode(v, null, Double.POSITIVE_INFINITY));
        }

        PriorityQueue<DijkstraNode> pq = new PriorityQueue<DijkstraNode>();

        nodeMap.get(source).distance = 0.0;
        pq.offer(nodeMap.get(source));

        while (!pq.isEmpty()) {
            DijkstraNode u = pq.poll();

            for (Edge e : u.vertex.getOutEdges()) {
                DijkstraNode v = nodeMap.get(e.getTarget());
                double weight = e.getWeight();
                double distanceThroughU = u.distance + weight;
                if (distanceThroughU < v.distance) {
                    pq.remove(v);
                    v.distance = distanceThroughU;
                    v.parent = u.vertex;
                    pq.add(v);
                }
            }
        }

        HashMap<Vertex, Double> r = new HashMap<Vertex, Double>();
        for (DijkstraNode e : nodeMap.values()) {
            r.put(e.vertex, e.distance);
        }
        return r;
    }

    private static class DijkstraNode implements Comparable {
        // TODO: We already have ObjectWithWeight, maybe use it here and ditch this class
        public Vertex vertex;
        public double distance;
        public Vertex parent;

        public DijkstraNode(Vertex vertex, Vertex parent, double distance) {
            this.vertex = vertex;
            this.parent = parent;
            this.distance = distance;
        }

        @Override
        public int compareTo(Object o) {
            DijkstraNode other = (DijkstraNode) o;
            return Double.compare(this.distance, other.distance);
        }

        @Override
        public int hashCode() {
            return this.vertex.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.vertex.equals(obj);
        }
    }
}