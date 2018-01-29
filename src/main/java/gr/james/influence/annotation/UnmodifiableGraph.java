package gr.james.influence.annotation;

import gr.james.influence.graph.Graph;

import java.lang.annotation.*;

/**
 * A constructor parameter of type {@link Graph} annotated {@code @UnmodifiableGraph} indicates that instances of the
 * class expect that the graph will not be mutated after the constructor is invoked and will not reflect changes to it.
 * <p>
 * Often, this means that the constructor is generating state that relies upon the graph annotated with
 * {@code UnmodifiableGraph} and mutations to it may result in unexpected behavior when invoking instance members that
 * utilize that state. In these cases, reliable operation of the instance can be achieved by re-instantiation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UnmodifiableGraph {
}
