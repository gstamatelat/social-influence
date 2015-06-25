package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.graph.algorithms.Dijkstra;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.helper.WeightedRandom;

import java.util.*;

/**
 * <p>Represents an in-memory {@link Graph}, implemented using adjacency lists. Suitable for sparse graphs.</p>
 */
public class MemoryGraph extends Graph {
    private Set<Vertex> vertices;
    private Set<Edge> edges;

    /**
     * <p>Creates a new empty graph with name {@link Finals#DEFAULT_GRAPH_NAME} and default
     * {@link MemoryGraphOptions options}.</p>
     */
    public MemoryGraph() {
        this(EnumSet.noneOf(MemoryGraphOptions.class));
    }

    /**
     * <p>Creates a new empty graph with name {@link Finals#DEFAULT_GRAPH_NAME} and the specified
     * {@link MemoryGraphOptions options}.</p>
     *
     * @param e the options to use when constructing this Graph
     */
    public MemoryGraph(EnumSet<MemoryGraphOptions> e) {
        if (e.contains(MemoryGraphOptions.VERTEX_USE_LINKED_HASH_SET)) {
            this.vertices = new LinkedHashSet<>();
        } else {
            this.vertices = new HashSet<>();
        }
        if (e.contains(MemoryGraphOptions.EDGE_USE_LINKED_HASH_SET)) {
            this.edges = new LinkedHashSet<>();
        } else {
            this.edges = new HashSet<>();
        }
    }

    public Vertex addVertex(Vertex v) {
        this.vertices.add(v);
        return v;
    }

    /**
     * {@inheritDoc}
     * <p><b>Running Time:</b> O(n)</p>
     */
    public Graph removeVertex(Vertex v) {
        for (Iterator<Edge> i = this.edges.iterator(); i.hasNext(); ) {
            Edge e = i.next();
            if (e.getSource().equals(v) || e.getTarget().equals(v)) {
                i.remove();
            }
        }
        this.vertices.remove(v);
        return this;
    }

    public Graph clear() {
        this.vertices.clear();
        this.edges.clear();
        return this;
    }

    public Vertex getVertexFromId(int id) {
        for (Vertex v : this.vertices) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public boolean containsVertex(Vertex v) {
        return this.vertices.contains(v);
    }

    public Vertex getVertexFromIndex(int index) {
        if (index < 0 || index >= this.getVerticesCount()) {
            throw new GraphException(Finals.E_GRAPH_INDEX_OUT_OF_BOUNDS, index);
        }
        TreeSet<Vertex> allVertices = new TreeSet<>();
        allVertices.addAll(this.vertices);
        Iterator<Vertex> it = allVertices.iterator();
        Vertex v = it.next();
        while (index-- > 0) {
            v = it.next();
        }
        return v;
    }

    public Vertex fuseVertices(Vertex[] f) {
        Vertex v = this.addVertex();

        for (Vertex y : f) {
            for (Edge e : this.getOutEdges(y)) {
                this.addEdge(v, e.getTarget()).setWeight(e.getWeight());
            }
            for (Edge e : this.getInEdges(y)) {
                this.addEdge(e.getSource(), v).setWeight(e.getWeight());
            }
            this.removeVertex(y);
        }

        return v;
    }

    public Vertex getRandomVertex() {
        // TODO: There must be some better way ...
        RandomVertexIterator rvi = new RandomVertexIterator(this);
        return rvi.next();
    }

    public Set<Vertex> getStubbornVertices() {
        Set<Vertex> stubborn = new TreeSet<>();
        for (Vertex v : this.vertices) {
            if (this.getOutDegree(v) == 1 && this.getOutEdges(v).iterator().next().getTarget().equals(v)) {
                stubborn.add(v);
            }
        }
        return Collections.unmodifiableSet(stubborn);
    }

    public Graph connectAllVertices() {
        for (Vertex v : vertices) {
            for (Vertex w : vertices) {
                if (!v.equals(w)) {
                    this.addEdge(v, w);
                }
            }
        }
        return this;
    }

    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(this.edges);
    }

    public int getEdgesCount() {
        return this.edges.size();
    }

    public Edge addEdge(Vertex source, Vertex target) {
        if (!this.vertices.contains(source) || !this.vertices.contains(target)) {
            throw new GraphException(Finals.E_GRAPH_EDGE_DIFFERENT);
        }
        Edge e = new Edge(source, target);
        this.edges.add(e);
        return e;
    }

    public Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected) {
        Set<Edge> addedEdges = new HashSet<>();
        addedEdges.add(this.addEdge(source, target));
        if (undirected) {
            addedEdges.add(this.addEdge(target, source));
        }
        return Collections.unmodifiableSet(addedEdges);
    }

    public Graph removeEdge(Edge e) {
        this.edges.remove(e);
        return this;
    }

    public Graph removeEdge(Vertex source, Vertex target) {
        // TODO: Consider doing something like this.remove(new Edge(source, target))
        Edge candidate = null;
        for (Edge e : this.edges) {
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                candidate = e;
                break;
            }
        }
        if (candidate != null) {
            this.removeEdge(candidate);
        }
        return this;
    }

    public Map<Vertex, Set<Edge>> getOutEdges() {
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
    }

    public Set<Edge> getOutEdges(Vertex v) {
        Set<Edge> outEdges = new HashSet<>();
        for (Edge e : this.edges) {
            if (e.getSource().equals(v)) {
                outEdges.add(e);
            }
        }
        return Collections.unmodifiableSet(outEdges);
    }

    public Map<Vertex, Set<Edge>> getInEdges() {
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
    }

    public Set<Edge> getInEdges(Vertex v) {
        Set<Edge> inEdges = new HashSet<>();
        for (Edge e : this.edges) {
            if (e.getTarget().equals(v)) {
                inEdges.add(e);
            }
        }
        return Collections.unmodifiableSet(inEdges);
    }

    public double getOutWeightSum(Vertex v) {
        return Helper.getWeightSum(this.getOutEdges(v));
    }

    public double getInWeightSum(Vertex v) {
        return Helper.getWeightSum(this.getInEdges(v));
    }

    public Map<Vertex, Integer> getOutDegree() {
        Map<Vertex, Integer> outDegrees = new HashMap<>();
        for (Vertex v : this.vertices) {
            outDegrees.put(v, 0);
        }
        for (Edge e : this.edges) {
            outDegrees.put(e.getSource(), outDegrees.get(e.getSource()) + 1);
        }
        return Collections.unmodifiableMap(outDegrees);
    }

    public int getOutDegree(Vertex v) {
        return this.getOutEdges(v).size();
    }

    public Map<Vertex, Integer> getInDegree() {
        Map<Vertex, Integer> inDegrees = new HashMap<>();
        for (Vertex v : this.vertices) {
            inDegrees.put(v, 0);
        }
        for (Edge e : this.edges) {
            inDegrees.put(e.getTarget(), inDegrees.get(e.getTarget()) + 1);
        }
        return Collections.unmodifiableMap(inDegrees);
    }

    public int getInDegree(Vertex v) {
        return this.getInEdges(v).size();
    }

    public boolean isUndirected() {
        // TODO: Not sure if this method is slow. Could be very slow.
        // TODO: Not tested
        ArrayList<Vertex[]> edgeList = new ArrayList<>();
        for (Edge e : this.edges) {
            Vertex v = e.getSource();
            Vertex w = e.getTarget();
            if (!v.equals(w)) {
                int indexOfOpposite = -1;
                for (int i = 0; i < edgeList.size(); i++) {
                    if (edgeList.get(i)[0].equals(w) && edgeList.get(i)[1].equals(v)) {
                        indexOfOpposite = i;
                        break;
                    }
                }
                if (indexOfOpposite > -1) {
                    edgeList.remove(indexOfOpposite);
                } else {
                    edgeList.add(new Vertex[]{v, w});
                }
            }
        }
        return edgeList.size() == 0;
    }

    public Graph createCircle(boolean undirected) {
        // TODO: Not tested
        Iterator<Vertex> vertexIterator = this.vertices.iterator();
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
        return Collections.unmodifiableSet(this.vertices);
    }

    public int getVerticesCount() {
        return this.vertices.size();
    }

    public Edge getRandomOutEdge(Vertex from, boolean weighted) {
        HashMap<Edge, Double> weightMap = new HashMap<>();
        Set<Edge> outEdges = this.getOutEdges(from);
        for (Edge e : outEdges) {
            weightMap.put(e, (weighted ? e.getWeight() : 1.0));
        }
        List<Edge> edges = WeightedRandom.makeRandomSelection(weightMap, 1);
        return edges.get(0);
    }

    public double getDiameter() {
        // TODO: Should return a list/path/walk of vertices to show both the weight sum and the steps
        HashMap<Vertex[], Double> distanceMap = new HashMap<>();

        for (Vertex v : this.getVertices()) {
            HashMap<Vertex, Double> temp = Dijkstra.execute(this, v);
            for (Map.Entry<Vertex, Double> e : temp.entrySet()) {
                distanceMap.put(new Vertex[]{v, e.getKey()}, e.getValue());
            }
        }

        double diameter = 0;
        for (Double d : distanceMap.values()) {
            if (d > diameter) {
                diameter = d;
            }
        }

        return diameter;
    }
}