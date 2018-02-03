package gr.james.influence.annotation;

import gr.james.influence.graph.DirectedGraph;

import java.lang.annotation.*;

/**
 * A constructor parameter of type {@link DirectedGraph} annotated {@code @ModifiableGraph} indicates that the graph can be
 * freely mutated after the constructor is invoked. Instance members will reflect changes to it.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ModifiableGraph {
}
