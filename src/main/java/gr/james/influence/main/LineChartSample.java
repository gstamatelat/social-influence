package gr.james.influence.main;

import gr.james.influence.IntegerVertexProvider;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Finals;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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

        // -------------------------------------------------------------
        DirectedGraph g = new RandomGenerator<Integer, Object>(10000, 0.1).generate(IntegerVertexProvider.provider);

        List<Double> h = new ArrayList<>();

        PageRank.execute(g, 0.15, 0.0);

        Finals.LOG.debug("List size: {}", h.size());

        for (int i = 0; i < h.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, h.get(i)));
        }
        // -------------------------------------------------------------

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }
}
