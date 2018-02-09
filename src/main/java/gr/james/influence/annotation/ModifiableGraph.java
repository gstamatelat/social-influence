package gr.james.influence.annotation;

import gr.james.influence.graph.Graph;

import java.lang.annotation.*;

/**
 * A constructor parameter of type {@link Graph} annotated {@code @ModifiableGraph} indicates that the graph can be
 * freely mutated after the constructor is invoked. Instance members will reflect changes to it.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ModifiableGraph {
}
