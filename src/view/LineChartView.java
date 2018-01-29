package view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChartView extends JPanel {

	private static final long serialVersionUID = 1L;
	public ChartPanel panel;

	  public LineChartView() {
	    // Create dataset
	    DefaultCategoryDataset dataset = createDataset();
	    // Create chart
	    JFreeChart chart = ChartFactory.createLineChart(
	        "Site Traffic (WWW.BORAJI.COM)", // Chart title
	        "Date", // X-Axis Label
	        "Number of Visitor", // Y-Axis Label
	        dataset
	        );

	    panel = new ChartPanel(chart);
	  }

	  private DefaultCategoryDataset createDataset() {

	    String series1 = "Vistor";
	    String series2 = "Unique Visitor";

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    dataset.addValue(200, series1, "2016-12-19");
	    dataset.addValue(150, series1, "2016-12-20");
	    dataset.addValue(100, series1, "2016-12-21");
	    dataset.addValue(210, series1, "2016-12-22");
	    dataset.addValue(240, series1, "2016-12-23");
	    dataset.addValue(195, series1, "2016-12-24");
	    dataset.addValue(245, series1, "2016-12-25");

	    dataset.addValue(150, series2, "2016-12-19");
	    dataset.addValue(130, series2, "2016-12-20");
	    dataset.addValue(95, series2, "2016-12-21");
	    dataset.addValue(195, series2, "2016-12-22");
	    dataset.addValue(200, series2, "2016-12-23");
	    dataset.addValue(180, series2, "2016-12-24");
	    dataset.addValue(230, series2, "2016-12-25");

	    return dataset;
	  }
}
