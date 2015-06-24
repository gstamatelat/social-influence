package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.helper.GraphException;

import java.io.OutputStream;
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
public interface Graph {
    /**
     * <p>Sets a new name for this graph. The name is used on printing and other exporting functionality.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @param name The new name for this graph
     * @return this instance
     */
    public Graph setName(String name);

    public String getMeta();

    public Graph setMeta(String meta);

    /**
     * <p>Inserts a new vertex to the graph and returns it. Use {@link #addVertices} for bulk inserts.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the new vertex object
     */
    public Vertex addVertex();

    public Vertex addVertex(Vertex v);

    public Set<Vertex> addVertices(int count);

    /**
     * <p>Removes a vertex from the graph. This method will also remove the inbound and outbound edges of that vertex.
     * </p>
     * <p><b>Running Time:</b> Fast</p>
     *
     * @param v the vertex to be removed
     * @return the current instance
     */
    public Graph removeVertex(Vertex v);

    public Vertex getVertexFromId(int id);

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
    public Vertex getVertexFromIndex(int index);

    /**
     * <p>Fuses two or more vertices into a single one. This method may cause information loss
     * if there are conflicts on the edges.</p>
     *
     * @param f an array of vertices to be fused
     * @return the vertex that is the result of the fusion
     */
    public Vertex fuseVertices(Vertex[] f);

    public Vertex getRandomVertex();

    public Set<Vertex> getStubbornVertices();

    /**
     * <p>Connects all the vertices in the graph. Does not create self-connections (loops).</p>
     * <p><b>Complexity:</b> O(n<sup>2</sup>)</p>
     *
     * @return the current instance
     */
    public Graph connectAllVertices();

    public Set<Edge> getEdges();

    /**
     * <p>Calculates the total amount of directed edges that this graph has. This method is a little faster than using
     * {@code getEdges().size()}.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of directed edges in this graph
     */
    public int getEdgesCount();

    public Edge addEdge(Edge e);

    public Edge addEdge(Vertex source, Vertex target);

    public Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected);

    public Graph removeEdge(Edge e);

    public Graph removeEdge(Vertex source, Vertex target);

    public Map<Vertex, Set<Edge>> getOutEdges();

    public Set<Edge> getOutEdges(Vertex v);

    public Map<Vertex, Set<Edge>> getInEdges();

    public Set<Edge> getInEdges(Vertex v);

    public double getOutWeightSum(Vertex v);

    public double getInWeightSum(Vertex v);

    public Map<Vertex, Integer> getOutDegree();

    public int getOutDegree(Vertex v);

    public Map<Vertex, Integer> getInDegree();

    public int getInDegree(Vertex v);

    /**
     * <p>Returns true if for every edge with source S and target T where S and T are different,
     * there is always an edge with source T and target S.</p>
     * <p><b>Running Time:</b> Slow - Very Slow (depends on the graph)</p>
     *
     * @return true if the graph is undirected, otherwise false
     */
    public boolean isUndirected();

    public Graph createCircle(boolean undirected);

    /**
     * <p>Returns an unmodifiable Set of vertices that this graph consists of.</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the list of vertices of this graph
     */
    public Set<Vertex> getVertices();

    /**
     * <p>Returns the number of vertices in this graph. This method is faster than using getVertices().size()</p>
     * <p><b>Complexity:</b> O(1)</p>
     *
     * @return the number of vertices in this graph
     */
    public int getVerticesCount();

    public Edge getRandomOutEdge(Vertex from, boolean weighted);

    public double getDiameter();

    /**
     * <p>Exports this graph in DOT format. If the graph is undirected, then the undirected DOT format will be used.
     * This method uses UTF-8 as character set when writing to the stream.</p>
     * <p><b>Running Time:</b> Slow - Very Slow (depends on the graph)</p>
     *
     * @param out the OutputStream to write the DOT file to
     * @return the current instance
     */
    public Graph exportToDot(OutputStream out);
}