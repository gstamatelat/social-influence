package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.collections.Pair;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.WeightedRandom;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph extends Graph {
    private Map<Vertex, Pair<Map<Vertex, Edge>>> m;

    public MemoryGraph() {
        this.m = new LinkedHashMap<>();
    }

    public Vertex addVertex(Vertex v) {
        Pair<Map<Vertex, Edge>> pp = new Pair<Map<Vertex, Edge>>(new LinkedHashMap<Vertex, Edge>(), new LinkedHashMap<Vertex, Edge>());
        this.m.put(v, pp);
        return v;
    }

    public Graph removeVertex(Vertex v) {
        if (!this.containsVertex(v)) {
            throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "removeVertex");
        }
        for (Map.Entry<Vertex, Pair<Map<Vertex, Edge>>> e : this.m.entrySet()) {
            e.getValue().getFirst().remove(v);
            e.getValue().getSecond().remove(v);
        }
        this.m.remove(v);
        return this;
    }

    public Graph clear() {
        this.m.clear();
        return this;
    }

    public boolean containsVertex(Vertex v) {
        return this.m.containsKey(v);
    }

    public Vertex getVertexFromIndex(int index) {
        if (index < 0 || index >= this.getVerticesCount()) {
            throw new GraphException(Finals.E_GRAPH_INDEX_OUT_OF_BOUNDS, index);
        }
        Iterator<Vertex> it = this.m.keySet().iterator();
        Vertex v = it.next();
        while (index-- > 0) {
            v = it.next();
        }
        return v;
    }

    public Edge addEdge(Vertex source, Vertex target) {
        if (!this.containsVertex(source) || !this.containsVertex(target)) {
            throw new GraphException(Finals.E_GRAPH_EDGE_DIFFERENT);
        }
        Edge e = new Edge();
        if (!this.m.get(source).getFirst().containsKey(target)) {
            this.m.get(source).getFirst().put(target, e);
            this.m.get(target).getSecond().put(source, e);
            return e;
        } else {
            return null;
        }
    }

    /*public Graph removeEdge(Edge e) {
        this.edges.remove(e);
        return this;
    }*/

    public Graph removeEdge(Vertex source, Vertex target) {
        this.m.get(source).getFirst().remove(target);
        this.m.get(target).getSecond().remove(source);
        return this;
    }

    /*public Map<Vertex, Set<Edge>> getOutEdges() {
        Map<Vertex, Set<Edge>> map = new HashMap<>();
        for (Vertex v : this.vertices) {
            map.put(v, new HashSet<Edge>());
        }
        for (Edge e : this.edges) {
            map.get(e.getSource()).add(e);
        }
        Map<Vertex, Set<Edge>> unmodifiableMap = new HashMap<>();
        for (Vertex v : map.keySet()) {
            unmodifiableMap.put(v, Collections.unmodifiableSet(map.get(v)));
        }
        return Collections.unmodifiableMap(unmodifiableMap);
    }*/

    public Map<Vertex, Edge> getOutEdges(Vertex v) {
        return Collections.unmodifiableMap(this.m.get(v).getFirst());
    }

    public Map<Vertex, Edge> getInEdges(Vertex v) {
        return Collections.unmodifiableMap(this.m.get(v).getSecond());
    }

    /*public Map<Vertex, Set<Edge>> getInEdges() {
        Map<Vertex, Set<Edge>> map = new HashMap<>();
        for (Vertex v : this.vertices) {
            map.put(v, new HashSet<Edge>());
        }
        for (Edge e : this.edges) {
            map.get(e.getTarget()).add(e);
        }
        Map<Vertex, Set<Edge>> unmodifiableMap = new HashMap<>();
        for (Vertex v : map.keySet()) {
            unmodifiableMap.put(v, Collections.unmodifiableSet(map.get(v)));
        }
        return Collections.unmodifiableMap(unmodifiableMap);
    }*/

    /*public Map<Vertex, Integer> getOutDegree() {
        Map<Vertex, Integer> outDegrees = new HashMap<>();
        for (Vertex v : this.vertices) {
            outDegrees.put(v, 0);
        }
        for (Edge e : this.edges) {
            outDegrees.put(e.getSource(), outDegrees.get(e.getSource()) + 1);
        }
        return Collections.unmodifiableMap(outDegrees);
    }*/

    /*public Map<Vertex, Integer> getInDegree() {
        Map<Vertex, Integer> inDegrees = new HashMap<>();
        for (Vertex v : this.vertices) {
            inDegrees.put(v, 0);
        }
        for (Edge e : this.edges) {
            inDegrees.put(e.getTarget(), inDegrees.get(e.getTarget()) + 1);
        }
        return Collections.unmodifiableMap(inDegrees);
    }*/

    public Graph createCircle(boolean undirected) {
        // TODO: Not tested
        Iterator<Vertex> vertexIterator = this.m.keySet().iterator();
        Vertex previous = vertexIterator.next();
        Vertex first = previous;
        while (vertexIterator.hasNext()) {
            Vertex next = vertexIterator.next();
            // TODO: Should only add if not exists in order to leave the weight unmodified
            this.addEdge(previous, next, undirected);
            previous = next;
        }
        this.addEdge(previous, first, undirected);
        return this;
    }

    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(this.m.keySet());
    }

    public Vertex getRandomOutEdge(Vertex from, boolean weighted) {
        HashMap<Vertex, Double> weightMap = new HashMap<>();
        Map<Vertex, Edge> outEdges = this.getOutEdges(from);
        for (Map.Entry<Vertex, Edge> e : outEdges.entrySet()) {
            weightMap.put(e.getKey(), (weighted ? e.getValue().getWeight() : 1.0));
        }
        List<Vertex> edges = WeightedRandom.makeRandomSelection(weightMap, 1);
        return edges.get(0);
    }
}