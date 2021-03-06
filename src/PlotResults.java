import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PlotResults extends Application {
    private List mplMysql;
    private ArrayList<Double> workloadResponseMysql;
//    private ArrayList<Double> workloadResponseMysql;
    
    private List mpl_postgres;
    private ArrayList<Double> workloadResponsePostgres;
//    private ArrayList<Double> workloadResponsePostgres;

    public PlotResults() {
        this.mpl_postgres = new ArrayList<>(Arrays.asList(5, 10, 15, 20, 50, 80));
        this.workloadResponsePostgres = new ArrayList<>(Arrays.asList(0.01335897, 0.01066667, 0.04818055, 0.07329167, 0.08545206, 0.27143243));

        this.mplMysql = new ArrayList<>(Arrays.asList(5, 10, 15, 20, 50, 80));
        this.workloadResponseMysql = new ArrayList<>(Arrays.asList(0.00373333, 0.03038621, 0.04478358, 0.08046032, 0.07039838, 0.08693220));
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Replace this with actual values
    	ArrayList<String> isolation_levels = new ArrayList<>(Arrays.asList("TRANSACTION_READ_UNCOMMITTED","TRANSACTION_READ_COMMITTED","TRANSACTION_REPEATABLE_READ","TRANSACTION_SERIALIZABLE"));
        ArrayList<String> concurrency_levels = new ArrayList<>(Arrays.asList("LOW_CONCURRENCY", "HIGH_CONCURRENCY"));
    	String isolation = isolation_levels.get(3);
    	String concurrency = concurrency_levels.get(1);
        String title="Average Read Response Time vs MPL for " + concurrency + " and " + isolation;

        stage.setTitle(title);
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("MPL");
        yAxis.setLabel("Average Read Response Time");
        final LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setTitle(title);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("MySQL");
        try {
            for (int i = 0; i < this.mplMysql.size(); i++)
            {series1.getData().add(new XYChart.Data(this.mplMysql.get(i),this.workloadResponseMysql.get(i)));}
            
            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Postgres");
            try {
                for (int i = 0; i < this.mpl_postgres.size(); i++) {
                    series2.getData().add(new XYChart.Data(this.mpl_postgres.get(i),this.workloadResponsePostgres.get(i)));}

                
            Scene scene = new Scene(lineChart, 1600, 1200);
            lineChart.getData().addAll(series1);
            lineChart.getData().addAll(series2);
            
            stage.setScene(scene);
            stage.show();
            
            WritableImage snapShot = scene.snapshot(null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(title+".png"));

        }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        finally {
		}
        }

    public static void main(String[] args) {
        launch(args);
    }
}
