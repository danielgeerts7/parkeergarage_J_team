package view;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart extends JPanel{
	public DefaultPieDataset dataset;
	public JFreeChart chart;
	public PiePlot plot;
	
    public PieChart() {
        super();
        dataset = new DefaultPieDataset();
        dataset.setValue("Paying cars", 0);
		dataset.setValue("Parking pass holders", 0);
		dataset.setValue("Free spots", 0);
		
        chart = ChartFactory.createPieChart("", dataset, false, false, false);
        plot = (PiePlot) chart.getPlot();
        
        plot.setForegroundAlpha(1);
        plot.setBackgroundImageAlpha(0);
        plot.setOutlineVisible(false);
        plot.setCircular(true);
        plot.setShadowYOffset(0);
        plot.setShadowXOffset(0);
        chart.setBorderPaint(Color.blue);
        
        plot.setSectionPaint("AdHoc", Color.red);
        plot.setSectionPaint("Parking pass holders", Color.blue);
        plot.setSectionPaint("Free spots", Color.gray);
        
        add(new ChartPanel(chart));
    }
}