package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
    public abstract Vertex addVertex();

    public abstract Vertex addVertex(Vertex v);

    public abstract Set<Vertex> addVertices(int count);

    /**
     * <p>Removes a vertex from the graph. This method will also remove the inbound and outbound edges of that vertex.
     * </p>
     * <p><b>Running Time:</b> Fast</p>
     *
     * @param v the vertex to be removed
     * @return the current instance
     */
    public abstract Graph removeVertex(Vertex v);

    public abstract Vertex getVertexFromId(int id);

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
    public abstract Vertex fuseVertices(Vertex[] f);

    public abstract Vertex getRandomVertex();

    public abstract Set<Vertex> getStubbornVertices();

    /**
     * <p>Connects all the vertices in the graph. Does not create self-connections (loops).</p>
     * <p><b>Complexity:</b> O(n<sup>2</sup>)</p>
     *
     * @return the current instance
     */
    public abstract Graph connectAllVertices();

    public abstract Set<Edge> getEdges();

    /**
     * <p>Calculates the total amount of directed edges that this graph has. This method is a little faster than using
     * {@code getEdges().size()}.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of directed edges in this graph
     */
    public abstract int getEdgesCount();

    public abstract Edge addEdge(Edge e);

    public abstract Edge addEdge(Vertex source, Vertex target);

    public abstract Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected);

    public abstract Graph removeEdge(Edge e);

    public abstract Graph removeEdge(Vertex source, Vertex target);

    public abstract Map<Vertex, Set<Edge>> getOutEdges();

    public abstract Set<Edge> getOutEdges(Vertex v);

    public abstract Map<Vertex, Set<Edge>> getInEdges();

    public abstract Set<Edge> getInEdges(Vertex v);

    public abstract double getOutWeightSum(Vertex v);

    public abstract double getInWeightSum(Vertex v);

    public abstract Map<Vertex, Integer> getOutDegree();

    public abstract int getOutDegree(Vertex v);

    public abstract Map<Vertex, Integer> getInDegree();

    public abstract int getInDegree(Vertex v);

    /**
     * <p>Returns true if for every edge with source S and target T where S and T are different,
     * there is always an edge with source T and target S.</p>
     * <p><b>Running Time:</b> Slow - Very Slow (depends on the graph)</p>
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
    public abstract int getVerticesCount();

    public abstract Edge getRandomOutEdge(Vertex from, boolean weighted);

    public abstract double getDiameter();

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
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("{type=%s, name=%s, meta=%s}", this.getClass().getSimpleName(), name, meta);
    }
}