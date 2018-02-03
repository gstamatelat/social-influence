package gr.james.influence;

public class GraphDecoratorTest {
    /*@Test
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
    }*/
}
