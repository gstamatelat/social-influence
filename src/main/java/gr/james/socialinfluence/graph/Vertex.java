package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.util.Conditions;

/**
 * <p>Represents a single vertex. An object of type Vertex behaves like an immutable object, contains an id and a label.
 * The id field may not be changed, but the label can be. It is safe to use this class in a collection since
 * {@link #equals(Object) equals} and {@link #hashCode() hashCode} methods only depend on id, which is fixed. Vertex
 * objects returned by methods are always shallow copies (references).</p>
 * <p>Vertex id is automatically set to an auto-increment value each time you instantiate a new vertex. A vertex can
 * exist in multiple graphs, if that makes sense for the application.</p>
 */
public class Vertex implements Comparable<Vertex> {
    private static int nextId = 1;

    private String label;
    private int id;

    /**
     * <p>Creates a new {@link Vertex} that doesn't belong to a graph. You must bind it to a graph using
     * {@link Graph#addVertex(Vertex)}.</p>
     */
    public Vertex() {
        this.id = Vertex.getNextId();
        this.label = String.valueOf(this.id);
    }

    /**
     * <p>Returns an integer id that is guaranteed to be unique for every framework session. This method is used by the
     * constructor {@link #Vertex()} to produce a unique id for the new vertex.</p>
     *
     * @return the unique id
     */
    private static int getNextId() {
        return nextId++;
    }

    /**
     * <p>Gets the id of this vertex. Id's are integers.</p>
     *
     * @return the id of this vertex
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Returns the label of this vertex. Labels are strings that are used on printing, exporting etc.</p>
     *
     * @return the label of this vertex
     * @see #setLabel(String)
     */
    public String getLabel() {
        return label;
    }

    /**
     * <p>Sets the label of this vertex. Labels are strings that are used on printing, exporting etc.</p>
     *
     * @param label the new label of the vertex
     * @return the current instance
     * @throws NullPointerException if {@code label} is {@code null}
     * @see #getLabel()
     */
    public Vertex setLabel(String label) {
        this.label = Conditions.requireNonNull(label);
        return this;
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

    /**
     * <p>Returns a string representation of this vertex, which is its label.</p>
     *
     * @return a string representation of this vertex
     */
    @Override
    public String toString() {
        return this.label;
    }

    /**
     * <p>Compares this vertex with another vertex, using the underlying id as the comparator.</p>
     *
     * @param o the vertex to compare to
     * @return {@code -1}, {@code 0} or {@code 1} if this vertex has a smaller, equal or bigger id than vertex {@code o} respectively
     */
    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(this.id, o.id);
    }
}
