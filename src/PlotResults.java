import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;


public class PlotResults extends Application {
    private List mpl;
    private ArrayList<Double> throughput;
    
    private List mpl_postgres;
    private ArrayList<Double> throughput_postgres;

    public PlotResults() {
        this.mpl = new ArrayList<>(Arrays.asList(5,10,15,20,50,80));
        this.throughput = new ArrayList<>(Arrays.asList(87.93,90.90,81.33,49.65,49.27,48.02));
        
        this.mpl_postgres = new ArrayList<>(Arrays.asList(5,10,20,50,80));
        this.throughput_postgres = new ArrayList<>(Arrays.asList(84.5,83.11,71.86,81.92,77.24));
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Replace this with actual values
    	ArrayList<String> isolation_levels= new ArrayList<>(Arrays.asList("TRANSACTION_READ_UNCOMMITTED","TRANSACTION_READ_COMMITTED","TRANSACTION_REPEATABLE_READ","TRANSACTION_SERIALIZABLE"));
    	String isolation=isolation_levels.get(0);
        String title="MPL vs Throughput :"+isolation;

        stage.setTitle(title);
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("MPL");
        yAxis.setLabel("Throughput");
        final LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setTitle(title);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("MySQL");
        try {
            for (int i = 0; i < this.mpl.size(); i++) 
            {series1.getData().add(new XYChart.Data(this.mpl.get(i),this.throughput.get(i)));}
            
            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Postgres");
            try {
                for (int i = 0; i < this.mpl_postgres.size(); i++) {
                    series2.getData().add(new XYChart.Data(this.mpl_postgres.get(i),this.throughput_postgres.get(i)));}

                
            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().addAll(series1);
            lineChart.getData().addAll(series2);
            
            stage.setScene(scene);
            stage.show();
            
            WritableImage snapShot = scene.snapshot(null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(isolation+".png"));

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
