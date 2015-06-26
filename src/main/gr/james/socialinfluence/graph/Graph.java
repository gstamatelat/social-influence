package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.graph.algorithms.Dijkstra;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.Helper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.OutputStream;
import java.util.*;

/**
 * <p>Represents a collection of vertices and edges. The graph is weighted directed and there can't be more than one
 * edge from node {@code i} to node {@code j} (it's not a multigraph).</p><p><b>Remark on performance &ndash; </b>Some
 * methods have both single-vertex and multi-vertex variants, for example {@code getDegree(Vertex v)} and {@code
 * getDegree()}. You should prefer the latter when your algorithm needs all the degrees and avoid iterating through all
 * vertices and calling the single-vertex method each time. {@code for (Vertex v: g.getVertices()) { v.getInEdges(); }}
 * construct is to be avoided.</p><p><b>Collections returned &ndash; </b>Methods that return collections
 * ({@link Map Maps} or {@link Set Sets}) return read-only versions of them, meaning that you can't
 * insert or remove elements. These collections are also not backed by the graph, changes to the graph won't affect
 * these collections after they have been returned, you need to call the method again. The elements themselves,
 * however, are shallow copies and can be used to change the state of the graph.</p>
 */
public abstract class Graph {
    protected String name;
    protected String meta;

    public Graph() {
        this.name = Finals.DEFAULT_GRAPH_NAME;
        this.meta = "";
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
        return this.addVertex(v);
    }

    public abstract Vertex addVertex(Vertex v);

    public abstract boolean containsVertex(Vertex v);

    public Set<Vertex> addVertices(int count) {
        Set<Vertex> newVertices = new HashSet<>();
        for (int i = 0; i < count; i++) {
            newVertices.add(this.addVertex());
        }
        return Collections.unmodifiableSet(newVertices);
    }

    public <T extends Graph> Graph deepCopy(Class<T> type) {
        return deepCopy(type, this.getVertices());
    }

    public <T extends Graph> Graph deepCopy(Class<T> type, Set<Vertex> includeOnly) {
        Graph g = Helper.instantiateGeneric(type);
        for (Vertex v : includeOnly) {
            if (!this.containsVertex(v)) {
                throw new GraphException(Finals.E_GRAPH_VERTEX_NOT_CONTAINED, "deepCopy");
            }
            g.addVertex(v);
        }
        for (FullEdge e : this.getEdges()) {
            if (g.containsVertex(e.getSource()) && g.containsVertex(e.getTarget())) {
                g.addEdge(e.getSource(), e.getTarget()).setWeight(e.getEdge().getWeight());
            }
        }
        return g;
    }

    /**
     * <p>Removes a vertex from the graph. This method will also remove the inbound and outbound edges of that vertex.
     * </p>
     *
     * @param v the vertex to be removed
     * @return the current instance
     */
    public abstract Graph removeVertex(Vertex v);

    public Graph removeVertices(Collection<Vertex> vertices) {
        for (Vertex v : vertices) {
            this.removeVertex(v);
        }
        return this;
    }

    public abstract Graph clear();

    /*public abstract Vertex getVertexFromId(int id);*/

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
    public abstract Vertex getVertexFromIndex(int index);

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
            for (Map.Entry<Vertex, Edge> e : this.getOutEdges(y).entrySet()) {
                this.addEdge(v, e.getKey()).setWeight(e.getValue().getWeight());
            }
            for (Map.Entry<Vertex, Edge> e : this.getInEdges(y).entrySet()) {
                this.addEdge(e.getKey(), v).setWeight(e.getValue().getWeight());
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
        for (Vertex v : this.getVertices()) {
            if (this.getOutDegree(v) == 1 && this.getOutEdges(v).containsKey(v)) {
                stubborn.add(v);
            }
        }
        return Collections.unmodifiableSet(stubborn);
    }

    public Set<FullEdge> getEdges() {
        Set<FullEdge> edges = new HashSet<>();
        for (Vertex v : this.getVertices()) {
            for (Map.Entry<Vertex, Edge> e : this.getOutEdges(v).entrySet()) {
                edges.add(new FullEdge(v, e.getKey(), e.getValue()));
            }
        }
        return Collections.unmodifiableSet(edges);
    }

    /**
     * <p>Connects all the vertices in the graph. Does not create self-connections (loops).</p>
     * <p><b>Complexity:</b> O(n<sup>2</sup>)</p>
     *
     * @return the current instance
     */
    public Graph connectAllVertices() {
        for (Vertex v : this.getVertices()) {
            for (Vertex w : this.getVertices()) {
                if (!v.equals(w)) {
                    this.addEdge(v, w);
                }
            }
        }
        return this;
    }

    /*public abstract Set<Edge> getEdges();*/

    /**
     * <p>Calculates the total amount of directed edges that this graph has. This method is a little faster than using
     * {@code getEdges().size()}.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of directed edges in this graph
     */
    public int getEdgesCount() {
        throw new NotImplementedException();
    }

    public abstract Edge addEdge(Vertex source, Vertex target);

    public Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected) {
        Set<Edge> addedEdges = new HashSet<>();
        addedEdges.add(this.addEdge(source, target));
        if (undirected) {
            addedEdges.add(this.addEdge(target, source));
        }
        return Collections.unmodifiableSet(addedEdges);
    }

    /*public abstract Graph removeEdge(Edge e);*/

    public abstract Graph removeEdge(Vertex source, Vertex target);

    /*public abstract Map<Vertex, Set<Edge>> getOutEdges();*/

    /*public abstract Map<Vertex, Set<Edge>> getInEdges();*/

    public abstract Map<Vertex, Edge> getOutEdges(Vertex v);

    public abstract Map<Vertex, Edge> getInEdges(Vertex v);

    /**
     * <p>Returns the sum of the outbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all outbound edges of vertex {@code v}
     * @see #getInWeightSum(Vertex)
     */
    public double getOutWeightSum(Vertex v) {
        return Helper.getWeightSum(this.getOutEdges(v).values());
    }

    /**
     * <p>Returns the sum of the inbound edge weights of a vertex.</p>
     *
     * @param v the vertex
     * @return the sum of weights of all inbound edges of vertex {@code v}
     * @see #getOutWeightSum(Vertex)
     */
    public double getInWeightSum(Vertex v) {
        return Helper.getWeightSum(this.getInEdges(v).values());
    }

    /**
     * <p>Returns the outbound degree of a vertex, aka the number of outbound edges. Edge to self is included (if
     * present).</p>
     *
     * @param v the vertex
     * @return the outbound degree of vertex {@code v}
     * @see #getInDegree(Vertex)
     * @see #getInDegree()
     * @see #getOutDegree()
     */
    public int getOutDegree(Vertex v) {
        return this.getOutEdges(v).size();
    }

    /**
     * <p>Returns the inbound degree of a vertex, aka the number of inbound edges. Edge to self is included (if
     * present).</p>
     *
     * @param v the vertex
     * @return the inbound degree of vertex {@code v}
     * @see #getOutDegree(Vertex)
     * @see #getInDegree()
     * @see #getOutDegree()
     */
    public int getInDegree(Vertex v) {
        return this.getInEdges(v).size();
    }

    /*public abstract Map<Vertex, Integer> getInDegree();*/

    /*public abstract Map<Vertex, Integer> getOutDegree();*/

    /**
     * <p>Returns true if for every edge with source S and target T where S and T are different,
     * there is always an edge with source T and target S.</p>
     *
     * @return true if the graph is undirected, otherwise false
     */
    public abstract boolean isUndirected();

    public abstract Graph createCircle(boolean undirected);

    /**
     * <p>Returns an unmodifiable Set of vertices that this graph consists of.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the list of vertices of this graph
     */
    public abstract Set<Vertex> getVertices();

    /**
     * <p>Returns the number of vertices in this graph. This method is faster than using getVertices().size()</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of vertices in this graph
     */
    public int getVerticesCount() {
        return this.getVertices().size();
    }

    public abstract Vertex getRandomOutEdge(Vertex from, boolean weighted);

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

    /**
     * <p>Exports this graph in DOT format. If the graph is undirected, then the undirected DOT format will be used.
     * This method uses UTF-8 as character set when writing to the stream.</p>
     * <p><b>Running Time:</b> Slow - Very Slow (depends on the graph)</p>
     *
     * @param out the OutputStream to write the DOT file to
     * @return the current instance
     */
    public Graph exportToDot(OutputStream out) {
        /*if (this.isUndirected()) {
            ArrayList<Vertex[]> edgeList = new ArrayList<>();
            for (Edge e : this.getEdges()) {
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
            for (Edge e : this.getEdges()) {
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
        }*/
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return String.format("{type=%s, name=%s, meta=%s}", this.getClass().getSimpleName(), name, meta);
    }
}