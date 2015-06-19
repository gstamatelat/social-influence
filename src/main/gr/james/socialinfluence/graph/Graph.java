package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.graph.algorithms.Dijkstra;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.helper.WeightedRandom;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * <p>Represents a collection of vertices and edges. The graph is weighted directed and there can't be more than one
 * edge from node {@code i} to node {@code j} (it's not a multigraph).</p><p><b>Remark on performance &ndash; </b>Some
 * methods have both single-vertex and multi-vertex variants, for example {@code getDegree(Vertex v)} and {@code
 * getDegree()}. You should prefer the latter when your algorithm needs all the degrees and avoid iterating through all
 * vertices and calling the single-vertex method each time. {@code for (Vertex v: g.getVertices()) { v.getInEdges(); }}
 * construct is to be avoided.</p><p><b>Collections returned &ndash; </b>Methods that return collections
 * ({@link java.util.Map Maps} or {@link java.util.Set Sets}) return read-only versions of them, meaning that you can't
 * insert or remove elements. These collections are also not backed by the graph, changes to the graph won't affect
 * these collections after they have been returned, you need to call the method again. The elements themselves,
 * however, are shallow copies and can be used to change the state of the graph.</p>
 */
public class Graph {
    private String name;
    private String meta;
    private Set<Vertex> vertices;
    private Set<Edge> edges;

    /**
     * <p>Creates a new empty graph with name {@link Finals#DEFAULT_GRAPH_NAME} and default
     * {@link GraphOptions options}.</p>
     */
    public Graph() {
        this(EnumSet.noneOf(GraphOptions.class));
    }

    /**
     * <p>Creates a new empty graph with name {@link Finals#DEFAULT_GRAPH_NAME} and the specified
     * {@link GraphOptions options}.</p>
     *
     * @param e the options to use when constructing this Graph
     */
    public Graph(EnumSet<GraphOptions> e) {
        this.name = Finals.DEFAULT_GRAPH_NAME;
        this.meta = "";
        if (e.contains(GraphOptions.VERTEX_USE_LINKED_HASH_SET)) {
            this.vertices = new LinkedHashSet<Vertex>();
        } else {
            this.vertices = new HashSet<Vertex>();
        }
        if (e.contains(GraphOptions.EDGE_USE_LINKED_HASH_SET)) {
            this.edges = new LinkedHashSet<Edge>();
        } else {
            this.edges = new HashSet<Edge>();
        }
    }

    public static Graph combineGraphs(Graph[] graphs) {
        Graph r = new Graph();
        for (Graph g : graphs) {
            for (Vertex v : g.vertices) {
                v.parentGraph = r;
                r.vertices.add(v);
            }
            for (Edge e : g.edges) {
                r.edges.add(e);
            }
        }
        for (Graph g : graphs) {
            g.vertices.clear();
            g.edges.clear();
        }
        return r;
    }

    /**
     * <p>Sets a new name for this graph. The name is used on printing and other exporting functionality.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @param name The new name for this graph
     * @return this instance
     */
    public Graph setName(String name) {
        this.name = name;
        return this;
    }

    public String getMeta() {
        return this.meta;
    }

    public Graph setMeta(String meta) {
        this.meta = meta;
        return this;
    }

    /**
     * <p>Inserts a new vertex to the graph and returns it. Use {@link #addVertices} for bulk inserts.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the new vertex object
     */
    public Vertex addVertex() {
        Vertex v = new Vertex();
        v.parentGraph = this;
        this.vertices.add(v);
        return v;
    }

    public Vertex addVertex(Vertex v) {
        if (v.getParentGraph() != null && v.getParentGraph() != this) {
            throw new GraphException(Finals.E_GRAPH_VERTEX_BOUND);
        }
        v.parentGraph = this;
        this.vertices.add(v);
        return v;
    }

    public Set<Vertex> addVertices(int count) {
        Set<Vertex> newVertices = new HashSet<Vertex>();
        for (int i = 0; i < count; i++) {
            newVertices.add(this.addVertex());
        }
        return Collections.unmodifiableSet(newVertices);
    }

    /**
     * <p>Removes a vertex from the graph. This method will also remove the inbound and outbound edges of that vertex.
     * </p>
     * <p><b>Running Time:</b> Fast</p>
     *
     * @param v the vertex to be removed
     * @return the current instance
     */
    public Graph removeVertex(Vertex v) {
        for (Iterator<Edge> i = this.edges.iterator(); i.hasNext(); ) {
            Edge e = i.next();
            if (e.getSource().equals(v) || e.getTarget().equals(v)) {
                i.remove();
            }
        }
        this.vertices.remove(v);
        v.parentGraph = null;
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

    /**
     * <p>Get a {@link Vertex} of this graph based on its index. Index is a deterministic, per-graph attribute between
     * {@code 0} (inclusive) and {@link #getVerticesCount()} (exclusive), indicating the rank of the ID of the specific
     * vertex in the ordered ID list.</p>
     *
     * @param index the index of the vertex
     * @return the vertex reference with the provided index
     * @throws GraphException if {@code index} is outside of {@code 0} (inclusive) and {@link #getVerticesCount()}
     *                        (exclusive)
     */
    public Vertex getVertexFromIndex(int index) {
        if (index < 0 || index >= this.getVerticesCount()) {
            throw new GraphException(Finals.E_GRAPH_INDEX_OUT_OF_BOUNDS, index);
        }
        TreeSet<Vertex> allVertices = new TreeSet<Vertex>();
        allVertices.addAll(this.vertices);
        Iterator<Vertex> it = allVertices.iterator();
        Vertex v = it.next();
        while (index-- > 0) {
            v = it.next();
        }
        return v;
    }

    /**
     * <p>Fuses two or more vertices into a single one. This method may cause information loss
     * if there are conflicts on the edges.</p>
     *
     * @param f an array of vertices to be fused
     * @return the vertex that is the result of the fusion
     */
    public Vertex fuseVertices(Vertex[] f) {
        Vertex v = this.addVertex();

        for (Vertex y : f) {
            for (Edge e : y.getOutEdges()) {
                this.addEdge(v, e.getTarget()).setWeight(e.getWeight());
            }
            for (Edge e : y.getInEdges()) {
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

    /**
     * <p>Connects all the vertices in the graph. Does not create self-connections (loops).</p>
     * <p><b>Complexity:</b> O(n<sup>2</sup>)</p>
     *
     * @return the current instance
     */
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

    /**
     * <p>Calculates the total amount of directed edges that this graph has. This method is a little faster than using
     * {@code getEdges().size()}.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of directed edges in this graph
     */
    public int getEdgesCount() {
        return this.edges.size();
    }

    public Edge addEdge(Edge e) {
        if (e.getSource().getParentGraph() != this || e.getTarget().getParentGraph() != this) {
            throw new GraphException(Finals.E_GRAPH_EDGE_DIFFERENT);
        }
        this.edges.add(e);
        return e;
    }

    public Edge addEdge(Vertex source, Vertex target) {
        return this.addEdge(new Edge(source, target));
    }

    public Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected) {
        Set<Edge> addedEdges = new HashSet<Edge>();
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
        Map<Vertex, Set<Edge>> map = new HashMap<Vertex, Set<Edge>>();
        for (Vertex v : this.vertices) {
            map.put(v, new HashSet<Edge>());
        }
        for (Edge e : this.edges) {
            map.get(e.getSource()).add(e);
        }
        Map<Vertex, Set<Edge>> unmodifiableMap = new HashMap<Vertex, Set<Edge>>();
        for (Vertex v : map.keySet()) {
            unmodifiableMap.put(v, Collections.unmodifiableSet(map.get(v)));
        }
        return Collections.unmodifiableMap(unmodifiableMap);
    }

    public Set<Edge> getOutEdges(Vertex v) {
        Set<Edge> outEdges = new HashSet<Edge>();
        for (Edge e : this.edges) {
            if (e.getSource().equals(v)) {
                outEdges.add(e);
            }
        }
        return Collections.unmodifiableSet(outEdges);
    }

    public Map<Vertex, Set<Edge>> getInEdges() {
        Map<Vertex, Set<Edge>> map = new HashMap<Vertex, Set<Edge>>();
        for (Vertex v : this.vertices) {
            map.put(v, new HashSet<Edge>());
        }
        for (Edge e : this.edges) {
            map.get(e.getTarget()).add(e);
        }
        Map<Vertex, Set<Edge>> unmodifiableMap = new HashMap<Vertex, Set<Edge>>();
        for (Vertex v : map.keySet()) {
            unmodifiableMap.put(v, Collections.unmodifiableSet(map.get(v)));
        }
        return Collections.unmodifiableMap(unmodifiableMap);
    }

    public Set<Edge> getInEdges(Vertex v) {
        Set<Edge> inEdges = new HashSet<Edge>();
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
        Map<Vertex, Integer> outDegrees = new HashMap<Vertex, Integer>();
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
        Map<Vertex, Integer> inDegrees = new HashMap<Vertex, Integer>();
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

    /**
     * <p>Returns true if for every edge with source S and target T where S and T are different,
     * there is always an edge with source T and target S.</p>
     * <p><b>Running Time:</b> Slow - Very Slow (depends on the graph)</p>
     *
     * @return true if the graph is undirected, otherwise false
     */
    public boolean isUndirected() {
        // TODO: Not sure if this method is slow. Could be very slow.
        // TODO: Not tested
        ArrayList<Vertex[]> edgeList = new ArrayList<Vertex[]>();
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

    /**
     * <p>Returns an unmodifiable Set of vertices that this graph consists of.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the list of vertices of this graph
     */
    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(this.vertices);
    }

    /**
     * <p>Returns the number of vertices in this graph. This method is faster than using getVertices().size()</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of vertices in this graph
     */
    public int getVerticesCount() {
        return this.vertices.size();
    }

    public Edge getRandomOutEdge(Vertex from, boolean weighted) {
        HashMap<Edge, Double> weightMap = new HashMap<Edge, Double>();
        Set<Edge> outEdges = this.getOutEdges(from);
        for (Edge e : outEdges) {
            weightMap.put(e, (weighted ? e.getWeight() : 1.0));
        }
        List<Edge> edges = WeightedRandom.makeRandomSelection(weightMap, 1, null);
        return edges.get(0);
    }

    public double getDiameter() {
        // TODO: Should return a list/path/walk of vertices to show both the weight sum and the steps
        HashMap<Vertex[], Double> distanceMap = new HashMap<Vertex[], Double>();

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

    /**
     * <p>Exports this graph in DOT format. If the graph is undirected, then the undirected DOT format will be used.
     * This method uses UTF-8 as character set when writing to the stream.</p>
     * <p><b>Running Time:</b> Slow - Very Slow (depends on the graph)</p>
     *
     * @param out the OutputStream to write the DOT file to
     * @return the current instance
     */
    public Graph exportToDot(OutputStream out) {
        if (this.isUndirected()) {
            ArrayList<Vertex[]> edgeList = new ArrayList<Vertex[]>();
            for (Edge e : this.edges) {
                Vertex v = e.getSource();
                Vertex w = e.getTarget();
                int indexOfOpposite = -1;
                for (int i = 0; i < edgeList.size(); i++) {
                    if (edgeList.get(i)[0].equals(w) && edgeList.get(i)[1].equals(v)) {
                        indexOfOpposite = i;
                        break;
                    }
                }
                if (indexOfOpposite == -1) {
                    edgeList.add(new Vertex[]{v, w});
                }
            }

            String dot = "graph " + this.name + " {" + System.lineSeparator();
            dot += "  overlap = false;" + System.lineSeparator();
            dot += "  bgcolor = transparent;" + System.lineSeparator();
            dot += "  splines = true;" + System.lineSeparator();
            dot += "  dpi = 192;" + System.lineSeparator();
            dot += System.lineSeparator();
            dot += "  graph [fontname = \"Noto Sans\"];" + System.lineSeparator();
            dot += "  node [fontname = \"Noto Sans\", shape = circle, fixedsize = shape, penwidth = 2.0, color = \"#444444\", style = \"filled\", fillcolor = \"#CCCCCC\"];" + System.lineSeparator();
            dot += "  edge [fontname = \"Noto Sans\", penwidth = 2.0, color = \"#444444\"];" + System.lineSeparator();
            dot += System.lineSeparator();
            for (Vertex[] v : edgeList) {
                dot += "  " + v[0].toString() + " -- " + v[1].toString() + System.lineSeparator();
            }
            dot += "}" + System.lineSeparator();

            try {
                out.write(dot.getBytes(Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String dot = "digraph G {" + System.lineSeparator();
            dot += "  overlap = false;" + System.lineSeparator();
            dot += "  bgcolor = transparent;" + System.lineSeparator();
            dot += "  splines = true;" + System.lineSeparator();
            for (Edge e : this.edges) {
                Vertex v = e.getSource();
                Vertex w = e.getTarget();
                dot += "  " + v.toString() + " -> " + w.toString() + System.lineSeparator();
            }
            dot += "}" + System.lineSeparator();

            try {
                out.write(dot.getBytes(Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("{name=%s, meta=%s}", name, meta);
    }
}