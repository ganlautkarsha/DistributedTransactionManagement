import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class PlotResults extends Application {
    private List mpl;
    private List throughput;

    public PlotResults() {
        this.mpl = new ArrayList<Integer>();
        this.throughput = new ArrayList<Integer>();

    }

    @Override
    public void start(Stage stage) throws Exception {
        // Replace this with actual values
        this.mpl.add(1);
        this.mpl.add(2);
        this.mpl.add(3);
        this.throughput.add(1);
        this.throughput.add(2);
        this.throughput.add(3);


        stage.setTitle("MPL vs Throughput");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        final LineChart lineChart =
                new LineChart(xAxis, yAxis);

        lineChart.setTitle("MPL vs Throughput");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("MPL vs Throughput");
        try {
            for (int i = 0; i < this.mpl.size(); i++) {
                series1.getData().add(new XYChart.Data(this.throughput.get(i), this.mpl.get(i)));

            }
            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().addAll(series1);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
