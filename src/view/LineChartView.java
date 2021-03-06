package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChartView extends JPanel {

	private static final long serialVersionUID = 1L;
	public DefaultCategoryDataset dataset;
	public ChartPanel panel;

	public LineChartView() {
		// Create dataset
		dataset = createDataset();
		// Create chart
		JFreeChart chart = ChartFactory.createLineChart(
				"", // Chart title
				"Tick", // X-Axis Label
				"Number of cars", // Y-Axis Label
				dataset
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis range = plot.getDomainAxis();
		range.setVisible(false);
		plot.getRenderer().setSeriesPaint(2, Color.orange);
		
		panel = new ChartPanel(chart);
	}

	private DefaultCategoryDataset createDataset() {
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		return dataset;
	}

}
