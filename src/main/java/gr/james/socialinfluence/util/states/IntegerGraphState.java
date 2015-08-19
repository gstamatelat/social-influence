package gr.james.socialinfluence.util.states;

import gr.james.socialinfluence.api.AbstractGraphState;

import java.util.stream.Collectors;

public class IntegerGraphState extends AbstractGraphState<Integer> {
    public IntegerGraphState() {
    }

    @Override
    protected Integer add(Integer x1, Integer x2) {
        return x1 + x2;
    }

    @Override
    protected double add(Integer x1, double x2) {
        return x1 + x2;
    }

    @Override
    protected Integer subtract(Integer x1, Integer x2) {
        return x1 - x2;
    }

    @Override
    protected boolean greaterThan(Integer x1, Integer x2) {
        return x1 > x2;
    }

    @Override
    protected Integer abs(Integer x) {
        return Math.abs(x);
    }

    @Override
    public String toString() {
        return "{" + this.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(i -> String.format("%s=%d", i.getKey(), i.getValue())).collect(Collectors.joining(", ")) + "}";
    }
}
