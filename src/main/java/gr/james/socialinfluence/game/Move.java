package gr.james.socialinfluence.game;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.exceptions.WeightNonPositiveException;

import java.util.*;
import java.util.stream.Collectors;

public class Move implements Iterable<Vertex> {
    private Map<Vertex, Double> m;

    public Move() {
        this.m = new HashMap<>();
    }

    public Move(Vertex... args) {
        this();
        for (Vertex v : args) {
            this.putVertex(v, 1.0);
        }
    }

    public int getVerticesCount() {
        return this.m.size();
    }

    public double getWeightSum() {
        double sum = 0.0;
        for (double e : this.m.values()) {
            sum += e;
        }
        return sum;
    }

    public double getWeight(Vertex v) {
        return this.m.get(v);
    }

    /**
     * <p>Get an unmodifiable view of the vertices contained in this move. Weights are ignored in this operation.</p>
     *
     * @return an unmodifiable {@code Set} of vertices contained in this move
     */
    public Set<Vertex> vertexSet() {
        return Collections.unmodifiableSet(this.m.keySet());
    }

    /**
     * <p>Adds or replaces a specific move point to this move object.</p>
     *
     * @param v      the vertex of this move point, can't be null
     * @param weight the weight of this move point
     * @return the current instance
     * @throws NullPointerException       if {@code v} is {@code null}
     * @throws WeightNonPositiveException if {@code weight} input is non-positive
     */
    public Move putVertex(Vertex v, double weight) {
        Conditions.requireNonNull(v);
        if (weight <= 0) {
            throw new WeightNonPositiveException(Finals.E_MOVE_WEIGHT_NEGATIVE, weight);
        }

        this.m.put(v, weight);

        return this;
    }

    public boolean containsVertex(Vertex v) {
        return this.m.containsKey(v);
    }

    public Move removeVertex(Vertex v) {
        this.m.remove(v);
        return this;
    }

    public Move deepCopy() {
        Move newMove = new Move();
        for (Vertex v : this.m.keySet()) {
            newMove.putVertex(v, this.m.get(v));
        }
        return newMove;
    }

    public Move clear() {
        this.m.clear();
        return this;
    }

    public Move normalizeWeights(double sum) {
        double sumBefore = this.getWeightSum();
        for (Vertex v : this.m.keySet()) {
            this.m.put(v, this.m.get(v) * sum / sumBefore);
        }
        return this;
    }

    public Move sliceMove(int amount) {
        while (this.m.size() > amount) {
            List<Vertex> list = new ArrayList<>(this.m.keySet());
            this.m.remove(list.get(list.size() - 1));
        }
        return this;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this.m.keySet().iterator();
    }

    @Override
    public String toString() {
        return "{" + this.m.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(i -> String.format("%d [%.2f]", i.getKey().getId(), i.getValue()))
                .collect(Collectors.joining(", ")) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move vertexes = (Move) o;

        return m.equals(vertexes.m);
    }

    @Override
    public int hashCode() {
        return m.hashCode();
    }
}
