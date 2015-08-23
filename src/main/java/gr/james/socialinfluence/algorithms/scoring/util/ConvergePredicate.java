package gr.james.socialinfluence.algorithms.scoring.util;

public interface ConvergePredicate<T> {
    boolean converges(T t1, T t2, double epsilon);
}
