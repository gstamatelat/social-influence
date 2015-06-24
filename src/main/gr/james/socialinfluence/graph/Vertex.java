package gr.james.socialinfluence.graph;

import java.util.Set;

/**
 * <p>Represents a single vertex. An object of type Vertex behaves like an immutable object, contains an id, a label
 * and is bound to a specific Graph object at any given point in time which can be obtained with getParentPath. The
 * id field may not be changed, but the label can be. It is safe to use this class in a collection since equals and
 * hashCode methods only depend on id, which is fixed. Vertex objects returned by methods are always shallow copies.
 * There is no deep copy of a Vertex as that would not be practical for the operations that can be performed.</p>
 */
public class Vertex implements Comparable<Vertex> {

    /**
     * <p>This field holds a serial number needed by {@link #getNextId getNextId()}.</p>
     */
    private static int nextId = 1;
    String label;
    MemoryGraph parentGraph;
    private int id;

    /**
     * <p>Creates a new {@link Vertex} that doesn't belong to a graph. You must bind it to a graph using
     * {@link MemoryGraph#addVertex(Vertex)} or {@link #setParentGraph(MemoryGraph)} in order to be able to use it.</p>
     */
    Vertex() {
        this.id = Vertex.getNextId();
        this.label = String.valueOf(this.id);
        this.parentGraph = null;
    }

    /**
     * <p>Returns an integer id that is guaranteed to be unique for every framework session (execution). This method is
     * used by the constructor {@link #Vertex()} to produce a unique id for the new vertex.</p>
     *
     * @return the unique id
     */
    private static int getNextId() {
        return nextId++;
    }

    /**
     * <p>Gets the id of this vertex. Id's are integers.</p>
     * <p><b>Running Time:</b> Very Fast</p>
     *
     * @return the id of this vertex
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Returns the label of this vertex. Labels are strings that are used on printing and exporting.</p>
     * <p><b>Running Time:</b> Very Fast</p>
     *
     * @return the label of this vertex
     * @see #setLabel(String)
     */
    public String getLabel() {
        return label;
    }

    /**
     * <p>Sets the label of this vertex. Labels are used on printing and exporting.</p>
     * <p><b>Running Time:</b> Very Fast</p>
     *
     * @param label the new label of the vertex
     * @return the current instance
     * @see #getLabel()
     */
    public Vertex setLabel(String label) {
        this.label = label;
        return this;
    }

    public MemoryGraph getParentGraph() {
        return this.parentGraph;
    }

    public MemoryGraph setParentGraph(MemoryGraph g) {
        g.addVertex(this);
        return g;
    }

    /**
     * <p>Returns a {@code Set} with all the inbound edges of this vertex. The collection returned by this method is
     * not backed by this vertex or the graph and you may not modify it.</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return a {@code Set} with all the inbound edges of this vertex
     */
    public Set<Edge> getInEdges() {
        return this.parentGraph.getInEdges(this);
    }

    /**
     * <p>Returns a {@code Set} with all the outbound edges of this vertex. The collection returned by this method is
     * not backed by this vertex or the graph and you may not modify it.</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return a map with all the vertices that this vertex is pointing at
     */
    public Set<Edge> getOutEdges() {
        return this.parentGraph.getOutEdges(this);
    }

    /**
     * <p>Returns the inbound degree of this vertex, aka the number of inbound edges.
     * Edge from self is included (if present).</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return the inbound degree of this vertex
     * @see #getOutDegree()
     */
    public int getInDegree() {
        return this.parentGraph.getInDegree(this);
    }

    /**
     * <p>Returns the outbound degree of this vertex, aka the number of outbound edges.
     * Edge to self is included (if present).</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return the outbound degree of this vertex
     * @see #getInDegree()
     */
    public int getOutDegree() {
        return this.parentGraph.getOutDegree(this);
    }

    /**
     * <p>Returns the sum of the weights of outbound edges from this vertex.</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return the sum of weights of all outbound edges from this vertex
     * @see #getInWeightSum()
     */
    public double getOutWeightSum() {
        return this.parentGraph.getOutWeightSum(this);
    }

    /**
     * <p>Returns the sum of the weights of inbound edges to this vertex.</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return the sum of weights of all inbound edges to this vertex
     * @see #getOutWeightSum()
     */
    public double getInWeightSum() {
        return this.parentGraph.getInWeightSum(this);
    }

    public Edge addEdge(Vertex target) {
        return this.parentGraph.addEdge(this, target);
    }

    public Edge getRandomOutEdge(boolean weighted) {
        return this.parentGraph.getRandomOutEdge(this, weighted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return this.id == vertex.id;

    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.label;
    }

    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(this.id, o.id);
    }
}