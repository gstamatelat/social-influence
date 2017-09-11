package gr.james.influence;

import gr.james.influence.api.Graph;
import gr.james.influence.api.Metadata;
import gr.james.influence.graph.GraphDecorator;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class GraphDecoratorTest {
    @Test
    public void implementsGraph() {
        List<Method> graphMethods = Arrays.asList(Graph.class.getDeclaredMethods());
        List<Method> graphDecoratorMethods = Arrays.asList(GraphDecorator.class.getDeclaredMethods());
        for (Method m : graphMethods) {
            long count = graphDecoratorMethods.stream().filter(x ->
                    x.getName().equals(m.getName()) && Arrays.equals(x.getParameterTypes(), m.getParameterTypes())
            ).count();
            Assert.assertEquals("GraphDecoratorTest.implementsGraph", count, 1);
        }
    }

    @Test
    public void implementsMetadata() {
        List<Method> metadataMethods = Arrays.asList(Metadata.class.getDeclaredMethods());
        List<Method> graphDecoratorMethods = Arrays.asList(GraphDecorator.class.getDeclaredMethods());
        for (Method m : metadataMethods) {
            long count = graphDecoratorMethods.stream().filter(x ->
                    x.getName().equals(m.getName()) && Arrays.equals(x.getParameterTypes(), m.getParameterTypes())
            ).count();
            Assert.assertEquals("GraphDecoratorTest.implementsMetadata", count, 1);
        }
    }
}
