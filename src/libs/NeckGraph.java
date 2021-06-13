package libs;

import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class NeckGraph extends JPanel {
	
	Vector<String> date = null;
	Vector<Float> neck_avg = null;
	DefaultTableModel model = null;
	
	ChartPanel chartPanel = null;
	public NeckGraph(String title,DefaultTableModel model) {
		this.model = model;
		chartPanel = new ChartPanel(null);
		updateGraph();
		this.add(chartPanel);
	}
	
	public void updateGraph() {
		Vector<Vector> data = model.getDataVector();
		if(data.size() == 0) {
			return;
		}
		date= new Vector<String>();
		neck_avg = new Vector<Float>();
		for(Vector<String> v : data) {
			date.add(v.elementAt(0));
			neck_avg.add(Float.parseFloat(v.elementAt(1)));
		}
		
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset, "Neck Length by Time");
		chartPanel = new ChartPanel(chart);
	}
  
	private JFreeChart createChart(XYDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createXYLineChart(title,        
			"NeckLength Average", 
			"Activation", 
			dataset,                // 데이터
			PlotOrientation.HORIZONTAL, 
			false,                  
			false,
			false);

		XYPlot plot = (XYPlot) chart.getPlot();


		String[] datearr =  new String[date.size()];
		for(int i = 0;i<date.size();i++) {
			datearr[i] = date.get(i);
		}
		SymbolAxis rangeAxis = new SymbolAxis("시간", datearr);

		rangeAxis.setTickUnit(new NumberTickUnit(1));
		rangeAxis.setRange(0,datearr.length);
		plot.setRangeAxis(rangeAxis);

		return chart;
	}
  
  
	private  DefaultXYDataset createDataset() {
		DefaultXYDataset result = new DefaultXYDataset();
      
		XYSeries series = new XYSeries("");    
      
		for(int i = 0;i<neck_avg.size();i++) {
			series.add((double)neck_avg.get(i),i);   
		}
		result.addSeries("TITLE", series.toArray());

		return result;  
	}
}