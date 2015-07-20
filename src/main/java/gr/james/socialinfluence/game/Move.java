package gr.james.socialinfluence.game;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Move implements Iterable<MovePoint> {
    private Set<MovePoint> moveObject;

    public Move() {
        this.moveObject = new TreeSet<>();
    }

    public Move(Vertex... args) {
        this();
        for (Vertex v : args) {
            this.putVertex(v, 1.0);
        }
    }

    public int getVerticesCount() {
        return this.moveObject.size();
    }

    public double getWeightSum() {
        double sum = 0.0;
        for (MovePoint e : this.moveObject) {
            sum += e.weight;
        }
        return sum;
    }

    /**
     * <p>Adds or replaces a specific move point to this move object.</p>
     * <p><b>Running Time:</b> ???</p>
     *
     * @param v      the vertex of this move point, can't be null
     * @param weight the weight of this move point
     * @return the current instance
     * @throws GraphException if {@code weight} input is non-positive
     */
    public Move putVertex(Vertex v, double weight) {
        if (weight <= 0) {
            throw new GraphException(Finals.E_MOVE_WEIGHT_NEGATIVE, weight);
        }

        boolean exists = false;
        for (MovePoint e : this.moveObject) {
            if (v.equals(e.vertex)) {
                e.weight = weight;
                exists = true;
            }
        }

        if (!exists) {
            MovePoint mv = new MovePoint();
            mv.vertex = v;
            mv.weight = weight;
            this.moveObject.add(mv);
        }

        return this;
    }

    public boolean containsVertex(Vertex v) {
        for (MovePoint e : this.moveObject) {
            if (v.equals(e.vertex)) {
                return true;
            }
        }
        return false;
    }

    public Move removeVertex(Vertex v) {
        MovePoint f = null;
        for (MovePoint e : this.moveObject) {
            if (v.equals(e.vertex)) {
                f = e;
            }
        }

        if (f != null) {
            this.moveObject.remove(f);
        }

        return this;
    }

    public Move deepCopy() {
        Move newMove = new Move();
        for (MovePoint e : this) {
            newMove.putVertex(e.vertex, e.weight);
        }
        return newMove;
    }

    public Move clear() {
        this.moveObject.clear();
        return this;
    }

    public Move normalizeWeights(double sum) {
        double sumBefore = this.getWeightSum();
        for (MovePoint e : this.moveObject) {
            e.weight = (e.weight * sum) / sumBefore;
        }
        return this;
    }

    public Move sliceMove(int amount) {
        while (this.moveObject.size() > amount) {
            Iterator it = this.moveObject.iterator();
            it.next();
            it.remove();
        }
        return this;
    }

    @Override
    public Iterator<MovePoint> iterator() {
        return this.moveObject.iterator();
    }

    @Override
    public String toString() {
        if (this.moveObject.isEmpty()) {
            return "[]";
        }
        String outStr = "[";
        for (MovePoint t : this.moveObject) {
            outStr += t.vertex.toString();
            outStr += "=";
            outStr += Math.round(100d * t.weight) / 100d;
            outStr += ", ";
        }
        outStr = outStr.substring(0, outStr.length() - 2);
        outStr += "]";
        return outStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Move other = (Move) obj;

        return this.moveObject.equals(other.moveObject);
    }

    @Override
    public int hashCode() {
        return this.moveObject.hashCode();
    }
}
