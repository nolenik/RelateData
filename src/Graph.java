


import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author imssbora
 *
 */
public class Graph extends JFrame {

  private static final long serialVersionUID = 1L;



public Graph(String topR,String topT,List<NetFlow.Data> data) {
	
	// TODO Auto-generated constructor stub
	DefaultCategoryDataset dataset = createDataset( topR,topT, data);
    // Create chart
    JFreeChart chart = ChartFactory.createLineChart(
        "Line charts", // Chart title
        "Date", // X-Axis Label
        "Bytes / 10^4", // Y-Axis Label
        dataset
        );
    CategoryPlot p = chart.getCategoryPlot(); 
    CategoryAxis axis = p.getDomainAxis();
    Font font = new Font("Dialog", Font.PLAIN, 10);
    axis.setTickLabelFont(font);
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
}



private DefaultCategoryDataset createDataset(String topR,String topT, List<NetFlow.Data> data) {

    String series1 = "Top Receiver";
    String series2 = "Top Transmiter";

    
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    DateFormat dateFormat = new SimpleDateFormat("dd hh");
    for(int i=0; i<data.size();i+=7)
    {
        if(data.get(i).sourceAdress.equals(topR))
    	    dataset.addValue(data.get(i).bytesIn/10000, series1, dateFormat.format(data.get(i).date));
        if(data.get(i).sourceAdress.equals(topR))
    	    dataset.addValue(data.get(i).bytesOut/10000, series2, dateFormat.format(data.get(i).date));
    }	    


    return dataset;
  }


}
