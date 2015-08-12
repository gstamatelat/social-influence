package gr.james.socialinfluence.main;

import gr.james.socialinfluence.algorithms.generators.BarabasiAlbertGenerator;
import gr.james.socialinfluence.algorithms.scoring.Degree;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.util.Finals;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class LineChartSample extends Application implements Runnable {
    public static String TITLE = "Degree Distribution";

    public static void main(String[] args) {
        Finals.LOG.info("From inside main()");
        Thread tmp = new Thread(new LineChartSample(), "JavaFX Application Wrapper Thread");
        tmp.start();
        Finals.LOG.info("From inside main() after tmp.start()");
    }

    @Override
    public void run() {
        Finals.LOG.info("From inside run()");
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        Finals.LOG.info("From inside start()");

        lineChart.setTitle(LineChartSample.TITLE);
        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("My portfolio");
        //populating the series with data

        /*Graph g = new WattsStrogatzGenerator<>(MemoryGraph.class, 10000, 1000, 1.0).create();*/
        Graph g = new BarabasiAlbertGenerator<>(MemoryGraph.class, 10000, 2, 2, 0.6).create();
        /* Find largest degree */
        int largestDegree = 0;
        GraphState<Integer> degrees = Degree.execute(g, true);
        for (int d : degrees.values()) {
            if (d > largestDegree) {
                largestDegree = d;
            }
        }
        Map<Integer, Integer> degreeDist = new HashMap<>();
        for (int d : degrees.values()) {
            if (degreeDist.containsKey(d)) {
                degreeDist.put(d, degreeDist.get(d) + 1);
            } else {
                degreeDist.put(d, 1);
            }
        }
        for (Integer x : degreeDist.keySet()) {
            series.getData().add(new XYChart.Data<>(x, degreeDist.get(x)));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }
}
