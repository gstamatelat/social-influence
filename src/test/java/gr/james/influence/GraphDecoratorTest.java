package gr.james.influence;

import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.GraphDecorator;
import gr.james.influence.graph.Metadata;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class GraphDecoratorTest {
    @Test
    public void implementsGraph() {
        List<Method> graphMethods = Arrays.asList(DirectedGraph.class.getDeclaredMethods());
        List<Method> graphDecoratorMethods = Arrays.asList(GraphDecorator.class.getDeclaredMethods());
        for (Method m : graphMethods) {
            if (Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers())) {
                long count = graphDecoratorMethods.stream().filter(x ->
                        x.getName().equals(m.getName()) && Arrays.equals(x.getParameterTypes(), m.getParameterTypes())
                ).count();
                Assert.assertEquals("GraphDecoratorTest.implementsGraph", 1, count);
            }
        }
    }

    @Test
    public void implementsMetadata() {
        List<Method> metadataMethods = Arrays.asList(Metadata.class.getDeclaredMethods());
        List<Method> graphDecoratorMethods = Arrays.asList(GraphDecorator.class.getDeclaredMethods());
        for (Method m : metadataMethods) {
            if (Modifier.isPublic(m.getModifiers())) {
                long count = graphDecoratorMethods.stream().filter(x ->
                        x.getName().equals(m.getName()) && Arrays.equals(x.getParameterTypes(), m.getParameterTypes())
                ).count();
                Assert.assertEquals("GraphDecoratorTest.implementsMetadata", 1, count);
            }
        }
    }
}
