package gr.james.socialinfluence.graph;

import java.util.Set;

/**
 * <p>Represents a single vertex. An object of type Vertex behaves like an immutable object, contains an id, a label
 * and is bound to a specific Graph object at any given point in time which can be obtained with getParentPath. The
 * id field may not be changed, but the label can be. It is safe to use this class in a collection since equals and
 * hashCode methods only depend on id, which is constant. Vertex objects returned by methods are always shallow copies.
 * There is no deep copy of a Vertex as that would not be practical for the operations that can be performed.</p>
 */
public class Vertex implements Comparable {
    protected int id;
    protected String label;
    protected Graph parentGraph;

    Vertex(int id, Graph parentGraph) {
        this.id = id;
        this.label = String.valueOf(id);
        this.parentGraph = parentGraph;
    }

    /**
     * <p>Returns the id of this vertex. Id's are integers.</p>
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
     * @param label The new label of the vertex
     * @return the current instance
     * @see #getLabel()
     */
    public Vertex setLabel(String label) {
        this.label = label;
        return this;
    }

    public Graph getParentGraph() {
        return this.parentGraph;
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
     */
    public int getOutDegree() {
        return this.parentGraph.getOutDegree(this);
    }

    /**
     * <p>Returns the sum of the weights of outbound edges from this vertex.</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return the sum of weights of all outbound edges from this vertex
     */
    public double getOutWeightSum() {
        return this.parentGraph.getOutWeightSum(this);
    }

    /**
     * <p>Returns the sum of the weights of inbound edges to this vertex.</p>
     * <p><b>Running Time:</b> Slow</p>
     *
     * @return the sum of weights of all inbound edges to this vertex
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
    public int compareTo(Object o) {
        Vertex rhs = (Vertex) o;
        return Integer.compare(this.id, rhs.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Vertex))
            return false;

        Vertex rhs = (Vertex) obj;

        return this.id == rhs.id;
    }

    @Override
    public String toString() {
        return label;
    }
}